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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import com.crispico.flower.mp.codesync.code.adapter.AstModelElementAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Mapped to {@link File}s with the extension <code>java</code>. Chidren are {@link ASTNode}s.
 * 
 * @author Mariana
 */
public class JavaFileModelAdapter extends AstModelElementAdapter {

	private Map<File, CompilationUnit> compilationUnits = new HashMap<File, CompilationUnit>();
	
	private Map<File, String> filesToRename = new HashMap<File, String>();
	
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
			File file = getFile(element);
			filesToRename.put(file, (String) value);
		}
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		File file = getFile(element);
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
	private CompilationUnit getOrCreateCompilationUnit(File file) {
		if (compilationUnits.containsKey(file)) {
			return compilationUnits.get(file);
		} else {
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			Map options = new HashMap<>(JavaCore.getOptions());
			options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
			parser.setCompilerOptions(options);
			char[] initialContent = file.exists() ? getFileContent(file) : new char[0];
			parser.setSource(initialContent);
			CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
			compilationUnits.put(file, astRoot);
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
	
	private char[] getFileContent(File file) {
		try {
			return FileUtils.readFileToString(file).toCharArray();
		} catch (IOException e) {
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
		File file = getFile(element);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		String newName = filesToRename.get(file);
		if (newName != null) {
			File dest = new File(file.getParent(), newName);
			file.renameTo(dest);
		}
		
		if (file.exists()) {
			CompilationUnit cu = compilationUnits.get(file);
			if (cu != null) {
				Document document;
				try {
					document = new Document(FileUtils.readFileToString(file));
					TextEdit edits = cu.rewrite(document, null);
					edits.apply(document);
					FileUtils.writeStringToFile(file, document.get()); // TODO replace with call to IFileAccess
				} catch (IOException | MalformedTreeException | BadLocationException e) {
					throw new RuntimeException(e);
				}
			}
		}
		compilationUnits.remove(file);
		
		// no need to call save for the AST
		return false;
	}
	
	/**
	 * Discards the AST corresponding to this file.
	 */
	@Override
	public boolean discard(Object element) {
		compilationUnits.remove(getFile(element));
		
		// no need to call discard for the AST
		return false;
	}
	
	private File getFile(Object modelElement) {
		return (File) modelElement;
	}
	
}