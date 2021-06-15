package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    private IDictionary<T, Integer> dict;
    private int size;
    private int capacity;
    // Feel free to add more fields and constants.

    public ArrayHeap() {
        dict = new ChainedHashDictionary<T, Integer>();
        size = 0;
        capacity = 10;
        heap = makeArrayOfT(capacity);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * upwards from a given index, if necessary.
     */
    private void percolateUp(int index) {
        if (index != 0 && heap[(index - 1)/4].compareTo(heap[index]) > 0) {
            swap((index - 1)/4, index);
            percolateUp((index - 1)/4);
        }
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * downwards from a given index, if necessary.
     */
    private void percolateDown(int index) {
        if (size > 4*index) {
            int minChildIndex = index;
            for (int i = 1; i < 5; i++) {
                if (4 * index + i >= size){
                    break;
                }
                if (heap[4*index + i].compareTo(heap[minChildIndex]) < 0) {
                    minChildIndex = 4*index + i;
                }
            }
            if (minChildIndex != index) {
                swap(index, minChildIndex);
                percolateDown(minChildIndex);
            }
        }

    }

    /**
     * A method stub that you may replace with a helper method for determining
     * which direction an index needs to percolate and percolating accordingly.
     */
    private void percolate(int index) {
        if (heap[index] != null && heap[index/4] != null && heap[index].compareTo(heap[index/4]) < 0) {
            percolateUp(index);
        } else {
            boolean needToPerc = false;
            for (int i = 1; i < 5; i++) {
                if (4 * index + i < size - 1 && heap[4 * index + i].compareTo(heap[index]) < 0) {
                    needToPerc = true;
                }
            }
            if (needToPerc) {
                percolateDown(index);
            }
        }
    }

    /**
     * A method stub that you may replace with a helper method for swapping
     * the elements at two indices in the 'heap' array.
     */
    private void swap(int a, int b) {
        T temp = heap[a];
        dict.put(heap[a], b);
        dict.put(heap[b], a);
        heap[a] = heap[b];
        heap[b] = temp;
    }

    @Override
    public T removeMin() {
        if (size <= 0) {
            throw new EmptyContainerException();
        }
        T temp = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        percolateDown(0);
        dict.remove(temp);
        return temp;
    }

    @Override
    public T peekMin() {
        if (size <= 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void add(T item) {
        if (contains(item)) {
            throw new InvalidElementException();
        }

        if (size == capacity) {
            capacity *= 2;
            T[] temp = makeArrayOfT(capacity);
            for (int i = 0; i < size; i++) {
                temp[i] = heap[i];
            }
            heap = temp;
        }
        heap[size] = item;
        dict.put(item, size);
        percolateUp(size);
        size++;
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        return dict.containsKey(item);
    }

    @Override
    public void remove(T item) {
        if (!contains(item)) {
            throw new InvalidElementException();
        }
        int index = dict.remove(item);
        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        dict.put(heap[index], index);
        size--;
        percolate(index);
    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (contains(newItem) || !contains(oldItem)) {
            throw new InvalidElementException();
        }
        int indexNew = dict.remove(oldItem);
        heap[indexNew] = newItem;
        dict.put(newItem, indexNew);
        percolate(indexNew);
    }

    @Override
    public int size() {
        return size;
    }
}
