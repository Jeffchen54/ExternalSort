import java.io.BufferedOutputStream; 
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

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
                key = rBuffer.nextDouble(8);  // this is not working
                return false;
            }
            rBuffer.nextBlock();
            vBuffer.nextBlock();
            num += 1;
        }
        return true;
    }
    
///////////////////////////////////////////////////////
////// helper method for test case/////////////////////
///////////////////////////////////////////////////////
    
    static private Random value = new Random();

    static long randLong() {
        return value.nextLong();
    }

    static double randDouble() {
        return value.nextDouble();
    }
    
    
    public void genFile(int size, int errorHappen, String resultFileName, 
        String validationFileName) throws IOException {
        
        long ID;
        double key;

        DataOutputStream file = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream("testFile1")));
        DataOutputStream vfile = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream("valTestFile1")));    
        ArrayList<Pair> list = new ArrayList<Pair>();
        System.out.println("------------this is our testFile--------------");
        for (int i=0; i < size; i++) {
            for (int j=0; j<512; j++) {
                ID = (long)(randLong());
                file.writeLong(ID);
                key = (double)(randDouble());
                file.writeDouble(key);
                if (i == errorHappen) {
                    list.add(new Pair(ID, 0.123456789));
                }
                else {
                    list.add(new Pair(ID, key));
                }
                // print out the first key of each block
                if(j == 0) {
                    System.out.println(key);
                }
            }
        }
        file.flush();
        file.close();
        
     // print out first item of each block
        System.out.println("------------this is validation testFile--------------");
        for (int i = 0; i < list.size(); i+=512) {
            System.out.println(list.get(i).getKey());
        }

        // write the sorted data back to each file
        for (int i=0; i< list.size(); i++) {
            vfile.writeLong(list.get(i).getId());
            vfile.writeDouble(list.get(i).getKey());

//            // print out the whole sorted data
//            if (i%512 == 0) {
//                System.out.println("-----------------this is the first item--------------------");
//            }
//            System.out.println(list.get(i).getId() + "   " + list.get(i).getKey());
        }
        vfile.flush();
        vfile.close();
    }

}
