import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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
 * Test MainBuffer. Will also tests if Replacement selection and merge
 * are possible with the MainBuffer.
 * 
 * @author chenj (chenjeff4840)
 * @version 11.3.2021
 */
public class MainBufferTest extends TestCase {
    // Fields -------------------------------------------------------------
    private MainBuffer heap;
    private OutputBuffer output;
    private File created;
    private String testingOutput;

    // Setup -------------------------------------------------------------
    /**
     * Sets up an empty heap
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        heap = new MainBuffer();
    }

    // Tests -------------------------------------------------------------


    /**
     * Tests insert
     */
    public void testInsert() {
        assertEquals(0, heap.heapSize());

        // Inserts 8 blocks into the heap
        heap.insert(this.makeBlock(0, 0));
        assertEquals(1, heap.heapSize());
        heap.insert(this.makeBlock(512, 512));
        assertEquals(2, heap.heapSize());
        heap.insert(this.makeBlock(1024, 1024));
        assertEquals(3, heap.heapSize());
        heap.insert(this.makeBlock(1536, 1536));
        assertEquals(4, heap.heapSize());
        heap.insert(this.makeBlock(2048, 2048));
        assertEquals(5, heap.heapSize());
        heap.insert(this.makeBlock(2560, 2560));
        assertEquals(6, heap.heapSize());
        heap.insert(this.makeBlock(3072, 3072));
        assertEquals(7, heap.heapSize());
        heap.insert(this.makeBlock(3584, 3584));
        assertEquals(8, heap.heapSize());

        // Verifying heap elements
    }


    /**
     * Tests insert
     */
    public void testInsertRunNum() {

    }


    /**
     * Tests remove
     */
    public void testRemove() {

    }


    /**
     * Tests remove
     */
    public void testRemoveRunNum() {

    }


    /**
     * Tests reactivateHeap()
     */
    public void testReactivateHeap() {

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

}
