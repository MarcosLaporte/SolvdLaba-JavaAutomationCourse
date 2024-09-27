package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.crops.HarvestTracker;
import labaFarm.farm.people.Owner;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

import static labaFarm.services.ReflectionService.ClassExclusionPredicate;

public class MainMenu implements Runnable {
    private enum Menu {
        EXIT, PRINT_FARM, PRINT_OWNER, NEW_ANIMAL, NEW_CROP, ANIMAL_GOODS, HARVEST_CROP, BREED_ANIMALS,
        SELL_GOODS, ADD_EMPLOYEE, FILTER_ANIMALS, PERFORM_ACTION, GET_CLASS_INFO,
        PRINT_THREADS, TRACK_CROP, TEST_CONNECTION_POOL;

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

    private final Owner owner;
    private final Farm farm;

    public MainMenu(Owner owner, Farm farm) {
        this.owner = owner;
        this.farm = farm;
    }

    private final List<HarvestTracker> cropTrackerThreads = new ArrayList<>();
    @Override
    public void run() {
        int mainMenuOption;
        do {
            mainMenuOption = InputService.readInt(
                    "\n" + Menu.printMenu() + "Choose an option: ",
                    "This option does not exist. Try again: ",
                    0, Menu.values().length - 1
            );

            this.executeOption(Menu.values()[mainMenuOption]);

            this.checkForCropTrackerThreads();
        } while (mainMenuOption != 0);
    }

    private void executeOption(Menu menuOption) {
        switch (menuOption) {
            case EXIT -> System.out.println("Exiting...");
            case PRINT_FARM -> System.out.println(this.farm);
            case PRINT_OWNER -> System.out.println(this.owner);
            case NEW_ANIMAL -> AnimalsService.createNewAnimal(this.farm);
            case NEW_CROP -> CropsService.createNewCrop(this.farm);
            case ANIMAL_GOODS -> new GoodsService(this.farm).manageAnimalGoods(); //TODO: Make an inner menu in GoodsService
            case HARVEST_CROP -> new GoodsService(this.farm).trackHarvest(); //TODO: Make an inner menu in GoodsService
            case BREED_ANIMALS -> AnimalsService.breedAnimals(this.farm);
            case SELL_GOODS -> new GoodsService(this.farm).sellGoods(); //TODO: Make an inner menu in GoodsService
            case ADD_EMPLOYEE -> FarmService.createNewEmployee(this.owner);
            case FILTER_ANIMALS -> FilterService.filterAnimals(this.farm);
            case PERFORM_ACTION -> ActionsService.recordAction(this.farm, this.owner);
            case GET_CLASS_INFO -> getClassInfo();
            case PRINT_THREADS -> printActiveThreads();
            case TRACK_CROP -> {
                HarvestTracker harvestTracker = new GoodsService(this.farm).trackHarvest(); //TODO: Make an inner menu in GoodsService
                if (harvestTracker != null) {
                    this.cropTrackerThreads.add(harvestTracker);
                    harvestTracker.start();
                }
            }
            case TEST_CONNECTION_POOL -> ConnectionService.run();
            default -> System.out.println("This option does not exist.");
        }
    }

    private void checkForCropTrackerThreads() {
        Iterator<HarvestTracker> iterator = this.cropTrackerThreads.iterator();
        while (iterator.hasNext()) {
            HarvestTracker tracker = iterator.next();
            if (!tracker.isAlive()) {
                new GoodsService(this.farm).manageHarvest(tracker);
                iterator.remove();
            }
        }
    }

    public static void printLogs() {
        final String filePath = System.getProperty("user.dir") + "/logs/app.log";
        File logFile = new File(filePath);

        if (!logFile.exists() || logFile.length() == 0) {
            System.out.println("No logs found.");
            return;
        }

        try (Scanner scanner = new Scanner(logFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Log file not found: " + e.getMessage());
        }
    }

    private static void getClassInfo() {
        List<Class<?>> classList = new ArrayList<>();
        try {
            classList = ReflectionService.getClassesInPackage("labaFarm.farm",
                    ClassExclusionPredicate.INTERFACE,
                    ClassExclusionPredicate.EXCEPTION
            );
        } catch (ClassNotFoundException e) {
            LoggerService.consoleLog(Level.ERROR, e.getMessage());
        }

        if (classList.isEmpty()) {
            System.out.println("No classes found in this package.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        Map<String, Class<?>> classesMap = new HashMap<>();
        for (Class<?> clazz : classList) {
            classesMap.put((clazz.getSimpleName()).toLowerCase(), clazz);
            sb.append(" - ").append(clazz.getSimpleName()).append("\n");
        }

        System.out.println(sb);
        Class<?> clazz;

        while(true) {
            String chosenClass = InputService.readString("Select a class by entering its name: ", 1, 255);
            clazz = classesMap.get(chosenClass.toLowerCase());
            if (clazz != null) break;

            System.out.println("This class does not exist. Try again.");
        }

        ReflectionService<?> reflectionService = new ReflectionService<>(clazz);
        reflectionService.printClassInfo();
    }

    public static void printActiveThreads() {
        Set<Thread> activeThreads = Thread.getAllStackTraces().keySet();

        System.out.println("Active Threads:");
        for (Thread thread : activeThreads) {
            if (thread != null) {
                System.out.println("Thread Name: " + thread.getName() +
                        ", State: " + thread.getState());
            }
        }
    }
}
