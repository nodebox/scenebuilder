package nodebox.app.actions;

import nodebox.app.SceneDocument;
import nodebox.node.Description;
import nodebox.node.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateNodeAction extends AbstractDocumentAction {

    private Class<? extends Node> nodeClass;

    public CreateNodeAction(SceneDocument document, String name, Class<? extends Node> nodeClass) {
        super(document);
        putValue(NAME, name);
        this.nodeClass = nodeClass;
        Description description = nodeClass.getAnnotation(Description.class);
        putValue(SHORT_DESCRIPTION, description);
    }

    public void actionPerformed(ActionEvent e) {
        getDocument().createNode(nodeClass);
    }
}
