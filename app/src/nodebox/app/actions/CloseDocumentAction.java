package nodebox.app.actions;

import nodebox.app.PlatformUtils;
import nodebox.app.SceneDocument;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class CloseDocumentAction extends AbstractDocumentAction {

    public CloseDocumentAction(SceneDocument document) {
        super(document);
        putValue(NAME, "Close");
        putValue(ACCELERATOR_KEY, PlatformUtils.getKeyStroke(KeyEvent.VK_W));
    }

    public void actionPerformed(ActionEvent e) {
        getDocument().close();
    }

}

