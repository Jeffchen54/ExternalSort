import java.io.IOException;
import java.io.RandomAccessFile;

public class Validation {


    private double key; // the key of the error block
    private int num ;    // trace number of block that got error
    private String ourFile;
    private String answerFile;

    public Validation(String ourFile, String answerFile) {
        num = 1;
        this.ourFile = ourFile;
        this.answerFile = answerFile;
    }

    public boolean compare() throws IOException {
        RandomAccessFile result = new RandomAccessFile(ourFile, "r");
        InputBuffer rBuffer = new InputBuffer(result);

        RandomAccessFile validation = new RandomAccessFile(answerFile, "r");
        InputBuffer vBuffer = new InputBuffer(validation);

        if (equal(rBuffer, vBuffer)) {
            System.out.println("the output of your external sort is correct");
            return true;
        }
        else {
            System.out.println("you have error at the " + num + "th block, the key is " + key);
            return false;
        }
    }

    public boolean equal(InputBuffer rBuffer, InputBuffer vBuffer) throws IOException {
        SubBuffer rsBuffer = new SubBuffer();

        SubBuffer vsBuffer = new SubBuffer();

        while (!rBuffer.endOfFile() && !vBuffer.endOfFile()) {
            rsBuffer.setData(rBuffer.getData());
            vsBuffer.setData(vBuffer.getData());
            if (rsBuffer.compareTo(vsBuffer) != 0){
                key = 0;
                return false;
            }
            rBuffer.nextBlock();
            vBuffer.nextBlock();
            num += 1;
        }
        return true;
    }

}
