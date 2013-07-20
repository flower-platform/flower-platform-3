package org.flowerplatform.editor.model.java;

/**
 * @author Mariana Gheorghe
 */
public class JavaClassOperationProcessor extends JavaClassChildProcessor {

	@Override
	protected String getImageForVisibility(int type) {
		switch (type) {
		case org.eclipse.jdt.core.dom.Modifier.PUBLIC:		return "images/obj16/SyncOperation_public.gif";
		case org.eclipse.jdt.core.dom.Modifier.PROTECTED:	return "images/obj16/SyncOperation_protected.gif";
		case org.eclipse.jdt.core.dom.Modifier.PRIVATE:		return "images/obj16/SyncOperation_private.gif";
		default: 											return "images/obj16/SyncOperation_package.gif";
		}
	}


}
