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
package editor;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserFunction;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import equinox.FlowerFrameworkLauncher;
import myPackage.BridgeUtilIdeaToEclipse;
import myPackage.DummyFileEditorState;
import myPackage.IIdeaToEclipseBridge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.URL;

/**
 * @author Sebastian Solomon
 */
public class FlowerDiagramEditor implements FileEditor/*, FileEditorManagerListener*/ {

    public static IIdeaToEclipseBridge ideaToEclipseBridge;

    JPanel browserPanel;

    final JWebBrowser webBrowser = new JWebBrowser(JWebBrowser.destroyOnFinalization(), JWebBrowser.useXULRunnerRuntime());

    private final VirtualFile file;

    private final Project project;

    private boolean isDirty = false;

    private final PropertyChangeSupport myChangeSupport;

    static {
        // start eclipse
        FlowerFrameworkLauncher framework = new FlowerFrameworkLauncher();
        framework.init();
        framework.deploy();
        framework.start();

        // configure xulrunner
        File file = null;
        IdeaPluginDescriptor[] ipd = PluginManager.getPlugins();
        for(IdeaPluginDescriptor pluginDescriptor : ipd) {
            if (pluginDescriptor.getName().equals("FlowerPlatformPlugin")) {
                 file = pluginDescriptor.getPath();
            }
        }
        file = new File(file, "\\classes\\mozilla\\xulrunner_3_6_28_win32");
        if (!file.exists()) {
            System.out.println("Failed to find xulrunner folder");
        }
        try{
            URL resourceUrl = file.toURI().toURL();
            if (resourceUrl != null) {
                System.setProperty("org.eclipse.swt.browser.XULRunnerPath", file.getPath());
            }
        }   catch (Exception e) {
            System.out.println("Failed to configure xulrunner path for mozilla browser!");
        }
    }

    public FlowerDiagramEditor(Project project, VirtualFile file) {
        NativeInterface.open();
        UIUtils.setPreferredLookAndFeel();

        this.file = file;
        this.project = project;
        myChangeSupport = new PropertyChangeSupport(this);

        browserPanel = new JPanel(new BorderLayout());
        // webBrowser.setBarsVisible(false); // todo
        // This call must happen in the AWT Event Dispatch Thread
        webBrowser.navigate(BridgeUtilIdeaToEclipse.iIdeaToEclipseBridge.getUrl()+ "?openResources=" + file.getPath());

        browserPanel.add(webBrowser, BorderLayout.CENTER);
        browserPanel.setBorder(BorderFactory.createEtchedBorder());
        browserPanel.setVisible(true);

//        FileEditorManager.getInstance(project).addFileEditorManagerListener(this);

        webBrowser.registerFunction(new WebBrowserFunction("sendGlobalDirtyStateToJava") {
            @Override
            public Object invoke(JWebBrowser webBrowser, Object... args) {
                boolean newDirtyState = Boolean.parseBoolean((String)args[0]);
                if (isDirty != newDirtyState){
                    isDirty = newDirtyState;
                    myChangeSupport.firePropertyChange(FileEditor.PROP_MODIFIED, true, false);
                }
                return null;
            }

        });
        //todo delete
//            com.intellij.openapi.command.CommandProcessor.getInstance().addCommandListener(new CommandListener() {
//                @Override
//                public void commandStarted(CommandEvent event) {
//                    int i =0;
//                }
//
//                @Override
//                public void beforeCommandFinished(CommandEvent event) {
//                    //To change body of implemented methods use File | Settings | File Templates.
//                }
//
//                @Override
//                public void commandFinished(CommandEvent event) {
//                    //To change body of implemented methods use File | Settings | File Templates.
//                }
//
//                @Override
//                public void undoTransparentActionStarted() {
//                    //To change body of implemented methods use File | Settings | File Templates.
//                }
//
//                @Override
//                public void undoTransparentActionFinished() {
//                    //To change body of implemented methods use File | Settings | File Templates.
//                }
//            });
    }

    public static void setIideaToEclipseBridge(IIdeaToEclipseBridge ii){
        if (FlowerDiagramEditor.ideaToEclipseBridge == null) {
            FlowerDiagramEditor.ideaToEclipseBridge = ii;
        }
    }

    public JWebBrowser getBrowser(){
        return webBrowser;
    }

    public VirtualFile getFile(){
        return file;
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return browserPanel;
    }

    public Project getProject(){
        return project;
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
        return isDirty;
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
        // apelata cadnd tabul devine activ
    }

    /**
     This method is invoked each time when the editor is deselected.
     */
    @Override
    public void deselectNotify() {
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {
        myChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {
        myChangeSupport.removePropertyChangeListener(propertyChangeListener);
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
        // TODO delete
//        @Override
//        public void fileOpened(FileEditorManager fileEditorManager, VirtualFile virtualFile) {
//        }
//
//        @Override
//        public void fileClosed(FileEditorManager fileEditorManager, VirtualFile virtualFile) {
//        }
//
//        @Override
//        public void selectionChanged(FileEditorManagerEvent fileEditorManagerEvent) {
//            //fileEditorManagerEvent.getNewFile().get
//            //
//        }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> tKey) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> tKey, @Nullable T t) {
    }

}


