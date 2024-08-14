package farm.people;

import farm.animals.Animal;

import java.util.Iterator;
import java.util.List;

public class AnimalCaretaker extends Employee {
    public List<Animal> animalsInCare;
    public boolean isVetCertified;

    public AnimalCaretaker(String fullName, String ssn, int age, double annualSalary, WorkShift shift, List<Animal> animalsInCare, boolean isVetCertified) {
        super(fullName, ssn, age, annualSalary, shift, EmployeeType.ANIMAL_CARETAKER);
        this.animalsInCare = animalsInCare;
        this.isVetCertified = isVetCertified;
    }

    public AnimalCaretaker(String fullName, String ssn, int age, WorkShift shift, List<Animal> animalsInCare, boolean isVetCertified) {
        super(fullName, ssn, age, shift, EmployeeType.ANIMAL_CARETAKER);
        this.animalsInCare = animalsInCare;
        this.isVetCertified = isVetCertified;
    }

    public AnimalCaretaker(String fullName, String ssn, int age, double annualSalary, List<Animal> animalsInCare, boolean isVetCertified) {
        super(fullName, ssn, age, annualSalary, WorkShift.MORNING, EmployeeType.ANIMAL_CARETAKER);
        this.animalsInCare = animalsInCare;
        this.isVetCertified = isVetCertified;
    }

    public AnimalCaretaker(String fullName, String ssn, int age, List<Animal> animalsInCare, boolean isVetCertified) {
        super(fullName, ssn, age, WorkShift.MORNING, EmployeeType.ANIMAL_CARETAKER);
        this.animalsInCare = animalsInCare;
        this.isVetCertified = isVetCertified;
    }

    public String toString() {
        return String.format("| %-16s | %15s | %11s | $%9.2f |  %c  |\n",
                this.type, this.fullName, this.getSsn(), this.getAnnualSalary(), this.isVetCertified ? 'Y' : 'N');
    }

    public String animalsToString() {
        Iterator<Animal> it = this.animalsInCare.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(this);
        sb.append("+------+-----+------------+-----+--------+--------+\n");
        sb.append(String.format("| %-4s | %-3s | %-10s | %3s | %-6s | %-6s |\n",
                "ID", "AGE", "FOOD", "SEX", "WEIGHT", "HEIGHT"));
        sb.append("+------+-----+------------+-----+--------+--------+\n");
        while (it.hasNext()) {
            sb.append(it.next());
        }
        sb.append("+------+-----+------------+-----+--------+--------+\n");

        return sb.toString();
    }
}
