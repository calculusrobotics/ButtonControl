package frc.robot.buttoncontrol;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Custom Button class to use for mutually E-xclusive Buttons
 */
public class EButton {
    private static int nextID = 0;
    private final int ID;

    // under the hood button
    private JoystickButton button;
    // name of the button (used for logging button stuffs)
    // #TODO: should we log button "stuffs" history? (like presses/releases and command runnings)
    private final String NAME;

    // ECommands to run (under the correct circumstances)
    private ECommand cmd_whenPressed;
    private ECommand cmd_whileHeld;
    private ECommand cmd_whenReleased;

    private boolean prevState;

    private int ltci;

    private ArrayList<Integer> groupIDs;

    private int releaseRequests;
    private int trueRequests;



    /**
     * Create a new Exclusive Button
     * 
     * @param joystick
     * @param id
     * @param name Name of the EButton (used for logging button changes)
     */
    public EButton(GenericHID joystick, int id, String name) {
        ID = nextID;
        nextID++;

        // create the under the hood WPILib button
        button = new JoystickButton(joystick, id);

        NAME = name;

        prevState = false;

        groupIDs = new ArrayList<Integer>();

        trueRequests = 0;

        ltci = -1;
    }



    public boolean getPrevState() {
        return prevState;
    }

    public void setPrevState() {
        prevState = button.get();
    }





    public void whenPressed(ECommand cmd) {
        cmd_whenPressed = cmd;
    }

    public ECommand getWhenPressed() {
        return cmd_whenPressed;
    }

    public void e_whenPressed(Command cmd) {
        button.whenPressed(cmd);
    }



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



    public boolean get() {
        return button.get();
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
}