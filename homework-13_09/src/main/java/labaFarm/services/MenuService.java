package labaFarm.services;

import labaFarm.farm.*;
import labaFarm.farm.animals.*;
import labaFarm.farm.animals.interfaces.IEggLayer;
import labaFarm.farm.animals.interfaces.IShearable;
import labaFarm.farm.crops.*;
import labaFarm.farm.exceptions.RepeatedInstanceException;
import labaFarm.farm.people.Employee;
import labaFarm.farm.people.Owner;
import org.apache.logging.log4j.Level;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class MenuService {
    public static Farm handleFarm() {
        boolean createNewFarm = InputService.readCharInValues(
                "Create new farm (1) or load default (2)? ",
                "This option does not exist. Try again: ",
                new char[]{'1', '2'}
        ) == '1';

        return createNewFarm ?
                FarmService.createFarm() :
                JsonService.readData("farm.json", Farm.class);
    }

    public static Owner handleOwner(Farm farm) {
        boolean createNewOwner = InputService.readCharInValues(
                "Create new owner (1) or load default (2)? ",
                "This option does not exist. Try again: ",
                new char[]{'1', '2'}
        ) == '1';

        return createNewOwner ?
                FarmService.initOwner(farm) :
                JsonService.readData("owner.json", Owner.class);
    }

    public static void handleAnimalsAndCrops(Farm farm) {
        boolean loadData = InputService.readCharInValues(
                "Load default animals and crops? Y/N: ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        ) == 'Y';
        if (!loadData) return;

        try {
            farm.addCrop(new Wheat(farm.cropSectors, 6, 92, CropSector.GrowthStage.SEEDLING, Wheat.WheatVariety.HARD_RED_WINTER));
            farm.addCrop(new Corn(farm.cropSectors, 7, 125, CropSector.GrowthStage.FLOWERING, Corn.KernelType.FLOUR, 8.5F));
            farm.addCrop(new Wheat(farm.cropSectors, 8, 105, CropSector.GrowthStage.VEGETATIVE, Wheat.WheatVariety.HARD_WHITE, 7300));
            farm.addCrop(new Corn(farm.cropSectors, 11, 135, CropSector.GrowthStage.HARVEST, Corn.KernelType.WAXY, 7.8F));
            farm.addCrop(new Tomato(farm.cropSectors, 9, 75, CropSector.GrowthStage.MATURITY, Tomato.TomatoVariety.CHERRY));
            farm.addCrop(new Corn(farm.cropSectors, 10, 120, CropSector.GrowthStage.SEEDLING, Corn.KernelType.DENT));
            farm.addCrop(new Tomato(farm.cropSectors, 6, 70, CropSector.GrowthStage.HARVEST, Tomato.TomatoVariety.BIG_BEEF, 50));

            List<Animal> animals = new ArrayList<>();
            animals.add(new Cattle(LocalDate.of(2021, 5, 10), "Grass", Animal.AnimalSex.M, 500.0f, 150.0f, Cattle.CattleBreed.HOLSTEIN));
            animals.add(new Cattle(LocalDate.of(2020, 11, 6), "Hay", Animal.AnimalSex.F, 480.0f, 148.0f));
            animals.add(new Cattle(101, LocalDate.of(2019, 8, 12), "Grass", Animal.AnimalSex.M, 520.0f, 152.0f, Cattle.CattleBreed.JERSEY));
            animals.add(new Cattle(102, LocalDate.of(2020, 4, 13), "Corn", Animal.AnimalSex.F, 510.0f, 151.0f));
            animals.add(new Cattle(LocalDate.of(2022, 3, 13), "Silage", Animal.AnimalSex.F, 470.0f, 145.0f, Cattle.CattleBreed.ANGUS));
            animals.add(new Cattle(LocalDate.of(2022, 6, 1), "Grain", Animal.AnimalSex.M, 490.0f, 149.0f));

            animals.add(new Chicken(LocalDate.of(2023, 2, 14), "Corn", Animal.AnimalSex.F, 2.5f, 35.0f, 6, IEggLayer.EggSize.MEDIUM, Chicken.CoopLocation.BARNYARD));
            animals.add(new Chicken(LocalDate.of(2023, 2, 14), "Seeds", Animal.AnimalSex.M, 2.7f, 37.0f, Chicken.CoopLocation.HILLTOP));
            animals.add(new Chicken(201, LocalDate.of(2023, 1, 18), "Insects", Animal.AnimalSex.F, 2.4f, 34.5f, 7, IEggLayer.EggSize.LARGE, Chicken.CoopLocation.CENTRAL));
            animals.add(new Chicken(202, LocalDate.of(2023, 1, 18), "Worms", Animal.AnimalSex.M, 2.8f, 38.0f, Chicken.CoopLocation.LAKESIDE));
            animals.add(new Chicken(LocalDate.of(2023, 3, 12), "Fruits", Animal.AnimalSex.F, 2.6f, 36.0f, 5, IEggLayer.EggSize.SMALL, Chicken.CoopLocation.BARNYARD));
            animals.add(new Chicken(LocalDate.of(2023, 3, 12), "Vegetables", Animal.AnimalSex.M, 2.9f, 39.0f, Chicken.CoopLocation.CENTRAL));

            animals.add(new Goat(LocalDate.of(2022, 8, 20), "Hay", Animal.AnimalSex.M, 65.0f, 70.0f, IShearable.FurType.LONG));
            animals.add(new Goat(LocalDate.of(2022, 8, 20), "Grass", Animal.AnimalSex.F, 63.0f, 68.0f));
            animals.add(new Goat(301, LocalDate.of(2021, 7, 15), "Grain", Animal.AnimalSex.M, 67.0f, 72.0f, IShearable.FurType.DOUBLE_COATED));
            animals.add(new Goat(302, LocalDate.of(2021, 7, 15), "Leaves", Animal.AnimalSex.F, 66.0f, 71.0f));
            animals.add(new Goat(LocalDate.of(2023, 6, 25), "Vegetables", Animal.AnimalSex.F, 64.0f, 69.0f, IShearable.FurType.FINE));
            animals.add(new Goat(LocalDate.of(2023, 6, 25), "Fruit", Animal.AnimalSex.M, 68.0f, 73.0f));

            animals.add(new Sheep(LocalDate.of(2021, 11, 5), "Grass", Animal.AnimalSex.F, 50.0f, 55.0f, true, IShearable.FurType.MEDIUM));
            animals.add(new Sheep(LocalDate.of(2021, 11, 5), "Hay", Animal.AnimalSex.M, 52.0f, 57.0f, false));
            animals.add(new Sheep(401, LocalDate.of(2020, 10, 10), "Grain", Animal.AnimalSex.F, 51.0f, 56.0f, true, IShearable.FurType.FINE));
            animals.add(new Sheep(402, LocalDate.of(2020, 10, 10), "Silage", Animal.AnimalSex.M, 53.0f, 58.0f, false));
            animals.add(new Sheep(LocalDate.of(2022, 12, 1), "Leaves", Animal.AnimalSex.F, 49.0f, 54.0f, true, IShearable.FurType.LONG));
            animals.add(new Sheep(LocalDate.of(2022, 12, 1), "Vegetables", Animal.AnimalSex.M, 54.0f, 59.0f, false));

            animals.add(new Horse(LocalDate.of(2020, 5, 22), "Hay", Animal.AnimalSex.M, 450.0f, 160.0f, true, 40.0f, true));
            animals.add(new Horse(LocalDate.of(2020, 5, 22), "Grass", Animal.AnimalSex.F, 430.0f, 158.0f, true, 38.0f));
            animals.add(new Horse(501, LocalDate.of(2019, 4, 10), "Grain", Animal.AnimalSex.M, 470.0f, 162.0f, false, 45.0f, true));
            animals.add(new Horse(502, LocalDate.of(2019, 4, 10), "Fruits", Animal.AnimalSex.F, 460.0f, 161.0f, false, 43.0f));
            animals.add(new Horse(LocalDate.of(2021, 3, 30), "Vegetables", Animal.AnimalSex.F, 440.0f, 159.0f, true, 39.0f, true));
            animals.add(new Horse(LocalDate.of(2021, 3, 30), "Silage", Animal.AnimalSex.M, 480.0f, 163.0f, false, 46.0f));

            Collections.shuffle(animals);
            farm.setAnimals(animals);
        } catch (RepeatedInstanceException e) {
            LoggerService.log(Level.WARN, e.getMessage());
        }

        System.out.println("Animals and Crops added!");
    }

    public static Employee.WorkShift chooseWorkShift() {
        int shiftVal = InputService.readInt(
                Employee.WorkShift.getAll() + "Select work shift: ",
                "This option does not exist. Try again: ",
                1, Employee.WorkShift.values().length
        );

        return Employee.WorkShift.getWorkShift(shiftVal);
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

        int chosenOpt = InputService.readInt(
                sb + "Choose an animal: ",
                "This option does not exist. Try again: ",
                0, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }

    public static CropSector chooseCropToHarvest(List<CropSector> list) {
        Stream<CropSector> harvestStream = list.stream().filter(cr -> cr.currentGrowthStage == CropSector.GrowthStage.HARVEST);
        List<CropSector> finalList = harvestStream.toList();

        if (finalList.isEmpty()) {
            System.out.println("No crops to harvest in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n0. GO BACK\n");
        int index = 1;

        for (CropSector cr : finalList) {
            sb.append(String.format("%d. %s - %.2f acres\n", index, cr.type, cr.acres));
            index++;
        }

        int chosenOpt = InputService.readInt(
                sb + "\nChoose a crop: ",
                "This option does not exist. Try again: ",
                0, index
        );

        if (chosenOpt == 0) return null;
        return finalList.get(chosenOpt - 1);
    }

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

        int chosenOpt = InputService.readInt(
                sb + "\nChoose one of the goods from the list: ",
                "This option does not exist. Try again: ",
                0, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }

}
