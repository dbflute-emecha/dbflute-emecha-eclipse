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
package org.dbflute.emecha.eclipse.dfeditor.scanner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dbflute.emecha.eclipse.text.TextAttributeDefinition;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Base Scanner
 * @author schatten
 */
public abstract class BsDFPropScanner extends BufferedRuleBasedScanner implements IPartitionTokenScanner {

    private ISharedTextColors colorManager;
    private IPreferenceStore preferenceStore;

    public BsDFPropScanner(ISharedTextColors manager, IPreferenceStore store) {
        super();
        this.colorManager = manager;
        this.preferenceStore = store;
    }

    public void initialize() {
        if (_tokenMap.size() > 0) {
            _tokenMap = new HashMap<TextAttributeDefinition, Token>();
        }
        initializeRules();
    }

    private void initializeRules() {
        List<IRule> ruleList = createRules();
        if (ruleList != null) {
            IRule[] rules = ruleList.toArray(new IRule[ruleList.size()]);
            setRules(rules);
        }
    }

    protected abstract List<IRule> createRules();

    /** The content type of the partition in which to resume scanning. */
    protected String fContentType;
    /** The offset of the partition inside which to resume. */
    protected int fPartitionOffset;

    /**
     * @see org.eclipse.jface.text.rules.IPartitionTokenScanner#setPartialRange(org.eclipse.jface.text.IDocument, int, int, java.lang.String, int)
     */
    public void setPartialRange(IDocument document, int offset, int length, String contentType, int partitionOffset) {
        fContentType = contentType;
        fPartitionOffset = partitionOffset;
        if (partitionOffset > -1) {
            int delta = offset - partitionOffset;
            if (delta > 0) {
                super.setRange(document, partitionOffset, length + delta);
                fOffset = offset;
                return;
            }
        }
        super.setRange(document, offset, length);

    }

    private Map<TextAttributeDefinition, Token> _tokenMap = new HashMap<TextAttributeDefinition, Token>();

    protected Token getToken(TextAttributeDefinition colorType) {
        Token token = _tokenMap.get(colorType);
        if (token == null) {
            token = new Token(createTextAttribute(colorType));
            _tokenMap.put(colorType, token);
        }
        return token;
    }

    private TextAttribute createTextAttribute(TextAttributeDefinition fontManager) {
        String foreground = preferenceStore.getString(fontManager.getForegroundKey());
        RGB foreColor;
        if (IPreferenceStore.STRING_DEFAULT_DEFAULT.equals(foreground)) {
            foreColor = fontManager.getForeground();
        } else {
            foreColor = StringConverter.asRGB(foreground, fontManager.getForeground());
        }
        Color fore = colorManager.getColor(foreColor);

        String background = preferenceStore.getString(fontManager.getBackgroundKey());
        RGB backRGB = null;
        if (IPreferenceStore.STRING_DEFAULT_DEFAULT.equals(foreground)) {
            backRGB = fontManager.getBackground();
        } else {
            backRGB = StringConverter.asRGB( background, fontManager.getBackground());
        }
        Color back = colorManager.getColor(backRGB);

        int style = preferenceStore.getInt(fontManager.getStyleKey());
        return new TextAttribute(fore, back, style);
    }

}
