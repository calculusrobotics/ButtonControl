package frc.robot.buttoncontrol.group;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.buttoncontrol.EButton;
import frc.robot.buttoncontrol.ECommand;

public class ButtonManager {
    // groups the Manager handles
    private static ArrayList<ButtonGroup> groups = new ArrayList<ButtonGroup>();
    // buttons the Manager handles
    private static ArrayList<EButton> buttons = new ArrayList<EButton>();
    // IDs of the buttons the Manager handles
    private static ArrayList<Integer> buttonIDs = new ArrayList<Integer>();
    


    // number of Updates processed in this iteration
    private static int ran = 0;
    // number of Updates to be processed in this iteration
    private static int expected = 0;
    // iteration number (doesn't have to be the same as that for the driver station. This increments when there was some "change")
    private static int iteration = 1;



    // number of groups that have finished processing all their commands
    private static int pings = 0;





    /**
     * Internal way to handle button commands.
     * ECommands are only "dummy" commands, what is actually run by WPILib are these Updates which subsequently can run the ECommands
     */
    private static class Update extends Command {
        private final int INDEX;



        /**
         * Create a new Update command to internally handle mutual exclusion for a given button of the group
         * 
         * @param type what type of command is this (WHEN_PRESSED, WHILE_HELD, WHEN_RELEASED)
         * @param index index of the button this command will be for in the buttons array
         */
        private Update(int index) {
            INDEX = index;
        }

        // called when there is an "update" to take care of, such as a button was pressed, is continuing to be held, or is released
        protected void execute() {
            // if this is the first button to run for this iteration...
            if (ran == 0) {
                // set the expected number of buttons for this iteration
                setExpected();

                sendIteration();
            }

            ran++;


            sendToGroups(INDEX);


            // if all buttons have been processed...
            if (ran == expected) {
                ran = 0;
                expected = 0;

                iteration++;
            }
        }

        protected boolean isFinished() {
            return false;
        }
    }

    private static void setExpected() {
        // expected is assumed to be at 0 before the start of a new iteration

        // loop through each button and add 1 to true_expected per true button and 1 to false_expected per ex-true false button
        for (int i = 0; i < buttons.size(); i++) {
            EButton b = buttons.get(i);

            if (b.get()) {
                expected++;
            } else if (b.wasJustRun()) {
                expected++;
            }
        }
    }

    public static void add(ButtonGroup group) {
        group.setID(groups.size());
        groups.add(group);
        

        for (int i = 0; i < group.getLength(); i++) {
            EButton b = group.getButton(i);
            int id = b.getID();

            if (buttonIDs.indexOf(id) == -1) {
                int index = buttons.size();

                buttons.add(b);
                buttonIDs.add(id);

                b.e_whenPressed(new Update(index));
                b.e_whileHeld(new Update(index));
                b.e_whenReleased(new Update(index));
            }
        }
    }



    private static void sendToGroups(int index) {
        EButton b = buttons.get(index);

        b.updateState(iteration);

        for (int i = 0; i < b.getGroupLength(); i++) {
            groups.get(b.getGroupID(i)).send(b);
        }
    }

    private static void sendIteration() {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setExpected();
        }
    }



    public static void ping() {
        pings++;

        if (pings == groups.size()) {
            pings = 0;

            System.out.println("ITERATION " + iteration);

            for (int i = 0; i < buttons.size(); i++) {
                EButton b = buttons.get(i);

                if (b.getReleaseRequests() > 0) {
                    b.getWhenReleased().exec();

                    System.out.println("BUTTON " + b.getName() + ": false | RELEASED");
                } else if (b.get()) {
                    ECommand c;

                    System.out.print("BUTTON " + b.getName() + ": true | ");

                    if (b.wasJustRun()) {
                        c = b.getWhileHeld();

                        System.out.print("HELD");
                    } else {
                        c = b.getWhenPressed();

                        System.out.print("PRESSED");
                    }

                    if (b.getTrueRequests() == b.getGroupLength()) {
                        c.exec();

                        b.setLTCI(iteration);
                    } else {
                        c.onInterrupted();

                        System.out.println(" | IGNORED");
                    }
                }

                b.resetRequests();
            }
        }
    }
}