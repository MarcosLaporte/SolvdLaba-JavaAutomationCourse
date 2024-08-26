package services;

import farm.Farm;
import farm.people.Owner;

public class MainMenu {
    private enum Menu {
        EXIT, PRINT_FARM, NEW_ANIMAL, NEW_CROP, ANIMAL_GOODS, HARVEST_CROP, BREED_ANIMALS, SELL_GOODS, ADD_EMPLOYEE;

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

    public static void mainMenu(Owner owner, Farm farm) {
        int mainMenuOption;
        do {
            mainMenuOption = InputService.readInt(
                    "\n" + Menu.printMenu() + "Choose an option: ",
                    "This option does not exist. Try again: ",
                    0, Menu.values().length - 1
            );

            switch (Menu.values()[mainMenuOption]) {
                case EXIT -> System.out.println("Exiting...");
                case PRINT_FARM -> System.out.println(farm);
                case NEW_ANIMAL -> AnimalsService.createNewAnimal(farm);
                case NEW_CROP -> CropsService.createNewCrop(farm);
                case ANIMAL_GOODS -> GoodsService.manageAnimalGoods(farm);
                case HARVEST_CROP -> GoodsService.manageHarvest(farm);
                case BREED_ANIMALS -> AnimalsService.breedAnimals(farm);
                case SELL_GOODS -> GoodsService.sellGoods(farm);
                case ADD_EMPLOYEE -> FarmService.createNewEmployee(owner);
                default -> System.out.println("This option does not exist.");
            }

        } while (mainMenuOption != 0);
    }

}
