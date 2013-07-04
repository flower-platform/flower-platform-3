package com.crispico.flower.util.popup {
	import org.flowerplatform.flexutil.popup.IProgressMonitor;
	import org.flowerplatform.flexutil.popup.IProgressMonitorFactory;

	/**
	 * @author Cristina Contantinescu
	 */
	public class ProgressMonitorDialogHandlerFactory implements IProgressMonitorFactory {		
		
		public function createProgressMonitor():IProgressMonitor {
			return new ProgressMonitorDialog();
		}
		
	}	
}