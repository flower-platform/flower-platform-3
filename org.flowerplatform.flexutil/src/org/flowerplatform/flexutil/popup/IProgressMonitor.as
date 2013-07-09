package org.flowerplatform.flexutil.popup {
	
	/**
	 * @author Cristina Contantinescu
	 */
	public interface IProgressMonitor {
		
		function setTitle(value:String):IProgressMonitor;
		function setAllowCancellation(value:Boolean):IProgressMonitor;
		function setLabel(value:String):IProgressMonitor;
		function setWorked(value:Number):IProgressMonitor;
		function setTotalWork(value:Number):IProgressMonitor;
		function setHandler(value:IProgressMonitorHandler):IProgressMonitor;
		
		function show():void;
		function close():void;
	}
}