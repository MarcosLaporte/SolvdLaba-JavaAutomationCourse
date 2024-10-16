package entitiesDAO;

import entities.annotations.Column;
import entities.annotations.Id;
import entities.annotations.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import services.DatabaseService;
import services.LoggerService;
import services.ReflectionService;
import services.connection.ConnectionManager;

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
    private final List<Field> entColumns;

    public GenericDAO(Class<T> clazz) {
        try {
            this.connection = ConnectionManager.getConnection();
            this.reflectionService = new ReflectionService<>(clazz);
            entColumns = reflectionService.getFieldsByAnnotation(Column.class)
                    .stream().filter(field -> !field.getAnnotation(Column.class).autoIncrement())
                    .toList();
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
            LoggerService.log(Level.ERROR, e.getMessage());
            return new ArrayList<>();
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
            List<T> parsedResult = DatabaseService.parseResultSet(resultSet, clazz);
            return parsedResult.isEmpty() ? null : parsedResult.getFirst();
        } catch (SQLException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return null;
        }
    }

    @Override
    public int create(T t) {
        String[] columns = entColumns.stream().map(field -> field.getAnnotation(Column.class).name()).toArray(String[]::new);
        String placeholders = String.join(", ", Collections.nCopies(columns.length, "?"));

        String query = "INSERT INTO " + this.getTableName() + " (" + String.join(", ", columns) + ") VALUES (" + placeholders + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < entColumns.size(); i++) {
                Object value = reflectionService.getFieldValue(t, entColumns.get(i));

                preparedStatement.setObject(i + 1, value);
            }

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return 0;
        } catch (IllegalAccessException e) {
            LoggerService.log(Level.FATAL, e.getMessage());
            return 0;
        }
    }

    @Override
    public int update(long id, Map<String, Object> params) {
        String setFields = params.entrySet().stream()
                .map(entry -> entry.getKey() + " = '" + entry.getValue() + "'")
                .collect(Collectors.joining(", "));

        String query;
        try {
            query = "UPDATE " + this.getTableName() + " SET " + setFields + " WHERE " + this.getIdFieldName() + " = " + id;
        } catch (MultipleIdFieldsException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return 0;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return 0;
        }
    }

    @Override
    public int delete(long id) {
        String query;
        try {
            query = "DELETE FROM " + this.getTableName() + " WHERE " + this.getIdFieldName() + " = " + id;
        } catch (MultipleIdFieldsException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return 0;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return 0;
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
            LoggerService.log(Level.ERROR, e.getMessage());
            return new ArrayList<>();
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