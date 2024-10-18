package entities.lists;

import entities.Invoice;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "invoices")
public class InvoiceList {
    private List<Invoice> invoices;

    @XmlElement(name = "invoice")
    public List<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
