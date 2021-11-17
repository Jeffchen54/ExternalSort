import java.io.IOException;
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
 * MainBuffer organized as a min heap with 8 SubBuffers
 * 
 * @author chenj (chenjeff4840)
 * @version 11.3.2021
 */
public class MainBuffer {

    // Fields ---------------------------------------------------------------
    private SubBuffer[] heap;
    private int size;
    private int elements;

    // Constructor ------------------------------------------------------------
    /**
     * Constructs empty heap, initializes fields.
     */
    public MainBuffer() {
        elements = 0;
        size = 8;
        heap = new SubBuffer[size];

    }

    // Functions ------------------------------------------------------------


    /**
     * Inserts key into heap. Will fail if heap is full. Automatically builds
     * min heap. key ID defaults to SubBuffer flush value.
     * 
     * @param key
     *            Value into insert into heap
     */
    public void insert(byte[] block) {
        if (elements >= size) {
            System.out.println("Heap is full");
            return;
        }

        int curr = elements++;

        if (heap[curr] == null) {
            heap[curr] = new SubBuffer(block);
        }
        else {
            heap[curr].insertBlock(block);
        }
        // Start at end of heap
        // Now sift up until curr's parent's key <= curr's key
        while ((curr != 0) && (heap[curr].compareTo(heap[parent(curr)]) <= 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }


    /**
     * Inserts key into heap. Will fail if heap is full. Automatically builds
     * min heap. Assigns an ID to this key.
     * 
     * @param key
     *            Value into insert into heap
     */
    public void insert(byte[] block, int runNum) {
        if (elements >= size) {
            System.out.println("Heap is full");
            return;
        }
        int curr = elements++;

        if (heap[curr] == null) {
            heap[curr] = new SubBuffer(block);
        }
        else {
            heap[curr].insertBlock(block);
        }

        heap[curr].setRunNum(runNum);

        // Start at end of heap
        // Now sift up until curr's parent's key <= curr's key
        while ((curr != 0) && (heap[curr].compareTo(heap[parent(curr)]) <= 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }


    /**
     * Returns min record and heapifies the heap if needed. If heap[0] is
     * empty after being removed from, it is shifted to the end of the
     * heap and disconnected.
     * 
     * @return min value data, null if fails.
     */
    public Record removeMin() throws IOException {
        if (elements == 0) {
            return null;
        } // Removing from empty heap

        Record toReturn = heap[0].removeRt();

        if (heap[0].getActiveElements() == 0) {
            swap(0, --elements); // Swap min with last value
            siftdown(0); // Put new heap root val in correct place
        }

        this.siftdown(0);
        return toReturn;
    }


    /**
     * Saves shallow copy of min value to dest. Returns RunNum of the
     * min value. Fails if heap is empty. Automatically builds min heap.
     * 
     * @param dest
     *            Destination to store min value. Does not store a new
     *            value if this function fails.
     * @return ID of min key, -1 if fails
     */
    public int removeMin(OutputBuffer dest) throws IOException {
        int runNum = heap[0].getRunNum();

        if (elements == 0) {
            return -1;
        } // Removing from empty heap
        Record removedMin = this.removeMin();
        dest.insertRecord(removedMin);
        return runNum;
    }


    /**
     * Returns heap size
     * 
     * @return # of elements in heap
     */
    public int heapSize() {
        return elements;
    }


    /**
     * Reactivates all inactive heap elements. Does not activate heap elements
     * that do not have any data.
     */
    public void reactivateHeap() {
        int livePosition = 0;
        for (int i = 0; i < size; i++) {
            if (heap[i] != null && heap[0].getActiveElements() != 0) {
                this.swap(i, livePosition);
                livePosition++;
            }
        }
        elements = livePosition;
        buildheap();
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
        SubBuffer temp = heap[p1];
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
     * Checks if pos position in heap is a right child
     * 
     * @param pos
     *            Position in heap
     * @return true if pos is right child
     */
    private int rightchild(int pos) {
        if (pos >= (pos - 1) / 2) {
            return -1;
        }
        return 2 * pos + 2;
    }


    /**
     * Returns position for parent
     * 
     * @param pos
     *            Position in heap
     * @return parent position of pos
     */
    private int parent(int pos) {
        if (pos <= 0) {
            return -1;
        }
        return (pos - 1) / 2;
    }

}
