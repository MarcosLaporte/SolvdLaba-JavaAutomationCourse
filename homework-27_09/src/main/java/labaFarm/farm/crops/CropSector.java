package labaFarm.farm.crops;

import labaFarm.farm.Good;
import labaFarm.farm.IIdentifiable;
import labaFarm.farm.exceptions.UnableToHarvestException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public abstract class CropSector implements IIdentifiable<CropSector> {
    public enum CropType {
        WHEAT("Wheat flour", "Kg"),
        CORN("Ear of corn", "Unit"),
        TOMATO("Tomato", "Unit");

        private final int value;
        public final String goodTypeProduced;
        public final String goodUnitOfMeasure;
        CropType(String typeProduced, String unitOfMeasure) {
            this.value = this.ordinal() + 1;
            this.goodTypeProduced = typeProduced;
            this.goodUnitOfMeasure = unitOfMeasure;
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
        SEEDLING, VEGETATIVE, FLOWERING, FRUITING, MATURITY, HARVEST;

        private final int value;
        GrowthStage() {
            this.value = this.ordinal() + 1;
        }

        public int getValue() {
            return this.value;
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

    public int getId() {
        return this.id;
    }

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

        sb.append("+------+--------+-------+--------------+--------------+\n");
        sb.append(String.format("| %s | %s | %s | %s | %s |\n",
                StringUtils.center("ID", 4),
                StringUtils.center("TYPE", 6),
                StringUtils.center("ACRES", 5),
                StringUtils.center("DAYS TO GROW", 12),
                StringUtils.center("GROWTH STAGE", 12)));
        sb.append("+------+--------+-------+--------------+--------------+\n");
        for (CropSector cr : list) {
            sb.append(String.format("| %-4d | %-6s | %-5.2f | %-12d | %-12s |\n",
                    cr.id, cr.type, cr.acres, cr.daysToGrow, cr.currentGrowthStage));
            sb.append("+------+--------+-------+--------------+--------------+\n");
        }

        return sb.toString();
    }

    public static Good harvest(CropSector cropSector, float quantity, float unitValue) throws UnableToHarvestException {
        if (cropSector.currentGrowthStage != GrowthStage.HARVEST)
            throw new UnableToHarvestException("Can't harvest in this stage of growth!");

        return new Good(cropSector.type.goodTypeProduced, quantity, cropSector.type.goodUnitOfMeasure, Good.GoodsQuality.randomQualitySupplier.get(), LocalDate.now(), unitValue);
    }

    public transient BiFunction<CropSector, CropSector, Integer> compareTo = (x, y) -> (int) (x.acres - y.acres);

}
