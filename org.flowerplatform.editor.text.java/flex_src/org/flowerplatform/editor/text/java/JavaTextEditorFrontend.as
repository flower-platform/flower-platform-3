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