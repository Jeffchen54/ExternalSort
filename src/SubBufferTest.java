import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
 * @author chenj (chenjeff4840)
 * @version 11.2.2021
 */
public class SubBufferTest extends TestCase {

    // Fields ---------------------------------------------------------------
    private SubBuffer buffer;
    private InputBuffer input;

    // Setup ------------------------------------------------------------
    /**
     * Initialize buffers and set up input file with normal single block file
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void setUp() throws FileNotFoundException, IOException {
        input = new InputBuffer(new RandomAccessFile("normalBlk.bin", "r"));
        buffer = new SubBuffer();
    }

    // Tests ------------------------------------------------------------


    /**
     * Tests setData() and constructors
     * 
     * @throws IOException
     */
    public void testSetData() throws IOException {
        // Sets buffer data to a valid block
        buffer.setData(input.getData());
        assertTrue(input.endOfFile());
        input.next(8);
        double key = input.nextDouble(8);

        // Check if the buffer returns the correct key, flushing buffer as well
        assertEquals(7.25837957933813E-309, buffer.getKey(), 0.1);
        assertEquals(key, buffer.getKey(), 0.1);
        buffer.flush();
        assertTrue(buffer.isFlushed());

        // Setting buffer data to bad sized blocks, should pass since
        // inputbuffer handles it
        input.changeFile(new RandomAccessFile("TooBigBlock.bin", "r"));
        buffer.setData(input.getData());
        assertFalse(buffer.isFlushed());
        input.changeFile(new RandomAccessFile("TooSmallBlock.bin", "r"));
        buffer.setData(input.getData());
        assertFalse(buffer.isFlushed());

        // Doing the same as above but with constructor
        // Sets buffer data to a valid block
        buffer = new SubBuffer(input.getData());
        assertTrue(input.endOfFile());

        // Check if the buffer returns the correct key, flushing buffer as well
        assertEquals(7.25837957933813E-309, buffer.getKey(), 0.1);
        assertEquals(key, buffer.getKey(), 0.1);

        // Setting buffer data to bad sized blocks, ensuring buffer still
        // flushed
        input.changeFile(new RandomAccessFile("TooBigBlock.bin", "r"));
        buffer = new SubBuffer(input.getData());
        assertFalse(buffer.isFlushed());
        input.changeFile(new RandomAccessFile("TooSmallBlock.bin", "r"));
        buffer = new SubBuffer(input.getData());
        assertFalse(buffer.isFlushed());

        // Setting input to sample input data and simulating rapid info transfer
        input.changeFile(new RandomAccessFile("sampleInput16.bin", "r"));
        int iterations = 0;

        while (!input.endOfFile()) {
            buffer.setData(input.getData());

            input.next(8);
            key = input.nextDouble(8);

            assertEquals(key, buffer.getKey(), 0.1);
            assertFalse(buffer.isFlushed());
            iterations++;
            input.nextBlock();
        }

        // Last block skipped in loop
        buffer.setData(input.getData());

        input.next(8);
        key = input.nextDouble(8);

        assertEquals(key, buffer.getKey(), 0.1);
        assertFalse(buffer.isFlushed());
        iterations++;
        assertEquals(16, iterations);

        // Manually inputting bad sized buffer
        buffer.flush();
        assertTrue(buffer.isFlushed());

        buffer.setData(new byte[8191]);
        assertTrue(buffer.isFlushed());

        buffer.setData(new byte[8193]);
        assertTrue(buffer.isFlushed());

        // w/ constructor
        buffer = new SubBuffer(new byte[8191]);
        assertTrue(buffer.isFlushed());
        buffer = new SubBuffer(new byte[8193]);
        assertTrue(buffer.isFlushed());
        buffer = new SubBuffer(new byte[8192]);
        assertFalse(buffer.isFlushed());

        // Ensuring inserting bad blocks does not affect current data
        buffer.setData(new byte[8191]);
        assertFalse(buffer.isFlushed());

        buffer.setData(new byte[8193]);
        assertFalse(buffer.isFlushed());

    }


    /**
     * Tests flush() and isFlushed()
     */
    public void testFlushed() {
        // Checking empty flush
        assertTrue(buffer.isFlushed());
        assertNull(buffer.flush());
        assertEquals(-1, buffer.getKey(), 0.1);
        assertEquals(null, buffer.getData());

        // Checking valid flush
        buffer.setData(input.getData());
        assertFalse(buffer.isFlushed());
        assertFalse(-1 == buffer.getKey());
        assertNotNull(buffer.getData());
        assertNotNull(buffer.flush());

        // Checking double flush
        assertNull(buffer.flush());
        assertEquals(-1, buffer.getKey(), 0.1);
        assertEquals(null, buffer.getData());
        assertTrue(buffer.isFlushed());
    }


    /**
     * Tests getKey()
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testGetKey() throws FileNotFoundException, IOException {
        // Checking empty getKey()
        assertTrue(buffer.getKey() == -1);

        // Checking double key returns same key
        buffer.setData(input.getData());
        assertFalse(buffer.getKey() == -1);
        double key1 = buffer.getKey();
        assertEquals(key1, buffer.getKey(), 0.1);

        // Flushing data and comparing 1st key to new data
        input.changeFile(new RandomAccessFile("64Blocks_Barnette.bin", "r"));
        buffer.setData(input.getData());
        assertFalse(key1 == buffer.getKey());
    }


    /**
     * Tests compareTo()
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testCompareTo() throws FileNotFoundException, IOException {
        // Null
        buffer.setData(input.getData());
        assertEquals(-1, buffer.compareTo(null));

        // Same
        assertEquals(0, buffer.compareTo(buffer));

        // Different but same data
        SubBuffer other = new SubBuffer(input.getData());
        assertEquals(0, buffer.compareTo(other));

        // Different, greater
        input.changeFile(new RandomAccessFile("BiggerBlockMaybe.bin", "r"));
        other.setData(input.getData());
        assertEquals(-1, buffer.compareTo(other));

        // Different less
        input.changeFile(new RandomAccessFile("SmallerBlockMaybe.bin", "r"));
        other.setData(input.getData());
        assertEquals(1, buffer.compareTo(other));
    }
}
