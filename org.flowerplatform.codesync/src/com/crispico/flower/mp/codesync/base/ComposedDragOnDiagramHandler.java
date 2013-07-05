package com.crispico.flower.mp.codesync.base;

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
