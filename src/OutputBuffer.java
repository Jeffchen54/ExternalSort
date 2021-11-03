import java.io.IOException;
import java.io.RandomAccessFile;

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
 * Output buffer which flushes to an output file. Is an extension of SubBuffer
 * 
 * @author chenj (chenjeff4840)
 * @version 11.2.2021
 */
public class OutputBuffer extends SubBuffer {
    // Fields ----------------------------------------------------------------
    private RandomAccessFile output;

    // Constructor --------------------------------------------------------

    /**
     * Initializes buffer with no data. Sets the output file
     * 
     * @param output
     *            Output file
     * 
     * @precondition output has write permissions
     */
    public OutputBuffer(RandomAccessFile output) {
        super();
        this.output = output;
    }


    /**
     * Returns shallow copy of data in buffer, writes this data to output file.
     * Afterwards, sets flushed on.
     * 
     * @return shallow copy of data, null if flushed
     * @throws IOException
     *             not needed for this function but needed for child
     *             class.
     */
    @Override
    public byte[] flush() throws IOException {
        byte[] result = super.flush();

        if (result != null) {
            try {
                output.write(result);
            }
            catch (IOException e) {
                System.out.println(
                    "ERROR: Output file is not writable\n\nStack Trace:\n");
                e.printStackTrace();
                throw e;
            }
        }
        return result;
    }


    /**
     * Closes output file. Then sets the output to file.
     * 
     * @param file
     *            File to change to
     * @throws IOException
     */
    public void changeFile(RandomAccessFile file) throws IOException {
        output.close();
        output = file;
    }


    /**
     * Closes the output
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        output.close();
    }

}
