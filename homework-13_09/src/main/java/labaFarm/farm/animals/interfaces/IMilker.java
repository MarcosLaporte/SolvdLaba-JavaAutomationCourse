package labaFarm.farm.animals.interfaces;

import labaFarm.farm.Good;
import labaFarm.farm.exceptions.UnableToProduceException;

@FunctionalInterface
public interface IMilker {
    Good milk() throws UnableToProduceException;
}
