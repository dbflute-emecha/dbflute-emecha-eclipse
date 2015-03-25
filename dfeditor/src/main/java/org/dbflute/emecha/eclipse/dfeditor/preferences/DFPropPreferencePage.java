/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.emecha.eclipse.dfeditor.preferences;

import org.dbflute.emecha.eclipse.dfeditor.DFEditorActivator;
import org.dbflute.emecha.eclipse.dfeditor.DfColor;
import org.dbflute.emecha.eclipse.dfeditor.nls.Messages;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 *
 * @author schatten
 */
public class DFPropPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public DFPropPreferencePage() {
        super(GRID);
        setPreferenceStore(DFEditorActivator.getDefault().getPreferenceStore());
        setDescription(Messages.DFPropPreferencePage_description);
    }

    /**
     * Creates the field editors. Field editors are abstractions of
     * the common GUI blocks needed to manipulate various types
     * of preferences. Each field editor knows how to save and
     * restore itself.
     */
    public void createFieldEditors() {

        addField(new ColorFieldEditor(DfColor.LINE_COMMENT.getForegroundKey(), Messages.DFPropPreferencePage_CommentStyle, getFieldEditorParent()));
        addField(new ColorFieldEditor(DfColor.FIXED_LITERAL_MARK.getForegroundKey(), Messages.DFPropPreferencePage_LiteralMarkStyle, getFieldEditorParent()));
        addField(new ColorFieldEditor(DfColor.LIKE_SEARCH_MARK.getForegroundKey(), Messages.DFPropPreferencePage_SearchMarkStyle, getFieldEditorParent()));
        addField(new ColorFieldEditor(DfColor.ALIAS_MARK.getForegroundKey(), Messages.DFPropPreferencePage_AliasMarkStyle, getFieldEditorParent()));
        addField(new ColorFieldEditor(DfColor.VALIABLE.getForegroundKey(), Messages.DFPropPreferencePage_ValiableStyle, getFieldEditorParent()));
        addField(new ColorFieldEditor(DfColor.SQL.getForegroundKey(), Messages.DFPropPreferencePage_SQLCommentStyle, getFieldEditorParent()));

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

}
