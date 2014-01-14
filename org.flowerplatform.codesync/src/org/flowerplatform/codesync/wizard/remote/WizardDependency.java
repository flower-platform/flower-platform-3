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
package org.flowerplatform.codesync.wizard.remote;


/**
 * @author Cristina Constantinescu
 */
public class WizardDependency {

	private String type;
	
	private String label;
		
	private String targetLabel;
	
	private String targetIconUrl;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WizardDependency setTypeAs(String type) {
		this.type = type;
		return this;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public WizardDependency setLabelAs(String label) {
		this.label = label;
		return this;
	}
	
	public String getTargetLabel() {
		return targetLabel;
	}

	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}

	public WizardDependency setTargetLabelAs(String targetLabel) {
		this.targetLabel = targetLabel;
		return this;
	}
	
	public String getTargetIconUrl() {
		return targetIconUrl;
	}

	public void setTargetIconUrl(String targetIconUrl) {
		this.targetIconUrl = targetIconUrl;
	}

	public WizardDependency setTargetIconUrlAs(String targetIconUrl) {
		this.targetIconUrl = targetIconUrl;
		return this;
	}
	
	public WizardDependency() {	
	}
		
}
