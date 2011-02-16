package nodebox.node;

import junit.framework.TestCase;
import nodebox.node.event.NodeDirtyEvent;
import nodebox.node.event.NodeUpdatedEvent;
import processing.core.PGraphics;

import nodebox.node.TestNodes.TestNode;
import nodebox.node.TestNodes.Number;
import nodebox.node.TestNodes.Add;

public class NodeTest extends TestCase {

    private class TestDirtyListener implements NodeEventListener {
        public Node source;
        public int dirtyCounter, updatedCounter;

        private TestDirtyListener(Node source) {
            this.source = source;
        }

        public void receive(NodeEvent event) {
            if (event.getSource() != source) return;
            if (event instanceof NodeDirtyEvent) {
                dirtyCounter++;
            } else if (event instanceof NodeUpdatedEvent) {
                updatedCounter++;
            }
        }
    }

    public void testBasicUsage() {
        Node dotNode = new DotNode();

        // Check default values
        assertEquals(0F, dotNode.getValue("x"));
        assertEquals(0F, dotNode.getValue("y"));
        assertNull(dotNode.getValue("output"));

        // Process
        dotNode.update(new MockContext(), 0f);
        assertEquals("dot(0.0,0.0)", dotNode.getValue("output"));

        // Change values
        dotNode.setValue("x", 25F);
        dotNode.setValue("y", 42F);
        dotNode.update(new MockContext(), 0f);
        assertEquals("dot(25.0,42.0)", dotNode.getValue("output"));
    }

    public void testGetValue() {
        Node n = new CNode();
        assertEquals(1F, n.getValue("a"));
        assertEquals(2F, n.getValue("b"));
        assertEquals(3F, n.getValue("c"));
    }

    public void testSetValue() {
        Node nodeC = new CNode();
        nodeC.setValue("a", 10F);
        nodeC.setValue("b", 20F);
        nodeC.setValue("c", 30F);
        assertEquals(10F, nodeC.getValue("a"));
        assertEquals(20F, nodeC.getValue("b"));
        assertEquals(30F, nodeC.getValue("c"));
    }

    public void testPortAttributes() {
        Node nodeA = new TestNode();
        assertEquals(0, nodeA.getOutputPorts().size());
        new StringPort(nodeA, "output", Port.Direction.OUTPUT);
        assertTrue(nodeA.getPort("output") instanceof StringPort);
        assertEquals(1, nodeA.getOutputPorts().size());
        Port pOutput = nodeA.getOutputPorts().get(0);
        assertEquals("output", pOutput.getName());
        assertEquals("output", pOutput.getDisplayName());
        assertEquals(Port.Direction.OUTPUT, pOutput.getDirection());
        assertTrue(pOutput.isOutputPort());
        assertEquals(null, pOutput.getValue());
        Port pString = new StringPort(nodeA, "stringPort", Port.Direction.INPUT);
        assertEquals("stringPort", pString.getName());
        assertEquals("string Port", pString.getDisplayName());
        assertEquals(Port.Direction.INPUT, pString.getDirection());
        assertTrue(pString.isInputPort());
        assertEquals(null, pString.getValue());
    }

    public void testChildNodes() {
        Network net = new Network();
        Node test = new TestNode();
        net.addChild(test);
        assertTrue(net.hasChild("testNode"));
    }

    public void testUniqueName() {
        Network net = new Network();
        Node node1 = net.createChild(TestNode.class);
        assertEquals("testNode1", node1.getName());
        assertEquals("testNode2", net.uniqueChildName("testNode"));
        assertEquals("testNode2", net.uniqueChildName("testNode1"));
        assertEquals("testNode33", net.uniqueChildName("testNode33"));
        Node node99 = net.createChild(TestNode.class);
        node99.setName("testNode99");
        assertEquals("testNode2", net.uniqueChildName("testNode"));
        assertEquals("testNode100", net.uniqueChildName("testNode99"));
        assertEquals("testNode12a1", net.uniqueChildName("testNode12a"));
    }

    public void testDirty() {
        Network net = new Network();
        Node n = net.createChild(Number.class);
        assertTrue(n.isDirty());
        n.update(new MockContext(), 0f);
        assertFalse(n.isDirty());
        n.setValue("value", 12);
        assertTrue(n.isDirty());
        n.update(new MockContext(), 0f);
        assertFalse(n.isDirty());
        n.getPort("value").setValue(13);
        assertTrue(n.isDirty());
        n.update(new MockContext(), 0f);
        assertFalse(n.isDirty());
    }

