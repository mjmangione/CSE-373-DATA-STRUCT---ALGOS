package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        pairs = makeArrayOfPairs(10);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        int index = indexOf(key);
        if (index == -1){
            throw new NoSuchKeyException();
        } else {
            return pairs[index].value;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = indexOf(key);
        if (index != -1){
            pairs[index].value = value;
        } else {
            if (this.size == pairs.length) {
                Pair<K, V>[] temp = new Pair[this.size * 2];
                for (int i = 0; i < size; i++){
                    temp[i] = pairs[i];
                }
                pairs = temp;
            }
            pairs[this.size] = new Pair<>(key, value);
            this.size++;
        }
    }

    @Override
    public V remove(K key) {
        int index = indexOf(key);
        if (index == -1){
            throw new NoSuchKeyException();
        }
        V data;
        if (index != this.size - 1) {
            data = pairs[index].value;
            pairs[index] = pairs[this.size - 1];
        } else {
            data = pairs[this.size - 1].value;
        }
        this.size--;
        return data;
    }

    @Override
    public boolean containsKey(K key) {
        return indexOf(key) != -1;
    }

    // Returns the index of the passed in key, if found, and returns -1 otherwise
    private int indexOf(K key){
        for (int i = 0; i < size; i++){
            if (Objects.equals(key, pairs[i].key)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    public Iterator<KVPair<K, V>> iterator() {
        KVPair<K, V>[] kvs = new KVPair[size];
        for (int i = 0; i < size; i++) {
            kvs[i] = new KVPair(pairs[i].key, pairs[i].value);
        }

        return new ArrayDictionaryIterator<K, V>(kvs);
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }

    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        // Add any fields you need to store state information
        private KVPair<K, V>[] pairs;
        private int current;
        private int size;

        public ArrayDictionaryIterator(KVPair<K, V>[] input) {
            current = 0;
            pairs = input;
            size = pairs.length;
        }

        public boolean hasNext() {
            return (current < size);
        }

        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current++;
            return pairs[current - 1];
        }
    }
}
