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
package org.flowerplatform.codesync.regex.ide.remote;

/**
 * @author Cristina Constantinescu
 */
public class RegexIndexDto {

	private int nbLine;
	
	private int nbCharacter;

	public int getNbLine() {
		return nbLine;
	}

	public void setNbLine(int nbLine) {
		this.nbLine = nbLine;
	}

	public int getNbCharacter() {
		return nbCharacter;
	}

	public void setNbCharacter(int nbCharacter) {
		this.nbCharacter = nbCharacter;
	}

	@Override
	public String toString() {
		return "L" + (nbLine + 1) + " C" + (nbCharacter + 1);
	}		
	
}
