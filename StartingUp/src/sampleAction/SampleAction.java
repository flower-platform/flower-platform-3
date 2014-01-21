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

package sampleAction;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;

/**
 * @author Sebastian Solomon
 */
public class SampleAction extends AnAction {

    // just opens an  FlowerSampleEditor
    public void actionPerformed(AnActionEvent e) {
        VirtualFile vf = new LightVirtualFile("FlowerDummyFile.notation"){
            public String getPresentableName() {
                return "DUMMY";
            }
        };
        FileEditorManager.getInstance(e.getProject()).openFile(vf, true, false);
    }

    public void update(AnActionEvent e) {
        //TODO (visible when...)
//            if ( e.getData(PlatformDataKeys.VIRTUAL_FILE).getExtension()!= null &&
//                 e.getData(PlatformDataKeys.VIRTUAL_FILE).getExtension().equals("notation")){
//                e.getPresentation().setEnabled(true);
//                e.getPresentation().setVisible(true);
//                return;
//            }
//        e.getPresentation().setEnabled(false);
//        e.getPresentation().setVisible(false);
//        return;
//        }
    }

}
