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
