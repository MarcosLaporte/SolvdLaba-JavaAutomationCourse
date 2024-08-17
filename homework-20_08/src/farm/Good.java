package farm;

import farm.crops.Crop; //TODO: Implement this

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class Good {
    public enum GoodsQuality {
        EXCELLENT, GOOD, MID, BAD;

        private static final Random RANDOM = new Random();
        public static GoodsQuality getRandomQuality() {
            GoodsQuality[] values = GoodsQuality.values();
            return values[RANDOM.nextInt(values.length)];
        }
    }

    public String type;
    public float quantity;
    public String unitOfMeasure;
    public GoodsQuality quality;
    public LocalDate productionDate;
    protected float unitValue;

    public Good(String type, float quantity, String unitOfMeasure, GoodsQuality quality, LocalDate productionDate, float unitValue) {
        this.type = type;
        this.quantity = quantity;
        this.unitOfMeasure = unitOfMeasure;
        this.quality = quality;
        this.productionDate = productionDate;
        this.unitValue = unitValue;
    }

    public Good(String type, float quantity, String unitOfMeasure, GoodsQuality quality, float unitValue) {
        this(type, quantity, unitOfMeasure, quality, LocalDate.now(), unitValue);
    }

    public String toString() {
        return String.format("Type='%s'\nQuantity=%.2f %s\nQuality='%s'\nProduction Date=%s\nUnit value: %f.2\n",
                this.type, this.quantity, this.unitOfMeasure, this.quality, this.productionDate, this.unitValue);
    }

    public static String toString(List<Good> list) {
        if (list.isEmpty()) return "No goods in the list.\n";
        StringBuilder sb = new StringBuilder();

        for (Good go : list) {
            sb.append(go);
            sb.append("-----------------------------------\n");
        }

        return sb.toString();
    }
}
