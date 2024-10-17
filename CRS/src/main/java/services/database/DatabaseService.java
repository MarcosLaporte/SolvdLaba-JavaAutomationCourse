package services.database;

import services.ReflectionService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    public static <T> List<T> parseResultSet(ResultSet resultSet, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        if (resultSet == null) return results;

        ReflectionService<T> refServ = new ReflectionService<>(clazz);

        try {
            while (resultSet.next()) {
                T obj = refServ.createInstance(getRowValues(resultSet));

                results.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse ResultSet into list of " + clazz.getName(), e);
        }

        return results;
    }

    public static Object[] getRowValues(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        Object[] values = new Object[columnCount];

        for (int i = 0; i < columnCount; i++) {
            values[i] = resultSet.getObject(i + 1);
        }

        return values;
    }


}
