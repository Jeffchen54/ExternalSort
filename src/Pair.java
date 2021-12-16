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

// Java Doc ------------------------------------------------------------------
/**
 * Object representing a record with a key and id
 * 
 * @author Ben Chen
 * @version 11.20.2021
 */
public class Pair {
    private double key;
    private long id;

    /**
     * Constructs a pair according to parameters
     * 
     * @param newId
     *            id of pair
     * @param newKey
     *            key of pair
     */
    public Pair(long newId, double newKey) {
        this.id = newId;
        this.key = newKey;
    }


    /**
     * Returns the id
     * 
     * @return id
     */
    public long getId() {
        return id;
    }


    /**
     * Returns the key
     * 
     * @return key
     */
    public double getKey() {
        return key;
    }
}
