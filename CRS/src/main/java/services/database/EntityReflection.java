package services.database;

import entities.Entity;
import entities.annotations.*;
import services.InputService;
import services.ReflectionService;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static services.ReflectionService.ClassExclusionPredicate.ABSTRACT;

public class EntityReflection<T extends Entity> {

    private final ReflectionService<T> rs;
    public final Class<T> clazz;

    public final List<Field> COLUMN_FIELDS;
    public final List<Field> COLUMN_FIELDS_NOT_AI;

    public EntityReflection(Class<T> clazz) {
        this.rs = new ReflectionService<>(clazz);
        this.clazz = clazz;

        this.COLUMN_FIELDS = Collections.unmodifiableList(rs.getFieldsByAnnotation(Column.class));
        this.COLUMN_FIELDS_NOT_AI = COLUMN_FIELDS.stream()
                .filter(field -> !field.getAnnotation(Column.class).autoIncrement())
                .toList();
    }

    public static Class<? extends Entity> chooseEntity() {
        List<Class<? extends Entity>> classes = ReflectionService.getSubclassesOf(Entity.class, "entities", ABSTRACT)
                .stream().filter(clazz -> clazz.getSuperclass() == Entity.class).toList();

        int classIndex = InputService.selectIndexFromList(classes.stream().map(Class::getSimpleName).toList(), "CANCEL");

        return classIndex == -1 ? null : classes.get(classIndex);
    }

    public T readNewInstance(boolean readAutoIncrementFields) throws Exception {
        List<Field> fields = readAutoIncrementFields ? COLUMN_FIELDS : COLUMN_FIELDS_NOT_AI;
        Object[] paramValues = new Object[fields.size()];

        for (int i = 0; i < fields.size(); i++) {
            Field currField = fields.get(i);

            Range rangeAnn = currField.getAnnotation(Range.class);
            Size sizeAnn = currField.getAnnotation(Size.class);
            paramValues[i] = readValue(currField.getType(), currField.getName(), rangeAnn, sizeAnn);
        }

        try {
            return rs.createInstance(paramValues);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new Exception("Error while invoking constructor: " + e.getCause(), e);
        }
    }

    public Map<String, Object> readValues(Field[] fields) {
        Map<String, Object> valuesMap = new HashMap<>();

        do {
            int fieldIndex = InputService.selectIndexFromList(
                    Arrays.stream(fields).map(f -> String.format("[%s] %s", f.getType().getSimpleName(), f.getName())).toList(),
                    "FINISH"
            );

            if (fieldIndex == -1)
                break;

            Field currField = fields[fieldIndex];
            Range rangeAnn = currField.getAnnotation(Range.class);
            Size sizeAnn = currField.getAnnotation(Size.class);
            valuesMap.put(
                    currField.getAnnotation(Column.class).name(),
                    readValue(currField.getType(), currField.getName(), rangeAnn, sizeAnn)
            );

        } while (true);

        return valuesMap;
    }

    public Map<String, Object> readNewValues() {
        return readValues(COLUMN_FIELDS_NOT_AI.toArray(Field[]::new));
    }

    public Map<String, Object> readConditionValues() {
        return readValues(COLUMN_FIELDS.toArray(Field[]::new));
    }

    private Object readValue(Class<?> fieldType, String fieldName, Range rangeAnn, Size sizeAnn) {
        String inputMsg = String.format("Enter [%s] for %s: ", fieldType.getSimpleName(), fieldName);

        if (Number.class.isAssignableFrom(fieldType) || isPrimitiveNumericType(fieldType)) {
            double min = rangeAnn != null ? rangeAnn.min() : Double.MIN_VALUE;
            double max = rangeAnn != null ? rangeAnn.max() : Double.MAX_VALUE;
            String errorMsg = String.format("Invalid value for %s. Try again (%.2f - %.2f): ", fieldName, min, max);

            if (fieldType == Byte.class || fieldType == byte.class)
                return (byte) InputService.readInt(inputMsg, errorMsg, (int) min, (int) max);
            else if (fieldType == Short.class || fieldType == short.class)
                return (short) InputService.readInt(inputMsg, errorMsg, (int) min, (int) max);
            else if (fieldType == Integer.class || fieldType == int.class)
                return InputService.readInt(inputMsg, errorMsg, (int) min, (int) max);
            else if (fieldType == Long.class || fieldType == long.class)
                return (long) InputService.readInt(inputMsg, errorMsg, (int) min, (int) max);
            else if (fieldType == Float.class || fieldType == float.class)
                return InputService.readFloat(inputMsg, errorMsg, (float) min, (float) max);
            else if (fieldType == Double.class || fieldType == double.class)
                return (double) InputService.readFloat(inputMsg, errorMsg, (float) min, (float) max);

        } else if (fieldType == String.class) {
            int min = sizeAnn != null ? sizeAnn.min() : 0;
            int max = sizeAnn != null ? sizeAnn.max() : Integer.MAX_VALUE;
            return InputService.readString(inputMsg, min, max);
        } else if (fieldType == LocalDate.class) {
            return InputService.readValidDate();
        }
        return null;
    }

    private static boolean isPrimitiveNumericType(Class<?> fieldType) {
        return fieldType == int.class || fieldType == long.class || fieldType == double.class ||
                fieldType == float.class || fieldType == short.class || fieldType == byte.class;
    }

    public Map<String, Field> matchColumnField() {
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : this.clazz.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                field.setAccessible(true);
                fieldMap.put(column.name(), field);
            }
        }

        return fieldMap;
    }

    public List<T> filterListByFields(List<T> baseList, Map<String, Object> fieldValueFilters) {
        Map<String, Field> fieldMap = this.matchColumnField();
        if (fieldValueFilters == null || fieldValueFilters.isEmpty())
            return baseList;

        return baseList.stream()
                .filter(el -> fieldValueFilters.entrySet().stream().allMatch(
                        entry -> {
                            Field field = fieldMap.get(entry.getKey());
                            try {
                                return field != null && field.get(el).equals(entry.getValue());
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                ).collect(Collectors.toList());
    }
}
