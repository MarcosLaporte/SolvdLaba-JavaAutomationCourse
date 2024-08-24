package farm.crops;

import farm.Good;

import java.time.LocalDate;
import java.util.Random;

public final class Wheat extends Crop {
    public enum WheatVariety {
        HARD_RED_WINTER, HARD_RED_SPRING, SOFT_RED_WINTER, DURUM, HARD_WHITE, SOFT_WHITE;

        private final int value;
        WheatVariety() {
            this.value = this.ordinal()+1;
        }

        public static Wheat.WheatVariety getWheatVariety(int value) {
            for (Wheat.WheatVariety wt : Wheat.WheatVariety.values()) {
                if (wt.value == value)
                    return wt;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (Wheat.WheatVariety wt : Wheat.WheatVariety.values()) {
                sb.append(String.format("%d. %s\n", wt.value, wt));
            }

            return sb.toString();
        }
    }

    public WheatVariety variety;
    public int mgOfGluten;

    public Wheat(float acres, int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety, int mgOfGluten) {
        super(CropType.WHEAT, acres, daysToGrow, currentGrowthStage);
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(float acres, int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety) {
        this(acres, daysToGrow, currentGrowthStage, variety, 7500);
    }

    public Wheat(int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety, int mgOfGluten) {
        super(CropType.WHEAT, daysToGrow, currentGrowthStage);
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety) {
        this(daysToGrow, currentGrowthStage, variety, 7500);
    }

    public Wheat(float acres, int daysToGrow, WheatVariety variety, int mgOfGluten) {
        super(CropType.WHEAT, acres, daysToGrow);
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(float acres, int daysToGrow, WheatVariety variety) {
        this(acres, daysToGrow, variety, 7500);
    }

    public Wheat(int daysToGrow, WheatVariety variety, int mgOfGluten) {
        super(CropType.WHEAT, daysToGrow);
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(int daysToGrow, WheatVariety variety) {
        this(daysToGrow, variety, 7500);
    }

    @Override
    public Good harvest(float unitValue) throws Exception {
        if (this.currentGrowthStage != GrowthStage.HARVEST) throw new Exception("Can't harvest in this stage of growth!");
        final Random RANDOM = new Random();

        this.currentGrowthStage = GrowthStage.HARVESTED;
        float quantity = RANDOM.nextFloat(2500, 4000) * this.acres;
        return new Good("Wheat flour", quantity, "Kg", Good.GoodsQuality.getRandomQuality(), LocalDate.now(), unitValue);
    }
}
