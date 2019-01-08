package frc.robot.buttoncontrol;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Custom Button class to use for mutually E-xclusive Buttons
 */
public class EButton {
    /** First available ID for the next EButton */
    private static int nextID = 0;
    /** ID of this button */
    private final int ID;



    /** Internal "under the hood" WPILib button */
    private JoystickButton button;
    // #TODO: should we log button value/action history? (like presses/releases and command runnings)
    /** Name of the button (used for logging button values and actions) */
    private final String NAME;



    // ECommands to run (under the correct circumstances)

    /** Intended ECommand to call when the button is pressed (may not be called - determined by Behaviors). */
    private ECommand cmd_whenPressed;
    /** Intended ECommand to call when the button is held (may not be called - determined by Behaviors). */
    private ECommand cmd_whileHeld;
    /** Intended ECommand to call when the button is released (always called when it is released). */
    private ECommand cmd_whenReleased;



    // State of the button in the current iteration (these are stored to prevent having to recalculate them multiple times)

    // value of the button in the current iteration
    private boolean value;
    // whether a true command Was Just Run in the last iteration
    private boolean wjr;
    /**
     * Last True Called Iteration - the last iteration when a "true" command (whenPressed/whileHeld was called for this button).
     */
    private int ltci;



    /** ArrayList  of IDs of the ButtonGroups this button is in. */
    private ArrayList<Integer> groupIDs;

    
    /**
     * Number of requests in the current iteration to call the whenReleased command.
     * 
     * If, at the end of the iteration, this is at least 1, then the whenReleased command is called because all groups should have
     *     a consensus for releasing the button due to how the ButtonGroups handle released buttons (Behaviors handle true buttons).
     */
    private int releaseRequests;
    /**
     * Number of requests in the current iteration to call either whenPressed/whileHeld.
     * 
     * If, at the end of the iteration, this is equal to the number of groups the button is in, then either whenPressed or whileHeld
     *     is called depending on the button's previous state (specifically, if a true command was called in the last iteration
     *     (LTCI = iteration - 1), whileHeld is called, otherwise whenPressed is called).
     * However, if this is less than the number of groups the button is in, at least one group decided the button's true command
     *     should NOT run, so running a true command could have an effect that we are trying to avoid with mutual exclusion, so the
     *     true command that was supposed to be run is interrupted.
     */
    private int trueRequests;





    /**
     * Create a new Exclusive Button
     * 
     * @param joystick
     * @param id
     * @param name Name of the EButton (used for logging button changes)
     */
    public EButton(GenericHID joystick, int id, String name) {
        // set this button's ID
        ID = nextID;
        // make sure every button has a unique ID
        nextID++;

        // create the under the hood WPILib button
        button = new JoystickButton(joystick, id);

        // set the name of the button
        NAME = name;

        // initialize ArrayList of IDs for the groups the button is in (IDs are added as the button is added to groups)
        groupIDs = new ArrayList<Integer>();

        // number of requests to run a true command at the end of the current iteration
        trueRequests = 0;


        value = false;
        wjr = false;
        // has to be less than 0 because we start iterations at 1, and we don't want the ButtonManager to think a command
        // was called in "the previous" iteration - that is, 0 - when its the first iteration.
        ltci = -1;
    }





    /**
     * Set the intended command to call when the button is pressed.
     * If the button is pressed but other buttons in overlapping ButtonGroups are also being pressed/held, then this command
     *     may not actually get run: it depends on the logic implemented by the Behaviors of the ButtonGroups.
     * If a button is being held but the whenPressed command has not been run (because of the above reason), then this
     *     command may get run, once again depending on the Behavior logic.
     * 
     * @param cmd intended whenPressed command.
     */
    public void whenPressed(ECommand cmd) {
        cmd_whenPressed = cmd;
    }

    /**
     * <b>BUTTON CONTROL</b><br>
     * 
     * Get the intended command to call when the button is pressed.
     * 
     * @return intended whenPressed command
     */
    public ECommand getWhenPressed() {
        return cmd_whenPressed;
    }

    /**
     * <b>BUTTON CONTROL</b><br>
     * 
     * Set the actual WPILib command (should be a ButtonManager.Update) that gets sent to the internal WPILib button.
     * 
     * @param cmd 
     */
    public void e_whenPressed(Command cmd) {
        button.whenPressed(cmd);
    }



    /**
     * Set the intended command to call while the button is being continuously held.
     * If the button is held but other buttons in overlapping ButtonGroups are also being pressed/held, then this command
     *     may not actually get run: it depends on the logic implemented by the Behaviors of the ButtonGroups.
     * If a button is being held but the whenPressed command has not been run (because of the above reason), then this
     *     command may get run, once again depending on the Behavior logic.
     * 
     * @param cmd intended whenPressed command.
     */
    public void whileHeld(ECommand cmd) {
        cmd_whileHeld = cmd;
    }

    public ECommand getWhileHeld() {
        return cmd_whileHeld;
    }

    public void e_whileHeld(Command cmd) {
        button.whileHeld(cmd);
    }



    public void whenReleased(ECommand cmd) {
        cmd_whenReleased = cmd;
    }

    public ECommand getWhenReleased() {
        return cmd_whenReleased;
    }

    public void e_whenReleased(Command cmd) {
        button.whenReleased(cmd);
    }



    public void updateState(int it) {
        value = button.get();
        wjr = (ltci == it - 1);
    }

    public boolean get() {
        return value;
    }

    public boolean wasJustRun() {
        return wjr;
    }



    public void addGroupID(int groupID) {
        groupIDs.add(groupID);
    }

    public int getGroupLength() {
        return groupIDs.size();
    }

    public int getGroupID(int i) {
        return groupIDs.get(i);
    }



    public int getID() {
        return ID;
    }



    public void setLTCI(int it) {
        ltci = it;
    }

    public int getLTCI() {
        return ltci;
    }





    public void requestRelease() {
        releaseRequests++;
    }

    public int getReleaseRequests() {
        return releaseRequests;
    }



    public void requestTrue() {
        trueRequests++;
    }

    public int getTrueRequests() {
        return trueRequests;
    }



    public void resetRequests() {
        releaseRequests = 0;
        trueRequests = 0;
    }



    public String getName() {
        return NAME;
    }
}