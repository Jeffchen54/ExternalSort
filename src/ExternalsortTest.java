import java.io.FileNotFoundException;
import java.io.IOException;
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
 * Tests main method
 * 
 * @author chenj (chenjeff4840)
 * @version 11.1.2021
 */
public class ExternalsortTest extends TestCase {

    /**
     * Intentionally left empty
     */
    public void setUp() {
        // empty
    }


    /**
     * Tests invocation with or without an input file.
     */
    public void testInvocation() throws FileNotFoundException, IOException {
        Externalsort.main(new String[0]);
        assertFuzzyEquals(systemOut().getHistory(),
            "Invocation: java Externalsort <records-filename>\r\n");
        systemOut().clearHistory();
        Externalsort.main(new String[] { "sampleInput16.bin" });
        assertFuzzyEquals(systemOut().getHistory(),
            "5859826799363951096 7.25837957933813E-309" + " 872093003042532807 "
                + "2.846974648265778E-271 4746048651426934305 "
                + "8.021302838493087E-236 "
                + "2465224465483701295 1.979063847945134E-200 "
                + "1050792465528211139 " + "1.9121111284579667E-165\r\n"
                + "6050394105966916791 2.2317027604113507E-127 "
                + "1026023591337815624 1.5574815733570753E-91 "
                + "3727109532527581177 7.578200413844949E-58 "
                + "1666373987716394526 "
                + "1.948647168795557E-21 2109762501594140130 "
                + "4.7295568637570205E12\r\n"
                + "704373661941503400 2.8332973775294907E48 "
                + "168353935316054591 7.423511124391644E81 "
                + "1813093945163867404 "
                + "1.123748335113702E114 6011240381167188375 "
                + "5.443479801473815E147 "
                + "8334502261472908423 6.228961731759273E184\r\n"
                + "4290883147614596 1.206088797278413E221 \r\n");
    }
}
