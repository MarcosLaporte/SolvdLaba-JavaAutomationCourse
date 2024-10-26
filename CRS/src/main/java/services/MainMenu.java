package services;

import entities.*;
import entities.lists.*;
import jakarta.xml.bind.JAXBException;
import org.apache.logging.log4j.Level;
import services.connection.ConnectionManager;
import services.database.EntityReflection;
import services.database.MyBatis;
import services.xml.XMLService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainMenu {
    private enum DaoMenu {
        GET, CREATE, UPDATE, DELETE;

        public static DaoMenu readAction() {
            int menuIndex = InputService.selectIndexFromList (
                    Arrays.stream(DaoMenu.values()).map(DaoMenu::toString).toList(),
                    true
            );

            return menuIndex != -1 ? DaoMenu.values()[menuIndex] : null;
        }
    }

    public static <T extends Entity> void runDatabase() {
        do {
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

            LoggerService.println('\n' + entityClass.getSimpleName() + " selected.");
            try (MyBatis<T> dao = new MyBatis<>(entityClass)) {
                EntityReflection<T> rs = new EntityReflection<>(entityClass);
                switch (daoAction) {
                    case GET -> {
                        LoggerService.print("\nFill with fields to filter by.");
                        Map<String, Object> columnFilters = rs.readConditionValues();
                        List<?> values = dao.get(columnFilters);

                        LoggerService.print("Rows found with values");
                        for (Map.Entry<String, Object> entry : columnFilters.entrySet())
                            LoggerService.print(" [" + entry.getKey() + " = " + entry.getValue() + ']');
                        LoggerService.println(": ");
                        LoggerService.println(ReflectionService.toString(values));
                    }
                    case CREATE -> {
                        if (dao.create(rs.readNewInstance()) > 0)
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
                LoggerService.log(Level.ERROR, e.getMessage());

                try {
                    ConnectionManager.closePool();
                } catch (SQLException ex) {
                    LoggerService.log(Level.ERROR, ex.getMessage());
                }
            }

        } while (true);

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
                    Class<? extends Entity> entityClass = EntityReflection.chooseEntity();
                    if (entityClass == null) continue;
                    EntityReflection<? extends Entity> rs = new EntityReflection<>(entityClass);

                    try (MyBatis<? extends Entity> dao = new MyBatis<>(entityClass)) {
                        LoggerService.print("\nFill with fields to filter by.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        Object list = getListClassInstance(entityClass, dao.get(columnFilters));

                        LoggerService.print("Rows found with values");
                        for (Map.Entry<String, Object> entry : columnFilters.entrySet())
                            LoggerService.print(" [" + entry.getKey() + " = " + entry.getValue() + ']');
                        LoggerService.println(": ");
                        LoggerService.println(ReflectionService.toString(list));

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
                    Class<? extends Entity> entityClass = EntityReflection.chooseEntity();
                    if (entityClass == null) continue;
                    EntityReflection<? extends Entity> rs = new EntityReflection<>(entityClass);

                    try (MyBatis<? extends Entity> dao = new MyBatis<>(entityClass)) {
                        LoggerService.print("\nFill with fields to filter by.");
                        Map<String, Object> columnFilters = rs.readConditionValues();

                        Object list = getListClassInstance(entityClass, dao.get(columnFilters));

                        LoggerService.print("Rows found with values");
                        for (Map.Entry<String, Object> entry : columnFilters.entrySet())
                            LoggerService.print(" [" + entry.getKey() + " = " + entry.getValue() + ']');
                        LoggerService.println(": ");
                        LoggerService.println(ReflectionService.toString(list));

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
    private static <T extends Entity> Object getListClassInstance(Class<T> clazz, List<?> list) {
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
