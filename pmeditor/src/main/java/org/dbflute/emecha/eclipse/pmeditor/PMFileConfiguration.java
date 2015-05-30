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

import java.util.ArrayList;
import java.util.List;

import org.dbflute.emecha.eclipse.pmeditor.scanner.PMBodyScanner;
import org.dbflute.emecha.eclipse.pmeditor.scanner.PMCommentScanner;
import org.dbflute.emecha.eclipse.sqltools.action.AutoCloseCommentStrategy;
import org.dbflute.emecha.eclipse.sqltools.action.ParameterCommentAssistProcessor;
import org.dbflute.emecha.eclipse.text.scanner.EmRuleBasedScanner;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
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
 * DBFlute Parameter Comment Template Source View Configuration
 * @author schatten
 */
public class PMFileConfiguration extends TextSourceViewerConfiguration implements PMPartitions {

    private ISharedTextColors colors;
    private EmRuleBasedScanner pmCommentScanner;
    private EmRuleBasedScanner defaultScanner;
    private String separator;
    private static ParameterCommentAssistProcessor parameterCommentAssistProcessor;

    public PMFileConfiguration(ISharedTextColors colors, IPreferenceStore preferenceStore) {
        super(preferenceStore);
        this.colors = colors;
        this.separator = DEFAULT_SEPARATOR_TEXT;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return new String[]{IDocument.DEFAULT_CONTENT_TYPE, PM_HEADER, PM_PARAMETER, PM_SEPARATE};
    }

    protected ITokenScanner getPmCommentScanner() {
        if (pmCommentScanner == null) {
            pmCommentScanner = new PMCommentScanner(colors, fPreferenceStore);
        }
        return pmCommentScanner;
    }

    protected ITokenScanner getDefaultScanner() {
        if (defaultScanner == null) {
            defaultScanner = new PMBodyScanner(colors, fPreferenceStore);
        }
        return defaultScanner;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();
        reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

        DefaultDamagerRepairer bodyDr = new DefaultDamagerRepairer(getPmCommentScanner());
        reconciler.setDamager(bodyDr, PM_PARAMETER);
        reconciler.setRepairer(bodyDr, PM_PARAMETER);
        DefaultDamagerRepairer baseDr = new DefaultDamagerRepairer(getDefaultScanner()) {
            @Override
            public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e, boolean documentPartitioningChanged) {
                int sep = fDocument.get().indexOf(separator);
                IRegion retReg = null;
                if (!documentPartitioningChanged) {
                    IRegion damageRegion = super.getDamageRegion(partition, e, documentPartitioningChanged);
                    int lastSep = getLastSep();
                    int sepLength = separator.length() + 1;
                    if (lastSep == sep) {
                        if (sep >= damageRegion.getOffset()) {
                            int end = Math.max(sep + sepLength, damageRegion.getOffset() + damageRegion.getLength());
                            retReg = new Region(damageRegion.getOffset(), end - damageRegion.getOffset());
                        } else {
                            retReg = damageRegion;
                        }
                    } else if (lastSep <= 0 || sep <= 0) {
                        int end = Math.max(Math.max(lastSep + sepLength, sep + sepLength), damageRegion.getOffset() + damageRegion.getLength());
                        retReg = new Region(0, end);
                    } else {
                        int start = Math.min(Math.min(lastSep, sep), damageRegion.getOffset());
                        int end = Math.max(Math.max(lastSep + sepLength, sep + sepLength), damageRegion.getOffset() + damageRegion.getLength());
                        retReg = new Region(start, end - start);
                    }
                }
                setLastSep(sep);
                if (retReg != null) {
                    int start = Math.min(retReg.getOffset(), partition.getOffset());
                    int end = Math.max(retReg.getOffset() + retReg.getLength(), partition.getOffset() + partition.getLength());
                    return new Region(start, end - start);
                }
                return partition;
            }
        };
        reconciler.setDamager(baseDr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(baseDr, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }
    private int _lastSep = -1;
    public int getLastSep() {
        return _lastSep;
    }

    public void setLastSep(int lastSep) {
        this._lastSep = lastSep;
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
     * Get Content Assist
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentAssistant(org.eclipse.jface.text.source.ISourceViewer)
     */
    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant assistant =  new ContentAssistant();
        assistant.setDocumentPartitioning( getConfiguredDocumentPartitioning( sourceViewer ));

        if (parameterCommentAssistProcessor == null) {
            parameterCommentAssistProcessor = new ParameterCommentAssistProcessor() {
                private List<ParameterCommentProposal> proposals = new ArrayList<ParameterCommentProposal>();
                {
                    proposals.add(new IfEndCommentProposal());
                    proposals.add(new BeginEndCommentProposal());
                    proposals.add(new ForEndCommentProposal());
                    proposals.add(new ForFullSpecCommentProposal());
                }
                @Override
                protected List<ParameterCommentProposal> getProposals() {
                    return proposals;
                }
            };
        }
        assistant.setContentAssistProcessor(parameterCommentAssistProcessor, PM_PARAMETER);

        return assistant;
    }

    /**
     * Get Auto Edit Strategies
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAutoEditStrategies(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    @Override
    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
        if (!IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) {
            return super.getAutoEditStrategies(sourceViewer, contentType);
        }
        IAutoEditStrategy[] parentStrategies = super.getAutoEditStrategies(sourceViewer, contentType);
        IAutoEditStrategy[] myStrategies = new IAutoEditStrategy[parentStrategies.length + 1];
        System.arraycopy(parentStrategies, 0, myStrategies, 0, parentStrategies.length);
        myStrategies[myStrategies.length - 1] = new AutoCloseCommentStrategy();
        return myStrategies;
    }

    /**
     * Configuration update
     */
    public void updatePreferences() {
        if (defaultScanner != null) {
            defaultScanner.initialize();
        }
        if (pmCommentScanner != null) {
            pmCommentScanner.initialize();
        }
    }

}
