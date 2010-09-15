package nodebox.app;

import net.miginfocom.swing.MigLayout;
import nodebox.node.Scene;
import processing.core.PConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ScenePropertiesDialog extends JDialog {
    private static final List<ProcessingRenderer> renderers = new ArrayList<ProcessingRenderer>();

    static {
        renderers.add(new ProcessingRenderer("Java 2D", PConstants.JAVA2D));
        renderers.add(new ProcessingRenderer("OpenGL", PConstants.OPENGL));
        renderers.add(new ProcessingRenderer("P2D", PConstants.P2D));
        renderers.add(new ProcessingRenderer("P3D", PConstants.P3D));
        renderers.add(new ProcessingRenderer("PDF", PConstants.PDF));
        renderers.add(new ProcessingRenderer("DXF", PConstants.DXF));
    }

    public boolean success = false;
    public final JTextField widthField, heightField, frameRateField;
    private final JComboBox rendererField;
    private final JCheckBox drawBackgroundField;
    private final ColorWell backgroundField;
    private final JCheckBox smoothField;


    public ScenePropertiesDialog(SceneDocument document) {
        super(document);
        setTitle("Scene Properties");
        setModal(true);
        Scene scene = document.getScene();

        MigLayout layout = new MigLayout("wrap 2");
        setLayout(layout);
        widthField = new JTextField(scene.getProperty(Scene.PROCESSING_WIDTH));
        heightField = new JTextField(scene.getProperty(Scene.PROCESSING_HEIGHT));
        drawBackgroundField = new JCheckBox("Draw background", scene.getProperty(Scene.PROCESSING_DRAW_BACKGROUND).equals("true"));
        backgroundField = new ColorWell();
        rendererField = new JComboBox(renderers.toArray());
        rendererField.setSelectedItem(findRenderer(scene.getProperty(Scene.PROCESSING_RENDERER)));
        frameRateField = new JTextField(scene.getProperty(Scene.PROCESSING_FRAME_RATE));
        smoothField = new JCheckBox("Smooth edges", scene.getProperty(Scene.PROCESSING_SMOOTH).equals("true"));
        add(new JLabel("Width"));
        add(widthField, "w 150!");
        add(new JLabel("Height"));
        add(heightField, "w 150!");
        add(new JLabel("Background"));
        add(drawBackgroundField);
        add(new JLabel("Background color"));
        add(backgroundField);
        add(new JLabel("Renderer"));
        add(rendererField, "w 150!");
        add(new JLabel("Frame Rate"));
        add(frameRateField, "w 150!");
        add(new JLabel("Smooth"));
        add(smoothField, "w 150!");
        JButton applyButton = new JButton(new ApplyAction());
        applyButton.setDefaultCapable(true);
        add(applyButton, "gaptop 15, span, align right");
        pack();
    }

    public String getFieldValue(String key) {
        if (key.equals(Scene.PROCESSING_WIDTH)) {
            return widthField.getText();
        } else if (key.equals(Scene.PROCESSING_HEIGHT)) {
            return heightField.getText();
        } else if (key.equals(Scene.PROCESSING_DRAW_BACKGROUND)) {
            return String.valueOf(drawBackgroundField.isSelected());
        } else if (key.equals(Scene.PROCESSING_BACKGROUND_COLOR)) {
            Color c = backgroundField.getColor();
            return String.format("%s,%s,%s", c.getRed(), c.getGreen(), c.getBlue());
        } else if (key.equals(Scene.PROCESSING_RENDERER)) {
            return ((ProcessingRenderer)rendererField.getSelectedItem()).className;
        } else if (key.equals(Scene.PROCESSING_FRAME_RATE)) {
            return frameRateField.getText();
        } else if (key.equals(Scene.PROCESSING_SMOOTH)) {
            return String.valueOf(smoothField.isSelected());
        } else {
            throw new RuntimeException("Unknown field " + key);
        }
    }

    private class ApplyAction extends AbstractAction {
        private ApplyAction() {
            super("Apply");
        }

        public void actionPerformed(ActionEvent e) {
            success = true;
            setVisible(false);
        }
    }

    public ProcessingRenderer findRenderer(String className) {
        for (ProcessingRenderer r : renderers) {
            if (r.className.equals(className)) {
                return r;
            }
        }
        return null;
    }

    public static class ProcessingRenderer {
        public final String displayName;
        public final String className;

        public ProcessingRenderer(String displayName, String className) {
            this.displayName = displayName;
            this.className = className;
        }

        @Override
        public String toString() {
            return this.displayName;
        }
    }
}
