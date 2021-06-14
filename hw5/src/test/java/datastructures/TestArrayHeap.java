package datastructures;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.util.Arrays;

//import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;



/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestArrayHeap extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        assertEquals(1, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testBasicAddReflection() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertEquals(3, array[0]);
    }

    @Test(timeout=SECOND)
    public void testUpdateDecrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(0);
        heap.replace(values[2], newValue);

        assertEquals(newValue, heap.removeMin());
        assertEquals(values[0], heap.removeMin());
        assertEquals(values[1], heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testUpdateIncrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{0, 2, 4, 6, 8});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(5);
        heap.replace(values[0], newValue);

        assertEquals(values[1], heap.removeMin());
        assertEquals(values[2], heap.removeMin());
        assertEquals(newValue, heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testRemoveMinAndAdd() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{16, 5, 7, 25, 36, 4, 30, 8, 35, 19, 6, 49, 1});
        IntWrapper[] fakeValues = IntWrapper.createArray(new int[]{16, 49, 7, 25, 36, 4, 30, 8, 35, 19, 6, 53, 1});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        Arrays.sort(values);

        for (IntWrapper value : values) {
            heap.add(value);
        }
        int size = heap.size();
        for (int i = 0; i < size; i++) {
            assertEquals(values[i], heap.removeMin());
        }
        try{
            heap.removeMin();
            fail();
        } catch (EmptyContainerException ex) {
            //pass
        }

    }

    @Test(timeout=SECOND)
    public void testNullAndDuplicates() {
        int[] values = new int[]{16, 49, 7, 25, 36, 4, 30, 8, 35, 19, 6, 53, 1};
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < values.length; i++) {
            heap.add(values[i]);
        }
        assertTrue(heap.contains(49));
        //duplicates
        try {
            heap.add(49);
            fail("Expected InvalidElementException");
        } catch(InvalidElementException e) {
            //do nothing
        }
        //null
        try {
            heap.add(null);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            // do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testAddAndMinMany() {
        int numElements = 2000;
        int[] values = new int[numElements];
        for (int i = 0; i < numElements; i++){
            values[i] = i;
        }
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = values.length - 1; i >= 0; i--) {
            heap.add(values[i]);
        }

        assertEquals(0, heap.peekMin());
        for (int i = 0; i < numElements; i++){
             assertEquals(i, heap.removeMin());
        }


    }

    @Test(timeout=SECOND)
    public void testReplace() {
        int numElements = 10;
        int[] values = new int[numElements];
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < values.length; i++){
            values[i] = i;
        }
        for (int i : values){
            heap.add(i);
        }
        heap.replace(0, 70);
        assertTrue(heap.contains(70));
        for (int i = 1; i < 10; i++){
            assertEquals(i, heap.removeMin());
        }
        assertEquals(70, heap.removeMin());
        assertTrue(heap.isEmpty());
        for (int i : values){
            heap.add(i);
        }
        try {
            for (int i = 0; i < numElements; i++) {
                if (i != 9) {
                    assertTrue(heap.contains(i + 1));
                }
                heap.replace(i, i + 1);
            }
            fail();
        } catch (InvalidElementException e) {
            // Pass
        }
        try {
            heap.replace(0, 0);
            fail();
        } catch (InvalidElementException e){
            // Pass
        }
        for (int i = 0; i < numElements; i++){
            heap.replace(i, i * 15 + 17);
        }
        for (int i = 0; i < numElements; i++){
            assertEquals(i * 15 + 17, heap.removeMin());
        }

    }

    @Test(timeout=SECOND)
    public void testReplaceAgain() {
        int numElements = 5;
        int[] values = new int[numElements];
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < numElements; i++){
            values[i] = i;
        }

        for (int i = 0; i < values.length; i++) {
            heap.add(i);
        }
        heap.replace(3, -1);
        assertEquals(-1, heap.removeMin());
        assertEquals(0, heap.removeMin());
        heap.add(0);
        assertEquals(0, heap.removeMin());
        try{
            heap.replace(0, 25);
            fail();
        } catch (InvalidElementException e){
            // Pass
        }

        try{
            heap.replace(heap.peekMin(), 2);
            fail();
        } catch (InvalidElementException e){
            // Pass
        }
    }

    @Test(timeout =SECOND)
    public void testPeekMin() {
        int numElements = 10;
        int[] values = new int[numElements];
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < values.length; i++){
            heap.add(i);
        }
        //peekMin
        int size = heap.size();
        for (int i = 0; i < size; i++) {
            assertEquals(i, heap.peekMin());
            assertEquals(i, heap.removeMin());
        }
        try{
            heap.peekMin();
            fail();
        } catch (EmptyContainerException ex) {
            //pass
        }

    }
    @Test(timeout =SECOND)
    public void testRemove() {

        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(6);
        heap.add(29);
        heap.add(13);
        heap.add(7);
        heap.add(4);
        heap.add(14);
        heap.add(15);


        heap.remove(6);
        assertEquals(4, heap.removeMin());
        assertEquals(7, heap.removeMin());
        assertEquals(13, heap.removeMin());
        assertEquals(14, heap.removeMin());
        assertEquals(15, heap.removeMin());
        assertEquals(29, heap.removeMin());

        try{
            heap.remove(29);
            fail();
        } catch (InvalidElementException ex) {
            //pass
        }

        int[] values = new int[50];
        IPriorityQueue<Integer> heap2 = this.makeInstance();
        for (int i = 0; i < values.length; i++){
            heap2.add(i);
        }
        for (int i = 0; i < 24; i++) {
            heap2.remove(i);
            heap2.remove(25 + i);
        }
        assertEquals(24, heap2.removeMin());
        assertEquals(49, heap2.removeMin());
        assertTrue(heap2.isEmpty());
    }

    @Test (timeout = SECOND)
    public void testForbiddenInput() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(6);
        assertTrue(heap.contains(6));
        heap.removeMin();
        assertFalse(heap.contains(6));

        try{
            heap.contains(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //pass
        }
    }


    /**
     * A comparable wrapper class for ints. Uses reference equality so that two different IntWrappers
     * with the same value are not necessarily equal--this means that you may have multiple different
     * IntWrappers with the same value in a heap.
     */
    public static class IntWrapper implements Comparable<IntWrapper> {
        private final int val;

        public IntWrapper(int value) {
            this.val = value;
        }

        public static IntWrapper[] createArray(int[] values) {
            IntWrapper[] output = new IntWrapper[values.length];
            for (int i = 0; i < values.length; i++) {
                output[i] = new IntWrapper(values[i]);
            }
            return output;
        }

        @Override
        public int compareTo(IntWrapper o) {
            return Integer.compare(val, o.val);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return this.val;
        }

        @Override
        public String toString() {
            return Integer.toString(this.val);
        }
    }

    /**
     * A helper method for accessing the private array inside a heap using reflection.
     */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> Comparable<T>[] getArray(IPriorityQueue<T> heap) {
        return getField(heap, "heap", Comparable[].class);
    }

}
