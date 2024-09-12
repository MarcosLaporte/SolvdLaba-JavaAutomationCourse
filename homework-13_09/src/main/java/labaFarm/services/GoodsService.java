package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.Good;
import labaFarm.farm.animals.*;
import labaFarm.farm.crops.CropSector;
import labaFarm.farm.exceptions.MissingGoodException;
import labaFarm.farm.exceptions.RepeatedInstanceException;
import labaFarm.farm.exceptions.UnableToProduceException;
import org.apache.logging.log4j.Level;

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
            LoggerService.log(Level.WARN, e.getMessage());
        } catch (UnableToProduceException e) {
            LoggerService.log(Level.INFO, e.getMessage());
        }
    }

    public static void manageHarvest(Farm farm) {
        CropSector chosenCr = MenuService.chooseCropToHarvest(farm.cropSectors);
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
            LoggerService.log(Level.WARN, e.getMessage());
        }
    }

    public static void sellGoods(Farm farm) {
        Good chosenGood = MenuService.chooseGood(farm.getStock().toArrayList());
        if (chosenGood == null) {
            System.out.println("Back to main menu...");
            return;
        }

        try {
            farm.sellGood(chosenGood);
        } catch (MissingGoodException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }
        System.out.printf("%s sold for $%.2f%n", chosenGood.type, chosenGood.getTotalValue());
    }
}
