package services;

import entities.annotations.Column;
import entities.annotations.Range;
import entities.annotations.Size;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.Date;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReflectionService<T> {
    public enum ClassExclusionPredicate {
        INTERFACE(clazz -> !clazz.isInterface()),
        EXCEPTION(clazz -> !Exception.class.isAssignableFrom(clazz)),
        ABSTRACT(clazz -> !Modifier.isAbstract(clazz.getModifiers())),
        PRIVATE(clazz -> !Modifier.isPrivate(clazz.getModifiers())),
        STATIC(clazz -> !Modifier.isStatic(clazz.getModifiers())),
        ANNOTATION(clazz -> !clazz.isAnnotation()),
        ENUM(clazz -> !clazz.isEnum());

        private final Predicate<Class<?>> predicate;

        ClassExclusionPredicate(Predicate<Class<?>> predicate) {
            this.predicate = predicate;
        }
    }

    private final Class<T> clazz;

    public ReflectionService(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static List<Class<?>> getClassesInPackage(String packageName, ClassExclusionPredicate... classExclusionPredicates) {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        File directory = new File("src/main/java/" + path);

        if (directory.exists()) {
            addClassesFromDirectory(directory, packageName, classes, classExclusionPredicates);
        }

        return classes;
    }

    public static <T> List<Class<? extends T>> getSubclassesOf(Class<T> superClass, String packageName, ClassExclusionPredicate... classExclusionPredicates) {
        List<Class<?>> allClasses = getClassesInPackage(packageName, classExclusionPredicates);
        List<Class<? extends T>> filteredClasses = new ArrayList<>();

        for (Class<?> clazz : allClasses) {
            if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
                filteredClasses.add(clazz.asSubclass(superClass));
            }
        }

        return filteredClasses;
    }

    private static void addClassesFromDirectory(File directory, String packageName, List<Class<?>> classes, ClassExclusionPredicate... classExclusionPredicates) {
        File[] files = directory.listFiles();
        if (files != null) {
            main:
            for (File file : files) {
                if (file.isDirectory()) {
                    String subPackageName = packageName + "." + file.getName();
                    addClassesFromDirectory(file, subPackageName, classes, classExclusionPredicates);
                } else if (file.getName().endsWith(".java")) {
                    String className = file.getName().substring(0, file.getName().length() - 5);
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(packageName + "." + className);
                    } catch (ClassNotFoundException e) {
                        LoggerService.log(Level.WARN, String.format("%s.%s not found.", packageName, className));
                    }

                    for (ClassExclusionPredicate pred : classExclusionPredicates) {
                        if (!pred.predicate.test(clazz)) {
//                            System.out.println(clazz.getSimpleName() + " didn't pass predicate: " + pred);
                            continue main;
                        }
                    }

                    classes.add(clazz);
                }
            }
        }
    }

    public T createInstance(Object... args) throws Exception {
        Constructor<?>[] constructors = this.clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            constructor.setAccessible(true);

            if (matchParameterTypes(paramTypes, args)) {
                try {
                    return (T) constructor.newInstance(args);
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new Exception("Error while invoking constructor: " + e.getCause(), e);
                }
            }
        }

        throw new NoSuchMethodException("No constructor matched with given arguments in class " + this.clazz.getSimpleName());
    }

    public T readNewInstance() throws Exception {
        Constructor<?> constructor = this.clazz.getConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        Object[] paramValues = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter currParam = parameters[i];
            Optional<Field> respectiveField =
                    Arrays.stream(this.getAllFields())
                            .filter(f -> f.getName().equalsIgnoreCase(currParam.getName()))
                            .findFirst();
            Class<?> paramType = currParam.getType();

            if (respectiveField.isEmpty())
                throw new IllegalStateException("No matching field found for constructor parameter " + currParam.getName());

            Range rangeAnn = respectiveField.get().getAnnotation(Range.class);
            Size sizeAnn = respectiveField.get().getAnnotation(Size.class);
            paramValues[i] = readValue(paramType, currParam.getName(), rangeAnn, sizeAnn);
        }

        try {
            return (T) constructor.newInstance(paramValues);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new Exception("Error while invoking constructor: " + e.getCause(), e);
        }
    }

    public Object[] readNewValues(T ogInstance) throws Exception {
        List<Field> fields = this.getFieldsByAnnotation(Column.class)
                .stream().filter(field -> !field.getAnnotation(Column.class).autoIncrement())
                .toList();
        Object[] paramValues = new Object[fields.size()];

        for (int i = 0; i < fields.size(); i++) {
            Field currField = fields.get(i);
            Class<?> fieldType = currField.getType();
            Object fieldValue = currField.get(ogInstance);

            char enterValue = InputService.readCharInValues(
                    String.format("Do you want to change %s (Current value: %s)? Y/N: ", currField.getName(), fieldValue),
                    "ERROR. Input Y or N: ", new char[]{'Y', 'N'}
            );
            if (enterValue == 'N') {
                paramValues[i] = fieldValue;
                continue;
            }

            Range rangeAnn = currField.getAnnotation(Range.class);
            Size sizeAnn = currField.getAnnotation(Size.class);
            paramValues[i] = readValue(fieldType, currField.getName(), rangeAnn, sizeAnn);
        }

        return paramValues;
    }

    public Map<String, Object> readValues() throws Exception {
        List<Field> fields = this.getFieldsByAnnotation(Column.class)
                .stream().filter(field -> !field.getAnnotation(Column.class).autoIncrement())
                .toList();
        Map<String, Object> valuesMap = new HashMap<>();

        for (Field currField : fields) {
            Class<?> fieldType = currField.getType();

            char enterValue = InputService.readCharInValues(
                    String.format("Do you want to enter %s value? Y/N: ", currField.getName()),
                    "ERROR. Input Y or N: ", new char[]{'Y', 'N'}
            );
            if (enterValue == 'N')
                continue;

            Range rangeAnn = currField.getAnnotation(Range.class);
            Size sizeAnn = currField.getAnnotation(Size.class);
            valuesMap.put(
                    currField.getAnnotation(Column.class).name(),
                    readValue(fieldType, currField.getName(), rangeAnn, sizeAnn)
            );
        }

        return valuesMap;
    }

    private Object readValue(Class<?> fieldType, String fieldName, Range rangeAnn, Size sizeAnn) {
        String inputMsg = String.format("Enter %s for %s: ", fieldType.getSimpleName(), fieldName);

        if (Number.class.isAssignableFrom(fieldType) || isPrimitiveNumericType(fieldType)) {
            double min = rangeAnn != null ? rangeAnn.min() : Double.MIN_VALUE;
            double max = rangeAnn != null ? rangeAnn.max() : Double.MAX_VALUE;
            String errorMsg = String.format("Invalid value for %s. Try again (%.2f - %.2f): ", fieldName, min, max);

            if (fieldType == Byte.class || fieldType == Short.class || fieldType == Integer.class || fieldType == Long.class ||
                    fieldType == byte.class || fieldType == short.class || fieldType == int.class || fieldType == long.class) {
                return InputService.readInt(inputMsg, errorMsg, (int) min, (int) max);
            } else if (fieldType == Float.class || fieldType == Double.class || fieldType == float.class || fieldType == double.class) {
                return InputService.readFloat(inputMsg, errorMsg, (float) min, (float) max);
            }
        } else if (fieldType == String.class) {
            int min = sizeAnn != null ? sizeAnn.min() : 0;
            int max = sizeAnn != null ? sizeAnn.max() : Integer.MAX_VALUE;
            return InputService.readString(inputMsg, min, max);
        } else if (fieldType == Date.class) {
            return InputService.readValidDate();
        }
        return null;
    }

    private static boolean isPrimitiveNumericType(Class<?> fieldType) {
        return fieldType == int.class || fieldType == long.class || fieldType == double.class ||
                fieldType == float.class || fieldType == short.class || fieldType == byte.class;
    }

    private boolean matchParameterTypes(Class<?>[] paramTypes, Object[] args) {
        if (paramTypes.length != args.length) return false;
        for (int i = 0; i < paramTypes.length; i++) {
            if (args[i] != null && !paramTypes[i].isAssignableFrom(args[i].getClass())) {
//                System.out.printf("Arg%d (%s) does not match with parameter type (%s)\n", i+1, args[i].getClass(), paramTypes[i]);
                return false;
            }
        }
        return true;
    }

    public Object getFieldValue(T instance, String fieldName) throws Exception {
        Field field = this.clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    public Object getFieldValue(T instance, Field field) throws Exception {
        field.setAccessible(true);
        return field.get(instance);
    }

    public Map<String, Object> getFieldsWithValue(T instance) {
        Map<String, Object> fieldMap = new HashMap<>();
        Field[] fields = this.clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                fieldMap.put(field.getName(), field.get(instance));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return fieldMap;
    }

    public void setFieldValue(T instance, String fieldName, Object value) throws Exception {
        Field field = this.clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    public Object invokeMethod(T instance, String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        Method method = this.clazz.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(instance, args);
    }

    public Method[] getAllMethods() {
        Stream<Method> methodStream = Arrays.stream(this.clazz.getDeclaredMethods()).filter(cl -> !cl.isSynthetic());
        return methodStream.toArray(Method[]::new);
    }

    public Field[] getAllFields() {
        return this.clazz.getDeclaredFields();
    }

    public Map<String, List<Object>> getAllEnums() {
        Map<String, List<Object>> enumsMap = new HashMap<>();

        for (Field field : this.clazz.getDeclaredFields()) {
            if (field.getType().isEnum()) {
                try {
                    field.setAccessible(true);

                    Class<?> enumClass = field.getType();
                    Object[] enumConstants = enumClass.getEnumConstants();

                    List<Object> constantsList = new ArrayList<>();
                    Collections.addAll(constantsList, enumConstants);

                    enumsMap.put(enumClass.getSimpleName(), constantsList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return enumsMap;
    }

    @SafeVarargs
    public final List<Field> getFieldsByAnnotation(Class<? extends Annotation>... annotations) {
        List<Field> fields = new ArrayList<>();
        for (Field field : this.clazz.getDeclaredFields()) {
            if (Arrays.stream(annotations).allMatch(field::isAnnotationPresent)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }

        return fields;
    }

    public final List<Field> getFieldsByAnnotation(Class<? extends Annotation>[] includeAnnotations, Class<? extends Annotation>[] excludeAnnotations) {
        List<Field> fields = new ArrayList<>();
        for (Field field : this.clazz.getDeclaredFields()) {
            boolean includeMatch = (
                    includeAnnotations == null || includeAnnotations.length == 0 ||
                            Arrays.stream(includeAnnotations).allMatch(field::isAnnotationPresent)
            );
            boolean excludeMatch = (
                    excludeAnnotations == null || excludeAnnotations.length == 0 ||
                            Arrays.stream(excludeAnnotations).noneMatch(field::isAnnotationPresent)
            );

            if (includeMatch && excludeMatch) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        return fields;
    }

}

