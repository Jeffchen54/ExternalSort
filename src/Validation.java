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
 * Validates if 2 bin files are exactly the same
 * 
 * @author Ben Chen
 * @version 11.20.2021
 */
public class Validation {

    private double key; // the key of the error block
    private int num; // trace number of block that got error
    private String ourFile;
    private String answerFile;

    /**
     * Constructs a validator with 2 files
     * 
     * @param ourFile
     *            File to be compared
     * @param answerFile
     *            File to be compared
     */
    public Validation(String ourFile, String answerFile) {
        num = 1;
        this.ourFile = ourFile;
        this.answerFile = answerFile;
    }


    /**
     * Compares the 2 files within the validation object. Reports findings
     * to standard output and returns the result.
     * 
     * @return true if they are the same, false otherwise.
     */
    public boolean compare() throws IOException {
        RandomAccessFile result = new RandomAccessFile(ourFile, "r");
        InputBuffer rBuffer = new InputBuffer(result);

        RandomAccessFile validation = new RandomAccessFile(answerFile, "r");
        InputBuffer vBuffer = new InputBuffer(validation);

        boolean same = false;

        if (equal(rBuffer, vBuffer)) {
            System.out.println("the output of your external sort is correct");
            same = true;
        }
        else {
            System.out.println("you have error at the " + num
                + "th block, the key is " + key);
        }

        rBuffer.close();
        vBuffer.close();
        return same;
    }


    /**
     * Checks if the files within the InputBuffers in the parameter contain
     * the same data
     * 
     * @return true if files are equal, false otherwise.
     */
    public boolean equal(InputBuffer rBuffer, InputBuffer vBuffer)
        throws IOException {
        while (!rBuffer.endOfFile() && !vBuffer.endOfFile()) {
            ByteBuffer myRecord = ByteBuffer.wrap(rBuffer.getData());
            ByteBuffer valRecord = ByteBuffer.wrap(vBuffer.getData());
            for (int j = 0; j < 512; j++) {
                myRecord.getLong();
                double myDouble = myRecord.getDouble();
                valRecord.getLong();
                double valDouble = valRecord.getDouble();
                if (myDouble != valDouble) {
                    return false;
                }
            }
            rBuffer.nextBlock();
            vBuffer.nextBlock();
        }
        return true;
    }
}
