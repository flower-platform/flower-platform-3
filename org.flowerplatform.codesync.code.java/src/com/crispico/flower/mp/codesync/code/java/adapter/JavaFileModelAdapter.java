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
package com.crispico.flower.mp.codesync.code.java.adapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import com.crispico.flower.mp.codesync.code.adapter.AstModelElementAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Mapped to {@link IFile}s with the extension <code>java</code>. Chidren are {@link ASTNode}s.
 * 
 * @author Mariana
 */
public class JavaFileModelAdapter extends AstModelElementAdapter {

	private Map<IPath, CompilationUnit> compilationUnits = new HashMap<IPath, CompilationUnit>();
	
	private final String RENAMED = "renamed";
	
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
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			IFile file = getFile(element);
			try {
				IMarker marker = file.createMarker(IMarker.MARKER);
				marker.setAttribute(RENAMED, value);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		IFile file = getFile(element);
		CompilationUnit cu = getOrCreateCompilationUnit(file);
		ASTNode node = (ASTNode) JavaTypeModelAdapter.createCorrespondingModelElement(cu.getAST(), (CodeSyncElement) correspondingChild);
		cu.types().add(node);
		return node;
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		((ASTNode) child).delete();
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		return true;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return getOrCreateCompilationUnit(getFile(modelElement)).types();
	}
	
	/**
	 * Returns the compilation unit for the <code>file</code>'s path, if it exists.
	 * Otherwise, creates a new compilation unit from the file's content.
	 */
	private CompilationUnit getOrCreateCompilationUnit(IFile file) {
		IPath path = file.getFullPath();
		if (compilationUnits.containsKey(path)) {
			return compilationUnits.get(path);
		} else {
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			Map options = new HashMap<>(JavaCore.getOptions());
			options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
			parser.setCompilerOptions(options);
			try {
				file.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
			char[] initialContent = file.exists() ? getFileContent(file) : new char[0];
			parser.setSource(initialContent);
			CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
			compilationUnits.put(file.getFullPath(), astRoot);
			astRoot.recordModifications();
			return astRoot;
		}
	}

	@Override
	public String getLabel(Object modelElement) {
		return getFile(modelElement).getName();
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return null;
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			return getChildren(element);
		}
		return Collections.emptyList();
	}
	
	private char[] getFileContent(IFile file) {
		ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager(); // get the buffer manager
		IPath path = file.getFullPath();
		try {
			bufferManager.connect(path, LocationKind.IFILE, null);
			ITextFileBuffer textFileBuffer = bufferManager.getTextFileBuffer(path, LocationKind.IFILE);
			// retrieve the buffer
			IDocument document = textFileBuffer.getDocument();
			return document.get().toCharArray();
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		return null;
	}
	
	/**
	 * Creates the file, if it does not exist, and commits all the modifications recorded by the AST.
	 */
	@Override
	public boolean save(Object element) {
		IFile file = getFile(element);
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
				String value = (String) marker.getAttribute(RENAMED);
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
			CompilationUnit cu = compilationUnits.get(path);
			if (cu != null) {
				ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager(); // get the buffer manager
				try {
					bufferManager.connect(path, LocationKind.IFILE, null);
					ITextFileBuffer textFileBuffer = bufferManager.getTextFileBuffer(path, LocationKind.IFILE);
					// retrieve the buffer
					IDocument document = textFileBuffer.getDocument();
					TextEdit edits = cu.rewrite(document, null);
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
	
	/**
	 * Discards the AST corresponding to this file.
	 */
	@Override
	public boolean discard(Object element) {
		IPath path = getFile(element).getFullPath();
		compilationUnits.remove(path);
		
		// no need to call discard for the AST
		return false;
	}
	
	private IFile getFile(Object modelElement) {
		return (IFile) modelElement;
	}

	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// nothing to do
	}
	
}