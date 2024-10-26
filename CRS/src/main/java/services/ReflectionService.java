package services;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public record ReflectionService<T>(Class<T> clazz) {
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
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(packageName + "." + className);
                    } catch (ClassNotFoundException e) {
                        LoggerService.log(Level.WARN, String.format("%s.%s not found.", packageName, className));
                        continue;
                    }

                    for (ClassExclusionPredicate pred : classExclusionPredicates) {
                        if (!pred.predicate.test(clazz)) {
//                            LoggerService.println(clazz.getSimpleName() + " didn't pass predicate: " + pred);
                            continue main;
                        }
                    }

                    classes.add(clazz);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
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

    private boolean matchParameterTypes(Class<?>[] paramTypes, Object[] args) {
        if (paramTypes.length != args.length) return false;
        for (int i = 0; i < paramTypes.length; i++) {
            if (args[i] != null && !paramTypes[i].isAssignableFrom(args[i].getClass())) {
//                LoggerService.println(String.format("Arg%d (%s) does not match with parameter type (%s)", i+1, args[i].getClass(), paramTypes[i]));
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

    public Object getFieldValue(T instance, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(instance);
    }

    public Map<String, Object> getFieldsWithValue(T instance) throws RuntimeException {
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

    public List<Field> getFieldsByAnnotation(Class<? extends Annotation>[] includeAnnotations, Class<? extends Annotation>[] excludeAnnotations) {
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

    @SuppressWarnings("unchecked")
    public static String toString(Object obj) {
        if (obj instanceof List)
            return toString((List<Object>) obj);

        StringBuilder sb = new StringBuilder();
        Class<?> clazz = obj.getClass();
        sb.append(clazz.getSimpleName()).append(" { \n");

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                sb.append('\t').append(field.getName()).append(" = ").append(field.get(obj)).append('\n');
            } catch (IllegalAccessException e) {
                sb.append('\t').append(field.getName()).append(" = [access denied]\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public static String toString(List<Object> objects) {
        if (objects == null || objects.isEmpty()) {
            LoggerService.println("No data to display.");
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        Class<?> clazz = objects.getFirst().getClass();
        sb.append(clazz.getSimpleName()).append('s').append(" { \n");
        for (Object obj : objects) {
            sb.append('\t').append(clazz.getSimpleName()).append(" { \n");
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    sb.append("\t\t").append(field.getName()).append(" = ").append(field.get(obj)).append('\n');
                } catch (IllegalAccessException e) {
                    sb.append("\t\t").append(field.getName()).append(" = [access denied]\n");
                }
            }
            sb.append('\t').append("}\n");
        }
        sb.append("}");

        return sb.toString();
    }

}

