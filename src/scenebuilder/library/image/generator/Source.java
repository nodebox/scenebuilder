package scenebuilder.library.image.generator;

import scenebuilder.gui.FilePicker;
import scenebuilder.model.Context;
import scenebuilder.model.GLImage;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Source extends Node implements ActionListener {

    public static final String PORT_IMAGE = "image";
    private FilePicker picker;
    private File fileToLoad;

    public Source() {
        super(Node.Function.Generator);
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Image");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Read an image from a file.");
        addOutputPort(Port.Type.IMAGE, PORT_IMAGE);
    }

    public JComponent createEditorComponent() {
        JPanel contents = new JPanel();
        picker = new FilePicker();
        contents.add(picker);
        picker.addActionListener(this);
        return contents;
    }

    public void loadImage(File file) {
        fileToLoad = file;
    }

    @Override
    public boolean execute(Context context, double time) {
        if (fileToLoad != null) {
            setValue(PORT_IMAGE, GLImage.load(fileToLoad));
            fileToLoad = null;
        }
        return true;
    }

    public void actionPerformed(ActionEvent e) {
        loadImage(picker.getFile());
    }
}
