package entitiesDAO;

import entities.annotations.Column;
import entities.annotations.Id;
import entities.annotations.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import services.connection.ConnectionManager;
import services.DatabaseService;
import services.LoggerService;
import services.ReflectionService;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class GenericDAO<T> implements IDao<T>, AutoCloseable {
    private final Connection connection;
    public final Class<T> clazz;

    private final ReflectionService<T> reflectionService;

    public GenericDAO(Class<T> clazz) {
        try {
            this.connection = ConnectionManager.getConnection();
            this.reflectionService = new ReflectionService<>(clazz);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.clazz = clazz;
    }

    @Override
    public void close() {
        ConnectionManager.releaseConnection(connection);
    }

    @Override
    public List<T> getAll() {
        String query = "SELECT * FROM " + this.getTableName();

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            return DatabaseService.parseResultSet(resultSet, clazz);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T get(long id) {
        String query;
        try {
            query = "SELECT * FROM " + this.getTableName() + " WHERE " + this.getIdFieldName() + " = " + id;
        } catch (MultipleIdFieldsException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return null;
        }

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            return DatabaseService.parseResultSet(resultSet, clazz).getFirst();
        } catch (NoSuchElementException e) {
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T getRow(long rowNumber) {
        String query;
        try {
            query = "SELECT * FROM " + this.getTableName() + " ORDER BY " + this.getIdFieldName() + " LIMIT 1 OFFSET " + (rowNumber - 1);
        } catch (MultipleIdFieldsException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return null;
        }

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            return DatabaseService.parseResultSet(resultSet, clazz).getFirst();
        } catch (NoSuchElementException e) {
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long create(T t) {
        List<Field> fieldList = reflectionService.getFieldsByAnnotation(Column.class)
                .stream().filter(field -> !field.getAnnotation(Column.class).autoIncrement())
                .toList();

        String[] columns = fieldList.stream().map(field -> field.getAnnotation(Column.class).name()).toArray(String[]::new);
        String placeholders = String.join(", ", Collections.nCopies(columns.length, "?"));

        String query = "INSERT INTO " + this.getTableName() + " (" + String.join(", ", columns) + ") VALUES (" + placeholders + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < fieldList.size(); i++) {
                Field field = fieldList.get(i);
                field.setAccessible(true);
                Object value = field.get(t);

                preparedStatement.setObject(i + 1, value);
            }

            return preparedStatement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(long id, Object[] params) {
        List<Field> fieldList = reflectionService.getFieldsByAnnotation(Column.class)
                .stream().filter(field -> !field.getAnnotation(Column.class).autoIncrement())
                .toList();

        if (fieldList.size() != params.length)
            return;

        String setColumns = fieldList.stream()
                .map(field -> field.getAnnotation(Column.class).name() + " = ?")
                .collect(Collectors.joining(", "));

        String query;
        try {
            query = "UPDATE " + this.getTableName() + " SET " + setColumns + " WHERE " + this.getIdFieldName() + " = " + id;
        } catch (MultipleIdFieldsException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        String query;
        try {
            query = "DELETE FROM " + this.getTableName() + " WHERE " + this.getIdFieldName() + " = " + id;
        } catch (MultipleIdFieldsException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (NoSuchElementException e) {
            LoggerService.log(Level.WARN, e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> getAllMatches(Map<String, Object> columnValueMap) {
        Set<String> keys = columnValueMap.keySet();
        String whereClause = keys.isEmpty() ? StringUtils.EMPTY :
                " WHERE " + keys.stream()
                        .map(key -> String.format("%s = '%s'", key, columnValueMap.get(key)))
                        .collect(Collectors.joining(" AND "));

        String query = "SELECT * FROM " + this.getTableName() + whereClause;

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            return DatabaseService.parseResultSet(resultSet, clazz);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTableName() {
        Table tableAnn = this.clazz.getAnnotation(Table.class);
        return tableAnn != null ? tableAnn.name() : this.clazz.getSimpleName();
    }

    private String getIdFieldName() throws MultipleIdFieldsException {
        List<Field> idField = reflectionService.getFieldsByAnnotation(Id.class, Column.class);
        if (idField.size() > 1)
            throw new MultipleIdFieldsException();

        return idField.isEmpty() ? "id" : (idField.getFirst()).getAnnotation(Column.class).name();
    }

    @SuppressWarnings("unchecked")
    public static <T> GenericDAO<T> castDAO(GenericDAO<?> dao) {
        return (GenericDAO<T>) dao;
    }

    @SuppressWarnings("unchecked")
    public static <T> ReflectionService<T> castReflectionService(ReflectionService<?> rs) {
        return (ReflectionService<T>) rs;
    }

}