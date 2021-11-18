import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
 * Manages InputBuffer, OutputBuffer, and MainBuffer.
 * Automatic file overwriting function.
 * 
 * @author chenj (chenjeff4840)
 * @version 11.3.2021
 */
public class BufferController {

    // Fields ---------------------------------------------------------------
    private InputBuffer input;
    private OutputBuffer output;
    private MainBuffer heap;
    private String inputFileName;
    private String outputFileName;
    private ArrayWrapper<Long> runBeginDest;
    private ArrayWrapper<Long> runEndDest;
    private String runfile1 = "JeffChenRunUno.bin";
    private String runfile2 = "JeffChenRunDos.bin";

    // Constructor -----------------------------------------------------------

    /**
     * Initializes an 8 block main buffer, 1 block input buffer, 1 block output
     * buffer and all neccessary variables to run replacement selection and
     * merge sort.
     * 
     * @param inputName
     *            name of input file.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public BufferController(String inputName)
        throws FileNotFoundException,
        IOException {
        inputFileName = inputName;
        outputFileName = inputName;
        input = new InputBuffer(new RandomAccessFile(inputFileName, "r"));
        output = new OutputBuffer(new RandomAccessFile(outputFileName, "rw"));
        heap = new MainBuffer();
        runEndDest = new ArrayWrapper<Long>();
        runBeginDest = new ArrayWrapper<Long>();

    }

    // Functions -----------------------------------------------------------


    /**
     * Performs replacement selection and merge sort on the input file, saving
     * the result to the input file and logging results to standard output.
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void replacementMerge() throws FileNotFoundException, IOException {

        runfile2 = inputFileName;

        this.replacementSelection();
        File a = new File(runfile1);

        if (!a.exists()) {
            throw new IOException();
        }

        //this.mergeSort();
        this.close();
    }


    // Helpers -----------------------------------------------------------
    /**
     * Performs 8-way merge. Saves the result to inputFile and creates temp
     * files.
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void mergeSort() throws FileNotFoundException, IOException {

        clearFile(runfile2);

        // Required permanent variables
        input.changeFile(new RandomAccessFile(runfile1, "r"));
        output.changeFile(new RandomAccessFile(runfile2, "rw"));

        // Check if runBeginDest or runEndDest are already sorted, if so no
        // merge needs to be
        // done and can copy into output
        if (runEndDest.get().length < 2 || runEndDest.get()[1] == null) {
            output.changeFile(new RandomAccessFile(outputFileName, "rw"));
            this.printToFile(input, output);
            output.close();
            return;
        }

        // Each iteration means a new pass
        for (int pass = 0; runBeginDest.get()[1] != null; pass++) {

            // Initializing and reseting variables from pass to pass
            Long[] newBegin = new Long[runBeginDest.get().length];
            Long[] newEnd = new Long[runEndDest.get().length];
            int runCount = 0;

            // Cycles between 8 runs at a time
            for (int i = 0; i < runBeginDest.get().length && runBeginDest
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
                for (int n = i; n < runBeginDest.get().length && runBeginDest
                    .get()[n] != null && n - i != 8; n++) {
                    input.seek(runBeginDest.get()[n]);
                    heap.insert(input.getData(), n);
                }

                // Actual output, insert done here
                while (heap.heapSize() != 0) {

                    int id = heap.removeMin(output);
                    long newBeginDest = runBeginDest.get()[id] += 8192;
                    runBeginDest.setValue(id, newBeginDest);

                    // If run is not finished, insert another block from run
                    if (runBeginDest.get()[id] < runEndDest.get()[id]) {
                        input.seek(runBeginDest.get()[id]);
                        heap.insert(input.getData(), id);
                    }

                    // Output run block to file
                    output.flush();
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
            runBeginDest.wrap(newBegin);
            runEndDest.wrap(newEnd);
            output.close();

            if ((pass & 0x1) != 1) {
                output.changeFile(new RandomAccessFile(runfile1, "rw"));
                input.changeFile(new RandomAccessFile(runfile2, "r"));
            }
            else {
                output.changeFile(new RandomAccessFile(runfile2, "rw"));
                input.changeFile(new RandomAccessFile(runfile1, "r"));
            }
        }

        // At this point file should be sorted correctly, copies to output
        this.printToFile(input, output);
    }


    /**
     * Creates a run file using replacement selection.
     * 
     * @precondition All streams using runFIle must be closed.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void replacementSelection()
        throws FileNotFoundException,
        IOException {
        RandomAccessFile blocks = new RandomAccessFile(inputFileName, "r");
        input.changeFile(blocks);
        int inserted = 1;

        output.changeFile(new RandomAccessFile(runfile1, "rw"));
        int counter = 0;
        int maxRunCount = this.getMaxNumRuns(blocks);

        Long[] runBegin = new Long[maxRunCount];
        Long[] runEnd = new Long[maxRunCount];
        int runID = 0;

        // Filling heap with 8 values
        while (counter < 8 && !input.endOfFile()) {
            inserted++;
            heap.insert(input.getData());
            input.nextBlock();
            counter++;
        }

        // Last entry
        if (counter < 8) {
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

                inserted++;
                heap.replacementSelectionCycle(input.getData(), output);
                input.nextBlock();
                System.out.println(input.filePointer());
                System.out.println(input.endOfFile());
            }

            // Reactivating heap if needed, runEndDest run position is here
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
                // Current position run runEndDest == InputBlocks read - Blocks
                // in heap
                runEnd[runID] = input.filePointer() - 73728;

                // Incrementing runID
                runID++;
            }

            // Last statement not covered by while
            if (input.endOfFile()) {
                inserted++;
                heap.replacementSelectionCycle(input.getData(), output);

                int heapRemain = heap.heapSize();
                // Wiping remaining heap elements
                while (heap.heapSize() != 0) {
                    heap.replacementSelection(null, output);
                }
                // Case for runBegin
                if (runID == 0) {
                    runBegin[0] = 0L;
                }
                else {
                    runBegin[runID] = runEnd[runID - 1];
                }

                // runEnd only has 1 case
                // Current position run runEndDest == InputBlocks read - Blocks
                // in heap
                // that are not flushed
                runEnd[runID] = input.filePointer() - ((8 - heapRemain) << 13);

                // Incrementing runID
                runID++;

            }
        }

        // Recovering old heap elements not wiped.
        heap.reactivateHeap();
        if (heap.heapSize() > 0) {
            while (heap.heapSize() > 0) {
                heap.replacementSelection(null, output);
            }

            // Registers run data
            if (runID == 0) {
                runBegin[0] = 0L;
            }
            else {
                runBegin[runID] = runEnd[runID - 1];
            }

            runEnd[runID] = input.filePointer();
        }

        runBeginDest.wrap(runBegin);
        runEndDest.wrap(runEnd);
        output.flush();
        blocks.close();
    }


    /**
     * Copies the file within inputBuff into outputBuff.
     * 
     * @param inputBuff
     *            InputBuffer whose file is to be copied over
     * @param outputBuff
     *            OutputBuffer whose file is to be copied to
     * @throws IOException
     * 
     */
    private void printToFile(InputBuffer inputBuff, OutputBuffer outputBuff)
        throws IOException {
        input.rewind();
        int counter = 0;

        outputBuff.close();

        // clearFile(outputFileName);

        outputBuff.changeFile(new RandomAccessFile(outputFileName, "rw"));
        while (!input.endOfFile()) {

            System.out.print(input.nextLong(8) + " " + input.nextDouble(8)
                + " ");
            output.setData(input.getData());
            output.flush();
            input.nextBlock();
            counter++;

            if (counter % 5 == 0) {
                System.out.print("\n");
            }
        }
        // Last block not caught in loop
        System.out.println(input.nextLong(8) + " " + input.nextDouble(8) + " ");
        output.setData(input.getData());

        output.setData(input.getData());
        output.flush();

    }


