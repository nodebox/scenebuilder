package nodebox.node.event;

import nodebox.node.Node;
import nodebox.node.NodeEvent;
import nodebox.node.Context;

public class NodeUpdatedEvent extends NodeEvent {

    private Context context;


    public NodeUpdatedEvent(Node source, Context context) {
        super(source);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "NodeUpdatedEvent{" +
                "source=" + getSource() +
                '}';
    }

}
