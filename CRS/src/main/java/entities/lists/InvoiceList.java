package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Invoice;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "invoices")
public class InvoiceList {
    @JsonProperty("invoices")
    private List<Invoice> invoiceList;

    @XmlElement(name = "invoice")
    public List<Invoice> getInvoiceList() {
        return this.invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }
}