    /**
     * Removes all temporary files and closes the file stream buffers
     * 
     * @throws IOException
     */
    private void close() throws IOException {
        input.close();
        output.close();

        this.cleanUp();
    }


    /**
     * Removes all temporary files
     * 
     * @throws IOException
     */
    private void cleanUp() throws IOException {
        File temp = new File(runfile1);

        if (!temp.delete()) {
            throw new IOException();
        }
        // temp = new File(runfile2);
        // temp.delete();
    }


    /**
     * Grabs the maximum number of runs for the selected file
     * 
     * @param blocks
     *            File to check maximum number of runs for
     * @throws IOException
     */
    private int getMaxNumRuns(RandomAccessFile blocks) throws IOException {
        // Creating arrays to keep track of run positions
        // Converts bytes to blocks, then bytes to runs of size 8.
        // There is only 1 run if length of arrays are <= 1
        // runID tracks current position in runID

        int blockCount = ((int)(blocks.length() >> 13));
        int maxRunCount = blockCount >> 3;
        int mask = 0x7;

        // Catch remainder, if exists, we need 1 extra run
        if ((blockCount & mask) != 0) {
            maxRunCount++;
        }

        // For when only run of size 1 exists, not caught by above mask
        else if (maxRunCount == 0) {
            maxRunCount++;
        }

        return maxRunCount;

    }


    /**
     * Clears the contents of the selected file
     * 
     * @param filename
     *            File to clear.
     * @throws IOException
     */
    private static void clearFile(String filename) throws IOException {
        FileWriter fwOb = new FileWriter(filename, false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }

}
