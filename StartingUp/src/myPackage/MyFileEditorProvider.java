package myPackage;

import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: PowerUser
 * Date: 18.11.2013
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
public class MyFileEditorProvider implements ApplicationComponent, FileEditorProvider {

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {

    }


    @NotNull
    @Override
    public String getComponentName() {
        return "MyFileEditorProvider";
    }

    //whether the provider can create valid editor for the specified file or not
    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        //TODO
                return virtualFile.getName().endsWith("notation");
    }

    public FileEditor createEditor(Project project, final VirtualFile file) {
        //todo to review
//       if (file.getName().endsWith("notation")){
//
//       }
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
        //TODO
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
