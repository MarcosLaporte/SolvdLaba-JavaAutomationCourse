package farm.crops;

public class Wheat extends Crop {
    public enum WheatVariety {
        HARD_RED_WINTER,
        HARD_RED_SPRING,
        SOFT_RED_WINTER,
        DURUM,
        HARD_WHITE,
        SOFT_WHITE
    }

    public WheatVariety variety;
    public int mgOfGluten;

    public Wheat(int acres, int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety, int mgOfGluten) {
        super(acres, daysToGrow, currentGrowthStage);
        this.type = CropType.WHEAT;
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(int acres, int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety) {
        this(acres, daysToGrow, currentGrowthStage, variety, 7500);
    }

    public Wheat(int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety, int mgOfGluten) {
        super(daysToGrow, currentGrowthStage);
        this.type = CropType.WHEAT;
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(int daysToGrow, GrowthStage currentGrowthStage, WheatVariety variety) {
        this(daysToGrow, currentGrowthStage, variety, 7500);
    }

    public Wheat(int acres, int daysToGrow, WheatVariety variety, int mgOfGluten) {
        super(acres, daysToGrow);
        this.type = CropType.WHEAT;
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(int acres, int daysToGrow, WheatVariety variety) {
        this(acres, daysToGrow, variety, 7500);
    }

    public Wheat(int daysToGrow, WheatVariety variety, int mgOfGluten) {
        super(daysToGrow);
        this.type = CropType.WHEAT;
        this.variety = variety;
        this.mgOfGluten = mgOfGluten;
    }
    public Wheat(int daysToGrow, WheatVariety variety) {
        this(daysToGrow, variety, 7500);
    }
}
