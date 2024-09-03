package labaFarm.myCollections;

public class Node<T> {
    protected T data;
    protected Node<T> next;

    public T getData() {
        return this.data;
    }

    public Node(T data) {
        this.data = data;
        this.next = null;
    }
}
