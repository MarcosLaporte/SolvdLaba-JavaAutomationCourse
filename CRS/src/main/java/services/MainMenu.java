package services;

import services.database.GenericDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import services.connection.ConnectionManager;
import services.database.EntityReflection;
import services.xml.XMLService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MainMenu {
    private enum Menu {
        EXIT, GET, CREATE, UPDATE, DELETE;

        private final int value;

        Menu() {
            this.value = this.ordinal();
        }

        public static String printMenu() {
            StringBuilder sb = new StringBuilder();
            for (Menu option : Menu.values()) {
                String optionStr = StringUtils.replace(String.valueOf(option), "_", " ");
                sb.append(String.format("%d. %s\n", option.value, optionStr));
            }

            return sb.toString();
        }
    }

    public static void runDatabase() {
        int mainMenuOption;
        do {
            mainMenuOption = InputService.readInt(
                    "\n" + Menu.printMenu() + "Choose an option: ",
                    "This option does not exist. Try again: ",
                    0, Menu.values().length - 1
            );

            if (mainMenuOption == 0) {
                System.out.println("Exiting...");
                break;
            }

            Class<?> entityClass = EntityReflection.chooseEntity();
            if (entityClass == null) {
                System.out.println("Going back...");
                continue;
            }

            System.out.println('\n' + entityClass.getSimpleName() + " selected.");
            try (GenericDAO<Object> dao = GenericDAO.castDAO(new GenericDAO<>(entityClass))) {
//                ReflectionService<Object> rs = GenericDAO.castReflectionService(new ReflectionService<>(entityClass));
//                ReflectionService<?> rs = new ReflectionService<>(entityClass);
                EntityReflection<?> rs = new EntityReflection<>(entityClass);

//            try (GenericDAO<?> dao = new GenericDAO<>(entityClass)) {
                switch (Menu.values()[mainMenuOption]) {
                    case GET -> {
                        System.out.print("\nFill with fields to filter by.");
                        Map<String, Object> columnFilters = rs.readConditionValues();
                        List<?> values = dao.get(columnFilters);

                        System.out.print("Rows found with values");
                        for (Map.Entry<String, Object> entry : columnFilters.entrySet())
                            System.out.print(" [" + entry.getKey() + " = " + entry.getValue() + ']');
                        System.out.println(": ");
                        System.out.println(ReflectionService.toString(values));
                    }
                    case CREATE -> {
                        if (dao.create(rs.readNewInstance()) > 0)
                            System.out.println(entityClass.getSimpleName() + " created!");
                        else
                            System.out.println("No " + entityClass.getSimpleName() + " was created.");
                    }
                    case UPDATE -> {
                        System.out.print("\nEnter the new values.");
                        Map<String, Object> newValues = rs.readNewValues();

                        System.out.print("\nEnter the conditions to follow.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        int rowsAffected = dao.update(newValues, columnFilters);
                        System.out.printf("%d %s updated.\n", rowsAffected, entityClass.getSimpleName());
                    }
                    case DELETE -> {
                        System.out.print("\nEnter the conditions to follow.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        int rowsAffected = dao.delete(columnFilters);
                        System.out.printf("%d %s deleted.\n", rowsAffected, entityClass.getSimpleName());
                    }
                    default -> System.out.println("This option does not exist.");
                }
            } catch (Exception e) {
                LoggerService.log(Level.ERROR, e.getMessage());

                try {
                    ConnectionManager.closePool();
                } catch (SQLException ex) {
                    LoggerService.log(Level.ERROR, ex.getMessage());
                }
            }

        } while (true);

        try {
            ConnectionManager.closePool();
        } catch (SQLException ex) {
            LoggerService.log(Level.ERROR, ex.getMessage());
        }
    }

    public static void runXml() {
        XMLService.jaxbParse();
    }

    public static void printActiveThreads() {
        int threadCount = Thread.activeCount();
        Thread[] activeThreads = new Thread[threadCount];
        Thread.enumerate(activeThreads);

        System.out.println("Active Threads:");
        for (Thread thread : activeThreads) {
            if (thread != null) {
                System.out.println("Thread Name: " + thread.getName() +
                        ", State: " + thread.getState());
            }
        }
    }
}
