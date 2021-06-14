package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see datastructures.interfaces.IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (this.size > 0) {
            back.next = new Node<T>(back, item, null);
            back = back.next;
        } else {
            front = new Node(null, item, null);
            back = front;
        }
        size++;
    }

    @Override
    public T remove() {
        if (back == null) {
            throw new EmptyContainerException();
        }
        T item = back.data;
        if (back.prev != null) {
            back = back.prev;
            back.next = null;
        } else {
            back = null;
            front = null;
        }
        size--;
        return item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> root;
        if (index > size / 2) {
            root = back;
            for (int i = size - 1; i > index; i--) {
                root = root.prev;
            }
        } else {
            root = front;
            for (int i = 0; i < index; i++) {
                root = root.next;
            }
        }
        return root.data;
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (this.size == 1) {
            front = new Node<T>(null, item, null);
            back = front;
        } else {
            Node<T> root = front;
            if (index == 0) {
                root = root.next;
                root.prev = new Node<T>(null, item, root);
                front = root.prev;
            } else {
                for (int i = 0; i < index - 1; i++) {
                    root = root.next;
                }
                Node<T> temp = root.next.next;
                root.next = new Node<T>(root, item, temp);
                if (temp != null) {
                    temp.prev = root.next;
                } else {
                    back = root.next;
                }
            }
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (this.size == 0 || (this.size == index)){
            add(item);
            size--;
        } else if (index > size / 2){
                Node<T> root = back;
                for (int i = size - 1; i > index; i--) {
                    root = root.prev;
                }
                Node<T> temp = root.prev;
                root.prev = new Node<T>(temp, item, root);
                if (temp != null) {
                    temp.next = root.prev;
                }
        } else {
            if (index == 0) {
                Node<T> root = front;
                front = new Node<T>(null, item, front);
                root.prev = front;
            } else {
                Node<T> root = front;
                for (int i = 0; i < index - 1; i++) {
                    root = root.next;
                }
                Node<T> temp = root.next;
                root.next = new Node<T>(root, item, temp);
                temp.prev = root.next;
            }
        }
        size++;

    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T value;
        if (this.size == 1){
            value = front.data;
            front = null;
            back = null;
        } else if (index > size / 2){
            if (index == this.size - 1){
                value = back.data;
                back = back.prev;
                back.next = null;
            } else {
                Node<T> root = back;
                for (int i = size - 1; i > index - 1; i--) {
                    root = root.prev;
                }
                value = root.prev.data;
                root.prev = root.prev.prev;
                if (root.prev != null) {
                    root.prev.next = root;
                }
            }
        } else {
            if (index == 0){
                value = front.data;
                front = front.next;
                front.prev = null;
            } else {
                Node<T> root = front;
                for (int i = 0; i < index - 1; i++){
                    root = root.next;
                }
                value = root.next.data;
                root.next = root.next.next;
                root.next.prev = root;
            }
        }
        size--;
        return value;
    }

    @Override
    public int indexOf(T item) {

        int count = 0;
        Node<T> root = front;
        while (root != null) {
            if (root.data == null) {
                if (item == null) {
                    return count;
                }
            }
            else if (root.data.equals(item)) {
                return count;
            }
            count++;
            root = root.next;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        return (indexOf(other) != -1);
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            T temp = current.data;
            current = current.next;
            return temp;
        }
    }
}
