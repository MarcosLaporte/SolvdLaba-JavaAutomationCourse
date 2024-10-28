package services.dao;

import entities.*;
import entities.lists.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.Level;
import org.xml.sax.SAXException;
import utils.LoggerService;
import services.ReflectionService;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class XMLService<T extends Entity> extends FileDao<T> {
    private static final String baseDir = System.getProperty("user.dir") + "\\files\\xml\\values\\";

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

    public XMLService(Class<T> clazz) {
        super(clazz, baseDir, ".xml");
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
    @Override
    protected List<T> parse(String fileName) throws JAXBException {
        File file = new File(baseDir + fileName);

        if (!file.exists())
            return new ArrayList<>();

        JAXBContext context = JAXBContext.newInstance(ENTITY_CONTAINER_MAP.get(this.clazz));
        Unmarshaller unmarshaller = context.createUnmarshaller();
        IEntityList<T> entityContainer = (IEntityList<T>) unmarshaller.unmarshal(file);

        return entityContainer.getList();
    }

    @SuppressWarnings({"rawtypes", "unchecked", "ResultOfMethodCallIgnored"})
    @Override
    protected void serializeList(List<T> list) throws JAXBException, IOException {
        Class<IEntityList> containerClass = (Class<IEntityList>) ENTITY_CONTAINER_MAP.get(this.clazz);
        JAXBContext context = JAXBContext.newInstance(containerClass);
        File file = new File(baseDir + this.clazz.getSimpleName() + ".xml");

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
}
