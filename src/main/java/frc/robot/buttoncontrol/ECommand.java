package frc.robot.buttoncontrol;

import edu.wpi.first.wpilibj.command.Command;

public abstract class ECommand extends Command {
    protected void execute() {}
    protected void interrupted() {
        end();
    }

    // even though they are not used here, this is a good place to keep this enum b/c it is related to Commands
    public enum CommandType {
        WHEN_PRESSED,
        WHILE_HELD,
        WHEN_RELEASED
    }

    public abstract void exec();
    public void onInterrupted() {
        end();
    }
}