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
     * Tests insert and remove
     * 
     * @throws IOException
     */
    public void testInsertRemove() throws IOException {
        assertEquals(0, heap.heapSize());

        // Empty remove
        assertNull(heap.removeMin());

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
        for (double i = 0; i < 4096; i++) {
            assertEquals(i, heap.removeMin().getKey(), 0.1);
        }

        assertEquals(0, heap.heapSize());

        // Insert 8 blocks with alternating elements
        // Inserts 8 blocks into the heap
        heap.insert(this.makeAltBlock(0, 0));
        assertEquals(1, heap.heapSize());
        heap.insert(this.makeAltBlock(512, 512));
        assertEquals(2, heap.heapSize());
        heap.insert(this.makeAltBlock(1024, 1024));
        assertEquals(3, heap.heapSize());
        heap.insert(this.makeAltBlock(1536, 1536));
        assertEquals(4, heap.heapSize());
        heap.insert(this.makeAltBlock(2048, 2048));
        assertEquals(5, heap.heapSize());
        heap.insert(this.makeAltBlock(2560, 2560));
        assertEquals(6, heap.heapSize());
        heap.insert(this.makeAltBlock(3072, 3072));
        assertEquals(7, heap.heapSize());
        heap.insert(this.makeAltBlock(3584, 3584));
        assertEquals(8, heap.heapSize());

        // Verifying heap elements
        for (double i = 0; i < 4096; i++) {
            assertEquals(i, heap.removeMin().getKey(), 0.1);
        }

        // Insert 8 blocks with alternating elements
        // Inserts 8 blocks into the heap
        heap.insert(this.makeReverseBlock(3584, 3584));
        assertEquals(1, heap.heapSize());
        heap.insert(this.makeReverseBlock(3072, 3072));
        assertEquals(2, heap.heapSize());
        heap.insert(this.makeReverseBlock(2560, 2560));
        assertEquals(3, heap.heapSize());
        heap.insert(this.makeReverseBlock(2048, 2048));
        assertEquals(4, heap.heapSize());
        heap.insert(this.makeReverseBlock(1536, 1536));
        assertEquals(5, heap.heapSize());
        heap.insert(this.makeReverseBlock(1024, 1024));
        assertEquals(6, heap.heapSize());
        heap.insert(this.makeReverseBlock(512, 512));
        assertEquals(7, heap.heapSize());
        heap.insert(this.makeReverseBlock(0, 0));
        assertEquals(8, heap.heapSize());

        // Verifying heap elements
        for (double i = 0; i < 4096; i++) {
            assertEquals(i, heap.removeMin().getKey(), 0.1);
        }

        // Inserting 8 blocks, all with the same data
        // Inserts 8 blocks into the heap
        heap.insert(this.makeBlock(0, 0));
        assertEquals(1, heap.heapSize());
        heap.insert(this.makeBlock(0, 0));
        assertEquals(2, heap.heapSize());
        heap.insert(this.makeBlock(0, 0));
        assertEquals(3, heap.heapSize());
        heap.insert(this.makeBlock(0, 0));
        assertEquals(4, heap.heapSize());
        heap.insert(this.makeBlock(0, 0));
        assertEquals(5, heap.heapSize());
        heap.insert(this.makeBlock(0, 0));
        assertEquals(6, heap.heapSize());
        heap.insert(this.makeBlock(0, 0));
        assertEquals(7, heap.heapSize());
        heap.insert(this.makeBlock(0, 0));
        assertEquals(8, heap.heapSize());

        // Verifying heap elements
        for (double i = 0; i < 512; i++) {
            for (int n = 0; n < 8; n++) {
                assertEquals(i, heap.removeMin().getKey(), 0.1);
            }
        }
    }


    /**
     * Tests insert and remove
     * 
     * @throws IOException
     */
    public void testInsertRemoveRunNum() throws IOException {
        File file = new File("ToDelete.bin");
        file.createNewFile();
        output = new OutputBuffer(new RandomAccessFile("ToDelete.bin", "rw"));

        // Empty remove
        assertEquals(-1, heap.removeMin(output));
        assertNull(output.getLastRecord());

        // Inserting 8 blocks, all with the same data
        // Inserts 8 blocks into the heap
        heap.insert(this.makeBlock(100, 100), 8);
        assertEquals(1, heap.heapSize());
        assertEquals(8, heap.removeMin(null));

        heap.insert(this.makeBlock(50, 50));
        assertEquals(2, heap.heapSize());
        assertEquals(-1, heap.removeMin(null));

        heap.removeMin(output);
        assertEquals(51.0, output.getLastRecord().getKey(), 0.1);
        output.close();
        file.delete();

    }


    /**
     * Tests reactivateHeap()
     * 
     * @throws IOException
     */
    public void testReactivateHeap() throws IOException {
        // Empty reactivate
        heap.reactivateHeap();
        assertNull(heap.removeMin());
        assertEquals(0, heap.heapSize());

        // Reactivating heap with one element
        heap.insert(this.makeBlock(0, 0));
        heap.reactivateHeap();
        assertEquals(1, heap.heapSize());

        // Reactivating when element has been removed
        for (double i = 0; i < 512; i++) {
            assertEquals(i, heap.removeMin().getKey(), 0.1);
        }

        heap.reactivateHeap();
        assertEquals(0, heap.heapSize());

        // Reactivating when Replacement selection removed a block

        // Reactivating when replacement selection disconnected an inserted
        // Block
    }


    /**
     * Tests one cycle replacment selection
     * 
     * @throws IOException
     */
    public void testRS() throws IOException {
        File file = new File("Temp.bin");
        file.createNewFile();
        output = new OutputBuffer(new RandomAccessFile("Temp.bin", "rw"));

        // Calling RS on empty heap
        assertFalse(heap.replacementSelection(null, output));

        // Doing Replacement selection with no new input
        heap.insert(this.makeBlock(0, 0));
        heap.insert(this.makeBlock(512, 512));
        heap.insert(this.makeBlock(1024, 1024));
        heap.insert(this.makeBlock(1536, 1536));

        while (heap.heapSize() != 0) {
            heap.replacementSelection(null, output);
        }

        output.flush();
        output.close();

        // Verifying
        RandomAccessFile inputFile = new RandomAccessFile("Temp.bin", "r");

        for (int i = 0; i < 2048; i++) {
            assertEquals(i, inputFile.readLong());
            assertEquals(i, inputFile.readDouble(), 0.1);
        }

        inputFile.close();
        file.delete();
        file.createNewFile();
        output = new OutputBuffer(new RandomAccessFile("Temp.bin", "rw"));

        // Doing Replacement Selection with new input > last input value
        heap.insert(this.makeBlock(0, 0));
        heap.insert(this.makeBlock(512, 512));
        heap.insert(this.makeBlock(1536, 1536));
        byte[] inputBlock = this.makeBlock(1024, 1024);

        for (int i = 0; i < 511; i++) {
            assertFalse(heap.replacementSelection(inputBlock, output));
        }

        // This next one will insert the new inputBlock into RS
        assertTrue(heap.replacementSelection(inputBlock, output));

        while (heap.heapSize() != 0) {
            heap.replacementSelection(null, output);
        }
        output.flush();
        output.close();

        // Validation
        inputFile = new RandomAccessFile("Temp.bin", "r");

        for (int i = 0; i < 2048; i++) {
            assertEquals(i, inputFile.readLong());
            assertEquals(i, inputFile.readDouble(), 0.1);
        }

        output = new OutputBuffer(new RandomAccessFile("Temp.bin", "rw"));
        inputFile.close();
        file.delete();
        file.createNewFile();

        // Doing ReplacementSelection with new input < last input value

        heap.insert(this.makeBlock(1024, 1024));
        heap.insert(this.makeBlock(512, 512));
        heap.insert(this.makeBlock(1536, 1536));
        inputBlock = this.makeBlock(0, 0);

        for (int i = 0; i < 511; i++) {
            assertFalse(heap.replacementSelection(inputBlock, output));
        }

        // This next one will insert the new inputBlock into RS
        assertTrue(heap.replacementSelection(inputBlock, output));

        while (heap.heapSize() != 0) {
            heap.replacementSelection(null, output);
        }

        // There should be a block that can be reactivated
        assertEquals(0, heap.heapSize());
        heap.reactivateHeap();
        assertEquals(1, heap.heapSize());

        while (heap.heapSize() != 0) {
            heap.replacementSelection(null, output);
        }

        output.flush();
        output.close();

        // Validation
        inputFile = new RandomAccessFile("Temp.bin", "r");

        for (int i = 512; i < 2048; i++) {
            assertEquals(i, inputFile.readLong());
            assertEquals(i, inputFile.readDouble(), 0.1);
        }
        for (int i = 0; i < 512; i++) {
            assertEquals(i, inputFile.readLong());
            assertEquals(i, inputFile.readDouble(), 0.1);
        }

        inputFile.close();
        file.delete();
        file.createNewFile();

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


    /**
     * Creates a block of size 8192 with alternating bigger and smaller
     * values.
     */
    private byte[] makeAltBlock(long id, double key) {
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        for (int i = 0; i < 512; i++) {
            if (i % 2 == 0) {
                buffer.put(makeRecord(id + 1, key + 1));
            }
            else {
                buffer.put(makeRecord(id - 1, key - 1));
            }
            id++;
            key++;
        }
        return buffer.array();
    }


    /**
     * Creates a block of size 8192 with alternating bigger and smaller
     * values.
     */
    private byte[] makeReverseBlock(long id, double key) {
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        for (int i = 511; i >= 0; i--) {
            buffer.put(makeRecord(id + i, key + i));

        }
        return buffer.array();
    }

}
