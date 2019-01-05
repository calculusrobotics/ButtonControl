package frc.robot.buttoncontrol.group.behaviors;

import frc.robot.buttoncontrol.EButton;
import frc.robot.buttoncontrol.group.ButtonManager;

/**
 * If one button is true, run the command.
 * If two are true and one was just pressed in the previous iteration, run the old button
 * If not, do nothing/interrupt any commands
 */
public class OneOrOld extends Behavior {
    public void handle(int true_ran, int true_expected, EButton lastTrueB, EButton b, EButton[] buttons) {
        // if one button is pressed, run the corresponding "true" command for this button
        if (true_expected == 1) {
            b.requestTrue();
        // if 2 are pressed...
        } else if (true_expected == 2) {
            int it = ButtonManager.getIteration();



            // we want to check if the other pressed button was last pressed before this one
            // however, to make things more efficient, we instead check when the last true button is being processed
            if (true_ran == true_expected) {
                // get the iteration when the other true button's true command was last called
                int lastOtherIteration = lastTrueB.getLTCI();
                // get the iteration when this true button's true was last called
                int lastIteration = b.getLTCI();
                // if one of these two is equal to iteration-1, then it was called in the previous iteration

                // if the other true button's true command was called in the last iteration, call the other button
                if (lastOtherIteration == it - 1) {
                    lastTrueB.requestTrue();
                // if this true button's true command was called in the last iteration, call this button
                } else if (lastIteration == it - 1) {
                    b.requestTrue();
                }
            }
        }
    }
}