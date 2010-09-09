package nodebox.node;

import junit.framework.TestCase;

public class NetworkTest extends TestCase {
    

    public void testCreateChild() {
        Node n = new TestNode();
        assertEquals("testNode", n.getName());
        assertEquals("Test Node", n.getDisplayName());
        Network net = new Network();
        Node testNode1 = net.createChild(TestNode.class);
        assertEquals("testNode1", testNode1.getName());
        assertEquals("Test Node", testNode1.getDisplayName());
        // Change it to some other number.
        Node testNode2 = net.createChild(TestNode.class);
        assertEquals("testNode2", testNode2.getName());
        testNode2.setName("testNode1");
    }

    public static class TestNode extends Node {
        @Override
        public void execute(Context context, float time) {
        }
    }
}
