import java.io.IOException;

public class ValidationTest extends student.TestCase {

    // these are the tests 
    /*public void testCompare0() throws IOException {
        Validation v = new Validation("testFile", "testFilesorted");
        assertFalse(v.compare());
    }

    public void testCompare1() throws IOException {
        Validation v = new Validation("testFile", "testFile");
        assertTrue(v.compare());
    }

    public void testCompare2() throws IOException {
        Validation v = new Validation("testFile1", "valTestFile1");
        //comment out since the two files already exists.
        //v.genFile(8, 4, "testFile1", "valTestFile1");
        assertFalse(v.compare());
    }*/
    
    public void testValidation() throws IOException {
        Validation v = new Validation("BenTemp.bin", "BenSample11Bsorted.bin");
        assertTrue(v.compare());
        Validation k = new Validation("BenSample11B.bin", "BenSample11Bsorted.bin");
        assertFalse(k.compare());
        Validation t = new Validation("BenSample11Bsorted.bin", "BenSample11Bsorted.bin");
        assertTrue(t.compare());
        Validation s = new Validation("BenSample11BreverseSorted.bin", "BenSample11BreverseSorted.bin");
        assertTrue(s.compare());
    }
    
}
