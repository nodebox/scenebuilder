package nodebox.app.actions;

import nodebox.app.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitAction extends AbstractAction {

    public ExitAction() {
        putValue(NAME, "Exit");
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().quit();
    }

}
