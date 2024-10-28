package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.annotations.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "part")
@Table(name = "parts")
public class Part extends Entity {
    @JsonProperty
    @XmlElement
    @Id
    @Column(name = "part_id", autoIncrement = true)
    public int id;

    @JsonProperty
    @XmlElement
    @Column(name = "supplier_id")
    public int supplierId;

    @JsonProperty
    @XmlElement
    @Column(name = "name")
    @Size(min = 1, max = 255)
    public String name;

    @JsonProperty
    @XmlElement
    @Column(name = "description")
    @Size(min = 1, max = 255)
    public String description;

    @JsonProperty
    @XmlElement
    @Column(name = "value")
    @Range(min = 1)
    public double value;

    @JsonProperty
    @XmlElement
    @Column(name = "stock")
    @Range(min = 1)
    public int stock;

    private Part() {
    }

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
