package entities.lists;

import entities.Supplier;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "suppliers")
public class SupplierList {
    private List<Supplier> supplierList;

    @XmlElement(name = "supplier")
    public List<Supplier> getSupplierList() {
        return this.supplierList;
    }

    public void setSupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }
}
