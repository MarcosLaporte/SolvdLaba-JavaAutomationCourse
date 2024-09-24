package labaFarm.farm.animals;

import labaFarm.farm.Good;
import labaFarm.farm.animals.interfaces.IAnimalFilter;
import labaFarm.farm.animals.interfaces.IMilker;
import labaFarm.farm.animals.interfaces.IShearable;
import labaFarm.farm.exceptions.IncompatibleBreedingException;
import labaFarm.farm.exceptions.UnableToProduceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public final class Goat extends Animal implements IMilker, IShearable {
    public final FurType mohairType;

    public Goat(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, FurType mohairType) {
        super(Species.GOAT, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.mohairType = mohairType;
    }
    private Goat(Integer id, LocalDate dateOfBirth, String food, AnimalSex sex, Float weightInKg, Float heightInCm, FurType mohairType) {
        this(id.intValue(), dateOfBirth, food, sex, weightInKg.floatValue(), heightInCm.floatValue(), mohairType);
    }

    public Goat(List<Animal> existingAnimals, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, FurType mohairType) {
        super(Species.GOAT, existingAnimals, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.mohairType = mohairType;
    }
    private Goat(List<Animal> existingAnimals, LocalDate dateOfBirth, String food, AnimalSex sex, Float weightInKg, Float heightInCm, FurType mohairType) {
        this(existingAnimals, dateOfBirth, food, sex, weightInKg.floatValue(), heightInCm.floatValue(), mohairType);
    }

    @Override
    public Goat breedWith(Animal partner, List<Animal> animalList) throws IncompatibleBreedingException {
        if (!compatibleForBreeding.test(this, partner))
            throw new IncompatibleBreedingException(this, partner);

        Goat father, mother;
        if (this.sex == AnimalSex.M) {
            father = this;
            mother = (Goat) partner;
        } else {
            father = (Goat) partner;
            mother = this;
        }

        final Random RANDOM = new Random();
        AnimalSex sex = RANDOM.nextInt(1, 2) == 1 ? AnimalSex.M : AnimalSex.F;
        float weight = RANDOM.nextFloat(25F, 45F);
        float height = RANDOM.nextFloat(65F, 75F);

        Goat newGoat = new Goat(animalList, LocalDate.now(), "Milk", sex, weight, height, mother.mohairType);
        newGoat.setFather(father);
        newGoat.setMother(mother);

        return newGoat;
    }

    @Override
    public Good milk() throws UnableToProduceException {
        if (sex == AnimalSex.M)
            throw new UnableToProduceException("Billy goats do not produce milk.");

        final Random RANDOM = new Random();
        return new Good(
                "Milk",
                RANDOM.nextFloat(20, 30),
                "Liters",
                Good.GoodsQuality.randomQualitySupplier.get(),
                RANDOM.nextFloat(0.9F, 2.5F)
        );
    }

    @Override
    public Good shear() {
        final Random RANDOM = new Random();
        return new Good(
                this.mohairType + " Mohair",
                RANDOM.nextFloat(2, 5),
                "Kg",
                Good.GoodsQuality.randomQualitySupplier.get(),
                RANDOM.nextFloat(12, 16)
        );
    }


    public static IAnimalFilter<Goat> createFurFilter(FurType mohairType) {
        return goat -> goat.mohairType == mohairType;
    }
}
