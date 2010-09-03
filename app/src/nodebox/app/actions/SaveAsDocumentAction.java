package nodebox.app.actions;

import nodebox.app.PlatformUtils;
import nodebox.app.SceneDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SaveAsDocumentAction extends AbstractDocumentAction {

    public SaveAsDocumentAction(SceneDocument document) {
        super(document);
        putValue(NAME, "Save As...");
        putValue(ACCELERATOR_KEY, PlatformUtils.getKeyStroke(KeyEvent.VK_S, Event.SHIFT_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        getDocument().saveAs();
    }

}
