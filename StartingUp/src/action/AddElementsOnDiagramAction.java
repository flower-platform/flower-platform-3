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
package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import myPackage.MyEditor;

import java.util.ArrayList;
import java.util.List;

public class AddElementsOnDiagramAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        List<String> paths = new ArrayList();
        FileEditorManager.getInstance(e.getProject()).getAllEditors();
        FileEditor[] editorsList = FileEditorManager.getInstance(e.getProject()).getSelectedEditors();
        FileEditor editor = editorsList[0];
        VirtualFile vf = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        paths.add(vf.getPath());

        if (editor instanceof MyEditor){
            ((MyEditor)editor).getBrowser().executeJavascript("dragOnDiagram('" + paths + "')");
        }
        int i=0;

       // browser.execute("dragOnDiagram('" + paths + "')");
    }

    public void update(AnActionEvent e) {
        //TODO
//        if(isDiagramEditorOpen())
//            e.getPresentation().setEnabled(true);
//        else e.getPresentation().setEnabled(false);
    }
}
