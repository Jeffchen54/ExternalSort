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
 * Holds a single record
 * 
 * @author CS Staff
 * @version 2020-10-15
 */
public class Record implements Comparable<Record> {

    private byte[] completeRecord;

    /**
     * The constructor for the Record class
     * 
     * @param record
     *            The byte for this object
     */
    public Record(byte[] record) {
        completeRecord = record;
    }


    /**
     * returns the complete record
     * 
     * @return complete record
     */
    public byte[] getCompleteRecord() {
        return completeRecord;
    }


    /**
     * Returns the object's key
     * 
     * @return the key
     */
    public double getKey() {
        ByteBuffer buff = ByteBuffer.wrap(completeRecord);
        return buff.getDouble(8);
    }


    /**
     * Compare Two Records based on their keys
     * 
     * @param toBeCompared
     *            - The Record to be compared.
     * @return A negative integer, zero, or a positive integer as this employee
     *         is less than, equal to, or greater than the supplied record
     *         object.
     */
    @Override
    public int compareTo(Record toBeCompared) {
        return Double.compare(this.getKey(), toBeCompared.getKey());
    }


    /**
     * Outputs the record as a String
     * 
     * @return a string of what the record contains
     */
    public String toString() {
        return "" + this.getKey();
    }

}
