/*
 *
 */
package org.dbflute.emecha.eclipse.sqlassist;

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

/**
 * SQLコメント開始時に自動的に閉じる編集を行う。
 */
public class AutoCloseCommentStrategy implements IAutoEditStrategy {

    private static final String DEFAULT_LINE_DELIMEITER  = System.getProperty("line.separator");

    protected String getLineDelimiter() {
        return DEFAULT_LINE_DELIMEITER;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.IAutoEditStrategy#customizeDocumentCommand(org.eclipse.jface.text.IDocument, org.eclipse.jface.text.DocumentCommand)
     */
    @Override
    public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
        try {
            if (command.text.equals("*")
                    && command.offset > 0 && document.get(command.offset - 1, 1).equals("/")) {
                String appendText = "*/";
                if (command.offset == document.getLength()) {
                    appendText += getLineDelimiter();
                }
                command.text = command.text + appendText;
                command.caretOffset = command.offset + 1;
                command.shiftsCaret= false;
                command.doit = false;
            } else if (command.text.equals("-")
                    && command.offset > 0 && document.get(command.offset - 1, 1).equals("-")) {
                if (document.getLineDelimiter(document.getLineOfOffset(command.offset)) == null
                        && document.get(command.offset, document.getLength() - command.offset).trim().length() == 0) {
                    // ファイルの最終行で行コメントを入力した場合、改行コードを追加する。
                    command.text = command.text + getLineDelimiter();
                    command.caretOffset = command.offset + 1;
                    command.shiftsCaret= false;
                    command.doit = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
