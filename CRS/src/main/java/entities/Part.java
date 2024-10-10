package entities;

public class Part {
    public int part_id;
    public int supplier_id;
    public String name;
    public String description;
    public double value;
    public int stock;

    public Part(int part_id, int supplier_id, String name, String description, double value, int stock) {
        this.part_id = part_id;
        this.supplier_id = supplier_id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.stock = stock;
    }

    private Part(Integer part_id, Integer supplier_id, String name, String description, Double value, Integer stock) {
        this(part_id.intValue(), supplier_id.intValue(), name, description, value.doubleValue(), stock.intValue());
    }
}
