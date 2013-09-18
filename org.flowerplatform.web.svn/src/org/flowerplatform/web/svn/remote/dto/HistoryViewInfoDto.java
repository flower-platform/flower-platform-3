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
package org.flowerplatform.web.svn.remote.dto;

import java.io.File;

import org.eclipse.core.resources.IResource;

/**
 *	@author Victor Badila
 */
public class HistoryViewInfoDto {

	public static final int SHOWALLRESOURCE = 0;
	
	public static final int SHOWALLFOLDER = 1;
	
	public static final int SHOWALLPROJECT = 2;
	
	public static final int SHOWALLREPO = 3;
	
	private IResource resource;
	private File file;
	//private Repository repository;
		
	private String path;
	
	private String info;
	
	private int filter;
	
private String repositoryLocation;
	
	private String repositoryName;
	
	private Object selectedObject;
	
	private boolean isResource;
	
	private String statefulClientId;

	public String getRepositoryLocation() {
		return repositoryLocation;
	}

	public void setRepositoryLocation(String repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public Object getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(Object selectedObject) {
		this.selectedObject = selectedObject;
	}

	public boolean getIsResource() {
		return isResource;
	}
	
	public void setIsResource(boolean isResource) {
		this.isResource = isResource;
	}

	public String getStatefulClientId() {
		return statefulClientId;
	}

	public void setStatefulClientId(String statefulClientId) {
		this.statefulClientId = statefulClientId;
	}
	
	public HistoryViewInfoDto() {
		
	}
	
	public HistoryViewInfoDto(IResource resource) {		
		this.resource = resource;
		//this.repository = repository;
	}
	
	public HistoryViewInfoDto(File file) {		
		this.file = file;
		//this.repository = repository;
	}

	public IResource getResource() {
		return resource;
	}

	public File getFile() {
		return file;
	}

//	public Repository getRepository() {
//		return repository;
//	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getFilter() {
		return filter;
	}

	public void setFilter(int filter) {
		this.filter = filter;
	}

	public void setResource1(IResource resource) {
		this.resource = resource;
	}

	public void setFile1(File file) {
		this.file = file;
	}

//	public void setRepository1(Repository repository) {
//		this.repository = repository;
//	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}			
	
}