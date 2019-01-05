package frc.robot;

//import edu.wpi.first.wpilibj.command.Command;
import frc.robot.buttoncontrol.ECommand;

/**
 *
 */
public class TurnLeft extends ECommand {
    public TurnLeft() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

        // TODO: do we still need this if we're using ButtonMadness?
    	requires(Robot.driveSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.printf("TurnLeft INIT\n");
    }

    // Called repeatedly when this Command is scheduled to run
    public void exec() {
        Robot.driveSubsystem.turnLeft();
        System.out.printf("TurnLeft EXECUTE\n");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveSubsystem.stop();
        System.out.printf("TurnLeft END\n");
    }

    // Called when another command which requires one or more of the same
    // ButtonGroup is scheduled to run
    public void onInterrupted() {
        System.out.printf("TurnLeft INTERRUPTED\n");
        end();
    }
}
