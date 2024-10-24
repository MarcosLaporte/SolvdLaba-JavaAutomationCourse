package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Customer;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "customers")
public class CustomerList {
    @JsonProperty("customers")
    private List<Customer> customerList;

    @XmlElement(name = "customer")
    public List<Customer> getCustomerList() {
        return this.customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
