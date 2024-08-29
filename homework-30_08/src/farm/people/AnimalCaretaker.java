package farm.people;

import farm.animals.Animal;

import java.util.Queue;
import java.util.TreeSet;

public class AnimalCaretaker extends Employee {
    public TreeSet<Animal> animalsInCare;
    public boolean isVetCertified;

    public AnimalCaretaker(String fullName, String ssn, int age, double annualSalary, Queue<WorkShift> shift, TreeSet<Animal> animalsInCare, boolean isVetCertified) {
        super(fullName, ssn, age, annualSalary, shift, EmployeeType.ANIMAL_CARETAKER);
        this.animalsInCare = animalsInCare;
        this.isVetCertified = isVetCertified;
    }

    public AnimalCaretaker(String fullName, String ssn, int age, Queue<WorkShift> shift, TreeSet<Animal> animalsInCare, boolean isVetCertified) {
        super(fullName, ssn, age, shift, EmployeeType.ANIMAL_CARETAKER);
        this.animalsInCare = animalsInCare;
        this.isVetCertified = isVetCertified;
    }

    public AnimalCaretaker(String fullName, String ssn, int age, double annualSalary, TreeSet<Animal> animalsInCare, boolean isVetCertified) {
        super(fullName, ssn, age, annualSalary, EmployeeType.ANIMAL_CARETAKER);
        this.animalsInCare = animalsInCare;
        this.isVetCertified = isVetCertified;
    }

    public AnimalCaretaker(String fullName, String ssn, int age, TreeSet<Animal> animalsInCare, boolean isVetCertified) {
        super(fullName, ssn, age, EmployeeType.ANIMAL_CARETAKER);
        this.animalsInCare = animalsInCare;
        this.isVetCertified = isVetCertified;
    }

    @Override
    public String toString() {
        return String.format("| %-16s | %15s | %11s | $%9.2f |  %c  |\n",
                this.type, this.fullName, this.getSsn(), this.getAnnualSalary(), this.isVetCertified ? 'Y' : 'N');
    }

}
