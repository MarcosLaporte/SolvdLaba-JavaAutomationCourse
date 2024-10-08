package farm.animals;

import farm.Good;
import farm.animals.interfaces.Shearable;

import java.time.LocalDate;
import java.util.Random;

public final class Sheep extends Animal implements Shearable {
    public boolean isTrained;
    public final FurType woolType;

    public Sheep(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained, FurType woolType) {
        super(Species.SHEEP, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isTrained = isTrained;
        this.woolType = woolType;
    }
    public Sheep(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained) {
        this(dateOfBirth, food, sex, weightInKg, heightInCm, isTrained, FurType.FINE);
    }

    public Sheep(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained, FurType woolType) {
        super(Species.SHEEP, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isTrained = isTrained;
        this.woolType = woolType;
    }
    public Sheep(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained) {
        this(id, dateOfBirth, food, sex, weightInKg, heightInCm, isTrained, FurType.FINE);
    }

    @Override
    public Sheep breedWith(Animal partner) throws Exception {
        if (!Animal.compatibleForBreeding(this, partner))
            throw new Exception(
                String.format("Can't breed %s-%s with %s-%s", this.species, this.sex, partner.species, partner.sex)
            );

        Sheep father, mother;
        if (this.sex == AnimalSex.M) {
            father = this;
            mother = (Sheep) partner;
        } else {
            father = (Sheep) partner;
            mother = this;
        }

        final Random RANDOM = new Random();
        AnimalSex sex = RANDOM.nextInt(1, 2) == 1 ? AnimalSex.M : AnimalSex.F;
        float weight = RANDOM.nextFloat(1F, 1.8F);
        float height = RANDOM.nextFloat(20F, 30F);

        Sheep newSheep = new Sheep(LocalDate.now(), "Milk", sex, weight, height, false, mother.woolType);
        newSheep.setFather(father);
        newSheep.setMother(mother);

        return newSheep;
    }

    @Override
    public Good shear() {
        final Random RANDOM = new Random();

        return new Good(
                this.woolType + " Wool",
                RANDOM.nextFloat(2, 4),
                "Kg",
                Good.GoodsQuality.getRandomQuality(),
                LocalDate.now(),
                RANDOM.nextFloat(800, 1750)
        );
    }
}
