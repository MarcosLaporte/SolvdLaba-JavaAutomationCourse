package labaFarm.farm.animals;

import labaFarm.farm.Good;
import labaFarm.farm.animals.interfaces.IAnimalFilter;
import labaFarm.farm.animals.interfaces.IMilker;
import labaFarm.farm.exceptions.IncompatibleBreedingException;
import labaFarm.farm.exceptions.UnableToProduceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public final class Cattle extends Animal implements IMilker {
    public enum CattleBreed {
        HOLSTEIN, JERSEY, ANGUS, HEREFORD, BROWN_SWISS;

        private final int value;
        CattleBreed() {
            this.value = this.ordinal()+1;
        }

        public static CattleBreed getCattleBreed(int value) {
            for (CattleBreed cb : CattleBreed.values()) {
                if (cb.value == value)
                    return cb;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (CattleBreed wt : CattleBreed.values()) {
                sb.append(String.format("%d. %s\n", wt.value, wt));
            }

            return sb.toString();
        }
    }
    public final CattleBreed breed;

    public Cattle(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, CattleBreed breed) {
        super(Species.CATTLE, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.breed = breed;
    }

    public Cattle(List<Animal> existingAnimals, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, CattleBreed breed) {
        super(Species.CATTLE, existingAnimals, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.breed = breed;
    }

    @Override
    public Cattle breedWith(Animal partner, List<Animal> animalList) throws IncompatibleBreedingException {
        if (!compatibleForBreeding.test(this, partner))
            throw new IncompatibleBreedingException(this, partner);

        Cattle father, mother;
        if (this.sex == AnimalSex.M) {
            father = this;
            mother = (Cattle) partner;
        } else {
            father = (Cattle) partner;
            mother = this;
        }

        final Random RANDOM = new Random();
        AnimalSex sex = RANDOM.nextInt(1, 2) == 1 ? AnimalSex.M : AnimalSex.F;
        float weight = RANDOM.nextFloat(25F, 45F);
        float height = RANDOM.nextFloat(65F, 75F);
        CattleBreed breed = RANDOM.nextInt(1, 2) == 1 ? father.breed : mother.breed;

        Cattle newCattle = new Cattle(animalList, LocalDate.now(), "Milk", sex, weight, height, breed);
        newCattle.setFather(father);
        newCattle.setMother(mother);

        return newCattle;
    }

    @Override
    public Good milk() throws UnableToProduceException {
        if (sex == AnimalSex.M)
            throw new UnableToProduceException("Bulls do not produce milk.");

        final Random RANDOM = new Random();
        return new Good(
                "Milk",
                RANDOM.nextFloat(20, 30),
                "Liters",
                Good.GoodsQuality.randomQualitySupplier.get(),
                RANDOM.nextFloat(0.9F, 2.5F)
        );
    }

    public transient IAnimalFilter<Cattle> angusFilter = cow -> cow.breed == CattleBreed.ANGUS;

}
