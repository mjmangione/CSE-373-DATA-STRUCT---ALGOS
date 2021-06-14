package misc;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSorter extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testBoundaryCases() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(5);
        list.add(10);
        list.add(8);
        list.add(7);
        list.add(13);
        list.add(21);
        list.add(64);
        list.add(9);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(75);
        list.add(1);
        list.add(14);
        list.add(52);
        list.add(19);
        int[] top5 = {19, 21, 52, 64, 75};
        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(top5[i], top.get(i));
        }
        IList<Integer> top1 = Sorter.topKSort(1, list);
        assertEquals(75, top1.get(0));

        IList<Integer> all = Sorter.topKSort(list.size(), list);
        int[] topAll = {1, 2, 3, 4, 5, 7, 8, 9, 10, 13, 14, 19, 21, 52, 64, 75};
        assertEquals(topAll.length, all.size());
        for (int i = 0; i < topAll.length; i++) {
            assertEquals(topAll[i], all.get(i));
        }

    }

    @Test(timeout=SECOND)
    public void testEmptyLists() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> top = Sorter.topKSort(5, list);
        assertTrue(top.isEmpty());
        list.add(5);
        list.add(4);
        list.add(32);
        list.add(1);
        list.add(2);
        list.add(42);
        list.add(8);
        list.add(9);
        list.add(52);
        IList<Integer> top0 = Sorter.topKSort(0, list);
        assertTrue(top0.isEmpty());
        IList<Integer> emptyList = null;
        try {
            IList<Integer> topE = Sorter.topKSort(5, emptyList);
            fail();
        } catch (IllegalArgumentException ex) {
            //good job
        }
        try {
            IList<Integer> topE = Sorter.topKSort(-1, list);
            fail();
        } catch (IllegalArgumentException ex) {
            //good job
        }
    }

    @Test(timeout=SECOND)
    public void testSimpleUsageScaled() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 500; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(500, list);
        assertEquals(500, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testSimpleUsageEveryK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        for (int j = 0; j < 20; j++) {
            IList<Integer> top = Sorter.topKSort(j, list);
            for (int i = j; i < top.size(); i++) {
                assertEquals(j + i, top.get(i));
            }
        }
    }

}
