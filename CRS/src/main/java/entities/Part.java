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
    @Size(min = 1, max = 255)
    public String name;

    @Column(name = "description")
    @Size(min = 1, max = 255)
    public String description;

    @Column(name = "value")
    @Range(min = 1)
    public double value;

    @Column(name = "stock")
    @Range(min = 1)
    public int stock;

    public Part(int supplierId, String name, String description, double value, int stock) {
        this.supplierId = supplierId;
        this.name = name;
        this.description = description;
        this.value = value;
        this.stock = stock;
    }

    private Part(Integer supplierId, String name, String description, Double value, Integer stock) {
        this(supplierId.intValue(), name, description, value.doubleValue(), stock.intValue());
    }

    public Part(int id, int supplierId, String name, String description, double value, int stock) {
        this(supplierId, name, description, value, stock);
        this.id = id;
    }

    private Part(Integer id, Integer supplierId, String name, String description, Double value, Integer stock) {
        this(id.intValue(), supplierId.intValue(), name, description, value.doubleValue(), stock.intValue());
    }

    public String toString() {
        return String.format("ID %d - %s", this.id, this.name);
    }

}
