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
// letter of this restriction.

/**
 * SubBuffers to be used in the heap.
 * 
 * @author Ben Chen
 * @version 11.20.2021
 */
public class SubBuffer {

    private Record[] heap;
    private ByteBuffer record;
    private int elements;
    private int runNum;

    /**
     * Constructs a SubBuffer loaded with src
     * 
     * @param src
     *            Data to load into SubBuffer
     * @precondition src is a byte[] of size 8192 bytes
     */
    public SubBuffer(byte[] src) {
        record = ByteBuffer.wrap(src);
        heap = new Record[512];
        for (int i = 0; i < 512; i++) {
            byte[] b = new byte[16];
            record.get(b);
            heap[i] = new Record(b);
        }
        elements = 512;
        buildheap();
        runNum = -1;
    }


    /**
     * this function insert a whole block into
     * the subbuffer.
     * If it is not empty, it return false
     * if it is empty, take in the byte[] and
     * breakup to 512 records make the heap
     * 
     * @param src
     *            Block to be inserted into the buffer
     * @return True if inserted, false if not which occurs if buffer is not
     *         empty
     * @precondition src is of size 8192 bytes
     */
    public boolean insertBlock(byte[] src) {
        if (elements != 0) {
            return false;
        }
        record = ByteBuffer.wrap(src);
        for (int i = 0; i < 512; i++) {
            byte[] b = new byte[16];
            record.get(b);
            heap[i] = new Record(b);
        }
        elements = 512;
        buildheap();
        this.runNum = -1;
        return true;
    }


    /**
     * return the smallest key not remove it
     * 
     * @return the smallest record in the buffer
     */
    public Record getRt() {
        if (elements == 0) {
            return null;
        }
        return heap[0];
    }


    /**
     * return the smallest value of the rt
     * remove the rt to the last position
     * and decrement the heap
     * 
     * @return the record that has been removed
     */
    public Record removeRt() {
        if (elements == 0) {
            return null;
        }
        Record rec = heap[0];
        swap(0, --elements);
        siftdown(0);

        return rec;
    }


    /**
     * return the activated elements
     * 
     * @return an integer
     */
    public int getActiveElements() {
        return elements;
    }


    /**
     * compare the smallest key in this subBuffer with
     * the smallest key in another subBuffer
     * 
     * @param obj
     *            SubBuffer to compare to
     * @return 0 if equal, -1 if less than, 1 if greater than
     */
    public int compareTo(SubBuffer obj) {
        if (obj == null) {
            return -1;
        }
        if (obj == this) {
            return 0;
        }
        if (getRt().compareTo(obj.getRt()) == 0) {
            return 0;
        }
        if (getRt().compareTo(obj.getRt()) < 0) {
            return -1;
        }
        return 1;
    }


    /**
     * Returns the runNum
     * 
     * @return run number, -1 if not set
     */
    public int getRunNum() {
        return runNum;
    }


    /**
     * Sets the runNum
     * 
     * @param newRunNum
     *            runNum to set
     */
    public void setRunNum(int newRunNum) {
        this.runNum = newRunNum;
    }


    /**
     * Builds heap from rt to the # of elements in the heap.
     */
    private void buildheap() {
        for (int i = elements / 2 - 1; i >= 0; i--) {
            siftdown(i);
        }
    }


    /**
     * Puts element at pos in heap into correct minheap position
     * 
     * @param pos
     *            Position in heap to put into correct minheap position
     */
    private void siftdown(int pos) {
        if ((pos < 0) || (pos >= elements)) {
            return;
        } // Illegal position
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            if ((j < (elements - 1)) && (heap[j].compareTo(heap[j + 1]) >= 0)) {
                j++; // j is now index of child with greater value
            }
            if (heap[pos].compareTo(heap[j]) < 0) {
                return;
            }
            swap(pos, j);
            pos = j; // Move down
        }
    }

    // Helpers ------------------------------------------------------------


    /**
     * Swaps heap elements
     * 
     * @param p1
     *            heap element position to swap with p2
     * @param p2
     *            heap element position to swap with p1
     * 
     */
    private void swap(int p1, int p2) {
        Record temp = heap[p1];
        heap[p1] = heap[p2];
        heap[p2] = temp;
    }


    /**
     * Checks if pos position in heap is a leaf
     * 
     * @param pos
     *            Position in heap
     * @return true if pos is leaf
     */
    private boolean isLeaf(int pos) {
        return (pos >= elements / 2) && (pos < elements);
    }


    /**
     * Gets parent's leftchild position
     * 
     * @param pos
     *            Position in heap
     * @return true if pos is left child
     */
    private int leftchild(int pos) {
        if (pos >= elements / 2) {
            return -1;
        }
        return 2 * pos + 1;
    }


    /**
     * Returns toString of the root record
     * 
     * @return toString of the root record, aka smallest key record
     */
    public String toString() {
        return this.getRt().toString();
    }
}
