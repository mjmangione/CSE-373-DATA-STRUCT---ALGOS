package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    private final double lambda;
    private int capacity;
    private int size;

    private IDictionary<K, V>[] chains;

    public ChainedHashDictionary() {
        this(0.8);
    }

    public ChainedHashDictionary(double lambda) {
        this.lambda = lambda;
        this.capacity = 10;
        this.size = 0;
        this.chains = new IDictionary[capacity];
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    private int getIndex(K key) {
        if (key == null){
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    @Override
    public V get(K key) {
        int index = 0;
        if (key != null) {
            index = getIndex(key);
        }
        if (chains[index] == null) {
            throw new NoSuchKeyException();
        }
        return chains[index].get(key);
    }

    @Override
    public void put(K key, V value) {
        if ((double) size / capacity >= lambda) {
            capacity *= 2;
            IDictionary<K, V>[] tempChain = makeArrayOfChains(capacity);
            for (int i = 0; i < capacity / 2; i++) {
                if (chains[i] != null) {
                    for (KVPair<K, V> kv : chains[i]) {
                        putHelper(kv, tempChain);
                    }
                }
            }
            chains = tempChain;
        }

        int index = 0;
        if (key != null){
            index = getIndex(key);
        }
        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<K, V>();
            chains[index].put(key, value);
        }
        else {
            if (chains[index].containsKey(key)) {
                size--;
            }
            chains[index].put(key, value);
        }
        size++;
    }

    private void putHelper(KVPair<K, V> kv, IDictionary<K, V>[]tempChains) {
        int index = getIndex(kv.getKey());
        if (tempChains[index] == null) {
            tempChains[index] = new ArrayDictionary<K, V>();
        }
        tempChains[index].put(kv.getKey(), kv.getValue());
    }

    @Override
    public V remove(K key) {
        int index = getIndex(key);
        if (chains[index] == null){
            throw new NoSuchKeyException();
        }
        size--;
        V data = chains[index].remove(key);
        if (chains[index].isEmpty()){
            chains[index] = null;
        }
        return data;
    }

    @Override
    public boolean containsKey(K key) {
        int index = getIndex(key);
        if (index != 0) {
            if (chains[index] != null) {
                return chains[index].containsKey(key);
            }
        }
        return chains[0] != null && chains[0].containsKey(key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private Iterator<KVPair<K, V>> chainITR;
        private int index;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            index = 0;
            while (index < (chains.length - 1) && chains[index] == null) {
                index++;
            }
            if (chains[index] != null) {
                chainITR = chains[index].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            while (index < chains.length - 1) {
                if (chains[index] != null && chainITR.hasNext()){
                    return true;
                }
                index++;
                if (chains[index] != null){
                    chainITR = chains[index].iterator();
                }
            }
            if (index == chains.length - 1) {
                return chainITR != null && chainITR.hasNext();
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (chainITR.hasNext()) {
                return chainITR.next();
            } else {
                index++;
                while (chains[index] == null) {
                    index++;
                }
                chainITR = chains[index].iterator();
                return chainITR.next();
            }
        }
    }
}
