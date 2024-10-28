package services;

import entities.Entity;
import org.apache.logging.log4j.Level;
import services.dao.JsonService;
import services.dao.IDao;
import services.dao.MyBatis;
import services.dao.XMLService;
import utils.InputService;
import utils.LoggerService;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainMenu {
    private enum CRUD {
        GET, CREATE, UPDATE, DELETE;

        public static CRUD readAction() {
            int menuIndex = InputService.selectIndexFromList(
                    "Select an action to perform: ",
                    Arrays.stream(CRUD.values()).map(CRUD::toString).toList(),
                    "EXIT"
            );

            return menuIndex != -1 ? CRUD.values()[menuIndex] : null;
        }
    }

    public enum Datasource {
        DATABASE, XML, JSON;

        public static Datasource readDatasource() {
            int index = InputService.selectIndexFromList(
                    "Select a datasource: ",
                    Arrays.stream(Datasource.values()).map(Datasource::toString).toList(),
                    null
            );

            return Datasource.values()[index];
        }
    }

    public static <T extends Entity> void start(Datasource datasource) {
        while (true) {
            CRUD operation = CRUD.readAction();

            if (operation == null) {
                LoggerService.println("Exiting...");
                break;
            }

            @SuppressWarnings("unchecked")
            Class<T> entityClass = (Class<T>) EntityReflection.chooseEntity();
            if (entityClass == null) {
                LoggerService.println("Going back...");
                continue;
            }

            IDao<T> dao = null;
            try {
                dao = switch (datasource) {
                    case XML -> new XMLService<>(entityClass);
                    case DATABASE -> new MyBatis<>(entityClass);
                    case JSON -> new JsonService<>(entityClass);
                };

                EntityReflection<T> rs = new EntityReflection<>(entityClass);
                switch (operation) {
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
                        if (dao.create(rs.readNewInstance(datasource != Datasource.DATABASE)) > 0)
                            LoggerService.println(entityClass.getSimpleName() + " created!");
                        else
                            LoggerService.println("No " + entityClass.getSimpleName() + " was created.");
                    }
                    case UPDATE -> {
                        LoggerService.print("\nEnter the new values.");
                        Map<String, Object> newValues = rs.readNewValues(datasource != Datasource.DATABASE);

                        if (newValues.isEmpty()) {
                            LoggerService.println("No new values entered. Going back.");
                            break;
                        }

                        LoggerService.print("\nEnter the conditions to follow.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        int rowsAffected = dao.update(newValues, columnFilters);
                        LoggerService.println(String.format("%d %s updated.", rowsAffected, entityClass.getSimpleName()));
                    }
                    case DELETE -> {
                        LoggerService.print("\nEnter the conditions to follow.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        if (columnFilters.isEmpty()) {
                            LoggerService.consoleLog(Level.WARN, "No filters selected. This will delete the entire table.");
                            char confirmContinue = InputService.readCharInValues("Continue? Y/N: ", "ERROR. Input Y or N: ", new char[]{'Y', 'N'});
                            if (confirmContinue == 'N'){
                                LoggerService.println("Operation cancelled.");
                                break;
                            }
                        }

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