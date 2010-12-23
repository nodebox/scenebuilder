package nodebox.node;

import junit.framework.TestCase;
import nodebox.node.event.ConnectionAddedEvent;
import nodebox.node.event.ConnectionRemovedEvent;

public class ConnectTest extends TestCase {

    private class ConnectListener implements NodeEventListener {
        public int connectCounter = 0;
        public int disconnectCounter = 0;

        public void receive(NodeEvent event) {
            if (event instanceof ConnectionAddedEvent) {
                connectCounter++;
            } else if (event instanceof ConnectionRemovedEvent) {
                disconnectCounter++;
            }
        }
    }

    public void testConnect() {
        Network net = new Network();
        Node number1 = net.createChild(Number.class);
        Node multiply1 = net.createChild(Multiply.class);
        Node upper1 = net.createChild(ConvertToUppercase.class);

        assertFalse(multiply1.getPort("v1").isConnected());
        assertFalse(multiply1.getPort("v1").isConnectedTo(number1.getPort("output")));
        assertFalse(multiply1.getPort("v1").isConnectedTo(number1));
        assertFalse(number1.getPort("output").isConnected());
        assertFalse(number1.getPort("output").isConnectedTo(multiply1.getPort("v1")));
        assertFalse(number1.getPort("output").isConnectedTo(multiply1));

        assertTrue(multiply1.getPort("v1").canConnectTo(number1.getPort("output")));
        assertTrue(multiply1.getPort("v2").canConnectTo(number1.getPort("output")));
        assertFalse(upper1.getPort("output").canConnectTo(number1.getPort("value")));
        assertFalse(upper1.getPort("output").canConnectTo(number1.getPort("output")));

        Connection conn = net.connect(number1.getPort("output"), multiply1.getPort("v1"));
        assertTrue(multiply1.getPort("v1").isConnected());
        assertTrue(multiply1.getPort("v1").isConnectedTo(number1.getPort("output")));
        assertTrue(multiply1.getPort("v1").isConnectedTo(number1));
        assertTrue(number1.getPort("output").isConnected());
        assertTrue(number1.getPort("output").isConnectedTo(multiply1.getPort("v1")));
        assertTrue(number1.getPort("output").isConnectedTo(multiply1));
        assertEquals(multiply1.getPort("v1"), conn.getInputPort());
        assertEquals(number1.getPort("output"), conn.getOutputPort());
        assertEquals(multiply1, conn.getInputNode());
        assertEquals(number1, conn.getOutputNode());

        Node number2 = net.createChild(Number.class);
        Node multiply2 = net.createChild(Multiply.class);
        net.connect(number2.getPort("output"), multiply2.getPort("v1"));
        assertTrue(number2.getPort("output").isConnectedTo(multiply2.getPort("v1")));
        assertTrue(number2.getPort("output").isConnectedTo(multiply2));
        assertFalse(number1.getPort("output").isConnectedTo(multiply2.getPort("v1")));
        assertFalse(number1.getPort("output").isConnectedTo(multiply2));
        assertFalse(number2.getPort("output").isConnectedTo(multiply1.getPort("v1")));
        assertFalse(number2.getPort("output").isConnectedTo(multiply1));
    }

    public void testValuePropagation() {
        Network net = new Network();
        Node number1 = net.createChild(Number.class);
        Node number2 = net.createChild(Number.class);
        Node m = net.createChild(Multiply.class);
        net.connect(number1.getPort("output"), m.getPort("v1"));
        net.connect(number2.getPort("output"), m.getPort("v2"));
        assertEquals(0, m.getValue("output"));
        number1.setValue("value", 3);
        number2.setValue("value", 2);
        assertEquals(0, m.getValue("output"));
        // Updating the NumberIn node has no effect on the multiplier node.
        number1.update(new MockContext(), 0.0f);
        assertEquals(0, m.getValue("output"));
        m.update(new MockContext(), 0.0f);
        assertEquals(6, m.getValue("output"));
        // Test if value stops propagating after disconnection.
        net.disconnect(m.getPort("v1"));
        assertFalse(m.getPort("v1").isConnected());
        // The value is still the old value because the node has not been updated yet.
        assertEquals(6, m.getValue("output"));
        number1.setValue("value", 3);
    }

