package org.flowerplatform.web.git.history.remote.dto;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.web.git.remote.dto.ViewInfoDto;

/**
 *	@author Cristina Constantinescu
 */
public class HistoryViewInfoDto extends ViewInfoDto {

	public static final int SHOWALLRESOURCE = 0;
	
	public static final int SHOWALLFOLDER = 1;
	
	public static final int SHOWALLPROJECT = 2;
	
	public static final int SHOWALLREPO = 3;
	
	private IResource resource;
	private File file;
	private Repository repository;
		
	private String path;
	
	private String info;
	
	private int filter;
	
	public HistoryViewInfoDto() {
		
	}
	
	public HistoryViewInfoDto(IResource resource, Repository repository) {		
		this.resource = resource;
		this.repository = repository;
	}
	
	public HistoryViewInfoDto(File file, Repository repository) {		
		this.file = file;
		this.repository = repository;
	}

	public IResource getResource() {
		return resource;
	}

	public File getFile() {
		return file;
	}

	public Repository getRepository() {
		return repository;
	}

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

	public void setRepository1(Repository repository) {
		this.repository = repository;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}			
	
}
