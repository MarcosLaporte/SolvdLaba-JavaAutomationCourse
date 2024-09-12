package labaFarm.farm.animals;

import labaFarm.farm.exceptions.IncompatibleBreedingException;

import java.time.LocalDate;
import java.util.Random;

public final class Horse extends Animal {
    public boolean isForCompetition;
    public float speed;
    public boolean isRideable;

    public Horse(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isForCompetition, float speed, boolean isRideable) {
        super(Species.HORSE, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isForCompetition = isForCompetition;
        this.speed = speed;
        this.isRideable = isRideable;
    }

    public Horse(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isForCompetition, float speed) {
        this(dateOfBirth, food, sex, weightInKg, heightInCm, isForCompetition, speed, false);
    }

    public Horse(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isForCompetition, float speed, boolean isRideable) {
        super(Species.HORSE, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isForCompetition = isForCompetition;
        this.speed = speed;
        this.isRideable = isRideable;
    }

    public Horse(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isForCompetition, float speed) {
        this(id, dateOfBirth, food, sex, weightInKg, heightInCm, isForCompetition, speed, false);
    }

    @Override
    public Animal breedWith(Animal partner) throws IncompatibleBreedingException {
        if (!compatibleForBreeding.test(this, partner))
            throw new IncompatibleBreedingException(this, partner);

        Horse father, mother;
        if (this.sex == AnimalSex.M) {
            father = this;
            mother = (Horse) partner;
        } else {
            father = (Horse) partner;
            mother = this;
        }

        final Random RANDOM = new Random();
        AnimalSex sex = RANDOM.nextInt(1, 2) == 1 ? AnimalSex.M : AnimalSex.F;
        float weight = RANDOM.nextFloat(40, 60);
        float height = RANDOM.nextFloat(85, 100);
        float speed = (father.speed + mother.speed) / 2;

        Horse newHorse = new Horse(LocalDate.now(), "Milk", sex, weight, height, false, speed);
        newHorse.setFather(father);
        newHorse.setMother(mother);

        return newHorse;
    }
}
