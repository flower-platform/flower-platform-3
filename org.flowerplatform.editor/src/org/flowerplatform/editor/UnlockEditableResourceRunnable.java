package org.flowerplatform.editor;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.EditorStatefulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a runnable scheduled at a specified time to unlock a resource.
 * If the {@link EditableResource#getLockExpireTime()} is greater than the current time,
 * then runnable is rescheduled (because this means that the client renewed the lock
 * while this runnable was waiting).
 *  
 * @author Florin
 * @author Cristi
 * @flowerModelElementId _SsvZkLndEeGTi8MPVfmTnA
 */
public class UnlockEditableResourceRunnable implements Runnable {

	/**
	 * @flowerModelElementId _SswAoLndEeGTi8MPVfmTnA
	 */
	private static final Logger logger = LoggerFactory.getLogger(UnlockEditableResourceRunnable.class);
	
	/**
	 * @flowerModelElementId _SswAorndEeGTi8MPVfmTnA
	 */
	private Object editorInput;

	/**
	 * @flowerModelElementId _SswnsLndEeGTi8MPVfmTnA
	 */
	private EditableResourceClient lockOwner;
	
	/**
	 * Initial security context.
	 * 
	 * @flowerModelElementId _SswnsbndEeGTi8MPVfmTnA
	 */
	private AccessControlContext context;
	
	/**
	 * @flowerModelElementId _9PpUULooEeGbWKUNv6VenQ
	 * TODO CS/STFL cred ca atunci cand vom muta runnable-ul in ac. pachet, va avea acces la ac. camp prin serviciu direct
	 */
	private ScheduledExecutorService parentScheduler;
	
	private EditorStatefulService editorStatefulService;
	
	/**
	 * @flowerModelElementId _SsxOwLndEeGTi8MPVfmTnA
	 * @param editorStatefulService TODO
	 */
	public UnlockEditableResourceRunnable(EditorStatefulService editorStatefulService, Object editorInput, EditableResourceClient lockOwner, ScheduledExecutorService parentScheduler) {
		this.editorStatefulService = editorStatefulService;
		this.editorInput = editorInput;
		this.lockOwner = lockOwner;
		context = AccessController.getContext();
		this.parentScheduler = parentScheduler;
	}
	
	/**
	 * @flowerModelElementId _SsxOw7ndEeGTi8MPVfmTnA
	 */
	@Override
	public void run() {
		AccessController.doPrivileged(new PrivilegedAction<Void>() {

			@Override
			public Void run() {
				runWithInitialSecurityContext();
				return null;
			}
		}, context);
	}
	
	/**
	 * @flowerModelElementId _Ssx10rndEeGTi8MPVfmTnA
	 */
	public void runWithInitialSecurityContext() {
		// the try block is for the lock but for catching & logging exceptions as well
		try {
			EditableResource editableResource;
			
			// TODO CS/STFL de scapat de .toString(); de asemenea de scapat si de if-ul si codul legacy de mai sus
			editorStatefulService.namedLockPool.lock(editorInput.toString());
			editableResource = editorStatefulService.editableResources.get(editorInput.toString());
			
			if (editableResource == null) {
				logger.trace("Unlock timer expired, but EditableResource is gone. For Editable Resource with editorInput = {}, client = {}", editorInput, lockOwner.getCommunicationChannel());
				// the last client disconnected before the lock expired so there is no resource entry no more
				return;
			}
			// if the resource is still locked and the lock owner did not change
			if (editableResource.isLocked() && lockOwner.equals(editableResource.getLockOwner())) {					
				// lock expire time might have changed, let's check
				editableResource.getLockExpireTime();
				Calendar now = Calendar.getInstance();
				// if lockExpireTime <= now
				if (editableResource.getLockExpireTime().getTime() <= now.getTimeInMillis()) {
					logger.trace("Unlock timer expired; unlocking...; for Editable Resource with editorInput = {}, client = {}", editorInput, lockOwner.getCommunicationChannel());
					
					editorStatefulService.unlock(editableResource, lockOwner);
					editorStatefulService.dispatchEditableResourceStatus(editableResource);
				} else {
					long delay = editableResource.getLockExpireTime().getTime() - now.getTimeInMillis();
					parentScheduler.schedule(this, delay, TimeUnit.MILLISECONDS);
					if (logger.isTraceEnabled()) {
						logger.trace("Unlock timer expired but was rescheduled; for Editable Resource with editorInput = {}, client = {}. Lock expires = {} and now = {} => it will run again in {} ms", 
								new Object[] { editorInput, lockOwner.getCommunicationChannel(), editableResource.getLockExpireTime(), now.getTime(), delay});
					}
				}
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("Unlock timer exprired but won't unlock, for Editable Resource with editorInput = {}, client = {}. The resource isLocked = {} by client = {}", 
							new Object[] { editableResource.getEditorInput(), lockOwner.getCommunicationChannel(), editableResource.isLocked(), editableResource.getLockOwner().getCommunicationChannel() } );

				}
			}
		} catch (Throwable t) {
			// log here, because it doesn't get caught by the main app
			logger.error("Exception caught in scheduled runnable.", t);
			throw new RuntimeException(t);
		} finally {
			// TODO CS/STFL de scapat de .toString(); de asemenea de scapat si de if-ul si codul legacy de mai sus
			editorStatefulService.namedLockPool.unlock(editorInput.toString());
		}
	}
	
}