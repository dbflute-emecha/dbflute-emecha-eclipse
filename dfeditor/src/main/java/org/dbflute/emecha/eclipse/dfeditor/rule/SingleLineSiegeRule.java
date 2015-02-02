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
package org.dbflute.emecha.eclipse.dfeditor.rule;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;

/**
 * @author schatten
 * @see org.eclipse.jface.text.rules.PatternRule
 */
public class SingleLineSiegeRule extends PatternRule {

    /**
     * Comparator that orders <code>char[]</code> in decreasing array lengths.
     *
     * @since 3.1
     */
    private static class DecreasingCharArrayLengthComparator implements Comparator<char[]> {
        public int compare(char[] o1, char[] o2) {
            return ((char[]) o2).length - ((char[]) o1).length;
        }
    }

    /**
     * Line delimiter comparator which orders according to decreasing delimiter length.
     * @since 3.1
     */
    private Comparator<char[]> fLineDelimiterComparator = new DecreasingCharArrayLengthComparator();
    /**
     * Cached line delimiters.
     * @since 3.1
     */
    private char[][] fLineDelimiters;
    /**
     * Cached sorted {@linkplain #fLineDelimiters}.
     * @since 3.1
     */
    private char[][] fSortedLineDelimiters;

    public SingleLineSiegeRule(String startSequence, String endSequence, IToken token) {
        super(startSequence, endSequence, token, (char) 0, true, false);
    }

    /**
     * Returns whether the end sequence was detected. As the pattern can be considered
     * ended by a line delimiter, the result of this method is <code>true</code> if the
     * rule breaks on the end of the line, or if the EOF character is read.
     *
     * @param scanner the character scanner to be used
     * @return <code>true</code> if the end sequence has been detected
     */
    protected boolean endSequenceDetected(ICharacterScanner scanner) {

        char[][] originalDelimiters = scanner.getLegalLineDelimiters();
        int count = originalDelimiters.length;
        if (fLineDelimiters == null || fLineDelimiters.length != count) {
            fSortedLineDelimiters = new char[count][];
        } else {
            while (count > 0 && Arrays.equals(fLineDelimiters[count - 1], originalDelimiters[count - 1])) {
                count--;
            }
        }
        if (count != 0) {
            fLineDelimiters = originalDelimiters;
            System.arraycopy(fLineDelimiters, 0, fSortedLineDelimiters, 0, fLineDelimiters.length);
            Arrays.sort(fSortedLineDelimiters, fLineDelimiterComparator);
        }

        int readCount = 1;
        int c;
        while ((c = scanner.read()) != ICharacterScanner.EOF) {
            if (c == fEscapeCharacter) {
                // Skip escaped character(s)
                if (fEscapeContinuesLine) {
                    c = scanner.read();
                    for (int i = 0; i < fSortedLineDelimiters.length; i++) {
                        if (c == fSortedLineDelimiters[i][0] && sequenceDetected(scanner, fSortedLineDelimiters[i], fBreaksOnEOF)) {
                            break;
                        }
                    }
                } else {
                    scanner.read();
                }

            } else if (fEndSequence.length > 0 && c == fEndSequence[0]) {
                // Check if the specified end sequence has been found.
                if (sequenceDetected(scanner, fEndSequence, fBreaksOnEOF)) {
                    return true;
                }
            } else if (fBreaksOnEOL) {
                // Check for end of line since it can be used to terminate the pattern.
                boolean endOfLine = false;
                for (int i = 0; i < fSortedLineDelimiters.length; i++) {
                    if (c == fSortedLineDelimiters[i][0] && sequenceDetected(scanner, fSortedLineDelimiters[i], fBreaksOnEOF)) {
                        endOfLine = true;
                        break;
                    }
                }
                if (endOfLine) {
                    break;
                }
            }
            readCount++;
        }

        for (; readCount > 0; readCount--) {
            scanner.unread();
        }

        return false;
    }

}
