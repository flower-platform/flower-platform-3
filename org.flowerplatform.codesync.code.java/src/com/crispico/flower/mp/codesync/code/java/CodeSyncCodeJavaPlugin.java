package com.crispico.flower.mp.codesync.code.java;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.JavaCore;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.adapter.FolderModelAdapter;

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
		
//		ModelAdapterFactory astModelAdapterFactory = new ModelAdapterFactory();
//		
//		// folder adapter
//		folderModelAdapter = new FolderModelAdapter();
//		astModelAdapterFactory.addModelAdapter(IFolder.class, folderModelAdapter);
//		
//		// java specific adapters
//		JavaFileModelAdapter fileModelAdapter = new JavaFileModelAdapter();
//		astModelAdapterFactory.addModelAdapter(IFile.class, fileModelAdapter, TECHNOLOGY);
//		astModelAdapterFactory.addModelAdapter(AbstractTypeDeclaration.class, new JavaTypeModelAdapter());
//		astModelAdapterFactory.addModelAdapter(FieldDeclaration.class, new JavaAttributeModelAdapter());
//		astModelAdapterFactory.addModelAdapter(MethodDeclaration.class, new JavaOperationModelAdapter());
//		astModelAdapterFactory.addModelAdapter(SingleVariableDeclaration.class, new JavaParameterModelAdapter());
//		astModelAdapterFactory.addModelAdapter(Annotation.class, new JavaAnnotationModelAdapter());
//		astModelAdapterFactory.addModelAdapter(Modifier.class, new JavaModifierModelAdapter());
//		astModelAdapterFactory.addModelAdapter(MemberValuePair.class, new JavaMemberValuePairModelAdapter());
//		astModelAdapterFactory.addModelAdapter(EnumConstantDeclaration.class, new JavaEnumConstantDeclarationModelAdapter());
//		astModelAdapterFactory.addModelAdapter(AnnotationTypeMemberDeclaration.class, new JavaAnnotationTypeMemberDeclarationModelAdapter());
//
//		astModelAdapterFactory.addModelAdapter(String.class, new StringModelAdapter());
//		
//		CodeSyncCodePlugin.getInstance().getModelAdapterFactorySetProvider().getFactories().put(TECHNOLOGY, astModelAdapterFactory);
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
