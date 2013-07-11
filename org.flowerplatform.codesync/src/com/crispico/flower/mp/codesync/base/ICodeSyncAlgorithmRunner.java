package com.crispico.flower.mp.codesync.base;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.flowerplatform.communication.channel.CommunicationChannel;


/**
 * @author Mariana
 */
public interface ICodeSyncAlgorithmRunner {

	void runCodeSyncAlgorithm(IProject project, IResource resource, String technology, CommunicationChannel communicationChannel);
	
}
