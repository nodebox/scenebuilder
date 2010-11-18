package nodebox.builtins.utility;

import nodebox.node.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Description("Trigger a true value for the duration of one frame, otherwise false.")
@Category("Utility")
public class Trigger extends Node {
    public boolean output = false;
    public BooleanPort pOut = new BooleanPort(this, "out", Port.Direction.OUTPUT);

    public void execute(Context context, float time) {
        pOut.set(output);
        output = false;
    }

    @Override
    public JComponent createCustomEditor() {
        final JButton b = new JButton("Trigger");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                output = true;
            }
        });
        return b;
    }
}
