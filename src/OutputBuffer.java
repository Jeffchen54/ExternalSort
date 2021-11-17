import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

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
 * Output buffer which flushes to an output file. Is an extension of SubBuffer
 * 
 * @author chenj (chenjeff4840)
 * @version 11.2.2021
 */
public class OutputBuffer {
    // Fields ----------------------------------------------------------------
    private RandomAccessFile output;
    private byte[] data;
    private ByteBuffer buffer;
    private Record cache;

    // Constructor --------------------------------------------------------

    /**
     * Initializes buffer with buffer size 8192 bytes.
     * 
     * @param output
     *            Output file
     * 
     * @precondition output has write permissions
     */
    public OutputBuffer(RandomAccessFile output) {
        this.output = output;
        data = new byte[8192];
        buffer = ByteBuffer.wrap(data);
        cache = null;
    }


    /**
     * Returns shallow copy of data in buffer, writes this data to output file.
     * Resets buffer position to 0 which can be written to with new data.
     * 
     * Will always flush 8192 bytes of data to the output file
     * 
     * @return shallow copy of data
     */
    public byte[] flush() throws IOException {
        output.write(data);
        buffer.rewind(); // This "clears" the array, favorable instead of
                         // Setting arr contents to 0.

        return data;
    }


    /**
     * Closes output file. Then sets the output to file.
     * 
     * @param file
     *            File to change to
     * @throws IOException
     */
    public void changeFile(RandomAccessFile file) throws IOException {
        this.close();
        output = file;
    }


    /**
     * Inserts a record into the OutputBuffer, automatically flushed when the
     * Buffer is full
     * 
     * @throws IOException
     */
    public void insertRecord(Record record) throws IOException {

        if (!buffer.hasRemaining()) {
            this.flush();
        }

        cache = record;
        buffer.put(record.getCompleteRecord());
    }


    /**
     * Returns the last record inserted into the buffer
     * 
     * @return last record inserted into buffer, null if output file
     *         is closed.
     */
    public Record getLastRecord() {
        return cache;
    }


    /**
     * Closes the output, cache is reset.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        output.close();
        cache = null;
    }

}