    public void testDirtyEvent() {
        Scene scene = new Scene();
        Network net = new Network();
        net.setScene(scene);
        Node add = net.createChild(Add.class);
        TestDirtyListener listener = new TestDirtyListener(add);
        scene.addListener(listener);
        add.setValue("v1", 12);
        add.setValue("v2", 3);
        // Since the node starts out as dirty, setting values doesn't increase the counter.
        assertEquals(0, listener.dirtyCounter);
        // This code inherits the default code, which doesn't throw an error.
        add.update(new MockContext(), 0f);
        assertEquals(15, add.getValue("output"));
        // Updating the code marks it as clean.
        assertFalse(add.isDirty());
        assertEquals(1, listener.updatedCounter);
        assertEquals(0, listener.dirtyCounter);
        add.setValue("v1", 17);
        assertEquals(1, listener.dirtyCounter);
        // We just changed a parameter value, so the node is dirty.
        assertTrue(add.isDirty());
        add.update(new MockContext(), 0f);
        assertFalse(add.isDirty());
        assertEquals(20, add.getValue("output"));
        assertEquals(2, listener.updatedCounter);
        assertEquals(1, listener.dirtyCounter);
    }

    public void testExternalInput() {
        Scene scene = new Scene();
        Network net = new Network();
        net.setScene(scene);
        Node add = net.createChild(Add.class);
        TestDirtyListener listener = new TestDirtyListener(add);
        scene.addListener(listener);
        assertTrue(add.isDirty());
        Node counter = net.createChild(Counter.class);
        assertTrue(counter.isDirty());
        net.connect(counter.getPort("value"), add.getPort("v1"));
        add.setValue("v2", 20);
        assertEquals(0, add.getValue("output"));
        assertEquals(0, listener.dirtyCounter);
        assertEquals(0, listener.updatedCounter);
        add.update(new MockContext(), 0f);
        assertEquals(1, listener.updatedCounter);
        assertEquals(20, add.getValue("output"));
        assertTrue(add.isDirty());
        add.update(new MockContext(), 0f);
        assertEquals(2, listener.updatedCounter);
        assertEquals(1, counter.getValue("value"));
        add.update(new MockContext(), 0f);
        assertEquals(0, listener.dirtyCounter);
        assertEquals(3, listener.updatedCounter);
        assertEquals(22, add.getValue("output"));
        assertEquals(2, counter.getValue("value"));
    }

    public void testLifeCycleMethods() {
        Scene scene = new Scene();
        Network net = scene.getRootNetwork();
        LifeCycleNode alpha = (LifeCycleNode) net.createChild(LifeCycleNode.class);
        net.execute(new MockContext(), 0f);

        // Alpha is not the rendered node. This means it is not reachable, and will not be activated or executed.
        assertEquals(1, alpha.initialized);
        assertEquals(0, alpha.activated);
        assertEquals(0, alpha.executed);
        assertEquals(0, alpha.drawn);
        assertEquals(0, alpha.deactivated);
        assertEquals(0, alpha.destroyed);

        // Make it the rendered node. This activates the node.
        alpha.setRenderedNode();
        assertEquals(1, alpha.activated);
        assertEquals(0, alpha.executed);

        // Execute the network. This will execute the rendered node.
        // The activated node will not be called again.
        net.execute(new MockContext(), 0f);
        assertEquals(1, alpha.activated);
        assertEquals(1, alpha.executed);
        assertEquals(0, alpha.drawn);

        // Create a new node and set it as the rendered node.
        // Since beta does not connect to alpha, alpha should deactivate.
        LifeCycleNode beta = (LifeCycleNode) net.createChild(LifeCycleNode.class);
        beta.setRenderedNode();
        assertEquals(1, beta.activated);
        assertEquals(1, alpha.deactivated);

        // Remove the alpha node from the network. This means the node will be destroyed.
        net.removeChild(alpha);
        assertEquals(1, alpha.destroyed);

        // Remove the beta node from the network. This will deactivate and destroy it.
        net.removeChild(beta);
        assertEquals(1, beta.deactivated);
        assertEquals(1, beta.destroyed);
    }

