package scenebuilder.model;

public class Connection {

    private final Macro parent;
    private final Node outputNode;
    private final Port outputPort;
    private final Node inputNode;
    private final Port inputPort;

    public Connection(Macro parent, Node outputNode, Port outputPort, Node inputNode, Port inputPort) {
        this.parent = parent;
        this.outputNode = outputNode;
        this.outputPort = outputPort;
        this.inputNode = inputNode;
        this.inputPort = inputPort;
    }

    public Macro getParent() {
        return parent;
    }

    public Node getOutputNode() {
        return outputNode;
    }

    public Port getOutputPort() {
        return outputPort;
    }

    public Node getInputNode() {
        return inputNode;
    }

    public Port getInputPort() {
        return inputPort;
    }
}
