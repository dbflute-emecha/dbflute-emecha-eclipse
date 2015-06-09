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
package org.dbflute.emecha.eclipse.sqltools.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

/**
 * 補完を行うプロセッサのベースクラス
 */
public abstract class OutsideSqlAssistProcessorBase implements IContentAssistProcessor {

    protected static final String DEFAULT_LINE_DELIMEITER = System.getProperty("line.separator");

    protected String getLineDelimiter() {
        return DEFAULT_LINE_DELIMEITER;
    }

    private List<OutsideSqlAssistProcessorBase> processors = new ArrayList<OutsideSqlAssistProcessorBase>();

    public OutsideSqlAssistProcessorBase(OutsideSqlAssistProcessorBase... processor) {
        processors.add(this);
        processors.addAll(Arrays.asList(processor));
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
     */
    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        List<ICompletionProposal> list = new ArrayList<ICompletionProposal>();
        for (OutsideSqlAssistProcessorBase processor : processors) {
            processor.appendCompletionProposal(list, viewer, offset);
        }
        return list.toArray(new ICompletionProposal[list.size()]);
    }

    /**
     * @param list store the result
     * @param viewer the viewer whose document is used to compute the proposals
     * @param offset an offset within the document for which completions should be computed
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
     */
    protected abstract void appendCompletionProposal(List<ICompletionProposal> list, ITextViewer viewer, int offset);

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeContextInformation(org.eclipse.jface.text.ITextViewer, int)
     */
    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getCompletionProposalAutoActivationCharacters()
     */
    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationAutoActivationCharacters()
     */
    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getErrorMessage()
     */
    @Override
    public String getErrorMessage() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationValidator()
     */
    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

    // ===================================================================================
    //                                                                     Property Option
    //                                                                     ===============

    protected static class OutsideSqlProposal {
        private final String propertyMark;

        public OutsideSqlProposal(String propertyMark) {
            this.propertyMark = propertyMark;
        }

        public String getPropertyMark() {
            return this.propertyMark;
        }

        public int getCursorPositionOffset() {
            return this.propertyMark.length();
        }

        public String getAdditionalProposalInfo() {
            return this.propertyMark;
        }

        public String getDisplayString() {
            return this.propertyMark;
        }

        private static final String SPACE = "          " + "          " + "          " + "          " + "          ";

        protected String getSpaceIndent(int indent) {
            String base = indent > 50 ? SPACE + SPACE : SPACE;
            int length = indent > 100 ? 100 : indent;
            return base.substring(0, length);
        }
    }

}
