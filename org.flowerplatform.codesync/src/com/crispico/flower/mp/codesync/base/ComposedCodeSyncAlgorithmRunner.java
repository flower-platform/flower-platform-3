package com.crispico.flower.mp.codesync.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.flowerplatform.communication.channel.CommunicationChannel;

/**
 * @author Mariana
 */
public class ComposedCodeSyncAlgorithmRunner implements ICodeSyncAlgorithmRunner {

	protected Map<String, ICodeSyncAlgorithmRunner> runners = new HashMap<String, ICodeSyncAlgorithmRunner>();
	
	public void addRunner(String technology, ICodeSyncAlgorithmRunner runner) {
		runners.put(technology, runner);
	}
	
	@Override
	public void runCodeSyncAlgorithm(IProject project, IResource resource, String technology, CommunicationChannel communicationChannel, boolean showDialog) {
		runners.get(technology).runCodeSyncAlgorithm(project, resource, technology, communicationChannel, showDialog);
	}

}
