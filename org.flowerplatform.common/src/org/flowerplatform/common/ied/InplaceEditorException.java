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
package org.flowerplatform.common.ied;

import org.flowerplatform.common.CommonPlugin;

/**
 * @author Mariana Gheorghe
 */
public class InplaceEditorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InplaceEditorException(String messageKey) {
		super(CommonPlugin.getInstance().getMessage(messageKey));
	}

}
