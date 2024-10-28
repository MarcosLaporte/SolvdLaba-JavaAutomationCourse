package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Supplier;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "suppliers")
public class SupplierList implements IEntityList<Supplier> {
    @JsonProperty("suppliers")
    private List<Supplier> supplierList;

    private SupplierList() {
    }

    private SupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    @XmlElement(name = "supplier")
    @Override
    public List<Supplier> getList() {
        return this.supplierList;
    }

    @Override
    public void setList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }
}
