package entities;

import entities.annotations.*;
import entities.annotations.Entity;
import services.InputService;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @Column(name = "cust_id", autoIncrement = true)
    public int id;

    @Column(name = "full_name")
    public String fullName;

    @Column(name = "email")
    public String email;

    @Column(name = "phone_no")
    public long phoneNo;

    @Column(name = "address")
    public String address;

    @Column(name = "zip")
    public int zip;

    public Customer(int id, String fullName, String email, @Range(min = 10000000, max = 99999999) long phoneNo, @Size(min = 1, max = 255) String address, @Range(min = 1, max = 9999999) int zip) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
        this.zip = zip;
    }

    private Customer(Integer id, String fullName, String email, Long phoneNo, String address, Integer zip) {
        this(id.intValue(), fullName, email, phoneNo.longValue(), address, zip.intValue());
    }

    @Override
    public String toString() {
        return String.format("ID %d - %s", this.id, this.fullName);
    }

    public static Customer readCustomer() {
        String fullName = InputService.readString("Enter customer's full name: ", 1, 255);
        String email = InputService.readString("Enter customer's email address: ", 1, 255);
        long phoneNo = InputService.readInt("Enter customer's phone number: ", "Enter a valid number: ", 1, Integer.MAX_VALUE);
        String address = InputService.readString("Enter customer's address: ", 1, 255);
        int zip = InputService.readInt("Enter customer's ZIP or Postal code: ", "Enter a valid number: ", 1, Integer.MAX_VALUE);

        return new Customer(-1, fullName, email, phoneNo, address, -1);
    }
}
