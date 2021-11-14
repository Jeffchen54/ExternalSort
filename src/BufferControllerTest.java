import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BufferControllerTest extends student.TestCase {

    private BufferController bc;
    
    public void setUp() throws IOException {
        bc = new BufferController("SampleSort1250B.bin", "BCTEST.bin");
    }
    
    
    public void testFlow() throws IOException {
        //bc.run();
        getKey("SampleSort1250B.bin");
        System.out.println("this is result after run---------");
        getKey("BCTEST.bin");
        System.out.println("end of the doc");
    }
    
    
    
    public void getKey(String fileName) throws IOException {
        RandomAccessFile inputFile = new RandomAccessFile(fileName, "r");
        InputBuffer in = new InputBuffer(inputFile);
        while(!in.endOfFile()) {
            in.next(8);
            double key = in.nextDouble(8);
            System.out.println(key);
            in.nextBlock();
        }
        in.next(8);
        double key = in.nextDouble(8);
        System.out.println(key);
        
    }
}
