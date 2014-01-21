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
package myPackage;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserFunction;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import editor.FlowerDiagramEditor;
import editor.FlowerSampleEditor;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import editor.SampleEditor;

/**
 * @author Sebastian Solomon
 */
public class FlowerDiagramEditorProvider implements ApplicationComponent, FileEditorProvider {

    private boolean isOpenedFlag = false;
    // needed to know in what editor is the file oppend when the answer comes from javascrips
    private FlowerDiagramEditor currentEditor;

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    //whether the provider can create valid editor for the specified file or not
    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        return virtualFile.getName().endsWith("notation");
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "MyFileEditorProvider";
    }

    public FileEditor createEditor(Project project, final VirtualFile file) {
        if(file instanceof LightVirtualFile){  //to open the editor with no file behind
            return new FlowerSampleEditor(project, file);
        }
        VirtualFile vf = new LightVirtualFile(){
                            public String getName(){
                                return "null";
                            }
        };

        FileEditor[] editorsList = FileEditorManager.getInstance(project).getAllEditors();

        if (editorsList.length != 0) {
            if (!isFileOpened(file, project)) {
                FileEditor editor = FileEditorManager.getInstance(project).getSelectedEditors()[0];
                if (editor instanceof FlowerDiagramEditor) { //if active editor is FDE
                    ((FlowerDiagramEditor) editor).getBrowser().executeJavascript(
                            "handleLink('openResources=" + file.getPath() + "')");
                    return new SampleEditor(project, vf);
                } else {// find the last opened FlowerDiagramEditor,
                    // &set focus on it
                    for (int i = editorsList.length - 1; i >= 0; i--) {
                        editor = editorsList[i];
                        if (editor instanceof FlowerDiagramEditor) {
                            ((FlowerDiagramEditor)editor)
                                    .getBrowser()
                                    .executeJavascript(
                                            "handleLink('openResources="
                                                    + file.getPath()
                                                    + "')");
                            setActiveEditor(project, currentEditor);
                            return new SampleEditor(project, vf);
                        }
                    }
                    //if no FlowerEditorOpend
                    return new FlowerDiagramEditor(project, file);
                }
            }
            //the file is opened in an editor
            setActiveEditor(project, currentEditor);
            isOpenedFlag = false;
            return new SampleEditor(project, vf);
        }
        return new FlowerDiagramEditor(project, file);
    }

    @Override
    public void disposeEditor(@NotNull FileEditor fileEditor) {
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element element, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        return DummyFileEditorState.DUMMY;
    }

    @Override
    public void writeState(@NotNull FileEditorState fileEditorState, @NotNull Project project, @NotNull Element element) {
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return getComponentName();
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }

    private boolean isFileOpened(VirtualFile file, final Project project) {
        FileEditor[] editorReference = FileEditorManager.getInstance(project).getAllEditors();

        for (int i = 0; i < editorReference.length; i++) {
            FileEditor edit = editorReference[i];
            if (edit instanceof FlowerDiagramEditor) {
                final JWebBrowser browser = ((FlowerDiagramEditor) edit).getBrowser();
                //todo unregisterFunction
                browser.registerFunction(new WebBrowserFunction("sendIsDiagramOpenedToJava") {
                    @Override
                    public Object invoke(JWebBrowser webBrowser, Object... args) {
                        boolean isOpened = false;
                        if (args[0].toString().equals("true")) {
                                 isOpened = true;
                         }
                        //todo see if better is :
                        if (isOpened) {
                            FlowerDiagramEditorProvider.this.isOpenedFlag = isOpened;
                            //todo set focus on curent editor;
                        }
//                        FlowerDiagramEditorProvider.this.isOpenedFlag = isOpened;
//                        if (isOpened) {
////                            FileEditorManager.getInstance(project).
//
//                        }
                        return null;
                    }

                });

                currentEditor = (FlowerDiagramEditor) edit;
                ((FlowerDiagramEditor) edit).getBrowser().executeJavascriptWithResult(
                        "isFileOpened('" + file.getPath() + "')");
            }
        }

        return isOpenedFlag;
    }

    private void setActiveEditor(Project project, FlowerDiagramEditor fileEditor){
        FileEditorManagerImpl fileEditorManager = (FileEditorManagerImpl)FileEditorManager.getInstance(project);
        fileEditorManager.getWindows()[0].setSelectedEditor(fileEditorManager.getWindows()[0].findFileComposite(fileEditor.getFile()), true);
    }
}
