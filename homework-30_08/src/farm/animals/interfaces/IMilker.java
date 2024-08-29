package farm.animals.interfaces;

import farm.Good;
import farm.exceptions.UnableToProduceException;

public interface IMilker {
    Good milk() throws UnableToProduceException;
}
