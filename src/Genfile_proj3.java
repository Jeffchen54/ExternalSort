// WARNING: This program uses the Assertion class. When it is run,
// assertions must be turned on. For example, under Linux, use:
// java -ea Genfile

/** Generate a data file. The size is a multiple of 8192 bytes.
    Each record is one long and one double.
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
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        long ID;
        double key;
        assert (args.length == 2) :
            "\nUsage: Genfile <filename> <size>" +
            "\nOptions \nSize is measured in blocks of 8192 bytes";

        int filesize = Integer.parseInt(args[1]); // Size of file in blocks
        DataOutputStream file = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(args[0])));

        // create another two file that store sorted data and reverse sorted data
        DataOutputStream Sfile = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(args[0] + "sorted")));
        DataOutputStream RSfile = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(args[0] + "reverseSorted")));
        // create a list for data storage, we will use later for sorting
        ArrayList<Pair> list = new ArrayList<Pair>();


        for (int i=0; i<filesize; i++) {
            for (int j=0; j<NumRecs; j++) {
                ID = (long)(randLong());
                file.writeLong(ID);
                key = (double)(randDouble());
                file.writeDouble(key);

                //add each pair to the list
                list.add(new Pair(ID, key));

                // print out the first key of each block
                if(j == 0) {
                    System.out.println(key);
                }


            }
        }
        
//        // print out the whole data
//        for (int h = 0; h < list.size(); h++) {
//            if (h%512 == 0) {
//                System.out.println("-----------------this is the first item--------------------");
//            }
//            System.out.println(list.get(h).getId() + "   " + list.get(h).getKey());
//        }
        file.flush();
        file.close();



        System.out.println("-------------------------------------------------");
        System.out.println("----------------------sorted---------------------");
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////this is for sorted function/////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////

        // sort for the list
        for (int i = 0; i < list.size() - 512; i+= 512) {
            for (int j = i + 512; j < list.size() - 511; j+= 512) {
                if (list.get(i).getKey() > list.get(j).getKey()) {

                    ArrayList<Pair> temp = new ArrayList<Pair>();
                    for (int k = i; k < i + 512; k++) {
                        temp.add(list.get(k));
                    }
                    for (int a = 0; a < 512; a++) {
                        list.set(i + a, list.get(j + a));
                        list.set(j + a, temp.get(a));
                    }
                }
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// this is for sorted file////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////

        // print out first item of each block
        for (int i = 0; i < list.size(); i+=512) {
            System.out.println(list.get(i).getKey());
        }

        // write the sorted data back to each file
        for (int i=0; i< list.size(); i++) {
            Sfile.writeLong(list.get(i).getId());
            Sfile.writeDouble(list.get(i).getKey());

//            // print out the whole sorted data
//            if (i%512 == 0) {
//                System.out.println("-----------------this is the first item--------------------");
//            }
//            System.out.println(list.get(i).getId() + "   " + list.get(i).getKey());
        }
        Sfile.flush();
        Sfile.close();


        System.out.println("------------------reverse sorted-----------------");
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////this is for reverse sorted file/////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////

        // print out first item of each block
        for (int i = list.size() - 512; i > -1; i-=512) {
            System.out.println(list.get(i).getKey());
        }

        // write the sorted data back to each file reversely
        for (int i = list.size() - 512; i > -1; i-= 512) {
            for (int a = 0; a < 512; a++) {
                RSfile.writeLong(list.get(i + a).getId());
                RSfile.writeDouble(list.get(i + a).getKey());
                
//                //print out the first item
//                if (a == 0) {
//                    System.out.println("-----------------this is the first item--------------------");
//                }
//                // print out the whole reversely sorted data
//                System.out.println(list.get(i + a).getId() + "   " + list.get(i + a).getKey());
            }
        }
        RSfile.flush();
        RSfile.close();
    }
    

}


