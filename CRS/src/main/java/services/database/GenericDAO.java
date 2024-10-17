package services.database;

import entities.annotations.Column;
import entities.annotations.Id;
import entities.annotations.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
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
    public List<T> get(Map<String, Object> columnCondition) {
        String query = "SELECT * FROM " + this.getTableName() + mapToQueryCondition(columnCondition);

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
    public int update(Map<String, Object> newValues, Map<String, Object> columnCondition) {
        if (newValues.isEmpty())
            return 0;

        String setFields = newValues.entrySet().stream()
                .map(entry -> String.format("%s = '%s'", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));

        String query = "UPDATE " + this.getTableName() + " SET " + setFields + mapToQueryCondition(columnCondition);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return 0;
        }
    }

    @Override
    public int delete(Map<String, Object> columnCondition) {
        String query = "DELETE FROM " + this.getTableName() + mapToQueryCondition(columnCondition);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LoggerService.log(Level.ERROR, e.getMessage());
            return 0;
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

    private static String mapToQueryCondition(Map<String, Object> columnValueMap) {
        String whereClause = StringUtils.EMPTY;
        if (!columnValueMap.keySet().isEmpty()) {
            whereClause = " WHERE " + columnValueMap.entrySet().stream()
                    .map(entry -> String.format("%s = '%s'", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining(" AND "));
        }

        return whereClause;
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