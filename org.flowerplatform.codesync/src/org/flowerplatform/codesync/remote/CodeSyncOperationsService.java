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
package org.flowerplatform.codesync.remote;

import java.util.List;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncOperationsService {

	public CodeSyncElement create(String codeSyncType) {
		CodeSyncElement codeSyncElement = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		codeSyncElement.setType(codeSyncType);
		// test name
		codeSyncElement.setName(codeSyncType);
		return codeSyncElement;
	}
	
	public void add(CodeSyncElement parent, CodeSyncElement elementToAdd) {
		elementToAdd.setAdded(true);
		parent.getChildren().add(elementToAdd);
	}
	
	public Object getFeatureValue(CodeSyncElement element, Object feature) {
		// TODO
		return null;
	}
	
	public void setFeatureValue(CodeSyncElement element, Object feature, Object value) {
		// TODO
	}
	
	public void markDeleted(CodeSyncElement element) {
		// TODO
	}
	
	public List<Object> getFeatures(String codeSyncType) {
		// TODO
		return null;
	}
	
}
