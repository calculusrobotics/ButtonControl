package frc.robot.buttoncontrol.group;

import java.util.ArrayList;

import frc.robot.buttoncontrol.EButton;
import frc.robot.buttoncontrol.group.behaviors.Behavior;

/**
 * A grouping of EButtons that are mutually exclusive in some way
 */
public class ButtonGroup {
    // EButtons in the ButtonGroup
    private EButton[] buttons;
    // adds in EButtons one by one at index i, incrementing i after adding the button
    private int i;
    // true buttons to process in the iteration by the Behavior
    // True Buttons to be Processed
    private ArrayList<EButton> tbp;



    // number of buttons processed by the ButtonGroup in this iteration so far
    private int ran;
    // number of buttons that will be processed in this iteration
    private int expected;
    // when ran = expected, this means all buttons have been processed, and a new iteration will start soon



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

        tbp = new ArrayList<EButton>(count + 1);



        // no commands have been processed yet
        ran = 0;
        // set expected to 0 so that setExpected() will work when it is called the first time
        expected = 0;



        // set the specified behavior
        this.beh = beh;
    }

    

    public void setID(int groupID) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addGroupID(groupID);
        }
    }





    /**
     * Set the expected number of true buttons for this iteration
     */
    public void setExpected() {
        // expected is assumed to be at 0 before the start of a new iteration

        // loop through each button and add 1 to true_expected per true button and 1 to false_expected per ex-true false button
        for (int i = 0; i < buttons.length; i++) {
            // if it IS  pressed/held
            // if it WAS pressed/held and had a command being run (if it gets to here, it means it was just released)
            //     however, if it was released but had no command being run during the previous iteration, nothing happens to it
            if (buttons[i].get() || buttons[i].wasJustRun()) {
                expected++;
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



    public void send(EButton b) {
        ran++;

        // if the button was just released and was active in the previous iteration, (request to) release it programatically
        if (b.get() == false && b.wasJustRun()) {
            // request executing the when released command
            b.requestRelease();
        } else {
            // add this button to the list of buttons that will need to be handled by the Behavior soon
            tbp.add(b);
        }

        // once every button has been received
        if (ran == expected) {
            int size = tbp.size();

            // if there were no released buttons that were previously active, then decide what to do with the pressed/held ones
            // this way, those buttons will have their whenReleased called without possibly contradicting the other ones.
            if (expected - size == 0) {
                // if there was only one true button, request for its true command to be called automatically without giving it to
                // the Behavior
                if (tbp.size() == 1) {
                    tbp.get(0).requestTrue();
                // if there were more true buttons, then we need the buttons to be sent to the Behavior to determine what to do
                } else if (tbp.size() != 0) {
                    // handle all the true buttons
                    beh.handle(tbp);
                }
            }
            

            // reset the number of processed and expected buttons for the next iteration
            ran = 0;
            expected = 0;

            // clear the true buttons to be processed
            tbp.clear();

            // this group is done, tell the Manager
            ping();
        }
    }



    private void ping() {
        ButtonManager.ping();
    }
}