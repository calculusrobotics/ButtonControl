package frc.robot.buttoncontrol.group;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.buttoncontrol.EButton;
import frc.robot.buttoncontrol.ECommand;

public class ButtonManager {
    private static ArrayList<ButtonGroup> groups = new ArrayList<ButtonGroup>();
    private static ArrayList<EButton> buttons = new ArrayList<EButton>();
    private static ArrayList<Integer> buttonIDs = new ArrayList<Integer>();
    
    private static int ran = 0;
    private static int expected = 0;
    private static int iteration = 0;

    private static int pings;



    /**
     * Internal way to handle button commands.
     * ECommands are only "dummy" commands, what is actually run by WPILib are these Updates which subsequently can run the ECommands
     */
    private static class Update extends Command {
        // What type of command is this (WHEN_PRESSED, WHILE_HELD, WHEN_RELEASED)
        private final ECommand.CommandType TYPE;

        private final int INDEX;



        /**
         * Create a new Update command to internally handle mutual exclusion for a given button of the group
         * 
         * @param type what type of command is this (WHEN_PRESSED, WHILE_HELD, WHEN_RELEASED)
         * @param index index of the button this command will be for in the buttons array
         */
        private Update(ECommand.CommandType type, int index) {
            TYPE = type;
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


            sendToGroups(INDEX, TYPE);


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
            } else if (b.getPrevState() == true) {
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

                b.e_whenPressed(new Update(ECommand.CommandType.WHEN_PRESSED, index));
                b.e_whileHeld(new Update(ECommand.CommandType.WHILE_HELD, index));
                b.e_whenReleased(new Update(ECommand.CommandType.WHEN_RELEASED, index));
            }
        }
    }



    private static void sendToGroups(int index, ECommand.CommandType type) {
        EButton b = buttons.get(index);

        for (int i = 0; i < b.getGroupLength(); i++) {
            groups.get(b.getGroupID(i)).send(b, type);
        }
    }

    private static void sendIteration() {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).sendIteration(iteration);
        }
    }

    public static int getIteration() {
        return iteration;
    }



    public static void ping() {
        pings++;

        if (pings == groups.size()) {
            pings = 0;

            for (int i = 0; i < buttons.size(); i++) {
                EButton b = buttons.get(i);

                if (b.getReleaseRequests() > 0) {
                    b.getWhenReleased().exec();
                } else if (b.get()) {
                    ECommand c;

                    if (b.getPrevState()) {
                        c = b.getWhileHeld();
                    } else {
                        c = b.getWhenPressed();
                    }

                    if (b.getTrueRequests() == b.getGroupLength()) {
                        c.exec();

                        b.setLTCI(iteration);
                    } else {
                        c.onInterrupted();
                    }
                }
            }
        }
    }
}