package nodebox.app.actions;

import nodebox.app.SceneDocument;

import java.awt.event.ActionEvent;

public class ScenePropertiesAction extends AbstractDocumentAction {
    public ScenePropertiesAction(SceneDocument document) {
        super(document);
        putValue(NAME, "Scene Properties");
    }

     public void actionPerformed(ActionEvent e) {
        getDocument().showSceneProperties();
    }
}
