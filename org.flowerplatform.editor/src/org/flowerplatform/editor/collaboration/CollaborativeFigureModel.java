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
package org.flowerplatform.editor.collaboration;


/**
 * 
 */
public class CollaborativeFigureModel {
	
	/**
	 * 
	 */
	private int id;
	/**
	 * 
	 */
	private int x;
	/**
	 * 
	 */
	private int y;
	/**
	 * 
	 */
	private int width;
	/**
	 * 
	 */
	private int height;
	/**
	 * 
	 */
	private int borderThickness;
	/**
	 * 
	 */
	private int borderColor;
	/**
	 * 
	 */
	private float backgroundAlpha;
	/**
	 * 
	 */
	private int backgroundColor;
	/**
	 * 
	 */
	private String text;
	
	private String figureType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getBorderThickness() {
		return borderThickness;
	}
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
	}
	public int getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}
	public float getBackgroundAlpha() {
		return backgroundAlpha;
	}
	public void setBackgroundAlpha(float backgroundAlpha) {
		this.backgroundAlpha = backgroundAlpha;
	}
	public int getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getFigureType() {
		return figureType;
	}
	public void setFigureType(String figureType) {
		this.figureType = figureType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CollaborativeFigureModel))
			return false;
		CollaborativeFigureModel other = (CollaborativeFigureModel) obj;
		if (id != other.id)
			return false;
		return true;
	}
}