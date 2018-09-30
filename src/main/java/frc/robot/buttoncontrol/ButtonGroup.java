package frc.robot.buttoncontrol;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.buttons.JoystickButton;
//import frc.robot.buttoncontrol2.EButton;

// TODO: if multiple commands are to be run which require the same subsystem, interruption may occur, which will
// not occur as we expected from this class. We should find some way to fix this to have custom interrupt behavior,
// instead of by IDs
public class ButtonGroup {
	// TODO: is there any more efficient way to store this information?
	// map buttons to their priorities
	private HashMap<EButton, Integer> priorities;
	// button list in group
	private ArrayList<EButton> buttons;
	// map buttons to their ids
	private HashMap<Integer, EButton> ids;
	
	// name of the button group (i.e. "drive" for driving buttons)
	private final String NAME;
	// behavior of button group
	private behavior beh;
	
	// last button signature
	private int lastSignature = -1;
	
	/**
	 * Possible behaviors for ButtonGroups
	 */
	public static enum behavior {
		// do nothing when multiple buttons are pressed
		NONE,
		// define custom button priorities, if multiple are pressed, the highest priority one's Command is run
		PRIORITY
	}
	
	/**
	 * @param name name of the ButtonGroup (for example, "drive" for driving buttons)
	 */
	public ButtonGroup(String name) {
		priorities = new HashMap<EButton, Integer>();
		buttons = new ArrayList<EButton>();
		ids = new HashMap<Integer, EButton>();
		
		NAME = name;
		// default
		beh = behavior.NONE;
	}
	
	/**
	 * @param name name of the ButtonGroup (for example, "drive" for driving buttons)
	 * @param beh behavior for the ButtonGroup
	 */
	public ButtonGroup(String name, behavior beh) {
		priorities = new HashMap<EButton, Integer>();
		buttons = new ArrayList<EButton>();
		ids = new HashMap<Integer, EButton>();
		
		NAME = name;
		this.beh = beh;
	}
	
	/**
	 * Get the name of this ButtonGroup
	 * 
	 * @return name of this ButtonGroup
	 */
	public String getName() {
		return NAME;
	}
	
	
	
	/**
	 * Add buttons to this ButtonGroup
	 *  
	 * @param buts indefinite amount of buttons to add
	 */
	public void add(EButton... buts) {
		if (beh == behavior.PRIORITY) {
			System.out.println("Priorities are required for buttons!");
			return;
		}
		
		for (int i = 0; i < buts.length; i++) {
			// default priorities to 0 unless otherwise specified
			priorities.put(buts[i], 0);
			// add the button to the list of buttons
			buttons.add(buts[i]);
			ids.put(buts[i].getID(), buts[i]);
		}
	}
	
	/**
	 * Add a button to this ButtonGroup with its priority
	 * 
	 * @param button button to add
	 * @param priority the priority for <code>button</code>'s commands
	 */
	public void add(EButton button, int priority) {
		priorities.put(button, priority);
		buttons.add(button);
		ids.put(button.getID(), button);
	}
	
	/**
	 * Comparator used to sort EButtons by their priority in increasing order.<br>
	 * This is used to quickly check if any higher priority button's commands are to be run.
	 */
	private class PrioritySorter implements Comparator<EButton> {
		@Override
		public int compare(EButton o1, EButton o2) {
			int a = priorities.get(o1);
			int b = priorities.get(o2);
			
			return a - b;
		}
	}
	
	/**
	 * Get the ButtonGroup ready for usage
	 */
	public void init() {
		// sort the buttons for efficiency
		buttons.sort(new PrioritySorter());
		
		System.out.print(NAME + ": ");
		for (int i = 0; i < buttons.size(); i++) {
			System.out.print(priorities.get(buttons.get(i)) + ", ");
		}
		System.out.println();
	}
	
	/*
	 * TODO: should we consider making a Signature class?
	 * Pro: we could have methods for getting the signature at a bit instead of using bitwise operations in the code
	 * Con: more memory usage. The whole point of int signatures was to not use immutable Strings
	 * 
	 * TODO: instead of having all the bits we care about at the end, should we have them at the beginning?
	 * this would make the code cleaner
	*/
	
