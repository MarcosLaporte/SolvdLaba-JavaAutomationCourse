package services;

import entitiesDAO.GenericDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import services.connection.ConnectionManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static services.ReflectionService.ClassExclusionPredicate.ABSTRACT;
import static services.ReflectionService.ClassExclusionPredicate.ANNOTATION;

public class MainMenu {
    private enum Menu {
        EXIT, GET_ALL, GET_BY_ID, GET_FIELD_MATCHES, CREATE, UPDATE, DELETE;

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

    public static void run() {
        int mainMenuOption;
        do {
            mainMenuOption = InputService.readInt(
                    "\n" + Menu.printMenu() + "Choose an option: ",
                    "This option does not exist. Try again: ",
                    0, Menu.values().length - 1
            );

            if (mainMenuOption != 0) {
                Class<?> entityClass = chooseEntity();
                try (GenericDAO<Object> dao = GenericDAO.castDAO(new GenericDAO<>(entityClass))) {
                    switch (Menu.values()[mainMenuOption]) {
                        case EXIT -> {
                        }
                        case GET_ALL -> {
                            printAllValues(dao.getAll());
                        }
                        case GET_BY_ID -> {
                            int id = InputService.readInt(
                                    "Enter " + entityClass.getSimpleName() + " ID: ",
                                    "Value out of bounds. Try again: ",
                                    0, 999_999_999
                            );
                            System.out.println(dao.get(id));
                        }
                        case GET_FIELD_MATCHES -> {
                            System.out.println("Fill with fields ");
                            ReflectionService<Object> rs = GenericDAO.castReflectionService(new ReflectionService<>(entityClass));
                            List<?> values = dao.getAllMatches(rs.readValues());
                            printAllValues(values);
                        }
                        case CREATE -> {
                            ReflectionService<Object> rs = GenericDAO.castReflectionService(new ReflectionService<>(entityClass));
                            long newRow = dao.create(rs.readNewInstance());
                            System.out.println(dao.getRow(newRow));
                        }
                        case UPDATE -> {
                            ReflectionService<Object> rs = GenericDAO.castReflectionService(new ReflectionService<>(entityClass));
                            printAllValues(dao.getAll());
                            int id = InputService.readInt(
                                    "Enter " + entityClass.getSimpleName() + " ID: ",
                                    "Value out of bounds. Try again: ",
                                    0, 999_999_999
                            );

                            Object entity = dao.get(id);
                            if (entity != null) {
                                Object[] args = rs.readNewValues(entity);
                                dao.update(id, args);
                                System.out.println(dao.getRow(id));
                            }
                        }
                        case DELETE -> {
                            printAllValues(dao.getAll());
                            int id = InputService.readInt(
                                    "Enter " + entityClass.getSimpleName() + " ID: ",
                                    "Value out of bounds. Try again: ",
                                    0, 999_999_999
                            );
                            dao.delete(id);
                            printAllValues(dao.getAll());
                        }
                        default -> System.out.println("This option does not exist.");
                    }
                } catch (Exception e) {
                    try {
                        ConnectionManager.closePool();
                    } catch (SQLException ex) {
                        LoggerService.log(Level.ERROR, ex.getMessage());
                    }

                    throw new RuntimeException(e);
                }
            }

        } while (mainMenuOption != 0);

        try {
            ConnectionManager.closePool();
        } catch (SQLException ex) {
            LoggerService.log(Level.ERROR, ex.getMessage());
        }
    }

    private static Class<?> chooseEntity() {
        Map<String, Class<?>> classMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (Class<?> clazz : ReflectionService.getClassesInPackage("entities", ABSTRACT, ANNOTATION)) {
            String className = clazz.getSimpleName();
            sb.append(" - ").append(className).append('\n');
            classMap.put(className.toLowerCase(), clazz);
        }

        sb.append("Enter entity name: ");
        Class<?> entityClass;
        do {
            String chosenEnt = InputService.readString(sb.toString(), 1, 255);
            entityClass = classMap.get(chosenEnt.toLowerCase());
        } while (entityClass == null);

        return entityClass;
    }

    private static void printAllValues(List<?> values) {
        for (Object o : values) {
            System.out.println(o);
        }
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
