package com.crispico.flower.mp.codesync.code;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.flowerplatform.communication.channel.CommunicationChannel;

import com.crispico.flower.mp.codesync.base.ICodeSyncAlgorithmRunner;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class CodeSyncAlgorithmRunner implements ICodeSyncAlgorithmRunner {

	@Override
	public void runCodeSyncAlgorithm(IProject project, IResource resource, String technology, CommunicationChannel communicationChannel, boolean showDialog) {
		CodeSyncElement cse = CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, resource, technology, communicationChannel, showDialog);
	}

}
