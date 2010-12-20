package nodebox.node;

import junit.framework.TestCase;
import nodebox.node.event.ChildAddedEvent;
import nodebox.node.event.ChildRemovedEvent;

public class NetworkTest extends TestCase {
    
    class TestChildListener implements NodeEventListener {
        public Network source;
        public int childAddedCounter = 0;
        public int childRemovedCounter = 0;

        TestChildListener(Network source) {
            this.source = source;
        }

        public void receive(NodeEvent event) {
            System.out.println(event.getSource());
            if (event.getSource() != this.source) return;
            if (event instanceof ChildAddedEvent) {
                childAddedCounter++;
            } else if (event instanceof ChildRemovedEvent) {
                childRemovedCounter++;
            }
        }
    }

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

    public void testChildEvent() {
        Scene scene = new Scene();
        Network net1 = new Network();
        net1.setScene(scene);
        Network net2 = new Network();
        net2.setScene(scene);
        Network net3 = new Network();
        net3.setScene(scene);
        TestChildListener l1 = new TestChildListener(net1);
        TestChildListener l2 = new TestChildListener(net2);
        TestChildListener l3 = new TestChildListener(net3);
        scene.addListener(l1);
        scene.addListener(l2);
        scene.addListener(l3);
        Node n1 = net1.createChild(TestNode.class);
        assertEquals(1, l1.childAddedCounter);
        n1.setNetwork(net2);
        assertEquals(1, l1.childAddedCounter);
        assertEquals(1, l2.childAddedCounter);
        assertEquals(1, l1.childRemovedCounter);
        net3.addChild(n1);
        assertEquals(1, l2.childRemovedCounter);
        assertEquals(1, l3.childAddedCounter);
    }


    public static class TestNode extends Node {
        @Override
        public void execute(Context context, float time) {
        }
    }
}
