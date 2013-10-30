/**
 * 
 */
package org.flowerplatform.codesync.remote;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class RelationDescriptor {

	public static final String OPEN_ARROW = "open_arrow";
	public static final String CLOSED_ARROW = "closed_arrow";
	public static final String FILLED_ARROW = "filled_arrow";
	public static final String DIAMOND = "diamond";
	public static final String FILLED_DIAMOND = "filled_diamond";
	
	private String type;
	
	private String label;
	
	private String iconUrl;
	
	private List<String> sourceCodeSyncTypes;
	
	private List<String> targetCodeSyncTypes;
	
	private List<String> sourceCodeSyncTypeCategories;
	
	private List<String> targetCodeSyncTypeCategories;

	private String sourceEndFigureType;
	
	private String targetEndFigureType;
	
	public String getType() {
		return type;
	}

	public RelationDescriptor setType(String type) {
		this.type = type;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public RelationDescriptor setLabel(String label) {
		this.label = label;
		return this;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public RelationDescriptor setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
		return this;
	}

	public List<String> getSourceCodeSyncTypes() {
		return sourceCodeSyncTypes;
	}

	public RelationDescriptor setSourceCodeSyncTypes(List<String> sourceCodeSyncTypes) {
		this.sourceCodeSyncTypes = sourceCodeSyncTypes;
		return this;
	}

	public RelationDescriptor addSourceCodeSyncType(String value) {
		if (sourceCodeSyncTypes == null) {
			sourceCodeSyncTypes = new ArrayList<String>();
		}
		sourceCodeSyncTypes.add(value);
		return this;
	}

	public List<String> getTargetCodeSyncTypes() {
		return targetCodeSyncTypes;
	}

	public RelationDescriptor setTargetCodeSyncTypes(List<String> targetCodeSyncTypes) {
		this.targetCodeSyncTypes = targetCodeSyncTypes;
		return this;
	}

	public RelationDescriptor addTargetCodeSyncType(String value) {
		if (targetCodeSyncTypes == null) {
			targetCodeSyncTypes = new ArrayList<String>();
		}
		targetCodeSyncTypes.add(value);
		return this;
	}

	public List<String> getSourceCodeSyncTypeCategories() {
		return sourceCodeSyncTypeCategories;
	}

	public RelationDescriptor setSourceCodeSyncTypeCategories(
			List<String> sourceCodeSyncTypeCategories) {
		this.sourceCodeSyncTypeCategories = sourceCodeSyncTypeCategories;
		return this;
	}

	public RelationDescriptor addSourceCodeSyncTypeCategory(String value) {
		if (sourceCodeSyncTypeCategories == null) {
			sourceCodeSyncTypeCategories = new ArrayList<String>();
		}
		sourceCodeSyncTypeCategories.add(value);
		return this;
	}

	public List<String> getTargetCodeSyncTypeCategories() {
		return targetCodeSyncTypeCategories;
	}

	public RelationDescriptor setTargetCodeSyncTypeCategories(
			List<String> targetCodeSyncTypeCategories) {
		this.targetCodeSyncTypeCategories = targetCodeSyncTypeCategories;
		return this;
	}

	public RelationDescriptor addTargetCodeSyncTypeCategory(String value) {
		if (targetCodeSyncTypeCategories == null) {
			targetCodeSyncTypeCategories = new ArrayList<String>();
		}
		targetCodeSyncTypeCategories.add(value);
		return this;
	}

	public String getSourceEndFigureType() {
		return sourceEndFigureType;
	}

	public RelationDescriptor setSourceEndFigureType(String sourceEndFigureType) {
		this.sourceEndFigureType = sourceEndFigureType;
		return this;
	}

	public String getTargetEndFigureType() {
		return targetEndFigureType;
	}

	public RelationDescriptor setTargetEndFigureType(String targetEndFigureType) {
		this.targetEndFigureType = targetEndFigureType;
		return this;
	}
		
}
