package entitiesDAO;

import java.util.List;
import java.util.Map;

public interface IDao<T> {
    List<T> get(Map<String, Object> columnCondition);

    int create(T t);

    int update(Map<String, Object> newValues, Map<String, Object> columnCondition);

    int delete(Map<String, Object> columnCondition);
}
