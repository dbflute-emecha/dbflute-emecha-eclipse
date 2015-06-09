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

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

/**
 * SQLコメント開始時に自動的に閉じる編集を行う。
 */
public class AutoCloseCommentStrategy implements IAutoEditStrategy {

    private static final String DEFAULT_LINE_DELIMEITER = System.getProperty("line.separator");

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
            if (command.text.equals("*") && command.offset > 0 && document.get(command.offset - 1, 1).equals("/")) {
                String appendText = "*/";
                if (command.offset == document.getLength()) {
                    appendText += getLineDelimiter();
                }
                command.text = command.text + appendText;
                command.caretOffset = command.offset + 1;
                command.shiftsCaret = false;
                command.doit = false;
            } else if (command.text.equals("-") && command.offset > 0 && document.get(command.offset - 1, 1).equals("-")) {
                if (document.getLineDelimiter(document.getLineOfOffset(command.offset)) == null
                        && document.get(command.offset, document.getLength() - command.offset).trim().length() == 0) {
                    // ファイルの最終行で行コメントを入力した場合、改行コードを追加する。
                    command.text = command.text + getLineDelimiter();
                    command.caretOffset = command.offset + 1;
                    command.shiftsCaret = false;
                    command.doit = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
