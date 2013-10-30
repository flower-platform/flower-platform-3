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
package org.flowerplatform.codesync.remote;

import java.util.ArrayList;
import java.util.List;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class CodeSyncElementDescriptor {

	private String codeSyncType;
	
	private List<String> initializationTypes;
	
	private List<String> initializationTypesLabels;
	
	private List<Long> initializationTypesOrderIndexes;
	
	private String label;
	
	private String iconUrl;
	
	private String defaultName;
	
	// temp
	private String extension;
	
	private List<String> codeSyncTypeCategories;
	
	private List<String> childrenCodeSyncTypeCategories;
	
	private String category;
	
	private List<String> features;
	
	private String keyFeature;
	
	private String nextSiblingSeparator;
	
	private String standardDiagramControllerProviderFactory;
	
	/**
	 * If <code>true</code>, a corresponding {@link CodeSyncElement} 
	 * will be created in resource.
	 * 
	 * <p>
	 * E.g. for notes, the value is <code>false</code>
	 * ("use the create new element general logic, but don't create 
	 * {@link CodeSyncElement} in model").	 
	 */
	private boolean createCodeSyncElement;
	
	private String inplaceEditorFeature;
	
	private long orderIndex;
	
	public CodeSyncElementDescriptor() {
		// TODO CS/JS I'd recommend some lists lazy, cf. initializationTypes; maybe categories: leave always init
		codeSyncTypeCategories = new ArrayList<String>();
		childrenCodeSyncTypeCategories = new ArrayList<String>();
		features = new ArrayList<String>();
		
		createCodeSyncElement = true;
	}
	
	public String getCodeSyncType() {
		return codeSyncType;
	}

	public CodeSyncElementDescriptor setCodeSyncType(String codeSyncType) {
		this.codeSyncType = codeSyncType;
		return this;
	}
	
	public List<String> getInitializationTypes() {
		return initializationTypes;
	}

	public CodeSyncElementDescriptor setInitializationTypes(List<String> initializationTypes) {
		this.initializationTypes = initializationTypes;
		return this;
	}

	public CodeSyncElementDescriptor addInitializationType(String value) {
		if (initializationTypes == null) {
			initializationTypes = new ArrayList<String>();
		}
		initializationTypes.add(value);
		return this;
	}
	
	public List<String> getInitializationTypesLabels() {
		return initializationTypesLabels;
	}

	public CodeSyncElementDescriptor setInitializationTypesLabels(List<String> initializationTypesLabels) {
		this.initializationTypesLabels = initializationTypesLabels;
		return this;
	}

	public CodeSyncElementDescriptor addInitializationTypeLabel(String value) {
		if (initializationTypesLabels == null) {
			initializationTypesLabels = new ArrayList<String>();
		}
		initializationTypesLabels.add(value);
		return this;
	}
		
	public List<Long> getInitializationTypesOrderIndexes() {
		return initializationTypesOrderIndexes;
	}

	public CodeSyncElementDescriptor setInitializationTypesOrderIndexes(List<Long> initializationTypesOrderIndexes) {
		this.initializationTypesOrderIndexes = initializationTypesOrderIndexes;
		return this;
	}

	public CodeSyncElementDescriptor addInitializationTypesOrderIndexes(long value) {
		if (initializationTypesOrderIndexes == null) {
			initializationTypesOrderIndexes = new ArrayList<Long>();
		}
		initializationTypesOrderIndexes.add(value);
		return this;
	}
	
	public String getLabel() {
		return label;
	}

	public CodeSyncElementDescriptor setLabel(String label) {
		this.label = label;
		return this;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public CodeSyncElementDescriptor setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
		return this;
	}

	public String getDefaultName() {
		return defaultName;
	}

	public CodeSyncElementDescriptor setDefaultName(String defaultName) {
		this.defaultName = defaultName;
		return this;
	}

	public String getExtension() {
		return extension;
	}

	public CodeSyncElementDescriptor setExtension(String extension) {
		this.extension = extension;
		return this;
	}

	public List<String> getCodeSyncTypeCategories() {
		return codeSyncTypeCategories;
	}

	public CodeSyncElementDescriptor setCodeSyncTypeCategories(List<String> codeSyncTypeCategories) {
		this.codeSyncTypeCategories = codeSyncTypeCategories;
		return this;
	}
	
	public CodeSyncElementDescriptor addCodeSyncTypeCategory(String codeSyncTypeCategory) {
		codeSyncTypeCategories.add(codeSyncTypeCategory);
		return this;
	}

	public List<String> getChildrenCodeSyncTypeCategories() {
		return childrenCodeSyncTypeCategories;
	}

	public CodeSyncElementDescriptor setChildrenCodeSyncTypeCategories(
			List<String> childrenCodeSyncTypeCategories) {
		this.childrenCodeSyncTypeCategories = childrenCodeSyncTypeCategories;
		return this;
	}
	
	public CodeSyncElementDescriptor addChildrenCodeSyncTypeCategory(String childrenCodeSyncTypeCategory) {
		childrenCodeSyncTypeCategories.add(childrenCodeSyncTypeCategory);
		return this;
	}
	
	public String getCategory() {
		return category;
	}

	public CodeSyncElementDescriptor setCategory(String category) {
		this.category = category;
		return this;
	}

	public List<String> getFeatures() {
		return features;
	}

	public CodeSyncElementDescriptor setFeatures(List<String> features) {
		this.features = features;
		return this;
	}
	
	public CodeSyncElementDescriptor addFeature(String feature) {
		features.add(feature);
		return this;
	}
	
	public String getKeyFeature() {
		return keyFeature;
	}
	
	public CodeSyncElementDescriptor setKeyFeature(String keyFeature) {
		this.keyFeature = keyFeature;
		return this;
	}

	public String getNextSiblingSeparator() {
		return nextSiblingSeparator;
	}

	public CodeSyncElementDescriptor setNextSiblingSeparator(String nextSiblingSeparator) {
		this.nextSiblingSeparator = nextSiblingSeparator;
		return this;
	}

	public String getStandardDiagramControllerProviderFactory() {
		return standardDiagramControllerProviderFactory;
	}

	public CodeSyncElementDescriptor setStandardDiagramControllerProviderFactory(
			String standardDiagramControllerProviderFactory) {
		this.standardDiagramControllerProviderFactory = standardDiagramControllerProviderFactory;
		return this;
	}	
	
	public boolean getCreateCodeSyncElement() {
		return createCodeSyncElement;
	}

	public CodeSyncElementDescriptor setCreateCodeSyncElement(boolean createCodeSyncElement) {
		this.createCodeSyncElement = createCodeSyncElement;
		return this;
	}

	public String getInplaceEditorFeature() {
		return inplaceEditorFeature;
	}

	public CodeSyncElementDescriptor setInplaceEditorFeature(String inplaceEditorFeature) {
		this.inplaceEditorFeature = inplaceEditorFeature;
		return this;
	}

	public long getOrderIndex() {
		return orderIndex;
	}

	public CodeSyncElementDescriptor setOrderIndex(long orderIndex) {
		this.orderIndex = orderIndex;
		return this;
	}
	
}
