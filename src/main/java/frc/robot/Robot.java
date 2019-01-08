/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.buttons.Button;
//import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.buttoncontrol.EButton;
import frc.robot.buttoncontrol.group.ButtonGroup;
import frc.robot.buttoncontrol.group.ButtonManager;
// TODO: no wildcard imports
import frc.robot.buttoncontrol.group.behaviors.*;

/**
 * *****************************************************************************
 * *****************************************************************************
 * *****************************************************************************
 * BIT BUCKETS: This example creates a single joystick (controller) instance
 * to allow access to buttons that we will map to commands that drive a subsystem
 * The PURPOSE of this example is to demonstrate the potential conflicts of using
 * this built-in mapping facility. We will have another project that demonstrates
 * one way of preventing the conflicts.
 * 
 * The controls for this demo will NOT be using definitions from the previously
 * created Operator Interface (OI) used for other Bit Bucket projects. The OI 
 * creates the driver and operator controllers in a way that hides the interfaces
 * that have caused problems.
 * 
 * Yes, we are intentionally writing this code to demonstrate the problems.
 * 
 * The control will be defined as follows:
 * 
 * 		The POV buttons on the left side of a PS4 controller will be used to
 * 		command FORWARD, BACKWARD, LEFT, and RIGHT drive.
 * 
 * 		The buttons will be mapped to Commands that drive a Subsystem to go
 * 		FORWARD, BACKWARD, LEFT, RIGHT, and STOP. The drive speed will be a fixed
 * 		number just to keep the demo simple.
 * 
 * *****************************************************************************
 * *****************************************************************************
 * *****************************************************************************
 */


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	public static DriveSubsystem driveSubsystem;
	
	private Joystick controller;

	private EButton forward;
	private EButton backward;
	private EButton right;
	private EButton left;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		driveSubsystem = new DriveSubsystem();
		controller = new Joystick(0);	// MAGIC NUMBERS are evil
		
		forward  = new EButton(controller, PS4Constants.TRIANGLE.getValue(), "Forward");
		backward = new EButton(controller, PS4Constants.CROSS.getValue(), "Backward");
		right    = new EButton(controller, PS4Constants.CIRCLE.getValue(), "Right");
		left     = new EButton(controller, PS4Constants.SQUARE.getValue(), "Left");
		
		// NOTE NOTE NOTE:
		// This is a terrible way to drive a robot, but easily demonstrates
		// the problems with buttons
		forward.whenPressed(new MoveForward());
		forward.whenReleased(new Stop());
		
		backward.whenPressed(new MoveBackward());
		backward.whenReleased(new Stop());
		
		right.whileHeld(new TurnRight());
		
		left.whileHeld(new TurnLeft());

		ButtonGroup group = new ButtonGroup(4, new OneOrNone());
		group.add(forward);
		group.add(backward);
		group.add(left);
		group.add(right);

		ButtonManager.add(group);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run(); // run the commands on deck, now
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
