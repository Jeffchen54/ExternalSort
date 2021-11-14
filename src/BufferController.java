import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BufferController {

    private InputBuffer in;
    private MainBuffer main;
    private OutputBuffer out;

    private long[] startPos;
    private long[] endPos;
    private long start;
    private long end;
    
    private int id;
    
    private double key;
    // out put to a single run file 
    public BufferController(String infile, String outFile) 
        throws FileNotFoundException, IOException {
        RandomAccessFile inputFile = new RandomAccessFile(infile, "r");
        in = new InputBuffer(inputFile);
        RandomAccessFile outputFile = new RandomAccessFile(outFile, "rw");
        out = new OutputBuffer(outputFile);


        main = new MainBuffer();

        startPos = new long[getMaxRun(inputFile)];
        endPos = new long[getMaxRun(inputFile)];
        start = 0;
        end = 0;
        id = 0;
    }

    public void run() throws IOException {
        // case1: input file has less than 8 blocks
        if (!buildUp()) {
            clearMain();
        }
        // case2: input file has at least 8 blocks
        else {
            while(!in.endOfFile()) {
                if(main.heapSize() == 0) {
                    main.reactivateHeap();           
                    markPos();
                }
                flow();
                in.nextBlock();
            }
            // flow one more time for the last block
            flow();
            clearMain();
            markPos();
            main.reactivateHeap();
            clearMain();
            markPos();
        }
        for(int i = 0; i < startPos.length; i++) {
            System.out.print(startPos[i] + "   ");
            System.out.println(endPos[i]);
        }
        closeFile();
        
        
        
    }
    
    // haven't done with it
    /**
     * this is will merge sort once for the output file
     * another set of startPos and endPos track new   have origional
     * @throws FileNotFoundException 
     */
    public void merge(RandomAccessFile input, RandomAccessFile finalFile) 
        throws FileNotFoundException {
        long[] startCp = startPos;
        long[] endCp = endPos;
        for (int i = 0; i <= id; i++) {
            long begin = startPos[i];
        }
    }

    /**
     * flow the block from in -> main -> out
     * @throws IOException
     */
    private void flow() throws IOException {   
        main.replacementSelection(in.getData(), out);
        out.flush();
        end += 8192;
    }
    
    /**
     * clear the main heap
     */
    private void clearMain() throws IOException {
        while (main.heapSize() > 0) {
            main.replacementSelection(null, out);
            out.flush();
            end += 8192;
        }
    }

    /** this add blocks of data from inputBuffer to main
     * make the main buffer has 8 blocks
     * if there is no more things to build up, return false
     * if there is more blocks in the file, return true
     * 
     * @throws IOException
     */
    private boolean buildUp() throws IOException {
        while(main.heapSize() < 8) {
            if(in.endOfFile()) {
                main.insert(in.getData());
                return false;
            }
            main.insert(in.getData(), id);
            in.nextBlock();
        }
        return true;
    }

    /**
     * this method close all the files
     * @throws IOException 
     */
    private void closeFile() throws IOException {
        out.close();
        in.close();
    }
    
    
    /**
     *  take down the starting position and end position
     *  for the current block
     *  
     *  call this function when a new run start
     */
    private void markPos() {
        startPos[id] = start;
        endPos[id] = end;
        start = end;
        id++;
    }
    
    

    /**
     * to get the maximum run of a input file, which will be the size 
     * of the startPos and endPos array
     * @param input the input file
     * @return the number of the maximum run
     * @throws IOException
     */
    private int getMaxRun(RandomAccessFile input) throws IOException {
        int blockCount = ((int)(input.length() >> 13));
        int maxRunCount = blockCount >> 3;
        int mask = 0x3;

        // Catch remainder, if exists, we need 1 extra run
        if ((blockCount & mask) != 0) {
            maxRunCount++;
        }

        // For when only run of size 1 exists, not caught by above mask
        else if (maxRunCount == 0) {
            maxRunCount++;  // store the max run
        }
        return maxRunCount;
    }  

}
