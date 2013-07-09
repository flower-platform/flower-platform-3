package com.crispico.flower.mp.codesync.base;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.flowerplatform.communication.channel.CommunicationChannel;


/**
 * @author Mariana
 */
public interface ICodeSyncAlgorithmRunner {

	void runCodeSyncAlgorithm(IProject project, IFile file, String technology, CommunicationChannel communicationChannel);
	
}
