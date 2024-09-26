package labaFarm.connectionPool;

public class Connection {
    private final int id;

    public Connection(int id) {
        this.id = id;
    }

    public void use() {
        System.out.println("~ Using connection " + id);
    }
}