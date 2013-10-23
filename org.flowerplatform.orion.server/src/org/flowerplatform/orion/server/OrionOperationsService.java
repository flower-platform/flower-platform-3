package org.flowerplatform.orion.server;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.editor.model.java.remote.NewJavaClassDiagramAction;
import org.flowerplatform.editor.remote.EditorOperationsService;

/**
 * @author Cristina Constantinescu
 */
public class OrionOperationsService extends EditorOperationsService {

	@RemoteInvocation
	public void createDiagram(ServiceInvocationContext context, String parentFolder) {
		NewJavaClassDiagramAction command = new NewJavaClassDiagramAction();
		command.parentPath = getResourcePath(parentFolder);
		command.name = "New Diagram.notation";
		command.setCommunicationChannel(context.getCommunicationChannel());
		command.executeCommand();
	}
	
	@RemoteInvocation
	public List<String> getPaths(ServiceInvocationContext context, String paths) {
		List<String> friendlyEditableResourcePathList = parseFriendlyEditableResourcePathList(paths);		
		List<String> fsPaths = new ArrayList<String>();
		for (String path : friendlyEditableResourcePathList) {
			fsPaths.add(getResourcePath(path));
		}
		return fsPaths;
	}
	
}
