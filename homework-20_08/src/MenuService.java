import farm.Farm;
import farm.Good;
import farm.animals.Animal;
import farm.animals.Chicken;
import farm.animals.Cow;
import farm.animals.Sheep;
import farm.crops.Corn;
import farm.crops.Crop;
import farm.crops.Tomato;
import farm.crops.Wheat;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class MenuService {
    enum Menu {
        EXIT, PRINT_FARM, NEW_ANIMAL, NEW_CROP, ANIMAL_GOODS, HARVEST_CROP, BREED_ANIMALS, SELL_GOODS;

        private final int value;
        Menu() {
            this.value = this.ordinal();
        }

        public static String printMenu() {
            StringBuilder sb = new StringBuilder();
            for (Menu option : Menu.values()) {
                sb.append(String.format("%d. %s\n", option.value, option));
            }

            return sb.toString();
        }
    }

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void mainMenu(Farm farm) {
        int mainMenuOption;
        do {
            mainMenuOption = Input.readInt(
                    "\n" + Menu.printMenu() + "Choose an option: ",
                    "This option does not exist. Try again: ",
                    0, Menu.values().length - 1
            );

            switch (Menu.values()[mainMenuOption]) {
                case EXIT -> System.out.println("Exiting...");
                case PRINT_FARM -> System.out.println(farm);
                case NEW_ANIMAL -> createNewAnimal(farm);
                case NEW_CROP -> createNewCrop(farm);
                case ANIMAL_GOODS -> manageAnimalGoods(farm);
                case HARVEST_CROP -> manageHarvest(farm);
                case BREED_ANIMALS -> breedAnimals(farm);
                case SELL_GOODS -> sellGoods(farm);
                default -> System.out.println("This option does not exist.");
            }

        } while (mainMenuOption != 0);
    }

    public static void createNewAnimal(Farm farm) {
        int innerMenuOption = Input.readInt(
                "\nCreate a new instance of which animal?\n0. GO BACK\n" + Animal.Species.getAll() + "Choose an option: ",
                "This option does not exist. Try again: ",
                0, Animal.Species.values().length
        );

        if (innerMenuOption == 0) {
            System.out.println("Back to main menu...");
            return;
        }

        Animal newAnimal = switch (innerMenuOption) {
            case 1 -> initCow();
            case 2 -> initSheep();
            case 3 -> initChicken();
            default -> throw new IllegalStateException("Unexpected value: " + innerMenuOption);
        };

        farm.addAnimal(newAnimal);
        System.out.println(newAnimal.getClass().getSimpleName() + " created successfully!");
    }

    public static void createNewCrop(Farm farm) {
        int innerMenuOption = Input.readInt(
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

        farm.addCrop(newCrop);
        System.out.println(newCrop.getClass().getSimpleName() + " created successfully!");
    }

    public static void manageAnimalGoods(Farm farm) {
        Animal chosenAn = chooseAnimal(farm.animals);
        if (chosenAn == null) {
            System.out.println("Back to main menu...");
            return;
        }

        System.out.println("Getting the " + chosenAn.species + " ready...");
        try {
            farm.stock.add(chosenAn.produceGoods());
            System.out.println(chosenAn.getProducedGoods() + " added to stock!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void manageHarvest(Farm farm) {
        Crop chosenCr = chooseCropToHarvest(farm.crops);
        if (chosenCr == null) {
            System.out.println("Back to main menu...");
            return;
        }

        System.out.println("Harvesting " + chosenCr.type + "...");
        try {
            Good harvestedCrop = chosenCr.harvest(0);

            float unitValue = Input.readFloat(
                    chosenCr.type + " harvested! Enter each " + harvestedCrop.unitOfMeasure + " value: ",
                    "Invalid value. Try again: ",
                    0.1F, 999999
            );
            harvestedCrop.setUnitValue(unitValue);

            farm.stock.add(harvestedCrop);
            System.out.println(harvestedCrop.type + " added to stock!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void breedAnimals(Farm farm) {
        Animal animal1 = chooseAnimal(farm.animals);
        if (animal1 == null) {
            System.out.println("Back to main menu...");
            return;
        }
        Animal animal2 = chooseAnimal(farm.animals);
        if (animal2 == null) {
            System.out.println("Back to main menu...");
            return;
        }

        try {
            Animal babyAnimal = animal1.breed(animal2);
            farm.addAnimal(babyAnimal);
            System.out.println(babyAnimal.species + " was born! Information:");
            System.out.println(babyAnimal);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Back to main menu...");
        }
    }

    public static void sellGoods(Farm farm) {
        Good chosenGood = chooseGood(farm.stock);
        if (chosenGood == null) {
            System.out.println("Back to main menu...");
            return;
        }

        try {
            farm.sellGood(chosenGood);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("%s sold for $%.2f%n", chosenGood.type, chosenGood.getTotalValue());
    }

    /*--------------ANIMAL--------------*/

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
        char addId = Input.readCharInValues(
                "Add ID? Y/N ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        );
        if (addId == 'Y') {
            System.out.print("Enter animal ID: ");
            auxId = SCANNER.nextInt();
        }

        System.out.println("Fill with animal's date of birth.");
        LocalDate auxDate = Input.readValidDate();

        System.out.print("What does it eat? ");
        String auxFood = SCANNER.nextLine();

        char sexChar = Input.readCharInValues(
                "Enter animal sex (F/M): ",
                "This option does not exist. Try again: ",
                new char[]{'F', 'M'}
        );
        Animal.AnimalSex auxSex = Animal.AnimalSex.valueOf(String.valueOf(sexChar));

        float auxWeight = Input.readFloat(
                "Enter weight in Kg: ",
                "Enter a valid value (0.001 - 99,999): ",
                0.001F, 99_999
        );

        float auxHeight = Input.readFloat(
                "Enter height in cm: ",
                "Enter a valid value (0.1 - 9,999): ",
                0.1F, 9_999
        );

        return new AnimalData(auxId, auxDate, auxFood, auxSex, auxWeight, auxHeight);
    }

    public static Cow initCow() {
        System.out.println("Let's create a cow!");
        AnimalData animalData = initAnimal();

        boolean auxProducesMilk = false;
        if (animalData.sex == Animal.AnimalSex.F) {
            char charProdMilk = Input.readCharInValues(
                    "Does it produce milk? Y/N ",
                    "This option does not exist. Try again: ",
                    new char[]{'Y', 'N'}
            );
            auxProducesMilk = charProdMilk == 'Y';
        }

        float auxMilkProd = 0;
        if (auxProducesMilk) {
            auxMilkProd = Input.readFloat(
                    "How much liters of milk produces? ",
                    "Invalid value. Try again: ",
                    1, 1000
            );
        }

        Cow newCow;
        if (animalData.id != null)
            newCow = new Cow(animalData.id, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxProducesMilk, auxMilkProd);
        else
            newCow = new Cow(animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxProducesMilk, auxMilkProd);

        return newCow;
    }

    public static Sheep initSheep() {
        System.out.println("Let's create a sheep!");
        AnimalData animalData = initAnimal();

        boolean auxIsTrained;
        char charIsTrained = Input.readCharInValues(
                "Is it trained? Y/N ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        );
        auxIsTrained = charIsTrained == 'Y';

        int woolTypeVal = Input.readInt(
                Sheep.WoolType.getAll() + "Select wool type: ",
                "This option does not exist. Try again: ",
                1, Sheep.WoolType.values().length
        );
        Sheep.WoolType auxWoolType = Sheep.WoolType.getWoolType(woolTypeVal);

        Sheep newSheep;
        if (animalData.id != null)
            newSheep = new Sheep(animalData.id, animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxIsTrained, auxWoolType);
        else
            newSheep = new Sheep(animalData.dateOfBirth, animalData.food, animalData.sex, animalData.weightInKg, animalData.heightInCm, auxIsTrained, auxWoolType);

        return newSheep;
    }

    public static Chicken initChicken() {
        System.out.println("Let's create a chicken!");
        AnimalData animalData = initAnimal();

        int auxEggAmount = Input.readInt(
                "Enter amount of eggs per day: ",
                "Invalid value. Try Again: ",
                0, 50
        );

        int eggSizeVal = Input.readInt(
                Chicken.EggSize.getAll() + "Select egg size: ",
                "Invalid value. Try again: ",
                1, Chicken.EggSize.values().length
        );
        Chicken.EggSize auxEggSize = Chicken.EggSize.getEggSize(eggSizeVal);

        int coopLocVal = Input.readInt(
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

    public static Animal chooseAnimal(List<Animal> list) {
        if (list.isEmpty()) {
            System.out.println("No animals in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n0. GO BACK\n");
        int index = 1;

        for (Animal an : list) {
            sb.append(String.format("%d. %s (%s) - ID %d\n", index, an.species, an.sex, an.getId()));
            index++;
        }

        int chosenOpt = Input.readInt(
                sb + "Choose an animal: ",
                "This option does not exist. Try again: ",
                0, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }

    /*---------------CROP---------------*/

    private record CropData(
            float acres,
            int daysToGrow,
            Crop.GrowthStage currentGrowthStage
    ) {
    }

    private static CropData initCrop() {
        float auxAcres = Input.readFloat(
                "Enter acres to use: ",
                "Invalid value. Try again: ",
                1, 99_999
        );

        int auxDays = Input.readInt(
                "Enter amount of days to grow: ",
                "Invalid value. Try again: ",
                1, 99_999
        );

        int growthStageVal = Input.readInt(
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

        int wheatVarietyVal = Input.readInt(
                Wheat.WheatVariety.getAll() + "Select wheat variety: ",
                "This option does not exist. Try again: ",
                1, Wheat.WheatVariety.values().length
        );
        Wheat.WheatVariety auxWheatVariety = Wheat.WheatVariety.getWheatVariety(wheatVarietyVal);

        int auxGluten = Input.readInt(
                "Enter mg of gluten per 100 grams of wheat: ",
                "Invalid value. Try again: ",
                6000, 15000
        );

        return new Wheat(cropData.acres, cropData.daysToGrow, cropData.currentGrowthStage, auxWheatVariety, auxGluten);
    }

    public static Corn initCorn() {
        System.out.println("Let's create some corn crops!");
        CropData cropData = initCrop();

        int kernelTypeVal = Input.readInt(
                Corn.KernelType.getAll() + "Select kernel type: ",
                "This option does not exist. Try again: ",
                1, Corn.KernelType.values().length
        );
        Corn.KernelType auxKernelType = Corn.KernelType.getKernelType(kernelTypeVal);

        float auxKernelSize = Input.readFloat(
                "Enter the size of the kernels: ",
                "Invalid value. Try again: ",
                0.1F, 15
        );

        return new Corn(cropData.acres, cropData.daysToGrow, cropData.currentGrowthStage, auxKernelType, auxKernelSize);
    }

    public static Tomato initTomato() {
        System.out.println("Let's create some tomato crops!");
        CropData cropData = initCrop();

        int tomatoVarietyVal = Input.readInt(
                Tomato.TomatoVariety.getAll() + "Select tomato variety: ",
                "This option does not exist. Try again: ",
                1, Tomato.TomatoVariety.values().length
        );
        Tomato.TomatoVariety auxTomatoVariety = Tomato.TomatoVariety.getTomatoVariety(tomatoVarietyVal);

        int auxYieldPerPlant = Input.readInt(
                "Enter average yield of tomato per plant: ",
                "Invalid value. Try again: ",
                1, 15
        );

        return new Tomato(cropData.acres, cropData.daysToGrow, cropData.currentGrowthStage, auxTomatoVariety, auxYieldPerPlant);
    }

    public static Crop chooseCropToHarvest(List<Crop> list) {
        Stream<Crop> harvestStream = list.stream().filter(cr -> cr.currentGrowthStage == Crop.GrowthStage.HARVEST);
        List<Crop> finalList = harvestStream.toList();

        if (finalList.isEmpty()) {
            System.out.println("No crops to harvest in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n0. GO BACK\n");
        int index = 1;

        for (Crop cr : finalList) {
            sb.append(String.format("%d. %s - %.2f acres\n", index, cr.type, cr.acres));
            index++;
        }

        int chosenOpt = Input.readInt(
                sb + "\nChoose a crop: ",
                "This option does not exist. Try again: ",
                0, index
        );

        if (chosenOpt == 0) return null;
        return finalList.get(chosenOpt - 1);
    }

    /*---------------GOODS---------------*/

    public static Good chooseGood(List<Good> list) {
        if (list.isEmpty()) {
            System.out.println("No goods in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n0. GO BACK\n");
        int index = 1;

        for (Good good : list) {
            sb.append(String.format("%d. %s - $%.2f\n", index, good.type, good.getTotalValue()));
            index++;
        }

        int chosenOpt = Input.readInt(
                sb + "\nChoose one of the goods from the list: ",
                "This option does not exist. Try again: ",
                0, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }

}
