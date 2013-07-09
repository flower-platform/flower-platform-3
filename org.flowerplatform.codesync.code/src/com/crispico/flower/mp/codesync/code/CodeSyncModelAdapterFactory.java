package com.crispico.flower.mp.codesync.code;

import static com.crispico.flower.mp.codesync.code.CodeSyncElementTypeConstants.*;

import org.eclipse.emf.ecore.resource.Resource;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.code.adapter.AnnotationMemberModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.AnnotationModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.AnnotationValueModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.AttributeModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.ClassModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.CodeSyncElementModelAdapterAncestor;
import com.crispico.flower.mp.codesync.code.adapter.CodeSyncElementModelAdapterLeft;
import com.crispico.flower.mp.codesync.code.adapter.EnumConstantModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.ModifierModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.OperationModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.ParameterModelAdapter;
import com.crispico.flower.mp.codesync.code.adapter.StringModelAdapter;
import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.astcache.code.Annotation;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class CodeSyncModelAdapterFactory extends ModelAdapterFactory {

	private ModelAdapterFactory astModelAdapterFactory;
	private Resource resource;
	private boolean isLeft;
	
	public CodeSyncModelAdapterFactory(ModelAdapterFactory astModelAdapterFactory, Resource resource, boolean isLeft) {
		this.astModelAdapterFactory = astModelAdapterFactory;
		this.resource = resource;
		this.isLeft = isLeft;
		
		ClassModelAdapter typeModelAdapter = new ClassModelAdapter();
		addModelAdapter(CLASS, typeModelAdapter);
		addModelAdapter(INTERFACE, typeModelAdapter);
		addModelAdapter(ENUM, typeModelAdapter);
		addModelAdapter(ANNOTATION, typeModelAdapter);
		
		addModelAdapter(ATTRIBUTE, new AttributeModelAdapter());
		addModelAdapter(OPERATION, new OperationModelAdapter());
		addModelAdapter(ENUM_CONSTANT, new EnumConstantModelAdapter());
		addModelAdapter(ANNOTATION_MEMBER, new AnnotationMemberModelAdapter());
		
		addModelAdapter(Annotation.class, new AnnotationModelAdapter());
		addModelAdapter(AnnotationValue.class, new AnnotationValueModelAdapter());
		addModelAdapter(Modifier.class, new ModifierModelAdapter());
		addModelAdapter(Parameter.class, new ParameterModelAdapter());
		addModelAdapter(String.class, new StringModelAdapter());
	}
	
	private SyncElementModelAdapter createModelAdapter(SyncElementModelAdapter adapter) {
		return adapter
				.setModelAdapterFactory(this)
				.setEObjectConverter(astModelAdapterFactory)
				.setResource(resource);
	}
	
	@Override
	public IModelAdapter getModelAdapter(Object modelElement) {
		if (modelElement instanceof CodeSyncElement) {
			ModelAdapterEntry entry = modelAdapters.get(((CodeSyncElement) modelElement).getType());
			if (entry != null) {
				return entry.modelAdapter;
			}
		}
		return super.getModelAdapter(modelElement);
	}

	public ModelAdapterEntry addModelAdapter(String type, SyncElementModelAdapter modelAdapter) {
		ModelAdapterEntry e = new ModelAdapterEntry();
		e.modelAdapter = isLeft 
							? new CodeSyncElementModelAdapterLeft(createModelAdapter(modelAdapter)) 
							: new CodeSyncElementModelAdapterAncestor(createModelAdapter(modelAdapter));
		modelAdapters.put(type, e);
		return e;
	}
	
	public ModelAdapterEntry addModelAdapter(Class<?> clazz, SyncElementModelAdapter modelAdapter) {
		return super.addModelAdapter(clazz,
				isLeft 
					? createModelAdapter(modelAdapter)
					: createModelAdapter(modelAdapter));
	}
	
}
