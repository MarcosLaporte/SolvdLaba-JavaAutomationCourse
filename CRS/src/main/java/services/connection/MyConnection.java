package services.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyConnection implements AutoCloseable {
    private final Connection connection;
    private final ConnectionPool pool;

    public MyConnection(Connection connection, ConnectionPool pool) {
        this.connection = connection;
        this.pool = pool;
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    @Override
    public void close() {
        pool.releaseConnection(connection);
    }
}