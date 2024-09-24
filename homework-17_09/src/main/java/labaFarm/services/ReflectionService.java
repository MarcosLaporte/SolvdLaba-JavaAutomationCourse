package labaFarm.services;

import java.io.File;
import java.lang.reflect.*;
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

    public static List<Class<?>> getClassesInPackage(String packageName, ClassExclusionPredicate... classExclusionPredicates) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        File directory = new File("src/main/java/" + path);

        if (directory.exists()) {
            addClassesFromDirectory(directory, packageName, classes, classExclusionPredicates);
        }

        return classes;
    }

    public static <T> List<Class<? extends T>> getSubclassesOf(Class<T> superClass, String packageName, ClassExclusionPredicate... classExclusionPredicates) throws ClassNotFoundException {
        List<Class<?>> allClasses = getClassesInPackage(packageName, classExclusionPredicates);
        List<Class<? extends T>> filteredClasses = new ArrayList<>();

        for (Class<?> clazz : allClasses) {
            if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
                filteredClasses.add(clazz.asSubclass(superClass));
            }
        }

        return filteredClasses;
    }

    private static void addClassesFromDirectory(File directory, String packageName, List<Class<?>> classes, ClassExclusionPredicate... classExclusionPredicates) throws ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files != null) {
            main:
            for (File file : files) {
                if (file.isDirectory()) {
                    String subPackageName = packageName + "." + file.getName();
                    addClassesFromDirectory(file, subPackageName, classes, classExclusionPredicates);
                } else if (file.getName().endsWith(".java")) {
                    String className = file.getName().substring(0, file.getName().length() - 5);
                    Class<?> clazz = Class.forName(packageName + "." + className);

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

        throw new NoSuchMethodException("No constructor matched with given arguments.");
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

    public void printClassInfo() {
        System.out.println("Class Name: " + this.clazz.getName());

        Method[] methods = this.getAllMethods();
        if (methods.length > 0) {
            System.out.println("Methods: ");
            for (Method method : methods) {
                System.out.print(" + " + method.getName() + "(");
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    System.out.print(parameterTypes[i].getSimpleName());
                    if (i < parameterTypes.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println(")");
            }
        }

        Field[] fields = this.getAllFields();
        if (fields.length > 0) {
            System.out.println("Fields: ");
            for (Field field : fields) {
                System.out.println(" - " + field.getType().getSimpleName() + " " + field.getName());
            }
        }

        Map<String, List<Object>> enums = this.getAllEnums();
        if (!enums.isEmpty()) {
            System.out.println("Enums: ");
            for (Map.Entry<String, List<Object>> entry : enums.entrySet()) {
                System.out.println(" - " + entry.getKey() + ":");
                for (Object constant : entry.getValue()) {
                    System.out.println("   ~ " + constant);
                }
            }
        }
    }
}

