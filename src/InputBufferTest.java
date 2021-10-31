import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
 * Tests the InputBuffer
 * 
 * @author chenj (chenjeff4840)
 * @version 10.30.2021
 */
public class InputBufferTest extends TestCase {

    // Fields ---------------------------------------------------------------
    private InputBuffer buffer;

    // Setup ------------------------------------------------------------
    /**
     * Creates buffer with size 8192 bytes and Canvas provided input file
     */
    @Test
    public void setUp() throws IOException {
        RandomAccessFile file = new RandomAccessFile("sampleInput16.bin", "r");
        buffer = new InputBuffer(file);
    }


    // Tests ------------------------------------------------------------
    /**
     * Tests nextBlock(), nextLong(), nextDouble() in real world
     * applications with a sample file
     * 
     * @throws IOException
     */
    public void testNextSamples() throws IOException {
        // Reading every key and value in sample output file
        assertEquals(5859826799363951096L, buffer.nextLong(8));
        assertEquals(7.25837957933813E-309, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(872093003042532807L, buffer.nextLong(8));
        assertEquals(2.846974648265778E-271, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(4746048651426934305L, buffer.nextLong(8));
        assertEquals(8.021302838493087E-236, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(2465224465483701295L, buffer.nextLong(8));
        assertEquals(1.979063847945134E-200, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(1050792465528211139L, buffer.nextLong(8));
        assertEquals(1.9121111284579667E-165, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(6050394105966916791L, buffer.nextLong(8));
        assertEquals(2.2317027604113507E-127, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(1026023591337815624L, buffer.nextLong(8));
        assertEquals(1.5574815733570753E-91, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(3727109532527581177L, buffer.nextLong(8));
        assertEquals(7.578200413844949E-58, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(1666373987716394526L, buffer.nextLong(8));
        assertEquals(1.948647168795557E-21, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(2109762501594140130L, buffer.nextLong(8));
        assertEquals(4.7295568637570205E12, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(704373661941503400L, buffer.nextLong(8));
        assertEquals(2.8332973775294907E48, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(168353935316054591L, buffer.nextLong(8));
        assertEquals(7.423511124391644E81, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(1813093945163867404L, buffer.nextLong(8));
        assertEquals(1.123748335113702E114, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(6011240381167188375L, buffer.nextLong(8));
        assertEquals(5.443479801473815E147, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(8334502261472908423L, buffer.nextLong(8));
        assertEquals(6.228961731759273E184, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();
        assertEquals(4290883147614596L, buffer.nextLong(8));
        assertEquals(1.206088797278413E221, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();

        // At the end of the file now
        assertTrue(buffer.endOfFile());

    }


    /**
     * Playing with a tiny bin with total bytes < 8192
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testTiny() throws FileNotFoundException, IOException {
        // Initializing ---------------------------------------------------
        InputBuffer buff = new InputBuffer(new RandomAccessFile("tiny.bin",
            "r"));

        // Taking byte[] info
        byte[] data = buff.getData();

        // Test confirms byte[] start with an array of 0s (NULL in ASCII).
        // Reusing an old array to be filled with a not full block leaves old
        // data from the previous block in the reused array. This can be
        // remedied
        // with a clearing function; however, that is beyond the scope of this
        // project since all blocks will always be 8192 byte size.
        assertEquals(0, data[304]);
        assertFalse(0 == data[303]);

        buffer.changeFile(new RandomAccessFile("tiny.bin", "r"));
        assertEquals(data[303], buffer.getData()[303]);
        assertFalse(0 == buffer.getData()[304]);
    }


    /**
     * Moves the buffer to the beginning of the next block in the file where a
     * block is of size data.length
     * 
     * @throws IOException
     */
    public void testNextBlock() throws IOException {
        // Normal nextBlock()
        buffer.nextBlock();
        assertEquals(872093003042532807L, buffer.nextLong(8));

        // nextBlock at EOF -------------------------------------------
        buffer.seek(122880);

        // Verify
        assertEquals(4290883147614596L, buffer.nextLong(8));

        // Calling nextBlock to go through EOF -----------------------
        buffer.nextBlock();

        // Verify that buffer is still the current position
        assertEquals(1.206088797278413E221, buffer.nextDouble(8), 0.1);

        // nextBlock while at middle of 2 existing blocks ------------
        buffer.seek(4096);
        buffer.nextBlock();
        assertEquals(4746048651426934305L, buffer.nextLong(8));
        assertEquals(8.021302838493087E-236, buffer.nextDouble(8), 0.1);
        buffer.nextBlock();

        // Return to previous position then next block ----------------
        buffer.seek(0);
        buffer.nextBlock();
        assertEquals(872093003042532807L, buffer.nextLong(8));
        assertEquals(2.846974648265778E-271, buffer.nextDouble(8), 0.1);

        // Negative seek then nextBlock --------------------------------
        buffer.seek(-12312);
        buffer.nextBlock();
        assertEquals(4746048651426934305L, buffer.nextLong(8));
        assertEquals(8.021302838493087E-236, buffer.nextDouble(8), 0.1);

        // Seeking to past EOF and then calling nextBlock ---------------
        buffer.seek(131073);
        buffer.nextBlock();
        assertEquals(2465224465483701295L, buffer.nextLong(8));
        assertEquals(1.979063847945134E-200, buffer.nextDouble(8), 0.1);
    }


    /**
     * tests seek
     */
    public void testSeek() throws IOException {
        // Negative seek ---------------------------------------------
        buffer.seek(-1);

        // Seek past EOF ---------------------------------------------
        // Seek to key in first block, verify location
        buffer.seek(8);
        assertEquals(7.25837957933813E-309, buffer.nextDouble(8), 0.1);

        // Seek past EOF, verify file location has not changed
        buffer.seek(546546545646L);
        assertEquals(7.25837957933813E-309, buffer.nextDouble(8), 0.1);

        // Seeking to the beginning of the last block -----------------
        buffer.seek(122880);
        assertEquals(4290883147614596L, buffer.nextLong(8));

        // Seeking back to the beginning of the file
        buffer.seek(0);
        assertEquals(5859826799363951096L, buffer.nextLong(8));

        // Seek last byte in file ---------------------------------------
        buffer.seek(131071);
        assertTrue(buffer.next(8192));

    }


    /**
     * Tests next()
     */
    public void testNext() {
        // Skipping to the key -----------------------------------------------
        assertTrue(buffer.next(8));

        // Verifying
        assertEquals(7.25837957933813E-309, buffer.nextDouble(8), 0.1);

        // Skipping to the end of the buffer
        assertTrue(buffer.next(8192 - 16));

        // Going out of bounds
        assertFalse(buffer.next(1));
    }


    /**
     * test getData()
     */
    public void testGetData() {
        byte[] copy = buffer.getData();
        copy[0] = 91;

        assertEquals(copy[0], buffer.getData()[0]);
        copy[1] = 92;
        assertEquals(copy[0], buffer.getData()[0]);
    }


    /**
     * Changes this.file to file
     * 
     * @param file
     *            File to change to
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void testChangeFile() throws FileNotFoundException, IOException {
        // Saving first 8 bytes of input into a double
        double a = buffer.nextLong(8);

        // Switching the file
        buffer.changeFile(new RandomAccessFile("64Blocks_Barnette.bin", "r"));

        // Saving the first 8 bytes of this other file
        double b = buffer.nextLong(8);

        // Confirming this is indeed the beginning of the other file
        buffer.rewind();
        double b2 = buffer.nextLong(8);
        assertEquals(b, b2, 0.1);

        // Checking if a different file was indeed selected
        assertFalse(a == b);

    }


    /**
     * test rewind
     */
    public void testRewind() {
        // Saving initial value
        long original = buffer.nextLong(8);

        // Jumping 4000 bytes in buffer and saving value as long
        buffer.next(4000);
        long jump = buffer.nextLong(8);

        // Rewinding and verifying
        buffer.rewind();
        long rewind = buffer.nextLong(8);

        assertEquals(original, rewind);
        assertFalse(jump == rewind);
    }


    /**
     * test rewind
     */
    public void testResetMark() {
        // Saving initial values
        long begin = buffer.nextLong(8);
        buffer.mark();
        long original = buffer.nextLong(8);

        // Jumping 4000 bytes in buffer and saving value as long
        buffer.next(4000);
        long jump = buffer.nextLong(8);

        // Resetting and verifying
        buffer.reset();
        long reset = buffer.nextLong(8);

        assertEquals(original, reset);
        assertFalse(jump == reset);

        // Rewinding and making sure mark is gone
        buffer.rewind();
        assertEquals(begin, buffer.nextLong(8));
        assertFalse(begin == reset);
    }
    
    /**
     * test endOfFile()
     * @throws IOException 
     */
    public void testEndOfFile() throws IOException {
        // Beginning
        assertFalse(buffer.endOfFile());
        
        // Middle
        buffer.seek(70000);
        assertFalse(buffer.endOfFile());
        
        // End
        buffer.seek(122880);
        assertTrue(buffer.endOfFile());
        
        buffer.seek(122879);
        assertFalse(buffer.endOfFile());
        
        // Resetting
        buffer.seek(0);
        
        // Negative "seek logic should prevent this"
        buffer.seek(-121);
        assertFalse(buffer.endOfFile());
        
        // Past "Only occurs for specific seek positions which cannot occur with 
        // 8192 byte block size requirement for project"
        buffer.seek(130000);
        assertTrue(buffer.endOfFile());
        
        // Resetting
        buffer.seek(0);
        
        // Explicitly going way beyond EOF "seek prevents this"
        buffer.seek(8956565);
        assertFalse(buffer.endOfFile());
    }
}
