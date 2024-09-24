package labaFarm.farm;

import java.util.List;

public interface IIdentifiable<T> {
    int getNewId(List<T> existingObjects);
}
