package labaFarm.farm.animals;

import labaFarm.farm.exceptions.IncompatibleBreedingException;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Animal {
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
    private final int id;
    private final LocalDate dateOfBirth;
    public String food;
    public final AnimalSex sex;
    public float weightInKg;
    public float heightInCm;

    private Animal mother;
    private Animal father;

    public int getId() {
        return id;
    }

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

    public Animal(Species species, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm) {
        this(species, new Random().nextInt(1, 9999), dateOfBirth, food, sex, weightInKg, heightInCm);
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
        sb.append(String.format("| %-7s | %-4s | %-3s | %-10s | %3s | %-9s | %-9s |\n",
                "SPECIES", "ID", "AGE", "FOOD", "SEX", "WEIGHT", "HEIGHT"));
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

    public abstract Animal breedWith(Animal partner) throws IncompatibleBreedingException;

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
}
