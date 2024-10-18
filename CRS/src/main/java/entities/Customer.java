package entities;

import entities.annotations.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
@Entity
@Table(name = "customers")
public class Customer {
    @XmlElement
    @Id
    @Column(name = "cust_id", autoIncrement = true)
    public int id;

    @XmlElement
    @Column(name = "full_name")
    @Size(min = 1, max = 255)
    public String fullName;

    @XmlElement
    @Column(name = "email")
    @Size(min = 1, max = 255)
    public String email;

    @XmlElement
    @Column(name = "phone_no")
    @Range(min = 10000000, max = 99999999)
    public long phoneNo;

    @XmlElement
    @Column(name = "address")
    @Size(min = 1, max = 255)
    public String address;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNo(long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return String.format("ID %d - %s", this.id, this.fullName);
    }
}
