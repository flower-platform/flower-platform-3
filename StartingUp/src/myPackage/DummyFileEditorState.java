package myPackage;

import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;

/**
 * Created with IntelliJ IDEA.
 * User: PowerUser
 * Date: 18.11.2013
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public class DummyFileEditorState implements FileEditorState {

    public static final FileEditorState DUMMY = new DummyFileEditorState();

    public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level) {
        return true;
    }
}
