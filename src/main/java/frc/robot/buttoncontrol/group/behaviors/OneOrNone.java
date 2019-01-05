package frc.robot.buttoncontrol.group.behaviors;

import frc.robot.buttoncontrol.EButton;

/**
 * If one button is true, run the command.
 * If multiple are true, change nothing.
 */
public class OneOrNone extends Behavior {
    public void handle(int true_ran, int true_expected, EButton lastTrueB, EButton b, EButton[] buttons) {
        // if one button is pressed, run the corresponding "true" command for this button
        if (true_expected == 1) {
            b.requestTrue();
        }
    }
}