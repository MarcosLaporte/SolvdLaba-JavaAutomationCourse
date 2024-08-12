package farm.people;

import farm.animals.Animal;

import java.util.Iterator;
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

    public String animalsToString() {
        Iterator<Animal> it = this.animalsInCare.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
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
