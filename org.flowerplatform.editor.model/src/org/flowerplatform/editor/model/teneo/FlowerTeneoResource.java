package org.flowerplatform.editor.model.teneo;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.teneo.hibernate.resource.HibernateResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowerTeneoResource extends HibernateResource {

	private static final Logger logger = LoggerFactory.getLogger(FlowerTeneoResource.class);
	
	protected FlowerTeneoChangeRecorder changeRecorder;
	
	public FlowerTeneoResource(URI uri) {
		super(uri);
	}

	public FlowerTeneoChangeRecorder getChangeRecorder() {
		return changeRecorder;
	}

	protected void addChangeRecorderIfNeeded(EObject eObject) {
		if (changeRecorder == null) {
			changeRecorder = new FlowerTeneoChangeRecorder();
			changeRecorder.beginRecording(null);
		}
		if (!eObject.eAdapters().contains(changeRecorder)) {
			eObject.eAdapters().add(changeRecorder);
			if (logger.isDebugEnabled()) {
				logger.debug("Added Change Recorder({} targets) to: {}", changeRecorder.getTargetObjects().size(), eObject);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Added Change Recorder({} targets) NOT NEEDED for {}", changeRecorder.getTargetObjects().size(), eObject);
			}
		}
	}
	
	/**
	 * {@link #attached(EObject)} calls this method.
	 * 
	 * <p>
	 * For a new object tree (not yet persisted in the DB), the mechanism of {@link ResourceImpl} seems to work, therefore this
	 * method is invoked for every single object in the hierarchy. When loading by ID an object that is not "root", this method
	 * is called only for the "root" object from the hierarchy.
	 * 
	 * <p>
	 * We override this method instead of {@link #attached(EObject)} because {@link #attached(EObject)} gets called twice for the
	 * one particular case, probably because of a bug. When loading by a ID an object that is not "root", {@link #attached(EObject)}
	 * is called twice for the root. However this method (invoked from {@link #attached(EObject)}) has an additional check and will only
	 * be invoked once.
	 * 
	 * @see #setEResource(InternalEObject, boolean)
	 */
	@Override
	public void addedEObject(EObject eObject) {
		super.addedEObject(eObject);
		addChangeRecorderIfNeeded(eObject);
	}

	/**
	 * This method is invoked when loading an object by ID. When this happens, it's whole hierarchy is retrieved from the DB. So the
	 * children know their <code>eContainer</code>. However, the opposite is not true. I.e. the parents don't know the children, because
	 * they sit in a lazy loaded list, which is not loaded.
	 * 
	 * <p>
	 * The original logic, invokes {@link #attached(EObject)} for the "root" of this object. However, because of the above, attached won't
	 * be called for the other objects (e.g. this, this.eContainer(), this.eContainer().eContainer(), etc.). That's why we override this
	 * method and we climb the hierarchy to add the recorder.
	 * 
	 * @see #addedEObject(EObject)
	 */
	@Override
	public void setEResource(InternalEObject eobj, boolean force) {
		super.setEResource(eobj, force);
		while (eobj.eContainer() != null) {
			// this loop won't contain the "root", because it will be handled by
			// #attached() and #addedEObject()
			addChangeRecorderIfNeeded(eobj);
			eobj = (InternalEObject) eobj.eContainer();
		}
	}

	@Override
	protected void doUnload() {
		super.doUnload();
		if (changeRecorder != null) {
			changeRecorder.dispose();
		}
	}

	@Override
	protected EObject getEObjectByID(String id) {
		EObject result = super.getEObjectByID(id);
		// TODO CS/FP2 oricum aici ar trebui sa lucreze si cu arborescenta
		addChangeRecorderIfNeeded(result);
		return result;
	}

}
