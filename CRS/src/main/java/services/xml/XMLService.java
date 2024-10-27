package services.xml;

import entities.*;
import entities.lists.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.Level;
import org.xml.sax.SAXException;
import services.LoggerService;
import services.ReflectionService;
import services.database.EntityReflection;
import services.database.IDao;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class XMLService<T extends Entity> implements IDao<T> {
    private static final String baseDir = System.getProperty("user.dir") + "\\files\\xml\\";
    private static final String xmlDir = baseDir + "\\values\\";
    private static final String xsdDir = baseDir + "\\schemas\\";

    private static final Map<Class<? extends Entity>, Class<?>> ENTITY_CONTAINER_MAP =
            Map.ofEntries(
                    entry(Customer.class, CustomerList.class),
                    entry(Feedback.class, FeedbackList.class),
                    entry(Invoice.class, InvoiceList.class),
                    entry(Job.class, JobList.class),
                    entry(JobTechnician.class, JobTechnicianList.class),
                    entry(Part.class, PartList.class),
                    entry(Payment.class, PaymentList.class),
                    entry(RepairTicket.class, RepairTicketList.class),
                    entry(RepairTicketPart.class, RepairTicketPartList.class),
                    entry(Supplier.class, SupplierList.class),
                    entry(Technician.class, TechnicianList.class)
            );

    private final EntityReflection<T> entRef;
    public final Class<T> clazz;

    public XMLService(Class<T> clazz) {
        this.clazz = clazz;
        this.entRef = new EntityReflection<>(this.clazz);
    }

    public static boolean validateXMLSchema(String xsdPath, String xmlPath) throws IOException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (SAXException e) {
            LoggerService.println("Validation Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private List<T> parse(String fileName) throws JAXBException {
        File file = new File(xmlDir + fileName);

        if (!file.exists())
            return new ArrayList<>();

        JAXBContext context = JAXBContext.newInstance(ENTITY_CONTAINER_MAP.get(this.clazz));
        Unmarshaller unmarshaller = context.createUnmarshaller();
        IEntityList<T> entityContainer = (IEntityList<T>) unmarshaller.unmarshal(file);

        return entityContainer.getList();
    }

    @SuppressWarnings({"rawtypes", "unchecked", "ResultOfMethodCallIgnored"})
    private void serializeList(List<T> list) throws JAXBException, IOException {
        Class<IEntityList> containerClass = (Class<IEntityList>) ENTITY_CONTAINER_MAP.get(this.clazz);
        JAXBContext context = JAXBContext.newInstance(containerClass);
        File file = new File(xmlDir + this.clazz.getSimpleName() + ".xml");

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        if (!file.exists())
            file.createNewFile();

        ReflectionService<IEntityList> rs = new ReflectionService<>(containerClass);
        IEntityList<T> entityContainer = null;
        try {
            entityContainer = rs.createInstance(list);
        } catch (NoSuchMethodException e) {
            LoggerService.log(Level.ERROR, "Unable to create container for " + clazz.getSimpleName());
        }

        marshaller.marshal(entityContainer, file);
    }

    @Override
    public List<T> get(Map<String, Object> fieldValueFilters) {
        try {
            List<T> elements = parse(this.clazz.getSimpleName() + ".xml");

            return this.entRef.filterListByFields(elements, fieldValueFilters);
        } catch (JAXBException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public int create(T t) {
        List<T> elements = this.get(Map.of());
        elements.add(t);

        try {
            serializeList(elements);
            return 1;
        } catch (IOException | JAXBException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return 0;
        }
    }

    @Override
    public int update(Map<String, Object> newValues, Map<String, Object> fieldValueFilters) {
        List<T> elements = this.get(Map.of());
        List<T> elementsToUpdate = entRef.filterListByFields(elements, fieldValueFilters);
        Map<String, Field> fieldMap = this.entRef.matchColumnField();
        int updateCount = 0;

        for (T currentEl : elements) {
            if (!elementsToUpdate.contains(currentEl))
                continue;

            for (String column : fieldMap.keySet()) {
                if (!newValues.containsKey(column))
                    continue;

                updateCount++;
                try {
                    Field f = fieldMap.get(column);
                    f.setAccessible(true);
                    f.set(currentEl, newValues.get(column));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error updating field: " + column, e);
                }
            }
        }

        try {
            serializeList(elements);
        } catch (IOException | JAXBException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }

        return updateCount;
    }


    @Override
    public int delete(Map<String, Object> fieldValueFilters) {
        List<T> elements = this.get(Map.of());
        List<T> elementsToDelete = entRef.filterListByFields(elements, fieldValueFilters);

        try {
            elementsToDelete.forEach(elements::remove);
            serializeList(elements);
        } catch (IOException | JAXBException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }

        return elementsToDelete.size();
    }

}
