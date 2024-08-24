package farm;

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

    public void setUnitValue(float unitValue) {
        this.unitValue = unitValue;
    }

    public float getTotalValue() {
        return this.unitValue * this.quantity;
    }

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

    @Override
    public String toString() {
        return String.format("Type: '%s'\nQuantity: %.2f %s\nQuality: '%s'\nProduction Date: %s\nUnit value: %.2f\nTotal value: %.2f\n",
                this.type, this.quantity, this.unitOfMeasure, this.quality, this.productionDate, this.unitValue, this.getTotalValue());
    }

    public static String toString(List<Good> list) {
        if (list.isEmpty()) return "No goods in the list.\n";
        StringBuilder sb = new StringBuilder();

        for (Good gd : list) {
            sb.append(gd);
            sb.append("-----------------------------------\n");
        }

        return sb.toString();
    }

    public static String toTable(List<Good> list) {
        if (list.isEmpty()) return "No crops in the list.\n";
        StringBuilder sb = new StringBuilder();

        sb.append("+--------------+-----------------+-----------+------------+-----------+---------------+\n");
        sb.append(String.format("| %-12s | %-15s | %-9s | %-10s | %-9s | %-13s |\n",
                "TYPE", "QUANTITY", "QUALITY", "PROD. DATE", "UNIT $", "TOTAL $"));
        sb.append("+--------------+-----------------+-----------+------------+-----------+---------------+\n");
        for (Good gd : list) {
            String quantityStr = String.format("%.2f %s", gd.quantity, gd.unitOfMeasure);
            sb.append(String.format("| %-12s | %-15s | %-9s | %-10s | $%8.2f | $%12.2f |\n",
                    gd.type, quantityStr, gd.quality , gd.productionDate, gd.unitValue, gd.getTotalValue()));
        sb.append("+--------------+-----------------+-----------+------------+-----------+---------------+\n");
        }

        return sb.toString();
    }
}
