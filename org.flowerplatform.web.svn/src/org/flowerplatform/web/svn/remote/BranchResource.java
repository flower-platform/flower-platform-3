package org.flowerplatform.web.svn.remote;

import java.util.List;

import org.flowerplatform.communication.tree.remote.PathFragment;

/**
 * Holds information to be displayed in branch/tag wizard window on client.
 * 
 * @author Gabriela Murgoci
 * 
 * @flowerModelElementId _0N_4kGxTEeGmX-bWmnlzew
 */
public class BranchResource {
	
	/**
	 * @flowerModelElementId _1_VTUGxTEeGmX-bWmnlzew
	 */
	private Object path;
	
	/**
	 * @flowerModelElementId _3Lx4UGxTEeGmX-bWmnlzew
	 */
	private String name;
	
	/**
	 * @flowerModelElementId _AG-ssHJ-EeGZAqbcNpPqCg
	 */
	private String partialPath;

	/**
	 * @flowerModelElementId _k8gPAGxUEeGmX-bWmnlzew
	 */
	//private List<String> image;
	private String image;
	
	public Object getPath() {
		return path;
	}

	public void setPath(Object path) {
		this.path = path;
	}

	/**
	 * @flowerModelElementId _AHBwAXJ-EeGZAqbcNpPqCg
	 */
	public String getName() {
		return name;
	}

	/**
	 * @flowerModelElementId _AHCXEXJ-EeGZAqbcNpPqCg
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @flowerModelElementId _AHDlMXJ-EeGZAqbcNpPqCg
	 */
//	public List<String> getImage() {
//		return image;
//	}
	public String getImage() {
		return image;
	}

	/**
	 * @flowerModelElementId _AHEzUHJ-EeGZAqbcNpPqCg
	 */
//	public void setImage(List<String> image) {
//		this.image = image;
//	}
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @flowerModelElementId _AHGBcHJ-EeGZAqbcNpPqCg
	 */
	public String getPartialPath() {
		return partialPath;
	}

	/**
	 * @flowerModelElementId _AHGogXJ-EeGZAqbcNpPqCg
	 */
	public void setPartialPath(String partialPath) {
		this.partialPath = partialPath;
	}
		
}