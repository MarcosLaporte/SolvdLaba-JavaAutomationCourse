package labaFarm.farm.animals;

import labaFarm.farm.IIdentifiable;
import labaFarm.farm.animals.interfaces.IAnimalFilter;
import labaFarm.farm.exceptions.IncompatibleBreedingException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public abstract class Animal implements IIdentifiable<Animal> {
    public enum AnimalSex {F, M}

    public enum Species {
        CATTLE, SHEEP, CHICKEN, HORSE, GOAT;

        private final int value;

        Species() {
            this.value = this.ordinal() + 1;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (Species an : Species.values()) {
                sb.append(String.format("%d. %s\n", an.value, an));
            }

            return sb.toString();
        }
    }

    public final Species species;
    private int id;
    private final LocalDate dateOfBirth;
    public String food;
    public final AnimalSex sex;
    public float weightInKg;
    public float heightInCm;

    private Animal mother;
    private Animal father;

    public int getId() { return id; }

    public long getAge() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Animal getMother() {
        return mother;
    }
    protected void setMother(Animal mother) {
        this.mother = mother;
    }

    public Animal getFather() {
        return father;
    }
    protected void setFather(Animal father) {
        this.father = father;
    }

    public Animal(Species species, int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm) {
        this.species = species;
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.food = food;
        this.sex = sex;
        this.weightInKg = Math.abs(weightInKg);
        this.heightInCm = Math.abs(heightInCm);
    }
    private Animal(Species species, Integer id, LocalDate dateOfBirth, String food, AnimalSex sex, Float weightInKg, Float heightInCm) {
        this(species, id.intValue(), dateOfBirth, food, sex, weightInKg.floatValue(), heightInCm.floatValue());
    }

    @Override
    public int getNewId(List<Animal> existingObjects) {
        Stream<Animal> sameCropTypeStream = existingObjects.stream().filter(c -> c.species == this.species);
        Optional<Animal> maxIdCrop = sameCropTypeStream.max(Comparator.comparingInt(cr -> cr.id));

        return maxIdCrop.isPresent() ? maxIdCrop.get().id + 1 : this.species.value * 1000 + 1;
    }

    public Animal(Species species, List<Animal> existingAnimals, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm) {
        this(species, -1, dateOfBirth, food, sex, weightInKg, heightInCm);

        this.id = this.getNewId(existingAnimals);
    }
    private Animal(Species species, List<Animal> existingAnimals, LocalDate dateOfBirth, String food, AnimalSex sex, Float weightInKg, Float heightInCm) {
        this(species, existingAnimals, dateOfBirth, food, sex, weightInKg.floatValue(), heightInCm.floatValue());
    }


    @Override
    public String toString() {
        return "Species: " + this.species + "\n" +
                "ID: " + this.id + "\n" +
                "LocalDate of birth: " + this.dateOfBirth + "\n" +
                "Food: " + this.food + "\n" +
                "Sex: " + this.sex + "\n" +
                "Weight: " + this.weightInKg + "kg\n" +
                "heightInCm: " + this.heightInCm + "m\n";
    }

    public static String toString(List<Animal> list) {
        if (list.isEmpty()) return "No animals in the list.\n";
        StringBuilder sb = new StringBuilder();

        for (Animal an : list) {
            sb.append(an);
            sb.append("-----------------------------------\n");
        }

        return sb.toString();
    }

    public static String toTable(List<Animal> list) {
        if (list.isEmpty()) return "No animals in the list.\n";
        StringBuilder sb = new StringBuilder();

        sb.append("+---------+------+-----+------------+-----+-----------+-----------+\n");
        sb.append(String.format("| %s | %s | %s | %s | %s | %s | %s |\n",
                StringUtils.center("SPECIES", 7),
                StringUtils.center("ID", 4),
                StringUtils.center("AGE", 3),
                StringUtils.center("FOOD", 10),
                StringUtils.center("SEX", 3),
                StringUtils.center("WEIGHT", 9),
                StringUtils.center("HEIGHT", 9)));
        sb.append("+---------+------+-----+------------+-----+-----------+-----------+\n");
        for (Animal an : list) {
            sb.append(String.format("| %-7s | %-4d | %-3d | %-10s | %-3s | %-6.2f kg | %-6.2f cm |\n",
                    an.species, an.id, an.getAge(), an.food, an.sex, an.weightInKg, an.heightInCm));
            sb.append("+---------+------+-----+------------+-----+-----------+-----------+\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Animal animal = (Animal) obj;

        return id == animal.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.species, this.id);
    }

    public abstract Animal breedWith(Animal partner, List<Animal> animalList) throws IncompatibleBreedingException;

    protected transient BiPredicate<Animal, Animal> compatibleForBreeding = (x, y) -> x.species == y.species && x.sex != y.sex;

    public final List<Animal> getChildrenInCrowd(List<Animal> crowd) {
        List<Animal> children = new ArrayList<>();
        for (Animal an : crowd) {
            if (this.equals(an.mother) || this.equals(an.father))
                children.add(an);
        }

        return children;
    }

    public transient BiFunction<Animal, Animal, Integer> compareTo = (x, y) -> x.id - y.id;
    public transient BiPredicate<Float, Float> validWeight = (from, to) -> this.weightInKg >= from && this.weightInKg <= to;
    public transient BiPredicate<Float, Float> validHeight = (from, to) -> this.heightInCm >= from && this.heightInCm <= to;

    public transient IAnimalFilter<Animal> femaleFilter = animal -> animal.sex == AnimalSex.F;
    public transient IAnimalFilter<Animal> maleFilter = animal -> animal.sex == AnimalSex.M;

}
