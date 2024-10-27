package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entities.Entity;
import org.apache.logging.log4j.Level;
import services.database.EntityReflection;
import services.database.IDao;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonService<T extends Entity> implements IDao<T> {
    private static final String baseDir = System.getProperty("user.dir") + "\\files\\json\\";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final EntityReflection<T> entRef;

    static {
        MAPPER.registerModule(new JavaTimeModule());
    }

    public final Class<T> clazz;

    public JsonService(Class<T> clazz) {
        this.clazz = clazz;
        this.entRef = new EntityReflection<>(this.clazz);
    }

    private List<T> parse(String fileName) throws IOException {
        File file = new File(baseDir + fileName);
        if (!file.exists())
            return new ArrayList<>();

        return MAPPER.readValue(file,
                MAPPER.getTypeFactory().constructCollectionType(List.class, this.clazz)
        );
    }

    private void serializeList(List<T> list) throws IOException {
        File newFile = new File(baseDir + this.clazz.getSimpleName() + ".json");

        MAPPER.writerWithDefaultPrettyPrinter()
                .writeValue(newFile, list);
    }

    @Override
    public List<T> get(Map<String, Object> fieldValueFilters) {
        try {
            List<T> elements = parse(this.clazz.getSimpleName() + ".json");

            return this.entRef.filterListByFields(elements, fieldValueFilters);
        } catch (IOException | IllegalStateException e) {
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
        } catch (IOException e) {
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
        } catch (IOException e) {
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
        } catch (IOException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
        }

        return elementsToDelete.size();
    }

}