    public void testDisconnect() {
        Network net = new Network();
        Node number1 = net.createChild(Number.class);
        Node number2 = net.createChild(Number.class);
        Node m = net.createChild(Multiply.class);
        number1.setValue("value", 5);
        number2.setValue("value", 2);
        net.connect(number1.getPort("output"), m.getPort("v1"));
        net.connect(number2.getPort("output"), m.getPort("v2"));
        assertTrue(m.getPort("v1").isConnected());
        assertTrue(number1.getPort("output").isConnected());
        m.update(new MockContext(), 0.0f);
        assertEquals(5, m.getPort("v1").getValue());
        assertEquals(10, m.getValue("output"));
        assertFalse(m.getPort("v1").getConnections().isEmpty());

        // Disconnecting a port makes the dependent nodes dirty, but not the upstream nodes.
        // "Dirt flows downstream"
        net.disconnect(m.getPort("v1"));
        assertFalse(m.getPort("v1").isConnected());
        assertFalse(number1.getPort("output").isConnected());
        assertTrue(m.getPort("v1").getConnections().isEmpty());
    }

    /**
     * Check if all connections are destroyed when removing a node.
     */
    public void testRemoveNode() {
        Network net = new Network();
        // Create a basic connection.
        Node numberOut = net.createChild(Number.class);
        Node numberIn = net.createChild(Number.class);
        net.connect(numberOut.getPort("output"), numberIn.getPort("value"));
        // Remove the node. This should also remove all connections.
        net.removeChild(numberOut);
        assertFalse(numberOut.isConnected());
        assertFalse(numberIn.isConnected());
    }

    public void testOnlyOneConnect() {
        Network net = new Network();
        Node number1 = net.createChild(Number.class);
        Node number2 = net.createChild(Number.class);
        Node numberIn = net.createChild(Number.class);
        net.connect(number1.getPort("output"), numberIn.getPort("value"));
        assertTrue(number1.isConnected());
        assertFalse(number2.isConnected());
        assertTrue(numberIn.isConnected());
        // Now change the connection to number2.
        net.connect(number2.getPort("output"), numberIn.getPort("value"));
        assertFalse(number1.isConnected());
        assertTrue(number2.isConnected());
        assertTrue(numberIn.isConnected());
    }

    public void testConnectionEvents() {
        ConnectListener l = new ConnectListener();
        Scene scene = new Scene();
        scene.addListener(l);
        // Setup a basic network with number1 <- add1
        Network root = scene.getRootNetwork();
        Node number1 = root.createChild(Number.class);
        Node add1 = root.createChild(Add.class);
        // No connect/disconnectChildPort events have been fired.
        assertEquals(0, l.connectCounter);
        assertEquals(0, l.disconnectCounter);
        // Creating a connection fires the event.
        root.connect(number1.getPort("output"), add1.getPort("v1"));
        assertEquals(1, l.connectCounter);
        assertEquals(0, l.disconnectCounter);
        // Create a second number and connect it to the add node.
        // This should fire a disconnectChildPort event from number1, and a connect
        // event to number2.
        Node number2 = root.createChild(Number.class);
        root.connect(number2.getPort("output"), add1.getPort("v1"));
        assertEquals(2, l.connectCounter);
        assertEquals(1, l.disconnectCounter);
        // Disconnect the add node. This should remove all (1) connections,
        // and cause one disconnectChildPort event.
        root.disconnect(add1);
        assertEquals(2, l.connectCounter);
        assertEquals(2, l.disconnectCounter);
        scene.removeListener(l);
    }

    public static class Number extends Node {
        public IntPort pValue = new IntPort(this, "value", Port.Direction.INPUT);
        public IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pValue.get());
        }
    }

    public static class Add extends Node {
        public IntPort pValue1 = new IntPort(this, "v1", Port.Direction.INPUT);
        public IntPort pValue2 = new IntPort(this, "v2", Port.Direction.INPUT);
        public IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pValue1.get() + pValue2.get());
        }
    }

    public static class Multiply extends Node {
        public IntPort pValue1 = new IntPort(this, "v1", Port.Direction.INPUT);
        public IntPort pValue2 = new IntPort(this, "v2", Port.Direction.INPUT);
        public IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pValue1.get() * pValue2.get());
        }
    }

    public static class ConvertToUppercase extends Node {
        public StringPort pString = new StringPort(this, "string", Port.Direction.INPUT);
        public StringPort pOutput = new StringPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pString.get().toUpperCase());
        }
    }
}
