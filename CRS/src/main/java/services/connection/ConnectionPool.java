package services.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {

    private final BlockingQueue<Connection> connectionPool;
    private final int POOL_SIZE = 10;

    public ConnectionPool() throws SQLException {
        connectionPool = new LinkedBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            connectionPool.add(createConnection());
        }
    }

    private Connection createConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = ConnectionManager.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null)
                throw new IOException("Unable to find db.properties.");

            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.username"),
                props.getProperty("db.password")
        );
    }

    public Connection getConnection() throws InterruptedException {
        return connectionPool.take();
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.offer(connection);
        }
    }

    public void closePool() throws SQLException {
        while (!connectionPool.isEmpty()) {
            Connection connection = connectionPool.poll();
            if (connection != null) {
                connection.close();
            }
        }
    }
}
