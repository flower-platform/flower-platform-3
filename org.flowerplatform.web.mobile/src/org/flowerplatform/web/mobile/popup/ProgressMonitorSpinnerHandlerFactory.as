package org.flowerplatform.web.mobile.popup {
	import org.flowerplatform.flexutil.popup.IProgressMonitor;
	import org.flowerplatform.flexutil.popup.IProgressMonitorFactory;
	
	/**
	 * @author Cristina Contantinescu
	 */
	public class ProgressMonitorSpinnerHandlerFactory implements IProgressMonitorFactory {
				
		public function createProgressMonitor():IProgressMonitor {			
			return new ProgressMonitorSpinner();
		}
		
	}
}