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
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

public abstract class AbstractAutoEditStrategy extends DefaultIndentLineAutoEditStrategy {

    private static final String DEFAULT_LINE_DELIMEITER = System.getProperty("line.separator");

    /**
     * 改行コードを取得する。
     * @param d
     * @param c
     * @return 改行文字列
     * @throws BadLocationException
     */
    protected String getLineDelimiter(IDocument d, DocumentCommand c) throws BadLocationException {
        if ("\n".equals(c.text) || "\r\n".equals(c.text) || "\r".equals(c.text)) {
            return c.text;
        }
        int lineOfOffset = d.getLineOfOffset(c.offset);
        int maxLine = d.getNumberOfLines();
        String delimiter = null;
        if (maxLine - 1 > lineOfOffset) {
            delimiter = d.getLineDelimiter(lineOfOffset);
        } else if (lineOfOffset > 1) {
            delimiter = d.getLineDelimiter(lineOfOffset - 1);
        } else if (maxLine > 1) {
            delimiter = d.getLineDelimiter(1);
        }
        if (delimiter == null) {
            delimiter = DEFAULT_LINE_DELIMEITER;
        }
        return delimiter;
    }

}
