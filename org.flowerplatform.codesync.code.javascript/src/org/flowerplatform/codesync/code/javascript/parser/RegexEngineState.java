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
package org.flowerplatform.codesync.code.javascript.parser;

import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode;


/**
 * @author Mariana Gheorghe
 */
public class RegexEngineState {
	
	public String category;
	public RegExAstNode node;
	
	public RegexEngineState(String category, RegExAstNode node) {
		super();
		this.category = category;
		this.node = node;
	}

	@Override
	public String toString() {
		return category + " " + node;
	}
}
