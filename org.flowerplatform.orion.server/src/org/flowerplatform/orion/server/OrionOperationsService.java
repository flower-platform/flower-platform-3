package org.flowerplatform.orion.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.orion.internal.server.servlets.file.NewFileServlet;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.editor.model.java.remote.NewJavaClassDiagramAction;
import org.flowerplatform.editor.remote.EditorOperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class OrionOperationsService extends EditorOperationsService {

	private static final Logger logger = LoggerFactory.getLogger(OrionOperationsService.class);

	@Override
	protected String getResourcePath(String path) {		
		try {
			String decodedPath = getFriendlyNameDecoded(path);
			IPath iPath = new Path(decodedPath).removeFirstSegments(1);			
			
			@SuppressWarnings("restriction")
			IFileStore fileStore = NewFileServlet.getFileStore(null, iPath);			
			File file = fileStore.toLocalFile(EFS.NONE, null);
			
			return CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
		} catch (CoreException e) {
			return null;
		}		
	}
	
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
