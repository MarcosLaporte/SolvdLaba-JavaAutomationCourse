package farm.animals;

import farm.Good;

import java.time.LocalDate;
import java.util.Random;

public class Cow extends Animal {
    public boolean producesMilk;
    public float milkProduction; //Only if producesMilk is true
    static {
        producedGoods = "MILK";
    }

    public Cow(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean producesMilk, float milkProduction) {
        super(Species.COW, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.producesMilk = sex == AnimalSex.F && producesMilk;
        this.milkProduction = producesMilk ? milkProduction : 0;
    }
    public Cow(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean producesMilk) {
        this(dateOfBirth, food, sex, weightInKg, heightInCm, producesMilk, 2500);
    }

    public Cow(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean producesMilk, float milkProduction) {
        super(Species.COW, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.producesMilk = sex == AnimalSex.F && producesMilk;
        this.milkProduction = producesMilk ? milkProduction : 0;
    }
    public Cow(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean producesMilk) {
        this(id, dateOfBirth, food, sex, weightInKg, heightInCm, producesMilk, 2500);
    }

    @Override
    public Cow breed(Animal partner) throws Exception {
        if (!Animal.compatibleForBreeding(this, partner))
            throw new Exception(
                String.format("Can't breed %s-%s with %s-%s", this.species, this.sex, partner.species, partner.sex)
            );

        Cow father, mother;
        if (this.sex == AnimalSex.M) {
            father = this;
            mother = (Cow) partner;
        } else {
            father = (Cow) partner;
            mother = this;
        }

        final Random RANDOM = new Random();
        AnimalSex sex = RANDOM.nextInt(1, 2) == 1 ? AnimalSex.M : AnimalSex.F;
        float weight = RANDOM.nextFloat(25F, 45F);
        float height = RANDOM.nextFloat(65F, 75F);
        boolean producesMilk = sex == AnimalSex.F && mother.producesMilk;
        float milkProduction = (father.milkProduction + mother.milkProduction) / 2;

        Cow newCow = new Cow(LocalDate.now(), "Milk", sex, weight, height, producesMilk, milkProduction);
        newCow.setFather(father);
        newCow.setMother(mother);

        return newCow;
    }

    @Override
    public Good produceGoods() {
        final Random RANDOM = new Random();

        return new Good(
                this.getProducedGoods(),
                RANDOM.nextFloat(20, 30),
                "Liters",
                Good.GoodsQuality.getRandomQuality(),
                RANDOM.nextFloat(0.9F, 2.5F)
        );
    }
}
