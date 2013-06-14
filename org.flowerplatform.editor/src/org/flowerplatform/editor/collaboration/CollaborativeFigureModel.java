package org.flowerplatform.editor.collaboration;


/**
 * @flowerModelElementId _qb8sQKAzEeGkbI53l0cVuQ
 */
public class CollaborativeFigureModel {
	
	/**
	 * @flowerModelElementId _eQZDcKA1EeGkbI53l0cVuQ
	 */
	private int id;
	/**
	 * @flowerModelElementId _iJRkgKmAEeGe-9hyhocCYA
	 */
	private int x;
	/**
	 * @flowerModelElementId _k_4lQKmAEeGe-9hyhocCYA
	 */
	private int y;
	/**
	 * @flowerModelElementId _m7yRQKmAEeGe-9hyhocCYA
	 */
	private int width;
	/**
	 * @flowerModelElementId _pwChkKmAEeGe-9hyhocCYA
	 */
	private int height;
	/**
	 * @flowerModelElementId _sO2GUKmAEeGe-9hyhocCYA
	 */
	private int borderThickness;
	/**
	 * @flowerModelElementId _E5mlUKmAEeGe-9hyhocCYA
	 */
	private int borderColor;
	/**
	 * @flowerModelElementId _FxIqQKmAEeGe-9hyhocCYA
	 */
	private float backgroundAlpha;
	/**
	 * @flowerModelElementId _HYuTIKmAEeGe-9hyhocCYA
	 */
	private int backgroundColor;
	/**
	 * @flowerModelElementId _JQEPQKmAEeGe-9hyhocCYA
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