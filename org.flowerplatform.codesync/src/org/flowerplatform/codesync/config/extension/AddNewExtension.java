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
package org.flowerplatform.codesync.config.extension;

import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService1;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public interface AddNewExtension {
	
	/**
	 * May populate the {@code parameters} map with values for:
	 * <ul>
	 * 		<li>{@link CodeSyncDiagramOperationsService1#PARENT_CODE_SYNC_ELEMENT}
	 * 		<li>{@link CodeSyncDiagramOperationsService1#PARENT_VIEW}
	 * </ul>
	 * 
	 * @return <code>true</code> if other extensions can add new elements
	 */
	boolean addNew(CodeSyncElement codeSyncElement, View parent, Resource codeSyncMappingResource, Map<String, Object> parameters);
	
}
