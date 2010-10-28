package nodebox.app.actions;

import nodebox.app.Application;
import nodebox.app.PlatformUtils;
import nodebox.app.SceneDocument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class NodeLibrariesAction extends AbstractAction {

    public NodeLibrariesAction() {
        putValue(NAME, "Manage Node Libraries");
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().showNodeLibraryManager();
    }

}


