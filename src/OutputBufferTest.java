import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
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
    private OutputBuffer buffer;
    private final String TESTING = "testing.bin";
    private final String TESTING2 = "testing2.bin";

    // SetUp ------------------------------------------------------------
    /**
     * Sets up the buffer
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        buffer = new OutputBuffer(new RandomAccessFile(TESTING, "rw"));
    }

    // Tests ------------------------------------------------------------


    /**
     * Tests flush's file component
     * 
     * @throws IOException
     */
    public void testFlush() throws IOException {
        Record record = new Record(this.makeRecord(0, 0));

        // Inserting data into buffer then flushing
        for (int i = 0; i < 512; i++) {
            buffer.insertRecord(new Record(this.makeRecord(i, i + 1)));
        }

        // OutputBuffer should be flushed.
        buffer.close();

        InputBuffer input = new InputBuffer(new RandomAccessFile(TESTING, "r"));

        for (int i = 0; i < 512; i++) {
            assertEquals(i, input.nextLong(8));
            assertEquals(i + 1, input.nextDouble(8), 0.1);
        }

        assertTrue(input.endOfFile());
        input.close();
        this.cleanUp();
    }


    /**
     * Tests changeFile()
     * 
     * @throws IOException
     */

    public void testChangeFile() throws IOException {
        // Read only changeFile
        IOException exception = null;
        Record record = new Record(this.makeRecord(0, 0));
            
        
        try {
            buffer.changeFile(new RandomAccessFile(TESTING, "r"));
            buffer.insertRecord(record);
            buffer.flush();
        }
        catch (IOException e) {
            exception = e;
        }
        assertNotNull(exception);

        
        // Writing to one file, changing to another
        buffer.changeFile(new RandomAccessFile(TESTING, "rw"));
        buffer.insertRecord(record);        
        buffer.flush();

        // Changing
        buffer.changeFile(new RandomAccessFile(TESTING2, "rw"));

        // Putting different data in other file
        record = new Record(this.makeRecord(1, 1));
        buffer.insertRecord(record);
        buffer.flush();
        
        buffer.close();

        // Validation
       RandomAccessFile test1 = new RandomAccessFile(TESTING, "rw");
       RandomAccessFile test2 = new RandomAccessFile(TESTING2, "rw");
       
       

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
     * 
     * @throws IOException
     */
    private void cleanUp() throws IOException {
        File file = new File(TESTING);

        if (file.exists()) {
            file.delete();
        }

        file = new File(TESTING2);

        if (file.exists()) {
            file.delete();
        }

        buffer.close();

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
    private byte[] makeRecord(long id, double key) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(id);
        buffer.putDouble(key);

        return buffer.array();
    }
}
