package labaFarm.farm;

import java.util.List;

@FunctionalInterface
public interface IIdentifiable<T> {
    int getNewId(List<T> existingObjects);
}
