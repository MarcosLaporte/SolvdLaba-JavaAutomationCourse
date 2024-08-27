package services;

import farm.Farm;
import farm.Good;
import farm.animals.*;
import farm.crops.Crop;
import farm.exceptions.MissingGoodException;
import farm.exceptions.RepeatedInstanceException;
import farm.exceptions.UnableToProduceException;

import java.util.List;
import java.util.stream.Stream;

public class GoodsService {
    public static void manageAnimalGoods(Farm farm) {
        Animal chosenAn = MenuService.chooseAnimal(farm.getAnimals());
        if (chosenAn == null) {
            System.out.println("Back to main menu...");
            return;
        }

        System.out.println("Getting the " + chosenAn.species + " ready...");
        try {
            Good goodProduced = switch (chosenAn.species) {
                case HORSE -> throw new UnableToProduceException("Horses don't produce any goods.");
                case CATTLE -> ((Cattle) chosenAn).milk();
                case CHICKEN -> ((Chicken) chosenAn).getEggs();
                case SHEEP -> ((Sheep) chosenAn).shear();
                case GOAT -> {
                    int chosenGoatGood = InputService.readInt(
                            "1. Milk\n2. Mohair\nChoose what to gather from goat: ",
                            "This option does not exist. Try again: ",
                            1, 2
                    );
                    Goat goat = (Goat) chosenAn;
                    yield chosenGoatGood == 1 ? goat.milk() : goat.shear();
                }
            };

            farm.addGoodToStock(goodProduced);
            System.out.println(goodProduced.type + " added to stock!");
        } catch (RepeatedInstanceException e) {
            LoggerService.warn(e.getMessage());
        } catch (UnableToProduceException e) {
            LoggerService.info(e.getMessage());
        }
    }

    public static void manageHarvest(Farm farm) {
        Crop chosenCr = chooseCropToHarvest(farm.getCrops());
        if (chosenCr == null) {
            System.out.println("Back to main menu...");
            return;
        }

        System.out.println("Harvesting " + chosenCr.type + "...");
        try {
            Good harvestedCrop = chosenCr.harvest(0);

            float unitValue = InputService.readFloat(
                    chosenCr.type + " harvested! Enter each " + harvestedCrop.unitOfMeasure + " value: ",
                    "Invalid value. Try again: ",
                    0.1F, 999999
            );
            harvestedCrop.setUnitValue(unitValue);

            farm.addGoodToStock(harvestedCrop);
            System.out.println(harvestedCrop.type + " added to stock!");
        } catch (RepeatedInstanceException e) {
            LoggerService.warn(e.getMessage());
        }
    }

    public static void sellGoods(Farm farm) {
        Good chosenGood = MenuService.chooseGood(farm.getStock());
        if (chosenGood == null) {
            System.out.println("Back to main menu...");
            return;
        }

        try {
            farm.sellGood(chosenGood);
        } catch (MissingGoodException e) {
            LoggerService.error(e.getMessage());
        }
        System.out.printf("%s sold for $%.2f%n", chosenGood.type, chosenGood.getTotalValue());
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

        int chosenOpt = InputService.readInt(
                sb + "\nChoose a crop: ",
                "This option does not exist. Try again: ",
                0, index
        );

        if (chosenOpt == 0) return null;
        return finalList.get(chosenOpt - 1);
    }

}
