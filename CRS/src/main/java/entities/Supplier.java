package entities;

public class Supplier {
    public int supplier_id;
    public String full_name;
    public String email;
    public long phone_no;
    public String address;

    public Supplier(int supplier_id, String full_name, String email, long phone_no, String address) {
        this.supplier_id = supplier_id;
        this.full_name = full_name;
        this.email = email;
        this.phone_no = phone_no;
        this.address = address;
    }

    private Supplier(Integer supplier_id, String full_name, String email, Long phone_no, String address) {
        this(supplier_id.intValue(), full_name, email, phone_no.longValue(), address);
    }
}
