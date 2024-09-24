package labaFarm.farm.people;

import java.util.Queue;
import java.util.Set;

public class CropsCultivator extends Employee {
//    public Set<CropSector> cropsInCare;
    public Set<Integer> idCropsInCare;

    public CropsCultivator(String fullName, String ssn, int age, double annualSalary, Queue<WorkShift> shift, Set<Integer> idCropsInCare) {
        super(fullName, ssn, age, annualSalary, shift, EmployeeType.CULTIVATOR);
        this.idCropsInCare = idCropsInCare;
    }

    public CropsCultivator(String fullName, String ssn, int age, Queue<WorkShift> shift, Set<Integer> idCropsInCare) {
        super(fullName, ssn, age, shift, EmployeeType.CULTIVATOR);
        this.idCropsInCare = idCropsInCare;
    }

    public CropsCultivator(String fullName, String ssn, int age, double annualSalary, Set<Integer> idCropsInCare) {
        super(fullName, ssn, age, annualSalary, EmployeeType.CULTIVATOR);
        this.idCropsInCare = idCropsInCare;
    }

    public CropsCultivator(String fullName, String ssn, int age, Set<Integer> idCropsInCare) {
        super(fullName, ssn, age, EmployeeType.CULTIVATOR);
        this.idCropsInCare = idCropsInCare;
    }

    @Override
    public String toString() {
        return String.format("| %-16s | %15s | %11s | $%9.2f |\n",
                this.type, this.fullName, this.getSsn(), this.getAnnualSalary());
    }
}
