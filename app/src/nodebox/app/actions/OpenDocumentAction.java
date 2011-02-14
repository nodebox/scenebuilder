package nodebox.app.actions;

import nodebox.app.Application;
import nodebox.app.FileUtils;
import nodebox.app.PlatformUtils;
import nodebox.app.SceneDocument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class OpenDocumentAction extends AbstractAction {

    public OpenDocumentAction() {
        putValue(NAME, "Open...");
        putValue(ACCELERATOR_KEY, PlatformUtils.getKeyStroke(KeyEvent.VK_O));
    }

    public void actionPerformed(ActionEvent e) {
        File chosenFile = FileUtils.showOpenDialog(SceneDocument.getCurrentDocument(), SceneDocument.lastFilePath, "ndbx", "NodeBox Document");
        if (chosenFile != null) {
            Application.getInstance().openDocument(chosenFile);
        }
    }

}
