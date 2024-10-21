package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.annotations.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "supplier")
@Entity
@Table(name = "suppliers")
public class Supplier {
    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "supplier_id", autoIncrement = true)
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

    private Supplier() {
    }

    public Supplier(String fullName, String email, long phoneNo, String address) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
    }

    private Supplier(String fullName, String email, Long phoneNo, String address) {
        this(fullName, email, phoneNo.longValue(), address);
    }

    public Supplier(int id, String fullName, String email, long phoneNo, String address) {
        this(fullName, email, phoneNo, address);
        this.id = id;
    }

    private Supplier(Integer id, String fullName, String email, Long phoneNo, String address) {
        this(id.intValue(), fullName, email, phoneNo.longValue(), address);
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

    @Override
    public String toString() {
        return String.format("ID %d - %s", this.id, this.fullName);
    }

}
