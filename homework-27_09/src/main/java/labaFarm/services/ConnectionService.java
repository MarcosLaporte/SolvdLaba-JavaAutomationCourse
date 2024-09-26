package labaFarm.services;

import labaFarm.connectionPool.Connection;
import labaFarm.connectionPool.ConnectionPool;

import java.util.concurrent.*;

public class ConnectionService {
    private static final ConnectionPool pool = ConnectionPool.getInstance();
    private static final int nThreads = 7;
    private static final int timeoutSecs = 5;

    public static void run() {
        try {
            System.out.println("Running useConnectionWithThreadPool (Standard Thread Pool):");
            useConnectionWithThreadPool();
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }

        System.out.println("\n---------------------------------");

        try {
            System.out.println("\nRunning useConnectionWithFuture (Future):");
            useConnectionWithFuture();
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }

        System.out.println("\n---------------------------------");

        try {
            System.out.println("\nRunning useConnectionWithCompletableFuture (CompletableFuture):");
            useConnectionWithCompletableFuture();
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    /**
     * Synchronous method.
     * Each thread in the pool tries to acquire a connection from the pool.
     * If no connection is available, the thread waits until one is released.
     *
     * @throws InterruptedException – if interrupted while waiting
     */
    private static void useConnectionWithThreadPool() throws InterruptedException {
        try (ExecutorService executorService = Executors.newFixedThreadPool(nThreads)) {

            for (int i = 0; i < nThreads; i++) {
                executorService.submit(() -> {
                    try {
                        Connection connection = pool.getConnection();
                        System.out.println("+ " + Thread.currentThread().getName() + " acquired " + connection);
                        connection.use();
                        TimeUnit.SECONDS.sleep(timeoutSecs);
                        pool.releaseConnection(connection);
                        System.out.println("- " + Thread.currentThread().getName() + " released " + connection);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(15, TimeUnit.SECONDS);
        }
    }

    /**
     * Asynchronous method.
     * Each thread submits a task to acquire a connection asynchronously using Future.
     * futureConnection.get() is called to get the connection, which blocks until the connection is available.
     *
     * @throws InterruptedException – if interrupted while waiting
     */
    private static void useConnectionWithFuture() throws InterruptedException {
        try (ExecutorService executorService = Executors.newFixedThreadPool(nThreads)) {

            for (int i = 0; i < nThreads; i++) {
                Future<Connection> futureConnection = executorService.submit(pool::getConnection);

                executorService.submit(() -> {
                    try {
                        Connection connection = futureConnection.get();
                        System.out.println("+ " + Thread.currentThread().getName() + " acquired " + connection);
                        connection.use();
                        TimeUnit.SECONDS.sleep(timeoutSecs);
                        pool.releaseConnection(connection);
                        System.out.println("- " + Thread.currentThread().getName() + " released " + connection);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(15, TimeUnit.SECONDS);
        }
    }

    /**
     * Fully Asynchronous and Non-blocking method.
     * Uses CompletableFuture.supplyAsync() to acquire a connection in a non-blocking manner.
     * Once the connection is available, the thread proceeds with thenAcceptAsync() to handle the connection, without blocking for the result.
     *
     * @throws InterruptedException – if interrupted while waiting
     */
    private static void useConnectionWithCompletableFuture() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        for (int i = 0; i < nThreads; i++) {
            CompletableFuture<Connection> futureConnection = CompletableFuture.supplyAsync(() -> {
                try {
                    return pool.getConnection();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            futureConnection.thenAcceptAsync(connection -> {
                try {
                    System.out.println("+ " + Thread.currentThread().getName() + " acquired " + connection);
                    connection.use();
                    TimeUnit.SECONDS.sleep(timeoutSecs);
                    pool.releaseConnection(connection);
                    System.out.println("- " + Thread.currentThread().getName() + " released " + connection);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, executorService);
        }

        executorService.shutdown();
        executorService.awaitTermination(15, TimeUnit.SECONDS);
    }
}

