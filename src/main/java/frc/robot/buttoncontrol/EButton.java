package frc.robot.buttoncontrol;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

// TODO: consider extending JoystickButton instead of making an encapsulation class for it
public class EButton {
	private final Joystick CONTROLLER;
	private final int ID;
	private final JoystickButton BUTTON;
	
	
	
	
	
	public EButton(Joystick controller, int id) {
		CONTROLLER = controller;
		ID = id;
		
		BUTTON = new JoystickButton(CONTROLLER, ID);
	}
	
	
	
	public int getID() {
		return ID;
	}
	
	public void whenPressed(ECommand cmd) {
		cmd.setButtonID(ID);
		cmd.setType(1);
		
		BUTTON.whenPressed(cmd);
	}
	
	public void whenReleased(ECommand cmd) {
		cmd.setButtonID(ID);
		cmd.setType(-1);
		
		BUTTON.whenPressed(cmd);
	}
	
	public void whileHeld(ECommand cmd) {
		cmd.setButtonID(ID);
		cmd.setType(1);
		
		BUTTON.whileHeld(cmd);
	}
	
	public boolean get() {
		return BUTTON.get();
	}
}
