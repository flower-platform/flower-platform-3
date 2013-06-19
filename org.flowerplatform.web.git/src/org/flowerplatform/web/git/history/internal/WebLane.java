package org.flowerplatform.web.git.history.internal;

import org.eclipse.jgit.revplot.PlotLane;

/**
 *	@author Cristina Constantinescu
 */
public class WebLane extends PlotLane {

	private static final long serialVersionUID = 1L;
	
	String color;
	
	public WebLane(String color) {
		this.color = color;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && color.equals(((WebLane) o).color);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ color.hashCode();
	}
	
}
