package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.people.Owner;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainMenu {
    private enum Menu {
        EXIT, PRINT_FARM, NEW_ANIMAL, NEW_CROP, ANIMAL_GOODS, HARVEST_CROP, BREED_ANIMALS, SELL_GOODS, ADD_EMPLOYEE, FILTER_ANIMALS, PERFORM_ACTION, /*DEEP_FILTER*/;

        private final int value;
        Menu() {
            this.value = this.ordinal();
        }

        public static String printMenu() {
            StringBuilder sb = new StringBuilder();
            for (Menu option : Menu.values()) {
                String optionStr = StringUtils.replace(String.valueOf(option), "_", " ");
                sb.append(String.format("%d. %s\n", option.value, optionStr));
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
                case FILTER_ANIMALS -> FilterService.filterAnimals(farm);
                case PERFORM_ACTION -> ActionsService.recordAction(farm, owner);
                default -> System.out.println("This option does not exist.");
            }

        } while (mainMenuOption != 0);
    }

    public static void printLogs() {
        final String filePath = System.getProperty("user.dir") + "/logs/app.log";
        File logFile = new File(filePath);

        if (!logFile.exists() || logFile.length() == 0) {
            System.out.println("No logs found.");
            return;
        }

        try (Scanner scanner = new Scanner(logFile)) {
            System.out.println("Errors encountered in App Runtime: ");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Log file not found: " + e.getMessage());
        }
    }

}
