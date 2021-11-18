import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;

public class BufferControllerTest extends student.TestCase {

    private BufferController bc;
    private final String OUTPUT = "sampleInput16.bin";
    private final String OUTPUT2 = "Temp10BreverseSorted.bin";
    private final String GENFILE = "Temp";
    private final String RUN1 = "JeffChenRunUno.bin";
    private final String SOFILE = "StandardOutputTemp.txt";
    private Validation valid;

    public void setUp() throws IOException {
        bc = new BufferController(OUTPUT);
    }


    public void testReplacementSelection()
        throws FileNotFoundException,
        IOException {
        ////////////////////////////////////////////////////////////////
        // Testing a single run file of 16 blocks
        bc.replacementSelection();

        InputBuffer input = new InputBuffer(new RandomAccessFile(RUN1, "r"));
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
        bc = new BufferController(OUTPUT2);
        bc.replacementSelection();

        this.saveRecords(RUN1, SOFILE);

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
        Genfile_proj3.main(new String[] { GENFILE, Integer.toString(400) });

        bc = new BufferController(GENFILE + "reversesorted.bin");
        bc.replacementSelection();
        this.saveRecords(RUN1, SOFILE);
        
        
        // Checking if runs are correct
        // For a reverse sorted file, they should always have runs of 
        // size 65536 or 8 blocks
        for(int i = 0; i < 50; i++) {
            assertEquals(i * 65536, bc.getRunBegin()[i], 0.1);
            assertEquals(65536 + (i * 65536), bc.getRunEnd()[i], 0.1);
        }
        
        this.closeForRS(input);
        this.deleteTemp();
    }


    private void printRecords(String file)
        throws FileNotFoundException,
        IOException {
        System.out.println(this.storeRecords(file));
    }


    private String storeRecords(String file)
        throws FileNotFoundException,
        IOException {
        StringBuilder build = new StringBuilder();
        InputBuffer input = new InputBuffer(new RandomAccessFile(file, "r"));

        while (!input.endOfFile()) {
            for (int i = 0; i < 512; i++) {
                build.append(this.getKey(input) + " ");
                input.next(16);
            }
            build.append(
                "\n----------------------------------------------------"
                    + "-------------------------------------------------"
                    + "------------"
                    + "--------------------------------------------------"
                    + "----------"
                    + "---------------------------------------------------"
                    + "--------\n");
            input.nextBlock();
        }

        // Covers last missed block
        for (int i = 0; i < 512; i++) {
            build.append(this.getKey(input) + " ");
            input.next(16);
        }
        build.append("\n----------------------------------------------------"
            + "-------------------------------------------------------------"
            + "------------------------------------------------------------"
            + "-----------------------------------------------------------\n");
        input.close();

        return build.toString();
    }


    private void saveRecords(String file, String dest) throws IOException {
        String records = this.storeRecords(file);
        RandomAccessFile store = new RandomAccessFile(dest, "rw");
        store.write(records.getBytes());
        store.close();
    }


    /**
     * Checks if a run file contains already sorted results,
     * only for inputs of size 8 or less
     * 
     * @return true if files sorted correctly, assertFalse if not.
     * @throws IOException
     */
    private boolean compareSingleRunRS(int size) throws IOException {
        // Testing a 4 block random file
        Genfile_proj3.main(new String[] { GENFILE, Integer.toString(size) });

        // Random
        bc = new BufferController(GENFILE + ".bin");
        bc.replacementSelection();
        bc.close();
        valid = new Validation(RUN1, GENFILE + "sorted.bin");
        assertTrue(valid.compare());

        // Reverse sorted
        bc = new BufferController(GENFILE + "reversesorted.bin");
        bc.replacementSelection();
        bc.close();
        valid = new Validation(RUN1, GENFILE + "sorted.bin");
        assertTrue(valid.compare());

        // Sorted
        this.copyFile(new File(GENFILE + "sorted.bin"), new File(GENFILE
            + "copysorted.bin"));
        bc = new BufferController(GENFILE + "sorted.bin");
        bc.replacementSelection();
        bc.close();
        valid = new Validation(RUN1, GENFILE + "copysorted.bin");
        assertTrue(valid.compare());

        this.closeForRS(null);
        this.deleteTemp();

        return true;
    }


    private void deleteTemp() {
        File file = new File(GENFILE + ".bin");
        file.delete();
        file = new File(GENFILE + "reversesorted.bin");
        file.delete();
        file = new File(GENFILE + "sorted.bin");
        file.delete();
        file = new File(GENFILE + "copysorted.bin");
        file.delete();
    }


    private void closeForRS(InputBuffer input) throws IOException {
        bc.close();
        File file = new File(RUN1);
        if (input != null) {
            input.close();
        }
        file.delete();
    }


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
