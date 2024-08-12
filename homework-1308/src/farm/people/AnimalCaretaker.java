package farm.people;

import farm.animals.Animal;

import java.util.List;

public class AnimalCaretaker extends Farmer {
    public List<Animal> animalsInCare;
    public FarmerShift shift;

    public AnimalCaretaker(String fullName, String ssn, float annualSalary, List<Animal> animalsInCare, FarmerShift shift) {
        super(fullName, ssn, annualSalary);
        this.type = FarmerType.ANIMAL_CARETAKER ;
        this.animalsInCare = animalsInCare;
        this.shift = shift;
    }

    public AnimalCaretaker(String fullName, String ssn, List<Animal> animalsInCare, FarmerShift shift) {
        super(fullName, ssn);
        this.type = FarmerType.ANIMAL_CARETAKER;
        this.animalsInCare = animalsInCare;
        this.shift = shift;
    }

    public AnimalCaretaker(String fullName, String ssn, float salary, List<Animal> animalsInCare) {
        this(fullName, ssn, salary, animalsInCare, FarmerShift.MORNING);
    }

    public AnimalCaretaker(String fullName, String ssn, List<Animal> animalsInCare) {
        this(fullName, ssn, animalsInCare, FarmerShift.MORNING);
    }
}
