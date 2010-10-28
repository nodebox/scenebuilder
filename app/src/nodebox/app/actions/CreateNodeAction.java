package nodebox.app.actions;

import nodebox.app.SceneDocument;
import nodebox.node.Description;
import nodebox.node.Node;
import nodebox.node.NodeInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateNodeAction extends AbstractDocumentAction {

    private NodeInfo nodeInfo;

    public CreateNodeAction(SceneDocument document, NodeInfo info) {
        super(document);
        this.nodeInfo = info;
        putValue(NAME, info.getNodeClass().getSimpleName());
        putValue(SHORT_DESCRIPTION, info.getDescription());
    }

    public void actionPerformed(ActionEvent e) {
        getDocument().createNode(nodeInfo.getNodeClass());
    }
}
