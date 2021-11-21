import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;

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
 * Test MainBuffer. Will also tests if Replacement selection and merge
 * are possible with the MainBuffer.
 * 
 * @author chenj (chenjeff4840)
 * @version 11.18.2021
 */

public class BufferControllerTest extends student.TestCase {

    // Fields --------------------------------------------------------
    private BufferController bc;
    /**
     * Default bin file to be sorted
     */
    private final String output = "sampleInput16.bin";

    /**
     * Name of generated file, .bin is added on during generation
     */
    private final String genfile = "Temp";

    /**
     * Name of run file generated during replacmeent selection
     */
    private final String run1 = "JeffChenRunUno.bin";
    private Validation valid;

    // Set Up --------------------------------------------------------
    /**
     * Sets up the buffer controller with a 16 block file
     */
    public void setUp() throws IOException {
        bc = new BufferController(output);
    }


    // Tests --------------------------------------------------------
    /**
     * Tests replacement selection
     */
    public void testReplacementSelection()
        throws FileNotFoundException,
        IOException {
        ////////////////////////////////////////////////////////////////
        // Testing a single run file of 16 blocks
        bc.replacementSelection();

        InputBuffer input = new InputBuffer(new RandomAccessFile(run1, "r"));
        input.next(8);

        Double prev = input.nextDouble(8);
        input.rewind();

        while (!input.endOfFile()) {
            for (int i = 0; i < 206; i++) {
                assertTrue(prev.compareTo(this.getKey(input)) <= 0);
                input.next(16);

            }
            input.nextBlock();
        }

        assertEquals(0, bc.getRunBegin()[0], 0.1);
        assertEquals(2, bc.getRunBegin().length);
        assertEquals(131072, bc.getRunEnd()[0], 0.1);
        assertNull(bc.getRunBegin()[1]);
        assertNull(bc.getRunEnd()[1]);

        this.closeForRS(input);

        ////////////////////////////////////////////////////////////////
        // Testing a double run file
        Genfile.main(new String[] { genfile, Integer.toString(10) });

        bc = new BufferController(genfile + "reversesorted.bin");
        bc.replacementSelection();

        // this.saveRecords(run1, SOFILE);

        assertEquals(0, bc.getRunBegin()[0], 0.1);
        assertEquals(65536, bc.getRunBegin()[1], 0.1);
        assertEquals(65536, bc.getRunEnd()[0], 0.1);
        assertEquals(81920, bc.getRunEnd()[1], 0.1);

        this.closeForRS(input);

        ////////////////////////////////////////////////////////////////
        // Testing a 4 block random file
        assertTrue(this.compareSingleRunRS(4));
        assertTrue(this.compareSingleRunRS(8));

        ////////////////////////////////////////////////////////////////
        // Testing a 400 block reverse file
        Genfile.main(new String[] { genfile, Integer.toString(400) });

        bc = new BufferController(genfile + "reversesorted.bin");
        bc.replacementSelection();
        // this.saveRecords(run1, SOFILE);

        // Checking if runs are correct
        // For a reverse sorted file, they should always have runs of
        // size 65536 or 8 blocks
        for (int i = 0; i < 50; i++) {
            assertEquals(i * 65536, bc.getRunBegin()[i], 0.1);
            assertEquals(65536 + (i * 65536), bc.getRunEnd()[i], 0.1);
        }

        this.closeForRS(input);
        this.deleteTemp();
    }


    /**
     * Tests the merge sort process.
     */
    public void testMerge() throws FileNotFoundException, IOException {

        // Testing 8 run file
        assertTrue(this.sortAllCycle(64));

        // Testing 7 run file
        assertTrue(this.sortAllCycle(56));

        // Testing 4 run file
        assertTrue(this.sortAllCycle(32));

        // Testing tiny run file
        assertTrue(this.sortAllCycle(4));

        // Testing 9 run file - uncomment when ready to test
        // assertTrue(this.sortAllCycle(65));

        // Known webcat sizes
        assertTrue(this.sortAllCycle(200));
        assertTrue(this.sortAllCycle(24));
        assertTrue(this.sortAllCycle(32));
        assertTrue(this.sortAllCycle(400));
        assertTrue(this.sortAllCycle(48));
        assertTrue(this.sortAllCycle(8));
        assertTrue(this.sortAllCycle(340));

    }

// Helpers --------------------------------------------------------


