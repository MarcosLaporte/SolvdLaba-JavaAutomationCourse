package entities;

import entities.annotations.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "technician")
@Entity
@Table(name = "technicians")
public class Technician {
    @XmlElement
    @Id
    @Column(name = "tech_id", autoIncrement = true)
    int id;

    @XmlElement
    @Column(name = "full_name")
    @Size(min = 1, max = 255)
    String fullName;

    @XmlElement
    @Column(name = "salary")
    @Range(min = 1)
    float salary;

    private Technician() {
    }

    public Technician(String fullName, float salary) {
        this.fullName = fullName;
        this.salary = salary;
    }

    private Technician(String fullName, Float salary) {
        this(fullName, salary.floatValue());
    }

    public Technician(int id, String fullName, float salary) {
        this(fullName, salary);
        this.id = id;
    }

    private Technician(Integer id, String fullName, Float salary) {
        this(id.intValue(), fullName, salary.floatValue());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String toString() {
        return String.format("ID %d - %s", this.id, this.fullName);
    }
}
