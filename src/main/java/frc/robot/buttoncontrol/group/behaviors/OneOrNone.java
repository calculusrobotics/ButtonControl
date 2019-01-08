package frc.robot.buttoncontrol.group.behaviors;

import java.util.ArrayList;

import frc.robot.buttoncontrol.EButton;
/**
 * If one button is true, run the command.
 * If multiple are true, change nothing.
 */
public class OneOrNone extends Behavior {
    public void handle(ArrayList<EButton> buttons) {
        // if 1 button is pressed, then it is handled automatically by the ButtonGroup
        // otherwise, nothing should be done with the buttons
    }
}