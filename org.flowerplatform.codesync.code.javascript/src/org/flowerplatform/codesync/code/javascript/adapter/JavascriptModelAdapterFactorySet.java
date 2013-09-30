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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.code.javascript.feature_provider.RegExNodeFeatureProvider;
import org.flowerplatform.codesync.code.javascript.feature_provider.RegExParameterFeatureProvider;
import org.flowerplatform.codesync.code.javascript.regex_ast.Node;
import org.flowerplatform.codesync.code.javascript.regex_ast.Parameter;

import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.code.CodeSyncModelAdapterFactory;
import com.crispico.flower.mp.codesync.code.adapter.AstModelElementAdapter;
import com.crispico.flower.mp.codesync.code.adapter.CodeSyncElementModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.FolderModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class JavascriptModelAdapterFactorySet extends ModelAdapterFactorySet {

	@Override
	public void initialize(Resource cache, String limitedPath, boolean useUIDs) {
		super.initialize(cache, limitedPath, useUIDs);
		
		// right - AST
		rightFactory = new ModelAdapterFactory();
		
		// folder adapter
		FolderModelAdapter folderModelAdapter = (FolderModelAdapter) createAstModelAdapter(new FolderModelAdapter());
		folderModelAdapter.setLimitedPath(limitedPath);
		rightFactory.addModelAdapter(IFolder.class, folderModelAdapter);
		
		// javascript specific adapter
		rightFactory.addModelAdapter(IFile.class, createAstModelAdapter(new JavascriptFileModelAdapter()));
		rightFactory.addModelAdapter(Node.class, createAstModelAdapter(new RegExNodeAstModelAdapter()));
		rightFactory.addModelAdapter(Parameter.class, createAstModelAdapter(new RegExParameterModelAdapter()));
		
		// ancestor - CSE
		ancestorFactory = createCodeSyncModelAdapterFactory(cache, false);
		
		// left - CSE
		leftFactory = createCodeSyncModelAdapterFactory(cache, true);
		
		// feature providers
		CodeSyncElementFeatureProvider featureProvider = new CodeSyncElementFeatureProvider();
		addFeatureProvider(IFolder.class, featureProvider);
		addFeatureProvider(AstModelElementAdapter.FOLDER, featureProvider);
		addFeatureProvider(IFile.class, featureProvider);
		addFeatureProvider(AstModelElementAdapter.FILE, featureProvider);
		
		RegExNodeFeatureProvider regexNodeFeatureProvider = new RegExNodeFeatureProvider();
		addFeatureProvider(CodeSyncElement.class, regexNodeFeatureProvider);
		addFeatureProvider(Node.class, regexNodeFeatureProvider);
		
		addFeatureProvider(Parameter.class, new RegExParameterFeatureProvider());
	}
	
	private CodeSyncModelAdapterFactory createCodeSyncModelAdapterFactory(Resource resource, boolean isLeft) {
		CodeSyncModelAdapterFactory factory = new CodeSyncModelAdapterFactory(this, rightFactory, resource, isLeft);
		CodeSyncElementModelAdapter cseAdapter = (CodeSyncElementModelAdapter) createAstModelAdapter(
				new CodeSyncElementModelAdapter());
		cseAdapter.setEObjectConverter(rightFactory);
		factory.addModelAdapter(CodeSyncElement.class, cseAdapter);
		factory.addModelAdapter(Parameter.class, createAstModelAdapter(new RegExParameterModelAdapter()));
		return factory;
	}

	private IModelAdapter createAstModelAdapter(IModelAdapter adapter) {
		return adapter.setModelAdapterFactorySet(this);
	}
	
}
