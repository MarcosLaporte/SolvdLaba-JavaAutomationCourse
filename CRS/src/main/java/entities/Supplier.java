package entities;

import entities.annotations.*;

@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id
    @Column(name = "supplier_id", autoIncrement = true)
    public int id;

    @Column(name = "full_name")
    public String fullName;

    @Column(name = "email")
    public String email;

    @Column(name = "phone_no")
    public long phoneNo;

    @Column(name = "address")
    public String address;

    public Supplier(int id, @Size(min = 1, max = 255) String fullName, @Size(min = 1, max = 255) String email,
                    @Range(min = 10000000, max = 99999999) long phoneNo, @Size(min = 1, max = 255) String address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
    }

    private Supplier(Integer id, String fullName, String email, Long phoneNo, String address) {
        this(id.intValue(), fullName, email, phoneNo.longValue(), address);
    }

    @Override
    public String toString() {
        return String.format("ID %d - %s", this.id, this.fullName);
    }

}
