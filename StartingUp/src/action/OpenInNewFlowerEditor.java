//package action;
//
//import com.intellij.openapi.actionSystem.AnAction;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.actionSystem.PlatformDataKeys;
//import com.intellij.openapi.fileEditor.FileEditorManager;
//
// nu v-a functiona din cauza ca toata logica trece prin EditorProvider care v-a deschide diagramele in acelas editor
//public class OpenInNewFlowerEditor extends AnAction {
//    public void actionPerformed(AnActionEvent e) {
//        //TODO if needed see if file is opened
//        FileEditorManager.getInstance(e.getProject()).openFile(e.getData(PlatformDataKeys.VIRTUAL_FILE), true, false);
//    }
//
//    public void update(AnActionEvent e) {
//        //TODO (notation)
//            if ( e.getData(PlatformDataKeys.VIRTUAL_FILE).getExtension()!= null &&
//                 e.getData(PlatformDataKeys.VIRTUAL_FILE).getExtension().equals("notation")){
//                e.getPresentation().setEnabled(true);
//                e.getPresentation().setVisible(true);
//                return;
//            }
//        e.getPresentation().setEnabled(false);
//        e.getPresentation().setVisible(false);
//        return;
//    }
//}
