import java.io.File;
import java.io.IOException;

public class ValidationTest extends student.TestCase {

    private final String GENFILE = "Temp";

    // these are the tests
    /*
     * public void testCompare0() throws IOException {
     * Validation v = new Validation("testFile", "testFilesorted");
     * assertFalse(v.compare());
     * }
     * 
     * public void testCompare1() throws IOException {
     * Validation v = new Validation("testFile", "testFile");
     * assertTrue(v.compare());
     * }
     * 
     * public void testCompare2() throws IOException {
     * Validation v = new Validation("testFile1", "valTestFile1");
     * //comment out since the two files already exists.
     * //v.genFile(8, 4, "testFile1", "valTestFile1");
     * assertFalse(v.compare());
     * }
     */

    public void testValidation() throws IOException {
        Genfile_proj3.main(new String[] { GENFILE, Integer.toString(11) });
        Validation k = new Validation(GENFILE + ".bin", GENFILE + "sorted.bin");
        assertFalse(k.compare());
        Validation t = new Validation(GENFILE + "sorted.bin", GENFILE
            + "sorted.bin");
        assertTrue(t.compare());
        Validation s = new Validation(GENFILE + "reverseSorted.bin", GENFILE
            + "reverseSorted.bin");
        assertTrue(s.compare());

        this.deleteTemp();
    }


    /**
     * Deletes all generated files used for testing
     * 
     * @throws IOException
     */
    private void deleteTemp() throws IOException {
        File file = new File(GENFILE + ".bin");
        this.deleteFile(file);
        file = new File(GENFILE + "reversesorted.bin");
        this.deleteFile(file);
        file = new File(GENFILE + "sorted.bin");
        this.deleteFile(file);
        file = new File(GENFILE + "copysorted.bin");
        this.deleteFile(file);
    }


    /**
     * Attempts to delete a file, IOException if file has not been deleted
     * if it exists.
     * 
     * @param file
     *            File to delete
     * @throws IOException
     */
    private void deleteFile(File file) throws IOException {
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException();
            }
        }
    }
}
