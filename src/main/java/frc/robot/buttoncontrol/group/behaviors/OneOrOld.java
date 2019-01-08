package frc.robot.buttoncontrol.group.behaviors;

import java.util.ArrayList;

import frc.robot.buttoncontrol.EButton;

/**
 * If one button is true, run the command.
 * If two are true and one was just pressed in the previous iteration, run the old button
 * If not, do nothing/interrupt any commands
 */
public class OneOrOld extends Behavior {
    public void handle(ArrayList<EButton> buttons) {
        // if 1 button is pressed, then it is handled automatically by the ButtonGroup
        // if 2 buttons are pressed...
        if (buttons.size() == 2) {
            EButton a = buttons.get(0);
            EButton b = buttons.get(1);

            // if button A's true command was called in the last iteration, keep calling it
            if (a.wasJustRun()) {
                a.requestTrue();
            // if button B's true command was called in the last iteration, keep calling it
            } else if (b.wasJustRun()) {
                b.requestTrue();
            }
        }
    }
}