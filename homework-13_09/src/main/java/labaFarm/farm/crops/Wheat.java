package labaFarm.farm.crops;

import labaFarm.farm.Good;
import labaFarm.farm.exceptions.UnableToHarvestException;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public final class Wheat extends CropSector {
    public enum WheatVariety {
        HARD_RED_WINTER, HARD_RED_SPRING, SOFT_RED_WINTER, DURUM, HARD_WHITE, SOFT_WHITE;

        private final int value;
        WheatVariety() {
            this.value = this.ordinal()+1;
        }

        public static WheatVariety getWheatVariety(int value) {
            for (WheatVariety wt : WheatVariety.values()) {
                if (wt.value == value)
                    return wt;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (WheatVariety wt : WheatVariety.values()) {
                sb.append(String.format("%d. %s\n", wt.value, wt));
            }

            return sb.toString();
        }
    }

    public final WheatVariety variety;
    public int mgOfGluten;

    public Wheat(int id, float acres, int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety, int mgOfGluten) {
        super(CropType.WHEAT, id, acres, daysToGrow, currentGrowthStage);
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(int id, float acres, int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety) {
        this(id, acres, daysToGrow, currentGrowthStage, variety, 7500);
    }

    public Wheat(List<CropSector> existingCrops, float acres, int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety, int mgOfGluten) {
        super(CropType.WHEAT, existingCrops, acres, daysToGrow, currentGrowthStage);
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(List<CropSector> existingCrops, float acres, int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety) {
        this(existingCrops, acres, daysToGrow, currentGrowthStage, variety, 7500);
    }

    @Override
    public Good harvest(float unitValue) throws UnableToHarvestException {
        if (this.currentGrowthStage != GrowthStage.HARVEST) throw new UnableToHarvestException("Can't harvest in this stage of growth!");
        final Random RANDOM = new Random();

        this.currentGrowthStage = GrowthStage.HARVESTED;
        float quantity = RANDOM.nextFloat(2500, 4000) * this.acres;
        return new Good("Wheat flour", quantity, "Kg", Good.GoodsQuality.randomQualitySupplier.get(), LocalDate.now(), unitValue);
    }
}
