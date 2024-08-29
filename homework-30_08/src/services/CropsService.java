package services;

import farm.Farm;
import farm.crops.Corn;
import farm.crops.CropSector;
import farm.crops.Tomato;
import farm.crops.Wheat;
import farm.exceptions.RepeatedInstanceException;
import org.apache.logging.log4j.Level;

public class CropsService {
    public static void createNewCrop(Farm farm) {
        int innerMenuOption = InputService.readInt(
                "\nCreate a new instance of which crop?\n0. GO BACK\n" + CropSector.CropType.getAll() + "Choose an option: ",
                "This option does not exist. Try again: ",
                0, CropSector.CropType.values().length
        );

        if (innerMenuOption == 0) {
            System.out.println("Back to main menu...");
            return;
        }

        CropSector newCropSector = switch (CropSector.CropType.values()[innerMenuOption-1]) {
            case WHEAT -> initWheat();
            case CORN -> initCorn();
            case TOMATO -> initTomato();
        };

        try {
            farm.addCrop(newCropSector);
            System.out.println(newCropSector.type + " created successfully!");
        } catch (RepeatedInstanceException e) {
            LoggerService.log(Level.WARN, e.getMessage());
        }
    }

    private record CropData(
            float acres,
            int daysToGrow,
            CropSector.GrowthStage currentGrowthStage
    ) {
    }

    private static CropData initCrop() {
        float auxAcres = InputService.readFloat(
                "Enter acres to use: ",
                "Invalid value. Try again: ",
                1, 99_999
        );

        int auxDays = InputService.readInt(
                "Enter amount of days to grow: ",
                "Invalid value. Try again: ",
                1, 99_999
        );

        int growthStageVal = InputService.readInt(
                CropSector.GrowthStage.getAll() + "Select current growth stage: ",
                "This option does not exist. Try again: ",
                1, CropSector.GrowthStage.values().length
        );
        CropSector.GrowthStage auxGrowthStage = CropSector.GrowthStage.getGrowthStage(growthStageVal);

        return new CropData(auxAcres, auxDays, auxGrowthStage);
    }

    public static Wheat initWheat() {
        System.out.println("Let's create some wheat crops!");
        CropData cropData = initCrop();

        int wheatVarietyVal = InputService.readInt(
                Wheat.WheatVariety.getAll() + "Select wheat variety: ",
                "This option does not exist. Try again: ",
                1, Wheat.WheatVariety.values().length
        );
        Wheat.WheatVariety auxWheatVariety = Wheat.WheatVariety.getWheatVariety(wheatVarietyVal);

        int auxGluten = InputService.readInt(
                "Enter mg of gluten per 100 grams of wheat: ",
                "Invalid value. Try again: ",
                6000, 15000
        );

        return new Wheat(cropData.acres, cropData.daysToGrow, cropData.currentGrowthStage, auxWheatVariety, auxGluten);
    }

    public static Corn initCorn() {
        System.out.println("Let's create some corn crops!");
        CropData cropData = initCrop();

        int kernelTypeVal = InputService.readInt(
                Corn.KernelType.getAll() + "Select kernel type: ",
                "This option does not exist. Try again: ",
                1, Corn.KernelType.values().length
        );
        Corn.KernelType auxKernelType = Corn.KernelType.getKernelType(kernelTypeVal);

        float auxKernelSize = InputService.readFloat(
                "Enter the size of the kernels: ",
                "Invalid value. Try again: ",
                0.1F, 15
        );

        return new Corn(cropData.acres, cropData.daysToGrow, cropData.currentGrowthStage, auxKernelType, auxKernelSize);
    }

    public static Tomato initTomato() {
        System.out.println("Let's create some tomato crops!");
        CropData cropData = initCrop();

        int tomatoVarietyVal = InputService.readInt(
                Tomato.TomatoVariety.getAll() + "Select tomato variety: ",
                "This option does not exist. Try again: ",
                1, Tomato.TomatoVariety.values().length
        );
        Tomato.TomatoVariety auxTomatoVariety = Tomato.TomatoVariety.getTomatoVariety(tomatoVarietyVal);

        int auxYieldPerPlant = InputService.readInt(
                "Enter average yield of tomato per plant: ",
                "Invalid value. Try again: ",
                1, 15
        );

        return new Tomato(cropData.acres, cropData.daysToGrow, cropData.currentGrowthStage, auxTomatoVariety, auxYieldPerPlant);
    }
}
