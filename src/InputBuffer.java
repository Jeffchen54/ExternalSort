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
 * and ByteBuffer. Buffer block size fixed to 8192 bytes, cannot be modified
 * due to specialized and fast block calculation algorithm.
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
     * @param file
     *            file to be managed
     * @throws IOException
     */
    public InputBuffer(RandomAccessFile file) throws IOException {
        data = new byte[8192];
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
     * @apiNote parent
     */
    public void next(byte[] dest) {
        buffer.get(dest);
    }


    /**
     * Increments buffer by increment bytes
     * 
     * @param increment
     *            bytes to increment buffer
     * @apiNote parent
     */
    public boolean next(int increment) {
        try {
            buffer.position(buffer.position() + increment);
        }
        catch (IllegalArgumentException E) {
            return false;
        }
        return true;
    }


    /**
     * Reads next length bytes in buffer, returns as a double. Increment buffer
     * by length bytes.
     * 
     * @param length
     *            # of bytes to be read buffer
     * @return read bytes as double
     * @apiNote parent
     */
    public double nextDouble(int length) {
        byte[] temp = new byte[length];
        this.next(temp);
        return this.convertToDouble(temp);
    }


    /**
     * Reads next length bytes in buffer, returns as a long. Increments buffer
     * by
     * length bytes.
     * 
     * @param length
     *            # of bytes to be read buffer
     * @return read bytes as long
     * @apiNote parent
     */
    public long nextLong(int length) {
        byte[] temp = new byte[length];
        this.next(temp);
        return this.convertToLong(temp);
    }


    /**
     * Moves the buffer to the beginning of the next block in the file where a
     * block is of size data.length. If the buffer is currently inside 2 blocks,
     * the buffer will move to the next adjacent block.
     * 
     * File position will be unchanged if filePointer is at or past EOF.
     * 
     * @throws IOException
     */
    public void nextBlock() throws IOException {
        // Right shift == FilePointer // 8192
        // -1 is to push the filePointer back into the current block
        long curr = (file.getFilePointer() - 1) >> 13;

        // Left shift == quotient * 8192
        curr = curr << 13;

        //
        this.seek(curr + data.length);
    }


    /**
     * Moves buffer to position pos in the file with RandomAccessFile.seek().
     * Does nothing if seek is < 0. Seek can be set beyond the end of the file;
     * however, file position will remain unchanged.
     * 
     * If there is not enough data left in the file to read into the array, old
     * data may still persist:
     * 
     * Example:
     * File bytes left after seek = 300
     * buffer position 0->299 new data
     * buffer position 300-8191 old data
     * 
     * Although seek cannot allow parameters greater than file length, the
     * file pointer may still skip past the end of the file if parameter
     * given is such that there are less than 8192 bytes left past the
     * seek position, should not affect program as counted as EOF.
     * 
     * @param pos
     *            Position to move buffer to in file, will not affect file
     *            pointer
     *            is < 0 || >= file.length
     * @throws IOException
     */
    public void seek(long pos) throws IOException {
        if (pos >= 0 && pos < file.length()) {
            file.seek(pos);
            file.read(data);
            buffer.rewind();
        }
    }


    /**
     * Returns shallow copy of data from the current buffer
     * 
     * @return shallow copy of data from current buffer
     * @apiNote parent
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
     * 
     * @apiNote parent
     */
    public void rewind() {
        buffer.rewind();
    }


    /**
     * Resets the buffer with ByteBuffer.reset()
     * 
     * @precondition mark exists
     * @apiNote parent
     */
    public void reset() {
        buffer.reset();
    }


    /**
     * Marks the buffer with ByteBuffer.mark()
     * 
     * @apiNote parent
     */
    public void mark() {
        buffer.mark();
    }


    /**
     * Checks if EOF
     * 
     * @return true if file is at EOF or past, false otherwise
     * @throws IOException
     */
    public boolean endOfFile() throws IOException {
        return file.getFilePointer() >= file.length();
    }


    // Helpers --------------------------------------------------------------
    /**
     * Converts array to long
     * 
     * @param array
     *            Array to convert to long
     * @return array as long
     * @apiNote parent
     */
    private long convertToLong(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.getLong();
    }


    /**
     * Converts array to double
     * 
     * @param array
     *            Array to convert to double
     * @return array as double
     * @apiNote parent
     */
    private double convertToDouble(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.getDouble();
    }
}
