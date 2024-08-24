package farm.animals.interfaces;

import farm.Good;

public interface Shearable {
    enum FurType {
        FINE, DOWN, MEDIUM, LONG, DOUBLE_COATED;

        private final int value;
        FurType() {
            this.value = this.ordinal()+1;
        }

        public static FurType getFurType(int value) {
            for (FurType wt : FurType.values()) {
                if (wt.value == value)
                    return wt;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (FurType wt : FurType.values()) {
                sb.append(String.format("%d. %s\n", wt.value, wt));
            }

            return sb.toString();
        }
    }

    Good shear() throws Exception;
}
