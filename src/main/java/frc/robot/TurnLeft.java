package frc.robot;

import frc.robot.buttoncontrol.ECommand;

/**
 *
 */
public class TurnLeft extends ECommand {

    public TurnLeft() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveSubsystem);
    	inGroup("drive");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//System.out.printf("TurnLeft INIT\n");
    }


	protected void execute() {
		if (!shouldRun()) {
    		return;
    	}

		System.out.println("TurnLeft");
        Robot.driveSubsystem.turnLeft();
	}
	
    // Called repeatedly when this Command is scheduled to run
    /*protected void execute() {
        Robot.driveSubsystem.turnLeft();
        System.out.printf("TurnLeft EXECUTE\n");
    }*/

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
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.printf("TurnLeft INTERRUPTED\n");
        end();
    }
}
