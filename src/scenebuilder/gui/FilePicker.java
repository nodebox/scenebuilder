package scenebuilder.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;

public class FilePicker extends JPanel implements ActionListener {

    private static final Font SMALL_BOLD_FONT = new Font(Font.DIALOG, Font.BOLD, 11);


    private File file;
    private JTextField fileField;
    private JButton chooseButton;

    public FilePicker() {
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        fileField = new JTextField();
        fileField.putClientProperty("JComponent.sizeVariant", "small");
        fileField.setPreferredSize(new Dimension(150, 19));
        fileField.setEditable(false);
        fileField.setFont(SMALL_BOLD_FONT);
        chooseButton = new JButton("Choose...");
        chooseButton.putClientProperty("JButton.buttonType", "gradient");
        chooseButton.putClientProperty("JComponent.sizeVariant", "small");
        chooseButton.setPreferredSize(new Dimension(30, 27));
        chooseButton.addActionListener(this);
        add(fileField);
        add(chooseButton);
    }

    /**
     * Adds the specified action listener to receive
     * action events from this textfield.
     *
     * @param l the action listener to be added
     */
    public synchronized void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
     * Removes the specified action listener so that it no longer
     * receives action events from this textfield.
     *
     * @param l the action listener to be removed
     */
    public synchronized void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this JTextField with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public synchronized ActionListener[] getActionListeners() {
        return (ActionListener[]) listenerList.getListeners(
                ActionListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created.
     * The listener list is processed in last to
     * first order.
     */
    protected void fireActionPerformed() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        int modifiers = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        ActionEvent e =
                new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                        file.getAbsolutePath(),
                        EventQueue.getMostRecentEventTime(), modifiers);

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        chooseButton.setEnabled(enabled);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        fileField.setText(file.toString());
    }

    public String acceptedExtensions() {
        return "*";
    }

    public String acceptedDescription() {
        return "All files";
    }

    public void actionPerformed(ActionEvent e) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        File f = SwingUtils.showOpenDialog(frame, "", acceptedExtensions(), acceptedDescription());
        if (f != null) {
            setFile(f);
            File currentWorkingDirectory = new File(System.getProperty("user.dir"));
            if (currentWorkingDirectory != null) {
                String relativePath = SwingUtils.getRelativePath(f, currentWorkingDirectory);
                fireActionPerformed();
            }
        }
    }

}
