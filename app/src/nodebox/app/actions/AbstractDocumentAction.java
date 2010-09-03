package nodebox.app.actions;

import nodebox.app.SceneDocument;

import javax.swing.*;

public abstract class AbstractDocumentAction extends AbstractAction {

    private SceneDocument document;

    protected AbstractDocumentAction(SceneDocument document) {
        this.document = document;
    }

    public SceneDocument getDocument() {
        return document;
    }

    @Override
    public boolean isEnabled() {
        return document != null;
    }

}
