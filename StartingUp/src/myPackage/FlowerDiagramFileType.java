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
package myPackage;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FlowerDiagramFileType extends LanguageFileType {
    public static final FlowerDiagramFileType INSTANCE = new FlowerDiagramFileType();

    private FlowerDiagramFileType() {
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
        return FlowerDiagramIcon.FILE;
    }
}
