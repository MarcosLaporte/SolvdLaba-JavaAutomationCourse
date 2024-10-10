package entities;

public class Technician {
    int tech_id;
    String full_name;
    float salary;

    public Technician(int tech_id, String full_name, float salary) {
        this.tech_id = tech_id;
        this.full_name = full_name;
        this.salary = salary;
    }

    private Technician(Integer tech_id, String full_name, Float salary) {
        this(tech_id.intValue(), full_name, salary.floatValue());
    }
}
