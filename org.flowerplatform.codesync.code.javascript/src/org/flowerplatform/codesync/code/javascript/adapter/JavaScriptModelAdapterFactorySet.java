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

import java.io.File;

import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.code.javascript.CodeSyncCodeJavascriptPlugin;
import org.flowerplatform.codesync.code.javascript.feature_provider.RegExNodeFeatureProvider;
import org.flowerplatform.codesync.code.javascript.feature_provider.RegExParameterFeatureProvider;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;

import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.code.CodeSyncModelAdapterFactory;
import com.crispico.flower.mp.codesync.code.adapter.FolderModelAdapter;
import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class JavaScriptModelAdapterFactorySet extends ModelAdapterFactorySet {

	public static final String MODEL_ELEMENT = "modelElement";
	public static final String PARAMETER = "parameter";
	
	@Override
	public void initialize(Resource cache, String limitedPath, boolean useUIDs) {
		super.initialize(cache, limitedPath, useUIDs);
		
		// right - AST
		rightFactory = new ModelAdapterFactory();
		
		// folder adapter
		FolderModelAdapter folderModelAdapter = (FolderModelAdapter) createAstModelAdapter(new FolderModelAdapter());
		folderModelAdapter.setLimitedPath(limitedPath);
		rightFactory.addModelAdapter(File.class, folderModelAdapter, "", CodeSyncPlugin.FOLDER);
		
		// javascript specific adapter
		IModelAdapter fileModelAdapter = createAstModelAdapter(new JavaScriptFileModelAdapter());
		rightFactory.addModelAdapter(File.class, fileModelAdapter, CodeSyncCodeJavascriptPlugin.TECHNOLOGY, CodeSyncPlugin.FILE);
		rightFactory.addModelAdapter(File.class, fileModelAdapter, "html", CodeSyncPlugin.FILE);
		rightFactory.addModelAdapter(RegExAstNode.class, createAstModelAdapter(new RegExNodeAstModelAdapter()), MODEL_ELEMENT);
		rightFactory.addModelAdapter(RegExAstNodeParameter.class, createAstModelAdapter(new RegExParameterModelAdapter()), PARAMETER);
		
		// ancestor - CSE
		ancestorFactory = createCodeSyncModelAdapterFactory(cache, false);
		
		// left - CSE
		leftFactory = createCodeSyncModelAdapterFactory(cache, true);
		
		// feature providers
		CodeSyncElementFeatureProvider featureProvider = new CodeSyncElementFeatureProvider();
		addFeatureProvider(File.class, featureProvider);
		addFeatureProvider(CodeSyncPlugin.FOLDER, featureProvider);
		addFeatureProvider(CodeSyncPlugin.FILE, featureProvider);
		
		RegExNodeFeatureProvider regexRegExAstNodeFeatureProvider = new RegExNodeFeatureProvider();
		addFeatureProvider(CodeSyncElement.class, regexRegExAstNodeFeatureProvider);
		addFeatureProvider(RegExAstNode.class, regexRegExAstNodeFeatureProvider);
		
		addFeatureProvider(RegExAstNodeParameter.class, new RegExParameterFeatureProvider());
	}
	
	private CodeSyncModelAdapterFactory createCodeSyncModelAdapterFactory(Resource resource, boolean isLeft) {
		CodeSyncModelAdapterFactory factory = new CodeSyncModelAdapterFactory(this, rightFactory, resource, isLeft);
		factory.addModelAdapter(CodeSyncPlugin.FOLDER, new SyncElementModelAdapter(), CodeSyncPlugin.FOLDER);
		factory.addModelAdapter(CodeSyncPlugin.FILE, new SyncElementModelAdapter(), CodeSyncPlugin.FILE);
		factory.addModelAdapter(CodeSyncElement.class, new SyncElementModelAdapter(), MODEL_ELEMENT);
		factory.addModelAdapter(RegExAstNodeParameter.class, createAstModelAdapter(new RegExParameterModelAdapter()), PARAMETER);
		return factory;
	}
	
	private IModelAdapter createAstModelAdapter(IModelAdapter adapter) {
		return adapter.setModelAdapterFactorySet(this);
	}
	
}
