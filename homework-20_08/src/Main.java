import farm.*;
import farm.animals.*;
import farm.crops.*;
import farm.people.*;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Farm manorFarm = new Farm("Manor Farm", "Willingdon, England", 152.3F);
        Owner myOwner = new Owner("Mr. Jones", "243-12-5768", 74, manorFarm, 750_452.6);

//        Cow cowF1 = new Cow(LocalDate.of(2016, 6, 21), "Hay", Animal.AnimalSex.F, 564.3F, 2.01F, false, 25);
//        Cow cowM1 = new Cow(LocalDate.of(2014, 8, 12), "Grass", Animal.AnimalSex.M, 542.8F, 1.98F, false);
//
//        Cow cowF2 = new Cow(LocalDate.of(2020, 9, 4), "Grass", Animal.AnimalSex.F, 362.3F, 1.87F, true, 21.3F);
//        Cow cowM2 = new Cow(LocalDate.of(2021, 2, 28), "Grass", Animal.AnimalSex.M, 401.4F, 2.02F, false);

        enum Menu {
            EXIT, NEW_ANIMAL/*, NEW_CROP, SELL_ITEM, BREED_ANIMALS*/;

            private final int value;

            Menu() {
                this.value = this.ordinal();
            }

            public static boolean valueExists(int number) {
                for (Menu option : Menu.values()) {
                    if (option.value == number)
                        return true;
                }

                return false;
            }

            public static String getAll() {
                StringBuilder sb = new StringBuilder();
                for (Menu option : Menu.values()) {
                    sb.append(String.format("%d. %s\n", option.value, option));
                }

                return sb.toString();
            }
        }

        System.out.printf("\nWelcome to %s! My name is %s and I own this place. Located in %s with %.2f acres of land, but no animals or crops so far. Help me out and create some for me.\n", manorFarm.name, myOwner.fullName, manorFarm.location, manorFarm.size);

        char confContinue;
        main:
        do {
            int mainMenuOption = Input.readInt(
                    "\n" + Menu.getAll() + "Choose an option: ",
                    "This option does not exist. Try again: ",
                    0, Menu.values().length - 1
            );

            int innerMenuOption;
            switch (mainMenuOption) {
                case 0:
                    System.out.println("Exiting...");
                    break main;
                case 1:
                    innerMenuOption = Input.readInt(
                            "\nCreate a new instance of which animal?\n0. GO BACK\n" + Animal.Species.getAll() + "Choose an option: ",
                            "This option does not exist. Try again: ",
                            0, Animal.Species.values().length
                    );

                    Animal newAnimal;
                    switch (innerMenuOption) {
                        case 0:
                            System.out.println("Back to main menu...\n");
                            confContinue = 'Y';
                            continue main;
                        case 1:
                            newAnimal = initCow();
                            System.out.println("Cow created successfully!");
                            break;
                        case 2:
                            newAnimal = initSheep();
                            System.out.println("Sheep created successfully!");
                            break;
                        case 3:
                            newAnimal = initChicken();
                            System.out.println("Chicken created successfully!");
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + innerMenuOption);
                    }

                    manorFarm.addAnimal(newAnimal);
                    break;
            }

            System.out.print("Do you wish to continue? Y/N: ");
            confContinue = Character.toUpperCase(SCANNER.next().charAt(0));
            while (confContinue != 'Y' && confContinue != 'N') {
                System.out.println("This option does not exist. Try again.");
                System.out.print("Do you wish to continue? Y/N: ");
                confContinue = Character.toUpperCase(SCANNER.next().charAt(0));
            }
        } while (confContinue == 'Y');

        System.out.println("\n\n" + manorFarm);
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

    static AnimalData initAnimal() {
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

        //  heightInCm
        float auxHeight = Input.readFloat(
                "Enter height in cm: ",
                "Enter a valid value (0.1 - 9,999): ",
                0.1F, 9_999
        );

        return new AnimalData(auxId, auxDate, auxFood, auxSex, auxWeight, auxHeight);
    }

    static Cow initCow() {
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

    static Sheep initSheep() {
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
                "Invalid value. Try again: ",
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

    static Chicken initChicken() {
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

}