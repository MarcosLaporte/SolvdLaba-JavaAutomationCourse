package labaFarm.myCollections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Iterable<E> {
    private Node<E> head;
    private int size;

    public int getSize() {
        return size;
    }

    public MyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void add(E data) {
        Node<E> newNode = new Node<>(data);

        if (this.head == null) {
            this.head = newNode;
        } else {
            Node<E> current = this.head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public void add(E data, int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > this.size)  // Correct index check
            throw new IndexOutOfBoundsException(index);

        Node<E> newNode = new Node<>(data);

        if (index == 0) {
            newNode.next = this.head;
            this.head = newNode;
        } else {
            Node<E> current = this.head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    public Node<E> get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException(index);

        int count = 0;
        Node<E> node = this.head;
        while (count < index) {
            node = node.next;
            count++;
        }

        return node;
    }

    public void set(E data, int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException(index);

        Node<E> current = this.head;
        for (int count = 0; count < index - 1; count++) {
            current = current.next;
        }

        current.data = data;
    }

    public void remove(E data) {
        if (this.head == null) return;

        if (this.head.data.equals(data)) {
            this.head = head.next;
            this.size--;
            return;
        }

        Node<E> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                size--;
                return;
            }
            current = current.next;
        }
    }

    public void remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException(index);

        Node<E> current = this.head;
        if (index == 0) {
            this.head = this.head.next; // Remove the head node
        } else {
            for (int count = 0; count < index - 1; count++) {
                current = current.next;
            }
            current.next = current.next.next;
        }

        size--;
    }

    public void clear() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (E el : this) {
            sb.append(el).append(" -> ");
        }
        sb.append("null");

        return sb.toString();
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public MyLinkedList<E> clone() {
        MyLinkedList<E> newLL = new MyLinkedList<>();
        for (E el : this) {
            newLL.add(el);
        }

        return newLL;
    }

    public boolean contains(E data) {
        for (E el : this) {
            if (el.equals(data))
                return true;
        }

        return false;
    }

    public ArrayList<E> toArrayList() {
        ArrayList<E> list = new ArrayList<>();
        for (E el : this) {
            list.add(el);
        }

        return list;
    }

    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<E> {
        private Node<E> current = head;

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E data = this.current.data;
            this.current = this.current.next;
            return data;
        }
    }
}
