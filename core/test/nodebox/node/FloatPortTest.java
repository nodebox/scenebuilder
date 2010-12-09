package nodebox.node;

import junit.framework.TestCase;

public class FloatPortTest extends TestCase {
    /**
     * Test if we can also set integers.
     */
    public void testSetValue() {
        FloatPort p = new FloatPort(new TestNode(), "float", Port.Direction.INPUT, 0f);
        p.setValue(12);
        assertEquals(12f, p.getValue());
    }

    public static class TestNode extends Node {
        @Override
        public void execute(Context context, float time) {
        }
    }
}
