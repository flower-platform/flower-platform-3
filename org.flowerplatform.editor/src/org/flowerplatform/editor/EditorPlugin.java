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
package org.flowerplatform.editor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.blazeds.custom_serialization.CustomSerializationDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.file.IFileAccessController;
import org.flowerplatform.editor.remote.ContentTypeDescriptor;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.EditorStatefulService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Cristian Spiescu
 */
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
	
	private List<EditorStatefulService> editorStatefulServices = new ArrayList<EditorStatefulService>();
		
	private IFileAccessController fileAccessController;
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
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
		
		List<Editor> editors = new ArrayList<Editor>();
		
		// content types to editor mappings
		configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.editor.contentTypeToEditorMapping");
		for (IConfigurationElement configurationElement : configurationElements) {
			String contentType = configurationElement.getAttribute("contentType");
			String compatibleEditor = configurationElement.getAttribute("compatibleEditor");
			String serviceId = configurationElement.getAttribute("serviceId");
					
			boolean isDefaultEditor = Boolean.parseBoolean(configurationElement.getAttribute("isDefaultEditor"));
			
			int priorityEditor;
			if (configurationElement.getAttribute("priority") != null) {
				priorityEditor = Integer.parseInt(configurationElement.getAttribute("priority"));
			} else { // no priority, consider it the latest
				priorityEditor = Integer.MAX_VALUE;
			}
			editors.add(new Editor(compatibleEditor, contentType, isDefaultEditor, priorityEditor));
			
			EditorStatefulService editorStatefulService = (EditorStatefulService) configurationElement.createExecutableExtension("editorStatefulService");
			editorStatefulService.setEditorName(compatibleEditor);
			CommunicationPlugin.getInstance().getServiceRegistry().registerService(serviceId, editorStatefulService);
			editorStatefulServices.add(editorStatefulService);						
		}
		// sort editors based on priority (smallest priority -> editor is shown first in Open With menu)
		Collections.sort(editors, new Comparator<Editor>() {

			@Override
			public int compare(Editor editor1, Editor editor2) {				
				return Integer.compare(editor1.priority, editor2.priority);
			}			
		});
		
		for (Editor editor : editors) {
			if (editor.isDefault) { // default editors are added to all registered content types
				for (String contentType : contentTypeDescriptorsMap.keySet()) {
					addEditorToContentTypeDescriptor(editor.name, contentType);
				}
			} else {
				addEditorToContentTypeDescriptor(editor.name, editor.contentType);
			}
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
			.addDeclaredProperty("communicationChannelId")
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

	public List<EditorStatefulService> getEditorStatefulServices() {
		return editorStatefulServices;
	}
			
	public IFileAccessController getFileAccessController() {
		return fileAccessController;
	}

	public void setFileAccessController(IFileAccessController fileAccessController) {
		this.fileAccessController = fileAccessController;
	}

	public EditorStatefulService getEditorStatefulServiceByEditorName(String editorName) {
		for (EditorStatefulService editorStatefulService : editorStatefulServices)
			if (editorStatefulService.getEditorName().equals(editorName))
				return editorStatefulService;
		return null;
	}
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public String getContentTypeFromFileName(String fileName) {		
		String contentType = null;
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex >= 0) {
			// has an extension
			String extension = fileName.substring(lastDotIndex + 1);
			contentType = EditorPlugin.getInstance().getFileExtensionToContentTypeMap().get(extension);	
			if (contentType == null) { // not registered as extension, search it using *
				contentType = EditorPlugin.getInstance().getFileExtensionToContentTypeMap().get("*");				
			}
		}
		return contentType;
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	private void addEditorToContentTypeDescriptor(String editorName, String contentType) {
		ContentTypeDescriptor descriptor = contentTypeDescriptorsMap.get(contentType);
		if (descriptor == null) {
			throw new IllegalArgumentException("Cannot find contentType = " + contentType + 
					" to register the compatible editor = " + editorName);
		}
		descriptor.getCompatibleEditors().add(editorName);
	}
	
	public String getFriendlyNameDecoded(String friendlyName) {
		try {
			friendlyName = URLDecoder.decode(friendlyName, "UTF-8");		
		} catch (UnsupportedEncodingException e) {
			logger.error("Could not decode using UTF-8 charset : " + friendlyName);
		}
		return friendlyName;
	}
	
	private class Editor {
		
		public String name;
		public String contentType;
		public boolean isDefault;
		public int priority;
		
		public Editor(String name, String contentType, boolean isDefault, int priority) {			
			this.name = name;
			this.contentType = contentType;
			this.isDefault = isDefault;
			this.priority = priority;
		}		
	}

}
