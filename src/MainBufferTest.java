import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import org.junit.Test;
import student.TestCase;

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
 * Test MainBuffer
 * 
 * @author chenj (chenjeff4840)
 * @version 11.3.2021
 */
public class MainBufferTest extends TestCase {
    // Fields -------------------------------------------------------------
    private MainBuffer heap;
    private OutputBuffer output;
    private File created;
    private String testingOutput;

    // Setup -------------------------------------------------------------
    /**
     * Sets up an empty heap
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        heap = new MainBuffer();

        created = new File("testing.bin");
        created.delete();
        created.createNewFile();

        testingOutput = "testing.bin";
        output = new OutputBuffer(new RandomAccessFile(testingOutput, "rw"));

    }

    // Tests -------------------------------------------------------------


    /**
     * Tests replacement selection on a random sort file
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testRandomFile_RS() throws FileNotFoundException, IOException {
        this.validate("SampleSort4B.bin", "SampleSort4Bsorted.bin");
        this.setUp();
        this.validate("SampleSort8B.bin", "SampleSort8Bsorted.bin");
        this.setUp();
        //this.ioValidate("SampleSort16B.bin");
        this.setUp();
        // this.ioValidate("SampleSort32B.bin");
        this.setUp();
        // this.ioValidate("SampleSort50B.bin");
        this.setUp();
    }


    /**
     * Validate testing results
     */
    private void validate(String inputFile, String validationFile)
        throws FileNotFoundException,
        IOException {
        InputBuffer input = new InputBuffer(new RandomAccessFile(inputFile,
            "r"));
        int counter = 0;

        // Filling heap with 8 values
        while (counter < 8 && !input.endOfFile()) {
            heap.insert(input.getData());
            input.nextBlock();
            counter++;
        }

        // End of file condition
        if (counter != 8) {
            heap.insert(input.getData());
            input.nextBlock();
        }

        // Replacement selection
        while (!input.endOfFile()) {
            // Calling replacement selection until either max run is reached or
            // no more input
            while (!input.endOfFile() && heap.heapSize() != 0) {
                heap.replacementSelection(input.getData(), output);
                input.nextBlock();
                output.flush();
            }

            // Reactivating heap if needed
            if (heap.heapSize() == 0) {
                heap.reactivateHeap();
            }

            // Last statement not covered by while
            if (input.endOfFile()) {
                heap.replacementSelection(input.getData(), output);
                output.flush();
            }
        }

        // Removes last elements in heap
        while (heap.heapSize() > 0) {
            heap.replacementSelection(null, output);
            output.flush();
        }

        output.close();

        // Validation
        input.changeFile(new RandomAccessFile(testingOutput, "r"));
        InputBuffer validation = new InputBuffer(new RandomAccessFile(
            validationFile, "r"));
        SubBuffer inputData = new SubBuffer();
        SubBuffer valiData = new SubBuffer();

        while (!input.endOfFile() && !validation.endOfFile()) {
            inputData.setData(input.getData());
            valiData.setData(validation.getData());
            assertEquals(0, inputData.compareTo(valiData));

            input.nextBlock();
            validation.nextBlock();

            if (input.endOfFile()) {
                inputData.setData(input.getData());
                valiData.setData(validation.getData());
                assertEquals(0, inputData.compareTo(valiData));
            }
        }

        assertEquals(validation.endOfFile(), input.endOfFile());

    }


    /**
     * Validate testing results
     */
    private void ioValidate(String inputFile)
        throws FileNotFoundException,
        IOException {
        InputBuffer input = new InputBuffer(new RandomAccessFile(inputFile,
            "r"));
        int counter = 0;

        // Filling heap with 8 values
        while (counter < 8 && !input.endOfFile()) {
            input.next(8);
            System.out.println("Inserted: " + input.nextDouble(8));
            heap.insert(input.getData());
            input.nextBlock();
            counter++;
        }

        // End of file condition
        if (counter != 8) {
            input.next(8);
            System.out.println("Inserted: " + input.nextDouble(8));
            heap.insert(input.getData());
            input.nextBlock();
        }

        // Replacement selection
        while (!input.endOfFile()) {
            // Calling replacement selection until either max run is reached or
            // no more input
            while (!input.endOfFile() && heap.heapSize() != 0) {
                heap.replacementSelection(input.getData(), output);
                input.nextBlock();
                System.out.println("Inserted: " + output.getKey());
                output.flush();

            }

            // Reactivating heap if needed
            if (heap.heapSize() == 0) {
                heap.reactivateHeap();
            }

            // Last statement not covered by while
            if (input.endOfFile()) {
                heap.replacementSelection(input.getData(), output);
                System.out.println("Inserted: " + output.getKey());
                output.flush();
            }
        }
        
        heap.reactivateHeap();
        // Removes last elements in heap
        while (heap.heapSize() > 0) {
            heap.replacementSelection(null, output);
            output.flush();
        }

        output.close();

        // Validation
        input.changeFile(new RandomAccessFile(testingOutput, "r"));
        SubBuffer inputData = new SubBuffer();
        int block = 1;
        while (!input.endOfFile()) {
            inputData.setData(input.getData());
            input.nextBlock();
            System.out.println(block + ": " + inputData.getKey());
            block++;
            if (input.endOfFile()) {
                inputData.setData(input.getData());
                System.out.println(block + ": " + inputData.getKey());
            }
        }

    }

}
