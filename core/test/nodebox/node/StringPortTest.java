package nodebox.node;

import junit.framework.TestCase;

public class StringPortTest extends TestCase {
    
    public void setTestValue() {
        StringPort p = new StringPort(new TestNode(), "string", Port.Direction.INPUT);
        p.set("hello");
        assertEquals("hello", p.get());
        p.setValue("abcdef");
        assertEquals("abcdef", p.getValue());
        assertEquals("abcdef", p.getValueAsString());
        assertEquals("mystring", p.parseValue("mystring"));
    }

    public void testMenuItems() {
        StringPort p = new StringPort(new TestNode(), "string", Port.Direction.INPUT);
        assertFalse(p.hasMenu());
        assertEquals(0, p.getMenuItems().size());
        p.set("key1");
        assertFalse(p.hasMenu());
        assertEquals("string", p.getWidget());
        p.addMenuItem("key1", "item1");
        p.addMenuItem("key2", "item2");
        assertTrue(p.hasMenu());
        assertEquals("menu", p.getWidget());
        assertEquals(2, p.getMenuItems().size());
        p.removeMenuItem("key1");
        p.removeMenuItem("key2");
        assertFalse(p.hasMenu());
        assertEquals("string", p.getWidget());
    }

    public static class TestNode extends Node {
        @Override
        public void execute(Context context, float time) {
        }
    }
}
