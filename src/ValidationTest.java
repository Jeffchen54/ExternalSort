import java.io.IOException;

public class ValidationTest extends student.TestCase {

    
    Validation v = new Validation("testFile", "testFilesorted");
    
    
    public void testCompare() throws IOException {
        assertTrue(v.compare());
    }
}
