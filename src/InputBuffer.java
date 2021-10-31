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
 * Input buffer of X size, able to read and manage a RandomAccessFile, Byte[],
 * and
 * ByteBuffer.
 * 
 * @author chenj (chenjeff4840)
 * @version 10.30.2021
 */
public class InputBuffer {

    // Fields ------------------------------------------------------------
    private byte[] data;
    private ByteBuffer buffer;
    private RandomAccessFile file;

    // Constructor ------------------------------------------------------------
    /**
     * Initializes fields
     * 
     * @param bytes
     *            Byte size of buffer
     * @param file
     *            file to be managed
     * @throws IOException
     */
    public InputBuffer(int bytes, RandomAccessFile file) throws IOException {
        data = new byte[bytes];
        this.file = file;
        file.read(data);
        buffer = ByteBuffer.wrap(data);
    }

    // Functions ------------------------------------------------------------


    /**
     * Reads the next dest.length bytes in buffer and saves it to dest.
     * Increments
     * buffer forward by dest.length bytes.
     * 
     * @param dest
     *            byte[] to save dest.length bytes to
     */
    public void next(byte[] dest) {
        buffer.get(dest);
    }


    /**
     * Moves the buffer to the beginning of the next block in the file where a
     * block is of size data.length
     * 
     * @throws IOException
     */
    public void nextBlock() throws IOException {
        file.seek(data.length + file.getFilePointer());
    }


    /**
     * Moves buffer to position pos in the file
     * 
     * @param pos
     *            Position to move buffer to in file
     * @throws IOException
     */
    public void seek(long pos) throws IOException {
        file.seek(pos);
        file.read(data);
        buffer.rewind();
    }


    /**
     * Returns entirety of the current block
     * 
     * @return entirety of the current block
     */
    public byte[] getData() {
        return data;
    }


    /**
     * Changes this.file to file
     * 
     * @param file
     *            File to change to
     * @throws IOException
     */
    public void changeFile(RandomAccessFile file) throws IOException {
        this.file = file;
        this.seek(0);
    }


    /**
     * Rewinds the buffer with ByteBuffer.rewind()
     */
    public void rewind() {
        buffer.rewind();
    }


    /**
     * Resets the buffer with ByteBuffer.reset()
     */
    public void reset() {
        buffer.reset();
    }


    /**
     * Marks the buffer with ByteBuffer.mark()
     */
    public void mark() {
        buffer.mark();
    }
}
