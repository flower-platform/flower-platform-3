package myPackage;

/**
 * Created with IntelliJ IDEA.
 * User: PowerUser
 * Date: 20.11.2013
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
import com.intellij.AppTopics;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

public class OnFileSaveComponent implements ApplicationComponent {
    @NotNull
    public String getComponentName() {
        return "My On-Save Component";
    }

    public void initComponent() {
        //FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();

        MessageBusConnection connection = bus.connect();

        connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC,
                new FileDocumentManagerAdapter() {
                    @Override
                    public void beforeDocumentSaving(Document document) {
                        //TODO
                        document.getText();
                    }
                });
    }

    public void disposeComponent() {
    }
}
