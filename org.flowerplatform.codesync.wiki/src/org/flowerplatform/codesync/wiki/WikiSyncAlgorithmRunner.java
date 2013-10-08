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
package org.flowerplatform.codesync.wiki;

import java.io.File;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.communication.channel.CommunicationChannel;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.ICodeSyncAlgorithmRunner;
import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana Gheorghe
 */
public class WikiSyncAlgorithmRunner implements ICodeSyncAlgorithmRunner {

	@Override
	public void runCodeSyncAlgorithm(File project, File resource, String technology, CommunicationChannel communicationChannel, boolean showDialog) {
		String name = CodeSyncPlugin.getInstance().getProjectsProvider().getPath(project);
		ResourceSet resourceSet = CodeSyncPlugin.getInstance().getOrCreateResourceSet(project, "mindmapEditorStatefulService");
		// this will be a temporary tree, do not send the project
		CodeSyncRoot leftRoot = WikiPlugin.getInstance().getWikiTree(null, resourceSet, project, name, technology);
		CodeSyncRoot rightRoot = WikiPlugin.getInstance().getWikiTree(project, resourceSet, null, name, technology);
		
		WikiPlugin.getInstance().updateTree(leftRoot, rightRoot, project, resourceSet, technology, communicationChannel, true);
	}

}