package farm.people;

public abstract class Farmer {
    public enum FarmerType {OWNER, CULTIVATOR, ANIMAL_CARETAKER}
    public enum FarmerShift {MORNING, EVENING, NIGHT}

    public String fullName;
    private String ssn;
    public FarmerType type;
    private double annualSalary;

    public String getSsn() {
        return ssn;
    }
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public double getAnnualSalary() {
        return annualSalary;
    }
    public void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
    }

    public Farmer(String fullName, String ssn, double annualSalary) {
        this.fullName = fullName;
        setSsn(ssn);
        setAnnualSalary(annualSalary);
    }
    public Farmer(String fullName, String ssn) {
        this(fullName, ssn, 65000);
    }

    public String toString() {
        return String.format("| %-16s | %15s | %11s | $%9.2f |\n",
                this.type, this.fullName, this.ssn, this.annualSalary);
    }
}