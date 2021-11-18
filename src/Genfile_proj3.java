import java.io.*;
import java.util.*;

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
// letter of this restriction

// Java Doc ------------------------------------------------------------------
/**
 * Generate a data file. The size is a multiple of 8192 bytes.
 * Each record is one long and one double. *
 * 
 * @author Ben Chen
 * @version 11.17.2021
 */
public class Genfile_proj3 {

    static final int NumRecs = 512; // Because they are short ints

    /**
     * Generates sorted, unsorted, and reverse sorted .bin files.
     * 
     * @param args
     *            args[0] is file name w/o .bin and args[1] is number of blocks
     *            ot be generated
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
                key = (double)(512 * i + j);
                Sfile.writeDouble(key);

                // add each pair to the list
                list.add(new Pair(ID, key));
            }
        }

        Sfile.flush();
        Sfile.close();

        for (int i = 0; i < list.size() / 2; i++) {
            Pair temp = list.get(i);
            list.set(i, list.get(list.size() - 1 - i));
            list.set(list.size() - 1 - i, temp);
        }
        for (int i = 0; i < list.size(); i++) {
            RSfile.writeLong(list.get(i).getId());
            RSfile.writeDouble(list.get(i).getKey());
        }

        RSfile.flush();
        RSfile.close();

        for (int i = 0; i < list.size() / 2; i++) {
            if (i % 2 == 0) {
                Pair temp = list.get(i);
                list.set(i, list.get(list.size() - 1 - i));
                list.set(list.size() - 1 - i, temp);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            file.writeLong(list.get(i).getId());
            file.writeDouble(list.get(i).getKey());
        }

        file.flush();
        file.close();
    }

}
