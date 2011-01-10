package nodebox.node;

import junit.framework.TestCase;
import nodebox.node.TestNodes.TestNode;

public class IntPortTest extends TestCase {
    /**
     * Test if values are rounded correctly when using floating point.
     */
    public void testRounding() {
        IntPort p = new IntPort(new TestNode(), "int", Port.Direction.INPUT, 0);
        p.setValue(12.45f);
        assertEquals(12, p.getValue());
        p.setValue(12.5f);
        assertEquals(13, p.getValue());
    }

    public void testParseValue() {
        IntPort p = new IntPort(new TestNode(), "int", Port.Direction.INPUT, 0);
        assertEquals(123, p.parseValue("123"));
        assertEquals(-239, p.parseValue("-239"));
    }

    public void testValueAsString() {
        IntPort p = new IntPort(new TestNode(), "int", Port.Direction.INPUT, 123);
        assertEquals("123", p.getValueAsString());
        p.setValue(-239.0f);
        assertEquals("-239", p.getValueAsString());
    }

    public void testMenuItems() {
        IntPort p = new IntPort(new TestNode(), "int", Port.Direction.INPUT, 0);
        assertFalse(p.hasMenu());
        assertEquals(0, p.getMenuItems().size());
        p.set(1);
        assertFalse(p.hasMenu());
        assertEquals("int", p.getWidget());
        p.addMenuItem(1, "item1");
        p.addMenuItem(2, "item2");
        assertTrue(p.hasMenu());
        assertEquals("menu", p.getWidget());
        assertEquals(2, p.getMenuItems().size());
        p.removeMenuItem(1);
        p.removeMenuItem(2);
        assertFalse(p.hasMenu());
        assertEquals("int", p.getWidget());
    }
}
