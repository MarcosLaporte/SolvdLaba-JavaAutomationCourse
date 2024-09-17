package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.Good;
import labaFarm.farm.animals.*;
import labaFarm.farm.animals.interfaces.IEggLayer;
import labaFarm.farm.animals.interfaces.IShearable;
import labaFarm.farm.crops.Corn;
import labaFarm.farm.crops.CropSector;
import labaFarm.farm.crops.Tomato;
import labaFarm.farm.crops.Wheat;
import labaFarm.farm.exceptions.RepeatedInstanceException;
import labaFarm.farm.people.Employee;
import labaFarm.farm.people.Owner;
import org.apache.logging.log4j.Level;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

            farm.addAnimal(new Cattle(farm.animals, LocalDate.of(2021, 5, 10), "Grass", Animal.AnimalSex.M, 500.0f, 150.0f, Cattle.CattleBreed.HOLSTEIN));
            farm.addAnimal(new Cattle(farm.animals, LocalDate.of(2019, 8, 12), "Grass", Animal.AnimalSex.M, 520.0f, 152.0f, Cattle.CattleBreed.JERSEY));
            farm.addAnimal(new Cattle(farm.animals, LocalDate.of(2022, 3, 13), "Silage", Animal.AnimalSex.F, 470.0f, 145.0f, Cattle.CattleBreed.ANGUS));

            farm.addAnimal(new Chicken(farm.animals, LocalDate.of(2023, 2, 14), "Corn", Animal.AnimalSex.F, 2.5f, 35.0f, 6, IEggLayer.EggSize.MEDIUM, Chicken.CoopLocation.BARNYARD));
            farm.addAnimal(new Chicken(farm.animals, LocalDate.of(2023, 1, 18), "Insects", Animal.AnimalSex.F, 2.4f, 34.5f, 7, IEggLayer.EggSize.LARGE, Chicken.CoopLocation.CENTRAL));
            farm.addAnimal(new Chicken(farm.animals, LocalDate.of(2023, 3, 12), "Fruits", Animal.AnimalSex.F, 2.6f, 36.0f, 5, IEggLayer.EggSize.SMALL, Chicken.CoopLocation.BARNYARD));

            farm.addAnimal(new Goat(farm.animals, LocalDate.of(2022, 8, 20), "Hay", Animal.AnimalSex.M, 65.0f, 70.0f, IShearable.FurType.LONG));
            farm.addAnimal(new Goat(farm.animals, LocalDate.of(2021, 7, 15), "Grain", Animal.AnimalSex.M, 67.0f, 72.0f, IShearable.FurType.DOUBLE_COATED));
            farm.addAnimal(new Goat(farm.animals, LocalDate.of(2023, 6, 25), "Vegetables", Animal.AnimalSex.F, 64.0f, 69.0f, IShearable.FurType.FINE));

            farm.addAnimal(new Sheep(farm.animals, LocalDate.of(2021, 11, 5), "Grass", Animal.AnimalSex.F, 50.0f, 55.0f, true, IShearable.FurType.MEDIUM));
            farm.addAnimal(new Sheep(farm.animals, LocalDate.of(2020, 10, 10), "Grain", Animal.AnimalSex.F, 51.0f, 56.0f, true, IShearable.FurType.FINE));
            farm.addAnimal(new Sheep(farm.animals, LocalDate.of(2022, 12, 1), "Leaves", Animal.AnimalSex.F, 49.0f, 54.0f, true, IShearable.FurType.LONG));

            farm.addAnimal(new Horse(farm.animals, LocalDate.of(2020, 5, 22), "Hay", Animal.AnimalSex.M, 450.0f, 160.0f, true, 40.0f, true));
            farm.addAnimal(new Horse(farm.animals, LocalDate.of(2019, 4, 10), "Grain", Animal.AnimalSex.M, 470.0f, 162.0f, false, 45.0f, true));
            farm.addAnimal(new Horse(farm.animals, LocalDate.of(2021, 3, 30), "Vegetables", Animal.AnimalSex.F, 440.0f, 159.0f, true, 39.0f, true));
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

    public static Animal chooseAnimal(List<Animal> list, boolean goBackOption) {
        if (list.isEmpty()) {
            System.out.println("No animals in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (goBackOption) sb.append("\n0. GO BACK\n");
        int index = 1;

        for (Animal an : list) {
            sb.append(String.format("%d. %s (%s) - ID %d\n", index, an.species, an.sex, an.getId()));
            index++;
        }

        int chosenOpt = InputService.readInt(
                sb + "Choose an animal: ",
                "This option does not exist. Try again: ",
                goBackOption ? 0 : 1, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }
    public static Animal chooseAnimal(List<Animal> list) {
        return chooseAnimal(list, true);
    }

    public static CropSector chooseCrop(List<CropSector> list, boolean goBackOption) {
        if (list.isEmpty()) {
            System.out.println("No crops in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (goBackOption) sb.append("\n0. GO BACK\n");
        int index = 1;

        for (CropSector cr : list) {
            sb.append(String.format("%d. %s - %.2f acres\n", index, cr.type, cr.acres));
            index++;
        }

        int chosenOpt = InputService.readInt(
                sb + "\nChoose a crop: ",
                "This option does not exist. Try again: ",
                goBackOption ? 0 : 1, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }
    public static CropSector chooseCrop(List<CropSector> list) {
        return chooseCrop(list, true);
    }

    public static Good chooseGood(List<Good> list, boolean goBackOption) {
        if (list.isEmpty()) {
            System.out.println("No goods in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (goBackOption) sb.append("\n0. GO BACK\n");
        int index = 1;

        for (Good good : list) {
            sb.append(String.format("%d. %s - $%.2f\n", index, good.type, good.getTotalValue()));
            index++;
        }

        int chosenOpt = InputService.readInt(
                sb + "\nChoose one of the goods from the list: ",
                "This option does not exist. Try again: ",
                goBackOption ? 0 : 1, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }
    public static Good chooseGood(List<Good> list) {
        return chooseGood(list, true);
    }

    public static Employee chooseEmployee(Map<String, Employee> employeeMap) {
        String[] employeesSSN = employeeMap.keySet().toArray(new String[0]);
        System.out.println(Employee.toTable(employeeMap.values().stream().toList()));

        String chosenSSN = InputService.readStringInValues(
                "Enter selected employee's SSN: ",
                "That value does not exist. Try again: ",
                employeesSSN
        );

        return employeeMap.get(chosenSSN);
    }
}
