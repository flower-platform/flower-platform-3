package org.flowerplatform.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.blazeds.custom_serialization.CustomSerializationDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.remote.ContentTypeDescriptor;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.EditorStatefulService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditorPlugin extends AbstractFlowerJavaPlugin {

	private static final Logger logger = LoggerFactory.getLogger(EditorPlugin.class);
	
	protected static EditorPlugin INSTANCE;
	
	public static EditorPlugin getInstance() {
		return INSTANCE;
	}
	
	public static final String TREE_NODE_KEY_CONTENT_TYPE = "contentType";
	
	private Map<String, ContentTypeDescriptor> contentTypeDescriptorsMap;
	
	private List<ContentTypeDescriptor> contentTypeDescriptorsList;
	
	private Map<String, String> fileExtensionToContentTypeMap;
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;

		// content types
		contentTypeDescriptorsMap = new HashMap<String, ContentTypeDescriptor>();
		contentTypeDescriptorsList = new ArrayList<ContentTypeDescriptor>();

		int i = 0;
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.editor.contentType");
		for (IConfigurationElement configurationElement : configurationElements) {
			ContentTypeDescriptor descriptor = new ContentTypeDescriptor();
			descriptor.setContentType(configurationElement.getAttribute("contentType"));
			descriptor.setIndex(i++);
			if (contentTypeDescriptorsMap.put(descriptor.getContentType(), descriptor) != null) {
				throw new IllegalArgumentException("Content type " + descriptor.getContentType() + " is registered by more that one extension!");
			}
			contentTypeDescriptorsList.add(descriptor);
		}
		
		// content types to editor mappings
		configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.editor.contentTypeToEditorMapping");
		for (IConfigurationElement configurationElement : configurationElements) {
			String contentType = configurationElement.getAttribute("contentType");
			String compatibleEditor = configurationElement.getAttribute("compatibleEditor");
			String serviceId = configurationElement.getAttribute("serviceId");
			EditorStatefulService editorStatefulService = (EditorStatefulService) configurationElement.createExecutableExtension("editorStatefulService");
			editorStatefulService.setEditorName(compatibleEditor);
			CommunicationPlugin.getInstance().getServiceRegistry().registerService(serviceId, editorStatefulService);
			
			ContentTypeDescriptor descriptor = contentTypeDescriptorsMap.get(contentType);
			if (descriptor == null) {
				throw new IllegalArgumentException("Cannot find contentType = " + contentType + 
						" to register the compatible editor = " + compatibleEditor);
			}
			descriptor.getCompatibleEditors().add(compatibleEditor);
		}
		
		// file extensions to content types mappings
		fileExtensionToContentTypeMap = new HashMap<String, String>();
		configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.editor.fileExtensionToContentTypeMapping");
		for (IConfigurationElement configurationElement : configurationElements) {
			String contentType = configurationElement.getAttribute("contentType");
			String fileExtension = configurationElement.getAttribute("fileExtension");
			ContentTypeDescriptor descriptor = contentTypeDescriptorsMap.get(contentType);
			if (descriptor == null) {
				throw new IllegalArgumentException("Cannot find contentType = " + contentType + 
						" to associate with file extension = " + fileExtension);
			}
			if (fileExtensionToContentTypeMap.put(fileExtension, contentType) != null) {
				throw new IllegalArgumentException("Somebody already registered a content type for extension = " + fileExtension);
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Content types and compatible editors: {}", contentTypeDescriptorsList);
			logger.debug("File extensions to content types mappings: {}", fileExtensionToContentTypeMap);
		}
		
		// some custom serialzation descriptors
		new CustomSerializationDescriptor(EditableResource.class)
			.addDeclaredProperty("dirty")
			.addDeclaredProperty("lockOwner")
			.addDeclaredProperty("editorInput")
			.addDeclaredProperty("label")
			.addDeclaredProperty("lockUpdateTime")
			.addDeclaredProperty("masterEditorStatefulClientId")
			.addDeclaredProperty("iconUrl")
			.addDeclaredProperty("locked")
			.addDeclaredProperty("lockExpireTime")
			.addDeclaredProperty("editorStatefulClientId")
			.register();
	
		new CustomSerializationDescriptor(EditableResourceClient.class)
			.addDeclaredProperty("name")
			.addDeclaredProperty("login")
			.addDeclaredProperty("clientId")
			.register();

	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	public Map<String, ContentTypeDescriptor> getContentTypeDescriptorsMap() {
		return contentTypeDescriptorsMap;
	}

	public List<ContentTypeDescriptor> getContentTypeDescriptorsList() {
		return contentTypeDescriptorsList;
	}

	public Map<String, String> getFileExtensionToContentTypeMap() {
		return fileExtensionToContentTypeMap;
	}

}
