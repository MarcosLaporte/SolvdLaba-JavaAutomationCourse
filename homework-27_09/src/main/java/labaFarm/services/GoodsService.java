package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.Good;
import labaFarm.farm.animals.*;
import labaFarm.farm.crops.CropSector;
import labaFarm.farm.crops.HarvestTracker;
import labaFarm.farm.exceptions.MissingGoodException;
import labaFarm.farm.exceptions.RepeatedInstanceException;
import labaFarm.farm.exceptions.UnableToProduceException;
import org.apache.logging.log4j.Level;

import java.util.stream.Stream;

public class GoodsService {
    private Farm farm;

    public GoodsService(Farm farm) {
        this.farm = farm;
    }

    public void manageAnimalGoods() {
        Animal chosenAn = MenuService.chooseAnimal(this.farm.animals);
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

            this.farm.addGoodToStock(chosenAn, goodProduced);
            System.out.println(goodProduced.type + " added to stock!");
        } catch (RepeatedInstanceException e) {
            LoggerService.log(Level.WARN, e.getMessage());
        } catch (UnableToProduceException e) {
            LoggerService.log(Level.INFO, e.getMessage());
        }
    }

    public HarvestTracker trackHarvest() {
        Stream<CropSector> growingCropStream = this.farm.cropSectors.stream()
                .filter(cr -> cr.currentGrowthStage != CropSector.GrowthStage.HARVEST && !cropIsTracked(cr));
        CropSector chosenCr = MenuService.chooseCrop(growingCropStream.toList());

        if (chosenCr == null) {
            System.out.println("Back to main menu...");
            return null;
        }

        return new HarvestTracker(chosenCr);
    }

    private boolean cropIsTracked(CropSector cropSector) {
        Stream<String> activeThreadsNames = (Thread.getAllStackTraces().keySet()).stream().map(Thread::getName);

        String cropThreadName = HarvestTracker.getThreadName(cropSector);
        return activeThreadsNames.anyMatch(thread -> thread.equals(cropThreadName));
    }

    public void manageHarvest(HarvestTracker finishedTracker) {
        CropSector harvestCrop = finishedTracker.cropSector;
        System.out.println("\nBefore harvesting " + harvestCrop.type + ", complete this data.");
        try {
            float unitValue = InputService.readFloat(
                    String.format("Enter market value for one %s of %s: ", harvestCrop.type.goodUnitOfMeasure, harvestCrop.type.goodTypeProduced),
                    "Invalid value. Try again: ",
                    0.1F, 999_999
            );
            float quantity = InputService.readFloat(
                    String.format("Enter amount of %s of %s to harvest: ", harvestCrop.type.goodUnitOfMeasure, harvestCrop.type.goodTypeProduced),
                    "Invalid value. Try again: ",
                    0.1F, 99_999
            );
            Good harvestedCrop = CropSector.harvest(harvestCrop, unitValue, quantity);

            this.farm.addGoodToStock(harvestCrop, harvestedCrop);
            System.out.println(harvestedCrop.type + " added to stock!");
        } catch (RepeatedInstanceException e) {
            LoggerService.log(Level.WARN, e.getMessage());
        }
    }

    public void sellGoods() {
        Good chosenGood = MenuService.chooseGood(this.farm.getStock().toArrayList());
        if (chosenGood == null) {
            System.out.println("Back to main menu...");
            return;
        }

        try {
            this.farm.sellGood(chosenGood);
        } catch (MissingGoodException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }
        System.out.printf("%s sold for $%.2f%n", chosenGood.type, chosenGood.getTotalValue());
    }
}
