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
 * Sub Buffer to be used as buffer for main buffer, stores a copy of a given
 * block, can compare the key section of that block with other sub buffers,
 * return, flush, and bring in new data.
 * 
 * @author chenj (chenjeff4840)
 * @version 11.1.2021
 */
public class SubBuffer implements Comparable<byte[]> {

    // Fields -----------------------------------------------------------
    private byte[] data;
    private ByteBuffer buffer;
    private boolean flushed;

    // Constructor -------------------------------------------------------

    /**
     * Initializes fields. Copies the first 8192 array blocks from src. If
     * the src is not a proper block, it will not be copied and flushed
     * will be false. Flush will be true otherwise.
     * 
     * @param src
     *            Array to be copied
     */
    public SubBuffer(byte[] src) {

    }

    // Functions -----------------------------------------------------------


    /**
     * Returns the block's key, returns -1 if flushed
     * 
     * @return key, -1 if flushed
     */
    public double getKey() {
        return 0;
    }


    /**
     * Returns shallow copy of data in buffer. Afterwards, sets flushed on.
     * 
     * @return shallow copy of data, null if flushed
     */
    public byte[] flush() {
        return null;
    }


    /**
     * Copies src to buffer. Turns flushed off. If src is not a valid block,
     * do nothing.
     * 
     * @param src
     *            data to copy to buffer
     */
    public void setData(byte[] src) {

    }


    /**
     * Returns shallow copy of buffer's data
     * 
     * @return shallow copy of buffer's data, null if flushed
     */
    public byte[] getData() {
        return null;
    }


    /**
     * Checks if buffer has been flushed
     * 
     * @return true if flushed, false otherwised
     */
    public boolean isFlushed() {
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(byte[] o) {
        // TODO Auto-generated method stub
        return 0;
    }

    // Helpers -----------------------------------------------------------


    /**
     * Checks if the block is a block of 8192 byte size.
     */
    private boolean isBlock(byte[] block) {
        return false;
    }

}
