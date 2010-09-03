package nodebox.app.actions;

import nodebox.app.PlatformUtils;
import nodebox.app.SceneDocument;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SaveDocumentAction extends AbstractDocumentAction {

    public SaveDocumentAction(SceneDocument document) {
        super(document);
        putValue(NAME, "Save");
        putValue(ACCELERATOR_KEY, PlatformUtils.getKeyStroke(KeyEvent.VK_S));
    }

    public void actionPerformed(ActionEvent e) {
        getDocument().save();
    }

}
