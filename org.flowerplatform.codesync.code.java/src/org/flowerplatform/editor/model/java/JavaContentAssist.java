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

import java.util.List;
import java.util.Map;

import org.flowerplatform.editor.model.ContentAssistItem;
import org.flowerplatform.editor.model.IContentAssist;

/**
 * @author Mariana Gheorghe
 */
@SuppressWarnings("restriction")
public class JavaContentAssist implements IContentAssist {

	@Override
	public List<ContentAssistItem> findMatches(Map<String, Object> context, String pattern) {
		throw new UnsupportedOperationException();
	}
	
//
//	/*
//	 * Copied from CompletionEngine.
//	 */
//	static final BaseTypeBinding[] BASE_TYPES = {
//		TypeBinding.BOOLEAN,
//		TypeBinding.BYTE,
//		TypeBinding.CHAR,
//		TypeBinding.DOUBLE,
//		TypeBinding.FLOAT,
//		TypeBinding.INT,
//		TypeBinding.LONG,
//		TypeBinding.SHORT,
//		TypeBinding.VOID
//	};
//	
//	/**
//	 * Delegates to {@link SearchEngine} to search for Java types that match the prefix <code>pattern</code>.
//	 */
//	@Override
//	public List<ContentAssistItem> findMatches(Map<String, Object> context, String pattern) {
//		String elementType = context.get(TYPE).toString();
//		if (elementType == null || !elementType.startsWith(CodeSyncCodeJavaPlugin.TECHNOLOGY)) {
//			// not a java element
//			return null;
//		}
//		
//		List<ContentAssistItem> result = new ArrayList<ContentAssistItem>();
//		
//		////////////////////////////////////////////////////
//		// STEP 1. Primitive types (int, long etc)
//		////////////////////////////////////////////////////
//		
//		pattern = pattern.toLowerCase();
//		for (BaseTypeBinding binding : BASE_TYPES) {
//			String name = String.copyValueOf(binding.simpleName);
//			if (name.toLowerCase().startsWith(pattern)) {
//				ContentAssistItem item = new ContentAssistItem(name, name, null, null);
//				result.add(item);
//			}
//		}
//		
//		////////////////////////////////////////////////////
//		// STEP 2. Types from this project's scope
//		// (i.e. source dirs, referenced projects and libs)
//		////////////////////////////////////////////////////
//		
////		// debug
////		BasicSearchEngine.VERBOSE = true;
////		JobManager.VERBOSE = true;
//		
//		SearchEngine engine = new SearchEngine();
//		
//		// search in all the packages
//		char[] packageName = null;
//		int packageMatchRule = 0;
//		
//		// pattern is a prefix
//		char[] typeName = pattern.toCharArray();
//		int typeMatchRule = SearchPattern.R_PREFIX_MATCH;
//		
//		// searching for types only
//		int searchFor = IJavaSearchConstants.TYPE;
//		
//		// find the project to search in
//		IJavaElement[] elements = new IJavaElement[1];
//		File file = (File) context.get(RESOURCE);
//		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
//		if (resource == null) {
//			throw new RuntimeException(CodeSyncPlugin.getInstance().getMessage("contentAssist.resourceIsNotInProject"));
//		}
//		IJavaElement javaElement = JavaCore.create(resource.getProject());
//		elements[0] = javaElement;
//
//		// search everywhere for this project
//		int includeMask = IJavaSearchScope.SOURCES | IJavaSearchScope.REFERENCED_PROJECTS | IJavaSearchScope.SYSTEM_LIBRARIES | IJavaSearchScope.APPLICATION_LIBRARIES;
//		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements, includeMask);
//		
//		// report types to requestor
//		JavaTypeNameRequestor nameRequestor = new JavaTypeNameRequestor();
//		
//		try {
//			// perform search
//			engine.searchAllTypeNames(
//					packageName, packageMatchRule, 
//					typeName, typeMatchRule, 
//					searchFor, scope, 
//					nameRequestor, 
//					IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, 
//					nameRequestor.getProgressMonitor());
//		} catch (JavaModelException e) {
//			throw new RuntimeException(e);
//		} catch (OperationCanceledException e1) {
//			// MAX_TYPES_COUNT was reached
//		}
//		
//		// return matches
//		result.addAll(nameRequestor.getMatches());
//		
//		return result;
//	}

}
