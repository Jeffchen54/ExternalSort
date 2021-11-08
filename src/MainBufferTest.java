import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
 * Test MainBuffer. Will also tests if Replacement selection and merge
 * are possible with the MainBuffer.
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

    }

    // Tests -------------------------------------------------------------


    /**
     * Tests replacement selection on a random sort file. It is extremely
     * difficult to test run files with assert statements.
     * 
     * Length of run files created tested and run order by IO output
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testRandomFile_RS() throws FileNotFoundException, IOException {
        /*
         * this.validate("SampleSort4B.bin", "SampleSort4Bsorted.bin");
         * assertEquals(4, created.length() >> 13);
         * this.setUp();
         * this.validate("SampleSort8B.bin", "SampleSort8Bsorted.bin");
         * assertEquals(8, created.length() >> 13);
         *//*this.setUp();
        this.ioValidate("SampleSort16B.bin");
        assertEquals(16, created.length() >> 13);
        this.setUp();*/
        /*
         * this.ioValidate("SampleSort32B.bin");
         * assertEquals(32, created.length() >> 13);
         * this.setUp();
         * this.ioValidate("SampleSort50B.bin");
         * assertEquals(50, created.length() >> 13);
         * this.setUp();
         * this.ioValidate("SampleSort256B.bin");
         * assertEquals(256, created.length() >> 13);
         * this.setUp();
         * this.ioValidate("SampleSort1250B.bin");
         * assertEquals(1250, created.length() >> 13);
         */

        // Tests run files ------------------------------------------------
        // Single run files
        ArrayWrapper<Long> begin = new ArrayWrapper<>();
        ArrayWrapper<Long> end = new ArrayWrapper<>();
        this.createRun("SampleSort4B.bin", "run4B.bin", begin, end);
        assertEquals(1, begin.get().length);
        assertEquals(1, end.get().length);
        assertEquals(0, begin.get()[0], 0.1);
        assertEquals(4 << 13, end.get()[0], 0.1);
        this.createRun("SampleSort8B.bin", "run8B.bin", begin, end);
        assertEquals(1, begin.get().length);
        assertEquals(1, end.get().length);
        assertEquals(0, begin.get()[0], 0.1);
        assertEquals(8 << 13, end.get()[0], 0.1);
        this.createRun("SampleSort16B.bin", "run16B.bin", begin, end);
        this.ioValidate("SampleSort16B.bin");
        assertEquals(2, begin.get().length);
        assertEquals(2, end.get().length);
        assertEquals(0, begin.get()[0], 0.1);
        assertEquals(end.get()[0], begin.get()[1], 0.1);
        assertEquals(12 << 13, end.get()[0], 0.1);
        assertEquals(16 << 13, end.get()[1], 0.1);
        this.createRun("SampleSort32B.bin", "run32B.bin", begin, end);
        this.ioValidate("SampleSort32B.bin");
        assertEquals(4, begin.get().length);
        assertEquals(4, end.get().length);

        //

    }


    /**
     * Tests replacement selection on a already sort file. It is extremely
     * difficult to test run files with assert statements.
     * 
     * Length of run files created tested and run order by IO output
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Ignore
    public void testSortedFile_RS() throws FileNotFoundException, IOException {
        this.validate("SampleSort4Bsorted.bin", "SampleSort4Bsorted.bin");
        assertEquals(4, created.length() >> 13);
        this.setUp();
        this.validate("SampleSort8Bsorted.bin", "SampleSort8Bsorted.bin");
        assertEquals(8, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort16Bsorted.bin");
        assertEquals(16, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort32Bsorted.bin");
        assertEquals(32, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort50Bsorted.bin");
        assertEquals(50, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort256Bsorted.bin");
        assertEquals(256, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort1250Bsorted.bin");
        assertEquals(1250, created.length() >> 13);
    }


    /**
     * Tests replacement selection on reverse sort file. It is extremely
     * difficult to test run files with assert statements.
     * 
     * Length of run files created tested and run order by IO output
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Ignore

    public void testReverseSortedFile_RS()
        throws FileNotFoundException,
        IOException {
        this.validate("SampleSort4BreverseSorted.bin",
            "SampleSort4Bsorted.bin");
        assertEquals(4, created.length() >> 13);
        this.setUp();
        this.validate("SampleSort8Breversesorted.bin",
            "SampleSort8Bsorted.bin");
        assertEquals(8, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort16Breversesorted.bin");
        assertEquals(16, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort32Breversesorted.bin");
        assertEquals(32, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort50Breversesorted.bin");
        assertEquals(50, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort256Breversesorted.bin");
        assertEquals(256, created.length() >> 13);
        this.setUp();
        this.ioValidate("SampleSort1250Breversesorted.bin");
        assertEquals(1250, created.length() >> 13);
    }


    /**
     * Tests insert and removeMin()
     * 
     * @throws IOException
     */
    @Ignore
    public void testInsertRemoveMin() throws IOException {

        // Initializing byte array
        byte[] data = new byte[8192];
        for (int i = 8; i < 16; i++) {
            data[i] = 1;
        }

        // Removing empty heap
        assertNull(heap.removeMin());

        // Inserting ascending order
        heap.insert(data);
        data[8] = 2;
        heap.insert(data);
        data[8] = 3;
        heap.insert(data);
        data[8] = 4;
        heap.insert(data);
        data[8] = 5;
        heap.insert(data);
        data[8] = 6;
        heap.insert(data);
        data[8] = 7;
        heap.insert(data);
        data[8] = 8;
        heap.insert(data);

        // Validation
        assertEquals(1, heap.removeMin()[8]);
        assertEquals(2, heap.removeMin()[8]);
        assertEquals(3, heap.removeMin()[8]);
        assertEquals(4, heap.removeMin()[8]);
        assertEquals(5, heap.removeMin()[8]);
        assertEquals(6, heap.removeMin()[8]);
        assertEquals(7, heap.removeMin()[8]);
        assertEquals(8, heap.removeMin()[8]);

        // Inserting desending order
        heap.insert(data);
        data[8] = 7;
        heap.insert(data);
        data[8] = 6;
        heap.insert(data);
        data[8] = 5;
        heap.insert(data);
        data[8] = 4;
        heap.insert(data);
        data[8] = 3;
        heap.insert(data);
        data[8] = 2;
        heap.insert(data);
        data[8] = 1;
        heap.insert(data);

        // Validation
        assertEquals(1, heap.removeMin()[8]);
        assertEquals(2, heap.removeMin()[8]);
        assertEquals(3, heap.removeMin()[8]);
        assertEquals(4, heap.removeMin()[8]);
        assertEquals(5, heap.removeMin()[8]);
        assertEquals(6, heap.removeMin()[8]);
        assertEquals(7, heap.removeMin()[8]);
        assertEquals(8, heap.removeMin()[8]);

        // Inserting random order
        heap.insert(data);
        data[8] = 5;
        heap.insert(data);
        data[8] = 8;
        heap.insert(data);
        data[8] = 7;
        heap.insert(data);
        data[8] = 4;
        heap.insert(data);
        data[8] = 3;
        heap.insert(data);
        data[8] = 6;
        heap.insert(data);
        data[8] = 2;
        heap.insert(data);

        // Validation
        assertEquals(1, heap.removeMin()[8]);
        assertEquals(2, heap.removeMin()[8]);
        assertEquals(3, heap.removeMin()[8]);
        assertEquals(4, heap.removeMin()[8]);
        assertEquals(5, heap.removeMin()[8]);
        assertEquals(6, heap.removeMin()[8]);
        assertEquals(7, heap.removeMin()[8]);
        assertEquals(8, heap.removeMin()[8]);

        // Inserting duplicate elements
        data[8] = 1;
        heap.insert(data);
        data[8] = 1;
        heap.insert(data);
        data[8] = 4;
        heap.insert(data);
        data[8] = 2;
        heap.insert(data);
        data[8] = 4;
        heap.insert(data);
        data[8] = 3;
        heap.insert(data);
        data[8] = 2;
        heap.insert(data);
        data[8] = 3;
        heap.insert(data);

        // Validation
        assertEquals(1, heap.removeMin()[8]);
        assertEquals(1, heap.removeMin()[8]);
        assertEquals(2, heap.removeMin()[8]);
        assertEquals(2, heap.removeMin()[8]);
        assertEquals(3, heap.removeMin()[8]);
        assertEquals(3, heap.removeMin()[8]);
        assertEquals(4, heap.removeMin()[8]);
        assertEquals(4, heap.removeMin()[8]);

        // Removing when 0 elements
        assertEquals(null, heap.removeMin());

        // Inserting past heap length
        systemOut().clearHistory();
        data[8] = 1;
        heap.insert(data);
        data[8] = 1;
        heap.insert(data);
        data[8] = 4;
        heap.insert(data);
        data[8] = 2;
        heap.insert(data);
        data[8] = 4;
        heap.insert(data);
        data[8] = 3;
        heap.insert(data);
        data[8] = 2;
        heap.insert(data);
        data[8] = 3;
        heap.insert(data);
        data[8] = 5;
        heap.insert(data);
        assertFuzzyEquals("Heap is full", systemOut().getHistory());

    }


    /**
     * Tests replacementSelection
     * 
     * @throws IOException
     */
    @Ignore
    public void testReplacementSelection() throws IOException {
        // Initializing byte array
        byte[] data = new byte[8192];
        for (int i = 8; i < 16; i++) {
            data[i] = 1;
        }

        output = new OutputBuffer(new RandomAccessFile(created.getName(),
            "rw"));

        // Empty RS
        assertEquals(0, heap.heapSize());
        heap.replacementSelection(data, output);
        assertTrue(output.isFlushed());
        assertEquals(0, heap.heapSize());

        // Inserting in 8 elements random order
        data[8] = 1;
        heap.insert(data);
        data[8] = 5;
        heap.insert(data);
        data[8] = 8;
        heap.insert(data);
        data[8] = 7;
        heap.insert(data);
        data[8] = 4;
        heap.insert(data);
        data[8] = 3;
        heap.insert(data);
        data[8] = 6;
        heap.insert(data);
        data[8] = 2;
        heap.insert(data);

        // Attempting to insert 9th element
        data[8] = 9;
        heap.insert(data);

        // Checking if RS contains the out of bounds element
        assertEquals(8, heap.heapSize());

        for (int i = 0; i < 8; i++) {
            heap.replacementSelection(null, output);
            assertFalse(9 == output.flush()[8]);
        }
        assertEquals(0, heap.heapSize());

        // Resetting heap, checking if heap elements are correct
        heap.reactivateHeap();
        assertEquals(8, heap.heapSize());
        for (int i = 1; i <= 8; i++) {
            heap.replacementSelection(null, output);
            assertEquals(i, output.flush()[8], 0.1);
        }
        assertEquals(0, heap.heapSize());

        // Inserting mix of smaller and larger elements than min element

        // Inserting in 8 elements random order
        data[8] = 5;
        heap.insert(data);
        data[8] = 6;
        heap.insert(data);
        data[8] = 7;
        heap.insert(data);
        data[8] = 8;
        heap.insert(data);
        data[8] = 9;
        heap.insert(data);
        data[8] = 10;
        heap.insert(data);
        data[8] = 11;
        heap.insert(data);
        data[8] = 12;
        heap.insert(data);

        byte negCounter = 4;
        byte posCounter = 6;

        for (int i = 0; i < 16; i++) {
            if (i % 2 == 0) {
                data[8] = posCounter;
                heap.replacementSelection(data, output);
                posCounter++;
            }
            else {
                data[8] = negCounter;
                heap.replacementSelection(data, output);
                negCounter++;
            }
        }
        assertEquals(0, heap.heapSize());
        heap.reactivateHeap();
        assertEquals(8, heap.heapSize());
        // When reset, heap contains only negCounter elements
        for (int i = 4; i < 12; i++) {
            heap.replacementSelection(null, output);
            assertEquals(i, output.getData()[8]);
        }

        // Demonstrating when only 4 blocks input RS
        heap = new MainBuffer();

        data[8] = 4;
        heap.insert(data);
        data[8] = 1;
        heap.insert(data);
        data[8] = 2;
        heap.insert(data);
        data[8] = 3;
        heap.insert(data);

        // Calling RS until heapsize is 0
        for (int i = 1; heap.heapSize() > 0; i++) {
            heap.replacementSelection(null, output);
            assertEquals(i, output.flush()[8]);
        }
        output.close();

    }


    /**
     * Tests reactivateHeap()
     * 
     * @throws IOException
     */
    @Ignore
    public void testReactivateHeap() throws IOException {
        byte[] data = new byte[8192];
        for (int i = 8; i < 16; i++) {
            data[i] = 5;
        }

        // Reactivating empty heap
        heap.reactivateHeap();
        assertEquals(0, heap.heapSize());

        // Inserting 4 elements, calling reactivate heap
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);

        heap.reactivateHeap();
        assertEquals(4, heap.heapSize());

        // Inserting 8 elements, calling reactivate heap
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);

        heap.reactivateHeap();
        assertEquals(8, heap.heapSize());

        // Inserting 8 smaller elements through replacement selection
        output = new OutputBuffer(new RandomAccessFile(created.getName(),
            "rw"));

        data[8] = 1;
        heap.replacementSelection(data, output);
        heap.replacementSelection(data, output);
        heap.replacementSelection(data, output);
        heap.replacementSelection(data, output);
        heap.replacementSelection(data, output);
        heap.replacementSelection(data, output);
        heap.replacementSelection(data, output);
        heap.replacementSelection(data, output);

        assertEquals(0, heap.heapSize());

        // Reactivate heap and checking if all entries are the smaller elements
        heap.reactivateHeap();

        while (heap.heapSize() > 0) {
            heap.replacementSelection(null, output);
            assertEquals(1, output.getData()[8]);
        }
        output.close();

    }


    /**
     * Tests merge sort process
     */
    @Ignore
    public void testMergeSort() {

    }

    // Helpers ----------------------------------------------------------------


    /**
     * Creates a run file
     * 
     * @precondition All streams using runFIle must be closed.
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void createRun(
        String inputFile,
        String runFile,
        ArrayWrapper<Long> runBeginDest,
        ArrayWrapper<Long> runEndDest)
        throws FileNotFoundException,
        IOException {
        RandomAccessFile blocks = new RandomAccessFile(inputFile, "r");
        InputBuffer input = new InputBuffer(new RandomAccessFile(inputFile,
            "r"));

        File run = new File(runFile);
        run.delete();
        run.createNewFile();
        int inserted = 1;

        output = new OutputBuffer(new RandomAccessFile(runFile, "rw"));
        int counter = 0;

        // Creating arrays to keep track of run positions
        // Converts bytes to blocks, then bytes to runs of size 8.
        // There is only 1 run if length of arrays are <= 1
        // runID tracks current position in runID

        int blockCount = ((int)(blocks.length() >> 13));
        int maxRunCount = blockCount >> 3;
        int mask = 0x3;

        // Catch remainder, if exists, we need 1 extra run
        if ((blockCount & mask) != 0) {
            maxRunCount++;
        }

        // For when only run of size 1 exists, not caught by above mask
        else if (maxRunCount == 0) {
            maxRunCount++;
        }

        Long[] runBegin = new Long[maxRunCount];
        Long[] runEnd = new Long[maxRunCount];
        int runID = 0;

        // Filling heap with 8 values
        while (counter < 8 && !input.endOfFile()) {
            input.next(8);
            System.out.println(inserted + " Inserted:" + input.nextDouble(8));
            inserted++;
            heap.insert(input.getData());
            input.nextBlock();
            counter++;
        }

        // Last entry
        if (counter < 8) {
            input.next(8);
            System.out.println(inserted + " Inserted:" + input.nextDouble(8));
            inserted++;
            heap.insert(input.getData());
            input.nextBlock();
        }
        // Replacement selection - Processes all Input file items, heap size
        // may not be 0 after heap is finished
        while (heap.heapSize() != 0 && !input.endOfFile()) {
            // Calling replacement selection until either max run is reached or
            // no more input
            while (!input.endOfFile() && heap.heapSize() != 0) {
                input.next(8);
                System.out.println(inserted + " Inserted:" + input.nextDouble(
                    8));
                inserted++;
                heap.replacementSelection(input.getData(), output);
                input.nextBlock();
                output.flush();

            }

            // Reactivating heap if needed, end run position is here
            if (heap.heapSize() == 0) {
                heap.reactivateHeap();

                // Case for runBegin
                if (runID == 0) {
                    runBegin[0] = 0L;
                }
                else {
                    runBegin[runID] = runEnd[runID - 1];
                }

                // runEnd only has 1 case
                // Current position run end == InputBlocks read - Blocks in heap
                runEnd[runID] = input.filePointer() - 65536;

                // Incrementing runID
                runID++;
            }

            // Last statement not covered by while
            if (input.endOfFile()) {
                input.next(8);
                System.out.println(inserted + " Inserted:" + input.nextDouble(
                    8));
                inserted++;
                heap.replacementSelection(input.getData(), output);
                output.flush();

                int heapRemain = heap.heapSize();
                // Wiping remaining heap elements
                while (heap.heapSize() != 0) {

                    heap.replacementSelection(null, output);
                    input.nextBlock();
                    output.flush();
                }
                // Case for runBegin
                if (runID == 0) {
                    runBegin[0] = 0L;
                }
                else {
                    runBegin[runID] = runEnd[runID - 1];
                }

                // runEnd only has 1 case
                // Current position run end == InputBlocks read - Blocks in heap
                runEnd[runID] = input.filePointer() - (heapRemain << 13);

                // Incrementing runID
                runID++;
            }

        }

        // Wiping remaining live heap elements
        /**
         * while (heap.heapSize() > 0) {
         * heap.replacementSelection(null, output);
         * output.flush();
         * }
         */

        // Recovering old heap elements not wiped.
        heap.reactivateHeap();
        while (heap.heapSize() > 0) {
            heap.replacementSelection(null, output);
            output.flush();
        }

        // Registers run data
        if (runID == 0) {
            runBegin[0] = 0L;
        }
        else {
            runBegin[runID] = runEnd[runID - 1];
        }

        // Case for runEnd since run files of size 1 are processed here
       // if (runEnd.length <= 1) {
         //   runEnd[0] = input.filePointer();
        //}
       //else {
            runEnd[runID] = input.filePointer();
        //}

        if (runBeginDest != null) {
            runBeginDest.wrap(runBegin);
        }
        if (runEndDest != null) {
            runEndDest.wrap(runEnd);
        }

        input.close();
        output.close();

    }


    /**
     * Validate testing results, for inputs <= 8 blocks RS or all merge cases
     */
    private void validate(String inputFile, String validationFile)
        throws FileNotFoundException,
        IOException {

        this.createRun(inputFile, created.getName(), null, null);

        // Validation
        InputBuffer input = new InputBuffer(new RandomAccessFile(created
            .getName(), "r"));
        InputBuffer validation = new InputBuffer(new RandomAccessFile(
            validationFile, "r"));
        System.out.println(validationFile);

        SubBuffer inputData = new SubBuffer();
        SubBuffer valiData = new SubBuffer();

        input.rewind();
        validation.rewind();

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
        input.close();
        validation.close();

    }


    /**
     * Validate testing results, for inputs > 8 blocks
     */
    private void ioValidate(String inputFile)
        throws FileNotFoundException,
        IOException {

        this.createRun(inputFile, testingOutput, null, null);

        // Validation
        InputBuffer input = new InputBuffer(new RandomAccessFile(testingOutput,
            "r"));
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
        input.close();
    }

}
