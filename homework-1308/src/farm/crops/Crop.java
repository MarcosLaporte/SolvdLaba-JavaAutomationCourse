package farm.crops;

public abstract class Crop {
    public enum GrowthStage {
        SEEDLING,
        VEGETATIVE,
        FLOWERING,
        FRUITING,
        MATURITY,
        HARVEST
    }

    public int acres;
    public int daysToGrow;
    public GrowthStage currentGrowthStage;

    public Crop(int acres, int daysToGrow, GrowthStage currentGrowthStage) {
        this.acres = acres;
        this.daysToGrow = daysToGrow;
        this.currentGrowthStage = currentGrowthStage;
    }

    public Crop(int daysToGrow, GrowthStage currentGrowthStage) {
        this(10, daysToGrow, currentGrowthStage);
    }

    public Crop(int acres, int daysToGrow) {
        this(acres, daysToGrow, GrowthStage.SEEDLING);
    }

    public Crop(int daysToGrow) {
        this(10, daysToGrow, GrowthStage.SEEDLING);
    }
}
