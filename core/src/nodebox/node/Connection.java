package nodebox.node;

public class Connection {

    private final Network network;
    private final Port outputPort;
    private final Port inputPort;

    public Connection(Network network,Port outputPort, Port inputPort) {
        this.network = network;
        this.outputPort = outputPort;
        this.inputPort = inputPort;
    }

    public Network getNetwork() {
        return network;
    }

    public Node getOutputNode() {
        return outputPort.getNode();
    }

    public Port getOutputPort() {
        return outputPort;
    }

    public Node getInputNode() {
        return inputPort.getNode();
    }

    public Port getInputPort() {
        return inputPort;
    }
}
