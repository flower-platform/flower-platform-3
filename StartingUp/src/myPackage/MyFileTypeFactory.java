package myPackage;
/**
 * Created with IntelliJ IDEA.
 * User: PowerUser
 * Date: 13.11.2013
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class MyFileTypeFactory extends FileTypeFactory{
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(SimpleFileType.INSTANCE, "notation");
    }
}
