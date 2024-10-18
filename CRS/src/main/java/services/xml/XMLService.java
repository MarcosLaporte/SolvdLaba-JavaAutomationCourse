package services.xml;

import entities.lists.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.Level;
import org.xml.sax.SAXException;
import services.FileService;
import services.LoggerService;
import services.ReflectionService;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class XMLService {
    private static final String baseDir = System.getProperty("user.dir") + "\\xml\\";
    private static final String xmlDir = baseDir + "\\values\\";
    private static final String xsdDir = baseDir + "\\schemas\\";

    public static boolean validateXMLSchema(String xsdPath, String xmlPath) throws IOException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (SAXException e) {
            System.out.println("Validation Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static void staxParse() {
        String[] xmlFiles = FileService.getFileNames(xmlDir, "xml");
        for (String xml : xmlFiles) {
            String fileNoExt = FileService.stripExtension.apply(xml);
            try {
                String xsdPath = xsdDir + fileNoExt + ".xsd";
                validateXMLSchema(xsdPath, xmlDir + xml);
            } catch (IOException e) {
                LoggerService.log(Level.ERROR, xml + " does not follow Schema.\n" + e.getMessage());
                continue;
            }

            try {
                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(xmlDir + xml));

                List<?> objs = switch (fileNoExt) {
                    case "customer" -> EntityParser.parseCustomers(eventReader);
                    case "feedback" -> EntityParser.parseFeedbacks(eventReader);
                    case "invoice" -> EntityParser.parseInvoices(eventReader);
                    case "job" -> EntityParser.parseJobs(eventReader);
                    case "jobTechnician" -> EntityParser.parseJobTechnicians(eventReader);
                    case "part" -> EntityParser.parseParts(eventReader);
                    case "payment" -> EntityParser.parsePayments(eventReader);
                    case "repairTicket" -> EntityParser.parseRepairTickets(eventReader);
                    case "repairTicketPart" -> EntityParser.parseRepairTicketParts(eventReader);
                    case "supplier" -> EntityParser.parseSuppliers(eventReader);
                    case "technician" -> EntityParser.parseTechnicians(eventReader);
//                    case "ticketStatus" -> {}
                    default -> throw new IllegalStateException("Unexpected value: " + fileNoExt);
                };

                System.out.println(ReflectionService.toString(objs));
                System.out.println("-----------------------------------------------------------------------");
                System.out.println("-----------------------------------------------------------------------");

            } catch (Exception e) {
                LoggerService.log(Level.ERROR, e.getMessage());
            }
        }
    }

    public static <T> T unmarshal(File file, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        return (T) unmarshaller.unmarshal(file);
    }

    public static void jaxbParse() {
        String[] xmlFiles = FileService.getFileNames(xmlDir, "xml");
        for (String xml : xmlFiles) {
            try {
                String fileNoExt = FileService.stripExtension.apply(xml);
                Class<?> clazz = switch (fileNoExt) {
                    case "customer" -> CustomerList.class;
                    case "feedback" -> FeedbackList.class;
                    case "invoice" -> InvoiceList.class;
                    case "job" -> JobList.class;
                    case "jobTechnician" -> JobTechnicianList.class;
                    case "part" -> PartList.class;
                    case "payment" -> PaymentList.class;
                    case "repairTicket" -> RepairTicketList.class;
                    case "repairTicketPart" -> RepairTicketPartList.class;
                    case "supplier" -> SupplierList.class;
                    case "technician" -> TechnicianList.class;
//                    case "ticketStatus" -> {}
                    default -> throw new IllegalStateException("Unexpected value: " + fileNoExt);
                };

                System.out.println(
                        ReflectionService.toString(
                                unmarshal(new File(xmlDir + xml), clazz)
                        )
                );
            } catch (Exception e) {
                LoggerService.log(Level.ERROR, e.getMessage());
            }
        }
    }
}