    /**
     * Test activation with connected nodes.
     */
    public void testActivation() {
        // Create a network such that alpha <- beta <- gamma and alpha <- delta.
        Scene scene = new Scene();
        Network net = scene.getRootNetwork();
        LifeCycleNode alpha = (LifeCycleNode) net.createChild(LifeCycleNode.class);
        LifeCycleNode beta = (LifeCycleNode) net.createChild(LifeCycleNode.class);
        LifeCycleNode gamma = (LifeCycleNode) net.createChild(LifeCycleNode.class);
        LifeCycleNode delta= (LifeCycleNode) net.createChild(LifeCycleNode.class);
        net.connect(alpha.pOutput, beta.pInput);
        net.connect(beta.pOutput, gamma.pInput);
        net.connect(alpha.pOutput, delta.pInput);

        beta.setRenderedNode();
        assertEquals(1, alpha.activated);
        assertEquals(1, beta.activated);
        assertEquals(0, gamma.activated);

        delta.setRenderedNode();
        // Alpha was already activated, it does not get activated again.
        // Beta is no longer reachable and is deactivated.
        assertEquals(1, alpha.activated);
        assertEquals(1, beta.deactivated);
        assertEquals(1, delta.activated);

        gamma.setRenderedNode();
        // Alpha is still activated.
        // Beta is reachable again, will be activated another time.
        assertEquals(1, alpha.activated);
        assertEquals(2, beta.activated);
        assertEquals(1, gamma.activated);
        assertEquals(1, delta.deactivated);
    }

    public void testBreakChain() {
        // Create a network such that alpha <- beta <- gamma.
        Scene scene = new Scene();
        Network net = scene.getRootNetwork();
        LifeCycleNode alpha = (LifeCycleNode) net.createChild(LifeCycleNode.class);
        LifeCycleNode beta = (LifeCycleNode) net.createChild(LifeCycleNode.class);
        LifeCycleNode gamma = (LifeCycleNode) net.createChild(LifeCycleNode.class);
        net.connect(alpha.pOutput, beta.pInput);
        net.connect(beta.pOutput, gamma.pInput);
        gamma.setRenderedNode();

        // By removing beta, we break the connection between alpha and gamma.
        // Alpha is thus no longer reachable and will be deactivated.
        net.removeChild(beta);
        assertEquals(1, alpha.deactivated);
        assertEquals(1, beta.deactivated);
        assertEquals(0, gamma.deactivated);
    }

    public static class LifeCycleNode extends Node {

        public IntPort pInput = new IntPort(this, "input", Port.Direction.INPUT);
        public IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT); 

        private int initialized = 0;
        private int activated = 0;
        private int executed = 0;
        private int drawn = 0;
        private int deactivated = 0;
        private int destroyed = 0;

        @Override
        public void initialize() {
            initialized++;
        }

        @Override
        public void activate() {
            activated++;
        }

        @Override
        public void execute(Context context, float time) {
            executed++;
            int value = pInput.get();
            value = -value;
            pOutput.set(value);
        }

        @Override
        public void draw(PGraphics g, Context context, float time) {
            drawn++;
        }

        @Override
        public void deactivate() {
            deactivated++;
        }

        @Override
        public void destroy() {
            destroyed++;
        }
    }

    public static class DotNode extends Node {
        public FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
        public FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
        public StringPort pOutput = new StringPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            float x = pX.get();
            float y = pY.get();
            pOutput.set("dot(" + x + "," + y + ")");
        }
    }

    public static class ANode extends Node {
        public FloatPort pA = new FloatPort(this, "a", Port.Direction.INPUT, 1F);
    }

    public static class BNode extends ANode {
        public FloatPort pB = new FloatPort(this, "b", Port.Direction.INPUT, 2F);
    }

    public static class CNode extends BNode {
        public FloatPort pC = new FloatPort(this, "c", Port.Direction.INPUT, 3F);
    }

    @Category("Test")
    @ExternalInput
    public static class Counter extends Node {
        public IntPort pValue = new IntPort(this, "value", Port.Direction.OUTPUT);
        private int value;

        @Override
        public void initialize() {
            value = 0;
        }

        @Override
        public void execute(Context context, float time) {
            pValue.set(value);
            value++;
        }
    }
}
