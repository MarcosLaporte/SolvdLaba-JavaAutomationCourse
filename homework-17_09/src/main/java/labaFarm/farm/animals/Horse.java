package labaFarm.farm.animals;

import labaFarm.farm.animals.interfaces.IAnimalFilter;
import labaFarm.farm.exceptions.IncompatibleBreedingException;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public final class Horse extends Animal {
    public boolean isForCompetition;
    public float speed;
    public boolean isRideable;

    public Horse(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isForCompetition, float speed, boolean isRideable) {
        super(Species.HORSE, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isForCompetition = isForCompetition;
        this.speed = speed;
        this.isRideable = isRideable;
    }

    public Horse(List<Animal> existingAnimals, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isForCompetition, float speed, boolean isRideable) {
        super(Species.HORSE, existingAnimals, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isForCompetition = isForCompetition;
        this.speed = speed;
        this.isRideable = isRideable;
    }

    @Override
    public Animal breedWith(Animal partner, List<Animal> animalList) throws IncompatibleBreedingException {
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

        Horse newHorse = new Horse(animalList, LocalDate.now(), "Milk", sex, weight, height, false, speed, false);
        newHorse.setFather(father);
        newHorse.setMother(mother);

        return newHorse;
    }

    public static IAnimalFilter<Horse> competitionFilter = horse -> horse.isForCompetition;

}
