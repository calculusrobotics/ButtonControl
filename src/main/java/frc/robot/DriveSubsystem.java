package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Talon;
/**
 *
 */
public class DriveSubsystem extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	// Create some motor controllers for this subsystem
	private Talon leftFront;
	private Talon leftRear;
	private Talon rightFront;
	private Talon rightRear;

	final double POWER = 1.0;
	
	public DriveSubsystem()
	{
		leftFront  = new Talon(11);	// MAGIC NUMBERS are evil
		leftRear   = new Talon(12);
		rightFront = new Talon(13);
		rightRear  = new Talon(14);
	}
	
    public void initDefaultCommand() 
    {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void stop()
    {
    	leftFront.set(0.0);
    	leftRear.set(0.0);
    	rightFront.set(0.0);
    	rightRear.set(0.0);
    }
    
    public void driveForward()
    {
    	leftFront.set (POWER);		// More evil MAGIC NUMBERS, but just enough power to move slowly
    	leftRear.set  (POWER);
    	rightFront.set(POWER);
    	rightRear.set (POWER);
    }
    
    public void driveBackward()
    {
    	leftFront.set (-POWER);
    	leftRear.set  (-POWER);
    	rightFront.set(-POWER);
    	rightRear.set (-POWER);    	
    }
    
    public void turnRight()
    {
    	leftFront.set (POWER);
    	leftRear.set  (POWER);
    	rightFront.set(-POWER);
    	rightRear.set (-POWER);
    }
    
    public void turnLeft()
    {
    	leftFront.set (-POWER);
    	leftRear.set  (-POWER);
    	rightFront.set(POWER);
    	rightRear.set (POWER);
    }

}