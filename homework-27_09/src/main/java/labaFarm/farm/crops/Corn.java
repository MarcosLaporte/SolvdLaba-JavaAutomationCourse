package labaFarm.farm.crops;

import java.util.List;

public final class Corn extends CropSector {
    public enum KernelType {
        FLINT, FLOUR, DENT, POP, SWEET, WAXY;

        private final int value;
        KernelType() {
            this.value = this.ordinal() + 1;
        }

        public static KernelType getKernelType(int value) {
            for (KernelType kt : KernelType.values()) {
                if (kt.value == value)
                    return kt;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (KernelType kt : KernelType.values()) {
                sb.append(String.format("%d. %s\n", kt.value, kt));
            }

            return sb.toString();
        }
    }

    public final KernelType kernelType;
    public float avgKernelSize;

    public Corn(int id, float acres, int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType, float avgKernelSize) {
        super(CropType.CORN, id, acres, daysToGrow, currentGrowthStage);
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(int id, float acres, int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType) {
        this(id, acres, daysToGrow, currentGrowthStage, kernelType, 8.5F);
    }

    public Corn(List<CropSector> existingCrops, float acres, int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType, float avgKernelSize) {
        super(CropType.CORN, existingCrops, acres, daysToGrow, currentGrowthStage);
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(List<CropSector> existingCrops, float acres, int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType) {
        this(existingCrops, acres, daysToGrow, currentGrowthStage, kernelType, 8.5F);
    }
}
