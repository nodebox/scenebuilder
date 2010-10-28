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
        manager.registerNodeInfo(AllTypesNode.class, "Test");
        manager.registerNodeInfo(OutputNode.class, "Test");
        manager.registerNodeInfo(InputNode.class, "Test");
    }

    public void testLoad() {
        Scene scene = Scene.load(new File("core/test/example.ndbx"), manager);
        Network root = scene.getRootNetwork();
        assertEquals("root", root.getName());
        Collection<Node> children = root.getChildren();
        assertEquals(3, children.size());
        Node types1 = root.getChild("types1");
        assertEquals("Custom Display Name", types1.getDisplayName());
        assertEquals(11, types1.getValue("int"));
        assertEquals(22.22f, types1.getValue("float"));
        assertEquals("33-33-33", types1.getValue("string"));
        assertEquals(new Color(68, 68, 68, 68), types1.getValue("color"));
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
        Node output1 = root.createChild(OutputNode.class);
        Node input1 = root.createChild(InputNode.class);
        root.addChild(output1);
        root.addChild(input1);
        root.connect(output1.getPort("output"), input1.getPort("input"));
        String xml = scene.toXML();
        Scene newScene = Scene.load(xml, manager);
        Network newRoot = newScene.getRootNetwork();
        assertTrue(newRoot.getChild("outputNode1").getPort("output").isConnected());
    }

    public static class AllTypesNode extends Node {
        public final IntPort pInt = new IntPort(this, "int", Port.Direction.INPUT, 42);
        public final FloatPort pFloat = new FloatPort(this, "float", Port.Direction.INPUT, 1.23f);
        public final StringPort pString = new StringPort(this, "string", Port.Direction.INPUT, "hello");
        public final ColorPort pColor = new ColorPort(this, "color", Port.Direction.INPUT, Color.RED);
    }

    public static class OutputNode extends Node {
        public final IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT, 99);
    }

    public static class InputNode extends Node {
        public final IntPort pInput = new IntPort(this, "input", Port.Direction.INPUT, 0);
    }
}
