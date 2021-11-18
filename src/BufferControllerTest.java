import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BufferControllerTest extends student.TestCase {

    private BufferController bc;
    private final String OUTPUT = "sampleInput16.bin";

    public void setUp() throws IOException {
        bc = new BufferController(OUTPUT);
    }


    public void testReplacementSelection()
        throws FileNotFoundException,
        IOException {

        // Testing a single run file of 16 blocks
        bc.replacementSelection();

        InputBuffer input = new InputBuffer(new RandomAccessFile(
            "JeffChenRunUno.bin", "r"));
        input.next(8);

        Double prev = input.nextDouble(8);
        input.rewind();

        while (!input.endOfFile()) {
            for (int i = 0; i < 206; i++) {
                assertTrue(prev.compareTo(this.getKey(input)) <= 0);

            }
            input.nextBlock();
        }

        // Testing a double run file
        
        //

        input.close();
    }


    public double getKey(InputBuffer input) {
        input.mark();
        input.next(8);
        double toReturn = input.nextDouble(8);
        input.reset();
        return toReturn;
    }
}