	/**
	 * Get the signature of the EButton's at the time the method is called.<br>
	 * The signature is returned as an integer, whose binary has the boolean values of each button encoded inside
	 * it. The i-th bit from the right represents the value of the i-th button by priority.
	 * 
	 * @return the signature of this group's buttons at the time the method is called
	 */
	private int getSignature() {
		// initialize to 32 0s (ints are 32 bits)
		int signature = 0;
		
		//System.out.println();
		
		//System.out.println(buttons.size() + " - SIZE");
		for (int i = 0; i < buttons.size(); i++) {
			EButton button = buttons.get(i);
			
			boolean held = button.get();
			//System.out.println(button.getID() + " -> " + held);
			
			// right shift by a bit, leaving a 0 at the last bit 
			signature <<= 1;
			if (held) {
				// if the current button is held, put a 1 at the end
				// 1 is 63 0s followed by a 1, and x | 0 = x, so the rest of the signature is unchanged
				// the last bit is changed to 0 because 0 | 1 = 1
				signature |= 1;
			}
		}
		//System.out.println();
		
		return signature;
	}
	
	/**
	 * Return a String representation of the 32 bits that make up an integer
	 * 
	 * @param bin number to convert to binary
	 * @return 32 bit representation of <code>bin</code>
	 */
	private static String binToStr(int bin) {
		String ret = "";
		
		for (int i = 31; i >= 0; i--) {
			// 1 right shifted i times will have a 1 followed by i 0s.
			// by iterating from i = 31 to i = 0, this yields in every one of the 32 bits being a 1 in 1 << i
			//     at some i
			// this loop will give 1 << i with 1 at the first, then second, then third, ... bits
			
			// by anding it with bin, we can get the bit at the first, second, third, ... position
			// iff bin at the position is 0, then bin & (1 << i) = 0
			// iff bin at the position is 1, then bin & (1 << i) = 1 followed by a bunch of 0s, which isnt 0
			ret += ((bin & (1 << i)) == 0) ? 0 : 1;
		}
		
		return ret;
	}
	
	/**
	 * Determine if a ECommand should run given the values of other buttons
	 * 
	 * @param id id of the button the ECommand belongs to
	 * @param type type of the ECommand calling this (1 = whenPressed/whileHeld, -1 = whenReleased)
	 * @return whether or not the ECommand which called this method should run
	 */
	public boolean shouldRun(int id, int type) {
		// get the signature
		int signature = getSignature();
		
		// if the signature changed, log it to console
		
		// TODO: later, we should log it to a file to keep track of signatures (in a cleaner way then by priority,
		// maybe by name?)
		if (signature != lastSignature) {
			lastSignature = signature;
			System.out.println(binToStr(signature));
		}

		
		
		// get the EButton whose command ran this method
		EButton button = ids.get(id);
		// get its index is the button list
		int index = buttons.indexOf(button);
		
		
		
		// if it was just released and it is a whenReleased command, then always do the command
		// TODO: do we want this behavior?
		if (button.get() == false && type == -1) {
			return true;
		}
		/*
		 * TODO: In the SnoBot simulator, when the forward and backward buttons are pressed (both are whenPressed
		 * commands), the command will keep running in an infinite loop, even when they are released, until a
		 * whileHeld command is run. So I put this safety in so that if nothing is pressed, nothing should happen.
		 * We should find the source of this behavior and fix it
		 */
		if (signature == 0) {
			return false;
		}
		
		//System.out.println("sig: " + binToStr(signature));
		
		if (beh == behavior.NONE) {
			//System.out.println(index);
			
			//System.out.println("lshift: " + (buttons.size() - index - 1));
			
			// start with just 0s except for a 1 at the location of the button's value in the signature
			// not it, so that it is just 1s except for a 0 at that location
			int n = ~(1 << (buttons.size() - index - 1));
			//System.out.println("n: " + binToStr(n));
			

			//System.out.println("sig & n: " + binToStr(signature & n));
			
			// if any other button is pressed, it should not run
			// signature & n will have all other bits unaffected (x | 1 = x) except for the one of this button,
			// which will be converted to 0. So if signature & n = 0, then nothing else is pressed. If it isn't,
			// it means another button is pressed. Setting this bit to 0 has the effect of replacing it with a
			// an imaginary button that is not pressed
			return (signature & n) == 0;
		} else if (beh == behavior.PRIORITY) {
			// shift over signatures in such a way that anything of the same or lower priority is removed
			// TODO: how do we handle if multiple buttons have the same priority?
			signature = signature >> (buttons.size() - index);
			
			// if it is 0, nothing of higher priority is pressed, meaning its safe
			return signature == 0;
		}
		
		// Java likes to yell at you
		return false;
	}
}
