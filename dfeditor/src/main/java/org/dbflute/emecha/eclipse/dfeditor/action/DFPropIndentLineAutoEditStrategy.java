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
 * 改行入力時の自動インデントを行う。<br>
 * プロパティ区切り文字が先頭に存在する行で改行を行う場合、インデントにプロパティ区切り文字も追加する。
 * @author schatten
 * @see org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy
 */
public class DFPropIndentLineAutoEditStrategy extends AbstractAutoEditStrategy {

    public DFPropIndentLineAutoEditStrategy() {
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

            String before = d.get(start, c.offset - start);
            boolean startBrace = false;
            if (before.trim().endsWith(":{")) {
                buf.append("    ");
                startBrace = true;
            }
            String beforeIndent = null;
            boolean appendSeparate = false;
            if (end > start) {
                // append to input
                beforeIndent = d.get(start, end - start);
                buf.append(beforeIndent);
                if (!startBrace && end < c.offset && d.getChar(end) == ';' && !before.trim().endsWith("=")) {
                    buf.append("; ");
                    appendSeparate = true;
                }
            }
            if (startBrace) {
                buf.append("; ");
                appendSeparate = true;
            }
            int afterEnd = findEndOfWhiteSpace(d, c.offset, info.getOffset() + info.getLength());
            if (appendSeparate && afterEnd < d.getLength()) {
                char next = d.getChar(afterEnd);
                if (next == '}') {
                    c.caretOffset = c.offset + buf.length();
                    c.shiftsCaret = false;
                    c.doit = false;
                    buf.append(getLineDelimiter(d, c));
                    if (beforeIndent != null) {
                        buf.append(beforeIndent);
                    }
                    c.length = c.length + (afterEnd - c.offset);
                } else if (next == ';') {
                    c.length = c.length + (afterEnd - c.offset + 1);
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
