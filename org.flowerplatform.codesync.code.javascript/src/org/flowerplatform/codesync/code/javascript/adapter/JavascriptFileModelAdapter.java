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
package org.flowerplatform.codesync.code.javascript.adapter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.code.javascript.CodeSyncCodeJavascriptPlugin;
import org.flowerplatform.codesync.code.javascript.parser.Parser;
import org.flowerplatform.codesync.code.javascript.regex_ast.Node;
import org.flowerplatform.codesync.code.javascript.regex_ast.Parameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.web.projects.remote.ProjectsService;

import com.crispico.flower.mp.codesync.code.adapter.AstModelElementAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public class JavascriptFileModelAdapter extends AstModelElementAdapter {

	protected Map<IPath, Node> compilationUnits = new HashMap<IPath, Node>();
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			return getLabel(element);
		}
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type().equals(feature)) {
			return FILE;
		}
		return null;
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return getLabel(element);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		Resource resource = new ResourceImpl();
		Node node = RegExAstFactory.eINSTANCE.createNode();
		node.setAdded(true);
		// adding to resource to avoid UNDEFINED values during sync
		// see EObjectModelAdapter.getValueFeatureValue()
		resource.getContents().add(node);
		compilationUnits.put(((IFile) element).getFullPath(), node);
		return node;
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(Object element) {
		IFile file = (IFile) element;
		if (!file.exists()) {
			InputStream is = new ByteArrayInputStream(new byte[0]);
			try {
				file.create(is, true, null);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
		
		try {
			for (IMarker marker : file.findMarkers(IMarker.MARKER, false, IResource.DEPTH_ZERO)) {
				String value = (String) marker.getAttribute("renamed");
				if (value != null) {
					file.move(file.getFullPath().removeLastSegments(1).append((String) value), true, null);
					break;
				}
			}
		} catch (CoreException e1) {
			throw new RuntimeException(e1);
		}
		
		IPath path = file.getFullPath();
		if (file.exists()) {
			Node node = compilationUnits.get(path);
			if (node != null) {
				ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager(); // get the buffer manager
				try {
					bufferManager.connect(path, LocationKind.IFILE, null);
					ITextFileBuffer textFileBuffer = bufferManager.getTextFileBuffer(path, LocationKind.IFILE);
					// retrieve the buffer
					IDocument document = textFileBuffer.getDocument();
					TextEdit edits = rewrite(document, node);
					edits.apply(document);
					// commit changes to underlying file
					textFileBuffer.commit(null, true);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				try {
					bufferManager.disconnect(path, LocationKind.IFILE, null);
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
			}
		}
		compilationUnits.remove(path);
		
		// no need to call save for the AST
		return false;
	}

	private TextEdit rewrite(IDocument document, Node node) {
		MultiTextEdit edit = new MultiTextEdit();
		rewrite(document, node, edit);
		return edit;
	}

	private void rewrite(IDocument document, Node node, MultiTextEdit edit) {
		if (node.isAdded()) {
			String template = loadTemplate(node);
			edit.addChild(new InsertEdit(getInsertPoint(node), template));
		} else {
			for (Node child : node.getChildren()) {
				rewrite(document, child, edit);
			}
		}
	}

	/**
	 * Loads children templates as well, because we can't allow overlapping edits.
	 */
	private String loadTemplate(Node node) {
		String template = null;
		try {
			URL url = CodeSyncCodeJavascriptPlugin.getInstance().getBundleContext().getBundle().getResource("public-resources/templates/" + node.getTemplate() + ".tpl");
			File file = new File(FileLocator.resolve(url).toURI());
			template = FileUtils.readFileToString(file);
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("Template does not exist", e);
		}
		for (Parameter parameter : node.getParameters()) {
			template = template.replace("@" + parameter.getName(), parameter.getValue());
		}
		int childrenInsertPoint = template.indexOf("<!-- children-insert-point -->");
		if (childrenInsertPoint >= 0) {
			String childrenArea = new String();
			for (Node child : node.getChildren()) {
				childrenArea += loadTemplate(child);
			}
			template = template.substring(0, childrenInsertPoint) + childrenArea + template.substring(childrenInsertPoint);
		}
		return template;
	}
	
	/**
	 * Returns the {@link Node#getChildrenInsertPoint()} of the parent node, or the
	 * {@link Node#getNextSiblingInsertPoint()} of the previous sibling, if any.
	 */
	private int getInsertPoint(Node node) {
		Node parent = (Node) node.eContainer();
		if (parent == null) {
			return 0;
		}
		int childIndex = parent.getChildren().indexOf(node);
		for (int i = childIndex - 1; i >= 0 ; i--) {
			Node sibling = parent.getChildren().get(i);
			if (!sibling.isAdded()) {
				return sibling.getNextSiblingInsertPoint();
			}
		}
		return parent.getChildrenInsertPoint();
	}
	
	@Override
	public boolean discard(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		return true;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		Parser parser = new Parser();
		File file = ProjectsService.getInstance().getFileFromProjectWrapperResource((IResource) modelElement);
		Node node = parser.parse(file);
		compilationUnits.put(((IResource) modelElement).getFullPath(), node);
		return Collections.singletonList(node);
	}

	@Override
	public String getLabel(Object modelElement) {
		return ((IFile) modelElement).getName();
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			return getChildren(element);
		}
		return Collections.emptyList();
	}
	
	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// nothing to do
	}

}
