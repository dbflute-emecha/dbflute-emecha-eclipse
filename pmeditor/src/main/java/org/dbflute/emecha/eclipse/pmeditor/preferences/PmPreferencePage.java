/*
 * Copyright 2015 the original author or authors.
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
package org.dbflute.emecha.eclipse.pmeditor.preferences;

import org.dbflute.emecha.eclipse.pmeditor.PMEditorActivator;
import org.dbflute.emecha.eclipse.pmeditor.PmColorDef;
import org.dbflute.emecha.eclipse.pmeditor.nls.Messages;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * PMEditor Preference Page
 * @author schatten
 */
public class PmPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public PmPreferencePage() {
        super(GRID);
        setPreferenceStore(PMEditorActivator.getDefault().getPreferenceStore());
        setDescription(Messages.PmPreferencePage_description);
    }

    /**
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {
        addField(new ColorFieldEditor(PmColorDef.HEADER.getForegroundKey(), Messages.PmPreferencePage_HeaderStyle, getFieldEditorParent()));
        addField(new ColorFieldEditor(PmColorDef.META_MARK.getForegroundKey(), Messages.PmPreferencePage_MetaMarkStyle, getFieldEditorParent()));
        addField(new ColorFieldEditor(PmColorDef.PROPERTY_COMMENT.getForegroundKey(), Messages.PmPreferencePage_PropertyCommentStyle, getFieldEditorParent()));
        addField(new ColorFieldEditor(PmColorDef.SEPARATOR.getForegroundKey(), Messages.PmPreferencePage_SeparatorStyle, getFieldEditorParent()));
        addField(new ColorFieldEditor(PmColorDef.PARAMETER_COMMENT.getForegroundKey(), Messages.PmPreferencePage_ParameterCommentStyle, getFieldEditorParent()));
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
    }

}
