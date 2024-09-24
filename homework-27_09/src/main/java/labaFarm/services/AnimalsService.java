package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.animals.*;
import labaFarm.farm.animals.interfaces.IEggLayer;
import labaFarm.farm.animals.interfaces.IShearable;
import labaFarm.farm.exceptions.IncompatibleBreedingException;
import labaFarm.farm.exceptions.RepeatedInstanceException;
import org.apache.logging.log4j.Level;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static labaFarm.services.ReflectionService.ClassExclusionPredicate;

public class AnimalsService {
    public static void createNewAnimal(Farm farm) {
        List<Class<? extends Animal>> animalsClasses = new ArrayList<>();

        try {
            animalsClasses = ReflectionService.getSubclassesOf(Animal.class, "labaFarm.farm.animals", ClassExclusionPredicate.ABSTRACT);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sb = new StringBuilder(" - GO BACK (0)\n");
        Map<String, Class<? extends Animal>> classesMap = new HashMap<>();
        for (Class<? extends Animal> clazz : animalsClasses) {
            classesMap.put((clazz.getSimpleName()).toLowerCase(), clazz);
            sb.append(" - ").append(clazz.getSimpleName()).append("\n");
        }

        System.out.println(sb);
        Class<? extends Animal> clazz;
        do {
            String input = InputService.readString("Select a class by entering its name: ", 1, 255);
            if (input.equals("0")) {
                System.out.println("Back to main menu...");
                return;
            }

            clazz = classesMap.get(input.toLowerCase());
            if (clazz == null)
                System.out.println("This animal does not exist. Try again.");
        } while (clazz == null);


        try {
            Animal newAnimal = initAnimal(farm.animals, clazz);

            farm.addAnimal(newAnimal);
            System.out.println(newAnimal.species + " created successfully!");
        } catch (RepeatedInstanceException e) {
            LoggerService.log(Level.WARN, e.getMessage());
        } catch (Exception e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }
    }

    private static Animal initAnimal(List<Animal> existingAnimals, Class<? extends Animal> animalClass) throws Exception {
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

        ReflectionService<? extends Animal> rs = new ReflectionService<>(animalClass);
        Animal newAnimal;
        if (animalClass == Cattle.class) {
            int breedVal = InputService.readInt(
                    Cattle.CattleBreed.getAll() + "Select cattle breed: ",
                    "This option does not exist. Try again: ",
                    1, Cattle.CattleBreed.values().length
            );
            Cattle.CattleBreed auxCattleBreed = Cattle.CattleBreed.getCattleBreed(breedVal);

            newAnimal = rs.createInstance(existingAnimals, auxDate, auxFood, auxSex, auxWeight, auxHeight, auxCattleBreed);
        } else if (animalClass == Sheep.class) {
            char charIsTrained = InputService.readCharInValues(
                    "Is it trained? Y/N ",
                    "This option does not exist. Try again: ",
                    new char[]{'Y', 'N'}
            );
            boolean auxIsTrained = charIsTrained == 'Y';

            int woolTypeVal = InputService.readInt(
                    IShearable.FurType.getAll() + "Select fur type: ",
                    "This option does not exist. Try again: ",
                    1, IShearable.FurType.values().length
            );
            IShearable.FurType auxWoolType = IShearable.FurType.getFurType(woolTypeVal);

            newAnimal = rs.createInstance(existingAnimals, auxDate, auxFood, auxSex, auxWeight, auxHeight, auxIsTrained, auxWoolType);
        } else if (animalClass == Chicken.class) {
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

            newAnimal = rs.createInstance(existingAnimals, auxDate, auxFood, auxSex, auxWeight, auxHeight, auxEggAmount, auxEggSize, auxCoopLoc);
        } else if (animalClass == Goat.class) {
            int furTypeVal = InputService.readInt(
                    IShearable.FurType.getAll() + "Select mohair type: ",
                    "This option does not exist. Try again: ",
                    1, IShearable.FurType.values().length
            );
            IShearable.FurType auxFurType = IShearable.FurType.getFurType(furTypeVal);

            newAnimal = rs.createInstance(existingAnimals, auxDate, auxFood, auxSex, auxWeight, auxHeight, auxFurType);
        } else if (animalClass == Horse.class) {
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

            newAnimal = rs.createInstance(existingAnimals, auxDate, auxFood, auxSex, auxWeight, auxHeight, auxIsForCompetition, auxSpeed, auxIsRideable);
        } else {
            throw new Exception("This class does not extend class Animal.");
        }

        return newAnimal;
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
