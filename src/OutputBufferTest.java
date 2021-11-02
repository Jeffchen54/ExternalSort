import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.junit.AfterClass;
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
 * Tests the OutputBuffer
 * 
 * @author chenj (chenjeff4840)
 * @version 11.2.2021
 */
public class OutputBufferTest extends TestCase {
    // Fields ------------------------------------------------------------
    private static OutputBuffer buffer;
    private static InputBuffer input;
    static File created;

    // SetUp ------------------------------------------------------------
    /**
     * Sets up the buffer
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        created = new File("testing.bin");
        created.delete();
        created.createNewFile();
        buffer = new OutputBuffer(new RandomAccessFile("testing.bin", "rw"));
        input = new InputBuffer(new RandomAccessFile("sampleInput16.bin", "r"));
    }

    // Tests ------------------------------------------------------------


    /**
     * Tests flush's file component
     * 
     * @throws IOException
     */
    public void testFlush() throws IOException {
        // Inserting data into buffer then flushing
        buffer.setData(input.getData());
        assertNotNull(buffer.flush());

        // Trying to flush again
        assertNull(buffer.flush());
        buffer.close();

        // Reading in data from output file
        InputBuffer written = new InputBuffer(new RandomAccessFile(
            "testing.bin", "r"));
        assertTrue(written.endOfFile()); // at end if only 1 block written
        written.close();

        // Reading in 2 more blocks into same output file, overwriting contents
        buffer = new OutputBuffer(new RandomAccessFile("testing.bin", "rw"));

        buffer.setData(input.getData());
        input.next(8);
        double key1 = input.nextDouble(8);
        input.nextBlock();
        buffer.flush();

        buffer.setData(input.getData());
        input.next(8);
        double key2 = input.nextDouble(8);
        input.nextBlock();
        buffer.flush();

        buffer.close();

        // Now reading from written file
        input.changeFile(new RandomAccessFile("testing.bin", "r"));

        input.next(8);
        assertEquals(key1, input.nextDouble(8), 0.1);
        input.nextBlock();

        input.next(8);
        assertEquals(key2, input.nextDouble(8), 0.1);
        assertTrue(input.endOfFile());
        input.close();
    }


    /**
     * Tests changeFile()
     * 
     * @throws IOException
     */

    public void testChangeFile() throws IOException {
        // Read only changeFile
        IOException exception = null;

        try {
            buffer.changeFile(new RandomAccessFile("testing.bin", "r"));
            buffer.setData(input.getData());
            buffer.flush();
        }
        catch (IOException e) {
            exception = e;
        }
        assertNotNull(exception);

        // Writing to one file, changing to another
        buffer.changeFile(new RandomAccessFile("testing.bin", "rw"));
        buffer.setData(input.getData());
        double key = buffer.getKey();
        buffer.flush();

        // Changing
        File other = new File("temp.bin");
        other.delete();
        other.createNewFile();
        buffer.changeFile(new RandomAccessFile("temp.bin", "rw"));

        // Putting different data in other file
        input.nextBlock();
        buffer.setData(input.getData());
        double key2 = buffer.getKey();
        buffer.flush();
        buffer.close();

        // Validation
        input.changeFile(new RandomAccessFile("testing.bin", "rw"));
        input.next(8);
        assertEquals(key, input.nextDouble(8), 0.1);
        assertTrue(input.endOfFile());

        input.changeFile(new RandomAccessFile("temp.bin", "rw"));
        input.next(8);
        assertEquals(key2, input.nextDouble(8), 0.1);
        assertTrue(input.endOfFile());

        input.close();
        other.delete();
    }


    /**
     * Tests close()
     */
    public void testClose() {

        IOException exception = null;

        // open file close
        try {
            buffer.close();
        }
        catch (IOException e) {
            exception = e;
        }
        assertNull(exception);

        // closed file close (works since you can close a closed file)
        try {
            buffer.close();
        }
        catch (IOException e) {
            exception = e;
        }
        assertNull(exception);
    }
    
    /**
     * Cleanup
     * @throws IOException 
     */
    @AfterClass
    public static void cleanUp() throws IOException {
        created.delete();
        buffer.close();
        input.close();
    }
}
