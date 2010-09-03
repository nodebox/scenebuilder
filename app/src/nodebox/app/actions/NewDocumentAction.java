package nodebox.app.actions;

import nodebox.app.Application;
import nodebox.app.PlatformUtils;
import nodebox.app.SceneDocument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class NewDocumentAction extends AbstractAction {

    public NewDocumentAction() {
        putValue(NAME, "New");
        putValue(ACCELERATOR_KEY, PlatformUtils.getKeyStroke(KeyEvent.VK_N));
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().createNewDocument();
    }

}


