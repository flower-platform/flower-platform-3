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

package component;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.SettingsSavingComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import editor.FlowerDiagramEditor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Sebastian Solomon
 */
public class FlowerDiagramSaveComponent implements ProjectComponent, SettingsSavingComponent {

    private Project project;

    public FlowerDiagramSaveComponent(Project project) {
        this.project = project;
    }
    /**
     * Invoked when the project corresponding to this component instance is opened.<p>
     * Note that components may be created for even unopened projects and this method can be never
     * invoked for a particular component instance (for example for default project).
     */
    @Override
    public void projectOpened() {
    }

    /**
     * Invoked when the project corresponding to this component instance is closed.<p>
     * Note that components may be created for even unopened projects and this method can be never
     * invoked for a particular component instance (for example for default project).
     */
    @Override
    public void projectClosed() {
    }

    /**
     * Component should perform initialization and communication with other components in this method.
     */
    @Override
    public void initComponent() {
    }

    /**
     * Component should dispose system resources or perform other cleanup in this method.
     */
    @Override
    public void disposeComponent() {
    }

    /**
     * Unique name of this component. If there is another component with the same name or
     * name is null internal assertion will occur.
     *
     * @return the name of this component
     */
    @NotNull
    @Override
    public String getComponentName() {
        return "My Project Component";
    }

    @Override
    public void save() {
        FileEditor[] openedEditors = FileEditorManager.getInstance(project).getAllEditors();
        for(FileEditor editor : openedEditors) {
            if ( (editor instanceof FlowerDiagramEditor) && (editor.isModified()) ) {
                ((FlowerDiagramEditor) editor).getBrowser().executeJavascript("doSave()");
            }
        }
    }
}
