package nodebox.app.actions;

import nodebox.app.Application;
import nodebox.app.SceneDocument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class OpenRecentDocumentAction extends AbstractAction {

    private File file;

    public OpenRecentDocumentAction(File file) {
        this.file = file;
        putValue(NAME, file.getName());
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().openDocument(file);
    }

}



