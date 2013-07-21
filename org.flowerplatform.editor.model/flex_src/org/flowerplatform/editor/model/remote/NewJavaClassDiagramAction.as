package org.flowerplatform.editor.model.remote {

	/**
	 * @author Mariana Gheorghe
	 */
	[RemoteClass(alias="org.flowerplatform.editor.model.java.remote.NewJavaClassDiagramAction")]
	public class NewJavaClassDiagramAction extends NewDiagramAction {
		
		public function NewJavaClassDiagramAction() {
			super();
			
			label = "New Class Diagram";
			name = "NewDiagram.notation";
		}
	}
}