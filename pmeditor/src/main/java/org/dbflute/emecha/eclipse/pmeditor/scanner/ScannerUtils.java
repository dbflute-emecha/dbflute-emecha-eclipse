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
package org.dbflute.emecha.eclipse.pmeditor.scanner;

/**
 * @author schatten
 */
public final class ScannerUtils {
    private static final String LF = "\n";

    private ScannerUtils() {}

    public static int indexOf(String doc, String sepText) {
        return indexOf(doc, sepText, 0);
    }
    public static int indexOf(String doc, String sepText, int offset) {
        if (doc == null || sepText == null) {
            return -1;
        }
        int position = -1;
        int leng = sepText.length();
        position = doc.indexOf(sepText, offset);
        if (position > 0) {
            position = doc.indexOf(LF + sepText, offset);
            if (position >= 0) {
                position++;
                if (doc.length() > position + leng) {
                    char charAt = doc.charAt(position + leng);
                    if (!(charAt == '\r' || charAt == '\n')) {
                        return indexOf(doc, sepText, position + leng);
                    }
                }
                position++;
            }
        } else if (position == 0) {
            if (doc.length() > leng) {
                char charAt = doc.charAt(position + leng);
                if (!(charAt == '\r' || charAt == '\n')) {
                    return indexOf(doc, sepText, position + leng);
                }
                position++;
            }
        } else {
            position = -1;
        }
        return position;
    }
}
