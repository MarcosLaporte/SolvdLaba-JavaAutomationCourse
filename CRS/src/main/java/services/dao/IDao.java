package services.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface IDao<T> {
    List<T> get(Map<String, Object> fieldValueFilters);

    int create(T t);

    int update(
            @Param("values") Map<String, Object> newValues,
            @Param("filters") Map<String, Object> fieldValueFilters
    );

    int delete(Map<String, Object> fieldValueFilters);
}
