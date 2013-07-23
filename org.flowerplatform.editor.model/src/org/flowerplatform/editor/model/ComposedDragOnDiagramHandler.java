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
package org.flowerplatform.editor.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.View;

/**
 * @author Cristi
 */
public class ComposedDragOnDiagramHandler implements IDragOnDiagramHandler {

	protected List<IDragOnDiagramHandler> delegateHandlers = new ArrayList<IDragOnDiagramHandler>();
	
	public void addDelegateHandler(IDragOnDiagramHandler delegate) {
		delegateHandlers.add(delegate);
	}
	
	@Override
	public boolean handleDragOnDiagram(Collection<?> draggedObjects, Diagram diagram, View viewUnderMouse, Object layoutHint, CommunicationChannel communicationChannel) {
		for (IDragOnDiagramHandler delegate : delegateHandlers) {
			if (delegate.handleDragOnDiagram(draggedObjects, diagram, viewUnderMouse, layoutHint, communicationChannel)) {
				return true;
			}
		}
		return false;
	}

}