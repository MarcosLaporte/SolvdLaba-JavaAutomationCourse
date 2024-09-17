package labaFarm.farm.animals.interfaces;

import labaFarm.farm.Good;
import labaFarm.farm.exceptions.UnableToProduceException;

public interface IMilker {
    Good milk() throws UnableToProduceException;
}
