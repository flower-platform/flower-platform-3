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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import myPackage.FlowerDiagramEditor;

import java.util.ArrayList;
import java.util.List;

public class AddElementsOnDiagramAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        List<String> paths = new ArrayList();
        Integer innt = null;
        VirtualFile vf = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        paths.add(vf.getPath());

        FlowerDiagramEditor editor = findLastOpendDiagramEditor(e.getProject());
        if (editor != null){
            editor.getBrowser().executeJavascript("dragOnDiagram('" + paths + "')");
        }
    }

    public void update(AnActionEvent e) {
        //TODO
        FileEditor[] openedEditors = FileEditorManager.getInstance(e.getProject()).getAllEditors();
        for (int i = 0; i < openedEditors.length; i++) {
            if (openedEditors[i] instanceof FlowerDiagramEditor){
                e.getPresentation().setEnabled(true);
                return;
            }
        }
        e.getPresentation().setEnabled(false);
        return;
    }

    protected FlowerDiagramEditor findLastOpendDiagramEditor(Project project) {
        FlowerDiagramEditor flowerEditor = null;

        FileEditor[] editorsList = FileEditorManager.getInstance(project).getSelectedEditors();
        FileEditor activeEditor = editorsList[0];

        if (activeEditor instanceof FlowerDiagramEditor) {
            return (FlowerDiagramEditor)activeEditor;
        }else {
              FileEditor[] openedEditors = FileEditorManager.getInstance(project).getAllEditors();
            for (int i = openedEditors.length - 1; i >= 0 ; i--) {
                FileEditor editor = openedEditors[i];
                if (editor instanceof FlowerDiagramEditor) {
                    return (FlowerDiagramEditor)editor;
                }
            }
        }
        return flowerEditor;
    }
}
