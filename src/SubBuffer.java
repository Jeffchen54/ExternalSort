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
public class SubBuffer implements Comparable<SubBuffer> {

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
     * @precondition src != null
     */
    public SubBuffer(byte[] src) {
        data = new byte[8192];

        if (this.isBlock(src)) {
            flushed = false;
        }
        else {
            flushed = true;
        }

        this.setData(src);
        buffer = ByteBuffer.wrap(data);
    }


    /**
     * Initializes fields with no data, flushed set to true
     * 
     */
    public SubBuffer() {
        data = new byte[8192];
        flushed = true;
        buffer = ByteBuffer.wrap(data);
    }

    // Functions -----------------------------------------------------------


    /**
     * Returns the block's key, returns -1 if flushed
     * 
     * @return key, -1 if flushed
     */
    public double getKey() {
        // Conditional
        if (isFlushed()) {
            return -1;
        }

        // Creating temp key buffer and incrementing to key pos in buffer
        byte[] temp = new byte[8];
        buffer.position(8);
        buffer.get(temp);

        // Grabbing key as double and reset to beginning of buffer
        double toReturn = this.convertToDouble(temp);
        buffer.rewind();
        return toReturn;
    }


    /**
     * Returns shallow copy of data in buffer. Afterwards, sets flushed on.
     * 
     * @return shallow copy of data, null if flushed
     */
    public byte[] flush() {
        if (this.isFlushed()) {
            return null;
        }

        flushed = true;
        return data;
    }


    /**
     * Copies src to buffer. Turns flushed off. If src is not a valid block,
     * do nothing.
     * 
     * @param src
     *            data to copy to buffer
     * @precondition src != null
     */
    public void setData(byte[] src) {
        if (this.isBlock(src)) {
            System.arraycopy(src, 0, data, 0, 8192);
            flushed = false;
        }
    }


    /**
     * Returns shallow copy of buffer's data
     * 
     * @return shallow copy of buffer's data, null if flushed
     */
    public byte[] getData() {
        if (this.isFlushed()) {
            return null;
        }
        return data;
    }


    /**
     * Checks if buffer has been flushed
     * 
     * @return true if flushed, false otherwised
     */
    public boolean isFlushed() {
        return flushed == true;
    }


    /**
     * Compares this and obj's key. If:
     * 
     * this.key == obj.key -> returns 0
     * this.key < obj.key || obj == null -> returns -1
     * this.key > obj.key -> return 1
     * 
     * 2 Flushed buffers will return 0 since default key value is -1
     * 
     * @param obj
     *            SubBuffer to compare to this
     * @return 1 if equal, -1 if less, 1 if greater.
     */
    @Override
    public int compareTo(SubBuffer obj) {
        if (obj == null) {
            return -1;
        }

        if (obj == this) {
            return 0;
        }

        if (this.getKey() == obj.getKey()) {
            return 0;
        }

        if (this.getKey() < obj.getKey()) {
            return -1;
        }

        return 1;

    }

    // Helpers -----------------------------------------------------------


    /**
     * Checks if the block is a valid block.
     * 
     * @param block
     *            Block to check for validity.
     * @return true if block is a valid block, false otherwise
     */
    private boolean isBlock(byte[] block) {
        return block.length == 8192;
    }


    /**
     * Converts array to double
     * 
     * @param array
     *            Array to convert to double
     * @return array as double
     */
    private double convertToDouble(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.getDouble();
    }

}
