package myPackage;
/**
 * Created with IntelliJ IDEA.
 * User: PowerUser
 * Date: 13.11.2013
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SimpleFileType extends LanguageFileType {
    public static final SimpleFileType INSTANCE = new SimpleFileType();

    private SimpleFileType() {
        super (SimpleLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Flower Diagram";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Flower Diagram Editor";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "notation";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return SimpleIcon.FILE;
    }
}
