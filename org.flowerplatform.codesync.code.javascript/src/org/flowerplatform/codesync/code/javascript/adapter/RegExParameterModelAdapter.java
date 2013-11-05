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
package org.flowerplatform.codesync.code.javascript.adapter;

import java.util.List;

import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;

import com.crispico.flower.mp.codesync.merge.EObjectModelAdapter;

/**
 * Mapped to {@link RegExAstNodeParameter}.
 * 
 * @author Mariana Gheorghe
 */
public class RegExParameterModelAdapter extends EObjectModelAdapter {

	@Override
	public String getLabel(Object modelElement) {
		RegExAstNodeParameter parameter = (RegExAstNodeParameter) modelElement;
		return parameter.getName() + " = " + parameter.getValue();
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return null;
	}

	@Override
	public Object getMatchKey(Object element) {
		return ((RegExAstNodeParameter) element).getName();
	}
	
}
