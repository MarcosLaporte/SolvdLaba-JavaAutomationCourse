package labaFarm.farm.animals;

import labaFarm.farm.Good;
import labaFarm.farm.animals.interfaces.IShearable;
import labaFarm.farm.exceptions.IncompatibleBreedingException;

import java.time.LocalDate;
import java.util.Random;

public final class Sheep extends Animal implements IShearable {
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
    public Sheep breedWith(Animal partner) throws IncompatibleBreedingException {
        if (!compatibleForBreeding(this, partner))
            throw new IncompatibleBreedingException(this, partner);

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
