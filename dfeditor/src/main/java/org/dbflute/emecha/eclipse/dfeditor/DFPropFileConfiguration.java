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
package org.dbflute.emecha.eclipse.dfeditor;

import org.dbflute.emecha.eclipse.dfeditor.action.DFPropCommentLineAutoEditStrategy;
import org.dbflute.emecha.eclipse.dfeditor.action.DFPropDoubleClickStrategy;
import org.dbflute.emecha.eclipse.dfeditor.action.DFPropEnclosedAutoEditStrategy;
import org.dbflute.emecha.eclipse.dfeditor.action.DFPropEnclosedCommentAutoEditStrategy;
import org.dbflute.emecha.eclipse.dfeditor.action.DFPropIndentLineAutoEditStrategy;
import org.dbflute.emecha.eclipse.dfeditor.action.DFPropTabSpaceAutoEditStrategy;
import org.dbflute.emecha.eclipse.dfeditor.scanner.BsDFPropScanner;
import org.dbflute.emecha.eclipse.dfeditor.scanner.DFPropCommentScanner;
import org.dbflute.emecha.eclipse.dfeditor.scanner.DefaultTokenScanner;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.MarkerAnnotation;

/**
 * DBFlute Property File Source View Configuration.
 */
public class DFPropFileConfiguration extends TextSourceViewerConfiguration implements DFPropPartitions {

    private DFPropDoubleClickStrategy doubleClickStrategy;

    private BsDFPropScanner commentScanner;
    private BsDFPropScanner defaultScanner;
    private ISharedTextColors colorManager;

    public DFPropFileConfiguration(ISharedTextColors colorManager, IPreferenceStore preferenceStore) {
        super(preferenceStore);
        this.colorManager = colorManager;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
     */
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return new String[] { IDocument.DEFAULT_CONTENT_TYPE, DFP_PARTITIONING, DFP_COMMENT, DFP_LITERAL};
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getDoubleClickStrategy(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
        if (doubleClickStrategy == null)
            doubleClickStrategy = new DFPropDoubleClickStrategy();
        return doubleClickStrategy;
    }

    protected ITokenScanner getCommentScanner() {
        if (commentScanner == null) {
            commentScanner = new DFPropCommentScanner(colorManager, fPreferenceStore);
        }
        return commentScanner;
    }

    protected ITokenScanner getDefaultScanner() {
        if (defaultScanner == null)
            defaultScanner = new DefaultTokenScanner(colorManager, fPreferenceStore);
        return defaultScanner;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
     */
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getCommentScanner());
        reconciler.setDamager(dr, DFP_COMMENT);
        reconciler.setRepairer(dr, DFP_COMMENT);

        dr = new DefaultDamagerRepairer(getDefaultScanner());
        reconciler.setDamager(dr, DFP_PARTITIONING);
        reconciler.setRepairer(dr, DFP_PARTITIONING);

        dr = new DefaultDamagerRepairer(getDefaultScanner());
        reconciler.setDamager(dr, DFP_LITERAL);
        reconciler.setRepairer(dr, DFP_LITERAL);

        dr = new DefaultDamagerRepairer(getDefaultScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        return reconciler;
    }

    /**
     * Extended to support the toggle comment.
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getDefaultPrefixes(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    @Override
    public String[] getDefaultPrefixes(ISourceViewer sourceViewer, String contentType) {
        return new String[] { "#" };
    }

    /**
     * Get Show Marker Text Hover Message.
     * @see org.eclipse.ui.editors.text.TextSourceViewerConfiguration#getTextHover(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    @Override
    public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
        return new DefaultTextHover(sourceViewer) {
            @Override
            protected boolean isIncluded(Annotation annotation) {
                return annotation instanceof MarkerAnnotation;
            }
        };
    }

    /**
     * Get Auto Edit Strategies.
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAutoEditStrategies(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    @Override
    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
        if (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) {
            return new IAutoEditStrategy[]{new DFPropIndentLineAutoEditStrategy(), new DFPropEnclosedAutoEditStrategy(), new DFPropTabSpaceAutoEditStrategy()};
        }
        if (DFP_PARTITIONING.equals(contentType)) {
            return new IAutoEditStrategy[]{new DFPropEnclosedCommentAutoEditStrategy(), new DFPropTabSpaceAutoEditStrategy()};
        }
        if (DFP_COMMENT.equals(contentType)) {
            return new IAutoEditStrategy[]{new DFPropCommentLineAutoEditStrategy(), new DFPropTabSpaceAutoEditStrategy()};
        }
        return super.getAutoEditStrategies(sourceViewer, contentType);
    }

    public void updatePreferences() {
        if (defaultScanner != null) {
            defaultScanner.initialize();
        }
        if (commentScanner != null) {
            commentScanner.initialize();
        }

    }
}
