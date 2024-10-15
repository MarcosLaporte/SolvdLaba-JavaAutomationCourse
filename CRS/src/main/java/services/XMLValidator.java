package services;

import java.io.File;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import java.io.IOException;

public class XMLValidator {
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

    public static void main(String[] args) {
        String xsdPath = System.getProperty("user.dir") + "\\xml\\repair_shop.xsd";
        String xmlPath = System.getProperty("user.dir") + "\\xml\\repair_shop.xml";

        if (validateXMLSchema(xsdPath, xmlPath)) {
            System.out.println("XML is valid.");
        } else {
            System.out.println("XML is not valid.");
        }
    }
}
