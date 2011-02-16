package nodebox.builtins.utility;

import nodebox.node.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Description("Trigger a true value for the duration of one frame, otherwise false.")
@Category("Utility")
@ExternalInput
public class Trigger extends Node {
    public boolean triggered = false;
    public BooleanPort pTriggered = new BooleanPort(this, "triggered", Port.Direction.OUTPUT);

    public void execute(Context context, float time) {
        pTriggered.set(triggered);
        triggered = false;
    }

    @Override
    public JComponent createCustomEditor() {
        final JButton b = new JButton("Trigger");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                triggered = true;
            }
        });
        return b;
    }
}
