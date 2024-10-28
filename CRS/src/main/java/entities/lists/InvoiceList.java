package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Invoice;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "invoices")
public class InvoiceList implements IEntityList<Invoice> {
    @JsonProperty("invoices")
    private List<Invoice> invoiceList;

    private InvoiceList() {
    }

    private InvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @XmlElement(name = "invoice")
    @Override
    public List<Invoice> getList() {
        return this.invoiceList;
    }

    @Override
    public void setList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }
}
