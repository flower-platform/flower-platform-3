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
package org.flowerplatform.editor.text.java {
	import com.crispico.flower.texteditor.java.partitioning.JavaPartitionScanner;
	import com.crispico.flower.texteditor.java.providers.JavaPartitionTokenFormatProvider;
	import com.crispico.flower.texteditor.java.providers.JavaPartitionTokenizerProvider;
	
	import org.flowerplatform.editor.text.TextEditorFrontend;
	
	public class JavaTextEditorFrontend extends TextEditorFrontend {
		
		public function JavaTextEditorFrontend() {
			super();
			partitionScanner = new JavaPartitionScanner();
			partitionTokenizerProvider = new JavaPartitionTokenizerProvider();
			partitionTokenFormatProvider = new JavaPartitionTokenFormatProvider();
//			hyperlinkManager = new JavaHyperlinkManager(this);
		}
		
	}
}
	
//import com.crispico.flower.texteditor.hyperlink.HyperlinkManager;
//import com.crispico.flower.texteditor.java.JavaContentTypeConstants;
//import com.crispico.flower.texteditor.java.providers.JavaPartitionTokenizerProvider;
//
//class JavaHyperlinkManager extends HyperlinkManager {
//	
//	private var editorContainer:JavaTextEditorFrontend;
//	
//	public function JavaHyperlinkManager(editorContainer:JavaTextEditorFrontend):void {
//		super();
//		hyperlinkTypes.addItem(JavaContentTypeConstants.JAVA_DEFAULT_DEFAULT);
//		partitionTokenizerProvider = new JavaPartitionTokenizerProvider();
//		
//		this.editorContainer = editorContainer;
//	}
//	
//	override public function hyperlinkClickedHandler(absoluteOffset:int):void {
//		JavaTextEditorStatefulClient(editorContainer.editorStatefulClient).service_navigateHyperlink(absoluteOffset);
//	}
//	
//	override public function mouseHoverHandler(absoluteOffset:int):void {
//		JavaTextEditorStatefulClient(editorContainer.editorStatefulClient).service_getHoverText(absoluteOffset, new ServiceInvocationOptions().setResultCallbackFunction(
//			function (result:String):void {
//				setHoverDetails(result);
//			}
//		));
//	}
//}