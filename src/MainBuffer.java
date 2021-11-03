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
        heap = new SubBuffer[8];
        elements = 0;
        size = 8;

        for (int i = 0; i < heap.length; i++) {
            heap[i] = new SubBuffer();
        }
    }

    // Functions ------------------------------------------------------------


    /**
     * Inserts key into heap. Will fail if heap is full. Automatically builds
     * min heap.
     * 
     * @param key
     *            Value into insert into heap
     */
    public void insert(byte[] key) {
        if (elements >= size) {
            System.out.println("Heap is full");
            return;
        }
        int curr = elements++;
        heap[curr].setData(key);
        ; // Start at end of heap
          // Now sift up until curr's parent's key <= curr's key
        while ((curr != 0) && (heap[curr].compareTo(heap[parent(curr)]) <= 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }


    /**
     * Flushes min value. Fails if heap is empty. Automatically builds min heap.
     * 
     * @return min value data, null if fails.
     */
    public byte[] removeMin() throws IOException {
        if (elements == 0) {
            return null;
        } // Removing from empty heap
        swap(0, --elements); // Swap min with last value
        siftdown(0); // Put new heap root val in correct place
        return heap[elements].getData();
    }


    /**
     * Initiates 1 cycle of ReplacementSelection
     * 
     * @param key
     *            Block to insert into heap. Can be null.
     * @param compare
     *            Output buffer storing value that was replaced.
     * @throws IOException
     */
    public void replacementSelection(byte[] key, OutputBuffer compare)
        throws IOException {
        if (elements == 0) {
            return;
        }

        // No more input values left
        if (key == null) {
            compare.setData(removeMin());
        }
        // More input values
        else {
            // Removing lowest value, setting rt to key
            compare.setData(heap[0].getData());
            heap[0].setData(key);

            // RS checks on key and rt
            // If key is greater or equal, just insert to rt and shift
            if (heap[0].compareTo(compare) >= 0) {
                siftdown(0); // Put new heap root val in correct place
            }
            // Ifkey is less, swap with last element, decrement, and shift
            else {
                swap(0, --elements); // Swap maximum with last value
                siftdown(0); // Put new heap root val in correct place
            }
        }
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
     * Reactivates all inactive heap elements
     */
    public void reactivateHeap() {
        elements = 8;
        buildheap();
    }


    // Heapify contents of Heap
    void buildheap() {
        for (int i = elements / 2 - 1; i >= 0; i--) {
            siftdown(i);
        }
    }


    // Put element in its correct place
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
     * Checks if pos position in heap is a left child
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
