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
package org.flowerplatform.editor.model.teneo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;

public class FlowerTeneoChangeRecorder extends ChangeRecorder {

	public List<Notifier> getTargetObjects() {
		return targetObjects;
	}
	
	@Override
	public void setTarget(Notifier target) {
		if (!targetObjects.add(target)) {
			throw new IllegalStateException("The target should not be set more than once");
		}

//		if (loadingTargets) {
			originalTargetObjects.add(target);
//		}
	}

	@Override
	protected void addAdapter(Notifier notifier) {
		// do nothing
	}

	@Override
	public void beginRecording(ChangeDescription changeDescription,
			Collection<?> rootObjects) {
		super.beginRecording(changeDescription, Collections.emptyList());
	}

	@Override
	public void notifyChanged(Notification notification) {
		// TODO Auto-generated method stub
		super.notifyChanged(notification);
	}

}