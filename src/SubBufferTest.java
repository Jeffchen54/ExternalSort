import java.nio.ByteBuffer;
import student.TestCase;
// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction. - JC

// Java Doc ------------------------------------------------------------------
/**
 * Tests SubBuffer
 * 
 * @author Ben Chen
 * @version 11.20.2021
 */
public class SubBufferTest extends TestCase {

    // Fields ---------------------------------------------------------------
    private SubBuffer sub;

    // Setup ------------------------------------------------------------
    /**
     * Intentionally left empty since test functions have specialized setUps
     */
    public void setUp() {
        // Intentionally left empty
    }


    // Tests ------------------------------------------------------------
    /**
     * test the getRt method
     * with sorted block
     */
    public void testGetRt() {
        byte[] source = makeBlock(600, 40);
        sub = new SubBuffer(source);
        assertEquals(sub.getRt().getKey(), 40.0, 0);
    }


    /**
     * test the getRt method
     * with a the smallest value(55.5) in the middle
     */
    public void testGetRt1() {
        byte[] source = makeBlockRan(600, 80);
        sub = new SubBuffer(source);
        assertEquals(sub.getRt().getKey(), 55.5, 1);
    }


    /**
     * test the getRt method
     * with reverse sorted
     */
    public void testGetRtRe() {
        byte[] source = makeBlockReverse(600, 1000);
        sub = new SubBuffer(source);
        assertEquals(sub.getRt().getKey(), 1001, 1);
    }


    /**
     * test the getRt method
     * with empty heap
     */
    public void testGetRtEmpty() {
        byte[] source = makeBlockReverse(600, 1000);
        sub = new SubBuffer(source);
        for (int i = 0; i < 512; i++) {
            sub.removeRt();
        }
        assertNull(sub.getRt());
    }


    /**
     * test the removeRt method
     * with the sorted block
     */
    public void testRemoveRt() {
        byte[] source = makeBlock(600, 80);
        sub = new SubBuffer(source);
        assertEquals(sub.getRt().getKey(), 80, 1);
        assertEquals(sub.removeRt().getKey(), 80, 1);
        assertEquals(sub.getRt().getKey(), 81, 1);
    }


    /**
     * test the removeRt method
     * with the smallest value(55.5) in the middle
     */
    public void testRemoveRt1() {
        byte[] source = makeBlockRan(600, 80);
        sub = new SubBuffer(source);
        assertEquals(sub.getRt().getKey(), 55.5, 1);
        assertEquals(sub.removeRt().getKey(), 55.5, 1);
        assertEquals(sub.getRt().getKey(), 80.0, 1);
    }


    /**
     * test the removeRt method
     * with reverse sorted
     */
    public void testRemoveRtRE() {
        byte[] source = makeBlockReverse(600, 1000);
        sub = new SubBuffer(source);
        assertEquals(sub.getRt().getKey(), 1001, 1);
        assertEquals(sub.removeRt().getKey(), 1001, 1);
        assertEquals(sub.getRt().getKey(), 1002, 1);
    }


    /**
     * test the removeRt method
     * with empty heap
     */
    public void testRemoveRtEmpty() {
        byte[] source = makeBlockReverse(600, 1000);
        sub = new SubBuffer(source);
        for (int i = 0; i < 512; i++) {
            sub.removeRt();
        }
        assertNull(sub.removeRt());
    }


    /**
     * test get ActiveElemets
     */
    public void testGetActiveElements() {
        byte[] source = makeBlock(600, 80);
        sub = new SubBuffer(source);
        for (int i = 0; i < 50; i++) {
            sub.removeRt();
        }
        assertEquals(sub.getActiveElements(), 462);
    }


    /**
     * test get ActiveElemets
     * with empty heap
     */
    public void testGetActiveElementsEmpty() {
        byte[] source = makeBlock(600, 80);
        sub = new SubBuffer(source);
        for (int i = 0; i < 512; i++) {
            sub.removeRt();
        }
        assertEquals(sub.getActiveElements(), 0);
    }


    /**
     * this test insertBlock with empty heap
     */
    public void testInsertBlockEmpty() {
        byte[] source = makeBlock(600, 80);
        sub = new SubBuffer(source);
        for (int i = 0; i < 512; i++) {
            sub.removeRt();
        }
        assertTrue(sub.insertBlock(source));
        assertEquals(sub.getRt().getKey(), 80, 0);
    }


