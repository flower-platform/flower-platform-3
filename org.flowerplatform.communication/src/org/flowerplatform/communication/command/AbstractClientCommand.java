package org.flowerplatform.communication.command;

/**
 * Abstract subclass of the JavaToFlex command that adds its
 * class name in the toString() implementation. Children should use it
 * if they override toString().
 * 
 * @author Cristi
 * @flowerModelElementId _psIneMh-Ed6PXZq6ZItcPA
 */
public abstract class AbstractClientCommand {

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
