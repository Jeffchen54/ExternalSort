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
 * Object wrapping array. Used to bypass pass by value in order to modify array
 * by pass by pointer.
 * 
 * @param <T>
 *            Type of array
 * @author chenj (chenjeff4840)
 * @version 10.30.2021
 */

public class ArrayWrapper<T> {

    // Fields --------------------------------------------------------------
    private T[] arr;

    // Constructor ---------------------------------------------------------
    /**
     * Initializes wrapper with no data
     */
    public ArrayWrapper() {
        arr = null;
    }


    // Functions --------------------------------------------------------------
    /**
     * Wraps an array
     * 
     * @param src
     *            Array to wrap
     */
    public void wrap(T[] src) {
        arr = src;
    }


    /**
     * Returns array
     * 
     * @return array currently being wrapped, null if none exists.
     */
    public T[] get() {
        return arr;
    }


    /**
     * Sets position in array to a value
     * 
     * @param pos
     *            position of array to modified
     * @param data
     *            data to insert into value
     */
    public void setValue(int pos, T data) {
        if (arr == null || arr.length >= pos) {
            return;
        }

        arr[pos] = data;
    }


    /**
     * Sets array to null, clearing it
     */
    public void clear() {
        arr = null;
    }

}
