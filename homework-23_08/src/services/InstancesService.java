package services;

import farm.Farm;
import farm.animals.*;
import farm.animals.interfaces.Shearable;
import farm.crops.*;

import java.time.LocalDate;

public class InstancesService {
    /*---------------FARM---------------*/

    //TODO: initFarm
    //TODO: initOwner
    //TODO: initEmployees

    /*--------------ANIMAL--------------*/
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

        Animal newAnimal = switch (Animal.Species.values()[innerMenuOption]) {
            case CATTLE -> initCattle();
            case SHEEP -> initSheep();
            case CHICKEN -> initChicken();
            case GOAT -> initGoat();
            case HORSE -> initHorse();
        };

        try {
            farm.addAnimal(newAnimal);
            System.out.println(newAnimal.species + " created successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private record AnimalData(
            Integer id,
            LocalDate dateOfBirth,
            String food,
            Animal.AnimalSex sex,
            float weightInKg,
            float heightInCm
    ) {
    }

    private static AnimalData initAnimal() {
        Integer auxId = null;
        char addId = InputService.readCharInValues(
                "Add ID? Y/N ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        );
        if (addId == 'Y') {
            auxId = InputService.readInt(
                    "Enter animal ID: ",
                    "Invalid ID. Try again: ",
                    1, 99999);
        }

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

        return new AnimalData(auxId, auxDate, auxFood, auxSex, auxWeight, auxHeight);
    }

    public static Cattle initCattle() {
        System.out.println("Let's create cattle!");
        AnimalData animalData = initAnimal();

        int breedVal = InputService.readInt(
                Cattle.CattleBreed.getAll() + "Select cattle breed: ",
                "This option does not exist. Try again: ",
                1, Cattle.CattleBreed.values().length
        );
        Cattle.CattleBreed auxCattleBreed = Cattle.CattleBreed.getCattleBreed(breedVal);

        Cattle newCattle;
        if (animalData.id != null)
            newCattle = new Cattle(animalData.id, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxCattleBreed);
        else
            newCattle = new Cattle(animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxCattleBreed);

        return newCattle;
    }

    public static Sheep initSheep() {
        System.out.println("Let's create a sheep!");
        AnimalData animalData = initAnimal();

        boolean auxIsTrained;
        char charIsTrained = InputService.readCharInValues(
                "Is it trained? Y/N ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        );
        auxIsTrained = charIsTrained == 'Y';

        int furTypeVal = InputService.readInt(
                Shearable.FurType.getAll() + "Select fur type: ",
                "This option does not exist. Try again: ",
                1, Shearable.FurType.values().length
        );
        Shearable.FurType auxFurType = Shearable.FurType.getFurType(furTypeVal);

        Sheep newSheep;
        if (animalData.id != null)
            newSheep = new Sheep(animalData.id, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxIsTrained, auxFurType);
        else
            newSheep = new Sheep(animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxIsTrained, auxFurType);

        return newSheep;
    }

    public static Chicken initChicken() {
        System.out.println("Let's create a chicken!");
        AnimalData animalData = initAnimal();

        int auxEggAmount = InputService.readInt(
                "Enter amount of eggs per day: ",
                "Invalid value. Try Again: ",
                0, 50
        );

        int eggSizeVal = InputService.readInt(
                Chicken.EggSize.getAll() + "Select egg size: ",
                "Invalid value. Try again: ",
                1, Chicken.EggSize.values().length
        );
        Chicken.EggSize auxEggSize = Chicken.EggSize.getEggSize(eggSizeVal);

        int coopLocVal = InputService.readInt(
                Chicken.CoopLocation.getAll() + "Select coop location: ",
                "Invalid value. Try again: ",
                1, Chicken.CoopLocation.values().length
        );
        Chicken.CoopLocation auxCoopLoc = Chicken.CoopLocation.getCoopLocation(coopLocVal);

        Chicken newChicken;
        if (animalData.id != null)
            newChicken = new Chicken(animalData.id, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxEggAmount, auxEggSize, auxCoopLoc);
        else
            newChicken = new Chicken(animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxEggAmount, auxEggSize, auxCoopLoc);

        return newChicken;
    }

    public static Goat initGoat() {
        System.out.println("Let's create a goat!");
        AnimalData animalData = initAnimal();

        int furTypeVal = InputService.readInt(
                Shearable.FurType.getAll() + "Select mohair type: ",
                "This option does not exist. Try again: ",
                1, Shearable.FurType.values().length
        );
        Shearable.FurType auxFurType = Shearable.FurType.getFurType(furTypeVal);

        Goat newGoat;
        if (animalData.id != null)
            newGoat = new Goat(animalData.id, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxFurType);
        else
            newGoat = new Goat(animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxFurType);

        return newGoat;
    }

    public static Horse initHorse() {
        System.out.println("Let's create a horse!");
        AnimalData animalData = initAnimal();

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

        Horse newHorse;
        if (animalData.id != null)
            newHorse = new Horse(animalData.id, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxIsForCompetition, auxSpeed, auxIsRideable);
        else
            newHorse = new Horse(animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxIsForCompetition, auxSpeed, auxIsRideable);

        return newHorse;
    }

    public static void breedAnimals(Farm farm) {
        Animal animal1 = MenuService.chooseAnimal(farm.getAnimals());
        if (animal1 == null) {
            System.out.println("Back to main menu...");
            return;
        }
        Animal animal2 = MenuService.chooseAnimal(farm.getAnimals());
        if (animal2 == null) {
            System.out.println("Back to main menu...");
            return;
        }

        try {
            Animal babyAnimal = animal1.breedWith(animal2);
            farm.addAnimal(babyAnimal);
            System.out.println(babyAnimal.species + " was born! Information:");
            System.out.println(babyAnimal);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Back to main menu...");
        }
    }

    /*---------------CROP---------------*/
    public static void createNewCrop(Farm farm) {
        int innerMenuOption = InputService.readInt(
                "\nCreate a new instance of which crop?\n0. GO BACK\n" + Crop.CropType.getAll() + "Choose an option: ",
                "This option does not exist. Try again: ",
                0, Crop.CropType.values().length
        );

        if (innerMenuOption == 0) {
            System.out.println("Back to main menu...");
            return;
        }

        Crop newCrop = switch (innerMenuOption) {
            case 1 -> initWheat();
            case 2 -> initCorn();
            case 3 -> initTomato();
            default -> throw new IllegalStateException("Unexpected value: " + innerMenuOption);
        };

        try {
            farm.addCrop(newCrop);
            System.out.println(newCrop.type + " created successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private record CropData(
            float acres,
            int daysToGrow,
            Crop.GrowthStage currentGrowthStage
    ) {
    }

    private static CropData initCrop() {
        float auxAcres = InputService.readFloat(
                "Enter acres to use: ",
                "Invalid value. Try again: ",
                1, 99_999
        );

        int auxDays = InputService.readInt(
                "Enter amount of days to grow: ",
                "Invalid value. Try again: ",
                1, 99_999
        );

        int growthStageVal = InputService.readInt(
                Crop.GrowthStage.getAll() + "Select current growth stage: ",
                "This option does not exist. Try again: ",
                1, Crop.GrowthStage.values().length
        );
        Crop.GrowthStage auxGrowthStage = Crop.GrowthStage.getGrowthStage(growthStageVal);

        return new CropData(auxAcres, auxDays, auxGrowthStage);
    }

    public static Wheat initWheat() {
        System.out.println("Let's create some wheat crops!");
        CropData cropData = initCrop();

        int wheatVarietyVal = InputService.readInt(
                Wheat.WheatVariety.getAll() + "Select wheat variety: ",
                "This option does not exist. Try again: ",
                1, Wheat.WheatVariety.values().length
        );
        Wheat.WheatVariety auxWheatVariety = Wheat.WheatVariety.getWheatVariety(wheatVarietyVal);

        int auxGluten = InputService.readInt(
                "Enter mg of gluten per 100 grams of wheat: ",
                "Invalid value. Try again: ",
                6000, 15000
        );

        return new Wheat(cropData.acres, cropData.daysToGrow, cropData.currentGrowthStage, auxWheatVariety, auxGluten);
    }

    public static Corn initCorn() {
        System.out.println("Let's create some corn crops!");
        CropData cropData = initCrop();

        int kernelTypeVal = InputService.readInt(
                Corn.KernelType.getAll() + "Select kernel type: ",
                "This option does not exist. Try again: ",
                1, Corn.KernelType.values().length
        );
        Corn.KernelType auxKernelType = Corn.KernelType.getKernelType(kernelTypeVal);

        float auxKernelSize = InputService.readFloat(
                "Enter the size of the kernels: ",
                "Invalid value. Try again: ",
                0.1F, 15
        );

        return new Corn(cropData.acres, cropData.daysToGrow, cropData.currentGrowthStage, auxKernelType, auxKernelSize);
    }

    public static Tomato initTomato() {
        System.out.println("Let's create some tomato crops!");
        CropData cropData = initCrop();

        int tomatoVarietyVal = InputService.readInt(
                Tomato.TomatoVariety.getAll() + "Select tomato variety: ",
                "This option does not exist. Try again: ",
                1, Tomato.TomatoVariety.values().length
        );
        Tomato.TomatoVariety auxTomatoVariety = Tomato.TomatoVariety.getTomatoVariety(tomatoVarietyVal);

        int auxYieldPerPlant = InputService.readInt(
                "Enter average yield of tomato per plant: ",
                "Invalid value. Try again: ",
                1, 15
        );

        return new Tomato(cropData.acres, cropData.daysToGrow, cropData.currentGrowthStage, auxTomatoVariety, auxYieldPerPlant);
    }

}
