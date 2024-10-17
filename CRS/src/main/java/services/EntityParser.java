package services;

import entities.Customer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.List;

public class EntityParser {

    public static List<Customer> parseCustomers(XMLEventReader eventReader) throws XMLStreamException {
        List<Customer> customerList = new ArrayList<>();
        Customer currentCust = new Customer();

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("customer")) {
                    currentCust = new Customer();
                }

                switch (localName) {
                    case "id" -> currentCust.setId(Integer.parseInt(eventReader.getElementText()));
                    case "fullName" -> currentCust.setFullName(eventReader.getElementText());
                    case "email" -> currentCust.setEmail(eventReader.getElementText());
                    case "phoneNo" -> currentCust.setPhoneNo(Long.parseLong(eventReader.getElementText()));
                    case "address" -> currentCust.setAddress(eventReader.getElementText());
                    case "zip" -> currentCust.setZip(Integer.parseInt(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("customer")) {
                customerList.add(currentCust);
            }
        }

        return customerList;
    }

    public static void parseFeedbacks(XMLEventReader eventReader) {}

    public static void parseInvoices(XMLEventReader eventReader) {}

    public static void parseJobs(XMLEventReader eventReader) {}

    public static void parseJobTechnicians(XMLEventReader eventReader) {}

    public static void parseParts(XMLEventReader eventReader) {}

    public static void parsePayments(XMLEventReader eventReader) {}

    public static void parseRepairShops(XMLEventReader eventReader) {}

    public static void parseRepairTickets(XMLEventReader eventReader) {}

    public static void parseRepairTicketParts(XMLEventReader eventReader) {}

    public static void parseSuppliers(XMLEventReader eventReader) {}

    public static void parseTechnicians(XMLEventReader eventReader) {}
}
