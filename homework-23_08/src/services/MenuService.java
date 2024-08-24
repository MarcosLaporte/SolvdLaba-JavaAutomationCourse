package services;

import farm.Farm;
import farm.Good;
import farm.animals.*;
import farm.animals.interfaces.Shearable;
import farm.crops.Corn;
import farm.crops.Crop;
import farm.crops.Tomato;
import farm.crops.Wheat;

import java.time.LocalDate;
import java.util.List;

public class MenuService {
    public static void loadAnimalsAndCrops(Farm farm) {
        try {
            farm.addCrop(new Wheat(6, 92, Crop.GrowthStage.SEEDLING, Wheat.WheatVariety.HARD_RED_WINTER));
            farm.addCrop(new Corn(7, 125, Corn.KernelType.FLOUR, 8.5F));
            farm.addCrop(new Wheat(8, 105, Crop.GrowthStage.VEGETATIVE, Wheat.WheatVariety.HARD_WHITE, 7300));
            farm.addCrop(new Corn(11, 135, Crop.GrowthStage.HARVEST, Corn.KernelType.WAXY, 7.8F));
            farm.addCrop(new Tomato(9, 75, Crop.GrowthStage.MATURITY, Tomato.TomatoVariety.CHERRY));
            farm.addCrop(new Corn(120, Corn.KernelType.DENT));
            farm.addCrop(new Tomato(6, 70, Crop.GrowthStage.HARVEST, Tomato.TomatoVariety.BIG_BEEF, 50));

            farm.addAnimal(new Cattle(LocalDate.of(2020, 5, 14), "Grass", Animal.AnimalSex.M, 600.5f, 150.0f));
            farm.addAnimal(new Sheep(LocalDate.of(2018, 4, 22), "Grass", Animal.AnimalSex.M, 70.0f, 100.0f, true, Shearable.FurType.FINE));
            farm.addAnimal(new Chicken(201, LocalDate.of(2021, 9, 11), "Grain", Animal.AnimalSex.F, 2.8f, 47.0f, 4, Chicken.EggSize.MEDIUM, Chicken.CoopLocation.LAKESIDE));
            farm.addAnimal(new Sheep(302, LocalDate.of(2017, 12, 1), "Shrubs", Animal.AnimalSex.M, 69.8f, 99.5f, true));
            farm.addAnimal(new Cattle(101, LocalDate.of(2021, 11, 29), "Mixed Feed", Animal.AnimalSex.F, 620.2f, 152.5f, Cattle.CattleBreed.HEREFORD));
            farm.addAnimal(new Chicken(LocalDate.of(2022, 7, 20), "Corn", Animal.AnimalSex.M, 2.5f, 45.0f, 3, Chicken.EggSize.LARGE, Chicken.CoopLocation.HILLTOP));
            farm.addAnimal(new Cattle(102, LocalDate.of(2022, 1, 18), "Grain", Animal.AnimalSex.F, 610.8f, 148.3f, Cattle.CattleBreed.HOLSTEIN));
            farm.addAnimal(new Sheep(LocalDate.of(2020, 6, 15), "Herbs", Animal.AnimalSex.M, 68.5f, 98.0f, true));
            farm.addAnimal(new Chicken(LocalDate.of(2023, 2, 5), "Seeds", Animal.AnimalSex.F, 2.3f, 44.0f, Chicken.CoopLocation.HILLTOP));
            farm.addAnimal(new Chicken(202, LocalDate.of(2020, 11, 3), "Worms", Animal.AnimalSex.M, 2.6f, 46.0f, Chicken.CoopLocation.BARNYARD));
            farm.addAnimal(new Sheep(301, LocalDate.of(2019, 8, 10), "Hay", Animal.AnimalSex.M, 72.3f, 102.0f, true, Shearable.FurType.MEDIUM));
            farm.addAnimal(new Cattle(LocalDate.of(2019, 3, 7), "Hay", Animal.AnimalSex.F, 580.0f, 145.0f, Cattle.CattleBreed.JERSEY));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Animals and Crops added!");
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
