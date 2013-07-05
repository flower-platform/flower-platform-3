package com.crispico.flower.mp.codesync.code.java;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.web.projects.remote.ProjectsService;

import com.crispico.flower.mp.codesync.base.IDragOnDiagramHandler;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;

/**
 * @author Mariana
 */
public class JavaDragOnDiagramHandler extends AbstractServerCommand implements IDragOnDiagramHandler {

	public List<PathFragment> pathWithRoot;
	
	@Override
	public boolean handleDragOnDiagram(Collection<?> draggedObjects, Diagram diagram, View viewUnderMouse, Object layoutHint, CommunicationChannel communicationChannel) {
//		for (Object object : draggedObjects) {
//			if (!acceptDraggedObject(object)) {
//				return false;
//			}
//		}
//		
//		for (Object object : draggedObjects) {
//			String fqName = CodeSyncCodePlugin.getInstance().getFullyQualifiedNameProvider().getFullyQualifiedName(object);
//			
//			// TODO Mariana : create views
//			
//			CodeSyncCodeJavaPlugin.getInstance().getFolderModelAdapter().setLimitedPath(fqName);
//			CodeSyncCodePlugin.getInstance().getCodeSyncElement(fqName, CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel);
//		}
		
		return true;
	}

	private boolean acceptDraggedObject(Object object) {
		// TODO Mariana : add support for JE
		return (object instanceof String || object instanceof IFile && ((IFile) object).getFileExtension().equals(CodeSyncCodeJavaPlugin.TECHNOLOGY)) || object instanceof CompilationUnit;
	}

	@Override
	public void executeCommand() {
		Pair<IProject, IResource> pair = ProjectsService.getInstance().getEclipseProjectAndResource(pathWithRoot);
		CodeSyncCodePlugin.getInstance().getCodeSyncElement(pair.a, pair.b, CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel);
	}
}
