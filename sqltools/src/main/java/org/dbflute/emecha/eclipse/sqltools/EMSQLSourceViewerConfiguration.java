/*
 *
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
