import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SkipListTest {
    private static final int TIMEOUT = 500;
    private SkipList skipList;

    private static boolean validateNode(SkipNode a, Integer left, Integer right, Integer up, Integer down, Integer data) {
        Integer aLeft = a.getLeft() != null ? a.getLeft().getData() : null;
        Integer aRight = a.getRight() != null ? a.getRight().getData() : null;
        Integer aUp = a.getUp() != null ? a.getUp().getData() : null;
        Integer aDown = a.getDown() != null ? a.getDown().getData() : null;

        return Objects.equals(aLeft, left) && Objects.equals(aRight, right) && Objects.equals(aUp, up)
                && Objects.equals(aDown, down) && Objects.equals(a.getData(), data);
    }

    private static boolean validateSkipList(Integer[][] expected, SkipList list) {
        SkipNode head = list.getHead();
        int i = 0;
        int j = 0;
        while (head != null) {
            SkipNode curr = head;
            Integer left = null;
            while (curr != null) {
                Integer data = expected[i][j];

                Integer up = i > 0 ? expected[i - 1][j] : null;
                Integer down = i < expected.length - 1 ? expected[i + 1][j] : null;

                Integer right = null;
                while (right == null && ++j < expected[i].length) {
                    right = expected[i][j];
                }

                if (!validateNode(curr, left, right, up, down, data)) {
                    return false;
                }

                left = data != null ? data : left;
                curr = curr.getRight();
            }
            head = head.getDown();
            i++;
            j = 0;
        }
        return true;
    }

    @Before
    public void setup() {
        skipList = new SkipList();
    }

    /**
     * [-inf]   [+inf]
     */
    @Test
    public void testConstructor() {
        Integer[][] expected = {
                {Integer.MIN_VALUE, Integer.MAX_VALUE},
        };

        // setup() calls constructor already

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(0, skipList.getSize());
        assertEquals(1, skipList.getLevels());
    }

    /**
     * [-inf]   [+inf]
     *
     * [-inf]   [+inf]
     */
    @Test
    public void testCreateLevelOnce() {
        Integer[][] expected = {
                {Integer.MIN_VALUE, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, Integer.MAX_VALUE},
        };

        skipList.createNewLevel();

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(0, skipList.getSize());
        assertEquals(2, skipList.getLevels());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateLevelToExceedLevelCap() {
        for (int i = 0; i < 5; i++) {
            skipList.createNewLevel();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveLevelOnSingleLevel() {
        skipList.removeTopLevel();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeHeads() {
        skipList.add(10, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExceedsLevelCap() {
        skipList.add(10, 5);
    }

    /**
     * [-inf]   [10]   [-inf]
     */
    @Test(timeout = TIMEOUT)
    public void testAddOneElementZeroHeads() {
        Integer[][] expected = {
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
        };

        skipList.add(10, 0);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(1, skipList.getSize());
        assertEquals(1, skipList.getLevels());
    }

    /**
     * [-inf]   [10]    [+inf]
     *
     * [-inf]   [10]    [+inf]
     */
    @Test(timeout = TIMEOUT)
    public void testAddOneElementOneHead() {
        Integer[][] expected = {
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
        };

        skipList.add(10, 1);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(1, skipList.getSize());
        assertEquals(2, skipList.getLevels());
    }

    /**
     * [-inf]   [10]    [-inf]
     *
     * [-inf]   [10]    [-inf]
     */
    @Test(timeout = TIMEOUT)
    public void testAddSameDataSameHeads() {
        Integer[][] expected = {
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
        };

        skipList.add(10, 1);
        skipList.add(10, 1);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(1, skipList.getSize());
        assertEquals(2, skipList.getLevels());
    }

    /**
     * [-inf]   [10]    [-inf]
     *
     * [-inf]   [10]    [-inf]
     *
     * [-inf]   [10]    [-inf]
     *
     * [-inf]   [10]    [-inf]
     *
     * [-inf]   [10]    [-inf]
     */
    @Test(timeout = TIMEOUT)
    public void testAddOneElementMaxHeads() {
        Integer[][] expected = {
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
        };

        skipList.add(10, 4);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(1, skipList.getSize());
        assertEquals(5, skipList.getLevels());
    }

    /**
     * [-inf]   [10]    [-inf]
     *
     * [-inf]   [10]    [-inf]
     *
     * [-inf]   [10]    [-inf]
     *
     * [-inf]   [10]    [-inf]
     */
    @Test(timeout = TIMEOUT)
    public void testAddSameElementsIncreasingHeads() {
        Integer[][] expected = {
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, 10, Integer.MAX_VALUE},
        };

        skipList.add(10, 1);
        skipList.add(10, 3);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(1, skipList.getSize());
        assertEquals(4, skipList.getLevels());
    }

    /**
     * [-inf]                   [33]                    [+inf]
     *
     * [-inf]                   [33]                    [+inf]
     *
     * [-inf]          [10]     [33]                    [+inf]
     *
     * [-inf]          [10]     [33]     [88]           [+inf]
     *
     * [-inf]   [3]    [10]     [33]     [88]    [90]   [+inf]
     */
    @Test()
    public void testAddGeneral() {
        Integer[][] expected = {
                {Integer.MIN_VALUE, null, null, 33, null, null, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, null, null, 33, null, null, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, null,   10, 33, null, null, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, null,   10, 33,   88, null, Integer.MAX_VALUE},
                {Integer.MIN_VALUE,    3,   10, 33,   88,   90, Integer.MAX_VALUE},
        };

        skipList.add(10, 2);
        skipList.add(3, 0);
        skipList.add(88, 1);
        skipList.add(33, 4);
        skipList.add(90, 0);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(5, skipList.getSize());
        assertEquals(5, skipList.getLevels());
    }

    /**
     * [-inf]                   [33]                    [+inf]
     *
     * [-inf]                   [33]                    [+inf]
     *
     * [-inf]          [10]     [33]                    [+inf]
     *
     * [-inf]          [10]     [33]     [88]           [+inf]
     *
     * [-inf]   [3]    [10]     [33]     [88]    [90]   [+inf]
     */
    @Test(timeout = TIMEOUT)
    public void testAddMultipleDataWithDuplicatesAndIncreasingHeads() {
        Integer[][] expected = {
                {Integer.MIN_VALUE, null, null, 33, null, null, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, null, null, 33, null, null, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, null,   10, 33, null, null, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, null,   10, 33,   88, null, Integer.MAX_VALUE},
                {Integer.MIN_VALUE,    3,   10, 33,   88,   90, Integer.MAX_VALUE},
        };

        skipList.add(10, 2);
        skipList.add(10, 0);
        skipList.add(10, 1);
        skipList.add(10, 2);
        skipList.add(3, 0);
        skipList.add(3, 0);
        skipList.add(88, 1);
        skipList.add(33, 3);
        skipList.add(33, 1);
        skipList.add(33, 4);
        skipList.add(90, 0);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(5, skipList.getSize());
        assertEquals(5, skipList.getLevels());
    }

    /**
     * [-inf]    [1]                     [+inf]
     *
     * [-inf]    [1]                     [+inf]
     *
     * [-inf]    [1]             [55]    [+inf]
     *
     * [-inf]    [1]    [10]     [55]    [+inf]
     */
    @Test(timeout = TIMEOUT)
    public void testContainsNotInSkipList() {
        skipList.add(10, 0);
        skipList.add(1, 3);
        skipList.add(55, 1);

        assertFalse(skipList.contains(54));
        assertFalse(skipList.contains(Integer.MAX_VALUE - 1));
        assertFalse(skipList.contains(Integer.MIN_VALUE + 1));
    }

    /**
     * [-inf]   [10]    [+inf]
     */
    @Test(timeout = TIMEOUT)
    public void testContainsDataOnOneLevel() {
        skipList.add(10, 0);

        assertTrue(skipList.contains(10));
    }

    /**
     * [-inf]    [10]     [+inf]
     *
     * [-inf]    [10]     [+inf]
     *
     * [-inf]    [10]     [+inf]
     */
    @Test(timeout = TIMEOUT)
    public void testContainsDataOnMultipleLevels() {
        skipList.add(10, 2);

        assertTrue(skipList.contains(10));
    }


    /**
     * [-inf]    [+inf]
     */
    @Test(timeout = TIMEOUT)
    public void testClear() {
        skipList.add(10, 0);
        skipList.add(12, 2);
        skipList.add(99, 0);
        skipList.add(454, 1);
        Integer[][] expected = {
                {Integer.MIN_VALUE, Integer.MAX_VALUE},
        };

        skipList.clear();

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(0, skipList.getSize());
        assertEquals(1, skipList.getLevels());
    }

    /**
     * [-inf]    [1]                   [+inf]
     *
     * [-inf]    [1]                   [+inf]
     *
     * [-inf]    [1]    [10]    [44]   [+inf]
     */
    @Test(expected = NoSuchElementException.class)
    public void removeNotInSkipList() {
        skipList.add(10, 0);
        skipList.add(1, 2);
        skipList.add(44, 0);
        skipList.remove(55);
    }

    /**
     *  [-inf]   [10]   [+inf]       ==>        [-inf]   [+inf]
     */
    @Test(timeout = TIMEOUT)
    public void testRemoveDataOnOneLevel() {
        skipList.add(10, 0);
        Integer[][] expected = {
                {Integer.MIN_VALUE, Integer.MAX_VALUE},
        };

        int returnValue = skipList.remove(10);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(10, returnValue);
        assertEquals(0, skipList.getSize());
        assertEquals(1, skipList.getLevels());
    }

    /**
     *  [-inf]   [10]   [+inf]
     *                               ==>       [-inf]   [+inf]
     *  [-inf]   [10]   [+inf]
     */
    @Test(timeout = TIMEOUT)
    public void testRemoveDataOnMultipleLevels() {
        skipList.add(10, 1);
        Integer[][] expected = {
                {Integer.MIN_VALUE, Integer.MAX_VALUE},
        };

        int returnValue = skipList.remove(10);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(10, returnValue);
        assertEquals(0, skipList.getSize());
        assertEquals(1, skipList.getLevels());
    }

    /**
     * [-inf]                          [101]     [+inf]
     *
     * [-inf]          [10]            [101]     [+inf]
     *                                                        ==>       [-inf]    [5]    [55]    [+inf]
     * [-inf]          [10]            [101]     [+inf]
     *
     * [-inf]   [5]    [10]    [55]    [101]     [+inf]
     */
    @Test(timeout = TIMEOUT)
    public void testRemoveGeneral() {
        skipList.add(10, 2);
        skipList.add(55, 0);
        skipList.add(101, 3);
        skipList.add(5, 0);
        Integer[][] expected = {
                {Integer.MIN_VALUE, 5, 55, Integer.MAX_VALUE},
        };

        int returnValue1 = skipList.remove(10);
        int returnValue2 = skipList.remove(101);

        assertTrue(validateSkipList(expected, skipList));
        assertEquals(10, returnValue1);
        assertEquals(101, returnValue2);
        assertEquals(2, skipList.getSize());
        assertEquals(1, skipList.getLevels());
    }
}