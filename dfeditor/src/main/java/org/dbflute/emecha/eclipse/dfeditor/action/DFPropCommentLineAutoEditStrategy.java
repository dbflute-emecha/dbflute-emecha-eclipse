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
package org.dbflute.emecha.eclipse.dfeditor.action;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;

/**
 * コメント行で改行入力時の自動インデントを行う。
 * @author schatten
 */
public class DFPropCommentLineAutoEditStrategy extends AbstractAutoEditStrategy {

    public DFPropCommentLineAutoEditStrategy() {
    }

    /**
     * Copies the indentation of the previous line.
     *
     * @param d the document to work on
     * @param c the command to deal with
     */
    private void autoIndentAfterNewLine(IDocument d, DocumentCommand c) {
        if (c.offset == -1 || d.getLength() == 0) {
            return;
        }
        try {
            // find start of line
            int p = (c.offset == d.getLength() ? c.offset - 1 : c.offset);
            IRegion info = d.getLineInformationOfOffset(p);
            int start = info.getOffset();

            // find white spaces
            int end = findEndOfWhiteSpace(d, start, c.offset);

            StringBuilder buf = new StringBuilder(c.text);
            String beforeIndent = null;
            if (end > start) {
                // append to input
                beforeIndent = d.get(start, end - start);
                buf.append(beforeIndent);
                if (end < c.offset) {
                    char prefix = d.getChar(end);
                    if (prefix == ';') {
                        String after = d.get(end, c.offset - end);
                        int commentIndex = after.indexOf('#');
                        if (commentIndex == -1 || !after.substring(0, commentIndex).trim().endsWith("=")) {
                            buf.append("; ");
                        }
                    }
                }
            }
            c.text = buf.toString();
        } catch (BadLocationException excp) {
            // stop work
        }

    }

    /*
     * @see org.eclipse.jface.text.IAutoEditStrategy#customizeDocumentCommand(org.eclipse.jface.text.IDocument, org.eclipse.jface.text.DocumentCommand)
     */
    public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
        if (c.length == 0 && c.text != null && TextUtilities.endsWith(d.getLegalLineDelimiters(), c.text) != -1) {
            autoIndentAfterNewLine(d, c);
        }
    }

}
