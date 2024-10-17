package entities;

import entities.annotations.*;

@Entity
@Table(name = "technicians")
public class Technician {
    @Id
    @Column(name = "tech_id", autoIncrement = true)
    int id;

    @Column(name = "full_name")
    @Size(min = 1, max = 255)
    String fullName;

    @Column(name = "salary")
    @Range(min = 1)
    float salary;

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

    public String toString() {
        return String.format("ID %d - %s", this.id, this.fullName);
    }
}
