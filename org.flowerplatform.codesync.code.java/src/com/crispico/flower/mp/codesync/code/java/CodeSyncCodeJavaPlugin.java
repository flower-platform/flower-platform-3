package com.crispico.flower.mp.codesync.code.java;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.java.JavaClassTitleProcessor;
import org.flowerplatform.editor.model.java.remote.JavaClassDiagramOperationsService;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.code.adapter.FolderModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.StringModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaAnnotationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaAnnotationTypeMemberDeclarationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaAttributeModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaEnumConstantDeclarationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaFileModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaMemberValuePairModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaModifierModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaOperationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaParameterModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter;

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
		
		EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classTitle", new JavaClassTitleProcessor());
		CodeSyncPlugin.getInstance().getFullyQualifiedNameProvider().addDelegateProvider(new JavaFullyQualifiedNameProvider());
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new JavaResourceChangeListener());
		JavaCore.addElementChangedListener(new JavaElementChangedListener(), ElementChangedEvent.POST_RECONCILE);
		
		CommunicationPlugin.getInstance().getServiceRegistry().registerService("classDiagramOperationsDispatcher", new JavaClassDiagramOperationsService());
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
