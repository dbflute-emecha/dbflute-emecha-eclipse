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
package org.dbflute.emecha.eclipse.sqltools;

import org.eclipse.datatools.sqltools.sqleditor.SQLEditor;
import org.eclipse.datatools.sqltools.sqleditor.internal.editor.SQLSourceViewerConfiguration;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * DTPのSQLエディタの設定情報をオーバーライドする。
 */
public class EMSQLSourceViewerConfiguration extends SQLSourceViewerConfiguration {

    private static PropertyOptionAssistProcessor propertyOptionAssistProcessor;
    private static ParameterCommentAssistProcessor parameterCommentAssistProcessor;
    private static PropertyCommentAssistProcessor propertyCommentAssistProcessor;

    /**
     * @see SQLSourceViewerConfiguration#SQLSourceViewerConfiguration()
     */
    public EMSQLSourceViewerConfiguration() {
        super();
    }

    /**
     * @param editor
     * @see SQLSourceViewerConfiguration#SQLSourceViewerConfiguration(SQLEditor)
     */
    public EMSQLSourceViewerConfiguration(SQLEditor editor) {
        super(editor);
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant assistant = (ContentAssistant) super.getContentAssistant(sourceViewer);
        if (propertyOptionAssistProcessor == null) {
            propertyOptionAssistProcessor = new PropertyOptionAssistProcessor();
        }
        if (parameterCommentAssistProcessor == null) {
            parameterCommentAssistProcessor = new ParameterCommentAssistProcessor(propertyOptionAssistProcessor);
        }
        assistant.setContentAssistProcessor(parameterCommentAssistProcessor, "sql_multiline_comment");
        if (propertyCommentAssistProcessor == null) {
            propertyCommentAssistProcessor = new PropertyCommentAssistProcessor(propertyOptionAssistProcessor);
        }
        assistant.setContentAssistProcessor(propertyCommentAssistProcessor, "sql_comment");
        return assistant;
    }

    @Override
    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
        if ("sql_character".equals(contentType) || "sql_double_quotes_identifier".equals(contentType) || "sql_comment".equals(contentType)
                || "sql_multiline_comment".equals(contentType)) {
            return super.getAutoEditStrategies(sourceViewer, contentType);
        }
        IAutoEditStrategy[] parentStrategies = super.getAutoEditStrategies(sourceViewer, contentType);
        IAutoEditStrategy[] myStrategies = new IAutoEditStrategy[parentStrategies.length + 1];
        System.arraycopy(parentStrategies, 0, myStrategies, 0, parentStrategies.length);
        myStrategies[myStrategies.length - 1] = new AutoCloseCommentStrategy();
        return myStrategies;
    }
}
