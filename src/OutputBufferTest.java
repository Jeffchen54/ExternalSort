import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
 * Tests the OutputBuffer
 * 
 * @author chenj (chenjeff4840)
 * @version 11.2.2021
 */
public class OutputBufferTest extends TestCase {
    // Fields ------------------------------------------------------------
    private OutputBuffer buffer;

    /**
     * Name of a bin file to output to
     */
    private final String testing = "testing.bin";
    /**
     * Name of a bin file to output to
     */
    private final String testing2 = "testing2.bin";

    // SetUp ------------------------------------------------------------
    /**
     * Sets up the buffer
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        this.cleanUp();
        buffer = new OutputBuffer(new RandomAccessFile(testing, "rw"));
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

        // This element is inserted when buffer is full, flushes buffer
        buffer.insertRecord(record);

        // OutputBuffer should be flushed.
        buffer.close();

        InputBuffer input = new InputBuffer(new RandomAccessFile(testing, "r"));

        for (int i = 0; i < 512; i++) {
            assertEquals(i, input.nextLong(8));
            assertEquals(i + 1, input.nextDouble(8), 0.1);
        }

        assertTrue(input.endOfFile());
        input.close();
        this.cleanUp();

        // Inserting 2 blocks of data into buffer.
        buffer = new OutputBuffer(new RandomAccessFile(testing, "rw"));
        for (int i = 0; i < 512; i++) {
            buffer.insertRecord(new Record(this.makeRecord(i, i + 1)));

        }
        for (int i = 0; i < 512; i++) {
            buffer.insertRecord(new Record(this.makeRecord(i, i + 1)));

        }

        // This element is inserted when buffer is full, flushes buffer
        buffer.flush();

        // OutputBuffer should be flushed.
        buffer.close();

        input = new InputBuffer(new RandomAccessFile(testing, "r"));

        for (int i = 0; i < 512; i++) {
            assertEquals(i, input.nextLong(8));
            assertEquals(i + 1, input.nextDouble(8), 0.1);
        }
        input.nextBlock();

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
            buffer.changeFile(new RandomAccessFile(testing, "r"));
            buffer.insertRecord(record);
            buffer.flush();
        }
        catch (IOException e) {
            exception = e;
        }
        assertNotNull(exception);

        // Writing to one file, changing to another
        buffer.changeFile(new RandomAccessFile(testing, "rw"));
        buffer.insertRecord(record);
        buffer.flush();

        // Changing
        buffer.changeFile(new RandomAccessFile(testing2, "rw"));

        // Putting different data in other file
        record = new Record(this.makeRecord(1, 1));
        buffer.insertRecord(record);
        buffer.flush();

        buffer.close();

        // Validation
        RandomAccessFile test1 = new RandomAccessFile(testing, "rw");
        RandomAccessFile test2 = new RandomAccessFile(testing2, "rw");

        byte[] data1 = new byte[8192];
        byte[] data2 = new byte[8192];

        test1.read(data1);
        test2.read(data2);

        ByteBuffer b1 = ByteBuffer.wrap(data1);
        ByteBuffer b2 = ByteBuffer.wrap(data2);

        assertEquals(0, b1.getLong());
        assertEquals(0, b1.getDouble(), 0.1);

        assertEquals(1, b2.getLong());
        assertEquals(1, b2.getDouble(), 0.1);

        assertEquals(8192, test1.length());
        assertEquals(8192, test2.length());

        test1.close();
        test2.close();
        this.cleanUp();

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
        File file = new File(testing);

        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException();
            }
        }

        file = new File(testing2);

        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException();
            }
        }
        if (buffer != null) {
            buffer.close();
        }

    }


    /**
     * Creates a block of size 8192 with first record first id at id and first
     * key
     * at key, additional records increment key and value by 1.
     */
    private byte[] makeRecord(long id, double key) {
        ByteBuffer buff = ByteBuffer.allocate(16);
        buff.putLong(id);
        buff.putDouble(key);

        return buff.array();
    }


    /**
     * Tests getLastRecord()
     * 
     * @throws IOException
     */
    public void testGetLastRecord() throws IOException {
        Record record = null;

        // Inserting data into buffer then flushing
        for (int i = 0; i < 512; i++) {
            record = new Record(this.makeRecord(i, i + 1));
            buffer.insertRecord(record);
            assertEquals(0, buffer.getLastRecord().compareTo(record));

        }

        // This element is inserted when buffer is full, flushes buffer
        buffer.flush();
        assertEquals(0, buffer.getLastRecord().compareTo(record));

        buffer.close();
        this.cleanUp();

    }
}
