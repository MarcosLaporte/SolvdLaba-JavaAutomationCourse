package farm.people;

import farm.crops.Crop;

import java.util.Iterator;
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

    public String cropsToString() {
        Iterator<Crop> it = this.cropsInCare.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("+--------+-------+--------------+--------------+\n");
        sb.append(String.format("| %-6s | %-5s | %-12s | %-12s |\n",
                "TYPE", "ACRES", "DAYS TO GROW", "GROWTH STAGE"));
        sb.append("+--------+-------+--------------+--------------+\n");
        while (it.hasNext()) {
            sb.append(it.next());
        }
        sb.append("+--------+-------+--------------+--------------+\n");

        return sb.toString();
    }
}
