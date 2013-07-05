package com.crispico.flower.mp.codesync.code.java;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.codesync.code.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.codesync.code.FolderModelAdapter;
import com.crispico.flower.mp.codesync.code.StringModelAdapter;

/**
 * @author Mariana
 */
public class CodeSyncCodeJavaPlugin extends AbstractFlowerJavaPlugin {

	public static final String TECHNOLOGY = "java";
	
	protected static CodeSyncCodeJavaPlugin INSTANCE;
	
	private FolderModelAdapter folderModelAdapter;
	
	public static CodeSyncCodeJavaPlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CodeSyncPlugin.getInstance().getDragOnDiagramHandler().addDelegateHandler(new JavaDragOnDiagramHandler());
		CodeSyncPlugin.getInstance().getFullyQualifiedNameProvider().addDelegateProvider(new JavaFullyQualifiedNameProvider());
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new JavaResourceChangeListener());
		JavaCore.addElementChangedListener(new JavaElementChangedListener(), ElementChangedEvent.POST_RECONCILE);
		
		ModelAdapterFactory astModelAdapterFactory = new ModelAdapterFactory();
		CodeSyncElementFeatureProvider featureProvider = new JavaFeatureProvider();
		
		// folder adapter
		folderModelAdapter = new FolderModelAdapter();
		folderModelAdapter.setFeatureProvider(featureProvider);
		astModelAdapterFactory.addModelAdapter(IFolder.class, folderModelAdapter);
		
		// java specific adapters
		JavaFileModelAdapter fileModelAdapter = new JavaFileModelAdapter();
		fileModelAdapter.setFeatureProvider(featureProvider);
		astModelAdapterFactory.addModelAdapter(IFile.class, fileModelAdapter, TECHNOLOGY);
		JavaComposedAstNodeModelAdapter composedModelAdapter = new JavaComposedAstNodeModelAdapter();
		composedModelAdapter.setFeatureProvider(featureProvider);
		astModelAdapterFactory.addModelAdapter(ASTNode.class, composedModelAdapter);
		astModelAdapterFactory.addModelAdapter(String.class, new StringModelAdapter(astModelAdapterFactory));
		
		CodeSyncCodePlugin.getInstance().getModelAdapterFactoryProvider().getFactories().put(TECHNOLOGY, astModelAdapterFactory);
		CodeSyncCodePlugin.getInstance().getModelAdapterFactoryProvider().getFeatureProviders().put(TECHNOLOGY, new JavaFeatureProvider());
	}

	public FolderModelAdapter getFolderModelAdapter() {
		return folderModelAdapter;
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		INSTANCE = null;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}

}
