package com.crispico.flower.mp.codesync.base;

import java.util.Collection;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.View;

/**
 * @author Cristi
 */
public interface IDragOnDiagramHandler {

	public boolean handleDragOnDiagram(Collection<?> draggedObjects, Diagram diagram, View viewUnderMouse, Object layoutHint, CommunicationChannel communicationChannel);
	
}
