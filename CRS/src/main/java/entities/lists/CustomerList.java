package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Customer;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "customers")
public class CustomerList implements IEntityList<Customer> {
    @JsonProperty("customers")
    private List<Customer> customerList;

    private CustomerList() {
    }

    private CustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @XmlElement(name = "customer")
    @Override
    public List<Customer> getList() {
        return this.customerList;
    }

    @Override
    public void setList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
