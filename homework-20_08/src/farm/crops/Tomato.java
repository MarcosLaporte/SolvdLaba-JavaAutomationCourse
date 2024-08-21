package farm.crops;

import farm.Good;

import java.time.LocalDate;
import java.util.Random;

public class Tomato extends Crop {
    public enum TomatoVariety {
        CHERRY, BEEFSTEAK, BRANDYWINE, BETTER_BOY, BIG_BEEF;

        private final int value;
        TomatoVariety() {
            this.value = this.ordinal()+1;
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

    public TomatoVariety variety;
    public int yieldPerPlant;

    public Tomato(float acres, int daysToGrow, GrowthStage currentGrowthStage,TomatoVariety variety, int yieldPerPlant) {
        super(CropType.TOMATO, acres, daysToGrow, currentGrowthStage);
        this.variety = variety;
        this.yieldPerPlant = yieldPerPlant;
    }
    public Tomato(float acres, int daysToGrow, GrowthStage currentGrowthStage,TomatoVariety variety) {
        this(acres, daysToGrow, currentGrowthStage, variety, 45);
    }

    public Tomato(int daysToGrow, GrowthStage currentGrowthStage,TomatoVariety variety, int yieldPerPlant) {
        super(CropType.TOMATO, daysToGrow, currentGrowthStage);
        this.variety = variety;
        this.yieldPerPlant = yieldPerPlant;
    }
    public Tomato(int daysToGrow, GrowthStage currentGrowthStage,TomatoVariety variety) {
        this(daysToGrow, currentGrowthStage, variety, 45);
    }

    public Tomato(float acres, int daysToGrow,TomatoVariety variety, int yieldPerPlant) {
        super(CropType.TOMATO, acres, daysToGrow);
        this.variety = variety;
        this.yieldPerPlant = yieldPerPlant;
    }
    public Tomato(float acres, int daysToGrow,TomatoVariety variety) {
        this(acres, daysToGrow, variety, 45);
    }

    public Tomato(int daysToGrow,TomatoVariety variety, int yieldPerPlant) {
        super(CropType.TOMATO, daysToGrow);
        this.variety = variety;
        this.yieldPerPlant = yieldPerPlant;
    }
    public Tomato(int daysToGrow,TomatoVariety variety) {
        this(daysToGrow, variety, 45);
    }

    @Override
    public Good harvest(float unitValue) throws Exception {
        if (this.currentGrowthStage != GrowthStage.HARVEST) throw new Exception("Can't harvest in this stage of growth!");
        final Random RANDOM = new Random();

        this.currentGrowthStage = GrowthStage.HARVESTED;
        int plantsAmount = (int)(RANDOM.nextInt(4000, 5000) * this.acres);
        int quantity = this.yieldPerPlant * plantsAmount;
        return new Good("Tomato", quantity, "Unit", Good.GoodsQuality.getRandomQuality(), LocalDate.now(), unitValue);
    }
}
