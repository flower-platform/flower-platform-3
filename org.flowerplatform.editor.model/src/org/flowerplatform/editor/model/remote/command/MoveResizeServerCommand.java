package org.flowerplatform.editor.model.remote.command;

import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Location;

public class MoveResizeServerCommand extends AbstractEMFServerCommand {

	public String id;
	public int newX;
	public int newY;
	public int newHeight;
	public int newWidth;
	
	@Override
	public void executeCommand() {
		Location location = (Location) getEditableResource().getMainResource().getEObject(id);
		
		location.setX(newX);
		location.setY(newY);
		if (location instanceof Bounds) {
			if (newWidth != -1) {
				((Bounds) location).setWidth(newWidth);
			}
			if (newHeight != -1) {
				((Bounds) location).setWidth(newHeight);
			}
		}
		
	}

}
