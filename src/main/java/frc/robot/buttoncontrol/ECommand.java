package frc.robot.buttoncontrol;

import edu.wpi.first.wpilibj.command.Command;

public abstract class ECommand extends Command {
	// group this command's button belongs to
	protected ButtonGroup group;
	// id of this command's button
	protected int buttonID;
	// type of this command, 1 = whenPressed/whileHeld, -1 = whenReleased
	protected int type;
	
	public void setButtonID(int id) {
		buttonID = id;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	protected void inGroup(String group) {
		this.group = ButtonGroupGroup.getGroup(group);
	}
	
	protected boolean shouldRun() {
		return group.shouldRun(buttonID, type);
	}
}
