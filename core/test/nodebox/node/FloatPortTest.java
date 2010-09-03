package nodebox.node;

import junit.framework.TestCase;

public class FloatPortTest extends TestCase {
    /**
     * Test if we can also set integers.
     */
    public void testSetValue() {
        FloatPort p = new FloatPort(null, "float", Port.Direction.INPUT, 0f);
        p.setValue(12);
        assertEquals(12f, p.getValue());
    }
}
