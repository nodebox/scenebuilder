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

    public void testParseValue() {
        FloatPort p = new FloatPort(new TestNode(), "float", Port.Direction.INPUT, 0f);
        assertEquals(123f, p.parseValue("123"));
        assertEquals(123f, p.parseValue("123.0"));
        assertEquals(123.45f, p.parseValue("123.45"));
        assertEquals(-239.7f, p.parseValue("-239.7"));
    }

    public void testValueAsString() {
        FloatPort p = new FloatPort(new TestNode(), "float", Port.Direction.INPUT, 0f);
        p.set(123f);
        assertEquals("123.0", p.getValueAsString());
        p.set(123.45f);
        assertEquals("123.45", p.getValueAsString());
        p.setValue(-239f);
        assertEquals("-239.0", p.getValueAsString());
    }

    public static class TestNode extends Node {
        @Override
        public void execute(Context context, float time) {
        }
    }
}
