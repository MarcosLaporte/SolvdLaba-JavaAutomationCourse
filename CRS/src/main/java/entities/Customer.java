package entities;

public class Customer {
    public int cust_id;
    public String full_name;
    public String email;
    public long phone_no;
    public String address;
    public int zip;

    public Customer(int cust_id, String full_name, String email, long phone_no, String address, int zip) {
        this.cust_id = cust_id;
        this.full_name = full_name;
        this.email = email;
        this.phone_no = phone_no;
        this.address = address;
        this.zip = zip;
    }

    private Customer(Integer cust_id, String full_name, String email, Long phone_no, String address, Integer zip) {
        this(cust_id.intValue(), full_name, email, phone_no.longValue(), address, zip.intValue());
    }

    @Override
    public String toString() {
        return String.format("ID %d - %s", this.cust_id, this.full_name);
    }
}
