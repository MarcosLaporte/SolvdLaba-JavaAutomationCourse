package labaFarm.farm.crops;

import labaFarm.farm.Good;
import labaFarm.farm.IIdentifiable;
import labaFarm.farm.exceptions.UnableToHarvestException;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public abstract class CropSector implements IIdentifiable<CropSector> {
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
    private int id;
    public float acres;
    public int daysToGrow;
    public GrowthStage currentGrowthStage;

    public int getId() { return this.id; }

    public CropSector(CropType type, int id, float acres, int daysToGrow, GrowthStage currentGrowthStage) {
        this.id = id;
        this.type = type;
        this.acres = acres;
        this.daysToGrow = daysToGrow;
        this.currentGrowthStage = currentGrowthStage;
    }

    @Override
    public int getNewId(List<CropSector> existingObjects) {
        Stream<CropSector> sameCropTypeStream = existingObjects.stream().filter(c -> c.type == this.type);
        Optional<CropSector> maxIdCrop = sameCropTypeStream.max(Comparator.comparingInt(cr -> cr.id));

        return maxIdCrop.isPresent() ? maxIdCrop.get().id + 1 : this.type.value * 1000 + 1;
    }

    public CropSector(CropType type, List<CropSector> existingCrops, float acres, int daysToGrow, GrowthStage currentGrowthStage) {
        this(type, -1, acres, daysToGrow, currentGrowthStage);

        this.id = this.getNewId(existingCrops);
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

        sb.append("+--------+------+-------+--------------+--------------+\n");
        sb.append(String.format("| %s | %s | %s | %s | %s |\n",
                StringUtils.center("TYPE", 6),
                StringUtils.center("ID", 4),
                StringUtils.center("ACRES", 5),
                StringUtils.center("DAYS TO GROW", 12),
                StringUtils.center("GROWTH STAGE", 12)));
        sb.append("+--------+------+-------+--------------+--------------+\n");
        for (CropSector cr : list) {
            sb.append(String.format("| %-4d | %-6s | %-5.2f | %-12d | %-12s |\n",
                    cr.id, cr.type, cr.acres, cr.daysToGrow, cr.currentGrowthStage));
            sb.append("+--------+------+-------+--------------+--------------+\n");
        }

        return sb.toString();
    }

    public abstract Good harvest(float unitValue) throws UnableToHarvestException;

    public transient BiFunction<CropSector, CropSector, Integer> compareTo = (x, y) -> (int)(x.acres - y.acres);

}
