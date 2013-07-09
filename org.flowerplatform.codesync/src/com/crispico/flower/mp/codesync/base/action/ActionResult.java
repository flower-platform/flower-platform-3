package com.crispico.flower.mp.codesync.base.action;

/**
 * @flowerModelElementId _9TO04LQeEeCPJvLYgj5VDQ
 */
public class ActionResult {
	
	public boolean conflict;
	
	public boolean modifiedLeft;
	
	public boolean modifiedRight;
	
	/**
	 * The match key of the child object that was added or removed.
	 * 
	 * @author Mariana
	 */
	public Object childMatchKey;
	
	/**
	 * @author Mariana
	 */
	public boolean childAdded;

	public ActionResult(boolean conflict, boolean modifiedLeft, boolean modifiedRight) {
		super();
		this.conflict = conflict;
		this.modifiedLeft = modifiedLeft;
		this.modifiedRight = modifiedRight;
	}
	
	/**
	 * @author Mariana
	 */
	public ActionResult(boolean conflict, boolean modifiedLeft, boolean modifiedRight, Object childMatchKey, boolean childAdded) {
		this(conflict, modifiedLeft, modifiedRight);
		this.childMatchKey = childMatchKey;
		this.childAdded = childAdded;
	}
	
}
