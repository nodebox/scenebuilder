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
        root.connect(number1.getPort("value"), add1.getPort("value1"));
        assertEquals(1, l.connectCounter);
        assertEquals(0, l.disconnectCounter);
        // Create a second number and connect it to the add node.
        // This should fire a disconnectChildPort event from number1, and a connect
        // event to number2.
        Node number2 = root.createChild(Number.class);
        root.connect(number2.getPort("value"), add1.getPort("value1"));
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
        public IntPort pValue = new IntPort(this, "value", Port.Direction.OUTPUT);
    }

    public static class Add extends Node {
        public IntPort pValue1 = new IntPort(this, "value1", Port.Direction.INPUT);
        public IntPort pValue2 = new IntPort(this, "value2", Port.Direction.INPUT);
        public IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pValue1.get() + pValue2.get());
        }
    }
}
