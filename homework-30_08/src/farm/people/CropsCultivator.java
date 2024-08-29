package farm.people;

import farm.crops.CropSector;

import java.util.Queue;
import java.util.Set;

public class CropsCultivator extends Employee {
    public Set<CropSector> cropsInCare;

    public CropsCultivator(String fullName, String ssn, int age, double annualSalary, Queue<WorkShift> shift, Set<CropSector> cropsInCare) {
        super(fullName, ssn, age, annualSalary, shift, EmployeeType.CULTIVATOR);
        this.cropsInCare = cropsInCare;
    }

    public CropsCultivator(String fullName, String ssn, int age, Queue<WorkShift> shift, Set<CropSector> cropsInCare) {
        super(fullName, ssn, age, shift, EmployeeType.CULTIVATOR);
        this.cropsInCare = cropsInCare;
    }

    public CropsCultivator(String fullName, String ssn, int age, double annualSalary, Set<CropSector> cropsInCare) {
        super(fullName, ssn, age, annualSalary, EmployeeType.CULTIVATOR);
        this.cropsInCare = cropsInCare;
    }

    public CropsCultivator(String fullName, String ssn, int age, Set<CropSector> cropsInCare) {
        super(fullName, ssn, age, EmployeeType.CULTIVATOR);
        this.cropsInCare = cropsInCare;
    }

    @Override
    public String toString() {
        return String.format("| %-16s | %15s | %11s | $%9.2f |\n",
                this.type, this.fullName, this.getSsn(), this.getAnnualSalary());
    }
}
