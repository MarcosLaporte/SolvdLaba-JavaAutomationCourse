package services;

import entities.Entity;
import org.apache.logging.log4j.Level;
import services.database.EntityReflection;
import services.database.IDao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FileDao<T extends Entity> implements IDao<T> {
    public final EntityReflection<T> entRef;
    public final Class<T> clazz;
    public final String filesDir;

    public FileDao(Class<T> clazz, String filesDir) {
        this.clazz = clazz;
        this.entRef = new EntityReflection<>(clazz);
        this.filesDir = filesDir;
    }

    protected abstract List<T> parse(String fileName) throws Exception;

    protected abstract void serializeList(List<T> list) throws Exception;

    @Override
    public List<T> get(Map<String, Object> fieldValueFilters) {
        try {
            List<T> elements = parse(this.clazz.getSimpleName() + ".xml");

            return this.entRef.filterListByFields(elements, fieldValueFilters);
        } catch (Exception e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public int create(T t) {
        List<T> elements = this.get(Map.of());
        elements.add(t);

        try {
            serializeList(elements);
            return 1;
        } catch (Exception e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return 0;
        }
    }

    @Override
    public int update(Map<String, Object> newValues, Map<String, Object> fieldValueFilters) {
        List<T> elements = this.get(Map.of());
        List<T> elementsToUpdate = entRef.filterListByFields(elements, fieldValueFilters);
        Map<String, Field> fieldMap = this.entRef.matchColumnField();
        int updateCount = 0;

        for (T currentEl : elements) {
            if (!elementsToUpdate.contains(currentEl))
                continue;

            for (String column : fieldMap.keySet()) {
                if (!newValues.containsKey(column))
                    continue;

                updateCount++;
                try {
                    Field f = fieldMap.get(column);
                    f.setAccessible(true);
                    f.set(currentEl, newValues.get(column));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error updating field: " + column, e);
                }
            }
        }

        try {
            serializeList(elements);
        } catch (Exception e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }

        return updateCount;
    }


    @Override
    public int delete(Map<String, Object> fieldValueFilters) {
        List<T> elements = this.get(Map.of());
        List<T> elementsToDelete = entRef.filterListByFields(elements, fieldValueFilters);

        try {
            elementsToDelete.forEach(elements::remove);
            serializeList(elements);
        } catch (Exception e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }

        return elementsToDelete.size();
    }

}
