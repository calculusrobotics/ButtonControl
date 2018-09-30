package frc.robot.buttoncontrol;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Auxiliary class to keep track of all ButtonGroups and get them by name (instead of passing them in to
 * Commands, the Commands can just be given a name of the ButtonGroup and will store it).
 * 
 * Unwisely named ButtonGroupGroup, a grouping of ButtonGroups
 */
public class ButtonGroupGroup {
	// all the names of stored ButtonGroups
	private static ArrayList<String> keys = new ArrayList<String>();
	// map from name to ButtonGroup
	private static HashMap<String, ButtonGroup> groups = new HashMap<String, ButtonGroup>();
	
	/**
	 * Add a ButtonGroup to the ButtonGroupGroup
	 * 
	 * @param group group to add
	 */
	public static void add(ButtonGroup group) {
		String name = group.getName();
		System.out.println("New group: " + name);
		
		// add it to the keys and map from name to group
		keys.add(name);
		groups.put(name, group);
	}
	
	/**
	 * Get the ButtonGroup with the specified name
	 * 
	 * @param name name of the ButtonGroup to get 
	 * @return ButtonGroup with that name
	 */
	public static ButtonGroup getGroup(String name) {
		System.out.println("Getting group " + name);
		
		return groups.get(name);
	}
	
	public static void init() {
		// loop through all the ButtonGroups and get them ready for usage
		for (int i = 0; i < keys.size(); i++) {
			groups.get(keys.get(i)).init();
		}
	}
}
