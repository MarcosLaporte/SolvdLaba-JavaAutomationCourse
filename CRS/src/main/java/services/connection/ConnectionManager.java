package services.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private static final ConnectionPool connectionPool;

    static {
        try {
            connectionPool = new ConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize ConnectionPool", e);
        }
    }

    public static Connection getConnection() throws InterruptedException {
        return connectionPool.getConnection();
    }

    public static void releaseConnection(Connection connection) {
        connectionPool.releaseConnection(connection);
    }

    public static void closePool() throws SQLException {
        connectionPool.closePool();
    }
}

