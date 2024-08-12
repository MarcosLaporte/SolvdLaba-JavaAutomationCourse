package farm.people;

import farm.crops.Crop;

import java.util.List;

public class CropsCultivator extends Farmer {
    public List<Crop> cropsInCare;
    public FarmerShift shift;

    public CropsCultivator(String fullName, String ssn, float salary, List<Crop> cropsInCare, FarmerShift shift) {
        super(fullName, ssn, salary);
        this.type = FarmerType.CULTIVATOR;
        this.cropsInCare = cropsInCare;
        this.shift = shift;
    }

    public CropsCultivator(String fullName, String ssn, List<Crop> cropsInCare, FarmerShift shift) {
        super(fullName, ssn);
        this.type = FarmerType.CULTIVATOR;
        this.cropsInCare = cropsInCare;
        this.shift = shift;
    }

    public CropsCultivator(String fullName, String ssn, float salary, List<Crop> cropsInCare) {
        this(fullName, ssn, salary, cropsInCare, FarmerShift.MORNING);
    }

    public CropsCultivator(String fullName, String ssn, List<Crop> cropsInCare) {
        this(fullName, ssn, cropsInCare, FarmerShift.MORNING);
    }
}
