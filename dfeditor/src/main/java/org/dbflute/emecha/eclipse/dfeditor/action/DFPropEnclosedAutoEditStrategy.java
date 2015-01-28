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

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

/**
 * 囲み文字の入力時に自動的に閉じるサポートをする。
 * @author schatten
 */
public class DFPropEnclosedAutoEditStrategy implements IAutoEditStrategy {

    public DFPropEnclosedAutoEditStrategy() {
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.text.IAutoEditStrategy#customizeDocumentCommand(org.eclipse.jface.text.IDocument, org.eclipse.jface.text.DocumentCommand)
     */
    @Override
    public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
        if (c.offset < 3) {
            return;
        }
        try {
            StringBuilder buf = new StringBuilder(c.text);
            switch (c.text) {
            case "*": // /*～*/
                if (d.getChar(c.offset - 1) != '/' ) {
                    return;
                }
                if (d.getChar(c.offset - 2) == '*' ) {
                    return;
                }
                buf.append("*/");
                break;
            case "$": // $$～$$
                if (d.getChar(c.offset - 1) != '$' ) {
                    return;
                }
                if (d.getChar(c.offset - 2) == '$' ) {
                    return;
                }
                buf.append("$$");
                break;

            case "'": // '～'
                char before = d.getChar(c.offset - 1);
                if (( 'a' <= before && before <= 'z') || ('A' <= before && before <= 'Z')) {
                    return;
                }
            case "\"": // "～"
                buf.append(c.text);
                break;
            case "{": // { ～ }
                buf.append("}");
                break;
            case "(": // ( ～ )
                buf.append(")");
                break;
            case "[": // [ ～ ]
                buf.append("]");
                break;

            default:
                return;
            }
            c.text = buf.toString();
            c.caretOffset = c.offset + 1;
            c.shiftsCaret = false;
            c.doit = false;
        } catch (Exception e) {}
    }

}
