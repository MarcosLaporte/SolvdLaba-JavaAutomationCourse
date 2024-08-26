package farm.crops;

import farm.Good;

import java.time.LocalDate;
import java.util.Random;

public final class Corn extends Crop {
    public enum KernelType {
        FLINT, FLOUR, DENT, POP, SWEET, WAXY;

        private final int value;
        KernelType() {
            this.value = this.ordinal()+1;
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

    public Corn(float acres, int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType, float avgKernelSize) {
        super(CropType.CORN, acres, daysToGrow, currentGrowthStage);
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(float acres, int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType) {
        this(acres, daysToGrow, currentGrowthStage, kernelType, 8.5F);
    }

    public Corn(int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType, float avgKernelSize) {
        super(CropType.CORN, daysToGrow, currentGrowthStage);
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType) {
        this(daysToGrow, currentGrowthStage, kernelType, 8.5F);
    }

    public Corn(float acres, int daysToGrow, KernelType kernelType, float avgKernelSize) {
        super(CropType.CORN, acres, daysToGrow);
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(float acres, int daysToGrow, KernelType kernelType) {
        this(acres, daysToGrow, kernelType, 8.5F);
    }

    public Corn(int daysToGrow, KernelType kernelType, float avgKernelSize) {
        super(CropType.CORN, daysToGrow);
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(int daysToGrow, KernelType kernelType) {
        this(daysToGrow, kernelType, 8.5F);
    }

    @Override
    public Good harvest(float unitValue) throws Exception {
        if (this.currentGrowthStage != GrowthStage.HARVEST) throw new Exception("Can't harvest in this stage of growth!");
        final Random RANDOM = new Random();

        this.currentGrowthStage = GrowthStage.HARVESTED;
        int plantsAmount = RANDOM.nextInt(2800, 3500) * ((int) this.acres);
        int quantity = RANDOM.nextInt(1, 2) * plantsAmount;
        return new Good("Ear of corn", quantity, "Unit", Good.GoodsQuality.getRandomQuality(), LocalDate.now(), unitValue);
    }
}
