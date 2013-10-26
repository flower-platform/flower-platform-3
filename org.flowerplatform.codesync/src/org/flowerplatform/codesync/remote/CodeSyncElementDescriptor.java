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

import org.flowerplatform.codesync.operation_extension.FeatureAccessExtension;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncElementDescriptor {

	private String codeSyncType;
	
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
	 * For notes, the value is <code>false</code>
	 * ("use the create new element general logic, but don't create 
	 * {@link CodeSyncElement} in model").
	 * 
	 * @author Cristina Constantinescu
	 */
	private boolean createCodeSyncElement;
	
	public CodeSyncElementDescriptor() {
		codeSyncTypeCategories = new ArrayList<String>();
		childrenCodeSyncTypeCategories = new ArrayList<String>();
		features = new ArrayList<String>();
		features.add(FeatureAccessExtension.CODE_SYNC_NAME);
		
		createCodeSyncElement = true;
	}
	
	public String getCodeSyncType() {
		return codeSyncType;
	}

	public CodeSyncElementDescriptor setCodeSyncType(String codeSyncType) {
		this.codeSyncType = codeSyncType;
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
	
	/**
	 * @author Cristina Constantinescu
	 */
	public boolean getCreateCodeSyncElement() {
		return createCodeSyncElement;
	}

	/**
	 * @author Cristina Constantinescu
	 */
	public CodeSyncElementDescriptor setCreateCodeSyncElement(boolean createCodeSyncElement) {
		this.createCodeSyncElement = createCodeSyncElement;
		return this;
	}
	
}
