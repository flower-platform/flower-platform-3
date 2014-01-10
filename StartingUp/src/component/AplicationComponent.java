//package component;
//
//import com.intellij.ide.actions.SaveAllAction;
//import com.intellij.openapi.Disposable;
//import com.intellij.openapi.actionSystem.*;
//import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
//import com.intellij.openapi.actionSystem.ex.AnActionListener;
//import com.intellij.openapi.components.SettingsSavingComponent;
//import com.intellij.openapi.extensions.PluginId;
//import com.intellij.openapi.fileEditor.FileEditor;
//import com.intellij.openapi.fileEditor.FileEditorManager;
//import com.intellij.openapi.roots.ProjectRootManager;
//import com.intellij.openapi.util.ActionCallback;
//import com.intellij.openapi.project.Project;
//import com.intellij.util.containers.ContainerUtil;
//import myPackage.FlowerDiagramEditor;
//import org.jetbrains.annotations.NonNls;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.InputEvent;
//import java.util.List;
//
///* license-start
//*
//* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
//*
//* This program is free software: you can redistribute it and/or modify
//* it under the terms of the GNU General Public License as published by
//* the Free Software Foundation version 3.
//*
//* This program is distributed in the hope that it will be useful,
//* but WITHOUT ANY WARRANTY; without even the implied warranty of
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
//*
//* Contributors:
//*   Crispico - Initial API and implementation
//*
//* license-end
//*/
//public class AplicationComponent extends ActionManager implements SettingsSavingComponent, AnActionListener {
//
//    private final List<AnActionListener> myActionListeners = ContainerUtil.createLockFreeCopyOnWriteList();
//
//    public AplicationComponent() {
//        super();
//        ActionManagerEx.getInstance().addAnActionListener(this);
//    }
//
//    /**
//     * Factory method that creates an <code>ActionPopupMenu</code> from the
//     * specified group. The specified place is associated with the created popup.
//     *
//     * @param place Determines the place that will be set for {@link com.intellij.openapi.actionSystem.AnActionEvent} passed
//     *              when an action from the group is either performed or updated
//     *              See {@link com.intellij.openapi.actionSystem.ActionPlaces}
//     * @param group Group from which the actions for the menu are taken.
//     * @return An instance of <code>ActionPopupMenu</code>
//     */
//    @Override
//    public ActionPopupMenu createActionPopupMenu(@NonNls String place, @NotNull ActionGroup group) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    /**
//     * Factory method that creates an <code>ActionToolbar</code> from the
//     * specified group. The specified place is associated with the created toolbar.
//     *
//     * @param place      Determines the place that will be set for {@link com.intellij.openapi.actionSystem.AnActionEvent} passed
//     *                   when an action from the group is either performed or updated.
//     *                   See {@link com.intellij.openapi.actionSystem.ActionPlaces}
//     * @param group      Group from which the actions for the toolbar are taken.
//     * @param horizontal The orientation of the toolbar (true - horizontal, false - vertical)
//     * @return An instance of <code>ActionToolbar</code>
//     */
//    @Override
//    public ActionToolbar createActionToolbar(@NonNls String place, ActionGroup group, boolean horizontal) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    /**
//     * Returns action associated with the specified actionId.
//     *
//     * @param actionId Id of the registered action
//     * @return Action associated with the specified actionId, <code>null</code> if
//     *         there is no actions associated with the speicified actionId
//     * @throws IllegalArgumentException if <code>actionId</code> is <code>null</code>
//     * @see com.intellij.openapi.actionSystem.IdeActions
//     */
//    @Override
//    public AnAction getAction(@NonNls @NotNull String actionId) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    /**
//     * Returns actionId associated with the specified action.
//     *
//     * @return id associated with the specified action, <code>null</code> if action
//     *         is not registered
//     * @throws IllegalArgumentException if <code>action</code> is <code>null</code>
//     */
//    @Override
//    public String getId(@NotNull AnAction action) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    /**
//     * Registers the specified action with the specified id. Note that IDEA's keymaps
//     * processing deals only with registered actions.
//     *
//     * @param actionId Id to associate with the action
//     * @param action   Action to register
//     */
//    @Override
//    public void registerAction(@NonNls @NotNull String actionId, @NotNull AnAction action) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    /**
//     * Registers the specified action with the specified id.
//     *
//     * @param actionId Id to associate with the action
//     * @param action   Action to register
//     * @param pluginId Identifier of the plugin owning the action. Used to show the actions in the
//     *                 correct place under the "Plugins" node in the "Keymap" settings pane and similar dialogs.
//     */
//    @Override
//    public void registerAction(@NotNull String actionId, @NotNull AnAction action, @Nullable PluginId pluginId) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    /**
//     * Unregisters the action with the specified actionId.
//     *
//     * @param actionId Id of the action to be unregistered
//     */
//    @Override
//    public void unregisterAction(@NotNull String actionId) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    /**
//     * Returns the list of all registered action IDs with the specified prefix.
//     *
//     * @return all action <code>id</code>s which have the specified prefix.
//     * @since 5.1
//     */
//    @Override
//    public String[] getActionIds(@NotNull String idPrefix) {
//        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    /**
//     * Checks if the specified action ID represents an action group and not an individual action.
//     * Calling this method does not cause instantiation of a specific action class corresponding
//     * to the action ID.
//     *
//     * @param actionId the ID to check.
//     * @return true if the ID represents an action group, false otherwise.
//     * @since 5.1
//     */
//    @Override
//    public boolean isGroup(@NotNull String actionId) {
//        return false;
//    }
//
//    /**
//     * Creates a panel with buttons which invoke actions from the specified action group.
//     *
//     * @param actionPlace        the place where the panel will be used (see {@link com.intellij.openapi.actionSystem.ActionPlaces}).
//     * @param messageActionGroup the action group from which the toolbar is created.
//     * @return the created panel.
//     * @since 5.1
//     */
//    @Override
//    public JComponent createButtonToolbar(String actionPlace, ActionGroup messageActionGroup) {
//        return null;
//    }
//
//    @Override
//    public AnAction getActionOrStub(@NonNls String id) {
//        return null;
//    }
//
//    @Override
//    public void addTimerListener(int delay, TimerListener listener) {
//    }
//
//    @Override
//    public void removeTimerListener(TimerListener listener) {
//    }
//
//    @Override
//    public void addTransparentTimerListener(int delay, TimerListener listener) {
//    }
//
//    @Override
//    public void removeTransparentTimerListener(TimerListener listener) {
//    }
//
//    @Override
//    public ActionCallback tryToExecute(@NotNull AnAction action, @NotNull InputEvent inputEvent, @Nullable Component contextComponent, @Nullable String place, boolean now) {
//        return null;
//    }
//
//    @Override
//    public void addAnActionListener(AnActionListener listener) {
//        myActionListeners.add(listener);
//    }
//
//    @Override
//    public void addAnActionListener(AnActionListener listener, Disposable parentDisposable) {
//    }
//
//    @Override
//    public void removeAnActionListener(AnActionListener listener) {
//        myActionListeners.remove(listener);
//    }
//
//    @Nullable
//    @Override
//    public KeyboardShortcut getKeyboardShortcut(@NotNull String actionId) {
//        return null;
//    }
//
//    public void initComponent() {
//    }
//
//    public void disposeComponent() {
//    }
//
//    @NotNull
//    public String getComponentName() {
//        return "AplicationComponent";
//    }
//
//    @Override
//    public void save() {
//        //  todo if needed, get opend projects and save all unsaved diagrams
//    }
//
//    @Override
//    public void beforeActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
//        if (action instanceof SaveAllAction) {
//            Project project = PlatformDataKeys.PROJECT.getData(dataContext);
//            FileEditor[] openedEditors = FileEditorManager.getInstance(project).getAllEditors();
//            for(FileEditor editor : openedEditors) {
//                if ( (editor instanceof FlowerDiagramEditor) && (editor.isModified()) ) {
//                   ((FlowerDiagramEditor) editor).getBrowser().executeJavascript("doSave()");
//                }
//            }
//        }
//    }
//
//    @Override
//    public void afterActionPerformed(AnAction action, DataContext dataContext, AnActionEvent event) {
//    }
//
//    @Override
//    public void beforeEditorTyping(char c, DataContext dataContext) {
//    }
//}
