package nodebox.util;

import junit.framework.TestCase;

import static nodebox.util.Strings.humanizeName;

public class StringsTest extends TestCase {

    public void testHumanizeName() {
        assertEquals("lowercase", humanizeName("lowercase"));
        assertEquals("Class", humanizeName("Class"));
        assertEquals("My Class", humanizeName("MyClass"));
        assertEquals("HTML", humanizeName("HTML"));
        assertEquals("PDF Loader", humanizeName("PDFLoader"));
        assertEquals("A String", humanizeName("AString"));
        assertEquals("Simple XML Parser", humanizeName("SimpleXMLParser"));
        assertEquals("GL 11 Version", humanizeName("GL11Version"));
    }

}
