package labaFarm.farm.crops;

import java.util.List;

public final class Tomato extends CropSector {
    public enum TomatoVariety {
        CHERRY, BEEFSTEAK, BRANDYWINE, BETTER_BOY, BIG_BEEF;

        private final int value;
        TomatoVariety() {
            this.value = this.ordinal() + 1;
        }

        public static TomatoVariety getTomatoVariety(int value) {
            for (TomatoVariety tv : TomatoVariety.values()) {
                if (tv.value == value)
                    return tv;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (TomatoVariety tv : TomatoVariety.values()) {
                sb.append(String.format("%d. %s\n", tv.value, tv));
            }

            return sb.toString();
        }
    }

    public final TomatoVariety variety;
    public int yieldPerPlant;

    public Tomato(int id, float acres, int daysToGrow, GrowthStage currentGrowthStage, TomatoVariety variety, int yieldPerPlant) {
        super(CropType.TOMATO, id, acres, daysToGrow, currentGrowthStage);
        this.variety = variety;
        this.yieldPerPlant = yieldPerPlant;
    }
    public Tomato(int id, float acres, int daysToGrow, GrowthStage currentGrowthStage, TomatoVariety variety) {
        this(id, acres, daysToGrow, currentGrowthStage, variety, 45);
    }

    public Tomato(List<CropSector> existingCrops, float acres, int daysToGrow, GrowthStage currentGrowthStage, TomatoVariety variety, int yieldPerPlant) {
        super(CropType.TOMATO, existingCrops, acres, daysToGrow, currentGrowthStage);
        this.variety = variety;
        this.yieldPerPlant = yieldPerPlant;
    }
    public Tomato(List<CropSector> existingCrops, float acres, int daysToGrow, GrowthStage currentGrowthStage, TomatoVariety variety) {
        this(existingCrops, acres, daysToGrow, currentGrowthStage, variety, 45);
    }
}