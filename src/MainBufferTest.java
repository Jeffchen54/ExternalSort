import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import org.junit.After;
import org.junit.AfterClass;
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
     * Tests creation of runFile by visual inspection and run length validation.
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testRandomFile_RS() throws FileNotFoundException, IOException {

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
        assertEquals(2, begin.get().length);
        assertEquals(2, end.get().length);
        assertEquals(0, begin.get()[0], 0.1);
        assertEquals(end.get()[0], begin.get()[1], 0.1);
        assertEquals(12 << 13, end.get()[0], 0.1);
        assertEquals(16 << 13, end.get()[1], 0.1);

        this.createRun("SampleSort32B.bin", "run32B.bin", begin, end);
        assertEquals(4, begin.get().length);
        assertEquals(4, end.get().length);
        assertEquals(0, begin.get()[0], 0.1);
        assertEquals(end.get()[0], begin.get()[1], 0.1);
        assertEquals(end.get()[1], begin.get()[2], 0.1);
        assertNull(begin.get()[3]);

        assertEquals(16 << 13, end.get()[0], 0.1);
        assertEquals(29 << 13, end.get()[1], 0.1);
        assertEquals(32 << 13, end.get()[2], 0.1);
        assertNull(end.get()[3]);

        this.createRun("SampleSort50B.bin", "run50B.bin", begin, end);
        assertEquals(7, begin.get().length);
        assertEquals(7, end.get().length);
        assertEquals(0, begin.get()[0], 0.1);
        assertEquals(end.get()[0], begin.get()[1], 0.1);
        assertEquals(end.get()[1], begin.get()[2], 0.1);
        assertEquals(end.get()[2], begin.get()[3], 0.1);
        assertNull(begin.get()[4]);
        assertNull(begin.get()[5]);
        assertNull(begin.get()[6]);
        assertEquals(14 << 13, end.get()[0], 0.1);
        assertEquals(29 << 13, end.get()[1], 0.1);
        assertEquals(42 << 13, end.get()[2], 0.1);
        assertEquals(50 << 13, end.get()[3], 0.1);
        assertNull(end.get()[4]);
        assertNull(end.get()[5]);
        assertNull(end.get()[6]);

        this.createRun("SampleSort256B.bin", "run256B.bin", begin, end);
        assertEquals(32, begin.get().length);
        assertEquals(32, end.get().length);
        assertEquals(0, begin.get()[0], 0.1);
        assertEquals(end.get()[0], begin.get()[1], 0.1);
        assertEquals(end.get()[1], begin.get()[2], 0.1);
        assertEquals(end.get()[2], begin.get()[3], 0.1);
        assertEquals(end.get()[3], begin.get()[4], 0.1);
        assertEquals(end.get()[4], begin.get()[5], 0.1);
        assertEquals(end.get()[5], begin.get()[6], 0.1);
        assertEquals(end.get()[6], begin.get()[7], 0.1);
        assertEquals(end.get()[7], begin.get()[8], 0.1);
        assertEquals(end.get()[8], begin.get()[9], 0.1);
        assertEquals(end.get()[9], begin.get()[10], 0.1);
        assertEquals(end.get()[10], begin.get()[11], 0.1);
        assertEquals(end.get()[11], begin.get()[12], 0.1);
        assertEquals(end.get()[12], begin.get()[13], 0.1);
        assertEquals(end.get()[13], begin.get()[14], 0.1);
        assertEquals(end.get()[14], begin.get()[15], 0.1);
        assertNull(begin.get()[16]);

        assertEquals(16 << 13, end.get()[0], 0.1);
        assertEquals(31 << 13, end.get()[1], 0.1);
        assertEquals(45 << 13, end.get()[2], 0.1);
        assertEquals(59 << 13, end.get()[3], 0.1);
        assertEquals(78 << 13, end.get()[4], 0.1);
        assertEquals(93 << 13, end.get()[5], 0.1);
        assertEquals(111 << 13, end.get()[6], 0.1);
        assertEquals(129 << 13, end.get()[7], 0.1);
        assertEquals(146 << 13, end.get()[8], 0.1);
        assertEquals(161 << 13, end.get()[9], 0.1);
        assertEquals(178 << 13, end.get()[10], 0.1);
        assertEquals(196 << 13, end.get()[11], 0.1);
        assertEquals(213 << 13, end.get()[12], 0.1);
        assertEquals(228 << 13, end.get()[13], 0.1);
        assertEquals(245 << 13, end.get()[14], 0.1);
        assertEquals(256 << 13, end.get()[15], 0.1);
        assertNull(end.get()[16]);

        // Largest file to be generated successfully in genfile.
        // Used for speed purposes only and light testing & debug
        // 10MB file should be good enough
        this.createRun("SampleSort1250B.bin", "run1250B.bin", begin, end);
        this.ioValidate("SampleSort1250B.bin");
        assertEquals(157, begin.get().length);
        assertEquals(157, end.get().length);
    }


    /**
     * Tests replacement selection on a already sort file. Validates file
     * contents.
     * 
     * Length of run files created tested and run order by IO output.
     * Can be slow due to run file creation and validation by linear comparison.
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testSortedFile_RS() throws FileNotFoundException, IOException {
        this.validate("SampleSort4Bsorted.bin", "SampleSort4Bsorted.bin");
        assertEquals(4, created.length() >> 13);
        this.setUp();
        this.validate("SampleSort8Bsorted.bin", "SampleSort8Bsorted.bin");
        assertEquals(8, created.length() >> 13);
        this.setUp();
        this.validate("SampleSort16Bsorted.bin", "SampleSort16Bsorted.bin");
        assertEquals(16, created.length() >> 13);
        this.setUp();
        this.validate("SampleSort32Bsorted.bin", "SampleSort32Bsorted.bin");
        assertEquals(32, created.length() >> 13);
        this.setUp();
        this.validate("SampleSort50Bsorted.bin", "SampleSort50Bsorted.bin");
        assertEquals(50, created.length() >> 13);
        this.setUp();
        this.validate("SampleSort256Bsorted.bin", "SampleSort256Bsorted.bin");
        assertEquals(256, created.length() >> 13);
        this.setUp();
        this.validate("SampleSort1250Bsorted.bin", "SampleSort1250Bsorted.bin");
        assertEquals(1250, created.length() >> 13);
    }


    /**
     * Tests replacement selection on reverse sort file.
     * 
     * Reverse sorted runs appear as reverse order 8-block segments on
     * run file.
     * 
     * Run file validity checked by inspection and by # of runs where if a file
     * x
     * has the worst case scenario (reverse sorted), then all runs are <= 8,
     * therefore, # of runs are the worst case number
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
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
        ArrayWrapper<Long> begin = new ArrayWrapper<>();
        ArrayWrapper<Long> end = new ArrayWrapper<>();

        this.createRun("SampleSort16Breversesorted.bin", "run16B.bin", begin,
            end);
        assertEquals(16 << 13, end.get()[1], 0.1);

        this.setUp();
        this.createRun("SampleSort32Breversesorted.bin", "run32B.bin", begin,
            end);
        assertEquals(32 << 13, end.get()[3], 0.1);

        this.setUp();
        this.createRun("SampleSort50Breversesorted.bin", "run50B.bin", begin,
            end);
        assertEquals(50 << 13, end.get()[6], 0.1);

        this.setUp();
        this.createRun("SampleSort256Breversesorted.bin", "run256B.bin", begin,
            end);
        assertEquals(256 << 13, end.get()[31], 0.1);

        this.setUp();
        this.createRun("SampleSort1250Breversesorted.bin", "run1250B.bin",
            begin, end);
        assertEquals(1250 << 13, end.get()[156], 0.1);

        this.ioValidate("SampleSort8Breversesorted.bin");
    }


    /**
     * Tests insert and removeMin()
     * 
     * @throws IOException
     */
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

        // Resetting heap, checking if non flushed elements are correct
        heap.reactivateHeap();
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

        // Calling RS once heap is 0
        assertEquals(0, heap.heapSize());
        heap.replacementSelection(null, output);
        assertEquals(0, heap.heapSize());

        output.close();

    }


    /**
     * Tests reactivateHeap()
     * 
     * @throws IOException
     */
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

        // Flushing some values then checking if it exists after reactivation
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);
        heap.insert(data);

        heap.reactivateHeap();
        heap.flushMin();
        heap.flushMin();
        heap.flushMin();
        heap.flushMin();
        heap.flushMin();
        heap.flushMin();

        heap.reactivateHeap();
        assertEquals(2, heap.heapSize());
        assertNotNull(heap.removeMin());
        assertNotNull(heap.removeMin());
        assertNull(heap.removeMin());
        output.close();

    }


    /**
     * Tests merge sort process
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testMergeSort() throws FileNotFoundException, IOException {
        // 8B
        ArrayWrapper<Long> begin = new ArrayWrapper<>();
        ArrayWrapper<Long> end = new ArrayWrapper<>();
        InputBuffer inputBuff = new InputBuffer(new RandomAccessFile(
            "run4B.bin", "r"));
        OutputBuffer outputBuff = new OutputBuffer(new RandomAccessFile(
            "run4B.bin", "r"));
        this.createRun("SampleSort4B.bin", "run4B.bin", begin, end);
        this.eightMerge(begin, end, "run4B.bin", "output4B.bin", inputBuff,
            outputBuff);
        this.validate("output4B.bin", "SampleSort4Bsorted.bin");
        this.createRun("SampleSort8B.bin", "run8B.bin", begin, end);
        this.eightMerge(begin, end, "run8B.bin", "output8B.bin", inputBuff,
            outputBuff);
        this.validate("output8B.bin", "SampleSort8Bsorted.bin");
        this.createRun("SampleSort16B.bin", "run16B.bin", begin, end);
        this.eightMerge(begin, end, "run16B.bin", "output16B.bin", inputBuff,
            outputBuff);
        this.validate("output16B.bin", "SampleSort16Bsorted.bin");
        this.createRun("SampleSort32B.bin", "run32B.bin", begin, end);
        this.eightMerge(begin, end, "run32B.bin", "output32B.bin", inputBuff,
            outputBuff);
        this.validate("output32B.bin", "SampleSort32Bsorted.bin");
        this.createRun("SampleSort50B.bin", "run50B.bin", begin, end);
        this.eightMerge(begin, end, "run50B.bin", "output50B.bin", inputBuff,
            outputBuff);
        this.validate("output50B.bin", "SampleSort50Bsorted.bin");
        this.createRun("SampleSort256B.bin", "run256B.bin", begin, end);
        this.eightMerge(begin, end, "run256B.bin", "output256B.bin", inputBuff,
            outputBuff);
        this.validate("output256B.bin", "SampleSort256Bsorted.bin");
        this.createRun("SampleSort1250B.bin", "run1250B.bin", begin, end);
        this.eightMerge(begin, end, "run1250B.bin", "output1250B.bin", inputBuff,
            outputBuff);
        this.validate("output1250B.bin", "SampleSort1250Bsorted.bin");
                
        this.createRun("SampleSort4Breversesorted.bin", "run4B.bin", begin, end);
        this.eightMerge(begin, end, "run4B.bin", "output4B.bin", inputBuff,
            outputBuff);
        this.validate("output4B.bin", "SampleSort4Bsorted.bin");
        this.createRun("SampleSort8Breversesorted.bin", "run8B.bin", begin, end);
        this.eightMerge(begin, end, "run8B.bin", "output8B.bin", inputBuff,
            outputBuff);
        this.validate("output8B.bin", "SampleSort8Bsorted.bin");
        this.createRun("SampleSort16Breversesorted.bin", "run16B.bin", begin, end);
        this.eightMerge(begin, end, "run16B.bin", "output16B.bin", inputBuff,
            outputBuff);
        this.validate("output16B.bin", "SampleSort16Bsorted.bin");
        this.createRun("SampleSort32Breversesorted.bin", "run32B.bin", begin, end);
        this.eightMerge(begin, end, "run32B.bin", "output32B.bin", inputBuff,
            outputBuff);
        this.validate("output32B.bin", "SampleSort32Bsorted.bin");
        this.createRun("SampleSort50Breversesorted.bin", "run50B.bin", begin, end);
        this.eightMerge(begin, end, "run50B.bin", "output50B.bin", inputBuff,
            outputBuff);
        this.validate("output50B.bin", "SampleSort50Bsorted.bin");
        this.createRun("SampleSort256Breversesorted.bin", "run256B.bin", begin, end);
        this.eightMerge(begin, end, "run256B.bin", "output256B.bin", inputBuff,
            outputBuff);
        this.validate("output256B.bin", "SampleSort256Bsorted.bin");
        this.createRun("SampleSort1250Breversesorted.bin", "run1250B.bin", begin, end);
        this.eightMerge(begin, end, "run1250B.bin", "output1250B.bin", inputBuff,
            outputBuff);
        this.validate("output1250B.bin", "SampleSort1250Bsorted.bin");
        
        this.createRun("SampleSort4Bsorted.bin", "run4B.bin", begin, end);
        this.eightMerge(begin, end, "run4B.bin", "output4B.bin", inputBuff,
            outputBuff);
        this.validate("output4B.bin", "SampleSort4Bsorted.bin");
        this.createRun("SampleSort8Bsorted.bin", "run8B.bin", begin, end);
        this.eightMerge(begin, end, "run8B.bin", "output8B.bin", inputBuff,
            outputBuff);
        this.validate("output8B.bin", "SampleSort8Bsorted.bin");
        this.createRun("SampleSort16Bsorted.bin", "run16B.bin", begin, end);
        this.eightMerge(begin, end, "run16B.bin", "output16B.bin", inputBuff,
            outputBuff);
        this.validate("output16B.bin", "SampleSort16Bsorted.bin");
        this.createRun("SampleSort32Bsorted.bin", "run32B.bin", begin, end);
        this.eightMerge(begin, end, "run32B.bin", "output32B.bin", inputBuff,
            outputBuff);
        this.validate("output32B.bin", "SampleSort32Bsorted.bin");
        this.createRun("SampleSort50Bsorted.bin", "run50B.bin", begin, end);
        this.eightMerge(begin, end, "run50B.bin", "output50B.bin", inputBuff,
            outputBuff);
        this.validate("output50B.bin", "SampleSort50Bsorted.bin");
        this.createRun("SampleSort256Bsorted.bin", "run256B.bin", begin, end);
        this.eightMerge(begin, end, "run256B.bin", "output256B.bin", inputBuff,
            outputBuff);
        this.validate("output256B.bin", "SampleSort256Bsorted.bin");
        this.createRun("SampleSort1250Bsorted.bin", "run1250B.bin", begin, end);
        this.eightMerge(begin, end, "run1250B.bin", "output1250B.bin", inputBuff,
            outputBuff);
        this.validate("output1250B.bin", "SampleSort1250Bsorted.bin");
    }
    

    // Helpers ----------------------------------------------------------------
    
    /**
     * Deletes all temp files created by tests
     */
    @AfterClass
    public static void deleteTempFiles() {
        File temp = new File("output4B.bin");
        temp.delete();
        new File("output8B.bin");
        temp.delete();
        new File("output16B.bin");
        temp.delete();
        new File("output32B.bin");
        temp.delete();
        new File("output50B.bin");
        temp.delete();
        new File("output256B.bin");
        temp.delete();
        new File("output1250B.bin");
        temp.delete();
        
        temp = new File("run4B.bin");
        temp.delete();
        new File("run8B.bin");
        temp.delete();
        new File("run16B.bin");
        temp.delete();
        new File("run32B.bin");
        temp.delete();
        new File("run50B.bin");
        temp.delete();
        new File("run256B.bin");
        temp.delete();
        new File("run1250B.bin");
        temp.delete();
        
        temp = new File("testing.bin");
        temp.delete();
    }

    /**
     * Performs 8-way merge
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void eightMerge(
        ArrayWrapper<Long> begin,
        ArrayWrapper<Long> end,
        String runFile,
        String output,
        InputBuffer inputBuff,
        OutputBuffer outputBuff)
        throws FileNotFoundException,
        IOException {

        // Required permanent variables
        File temp = new File("tempRun.bin");
        temp.delete();
        String runFile2 = "tempRun.bin";
        inputBuff.changeFile(new RandomAccessFile(runFile, "r"));
        outputBuff.changeFile(new RandomAccessFile(runFile2, "rw"));

        // Check if begin or end are already sorted, if so no merge needs to be
        // done and can copy into output
        if (end.get().length < 2 || end.get()[1] == null) {
            outputBuff.changeFile(new RandomAccessFile(output, "rw"));
            this.printToFile(inputBuff, outputBuff);
            outputBuff.close();
            return;
        }

        // Each iteration means a new pass
        for (int pass = 0; begin.get()[1] != null; pass++) {

            // Initializing and reseting variables from pass to pass
            Long[] newBegin = new Long[begin.get().length];
            Long[] newEnd = new Long[end.get().length];
            int runCount = 0;

            // Cycles between 8 runs at a time
            for (int i = 0; i < begin.get().length && begin
                .get()[i] != null; i += 8) {

                int processedBlocks = 0;

                // Building beginning array
                if (runCount == 0) {
                    newBegin[0] = 0L;
                }
                else {
                    newBegin[runCount] = newEnd[runCount - 1];
                }

                // Inserts 1-8 initial run elements into heap
                for (int n = i; n < begin.get().length && begin.get()[n] != null
                    && n - i != 8; n++) {
                    inputBuff.seek(begin.get()[n]);
                    heap.insert(inputBuff.getData(), n);
                }

                // Actual output, insert done here
                while (heap.heapSize() != 0) {

                    int id = heap.removeMin(outputBuff);
                    System.out.println("Value: " + outputBuff.getKey());
                    begin.setValue(id, begin.get()[id] += 8192);

                    // If run is not finished, insert another block from run
                    if (begin.get()[id] < end.get()[id]) {
                        inputBuff.seek(begin.get()[id]);
                        heap.insert(inputBuff.getData(), id);
                    }

                    // Output run block to file
                    outputBuff.flush();
                    processedBlocks++;
                }

                // All runs done, computing runEnd
                if (runCount == 0) {
                    newEnd[0] = (long)(processedBlocks << 13);
                }
                else {
                    newEnd[runCount] = newEnd[runCount - 1]
                        + (long)(processedBlocks << 13);
                }

                // Incrementing run count since 1 run was just completed
                runCount++;
            }

            // End of current pass, set arrays up and runfile for next pass
            begin.wrap(newBegin);
            end.wrap(newEnd);
            outputBuff.close();

            if ((pass & 0x1) != 1) {
                outputBuff.changeFile(new RandomAccessFile(runFile, "rw"));
                inputBuff.changeFile(new RandomAccessFile(runFile2, "r"));
            }
            else {
                outputBuff.changeFile(new RandomAccessFile(runFile2, "rw"));
                inputBuff.changeFile(new RandomAccessFile(runFile, "r"));
            }
        }

        // At this point file should be sorted correctly, copies to output
        outputBuff.changeFile(new RandomAccessFile(output, "rw"));

        this.printToFile(inputBuff, outputBuff);
    }


    /**
     * @throws IOException
     * 
     */
    private void printToFile(InputBuffer inputBuff, OutputBuffer outputBuff)
        throws IOException {
        while (!inputBuff.endOfFile()) {
            outputBuff.setData(inputBuff.getData());
            System.out.println("Flushed: " + outputBuff.getKey());
            outputBuff.flush();
            inputBuff.nextBlock();
        }

        // Last block not caught in loop
        outputBuff.setData(inputBuff.getData());
        System.out.println("Flushed: " + outputBuff.getKey());
        outputBuff.flush();

    }


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
                runEnd[runID] = input.filePointer() - 73728;

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
                // that are not flushed
                runEnd[runID] = input.filePointer() - ((8 - heapRemain) << 13);

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
        if (heap.heapSize() > 0) {
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
            // runEnd[0] = input.filePointer();
            // }
            // else {
            runEnd[runID] = input.filePointer();
            // }
        }
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
            System.out.println(valiData.getKey() + " vs " + inputData.getKey());
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
