package myPackage;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import equinox.FlowerFrameworkLauncher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: PowerUser
 * Date: 18.11.2013
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
    public class FlowerDiagramEditor implements FileEditor, FileEditorManagerListener {
        private static Project editorProject;

        public static IIdeaToEclipseBridge ideaToEclipseBridge;

        JPanel browserPanel;

        //final JWebBrowser webBrowser = new JWebBrowser(JWebBrowser.useXULRunnerRuntime());
        //final JWebBrowser webBrowser = new JWebBrowser();

        final JWebBrowser webBrowser = new JWebBrowser(JWebBrowser.destroyOnFinalization(), JWebBrowser.useXULRunnerRuntime());

      //Browser browser;

        private final VirtualFile file;

        private final Project project;



        //start eclipse
    static {
            FlowerFrameworkLauncher framework = new FlowerFrameworkLauncher();

            framework.init();
            framework.deploy();
            framework.start();
//            if (ideaToEclipse != null){
//                System.out.println(ideaToEclipse.getServerPort());
//            }else {
//                System.out.println("NULL!!");
//            }
//            String xulHome = new File("libs/mozilla/xulrunner_3_6_28_win32").getAbsolutePath();
//            NSSystemPropertySWT.WEBBROWSER_XULRUNNER_HOME.set(xulHome);
//            System.setProperty("org.eclipse.swt.browser.XULRunnerPath", xulHome);

            File file = new File("D:\\data\\java_work\\git_repo_fp\\StartingUp\\libs\\mozilla\\xulrunner_3_6_28_win32"); //TODO
            URL resourceUrl = null;
            try{
                resourceUrl = file.toURL();
                if (resourceUrl != null) {
                    System.setProperty("org.eclipse.swt.browser.XULRunnerPath", file.getPath());
                }
            }   catch (Exception e) {
                System.out.println("Failed to configure xulrunner path for mozilla browser!");
            }
    }

//    public void setIideaToEclipseBridge(){
//        MyEditor.ideaToEclipseBridge =
//    }

     public static void setIideaToEclipseBridge(IIdeaToEclipseBridge ii){
         FlowerDiagramEditor.ideaToEclipseBridge = ii;
    }
        public FlowerDiagramEditor(Project project, VirtualFile file) {
            //Presentation

//            String s =  VfsUtilCore.getRelativePath(vfile, vAncestor, separator);
//            LocalFileSystem.getInstance().findFileByPath();
//            LocalFileSystem.getInstance().findFileByPath("path".replace(File.separatorChar, '/'));


            String path = getPath(project, file);
            System.out.println(file.getPath());
            NativeInterface.open();
            UIUtils.setPreferredLookAndFeel();
            this.file = file;
            this.project = project;
            //todo
            editorProject = project;

            browserPanel = new JPanel(new BorderLayout());
            //webBrowser.setBarsVisible(falses);

            //webBrowser.navigate(BridgeUtilIdeaToEclipse.iIdeaToEclipseBridge.getUrl()+ "?openResources=" + path);
            webBrowser.navigate(BridgeUtilIdeaToEclipse.iIdeaToEclipseBridge.getUrl()+ "?openResources=" + file.getPath());


            browserPanel.add(webBrowser, BorderLayout.CENTER);
            System.out.println();
            browserPanel.setBorder(BorderFactory.createEtchedBorder());

            browserPanel.setVisible(true);

            FileEditorManager.getInstance(project).addFileEditorManagerListener(this);
        }


    private String getPath(Project project, VirtualFile file) {
       String projectPath = project.getLocation().substring(0,project.getLocation().lastIndexOf("/"));
       return file.getPath().substring( file.getPath().indexOf(projectPath )+ projectPath.length());
    }

    @NotNull
        @Override
        public JComponent getComponent() {
            return browserPanel;
        }

    public Project getProject(){
        return project;
    }

    public static Project getEditorProject(){
        return editorProject;
    }

    public static String getWorkspaceRoot(){
        return editorProject.getBasePath().substring(0, editorProject.getBasePath().lastIndexOf('\\'));
    }


        /**
         Returns component to be focused when editor is opened. Method
         should never return null.
         */
        @Nullable
        @Override
        public JComponent getPreferredFocusedComponent() {
            return browserPanel;
        }

        @NotNull
        @Override
        public String getName() {
            return "Flower diagram editor";
        }

        @NotNull
        @Override
        public FileEditorState getState(@NotNull FileEditorStateLevel fileEditorStateLevel) {
            return DummyFileEditorState.DUMMY;
        }

        @Override
        public void setState(@NotNull FileEditorState fileEditorState) {

        }

        /**
         @return whether the editor's content is modified in comparision
         with its file.
         */
        @Override
        public boolean isModified() {
            //TODO
            return false;
        }
        /**
         @return whether the editor is valid or not. For some reasons
         editor can become invalid. For example, text editor
         becomes invalid when its file is deleted.
         */
        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public void selectNotify() {
        }

        private void reloadContent() {
//            String path = getPath(project,file);
//            webBrowser.navigate("http://localhost:51485/flower-platform/main.jsp?openResources=" + path);
        }

        /**
         This method is invoked each time when the editor is deselected.
         */
        @Override
        public void deselectNotify() {
        }

        @Override
        public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {
        }

        @Override
        public void removePropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {
        }

        @Nullable
        @Override
        public BackgroundEditorHighlighter getBackgroundHighlighter() {
            return null;
        }

        @Nullable
        @Override
        public FileEditorLocation getCurrentLocation() {
            return null;
        }

        @Nullable
        @Override
        public StructureViewBuilder getStructureViewBuilder() {
            return null;
        }

        @Override
        public void dispose() {
        }

        @Override
        public void fileOpened(FileEditorManager fileEditorManager, VirtualFile virtualFile) {
        }

        @Override
        public void fileClosed(FileEditorManager fileEditorManager, VirtualFile virtualFile) {
        }

        @Override
        public void selectionChanged(FileEditorManagerEvent fileEditorManagerEvent) {
            if (FileEditorManager.getInstance(project).getSelectedEditor(file) == this )
                reloadContent ();
        }

        @Nullable
        @Override
        public <T> T getUserData(@NotNull Key<T> tKey) {
            return null;
        }

        @Override
        public <T> void putUserData(@NotNull Key<T> tKey, @Nullable T t) {

        }

        public JWebBrowser getBrowser(){
            return webBrowser;
        }
    }


