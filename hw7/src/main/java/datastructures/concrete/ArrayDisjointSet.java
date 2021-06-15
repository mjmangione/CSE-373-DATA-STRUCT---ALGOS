package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * @see IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more fields and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    IDictionary<T, Integer> map;
    int size;

    public ArrayDisjointSet() {
        map = new ChainedHashDictionary<>();
        pointers = new int[10];
        size = 0;
    }

    @Override
    public void makeSet(T item) {
        if (map.containsKey(item)){
            throw new IllegalArgumentException();
        }
        if (size == pointers.length){
            int[] temp = new int[2 * size];
            for (int i = 0; i < size; i++){
                temp[i] = pointers[i];
            }
            pointers = temp;
        }
        pointers[size] = -1;
        map.put(item, size);
        size++;
    }

    @Override
    public int findSet(T item) {
        if (!map.containsKey(item)){
            throw new IllegalArgumentException();
        }
        int index = map.get(item);
        int oldIndex = index;
        while (pointers[index] >= 0){
            index = pointers[index];
        }
        while (pointers[oldIndex] >= 0){
            int temp = pointers[oldIndex];
            pointers[oldIndex] = index;
            oldIndex = temp;
        }
        return index;
    }

    @Override
    public void union(T item1, T item2) {
        int node1 = findSet(item1);
        int node2 = findSet(item2);
        if (node1 != node2) {
            int rank1 = pointers[node1];
            int rank2 = pointers[node2];

            if (rank1 < rank2) {
                pointers[node2] = node1;
                pointers[node1] = rank1;
            } else if (rank2 < rank1) {
                pointers[node1] = node2;
                pointers[node2] = rank2;
            } else {
                pointers[node2] = node1;
                pointers[node1] = rank1 - 1;
            }
        }
    }
}
