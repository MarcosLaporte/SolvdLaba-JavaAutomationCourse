package labaFarm.connectionPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
    private static final int POOL_SIZE = 5;
    private final BlockingQueue<Connection> connectionPool;
    private static volatile ConnectionPool instance;

    private ConnectionPool() {
        this.connectionPool = new LinkedBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            this.connectionPool.add(new Connection(i));
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null)
                    instance = new ConnectionPool();
            }
        }
        return instance;
    }

    public Connection getConnection() throws InterruptedException {
        return this.connectionPool.take();
    }

    public void releaseConnection(Connection connection) {
        this.connectionPool.offer(connection);
    }
}
