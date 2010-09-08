package nodebox.app;

import javax.swing.*;
import java.awt.*;

public class Pane extends JPanel {

    private PaneHeader paneHeader;
    private Component content;

    public Pane(PaneHeader paneHeader, Component content) {
        this.paneHeader = paneHeader;
        this.content = content;
        setLayout(new BorderLayout(0, 0));
        add(paneHeader, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }
}
