import java.io.IOException;
import java.io.RandomAccessFile;
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
 * Tests the InputBuffer
 * 
 * @author chenj (chenjeff4840)
 * @version 10.30.2021
 */
public class InputBufferTest  extends TestCase{
    
    // Fields ---------------------------------------------------------------
    private InputBuffer buffer;
    
    @Test
    public void setUp() throws IOException {
        RandomAccessFile file = new RandomAccessFile("sampleInput16.bin", "r");
        buffer = new InputBuffer(8192, file);
        System.out.println("Running???");

    }
    
    /**
     * Reads the next dest.length bytes in buffer and saves it to dest.
     * Increments
     * buffer forward by dest.length bytes.
     * 
     * @param dest
     *            byte[] to save dest.length bytes to
     */
    public void testNext(byte[] dest) {
        System.out.println("Running???");
    }


    /**
     * Moves the buffer to the beginning of the next block in the file where a
     * block is of size data.length
     * 
     * @throws IOException
     */
    public void nextBlock() throws IOException {
    }


    /**
     * Moves buffer to position pos in the file
     * 
     * @param pos
     *            Position to move buffer to in file
     * @throws IOException
     */
    public void seek(long pos) throws IOException {
    }


    /**
     * Returns entirety of the current block
     * 
     * @return entirety of the current block
     */
    public void getData() {
    }


    /**
     * Changes this.file to file
     * 
     * @param file
     *            File to change to
     * @throws IOException
     */
    public void changeFile(RandomAccessFile file) throws IOException {
    }


    /**
     * Rewinds the buffer with ByteBuffer.rewind()
     */
    public void rewind() {
    }


    /**
     * Resets the buffer with ByteBuffer.reset()
     */
    public void reset() {
    }


    /**
     * Marks the buffer with ByteBuffer.mark()
     */
    public void mark() {
    }

}
