package labaFarm.farm.crops;

import labaFarm.farm.Good;
import labaFarm.farm.exceptions.UnableToHarvestException;

import java.util.List;
import java.util.function.BiFunction;

public abstract class CropSector {
    public enum CropType {
        WHEAT, CORN, TOMATO;

        private final int value;
        CropType() {
            this.value = this.ordinal()+1;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (CropType an : CropType.values()) {
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

        public static GrowthStage getGrowthStage(int value) {
            for (GrowthStage wt : GrowthStage.values()) {
                if (wt.value == value)
                    return wt;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (GrowthStage wt : GrowthStage.values()) {
                sb.append(String.format("%d. %s\n", wt.value, wt));
            }

            return sb.toString();
        }
    }

    public final CropType type;
    public float acres;
    public int daysToGrow;
    public GrowthStage currentGrowthStage;

    public CropSector(CropType type, float acres, int daysToGrow, GrowthStage currentGrowthStage) {
        this.type = type;
        this.acres = acres;
        this.daysToGrow = daysToGrow;
        this.currentGrowthStage = currentGrowthStage;
    }

    public CropSector(CropType type, int daysToGrow, GrowthStage currentGrowthStage) {
        this(type, 10, daysToGrow, currentGrowthStage);
    }

    public CropSector(CropType type, float acres, int daysToGrow) {
        this(type, acres, daysToGrow, GrowthStage.SEEDLING);
    }

    public CropSector(CropType type, int daysToGrow) {
        this(type, 10, daysToGrow, GrowthStage.SEEDLING);
    }

    @Override
    public String toString() {
        return "Type: " + this.type + "\n" +
                "Acres: " + this.acres + "ac\n" +
                "Days To Grow: " + this.daysToGrow + "\n" +
                "Current Growth Stage: " + this.currentGrowthStage + "\n";
    }

    public static String toString(List<CropSector> list) {
        if (list.isEmpty()) return "No crops in the list.\n";
        StringBuilder sb = new StringBuilder();

        for (CropSector cr : list) {
            sb.append(cr);
            sb.append("-----------------------------------\n");
        }

        return sb.toString();
    }

    public static String toTable(List<CropSector> list) {
        if (list.isEmpty()) return "No crops in the list.\n";
        StringBuilder sb = new StringBuilder();

        sb.append("+--------+-------+--------------+--------------+\n");
        sb.append(String.format("| %-6s | %-5s | %-12s | %-12s |\n",
                "TYPE", "ACRES", "DAYS TO GROW", "GROWTH STAGE"));
        sb.append("+--------+-------+--------------+--------------+\n");
        for (CropSector cr : list) {
            sb.append(String.format("| %-6s | %-5.2f | %-12d | %-12s |\n",
                    cr.type, cr.acres, cr.daysToGrow, cr.currentGrowthStage));
            sb.append("+--------+-------+--------------+--------------+\n");
        }

        return sb.toString();
    }

    public abstract Good harvest(float unitValue) throws UnableToHarvestException;

    public transient BiFunction<CropSector, CropSector, Integer> compareTo = (x, y) -> (int)(x.acres - y.acres);

}
