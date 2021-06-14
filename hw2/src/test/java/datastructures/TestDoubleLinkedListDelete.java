package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.fail;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified. You should give your tests
 * with a timeout of 1 second.
 *
 * This test extends the BaseTestDoubleLinkedList class. This means that
 * you can use the helper methods defined within BaseTestDoubleLinkedList.
 * @see BaseTestDoubleLinkedList
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDoubleLinkedListDelete extends BaseTestDoubleLinkedList {


    // Above are some examples of provided assert methods from JUnit,
    // but in these tests you will also want to use a custom assert
    // we have provided you in BaseTestDoubleLinkedList called
    // assertListValidAndMatches. It will check many properties of
    // your DoubleLinkedList so you will want to use it frequently.
    // For usage examples, you can refer to TestDoubleLinkedList,
    // and refer to BaseTestDoubleLinkedList for the method comment.

    @Test(timeout=SECOND)
    public void testDeleteMiddleElement() {

        IList<String> list = new DoubleLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");

         for (int i = 0; i < 5; i++) {
             String item = list.get(1);
             String deleted = list.delete(1);
             assertEquals(item, deleted);
         }
        assertListValidAndMatches(new String[] {"a", "g"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteIndexOfAndDeleteMiddle() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        list.delete(1);
        int index = list.indexOf("b");
        assertEquals(-1, index);
    }

    @Test(timeout=SECOND)
    public void testDeleteUpdateSize() {

        IList<String> list = new DoubleLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        list.delete(1);
        assertEquals(2, list.size());
    }
    //
    // @Test(timeout=SECOND)
    // public void testDeleteFrontElement() {
    //     IList<String> list = new DoubleLinkedList<>();
    //     list.add("a");
    //     list.add("b");
    //     list.add("c");
    //
    //     list.delete(0);
    //     assertListValidAndMatches(new String[] {"b", "c"}, list);
    // }

    @Test(timeout=SECOND)
    public void testBackElement() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        list.delete(2);
        assertListValidAndMatches(new String[] {"a", "b"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteDuplicates() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("a");
        list.add("b");
        list.add("c");

        list.delete(list.indexOf("c"));
        list.delete(list.indexOf("c"));
        assertListValidAndMatches(new String[] {"a", "b", "a", "b"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteSingleElementList() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a");

        list.delete(0);
        assertListValidAndMatches(new String[] {}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteOutOfBoundsThrowException() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a");

        try {
            list.delete(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
        try {
            list.delete(list.size());
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }
    @Test(timeout=SECOND)
    public void testDeleteFrontElement() {
        IList<String> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add("a");
        }
        for (int i = 0; i < 5; i++) {
            list.delete(0);
        }
        assertListValidAndMatches(new String[] {}, list);

        int index = list.indexOf("a");
        assertEquals(-1, index);
    }

    @Test(timeout=SECOND)
    public void testDeleteFrontAndBack() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        for (int i = 0; i < 5; i++) {
            list.delete(0);
            list.delete(list.size() - 1);
        }
        assertListValidAndMatches(new Integer[] {}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteEmpty() {
        IList<String> list = new DoubleLinkedList<>();

        try {
            list.delete(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

    }

    @Test(timeout = SECOND)
    public void testGet() {
        IList<String> list = new DoubleLinkedList<>();

        try {
            list.get(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals("a", list.get(0));

        assertEquals("b", list.get(1));

        assertEquals("c", list.get(2));

        list.delete(1);

        assertEquals("c", list.get(1));
        list.remove();
        try {
            list.get(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

    }
}
