package entitiesDAO;

import java.util.List;

public interface IDao<T> {
    List<T> getAll();

    T get(long id);

    T getRow(long rowNumber);

    long create(T t);

    void update(long id, Object[] params);

    void delete(long id);
}
