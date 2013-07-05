package com.crispico.flower.mp.codesync.code;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.flowerplatform.communication.channel.CommunicationChannel;

import com.crispico.flower.mp.codesync.base.ICodeSyncAlgorithmRunner;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class CodeSyncAlgorithmRunner implements ICodeSyncAlgorithmRunner {

	@Override
	public void runCodeSyncAlgorithm(IProject project,
			IFile file, String technology, CommunicationChannel communicationChannel) {
		
		CodeSyncElement cse = CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, file, technology, communicationChannel);
	}

}
