package misc;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class Sorter {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "largest".
     *
     * If the input list contains fewer than 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     * @throws IllegalArgumentException  if input is null
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        IPriorityQueue<T> myHeap = new ArrayHeap<T>();
        int count = 0;
        if (k < 0 || input == null) {
            throw new IllegalArgumentException();
        }
        // while (input.size() > count && k != 0) {
        //     if (myHeap.size() >= k) {
        //         if (myHeap.peekMin().compareTo(input.get(count)) < 0) {
        //             myHeap.add(input.get(count));
        //             myHeap.removeMin();
        //         }
        //     } else {
        //         myHeap.add(input.get(count));
        //     }
        //     count++;
        // }
        if (k > 0) {
            for (T item : input) {
                if (myHeap.size() >= k) {
                    if (myHeap.peekMin().compareTo(item) < 0) {
                        myHeap.add(item);
                        myHeap.removeMin();
                    }
                } else {
                    myHeap.add(item);
                }
            }
        }

        IList<T> output = new DoubleLinkedList<>();
        int j = myHeap.size();
        for (int i = 0; i < j; i++) {
            output.add(myHeap.removeMin());
        }
        return output;
    }
}
