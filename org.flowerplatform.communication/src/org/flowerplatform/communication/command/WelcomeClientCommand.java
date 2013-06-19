package org.flowerplatform.communication.command;

public class WelcomeClientCommand extends CompoundClientCommand {
	private boolean containsFirstTimeInitializations;
	
	public boolean isContainsFirstTimeInitializations() {
		return containsFirstTimeInitializations;
	}

	public void setContainsFirstTimeInitializations(
			boolean containsFirstTimeInitializations) {
		this.containsFirstTimeInitializations = containsFirstTimeInitializations;
	}
}
