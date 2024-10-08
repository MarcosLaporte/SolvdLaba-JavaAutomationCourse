package farm.crops;

public class Corn extends Crop {
    public enum KernelType {FLINT, FLOUR, DENT, POP, SWEET, WAXY}

    public KernelType kernelType;
    public float avgKernelSize;

    public Corn(int acres, int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType, float avgKernelSize) {
        super(acres, daysToGrow, currentGrowthStage);
        this.type = CropType.CORN;
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(int acres, int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType) {
        this(acres, daysToGrow, currentGrowthStage, kernelType, 8.5F);
    }

    public Corn(int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType, float avgKernelSize) {
        super(daysToGrow, currentGrowthStage);
        this.type = CropType.CORN;
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(int daysToGrow, GrowthStage currentGrowthStage, KernelType kernelType) {
        this(daysToGrow, currentGrowthStage, kernelType, 8.5F);
    }

    public Corn(int acres, int daysToGrow, KernelType kernelType, float avgKernelSize) {
        super(acres, daysToGrow);
        this.type = CropType.CORN;
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(int acres, int daysToGrow, KernelType kernelType) {
        this(acres, daysToGrow, kernelType, 8.5F);
    }

    public Corn(int daysToGrow, KernelType kernelType, float avgKernelSize) {
        super(daysToGrow);
        this.type = CropType.CORN;
        this.kernelType = kernelType;
        this.avgKernelSize = avgKernelSize;
    }
    public Corn(int daysToGrow, KernelType kernelType) {
        this(daysToGrow, kernelType, 8.5F);
    }
}
