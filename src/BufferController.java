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
    // private String runfile2 = "JeffChenRunDos.bin";

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
        this.replacementSelection();

        this.close();
        // this.mergeSort();
        this.merge(new RandomAccessFile(runfile1, "rw"), new RandomAccessFile(
            outputFileName, "rw"), 0);
        this.close();
        this.printBlockRecords(inputFileName);
        this.close();

    }

    // Helpers -----------------------------------------------------------


    /**
     * this the merge for at most 8 runs
     * 
     * @param runFrom
     *            run file containing previous runs
     * @param runInto
     *            run file to put current runs into
     * @param pass
     *            What pass the merge is on, begins with 0
     * @throws IOException
     */
    public void merge(
        RandomAccessFile runFrom,
        RandomAccessFile runInto,
        int pass)
        throws IOException {
        if (pass % 2 == 0) {
            input.changeFile(runFrom);
            output.changeFile(runInto);
        }
        else if (pass % 2 == 1) {
            input.changeFile(runInto);
            output.changeFile(runFrom);
        }
        int current = 0;
        Long[] runBegin = runBeginDest.get();
        Long[] runEnd = runEndDest.get(); // pay attention to the type since
                                          // it's using runBegin.length
        // to build up the heap first time
        current = buildupMerge(current);

        // this while end when every run in the file sorted
        while (!allRunEmpty(runBegin, runEnd)) {
            // this while end when every eight run is sorted
            while (heap.heapSize() != 0) {
                int runNum = heap.mergeOnce(output);
                runBegin[runNum] = runBegin[runNum] + 8192;

                if (runBegin[runNum].compareTo(runEnd[runNum]) < 0) {
                    input.seek(runBegin[runNum]);
                    heap.insert(input.getData(), runNum);
                }
            }
            current = buildupMerge(current);
        }
        output.flush();
        runFrom.close();
        runInto.close();
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

        output.changeFile(new RandomAccessFile(runfile1, "rw"));
        int counter = 0;
        int maxRunCount = this.getMaxNumRuns(blocks);

        Long[] runBegin = new Long[maxRunCount];
        Long[] runEnd = new Long[maxRunCount];
        int runID = 0;

        // Filling heap with 8 values
        while (counter < 8 && !input.endOfFile()) {
            heap.insert(input.getData());
            input.nextBlock();
            counter++;
        }

        // Last entry
        if (counter < 8) {
            heap.insert(input.getData());
            input.nextBlock();
        }

        // Replacement selection - Processes all Input file items, heap size
        // may not be 0 after heap is finished
        while (heap.heapSize() != 0 && !input.endOfFile()) {
            // Calling replacement selection until either max run is reached or
            // no more input
            while (!input.endOfFile() && heap.heapSize() != 0) {

                heap.replacementSelectionCycle(input.getData(), output);
                input.nextBlock();
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
     * Removes all temporary files and closes the file stream buffers
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        input.close();
        output.close();

        // this.cleanUp();
    }


    /**
     * Returns the run begin dimensions. Used for debugging purposes after
     * replacement selection
     * 
     * @return Returns the run begins array
     */
    public Long[] getRunBegin() {
        return runBeginDest.get();
    }


    /**
     * Returns the run end dimensions. Used for debugging purposes after
     * replacement selection
     * 
     * @return Returns the run ends array
     */
    public Long[] getRunEnd() {
        return runEndDest.get();
    }


    /**
     * Builds a heap with up to 8 blocks starting with the current run
     * 
     * @param current
     *            Which run we are on. Begins with 0 for run 1.
     * @throws IOException
     */
    private int buildupMerge(int current) throws IOException {
        int currRun = current;
        while (heap.heapSize() < 8 && (runBeginDest.get().length > currRun
            && runBeginDest.get()[currRun] != null)) {
            input.seek(runBeginDest.get()[currRun]);
            heap.insert(input.getData(), currRun);
            currRun++;
        }
        return currRun;
    }


    /**
     * Checks if all runs have been exhausted.
     * 
     * @param begin
     *            Beginning position of the runs
     * @param end
     *            Ending position of the runs
     * @return True if all runs have been exhausted, false otherwise.
     */
    private boolean allRunEmpty(Long[] begin, Long[] end) {
        for (int i = 0; i < begin.length && begin[i] != null; i++) {
            if (begin[i].compareTo(end[i]) != 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * Copies the file within inputBuff into outputBuff.
     * 
     * @param inputBuff
     *            InputBuffer whose file is to be copied over
     * @param outputBuff
     *            OutputBuffer whose file is to be copied to
     * @throws FileNotFoundException
     * @throws IOException
     * 
     */
    private void printBlockRecords(String filename)
        throws FileNotFoundException,
        IOException {
        input.changeFile(new RandomAccessFile(filename, "r"));
        int counter = 0;

        while (!input.endOfFile()) {

            System.out.print(input.nextLong(8) + " ");
            System.out.print(input.nextDouble(8) + " ");

            input.nextBlock();
            counter++;

            if (counter % 5 == 0) {
                System.out.print("\n");
            }
        }
        System.out.print(input.nextLong(8) + " ");
        System.out.print(input.nextDouble(8) + " \n");

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
}
