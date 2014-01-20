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

//package sampleAction;
//
//import com.intellij.openapi.actionSystem.AnAction;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.PlatformDataKeys;
//import com.intellij.openapi.fileEditor.FileEditorManager;
//import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
//
//public class ChengeFocus extends AnAction {
//    /**
//     * Implement this method to provide your action handler.
//     *
//     * @param e Carries information on the invocation place
//     */
//    @Override
//    public void actionPerformed(AnActionEvent e) {
//        FileEditorManagerImpl fileEditorManager =   (FileEditorManagerImpl)FileEditorManager.getInstance(e.getProject());
//        fileEditorManager.getWindows()[0].setSelectedEditor(fileEditorManager.getWindows()[0].findFileComposite(e.getData(PlatformDataKeys.VIRTUAL_FILE)), true);
//    }
//}
