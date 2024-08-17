package farm.people;

import farm.crops.Crop;

import java.util.List;

public class CropsCultivator extends Employee {
    public List<Crop> cropsInCare;
    public int lastHarvestYield;

    public CropsCultivator(String fullName, String ssn, int age, double annualSalary, WorkShift shift, List<Crop> cropsInCare, int lastHarvestYield) {
        super(fullName, ssn, age, annualSalary, shift, EmployeeType.CULTIVATOR);
        this.cropsInCare = cropsInCare;
        this.lastHarvestYield = lastHarvestYield;
    }

    public CropsCultivator(String fullName, String ssn, int age, WorkShift shift, List<Crop> cropsInCare, int lastHarvestYield) {
        super(fullName, ssn, age, shift, EmployeeType.CULTIVATOR);
        this.cropsInCare = cropsInCare;
        this.lastHarvestYield = lastHarvestYield;
    }

    public CropsCultivator(String fullName, String ssn, int age, double annualSalary, List<Crop> cropsInCare, int lastHarvestYield) {
        super(fullName, ssn, age, annualSalary, EmployeeType.CULTIVATOR);
        this.cropsInCare = cropsInCare;
        this.lastHarvestYield = lastHarvestYield;
    }

    public CropsCultivator(String fullName, String ssn, int age, List<Crop> cropsInCare, int lastHarvestYield) {
        super(fullName, ssn, age, EmployeeType.CULTIVATOR);
        this.cropsInCare = cropsInCare;
        this.lastHarvestYield = lastHarvestYield;
    }

    public String toString() {
        return String.format("| %-16s | %15s | %11s | $%9.2f | %-3d |\n",
                this.type, this.fullName, this.getSsn(), this.getAnnualSalary(), this.lastHarvestYield);
    }
}
