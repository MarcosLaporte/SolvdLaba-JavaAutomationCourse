package farm.people;

import farm.animals.Animal;

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

}
