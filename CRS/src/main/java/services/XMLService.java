package services;

import entities.Customer;
import org.apache.logging.log4j.Level;
import org.xml.sax.SAXException;

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
    private static final String xmlDir = System.getProperty("user.dir") + "\\xml\\";

    public static boolean validateXMLSchema(String xsdPath, String xmlPath) throws IOException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new File(xsdPath));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (SAXException e) {
            System.out.println("Validation Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static void parse() {
        String[] xmlFiles = FileService.getFileNames(xmlDir, "xml");

        for (String xml : xmlFiles) {
            String fileNoExt = FileService.stripExtension.apply(xml);
            try {
                validateXMLSchema(xmlDir + fileNoExt + ".xsd", xmlDir + xml);
            } catch (IOException e) {
                LoggerService.log(Level.ERROR, xml + " does not follow Schema.\n" + e.getMessage());
                continue;
            }

            try {
                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(xmlDir + xml));

                switch (fileNoExt) {
                    case "customer" -> {
                        List<Customer> cust = EntityParser.parseCustomers(eventReader);
                        for (Customer customer : cust) {
                            System.out.println(customer);
                        }
                    }
                    case "feedback" -> EntityParser.parseFeedbacks(eventReader);
                    case "invoice" -> EntityParser.parseInvoices(eventReader);
                    case "job" -> EntityParser.parseJobs(eventReader);
                    case "jobTechnician" -> EntityParser.parseJobTechnicians(eventReader);
                    case "part" -> EntityParser.parseParts(eventReader);
                    case "payment" -> EntityParser.parsePayments(eventReader);
                    case "repairShop" -> EntityParser.parseRepairShops(eventReader);
                    case "repairTicket" -> EntityParser.parseRepairTickets(eventReader);
                    case "repairTicketPart" -> EntityParser.parseRepairTicketParts(eventReader);
                    case "supplier" -> EntityParser.parseSuppliers(eventReader);
                    case "technician" -> EntityParser.parseTechnicians(eventReader);
                }

            } catch (Exception e) {
                LoggerService.log(Level.ERROR, e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        parse();
    }
}
