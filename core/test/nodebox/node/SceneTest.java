package nodebox.node;

import junit.framework.TestCase;
import processing.core.PApplet;

import java.awt.*;
import java.io.File;
import java.util.Collection;

public class SceneTest extends TestCase {
    private NodeManager manager;

    @Override
    protected void setUp() throws Exception {
        manager = new NodeManager();
        manager.registerNodeClass(AllTypesNode.class, "Test");
        manager.registerNodeClass(OutputNode.class, "Test");
        manager.registerNodeClass(InputNode.class, "Test");
    }

    public void testLoad() {
        Scene scene = Scene.load(new File("core/test/example.ndbx"), manager);
        Network root = scene.getRootNetwork();
        assertEquals("root", root.getName());
        Collection<Node> children = root.getChildren();
        assertEquals(3, children.size());
        Node types1 = root.getChild("types1");
        assertEquals(11, types1.getValue("int"));
        assertEquals(22.22f, types1.getValue("float"));
        assertEquals("33-33-33", types1.getValue("string"));
        assertEquals(Color.decode("#44444444"), types1.getValue("color"));
        Node output1 = root.getChild("output1");
        Node input1 = root.getChild("input1");
        assertTrue(output1.getPort("output").isConnected());
        assertTrue(input1.getPort("input").isConnected());
        assertEquals(0, input1.getValue("input"));
        root.execute(new Context((PApplet) null), 0);
        assertEquals(99, input1.getValue("input"));
    }

    public void testSaveLoad() {
        Scene scene = new Scene();
        Network root = scene.getRootNetwork();
        Node output1 = manager.createNode(NodeManager.nodeId(OutputNode.class));
        // TODO Build API for automatically setting node name.
        output1.setName("output1");
        Node input1 = manager.createNode(NodeManager.nodeId(InputNode.class));
        input1.setName("input1");
        root.addChild(output1);
        root.addChild(input1);
        root.connect(output1.getPort("output"), input1.getPort("input"));
        String xml = scene.toXML();
        Scene newScene = Scene.load(xml, manager);
        Network newRoot = newScene.getRootNetwork();
        assertTrue(newRoot.getChild("output1").getPort("output").isConnected());
    }

    /**
     * TestNode is a rendering node since we want them to execute.
     */
    public static class TestNode extends RenderingNode {
        @Override
        public boolean execute(Context context, double time) {
            return true;
        }
    }

    public static class AllTypesNode extends TestNode {
        private IntPort pInt = (IntPort) addPort(new IntPort(this, "int", Port.Direction.INPUT, 42));
        private FloatPort pFloat = (FloatPort) addPort(new FloatPort(this, "float", Port.Direction.INPUT, 1.23f));
        private StringPort pString = (StringPort) addPort(new StringPort(this, "string", Port.Direction.INPUT, "hello"));
        private ColorPort pColor = (ColorPort) addPort(new ColorPort(this, "color", Port.Direction.INPUT, Color.RED));
    }

    public static class OutputNode extends TestNode {
        private IntPort pOutput = (IntPort) addPort(new IntPort(this, "output", Port.Direction.OUTPUT, 99));
    }

    public static class InputNode extends TestNode {
        private IntPort pInput = (IntPort) addPort(new IntPort(this, "input", Port.Direction.INPUT, 0));
    }
}
