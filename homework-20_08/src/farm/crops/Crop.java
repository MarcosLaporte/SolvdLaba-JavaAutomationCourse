package farm.crops;

import java.util.List;

public abstract class Crop {
    public enum CropType {WHEAT, CORN, TOMATO}
    public enum GrowthStage {
        SEEDLING,
        VEGETATIVE,
        FLOWERING,
        FRUITING,
        MATURITY,
        HARVEST,
        HARVESTED,
    }

    public CropType type;
    public int acres;
    public int daysToGrow;
    public GrowthStage currentGrowthStage;

    public boolean isHarvested() {
        return isHarvested;
    }
    protected void setHarvested(boolean harvested) {
        isHarvested = harvested;
    }

    private boolean isHarvested;

    public Crop(CropType type, int acres, int daysToGrow, GrowthStage currentGrowthStage) {
        this.type = type;
        this.acres = acres;
        this.daysToGrow = daysToGrow;
        this.currentGrowthStage = currentGrowthStage;
        this.isHarvested = currentGrowthStage == GrowthStage.HARVESTED;
    }

    public Crop(CropType type, int daysToGrow, GrowthStage currentGrowthStage) {
        this(type, 10, daysToGrow, currentGrowthStage);
    }

    public Crop(CropType type, int acres, int daysToGrow) {
        this(type, acres, daysToGrow, GrowthStage.SEEDLING);
    }

    public Crop(CropType type, int daysToGrow) {
        this(type, 10, daysToGrow, GrowthStage.SEEDLING);
    }

    public String toString() {
        return "Type: " + this.type + "\n" +
                "Acres: " + this.acres + "ac\n" +
                "Days To Grow: " + this.daysToGrow + "\n" +
                "Current Growth Stage: " + this.currentGrowthStage + "\n";
    }

    public static String toString(List<Crop> list) {
        if (list.isEmpty()) return "No crops in the list.\n";
        StringBuilder sb = new StringBuilder();

        for (Crop cr : list) {
            sb.append(cr);
            sb.append("-----------------------------------\n");
        }

        return sb.toString();
    }

    public abstract void harvest();
}
