package services.database;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface IDao<T> {
    List<T> get(Map<String, Object> columnCondition);

    int create(T t);

    int update(
            @Param("values") Map<String, Object> newValues,
            @Param("filters") Map<String, Object> columnCondition
    );

    int delete(Map<String, Object> columnCondition);
}
