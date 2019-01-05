package frc.robot.buttoncontrol.group;

import frc.robot.buttoncontrol.EButton;
import frc.robot.buttoncontrol.ECommand;
import frc.robot.buttoncontrol.group.behaviors.Behavior;

/**
 * A grouping of EButtons that are mutually exclusive in some way
 */
public class ButtonGroup {
    // EButtons in the ButtonGroup
    private EButton[] buttons;
    // adds in EButtons one by one at index i, incrementing i after adding the button
    private int i;



    // number of true buttons processed by the ButtonGroup in this iteration
    private int true_ran;
    // number of true buttons in this iteration
    private int true_expected;
    // number of false buttons processed by the ButtonGroup in this iteration
    private int false_ran;
    // number of false buttons in this iteration
    private int false_expected;
    // when ran = expected, this means all buttons have been processed, and a new iteration will start soon

    // last true button to get processed
    private EButton lastTrueB;



    // how the ButtonGroup handles multiple true buttons per iteration
    private Behavior beh;



    /**
     * Create a new ButtonGroup
     * 
     * @param count number of buttons in the group
     * @param beh Behavior of the ButtonGroup for handiling multiple true buttons per iteration
     */
    public ButtonGroup(int count, Behavior beh) {
        // set up the buttons array
        buttons = new EButton[count];
        // first index to be added is always 0
        i = 0;



        true_ran = 0;
        false_ran = 0;
        // set expected to 0 so that setExpected() will work when it is called the first time
        true_expected = 0;
        false_expected = 0;



        // set the specified behavior
        this.beh = beh;
    }

    

    public void setID(int groupID) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addGroupID(groupID);
        }
    }





    /**
     * Set the expected number of true buttons for this iterations
     */
    private void setExpected() {
        // expected is assumed to be at 0 before the start of a new iteration

        // loop through each button and add 1 to true_expected per true button and 1 to false_expected per ex-true false button
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].get()) {
                true_expected++;
            } else if (buttons[i].getPrevState() == true) {
                false_expected++;
            }
        }
    }



    

    /**
     * Add a button to the ButtonGroup
     * 
     * @param b EButton to add to group
     */
    public void add(EButton b) {
        // add it to the next index
        buttons[i] = b;
        // increment that index
        i++;
    }



    public int getLength() {
        return buttons.length;
    }

    public EButton getButton(int i) {
        return buttons[i];
    }



    public void send(EButton b, ECommand.CommandType type) {
        if (type == ECommand.CommandType.WHEN_RELEASED) {
            false_ran++;

            // request executing the when released command
            b.requestRelease();
        } else {
            true_ran++;

            beh.handle(true_ran, true_expected, lastTrueB, b, buttons);

            lastTrueB = b;
        }

        if (true_ran == true_expected && false_ran == false_expected) {
            // reset the number of processed and expected buttons for the next iteration
            true_ran = 0;
            false_ran = 0;

            true_expected = 0;
            false_expected = 0;

            ping();
        }
    }

    public void sendIteration(int it) {
        setExpected();
    }



    private void ping() {
        ButtonManager.ping();
    }
}