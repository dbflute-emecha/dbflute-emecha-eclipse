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

import java.util.ArrayList;
import java.util.List;

import org.dbflute.emecha.eclipse.dfeditor.DfColor;
import org.dbflute.emecha.eclipse.dfeditor.rule.CombinedWordRule;
import org.dbflute.emecha.eclipse.dfeditor.rule.CombinedWordRule.DfTagWordDetector;
import org.dbflute.emecha.eclipse.dfeditor.rule.SingleLineSiegeRule;
import org.dbflute.emecha.eclipse.dfeditor.rule.WhitespaceDetector;
import org.dbflute.emecha.eclipse.text.scanner.EmRuleBasedScanner;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.source.ISharedTextColors;

public class DefaultTokenScanner extends EmRuleBasedScanner {

    public DefaultTokenScanner(ISharedTextColors manager, IPreferenceStore store) {
        super(manager, store);
        setDefaultReturnToken(getToken(DfColor.DEFAULT));
        initialize();
    }

    @Override
    protected List<IRule> createRules() {
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(new WhitespaceRule(new WhitespaceDetector()));

        rules.add(new SingleLineSiegeRule("$$", "$$", getToken(DfColor.ALIAS_MARK)));
        rules.add(new SingleLineSiegeRule("/*", "*/", getToken(DfColor.SQL)));
        rules.add(new SingleLineSiegeRule("\"", "\"", getToken(DfColor.VALIABLE)));
        rules.add(new SingleLineSiegeRule("'", "'", getToken(DfColor.VALIABLE)));

        CombinedWordRule wordRule = new CombinedWordRule();
        CombinedWordRule.WordMatcher mapMacher = new CombinedWordRule.WordMatcher();
        mapMacher.addWord("map:", getToken(DfColor.FIXED_LITERAL_MARK));
        wordRule.addWordMatcher(mapMacher);
        CombinedWordRule.WordMatcher listMacher = new CombinedWordRule.WordMatcher();
        listMacher.addWord("list:", getToken(DfColor.FIXED_LITERAL_MARK));
        wordRule.addWordMatcher(listMacher);

        CombinedWordRule.WordMatcher trueMacher = new CombinedWordRule.WordMatcher();
        listMacher.addWord("true", getToken(DfColor.FIXED_LITERAL_MARK));
        wordRule.addWordMatcher(trueMacher);
        CombinedWordRule.WordMatcher falseMacher = new CombinedWordRule.WordMatcher();
        listMacher.addWord("false", getToken(DfColor.FIXED_LITERAL_MARK));
        wordRule.addWordMatcher(falseMacher);

        CombinedWordRule.WordMatcher suffixMacher = new CombinedWordRule.WordMatcher();
        suffixMacher.addWord("suffix:", getToken(DfColor.LIKE_SEARCH_MARK));
        wordRule.addWordMatcher(suffixMacher);
        CombinedWordRule.WordMatcher prefixMacher = new CombinedWordRule.WordMatcher();
        prefixMacher.addWord("prefix:", getToken(DfColor.LIKE_SEARCH_MARK));
        wordRule.addWordMatcher(prefixMacher);
        CombinedWordRule.WordMatcher containMacher = new CombinedWordRule.WordMatcher();
        containMacher.addWord("contain:", getToken(DfColor.LIKE_SEARCH_MARK));
        wordRule.addWordMatcher(containMacher);

        rules.add(wordRule);

        CombinedWordRule dollWordRule = new CombinedWordRule(new DfTagWordDetector() {
            @Override
            public boolean isWordStart(char c) {
                if (c == '$') {
                    return true;
                }
                return false;
            }
        });
        CombinedWordRule.WordMatcher sqlMacher = new CombinedWordRule.WordMatcher();
        sqlMacher.addWord("$sql:", getToken(DfColor.ALIAS_MARK));
        dollWordRule.addWordMatcher(sqlMacher);

        rules.add(dollWordRule);

        return rules;
    }

}
