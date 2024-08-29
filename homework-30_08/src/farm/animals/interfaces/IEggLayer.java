package farm.animals.interfaces;

import farm.Good;
import farm.exceptions.UnableToProduceException;

public interface IEggLayer {
    enum EggSize {
        SMALL, MEDIUM, LARGE, EXTRA_LARGE;

        private final int value;
        EggSize() {
            this.value = this.ordinal()+1;
        }

        public static EggSize getEggSize(int value) {
            for (EggSize es : EggSize.values()) {
                if (es.value == value)
                    return es;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (EggSize es : EggSize.values()) {
                sb.append(String.format("%d. %s\n", es.value, es));
            }

            return sb.toString();
        }
    }

    Good getEggs() throws UnableToProduceException;
}
