package services.xml;

import entities.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static services.xml.DateAdapter.stringToLocalDate;

public class EntityParser {

    private static final Function<Class<?>, ?> getDefaultInstance = clazz -> {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    };

    public static List<Customer> parseCustomers(XMLEventReader eventReader) throws XMLStreamException {
        List<Customer> customerList = new ArrayList<>();
        Customer currCust = (Customer) getDefaultInstance.apply(Customer.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("customer")) {
                    currCust = (Customer) getDefaultInstance.apply(Customer.class);
                }

                switch (localName) {
                    case "id" -> currCust.setId(Integer.parseInt(eventReader.getElementText()));
                    case "fullName" -> currCust.setFullName(eventReader.getElementText());
                    case "email" -> currCust.setEmail(eventReader.getElementText());
                    case "phoneNo" -> currCust.setPhoneNo(Long.parseLong(eventReader.getElementText()));
                    case "address" -> currCust.setAddress(eventReader.getElementText());
                    case "zip" -> currCust.setZip(Integer.parseInt(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("customer")) {
                customerList.add(currCust);
            }
        }

        return customerList;
    }

    public static List<Technician> parseTechnicians(XMLEventReader eventReader) throws XMLStreamException {
        List<Technician> technicianList = new ArrayList<>();
        Technician currTechnician = (Technician) getDefaultInstance.apply(Technician.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("technician")) {
                    currTechnician = (Technician) getDefaultInstance.apply(Technician.class);
                }

                switch (localName) {
                    case "id" -> currTechnician.setId(Integer.parseInt(eventReader.getElementText()));
                    case "fullName" -> currTechnician.setFullName(eventReader.getElementText());
                    case "salary" -> currTechnician.setSalary(Float.parseFloat(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("technician")) {
                technicianList.add(currTechnician);
            }
        }

        return technicianList;
    }

    public static List<RepairTicket> parseRepairTickets(XMLEventReader eventReader) throws XMLStreamException {
        List<RepairTicket> repairTicketList = new ArrayList<>();
        RepairTicket currRepairTicket = (RepairTicket) getDefaultInstance.apply(RepairTicket.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("repairTicket")) {
                    currRepairTicket = (RepairTicket) getDefaultInstance.apply(RepairTicket.class);
                }

                switch (localName) {
                    case "id" -> currRepairTicket.setId(Integer.parseInt(eventReader.getElementText()));
                    case "custId" -> currRepairTicket.setCustId(Integer.parseInt(eventReader.getElementText()));
                    case "computerDesc" -> currRepairTicket.setComputerDesc(eventReader.getElementText());
                    case "issue" -> currRepairTicket.setIssue(eventReader.getElementText());
                    case "dateSubmitted" ->
                            currRepairTicket.setDateSubmitted(stringToLocalDate(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("repairTicket")) {
                repairTicketList.add(currRepairTicket);
            }
        }

        return repairTicketList;
    }

    public static List<Invoice> parseInvoices(XMLEventReader eventReader) throws XMLStreamException {
        List<Invoice> invoiceList = new ArrayList<>();
        Invoice currInvoice = (Invoice) getDefaultInstance.apply(Invoice.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("invoice")) {
                    currInvoice = (Invoice) getDefaultInstance.apply(Invoice.class);
                }

                switch (localName) {
                    case "id" -> currInvoice.setId(Integer.parseInt(eventReader.getElementText()));
                    case "ticketId" -> currInvoice.setTicketId(Integer.parseInt(eventReader.getElementText()));
                    case "techId" -> currInvoice.setTechId(Integer.parseInt(eventReader.getElementText()));
                    case "diagnosis" -> currInvoice.setDiagnosis(eventReader.getElementText());
                    case "amount" -> currInvoice.setAmount(Double.parseDouble(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("invoice")) {
                invoiceList.add(currInvoice);
            }
        }

        return invoiceList;
    }

    public static List<Supplier> parseSuppliers(XMLEventReader eventReader) throws XMLStreamException {
        List<Supplier> supplierList = new ArrayList<>();
        Supplier currSupplier = (Supplier) getDefaultInstance.apply(Supplier.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("supplier")) {
                    currSupplier = (Supplier) getDefaultInstance.apply(Supplier.class);
                }

                switch (localName) {
                    case "id" -> currSupplier.setId(Integer.parseInt(eventReader.getElementText()));
                    case "fullName" -> currSupplier.setFullName(eventReader.getElementText());
                    case "email" -> currSupplier.setEmail(eventReader.getElementText());
                    case "phoneNo" -> currSupplier.setPhoneNo(Long.parseLong(eventReader.getElementText()));
                    case "address" -> currSupplier.setAddress(eventReader.getElementText());
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("supplier")) {
                supplierList.add(currSupplier);
            }
        }

        return supplierList;
    }

    public static List<Part> parseParts(XMLEventReader eventReader) throws XMLStreamException {
        List<Part> partList = new ArrayList<>();
        Part currPart = (Part) getDefaultInstance.apply(Part.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("part")) {
                    currPart = (Part) getDefaultInstance.apply(Part.class);
                }

                switch (localName) {
                    case "id" -> currPart.setId(Integer.parseInt(eventReader.getElementText()));
                    case "supplierId" -> currPart.setSupplierId(Integer.parseInt(eventReader.getElementText()));
                    case "name" -> currPart.setName(eventReader.getElementText());
                    case "description" -> currPart.setDescription(eventReader.getElementText());
                    case "value" -> currPart.setValue(Double.parseDouble(eventReader.getElementText()));
                    case "stock" -> currPart.setStock(Integer.parseInt(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("part")) {
                partList.add(currPart);
            }
        }

        return partList;
    }

    public static List<RepairTicketPart> parseRepairTicketParts(XMLEventReader eventReader) throws XMLStreamException {
        List<RepairTicketPart> repairTicketPartList = new ArrayList<>();
        RepairTicketPart currRepairTicketPart = (RepairTicketPart) getDefaultInstance.apply(RepairTicketPart.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("repairTicketPart")) {
                    currRepairTicketPart = (RepairTicketPart) getDefaultInstance.apply(RepairTicketPart.class);
                }

                switch (localName) {
                    case "ticketId" -> currRepairTicketPart.setTicketId(Integer.parseInt(eventReader.getElementText()));
                    case "partId" -> currRepairTicketPart.setPartId(Integer.parseInt(eventReader.getElementText()));
                    case "quantity" -> currRepairTicketPart.setQuantity(Integer.parseInt(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("repairTicketPart")) {
                repairTicketPartList.add(currRepairTicketPart);
            }
        }

        return repairTicketPartList;
    }

    public static List<Job> parseJobs(XMLEventReader eventReader) throws XMLStreamException {
        List<Job> jobList = new ArrayList<>();
        Job currJob = (Job) getDefaultInstance.apply(Job.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("job")) {
                    currJob = (Job) getDefaultInstance.apply(Job.class);
                }

                switch (localName) {
                    case "id" -> currJob.setId(Integer.parseInt(eventReader.getElementText()));
                    case "ticketId" -> currJob.setTicketId(Integer.parseInt(eventReader.getElementText()));
                    case "dateStart" -> currJob.setDateStart(stringToLocalDate(eventReader.getElementText()));
                    case "dateFinish" -> currJob.setDateFinish(stringToLocalDate(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("job")) {
                jobList.add(currJob);
            }
        }

        return jobList;
    }

    public static List<JobTechnician> parseJobTechnicians(XMLEventReader eventReader) throws XMLStreamException {
        List<JobTechnician> jobTechnicianList = new ArrayList<>();
        JobTechnician currJobTechnician = (JobTechnician) getDefaultInstance.apply(JobTechnician.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("jobTechnician")) {
                    currJobTechnician = (JobTechnician) getDefaultInstance.apply(JobTechnician.class);
                }

                switch (localName) {
                    case "jobId" -> currJobTechnician.setJobId(Integer.parseInt(eventReader.getElementText()));
                    case "techId" -> currJobTechnician.setTechId(Integer.parseInt(eventReader.getElementText()));
                    case "task" -> currJobTechnician.setTask(eventReader.getElementText());
                    case "done" -> currJobTechnician.setDone(Boolean.parseBoolean(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("jobTechnician")) {
                jobTechnicianList.add(currJobTechnician);
            }
        }

        return jobTechnicianList;
    }

    public static List<Payment> parsePayments(XMLEventReader eventReader) throws XMLStreamException {
        List<Payment> paymentList = new ArrayList<>();
        Payment currPayment = (Payment) getDefaultInstance.apply(Payment.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("payment")) {
                    currPayment = (Payment) getDefaultInstance.apply(Payment.class);
                }

                switch (localName) {
                    case "jobId" -> currPayment.setJobId(Integer.parseInt(eventReader.getElementText()));
                    case "payDate" -> currPayment.setPayDate(stringToLocalDate(eventReader.getElementText()));
                    case "amount" -> currPayment.setAmount(Double.parseDouble(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("payment")) {
                paymentList.add(currPayment);
            }
        }

        return paymentList;
    }

    public static List<Feedback> parseFeedbacks(XMLEventReader eventReader) throws XMLStreamException {
        List<Feedback> feedbackList = new ArrayList<>();
        Feedback currFeedback = (Feedback) getDefaultInstance.apply(Feedback.class);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                if (localName.equalsIgnoreCase("feedback")) {
                    currFeedback = (Feedback) getDefaultInstance.apply(Feedback.class);
                }

                switch (localName) {
                    case "jobId" -> currFeedback.setJobId(Integer.parseInt(eventReader.getElementText()));
                    case "custComment" -> currFeedback.setCustComment(eventReader.getElementText());
                    case "rating" -> currFeedback.setRating(Integer.parseInt(eventReader.getElementText()));
                    case "dateSubmit" -> currFeedback.setDateSubmit(stringToLocalDate(eventReader.getElementText()));
                }
            } else if (event.isEndElement() &&
                    event.asEndElement().getName().getLocalPart().equalsIgnoreCase("feedback")) {
                feedbackList.add(currFeedback);
            }
        }

        return feedbackList;
    }
}
