package services;

import entities.*;
import entities.lists.*;
import jakarta.xml.bind.JAXBException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import services.connection.ConnectionManager;
import services.database.EntityReflection;
import services.database.MyBatis;
import services.xml.XMLService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MainMenu {
    private enum DaoMenu {
        EXIT, GET, CREATE, UPDATE, DELETE;

        private final int value;

        DaoMenu() {
            this.value = this.ordinal();
        }

        public static String printMenu() {
            StringBuilder sb = new StringBuilder();
            for (DaoMenu option : DaoMenu.values()) {
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
                    "\n" + DaoMenu.printMenu() + "Choose an option: ",
                    "This option does not exist. Try again: ",
                    0, DaoMenu.values().length - 1
            );

            if (mainMenuOption == 0) {
                LoggerService.print("Exiting...");
                break;
            }

            Class<Object> entityClass = EntityReflection.chooseEntity();
            if (entityClass == null) {
                LoggerService.print("Going back...");
                continue;
            }

            LoggerService.print('\n' + entityClass.getSimpleName() + " selected.");
            try {
                EntityReflection<Object> rs = new EntityReflection<>(entityClass);
                MyBatis<Object> dao = new MyBatis<>(entityClass);
                switch (DaoMenu.values()[mainMenuOption]) {
                    case GET -> {
                        System.out.print("\nFill with fields to filter by.");
                        Map<String, Object> columnFilters = rs.readConditionValues();
                        List<?> values = dao.get(columnFilters);

                        System.out.print("Rows found with values");
                        for (Map.Entry<String, Object> entry : columnFilters.entrySet())
                            System.out.print(" [" + entry.getKey() + " = " + entry.getValue() + ']');
                        LoggerService.print(": ");
                        LoggerService.print(ReflectionService.toString(values));
                    }
                    case CREATE -> {
                        if (dao.create(rs.readNewInstance()) > 0)
                            LoggerService.print(entityClass.getSimpleName() + " created!");
                        else
                            LoggerService.print("No " + entityClass.getSimpleName() + " was created.");
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
                    default -> LoggerService.print("This option does not exist.");
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
        main:
        do {
            char mainMenuOption = InputService.readCharInValues(
                    "0. Exit\n1. Read XML files\n2. Write XML from Database\nChoose an option: ",
                    "This option does not exist. Try again: ",
                    new char[]{'0', '1', '2'}
            );

            switch (mainMenuOption) {
                case '0':
                    break main;
                case '1':
                    char parseOption = InputService.readCharInValues(
                            "1. StAX\n2. JAXB\nChoose an option: ",
                            "This option does not exist. Try again: ",
                            new char[]{'1', '2'}
                    );
                    if (parseOption == '1')
                        XMLService.staxParse();
                    else
                        XMLService.jaxbParse();

                    break;
                case '2':
                    Class<Object> entityClass = EntityReflection.chooseEntity();
                    if (entityClass == null) continue;
                    EntityReflection<Object> rs = new EntityReflection<>(entityClass);

                    try {
                        MyBatis<Object> dao = new MyBatis<>(entityClass);
                        System.out.print("\nFill with fields to filter by.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        Object list = getListClassInstance(
                                entityClass,
                                dao.get(columnFilters)
                        );

                        System.out.print("Rows found with values");
                        for (Map.Entry<String, Object> entry : columnFilters.entrySet())
                            System.out.print(" [" + entry.getKey() + " = " + entry.getValue() + ']');
                        LoggerService.print(": ");
                        LoggerService.print(ReflectionService.toString(list));

                        XMLService.jaxbSerializeList(list);
                    } catch (JAXBException | IOException e) {
                        LoggerService.log(Level.ERROR, e.getMessage());
                    }
                    break;
                default:
                    LoggerService.consoleLog(Level.WARN, "This option does not exist.");
            }
        } while (true);

    }

    public static void runJson() {
        main:
        do {
            char mainMenuOption = InputService.readCharInValues(
                    "0. Exit\n1. Read JSON files\n2. Write JSON from Database\nChoose an option: ",
                    "This option does not exist. Try again: ",
                    new char[]{'0', '1', '2'}
            );

            switch (mainMenuOption) {
                case '0':
                    break main;
                case '1':
                    JsonService.parse();
                    break;
                case '2':
                    Class<Object> entityClass = EntityReflection.chooseEntity();
                    if (entityClass == null) continue;
                    EntityReflection<Object> rs = new EntityReflection<>(entityClass);

                    try {
                        MyBatis<Object> dao = new MyBatis<>(entityClass);
                        System.out.print("\nFill with fields to filter by.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        Object list = getListClassInstance(
                                entityClass,
                                dao.get(columnFilters)
                        );

                        System.out.print("Rows found with values");
                        for (Map.Entry<String, Object> entry : columnFilters.entrySet())
                            System.out.print(" [" + entry.getKey() + " = " + entry.getValue() + ']');
                        LoggerService.print(": ");
                        LoggerService.print(ReflectionService.toString(list));

                        JsonService.serializeList(list);
                    } catch (IOException e) {
                        LoggerService.log(Level.ERROR, e.getMessage());
                    }
                    break;
                default:
                    LoggerService.consoleLog(Level.WARN, "This option does not exist.");
            }
        } while (true);

    }

    @SuppressWarnings("unchecked")
    private static Object getListClassInstance(Class<?> clazz, List<?> list) {
        if (clazz == Customer.class) {
            CustomerList listClass = new CustomerList();
            listClass.setCustomerList((List<Customer>) list);
            return listClass;
        } else if (clazz == Feedback.class) {
            FeedbackList listClass = new FeedbackList();
            listClass.setFeedbackList((List<Feedback>) list);
            return listClass;
        } else if (clazz == Invoice.class) {
            InvoiceList listClass = new InvoiceList();
            listClass.setInvoiceList((List<Invoice>) list);
            return listClass;
        } else if (clazz == Job.class) {
            JobList listClass = new JobList();
            listClass.setJobList((List<Job>) list);
            return listClass;
        } else if (clazz == JobTechnician.class) {
            JobTechnicianList listClass = new JobTechnicianList();
            listClass.setJobTechnicianList((List<JobTechnician>) list);
            return listClass;
        } else if (clazz == Part.class) {
            PartList listClass = new PartList();
            listClass.setPartList((List<Part>) list);
            return listClass;
        } else if (clazz == Payment.class) {
            PaymentList listClass = new PaymentList();
            listClass.setPaymentList((List<Payment>) list);
            return listClass;
        } else if (clazz == RepairTicket.class) {
            RepairTicketList listClass = new RepairTicketList();
            listClass.setRepairTicketList((List<RepairTicket>) list);
            return listClass;
        } else if (clazz == RepairTicketPart.class) {
            RepairTicketPartList listClass = new RepairTicketPartList();
            listClass.setRepairTicketPartList((List<RepairTicketPart>) list);
            return listClass;
        } else if (clazz == Supplier.class) {
            SupplierList listClass = new SupplierList();
            listClass.setSupplierList((List<Supplier>) list);
            return listClass;
        } else if (clazz == Technician.class) {
            TechnicianList listClass = new TechnicianList();
            listClass.setTechnicianList((List<Technician>) list);
            return listClass;
        }

        return List.of();
    }

}
