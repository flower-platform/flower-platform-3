package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import myPackage.BridgeUtilIdeaToEclipse;
import myPackage.FlowerDiagramEditor;

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
public class NewDiagramAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
//        new Thread(){
//            public void run() {
            FlowerDiagramEditor.getEditorProject(); //to start eclipse if wasn't started
//            }
//
//        }.start();

        String diagramName = Messages.showInputDialog("Diagram name", "New Diagram", Messages.getQuestionIcon());
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if (vFile != null) {
            if (diagramName.length() <= 0) {
                diagramName = "NewDiagram";
            }
            BridgeUtilIdeaToEclipse.iIdeaToEclipseBridge.createDiagram(vFile, diagramName);
        }
    }

}
