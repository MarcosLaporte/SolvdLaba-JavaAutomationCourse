package entities;

import entities.annotations.*;

@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id
    @Column(name = "supplier_id", autoIncrement = true)
    public int id;

    @Column(name = "full_name")
    @Size(min = 1, max = 255)
    public String fullName;

    @Column(name = "email")
    @Size(min = 1, max = 255)
    public String email;

    @Column(name = "phone_no")
    @Range(min = 10000000, max = 99999999)
    public long phoneNo;

    @Column(name = "address")
    @Size(min = 1, max = 255)
    public String address;

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

    @Override
    public String toString() {
        return String.format("ID %d - %s", this.id, this.fullName);
    }

}
