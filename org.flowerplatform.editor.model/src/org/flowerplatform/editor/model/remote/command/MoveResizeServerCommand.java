/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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