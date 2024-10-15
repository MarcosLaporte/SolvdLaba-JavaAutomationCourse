package services;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XMLService {
    private static final String xsdPath = System.getProperty("user.dir") + "\\xml\\repair_shop.xsd";
    private static final String xmlPath = System.getProperty("user.dir") + "\\xml\\repair_shop.xml";

    public static boolean validateXMLSchema(String xsdPath, String xmlPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new File(xsdPath));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException | SAXException e) {
            System.out.println("Validation Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static void printXml() {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(xmlPath));

            int tabs = 0;
            boolean prevWasStart = false;
            boolean prevWasEnd = false;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    if (prevWasStart)
                        System.out.print("\n" + StringUtils.repeat('\t', tabs));

                    String localPart = event.asStartElement().getName().getLocalPart();
                    System.out.print("<" + localPart + ">");

                    tabs++;
                    prevWasStart = true;
                    prevWasEnd = false;
                } else if (event.isCharacters()) {
                    String text = event.asCharacters().getData();
                    if (!text.isEmpty())
                        System.out.print(text);

                    prevWasStart = false;
                    prevWasEnd = false;
                } else if (event.isEndElement()) {
                    tabs--;
                    String localPart = event.asEndElement().getName().getLocalPart();

                    if (prevWasStart) {
                        System.out.print("</" + localPart + ">");
                    } else {
                        if (prevWasEnd)
                            System.out.print("\n" + StringUtils.repeat('\t', tabs));

                        System.out.print("</" + localPart + ">");
                    }

                    prevWasStart = false;
                    prevWasEnd = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (validateXMLSchema(xsdPath, xmlPath)) {
            System.out.println("XML is valid.");
            printXml();
        } else {
            System.out.println("XML is not valid.");
        }
    }
}
