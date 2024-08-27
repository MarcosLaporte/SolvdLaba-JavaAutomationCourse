package farm.animals.interfaces;

import farm.Good;
import farm.exceptions.UnableToProduceException;

public interface Milker {
    Good milk() throws UnableToProduceException;
}
