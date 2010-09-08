package nodebox.app;

import javax.swing.*;
import java.awt.*;

public class PaneHeader extends JPanel {

    public static final Color BACKGROUND_COLOR = new Color(210, 210, 210);
    public static final Color BACKGROUND_COLOR_LIGHTER = new Color(225, 225, 225);
    public static final Color BACKGROUND_COLOR_DARKER = new Color(136, 136, 136);
    public static final int PANE_HEADER_HEIGHT = 25;

    public PaneHeader() {
        super(new FlowLayout(FlowLayout.LEADING, 5, 2));
        setPreferredSize(new Dimension(100, PANE_HEADER_HEIGHT));
        setMinimumSize(new Dimension(100, PANE_HEADER_HEIGHT));
        setMaximumSize(new Dimension(100, PANE_HEADER_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(BACKGROUND_COLOR_LIGHTER);
        g.drawLine(0, 0, getWidth(), 0);
        g.setColor(BACKGROUND_COLOR_DARKER);
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
}
