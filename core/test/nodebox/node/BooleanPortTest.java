package nodebox.node;

import junit.framework.TestCase;
import nodebox.node.TestNodes.TestNode;

public class BooleanPortTest extends TestCase {
    
    public void testValueAsString() {
        BooleanPort p = new BooleanPort(new TestNode(), "bool", Port.Direction.INPUT, true);
        assertEquals("true", p.getValueAsString());
        p.set(false);
        assertEquals("false", p.getValueAsString());
    }

    public void testSetValue() {
        BooleanPort p = new BooleanPort(new TestNode(), "bool", Port.Direction.INPUT, true);
        assertEquals(true, p.getValue());
        p.set(false);
        assertEquals(false, p.getValue());
    }

    public void testParseValue() {
        BooleanPort p = new BooleanPort(new TestNode(), "bool", Port.Direction.INPUT, false);
        assertEquals(true, p.parseValue("true"));
        assertEquals(false, p.parseValue("false"));
    }
}
