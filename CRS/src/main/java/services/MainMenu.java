package services;

import entities.Entity;
import org.apache.logging.log4j.Level;
import services.database.EntityReflection;
import services.database.IDao;
import services.database.MyBatis;
import services.xml.XMLService;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainMenu {
    private enum DaoMenu {
        GET, CREATE, UPDATE, DELETE;

        public static DaoMenu readAction() {
            int menuIndex = InputService.selectIndexFromList(
                    "Select an action to perform: ",
                    Arrays.stream(DaoMenu.values()).map(DaoMenu::toString).toList(),
                    "CANCEL"
            );

            return menuIndex != -1 ? DaoMenu.values()[menuIndex] : null;
        }
    }

    private enum DatasourceMenu {
        DATABASE, XML, JSON;

        public static DatasourceMenu readDatasource() {
            int index = InputService.selectIndexFromList(
                    Arrays.stream(DatasourceMenu.values()).map(DatasourceMenu::toString).toList(),
                    null
            );

            return DatasourceMenu.values()[index];
        }
    }

    public static <T extends Entity> void start() {
        while (true) {
            DaoMenu daoAction = DaoMenu.readAction();

            if (daoAction == null) {
                LoggerService.println("Exiting...");
                break;
            }

            @SuppressWarnings("unchecked")
            Class<T> entityClass = (Class<T>) EntityReflection.chooseEntity();
            if (entityClass == null) {
                LoggerService.println("Going back...");
                continue;
            }

            DatasourceMenu datasource = DatasourceMenu.readDatasource();
            IDao<T> dao = null;
            try {
                dao = switch (datasource) {
                    case XML -> new XMLService<>(entityClass);
                    case DATABASE -> new MyBatis<>(entityClass);
                    case JSON -> new JsonService<>(entityClass);
                };

                EntityReflection<T> rs = new EntityReflection<>(entityClass);
                switch (daoAction) {
                    case GET -> {
                        LoggerService.print("\nFill with fields to filter by.");
                        Map<String, Object> columnFilters = rs.readConditionValues();
                        List<T> values = dao.get(columnFilters);

                        if (!columnFilters.isEmpty()){
                            LoggerService.print("Rows found with values");
                            for (Map.Entry<String, Object> entry : columnFilters.entrySet())
                                LoggerService.print(" [" + entry.getKey() + " = " + entry.getValue() + ']');
                            LoggerService.println(": ");
                        }

                        LoggerService.println(ReflectionService.toString(values));
                    }
                    case CREATE -> {
                        if (dao.create(rs.readNewInstance(datasource != DatasourceMenu.DATABASE)) > 0)
                            LoggerService.println(entityClass.getSimpleName() + " created!");
                        else
                            LoggerService.println("No " + entityClass.getSimpleName() + " was created.");
                    }
                    case UPDATE -> {
                        LoggerService.print("\nEnter the new values.");
                        Map<String, Object> newValues = rs.readNewValues();

                        LoggerService.print("\nEnter the conditions to follow.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        int rowsAffected = dao.update(newValues, columnFilters);
                        LoggerService.println(String.format("%d %s updated.", rowsAffected, entityClass.getSimpleName()));
                    }
                    case DELETE -> {
                        LoggerService.print("\nEnter the conditions to follow.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        int rowsAffected = dao.delete(columnFilters);
                        LoggerService.println(String.format("%d %s deleted.", rowsAffected, entityClass.getSimpleName()));
                    }
                }
            } catch (Exception e) {
                LoggerService.log(Level.ERROR, e.getMessage() != null ? e.getMessage() : e.getClass().toString());
            } finally {
                try {
                    if (dao != null && Closeable.class.isAssignableFrom(dao.getClass()))
                            ((Closeable) dao).close();
                } catch (IOException e) {
                    LoggerService.log(Level.ERROR, e.getMessage());
                }
            }
        }
    }

}