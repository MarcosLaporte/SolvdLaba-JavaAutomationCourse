package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.annotations.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "customer")
@Table(name = "customers")
public class Customer extends Entity {
    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "cust_id", autoIncrement = true)
    public int id;

    @JsonProperty
    @XmlElement
    @Column(name = "full_name")
    @Size(min = 1, max = 255)
    public String fullName;

    @JsonProperty
    @XmlElement
    @Column(name = "email")
    @Size(min = 1, max = 255)
    public String email;

    @JsonProperty
    @XmlElement
    @Column(name = "phone_no")
    @Range(min = 10000000, max = 99999999)
    public long phoneNo;

    @JsonProperty
    @XmlElement
    @Column(name = "address")
    @Size(min = 1, max = 255)
    public String address;

    @JsonProperty
    @XmlElement
    @Column(name = "zip")
    @Range(min = 1, max = 9999999)
    public int zip;

    private Customer() {
    }

    public Customer(String fullName, String email, long phoneNo, String address, int zip) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
        this.zip = zip;
    }

    private Customer(String fullName, String email, Long phoneNo, String address, Integer zip) {
        this(fullName, email, phoneNo.longValue(), address, zip.intValue());
    }

    public Customer(int id, String fullName, String email, long phoneNo, String address, int zip) {
        this(fullName, email, phoneNo, address, zip);
        this.id = id;
    }

    private Customer(Integer id, String fullName, String email, Long phoneNo, String address, Integer zip) {
        this(id.intValue(), fullName, email, phoneNo.longValue(), address, zip.intValue());
    }

    @Override
    public String toString() {
        return String.format("ID %d - %s", this.id, this.fullName);
    }
}
