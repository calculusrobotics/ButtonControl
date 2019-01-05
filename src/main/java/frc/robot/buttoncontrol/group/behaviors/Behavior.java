package frc.robot.buttoncontrol.group.behaviors;

import frc.robot.buttoncontrol.EButton;

/**
 * Abstract behavior class for handling mutually exclusive commands
 */
public abstract class Behavior {    
    /**
     * Handle a given true button for an iteration
     * 
     * @param true_ran number of true buttons processed so far
     * @param true_expected number of true buttons in the iteration
     * @param lastTrueIndex the index of the last true button processed
     * @param index the index of the current true button being processed
     * @param iteration the iteration number
     * @param buttons array of buttons in the ButtonGroup
     */
    public abstract void handle(int true_ran, int true_expected, EButton lastTrueB, EButton b, EButton[] buttons);
}