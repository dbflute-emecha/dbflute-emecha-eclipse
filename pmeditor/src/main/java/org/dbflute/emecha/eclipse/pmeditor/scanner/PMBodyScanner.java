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

import java.util.ArrayList;
import java.util.List;

import org.dbflute.emecha.eclipse.pmeditor.PMPartitions;
import org.dbflute.emecha.eclipse.pmeditor.PmColorDef;
import org.dbflute.emecha.eclipse.text.TextAttributeDefinition;
import org.dbflute.emecha.eclipse.text.rule.SingleLineSiegeRule;
import org.dbflute.emecha.eclipse.text.scanner.EmRuleBasedScanner;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISharedTextColors;

/**
 * PM Text Body Scanner
 * @author schatten
 */
public class PMBodyScanner extends EmRuleBasedScanner implements PMPartitions {
    private static final String LF = "\n";
    private String sepText;
    private int separatePosition = -1;

    public PMBodyScanner(ISharedTextColors manager, IPreferenceStore store) {
        super(manager, store);
        setDefaultReturnToken(new Token(PmColorDef.DEFAULT));
        initialize();
    }

    @Override
    public void initialize() {
        separatePosition = -1;
        this.sepText = DEFAULT_SEPARATOR_TEXT;
        super.initialize();
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.text.scanner.EmRuleBasedScanner#createRules()
     */
    @Override
    protected List<IRule> createRules() {
        List<IRule> rules = new ArrayList<IRule>();

        rules.add(new EndOfLineRule("--", new Token(PmColorDef.PROPERTY_COMMENT)));
        rules.add(new EndOfLineRule(this.sepText, new Token(PmColorDef.SEPARATOR)));
        rules.add(new SingleLineSiegeRule("/*", "*/", getToken(PmColorDef.PARAMETER_COMMENT)));

        return rules;
    }

    /**
     *
     * @see org.eclipse.jface.text.rules.BufferedRuleBasedScanner#setRange(org.eclipse.jface.text.IDocument, int, int)
     */
    @Override
    public void setRange(IDocument document, int offset, int length) {
        if (document != null) {
            String doc = document.get();
            if (doc != null) {
                int leng = this.sepText.length();
                separatePosition = doc.indexOf(this.sepText);
                if (separatePosition > 0) {
                    separatePosition = doc.indexOf(LF + this.sepText);
                    if (separatePosition >= 0 && doc.length() > separatePosition + leng + 1) {
                        char charAt = doc.charAt(separatePosition + leng + 1);
                        if (charAt == '\r' || charAt == '\n') {
                            separatePosition += leng + 2;
                        } else {
                            separatePosition = -1;
                        }
                    }
                } else if (separatePosition == 0 && doc.length() > leng) {
                    char charAt = doc.charAt(separatePosition + leng);
                    if (charAt == '\r' || charAt == '\n') {
                        separatePosition += leng + 1;
                    } else {
                        separatePosition = -1;
                    }
                } else {
                    separatePosition = -1;
                }
            }
        }
        super.setRange(document, offset, length);
    }

    @Override
    public IToken nextToken() {
        IToken nextToken = super.nextToken();
        Object data = nextToken.getData();
        if (data instanceof PmColorDef) {
            if (fOffset <= separatePosition) {
                PmColorDef def = (PmColorDef) data;
                switch (def) {
                case DEFAULT:
                    return getToken(PmColorDef.HEADER);
                default:
                    return getToken(def);
                }
            } else if (PmColorDef.PARAMETER_COMMENT.equals(data)) {
                return getToken((TextAttributeDefinition) data);
            }
            return getToken(PmColorDef.DEFAULT);
        }
        return nextToken;
    }
}
