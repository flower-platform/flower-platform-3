/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.editor.model.java;

import java.io.File;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.IDragOnDiagramHandler;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.codesync.code.java.CodeSyncCodeJavaPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class JavaDragOnDiagramHandler implements IDragOnDiagramHandler {

	@Override
	public boolean handleDragOnDiagram(ServiceInvocationContext context, Collection<?> draggedObjects, Diagram diagram, View viewUnderMouse, Object layoutHint, CommunicationChannel communicationChannel) {
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
		
		for (Object object : draggedObjects) {
			Object resource;
			try {
				resource = EditorPlugin.getInstance().getFileAccessController().getFile((String) object);
			} catch (Exception e) {
				throw new RuntimeException(String.format("Error while getting resource %s", object), e);
			}
			Object project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile(resource);
			if (!acceptDraggedObject(resource)) {
				return false;
			}
			CodeSyncElement cse = CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, resource, CodeSyncCodeJavaPlugin.TECHNOLOGY, communicationChannel, false);
			
			Node node = NotationFactory.eINSTANCE.createNode();
			node.setViewType("class");
			node.setDiagrammableElement(cse.getChildren().get(0)); // get the class from the file
			
			Node classTitle = NotationFactory.eINSTANCE.createNode();
			classTitle.setViewType("classTitle");
			classTitle.setDiagrammableElement(cse.getChildren().get(0));
			node.getPersistentChildren().add(classTitle);
			
			Node classAttrSeparator = NotationFactory.eINSTANCE.createNode();
//			classAttrSeparator.setViewType(JavaClassDiagramOperationsService.ATTRIBUTE_SEPARATOR);
			node.getPersistentChildren().add(classAttrSeparator);
			
			Node classOperationSeparator = NotationFactory.eINSTANCE.createNode();
//			classOperationSeparator.setViewType(JavaClassDiagramOperationsService.OPERATIONS_SEPARATOR);
			node.getPersistentChildren().add(classOperationSeparator);
			
			Bounds bounds = NotationFactory.eINSTANCE.createBounds();
			bounds.setX(200);
			bounds.setHeight(100);
			bounds.setWidth(100);
			node.setLayoutConstraint(bounds);
			diagram.getPersistentChildren().add(node);
		}
		
		return true;
	}
	/**
	 * @author Sebastian Solomon
	 */
	private boolean acceptDraggedObject(Object object) {
		// TODO Mariana : add support for JE
		return (object instanceof String 
				|| object instanceof CompilationUnit)
				|| object instanceof File && (EditorPlugin.getInstance().getFileAccessController().getFileExtension(object).equals(CodeSyncCodeJavaPlugin.TECHNOLOGY)) 
				|| object instanceof IFile && (EditorPlugin.getInstance().getFileAccessController().getFileExtension(object).equals(CodeSyncCodeJavaPlugin.TECHNOLOGY));
	}

}
