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
package org.dbflute.emecha.eclipse.pmeditor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * DBFlute Parameter coMment template Editor.
 * @author schatten
 */
public class PMEditor extends TextEditor implements IPropertyChangeListener{

    public PMEditor() {
        super();
        // TODO 自動生成されたコンストラクター・スタブ
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        // TODO 自動生成されたメソッド・スタブ
        ISharedTextColors colors = getSharedColors();
        IPreferenceStore preferenceStore = PMEditorActivator.getDefault().getPreferenceStore();
        SourceViewerConfiguration configuration = new PMFileConfiguration(colors, preferenceStore);
        setSourceViewerConfiguration(configuration);
        preferenceStore.addPropertyChangeListener(this);
        super.init(site, input);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.editors.text.TextEditor#dispose()
     */
    @Override
    public void dispose() {
        // TODO 自動生成されたメソッド・スタブ
        IPreferenceStore preferenceStore = PMEditorActivator.getDefault().getPreferenceStore();
        preferenceStore.removePropertyChangeListener(this);
        super.dispose();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        SourceViewerConfiguration configuration = getSourceViewerConfiguration();
        if (configuration instanceof PMFileConfiguration) {
            ((PMFileConfiguration) configuration).updatePreferences();
            getSourceViewer().configure(configuration);
            getSourceViewer().invalidateTextPresentation();
        }
    }

    @Override
    public void doRevertToSaved() {
        SourceViewerConfiguration configuration = getSourceViewerConfiguration();
        if (configuration instanceof PMFileConfiguration) {
            ((PMFileConfiguration) configuration).updatePreferences();
        }
        super.doRevertToSaved();
    }

//    /* (非 Javadoc)
//     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
//     */
//    @Override
//    public void doSave(IProgressMonitor monitor) {
//        super.doSave(monitor);
//        // TODO 自動生成されたメソッド・スタブ
//    }
//
//    /* (非 Javadoc)
//     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
//     */
//    @Override
//    public void doSaveAs() {
//        super.doSaveAs();
//        // TODO 自動生成されたメソッド・スタブ
//
//    }
//    /* (非 Javadoc)
//     * @see org.eclipse.ui.texteditor.StatusTextEditor#doRevertToSaved()
//     */
//    @Override
//    public void doRevertToSaved() {
//        // TODO 自動生成されたメソッド・スタブ
//        super.doRevertToSaved();
//    }

//    /* (非 Javadoc)
//     * @see org.eclipse.ui.part.EditorPart#isDirty()
//     */
//    @Override
//    public boolean isDirty() {
//        // TODO 自動生成されたメソッド・スタブ
//        return false;
//    }
//
//    /* (非 Javadoc)
//     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
//     */
//    @Override
//    public boolean isSaveAsAllowed() {
//        // TODO 自動生成されたメソッド・スタブ
//        return false;
//    }
//
//    /* (非 Javadoc)
//     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
//     */
//    @Override
//    public void createPartControl(Composite parent) {
//        // TODO 自動生成されたメソッド・スタブ
//
//    }
//
//    /* (非 Javadoc)
//     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
//     */
//    @Override
//    public void setFocus() {
//        // TODO 自動生成されたメソッド・スタブ
//
//    }

}
