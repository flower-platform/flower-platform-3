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
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.flowerplatform.editor.model.ContentAssistItem;
import org.flowerplatform.editor.model.IContentAssist;
import org.flowerplatform.web.projects.remote.ProjectsService;

import com.crispico.flower.mp.codesync.code.java.CodeSyncCodeJavaPlugin;

/**
 * @author Mariana Gheorghe
 */
public class JavaContentAssist implements IContentAssist {

	/**
	 * Delegates to {@link SearchEngine} to search for Java types that match the prefix <code>pattern</code>.
	 */
	@Override
	public List<ContentAssistItem> findMatches(Map<String, Object> context, String pattern) {
		if (!context.get(TYPE).toString().startsWith(CodeSyncCodeJavaPlugin.TECHNOLOGY)) {
			// not a java element
			return null;
		}
		
//		BasicSearchEngine.VERBOSE = true;
//		JobManager.VERBOSE = true;
		
		SearchEngine engine = new SearchEngine();
		
		// search in all the packages
		char[] packageName = null;
		int packageMatchRule = 0;
		
		// pattern is a prefix
		char[] typeName = pattern.toCharArray();
		int typeMatchRule = SearchPattern.R_PREFIX_MATCH;
		
		// searching for types only
		int searchFor = IJavaSearchConstants.TYPE;
		
		// find the project to search in
		IJavaElement[] elements = new IJavaElement[1];
		File file = (File) context.get(RESOURCE);
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
		if (resource == null) {
			throw new RuntimeException("The resource is not contained in a project!");
		}
		IJavaElement javaElement = JavaCore.create(resource.getProject());
		elements[0] = javaElement;

		// search everywhere for this project
		int includeMask = IJavaSearchScope.SOURCES | IJavaSearchScope.REFERENCED_PROJECTS | IJavaSearchScope.SYSTEM_LIBRARIES | IJavaSearchScope.APPLICATION_LIBRARIES;
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements, includeMask);
		
		// report types to requestor
		JavaTypeNameRequestor nameRequestor = new JavaTypeNameRequestor();
		
		try {
			// perform search
			engine.searchAllTypeNames(
					packageName, packageMatchRule, 
					typeName, typeMatchRule, 
					searchFor, scope, 
					nameRequestor, 
					IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
		
		// return matches
		return nameRequestor.getMatches();
	}

}
