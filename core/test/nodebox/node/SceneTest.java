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
        manager.registerNodeClass(AllTypesNode.class);
        manager.registerNodeClass(OutputNode.class);
        manager.registerNodeClass(InputNode.class);
        manager.registerNodeClass(InputSplitter.class);
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
        input1.setRenderedNode();
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
        assertEquals(2, newRoot.getChildren().size());
        assertTrue(newRoot.getChild("outputNode1").getPort("output").isOutputPort());
        assertTrue(newRoot.getChild("outputNode1").getPort("output").isConnected());
        assertTrue(newRoot.getChild("inputNode1").getPort("input").isConnectedTo(newRoot.getChild("outputNode1")));
    }

    public void testSavePublished() {
        Scene scene = new Scene();
        Network root = scene.getRootNetwork();
        Network net = (Network) root.createChild(Network.class);
        net.setScene(scene);
        Node input1 = net.createChild(InputNode.class);
        Node input2 = net.createChild(InputNode.class);
        Node input3 = net.createChild(InputNode.class);
        Node input4 = net.createChild(InputNode.class);
        net.publishPort(input1.getPort("input"));
        net.publishPort(input3.getPort("input"));
        net.unPublishPort(input3.getPort("input"));
        net.publishPort(input4.getPort("input"));
        Node output1 = root.createChild(OutputNode.class);
        root.connect(output1.getPort("output"), net.getPort("inputNode4_input"));
        String xml = scene.toXML();
        Scene newScene = Scene.load(xml, manager);
        Network newRoot = newScene.getRootNetwork();
        assertTrue(newRoot.hasChild("network1"));
        Network net1 = (Network) newRoot.getChild("network1");
        assertTrue(net1.hasPort("inputNode1_input"));
        assertFalse(net1.hasPort("intputNode2_input"));
        assertFalse(net1.hasPort("intputNode3_input"));
        assertTrue(net1.hasPort("inputNode4_input"));
        assertTrue(net1.isPublished(net1.getChild("inputNode1").getPort("input")));
        assertFalse(net1.isPublished(net1.getChild("inputNode2").getPort("input")));
        assertFalse(net1.isPublished(net1.getChild("inputNode3").getPort("input")));
        assertFalse(net1.getChild("inputNode1").isConnected());
        assertFalse(net1.getPort("inputNode1_input").isConnected());
        assertTrue(net1.getPort("inputNode4_input").isConnected());
        assertEquals(0, net1.getChild("inputNode4").getPort("input").getValue());
        newRoot.execute(new Context((PApplet) null), 0);
        assertEquals(99, net1.getChild("inputNode4").getPort("input").getValue());
    }

    public void testSaveVariant() {
        Scene scene = new Scene();
        Network root = scene.getRootNetwork();
        Network net = (Network) root.createChild(Network.class);
        net.setScene(scene);
        Node inputSplitter1 = net.createChild(InputSplitter.class);
        Node inputSplitter2 = net.createChild(InputSplitter.class);
        net.connect(inputSplitter2.getPort("output"), inputSplitter1.getPort("input"));
        net.publishPort(inputSplitter2.getPort("input"));
        net.publishPort(inputSplitter1.getPort("output"));
        inputSplitter1.setRenderedNode();
        Node outputNode = root.createChild(OutputNode.class);
        Node inputNode = root.createChild(InputNode.class);
        root.connect(outputNode.getPort("output"), net.getPort("inputSplitter2_input"));
        root.connect(net.getPort("inputSplitter1_output"), inputNode.getPort("input"));
        inputNode.setRenderedNode();
        Scene newScene = Scene.load(scene.toXML(), manager);
        Network newRoot = newScene.getRootNetwork();
        assertTrue(newRoot.getChild("network1").getPort("inputSplitter2_input").isConnected());
        assertTrue(newRoot.getChild("network1").getPort("inputSplitter1_output").isConnected());
        newRoot.getChild("outputNode1").setValue("output", 42);
        assertEquals(0, newRoot.getChild("inputNode1").getValue("input"));
        newRoot.execute(new Context((PApplet) null), 0);
        assertEquals(42, newRoot.getChild("inputNode1").getValue("input"));
    }

    @Category("Test")
    public static class AllTypesNode extends Node {
        public final IntPort pInt = new IntPort(this, "int", Port.Direction.INPUT, 42);
        public final FloatPort pFloat = new FloatPort(this, "float", Port.Direction.INPUT, 1.23f);
        public final StringPort pString = new StringPort(this, "string", Port.Direction.INPUT, "hello");
        public final ColorPort pColor = new ColorPort(this, "color", Port.Direction.INPUT, Color.RED);
    }

    @Category("Test")
    public static class OutputNode extends Node {
        public final IntPort pOutput = new IntPort(this, "output", Port.Direction.OUTPUT, 99);
    }

    @Category("Test")
    public static class InputNode extends Node {
        public final IntPort pInput = new IntPort(this, "input", Port.Direction.INPUT, 0);
    }

    @Category("Test")
    public static class InputSplitter extends Node {
        public VariantPort pInput = new VariantPort(this, "input", Port.Direction.INPUT);
        public VariantPort pOutput = new VariantPort(this, "output", Port.Direction.OUTPUT);

        @Override
        public void execute(Context context, float time) {
            pOutput.set(pInput.get());
        }
    }
}
