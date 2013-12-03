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
package com.crispico.flower.mp.codesync.code;

import org.flowerplatform.communication.channel.CommunicationChannel;

import com.crispico.flower.mp.codesync.base.ICodeSyncAlgorithmRunner;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class CodeSyncAlgorithmRunner implements ICodeSyncAlgorithmRunner {

	@Override
	public void runCodeSyncAlgorithm(Object project, Object resource, String technology, CommunicationChannel communicationChannel, boolean showDialog) {
		CodeSyncElement cse = CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, resource, technology, communicationChannel, showDialog);
	}

}