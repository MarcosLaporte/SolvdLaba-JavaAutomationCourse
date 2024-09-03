package labaFarm.farm.animals.interfaces;

import labaFarm.farm.Good;

public interface IShearable {
    enum FurType {
        FINE, DOWN, MEDIUM, LONG, DOUBLE_COATED;

        private final int value;
        FurType() {
            this.value = this.ordinal()+1;
        }

        public static FurType getFurType(int value) {
            for (FurType ft : FurType.values()) {
                if (ft.value == value)
                    return ft;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (FurType ft : FurType.values()) {
                sb.append(String.format("%d. %s\n", ft.value, ft));
            }

            return sb.toString();
        }
    }

    Good shear();
}
