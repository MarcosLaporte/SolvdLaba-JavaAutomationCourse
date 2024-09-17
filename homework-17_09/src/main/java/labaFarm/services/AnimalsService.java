package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.animals.*;
import labaFarm.farm.animals.interfaces.IEggLayer;
import labaFarm.farm.animals.interfaces.IShearable;
import labaFarm.farm.exceptions.IncompatibleBreedingException;
import labaFarm.farm.exceptions.RepeatedInstanceException;
import org.apache.logging.log4j.Level;

import java.time.LocalDate;
import java.util.List;

public class AnimalsService {
    public static void createNewAnimal(Farm farm) {
        int innerMenuOption = InputService.readInt(
                "\nCreate a new instance of which animal?\n0. GO BACK\n" + Animal.Species.getAll() + "Choose an option: ",
                "This option does not exist. Try again: ",
                0, Animal.Species.values().length
        );

        if (innerMenuOption == 0) {
            System.out.println("Back to main menu...");
            return;
        }

        Animal newAnimal = switch (Animal.Species.values()[innerMenuOption-1]) {
            case CATTLE -> initCattle(farm.animals);
            case SHEEP -> initSheep(farm.animals);
            case CHICKEN -> initChicken(farm.animals);
            case GOAT -> initGoat(farm.animals);
            case HORSE -> initHorse(farm.animals);
        };

        try {
            farm.addAnimal(newAnimal);
            System.out.println(newAnimal.species + " created successfully!");
        } catch (RepeatedInstanceException e) {
            LoggerService.log(Level.WARN, e.getMessage());
        }
    }

    private record AnimalData(
            List<Animal> existingAnimals,
            LocalDate dateOfBirth,
            String food,
            Animal.AnimalSex sex,
            float weightInKg,
            float heightInCm
    ) {
    }

    private static AnimalData initAnimal(List<Animal> existingAnimals) {
        System.out.println("Fill with animal's date of birth.");
        LocalDate auxDate = InputService.readValidDate();

        String auxFood = InputService.readString("What does it eat? ", 1, 10);

        char sexChar = InputService.readCharInValues(
                "Enter animal sex (F/M): ",
                "This option does not exist. Try again: ",
                new char[]{'F', 'M'}
        );
        Animal.AnimalSex auxSex = Animal.AnimalSex.valueOf(String.valueOf(sexChar));

        float auxWeight = InputService.readFloat(
                "Enter weight in Kg: ",
                "Enter a valid value (0.001 - 99,999): ",
                0.001F, 99_999
        );

        float auxHeight = InputService.readFloat(
                "Enter height in cm: ",
                "Enter a valid value (0.1 - 9,999): ",
                0.1F, 9_999
        );

        return new AnimalData(existingAnimals, auxDate, auxFood, auxSex, auxWeight, auxHeight);
    }

    public static Cattle initCattle(List<Animal> existingAnimals) {
        System.out.println("Let's create cattle!");
        AnimalData animalData = initAnimal(existingAnimals);

        int breedVal = InputService.readInt(
                Cattle.CattleBreed.getAll() + "Select cattle breed: ",
                "This option does not exist. Try again: ",
                1, Cattle.CattleBreed.values().length
        );
        Cattle.CattleBreed auxCattleBreed = Cattle.CattleBreed.getCattleBreed(breedVal);

        return new Cattle(existingAnimals, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxCattleBreed);
    }

    public static Sheep initSheep(List<Animal> existingAnimals) {
        System.out.println("Let's create a sheep!");
        AnimalData animalData = initAnimal(existingAnimals);

        char charIsTrained = InputService.readCharInValues(
                "Is it trained? Y/N ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        );
        boolean auxIsTrained = charIsTrained == 'Y';

        int furTypeVal = InputService.readInt(
                IShearable.FurType.getAll() + "Select fur type: ",
                "This option does not exist. Try again: ",
                1, IShearable.FurType.values().length
        );
        IShearable.FurType auxFurType = IShearable.FurType.getFurType(furTypeVal);

        return new Sheep(existingAnimals, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxIsTrained, auxFurType);
    }

    public static Chicken initChicken(List<Animal> existingAnimals) {
        System.out.println("Let's create a chicken!");
        AnimalData animalData = initAnimal(existingAnimals);

        int auxEggAmount = InputService.readInt(
                "Enter amount of eggs per day: ",
                "Invalid value. Try Again: ",
                0, 50
        );

        int eggSizeVal = InputService.readInt(
                IEggLayer.EggSize.getAll() + "Select egg size: ",
                "Invalid value. Try again: ",
                1, IEggLayer.EggSize.values().length
        );
        IEggLayer.EggSize auxEggSize = IEggLayer.EggSize.getEggSize(eggSizeVal);

        int coopLocVal = InputService.readInt(
                Chicken.CoopLocation.getAll() + "Select coop location: ",
                "Invalid value. Try again: ",
                1, Chicken.CoopLocation.values().length
        );
        Chicken.CoopLocation auxCoopLoc = Chicken.CoopLocation.getCoopLocation(coopLocVal);

        return new Chicken(existingAnimals, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxEggAmount, auxEggSize, auxCoopLoc);
    }

    public static Goat initGoat(List<Animal> existingAnimals) {
        System.out.println("Let's create a goat!");
        AnimalData animalData = initAnimal(existingAnimals);

        int furTypeVal = InputService.readInt(
                IShearable.FurType.getAll() + "Select mohair type: ",
                "This option does not exist. Try again: ",
                1, IShearable.FurType.values().length
        );
        IShearable.FurType auxFurType = IShearable.FurType.getFurType(furTypeVal);

        return new Goat(existingAnimals, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxFurType);
    }

    public static Horse initHorse(List<Animal> existingAnimals) {
        System.out.println("Let's create a horse!");
        AnimalData animalData = initAnimal(existingAnimals);

        char charIsForCompetition = InputService.readCharInValues(
                "Is it trained for competition? Y/N ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        );
        boolean auxIsForCompetition = charIsForCompetition == 'Y';

        float auxSpeed = InputService.readFloat(
                "Enter average speed (km/h): ",
                "Invalid value. Try again (10-65): ",
                10, 65
        );

        char charIsRideable = InputService.readCharInValues(
                "Allows people to ride it? Y/N ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        );
        boolean auxIsRideable = charIsRideable == 'Y';

        return new Horse(existingAnimals, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxIsForCompetition, auxSpeed, auxIsRideable);
    }

    public static void breedAnimals(Farm farm) {
        Animal animal1 = MenuService.chooseAnimal(farm.animals);
        if (animal1 == null) {
            System.out.println("Back to main menu...");
            return;
        }
        Animal animal2 = MenuService.chooseAnimal(farm.animals);
        if (animal2 == null) {
            System.out.println("Back to main menu...");
            return;
        }

        try {
            Animal babyAnimal = animal1.breedWith(animal2, farm.animals);
            farm.addAnimal(babyAnimal);
            System.out.println(babyAnimal.species + " was born! Information:");
            System.out.println(babyAnimal);
        } catch (IncompatibleBreedingException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            System.out.println("Back to main menu...");
        }
    }
}
