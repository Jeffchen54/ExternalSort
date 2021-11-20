import java.io.File;
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
 * Tests the Validation class
 * 
 * @author Ben Chen
 * @author Jeff Chen (chenjeff4840)
 * @version 11.20.2021
 */
public class ValidationTest extends student.TestCase {

    /**
     * Name to be used in generated .bin files for testing. .bin is added
     * later.
     */
    private final String genfile = "Temp";

    /**
     * Tests validation and constructor.
     */
    public void testValidation() throws IOException {
        Genfile.main(new String[] { genfile, Integer.toString(11) });
        Validation k = new Validation(genfile + ".bin", genfile + "sorted.bin");
        assertFalse(k.compare());
        Validation t = new Validation(genfile + "sorted.bin", genfile
            + "sorted.bin");
        assertTrue(t.compare());
        Validation s = new Validation(genfile + "reverseSorted.bin", genfile
            + "reverseSorted.bin");
        assertTrue(s.compare());

        this.deleteTemp();
    }


    /**
     * Deletes all generated files used for testing
     * 
     * @throws IOException
     */
    private void deleteTemp() throws IOException {
        File file = new File(genfile + ".bin");
        this.deleteFile(file);
        file = new File(genfile + "reversesorted.bin");
        this.deleteFile(file);
        file = new File(genfile + "sorted.bin");
        this.deleteFile(file);
        file = new File(genfile + "copysorted.bin");
        this.deleteFile(file);
    }


    /**
     * Attempts to delete a file, IOException if file has not been deleted
     * if it exists.
     * 
     * @param file
     *            File to delete
     * @throws IOException
     */
    private void deleteFile(File file) throws IOException {
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException();
            }
        }
    }
}
