package labaFarm.farm.animals;

import labaFarm.farm.Good;
import labaFarm.farm.animals.interfaces.IAnimalFilter;
import labaFarm.farm.animals.interfaces.IShearable;
import labaFarm.farm.exceptions.IncompatibleBreedingException;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public final class Sheep extends Animal implements IShearable {
    public boolean isTrained;
    public final FurType woolType;

    public Sheep(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained, FurType woolType) {
        super(Species.SHEEP, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isTrained = isTrained;
        this.woolType = woolType;
    }

    public Sheep(List<Animal> existingAnimals, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained, FurType woolType) {
        super(Species.SHEEP, existingAnimals, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isTrained = isTrained;
        this.woolType = woolType;
    }

    @Override
    public Sheep breedWith(Animal partner, List<Animal> animalList) throws IncompatibleBreedingException {
        if (!compatibleForBreeding.test(this, partner))
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

        Sheep newSheep = new Sheep(animalList, LocalDate.now(), "Milk", sex, weight, height, false, mother.woolType);
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
                Good.GoodsQuality.randomQualitySupplier.get(),
                LocalDate.now(),
                RANDOM.nextFloat(800, 1750)
        );
    }


    public static IAnimalFilter<Sheep> createFurFilter(FurType woolType) {
        return sheep -> sheep.woolType == woolType;
    }
}
