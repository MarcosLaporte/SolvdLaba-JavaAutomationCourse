package farm.animals;

public class Cow extends Animal {
    boolean producesMilk;
    public float milkProduction; //Only if producesMilk is true

    public Cow(int age, String food, AnimalSex sex, float weight, float height, boolean producesMilk, float milkProduction) {
        super(age, food, sex, weight, height);
        this.producesMilk = producesMilk;
        this.milkProduction = milkProduction;
    }
    public Cow(int age, String food, AnimalSex sex, float weight, float height, boolean producesMilk) {
        this(age, food, sex, weight, height, producesMilk, producesMilk ? 2500 : 0);
    }

    public Cow(int id, int age, String food, AnimalSex sex, float weight, float height, boolean producesMilk, float milkProduction) {
        super(id, age, food, sex, weight, height);
        this.producesMilk = producesMilk;
        this.milkProduction = milkProduction;
    }
    public Cow(int id, int age, String food, AnimalSex sex, float weight, float height, boolean producesMilk) {
        this(id, age, food, sex, weight, height, producesMilk, producesMilk ? 2500 : 0);
    }
}
