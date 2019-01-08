package frc.robot.buttoncontrol.group.behaviors;

import java.util.ArrayList;

import frc.robot.buttoncontrol.EButton;

/**
 * Abstract behavior class for handling mutually exclusive commands
 */
public abstract class Behavior {    
    /**
     * Handle the true buttons of a group after they have all been received by the group.
     * This is always called when there are more than 1 true buttons in the group.
     * If there is only one, then it is automatically handled by the group by requesting the true command.
     * 
     * @param buttons buttons in the group
     */
    public abstract void handle(ArrayList<EButton> buttons);
}