    /**
     * this test insertBlock with non-empty heap
     */
    public void testInsertBlock() {
        byte[] source = makeBlock(600, 80);
        sub = new SubBuffer(source);
        for (int i = 0; i < 400; i++) {
            sub.removeRt();
        }
        assertFalse(sub.insertBlock(source));
        assertEquals(sub.getRt().getKey(), 480, 0);
    }


    /**
     * test compare to
     */
    public void testCompareTo() {
        byte[] source = makeBlock(600, 80);
        sub = new SubBuffer(source);
        byte[] source1 = makeBlock(600, 80);
        SubBuffer sub1 = new SubBuffer(source1);
        assertEquals(sub.compareTo(sub1), 0);
    }


    /**
     * test compare to
     */
    public void testCompareToSmaller() {
        byte[] source = makeBlock(600, 80);
        sub = new SubBuffer(source);
        byte[] source1 = makeBlock(600, 100);
        SubBuffer sub1 = new SubBuffer(source1);
        assertEquals(sub.compareTo(sub1), -1);
    }


    /**
     * test compare to
     */
    public void testCompareToLarger() {
        byte[] source = makeBlock(600, 80);
        sub = new SubBuffer(source);
        byte[] source1 = makeBlock(600, 40);
        SubBuffer sub1 = new SubBuffer(source1);
        assertEquals(sub.compareTo(sub1), 1);
    }


    /**
     * this is to test if the heap sort correctly
     */
    public void testSortCorrection() {
        byte[] source = makeBlock(600, 80);
        sub = new SubBuffer(source);
        for (int i = 0; i < 511; i++) {
            double first = sub.getRt().getKey();
            sub.removeRt();
            double second = sub.getRt().getKey();
            assertTrue(first < second);
        }
    }


    /**
     * this is to test if the heap reverse sort correctly
     */
    public void testSortCorrectionReverse() {
        byte[] source = makeBlockReverse(600, 80);
        sub = new SubBuffer(source);
        for (int i = 0; i < 511; i++) {
            double first = sub.getRt().getKey();
            sub.removeRt();
            double second = sub.getRt().getKey();
            assertTrue(first < second);
        }
    }


    /**
     * this is to test if the heap randomly sort correctly
     */
    public void testSortCorrectionRan() {
        byte[] source = makeBlockRandom(600, 0);
        sub = new SubBuffer(source);
        for (int i = 0; i < 511; i++) {
            double first = sub.getRt().getKey();
            sub.removeRt();
            double second = sub.getRt().getKey();
            assertTrue(first < second);
        }
    }


    /**
     * Creates a block of size 8192 with first record first id at id and first
     * key
     * at key, additional records increment key and value by 1.
     */
    private byte[] makeBlock(long id, double key) {
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        for (int i = 0; i < 512; i++) {
            buffer.put(makeRecord(id + i, key + i));
        }
        return buffer.array();
    }


    /**
     * Creates a block of size 8192 with first record first id at id and first
     * key
     * at key, additional records increment key and value by 1.
     */
    private byte[] makeBlockRan(long id, double key) {
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        for (int i = 0; i < 100; i++) {
            buffer.put(makeRecord(id + i, key + i));
        }
        buffer.put(makeRecord(40, 55.5));
        for (int i = 101; i < 512; i++) {
            buffer.put(makeRecord(id + i, key + i));
        }
        return buffer.array();
    }


    /**
     * Creates a block of size 8192 with first record first id at id and first
     * key
     * at key, additional records increment key and value by 1.
     */
    private byte[] makeBlockRandom(long id, double key) {
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        for (int i = 0; i < 512; i++) {

            if (i % 2 == 1) {
                buffer.put(makeRecord(id + i, key + i));
            }
            else {
                buffer.put(makeRecord(id + i, key + 1000 - i));
            }
        }
        return buffer.array();
    }


    /**
     * Creates a block of size 8192 with first record first id at id and first
     * key
     * at key, additional records increment key and value by 1.
     */
    private byte[] makeBlockReverse(long id, double key) {
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        for (int i = 512; i > 0; i--) {
            buffer.put(makeRecord(id + i, key + i));
        }
        return buffer.array();
    }


    /**
     * Creates a block of size 8192 with first record first id at id and first
     * key
     * at key, additional records increment key and value by 1.
     */
    private byte[] makeRecord(long id, double key) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(id);
        buffer.putDouble(key);

        return buffer.array();
    }
}
