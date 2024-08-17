package farm.animals;

import farm.Good;

import java.time.LocalDate;
import java.util.*;

public abstract class Animal {
    public enum AnimalSex {F, M}
    public enum Species {
        COW, SHEEP, CHICKEN;

        private final int value;
        Species() {
            this.value = this.ordinal()+1;
        }

        public static boolean valueExists(int number){
            for (Species an : Species.values()) {
                if (an.value == number)
                    return true;
            }

            return false;
        }
        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (Species an : Species.values()) {
                sb.append(String.format("%d. %s\n", an.value, an));
            }

            return sb.toString();
        }
    }

    public Species species;
    private final int id;
    private final LocalDate dateOfBirth;
    public String food;
    public AnimalSex sex;
    public float weightInKg;
    public float heightInCm;
    protected static String producedGoods;

    private Animal mother;
    private Animal father;

    public int getId() {
        return id;
    }

    public String getProducedGoods() {
        return producedGoods;
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

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Animal animal = (Animal) obj;

        return id == animal.id;
    }

    public int hashCode() {
        return Objects.hash(this.species, this.id);
    }

    public abstract Animal breed(Animal partner) throws Exception;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected static boolean compatibleForBreeding(Animal x, Animal y) {
        return x.species == y.species && x.sex != y.sex;
    }

    public List<Animal> getChildrenInCrowd(List<Animal> crowd) {
        List<Animal> children = new ArrayList<>();
        for (Animal an : crowd) {
            if (this.equals(an.mother) || this.equals(an.father))
                children.add(an);
        }

        return children;
    }

    public abstract Good produceGoods();
}
