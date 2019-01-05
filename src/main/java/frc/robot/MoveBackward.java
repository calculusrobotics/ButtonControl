package frc.robot;

import frc.robot.buttoncontrol.ECommand;

/**
 *
 */
public class MoveBackward extends ECommand {

    public MoveBackward() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.printf("Initialize MOVE BACKWARD\n");
    }

    // Called repeatedly when this Command is scheduled to run
    public void exec() {
        System.out.printf("Execute MOVE BACKWARD\n");
    	Robot.driveSubsystem.driveBackward();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.printf("End MOVE BACKWARD\n");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    public void onInterrupted() {
        System.out.printf("Interrupted MOVE BACKWARD\n");
    }
}
