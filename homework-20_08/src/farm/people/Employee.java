package farm.people;

public abstract class Employee extends Person {
    public enum WorkShift {MORNING, EVENING, NIGHT}
    public enum EmployeeType {CULTIVATOR, ANIMAL_CARETAKER}

    private double annualSalary;
    public WorkShift shift;
    public EmployeeType type;

    public double getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
    }

    public Employee(String fullName, String ssn, int age, double annualSalary, WorkShift shift, EmployeeType type) {
        super(fullName, ssn, age);
        setAnnualSalary(annualSalary);
        this.shift = shift;
        this.type = type;
    }

    public Employee(String fullName, String ssn, int age, WorkShift shift, EmployeeType type) {
        this(fullName, ssn, age, 65000, shift, type);
    }

    public Employee(String fullName, String ssn, int age, double annualSalary, EmployeeType type) {
        this(fullName, ssn, age, annualSalary, WorkShift.MORNING, type);
    }

    public Employee(String fullName, String ssn, int age, EmployeeType type) {
        this(fullName, ssn, age, 65000, WorkShift.MORNING, type);
    }

    @Override
    public abstract String toString();
}
