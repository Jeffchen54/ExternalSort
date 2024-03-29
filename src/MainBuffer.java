import java.io.IOException;

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
     * min heap.
     * 
     * @param block
     *            Block to be inserted into buffer
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
     * min heap. runNum assigned to the inserted block
     * 
     * @param block
     *            Value into insert into heap
     * @param runNum
     *            Value to be set to the block inserted into the heap
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
     * @return runNum of min key, -1 if fails
     */
    public int removeMin(OutputBuffer dest) throws IOException {
        if (elements == 0) {
            return -1;
        } // Removing from empty heap

        int runNum = heap[0].getRunNum();

        Record removedMin = this.removeMin();

        if (dest != null) {
            dest.insertRecord(removedMin);
        }
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
     * who have 0 elements or are null.
     */
    public void reactivateHeap() {
        int livePosition = 0;
        for (int i = 0; i < size; i++) {
            if (heap[i] != null && heap[i].getActiveElements() != 0) {
                this.swap(i, livePosition);
                livePosition++;
            }
        }
        elements = livePosition;
        buildheap();
    }


    /**
     * Initiates 1 cycle of ReplacementSelection.
     * 
     * Will not do anything if heapSize() is 0. Removes smallest value,
     * sets compare's data to it. Afterwards, compares the removed smallest
     * value to key.
     * 
     * If block >= recent removed record, replace empty spot in
     * heap with block. Shift down to make minheap.
     * 
     * If key < recent removed record, replace empty spot in heap with
     * block (At end of heap). Swap block with current largest value in heap.
     * Heap size decremented. Shift down largest block to make minheap.
     * 
     * Notes:
     * - Call insert(byte[]) to insert initial RS elements
     * - Call heapSize() to get current heap size
     * - Call reactivateHeap() to reactivate all keys matching a criteria
     * - Call replacementSelection(null, OutputBuffer) to remove smallest
     * value without adding new values.
     * 
     * @param block
     *            Block to insert into heap. Can be null.
     * @param compare
     *            Output buffer storing value that was replaced.
     * @throws IOException
     * @return true if block was inserted, false if not. Block inserted if
     *         a block in the heap is empty
     */
    public boolean replacementSelection(byte[] block, OutputBuffer compare)
        throws IOException {
        if (elements == 0) {
            return false;
        }
        boolean inserted = false;

        // No more input values left
        if (block == null) {
            compare.insertRecord(this.removeMin());
        }
        // More input values
        else {
            // Removing lowest value, setting rt to key
            int before = elements;
            compare.insertRecord(this.removeMin());

            // Checking if rt is now empty
            if (before != elements) {
                // If rt is empty, we insert in another block
                this.insert(block);

                // We don't know where the block is after shift; however,
                // we know if the rt is < than last removed record, we
                // disconnect that block
                if (heap[0].getRt().compareTo(compare.getLastRecord()) < 0) {
                    elements--;
                    this.swap(0, elements);
                }
                else {
                    this.buildheap();
                }
                // Else, we siftdown which we also do if there is no new
                // block inserted
                inserted = true;
            }
        }

        // We siftdown rt as a new record is at the front of the block
        // even when a new block is inserted
        this.siftdown(0);
        return inserted;
    }


    /**
     * Continually performs replacement selection until block is inserted
     * into the heap
     * 
     * @param block
     *            Block to insert into heap
     * @param compare
     *            OutputBuffer loaded with removed records
     * @throws IOException
     */
    public void replacementSelectionCycle(byte[] block, OutputBuffer compare)
        throws IOException {
        if (elements == 0) {
            return;
        }
        else {
            int counter = 0;
            boolean inserted = false;
            while (!inserted) {
                // Continually removes from replacement selection
                // until block is finally inserted into the heap.
                inserted = replacementSelection(block, compare);
            }
        }
    }


    /**
     * this merge function merge one block
     * meaning it will stop after one block is empty
     * 
     * @throws IOException
     * @param output
     *            OutputBuffer to output records into
     * @return the run number of the run that is empty
     */
    public int mergeOnce(OutputBuffer output) throws IOException {
        int originSize = elements;
        int runNum = -1;
        while (elements == originSize) {
            runNum = this.removeMin(output);
            siftdown(0);
        }
        return runNum;
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
