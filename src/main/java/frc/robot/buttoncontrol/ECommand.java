package frc.robot.buttoncontrol;

import edu.wpi.first.wpilibj.command.Command;

public abstract class ECommand extends Command {
    protected void execute() {}
    protected void interrupted() {
        end();
    }

    public abstract void exec();
    public void onInterrupted() {
        end();
    }
}