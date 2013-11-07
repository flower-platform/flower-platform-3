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
package com.crispico.flower.mp.codesync.code.adapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.file.IFileAccessController;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 * @author Sebastian Solomon
 */
public abstract class AbstractFileModelAdapter extends AstModelElementAdapter {

	protected Map<Object, Object> fileInfos = new HashMap<Object, Object>();
	
	protected Map<Object, String> filesToRename = new HashMap<Object, String>();
	
	protected Object getOrCreateFileInfo(Object file) {
		if (fileInfos.containsKey(file)) {
			return fileInfos.get(file);
		} else {
			Object fileInfo = createFileInfo(file);
			fileInfos.put(file, fileInfo);
			return fileInfo;
		}
	}
	
	protected abstract Object createFileInfo(Object file);
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			return getLabel(element);
		}
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type().equals(feature)) {
			return CodeSyncPlugin.FILE;
		}
		return null;
	}
	
	@Override
	public void setValueFeatureValue(Object file, Object feature, Object value) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			filesToRename.put(file, (String) value);
		}
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return getLabel(element);
	}

	/**
	 * @author Sebastian Solomonm
	 */
	@Override
	public String getLabel(Object modelElement) {
		return EditorPlugin.getInstance().getFileAccessController().getName(modelElement);
	}
	
	@Override
	public List<String> getIconUrls(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		return true;
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			return getChildren(element);
		}
		return Collections.emptyList();
	}
	
	@Override
	public Object createCorrespondingModelElement(Object element) {
		return null;
	}
	
	/**
	 * Creates the file, if it does not exist, and commits all the modifications recorded by the AST.
	 */
	@Override
	public boolean save(Object file) {
		IFileAccessController fileAccessController = EditorPlugin.getInstance().getFileAccessController();
		Object initialFile = file;
		
		String newName = filesToRename.get(file);
		if (newName != null) {
			Object dest = fileAccessController.createNewFile(fileAccessController.getParent(initialFile), newName);
			fileAccessController.rename(file, dest);
			file = dest;
		}
		
		if (!fileAccessController.exists(file)){
				fileAccessController.createNewFile(file);
		}
		
		if (!fileAccessController.exists(file)){
			Object fileInfo = fileInfos.get(initialFile);
			if (fileInfo != null) {
				Document document;
				try {
					document = new Document(fileAccessController.readFileToString(file));
					TextEdit edits = rewrite(document, fileInfo);
					edits.apply(document);
					fileAccessController.writeStringToFile(fileInfo, document.get());
				} catch (MalformedTreeException | BadLocationException e) {
					throw new RuntimeException(e);
				}
			}
		}
		fileInfos.remove(initialFile);
		
		// no need to call save for the AST
		return false;
	}
	
	protected abstract TextEdit rewrite(Document document, Object fileInfo);
	
	/**
	 * Discards the AST corresponding to this file.
	 */
	@Override
	public boolean discard(Object element) {
		fileInfos.remove(element);
		
		// no need to call discard for the AST
		return false;
	}
	
	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// nothing to do
	}
	
}
