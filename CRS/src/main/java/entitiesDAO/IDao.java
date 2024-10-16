package entitiesDAO;

import java.util.List;
import java.util.Map;

public interface IDao<T> {
    List<T> getAll();

    T get(long id);

    int create(T t);

    int update(long id, Map<String, Object> params);

    int delete(long id);
}
