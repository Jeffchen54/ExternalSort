// WARNING: This program uses the Assertion class. When it is run,
// assertions must be turned on. For example, under Linux, use:
// java -ea Genfile

/**
 * Generate a data file. The size is a multiple of 8192 bytes.
 * Each record is one long and one double.
 */
import java.io.*;
import java.util.*;

public class Genfile_proj3 {

    static final int NumRecs = 512; // Because they are short ints

    /** Initialize the random variable */
    static private Random value = new Random(); // Hold the Random class object

    static long randLong() {
        return value.nextLong();
    }


    static double randDouble() {
        return value.nextDouble();
    }


    /**
     * size 1 means 1 block
     * 5 means 5 blocks
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        long ID;
        double key;
        assert (args.length == 2) : "\nUsage: Genfile <filename> <size>"
            + "\nOptions \nSize is measured in blocks of 8192 bytes";

        int filesize = Integer.parseInt(args[1]); // Size of file in blocks
        DataOutputStream file = new DataOutputStream(new BufferedOutputStream(
            new FileOutputStream(args[0] + ".bin")));

        // create another two file that store sorted data and reverse sorted
        // data
        DataOutputStream Sfile = new DataOutputStream(new BufferedOutputStream(
            new FileOutputStream(args[0] + "sorted.bin")));
        DataOutputStream RSfile = new DataOutputStream(new BufferedOutputStream(
            new FileOutputStream(args[0] + "reverseSorted.bin")));
        // create a list for data storage, we will use later for sorting
        ArrayList<Pair> list = new ArrayList<Pair>();

        for (int i = 0; i < filesize; i++) {
            for (int j = 0; j < NumRecs; j++) {
                ID = (long)(512 * i + j);
                Sfile.writeLong(ID);
                key = (double)(randDouble());
                Sfile.writeDouble(512 * i + j);

                // add each pair to the list
                list.add(new Pair(ID, key));

//                // print out the first key of each block
//                if (j == 0) {
//                    System.out.println(key);
//                }

            }
        }

// // print out the whole data
// for (int h = 0; h < list.size(); h++) {
// if (h%512 == 0) {
// System.out.println("-----------------this is the first
// item--------------------");
// }
// System.out.println(list.get(h).getId() + " " + list.get(h).getKey());
// }
        Sfile.flush();
        Sfile.close();

        System.out.println("-------------------------------------------------");
        System.out.println("----------------------sorted---------------------");
////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// this is for sorted file////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////// 
////////////////////////////////////////////////////////////////////////////////////////////////

//        // print out first item of each block
//        for (int i = 0; i < list.size(); i += 512) {
//            System.out.println(list.get(i).getKey());
//        }
        for (int i = 0; i < list.size()/2; i++) {
            Pair temp = list.get(i);
            list.set(i, list.get(list.size() - 1 - i));
            list.set(list.size() - 1, temp);
        }
        for (int i = 0; i < list.size(); i++) {
            file.writeLong(list.get(i).getId());
            file.writeDouble(list.get(i).getKey());
        }

// // print out the whole sorted data
// if (i%512 == 0) {
// System.out.println("-----------------this is the first
// item--------------------");
// }
// System.out.println(list.get(i).getId() + " " + list.get(i).getKey());
        
        file.flush();
        file.close();

        System.out.println("------------------reverse sorted-----------------");
////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// this is for reverse sorted
//////////////////////////////////////////////////////////////////////////////////////////////// file/////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////

        
        for (int i = 0; i < list.size()/2; i++) {
            Pair temp = list.get(i);
            list.set(i, list.get(list.size() - 1 - i));
            list.set(list.size() - 1, temp);
        }
        for (int i = 0; i < list.size(); i++) {
            RSfile.writeLong(list.get(i).getId());
            RSfile.writeDouble(list.get(i).getKey());
        }
        
        RSfile.flush();
        RSfile.close();
    }

}
