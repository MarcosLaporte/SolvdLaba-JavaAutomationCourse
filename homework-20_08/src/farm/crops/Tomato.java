package farm.crops;

public class Tomato extends Crop {
    @Override
    public void harvest() {

    }

    public enum TomatoVariety {CHERRY, BEEFSTEAK, BRANDYWINE, BETTER_BOY, BIG_BEEF}

    public TomatoVariety variety;
    public int yieldPerPlant;

    public Tomato(int acres, int daysToGrow, GrowthStage currentGrowthStage,TomatoVariety variety, int yieldPerPlant) {
        super(CropType.TOMATO, acres, daysToGrow, currentGrowthStage);
        this.variety = variety;
        this.yieldPerPlant = yieldPerPlant;
    }
    public Tomato(int acres, int daysToGrow, GrowthStage currentGrowthStage,TomatoVariety variety) {
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

    public Tomato(int acres, int daysToGrow,TomatoVariety variety, int yieldPerPlant) {
        super(CropType.TOMATO, acres, daysToGrow);
        this.variety = variety;
        this.yieldPerPlant = yieldPerPlant;
    }
    public Tomato(int acres, int daysToGrow,TomatoVariety variety) {
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
}
