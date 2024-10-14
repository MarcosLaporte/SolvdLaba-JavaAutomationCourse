package entities;

import entities.annotations.*;

@Entity
@Table(name = "parts")
public class Part {
    @Id
    @Column(name = "part_id", autoIncrement = true)
    public int id;

    @Column(name = "supplier_id")
    public int supplierId;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "value")
    public double value;

    @Column(name = "stock")
    public int stock;

    public Part(int id, int supplierId, @Size(min = 1, max = 255) String name, @Size(min = 1, max = 255) String description,
                @Range(min = 1) double value, @Range(min = 1) int stock) {
        this.id = id;
        this.supplierId = supplierId;
        this.name = name;
        this.description = description;
        this.value = value;
        this.stock = stock;
    }

    private Part(Integer id, Integer supplierId, String name, String description, Double value, Integer stock) {
        this(id.intValue(), supplierId.intValue(), name, description, value.doubleValue(), stock.intValue());
    }

    public String toString() {
        return String.format("ID %d - %s", this.id, this.name);
    }

}