    /**
     * Performs replacement cycle and merge sort on a file of block size
     * numBlocks. Sorts a sorted, unsorted, and reverseSorted file of
     * the provided block size.
     * 
     * @param numBlocks
     *            block size to test
     * @return true if sorted and assert fails if not
     * @throws IOException
     * 
     */
    private boolean sortAllCycle(int numBlocks) throws IOException {
        // Generating file, modify toString(x) to increase size
        Genfile.main(new String[] { genfile, Integer.toString(numBlocks) });

        this.sortCycle(genfile + ".bin", genfile + "sorted.bin");
        this.sortCycle(genfile + "reversesorted.bin", genfile + "sorted.bin");
        this.copyFile(new File(genfile + "sorted.bin"), new File(genfile
            + "copysorted.bin"));
        this.sortCycle(genfile + "copysorted.bin", genfile + "sorted.bin");

        this.deleteTemp();
        return true;
    }


    /**
     * Sorts a file with replacement selection and merge sort and verifies it
     * Does not remove any files except files generated during sorting.
     * 
     * @param file
     *            File to be sorted
     * @param sortedFile
     *            File with already sorted records
     * @return true if sorted and verified correctly, assert fails if not.
     */
    private boolean sortCycle(String file, String sortedFile)
        throws IOException {

        // Performing replacement selection
        bc = new BufferController(file);
        // bc.replacementSelection();

        // Performing merge sort
        RandomAccessFile from = new RandomAccessFile(run1, "rw");
        RandomAccessFile to = new RandomAccessFile(file, "rw");
        // bc.merge(from, to, 0);
        // bc.mergeAll(from, to);
        bc.replacementMerge();

        // Finding results
        InputBuffer input = new InputBuffer(new RandomAccessFile(file, "r"));

        valid = new Validation(file, sortedFile);
        assertTrue(valid.compare());

        // Closing
        from.close();
        to.close();
        bc.close();
        input.close();
        this.deleteFile(new File(run1));

        return true;
    }


    /**
     * Checks if a run file contains already sorted results,
     * only for inputs of size 8 or less. All temp files are deleted.
     * 
     * @param size
     *            Size of the file in blocks to be generated and tested
     * @return true if files sorted correctly, assertFalse if not.
     * @throws IOException
     */
    private boolean compareSingleRunRS(int size) throws IOException {
        // Testing a 4 block random file
        Genfile.main(new String[] { genfile, Integer.toString(size) });

        // Random
        bc = new BufferController(genfile + ".bin");
        bc.replacementSelection();
        bc.close();
        valid = new Validation(run1, genfile + "sorted.bin");
        assertTrue(valid.compare());

        // Reverse sorted
        bc = new BufferController(genfile + "reversesorted.bin");
        bc.replacementSelection();
        bc.close();
        valid = new Validation(run1, genfile + "sorted.bin");
        assertTrue(valid.compare());

        // Sorted
        this.copyFile(new File(genfile + "sorted.bin"), new File(genfile
            + "copysorted.bin"));
        bc = new BufferController(genfile + "sorted.bin");
        bc.replacementSelection();
        bc.close();
        valid = new Validation(run1, genfile + "copysorted.bin");
        assertTrue(valid.compare());

        this.closeForRS(null);
        this.deleteTemp();

        return true;
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


    /**
     * Closes and removes all temporary files and buffers used for and only
     * for the replacementSelection function
     * 
     * @param input
     *            InputBuffer used during replacement selection
     */
    private void closeForRS(InputBuffer input) throws IOException {
        bc.close();
        File file = new File(run1);
        if (input != null) {
            input.close();
        }
        this.deleteFile(file);
    }


    /**
     * Grabs the next key from input, input position before function call
     * is unchanged.
     * 
     * @param input
     *            InputBuffer to get next key from
     * @return next key in input
     */
    private double getKey(InputBuffer input) {
        input.mark();
        input.next(8);
        double toReturn = input.nextDouble(8);
        input.reset();
        return toReturn;
    }


    /**
     * Copies src file to dest file
     * 
     * @param src
     *            File to copy from
     * @param dest
     *            File to copy to
     * @throws IOException
     */
    private void copyFile(File src, File dest) throws IOException {
        Files.copy(src.toPath(), dest.toPath());

    }

}
