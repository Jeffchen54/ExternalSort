import java.io.IOException;
import java.io.RandomAccessFile;

public class Validation {


    private double key; // the key of the error block
    private int num ;    // trace number of block that got error

    public Validation() {
        key = 0;
        num = 0;
    }

    public static void main(String[] args) throws IOException {
        RandomAccessFile result = new RandomAccessFile(args[0], "r");
        InputBuffer rBuffer = new InputBuffer(result);

        RandomAccessFile validation = new RandomAccessFile(args[1], "r");
        InputBuffer vBuffer = new InputBuffer(validation);

        if (equal(rBuffer, vBuffer)) {
            System.out.println("the output of your external sort is correct");
        }
        else {
            System.out.println("you have error at the " + 0 + "th block, the key is " + 0);
        }
    }

    public static boolean equal(InputBuffer rBuffer, InputBuffer vBuffer) throws IOException {
        SubBuffer rsBuffer = new SubBuffer();

        SubBuffer vsBuffer = new SubBuffer();

        while (!rBuffer.endOfFile() && !vBuffer.endOfFile()) {
            rsBuffer.setData(rBuffer.getData());
            vsBuffer.setData(vBuffer.getData());
            if (rsBuffer.compareTo(vsBuffer) != 0){
                return false;
            }
            rBuffer.nextBlock();
            vBuffer.nextBlock();
        }
        return true;
    }
    
    
    public double getNum() {
        return num;
    }
    
    
    
}
