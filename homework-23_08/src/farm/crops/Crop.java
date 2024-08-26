package farm.crops;

import farm.Good;

import java.util.List;

public abstract class Crop {
    public enum CropType {
        WHEAT, CORN, TOMATO;

        private final int value;
        CropType() {
            this.value = this.ordinal()+1;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (Crop.CropType an : Crop.CropType.values()) {
                sb.append(String.format("%d. %s\n", an.value, an));
            }

            return sb.toString();
        }
    }
    public enum GrowthStage {
        SEEDLING, VEGETATIVE, FLOWERING, FRUITING, MATURITY, HARVEST, HARVESTED;

        private final int value;
        GrowthStage() {
            this.value = this.ordinal()+1;
        }

        public static Crop.GrowthStage getGrowthStage(int value) {
            for (Crop.GrowthStage wt : Crop.GrowthStage.values()) {
                if (wt.value == value)
                    return wt;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (Crop.GrowthStage wt : Crop.GrowthStage.values()) {
                sb.append(String.format("%d. %s\n", wt.value, wt));
            }

            return sb.toString();
        }
    }

    public final CropType type;
    public float acres;
    public int daysToGrow;
    public GrowthStage currentGrowthStage;

    public Crop(CropType type, float acres, int daysToGrow, GrowthStage currentGrowthStage) {
        this.type = type;
        this.acres = acres;
        this.daysToGrow = daysToGrow;
        this.currentGrowthStage = currentGrowthStage;
    }

    public Crop(CropType type, int daysToGrow, GrowthStage currentGrowthStage) {
        this(type, 10, daysToGrow, currentGrowthStage);
    }

    public Crop(CropType type, float acres, int daysToGrow) {
        this(type, acres, daysToGrow, GrowthStage.SEEDLING);
    }

    public Crop(CropType type, int daysToGrow) {
        this(type, 10, daysToGrow, GrowthStage.SEEDLING);
    }

    @Override
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

    public static String toTable(List<Crop> list) {
        if (list.isEmpty()) return "No crops in the list.\n";
        StringBuilder sb = new StringBuilder();

        sb.append("+--------+-------+--------------+--------------+\n");
        sb.append(String.format("| %-6s | %-5s | %-12s | %-12s |\n",
                "TYPE", "ACRES", "DAYS TO GROW", "GROWTH STAGE"));
        sb.append("+--------+-------+--------------+--------------+\n");
        for (Crop cr : list) {
            sb.append(String.format("| %-6s | %-5.2f | %-12d | %-12s |\n",
                    cr.type, cr.acres, cr.daysToGrow, cr.currentGrowthStage));
            sb.append("+--------+-------+--------------+--------------+\n");
        }

        return sb.toString();
    }

    public abstract Good harvest(float unitValue) throws Exception;

}
