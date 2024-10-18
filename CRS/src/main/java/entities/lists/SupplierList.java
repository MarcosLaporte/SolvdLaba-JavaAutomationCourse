package entities.lists;

import entities.Supplier;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "suppliers")
public class SupplierList {
    private List<Supplier> suppliers;

    @XmlElement(name = "supplier")
    public List<Supplier> getSuppliers() {
        return this.suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }
}
