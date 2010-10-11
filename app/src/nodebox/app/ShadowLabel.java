package nodebox.app;

import javax.swing.*;
import java.awt.*;

class ShadowLabel extends JLabel {

    private static final Color ENABLED_COLOR = new Color(255, 255, 255, 120);
    private static final Color DISABLED_COLOR = new Color(255, 255, 255, 30);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 100);

    public ShadowLabel(String text) {
        super(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (isEnabled()) {
            g2.setColor(ENABLED_COLOR);
        } else {
            g2.setColor(DISABLED_COLOR);
        }
        g2.setFont(Theme.SMALL_BOLD_FONT);
        int textX = ParameterPanel.LABEL_WIDTH - g2.getFontMetrics().stringWidth(getText()) - 10;
        // Add some padding to align it to 30px high components.
        int textY = (getHeight() - g2.getFont().getSize()) / 2 + 10;
        SwingUtils.drawShadowText(g2, getText(), textX, textY, SHADOW_COLOR, 1);
    }
}
