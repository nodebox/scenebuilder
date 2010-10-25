package nodebox.node;

import junit.framework.TestCase;
import processing.core.PGraphics;

public class NodeTest extends TestCase {

    public void testLifeCycleMethods() {
        Network net = new Network();
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
        Network net = new Network();
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
        Network net = new Network();
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

}
