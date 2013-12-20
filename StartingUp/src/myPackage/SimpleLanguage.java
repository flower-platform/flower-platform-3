package myPackage; /**
 * Created with IntelliJ IDEA.
 * User: PowerUser
 * Date: 13.11.2013
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */
import com.intellij.lang.Language;

public class SimpleLanguage extends Language {
    public static final SimpleLanguage INSTANCE = new SimpleLanguage();

    private SimpleLanguage() {
        super("Notations");
    }
}