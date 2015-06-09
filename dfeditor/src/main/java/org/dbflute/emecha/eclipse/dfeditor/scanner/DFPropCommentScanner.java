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
import org.dbflute.emecha.eclipse.text.scanner.EmRuleBasedScanner;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.source.ISharedTextColors;

public class DFPropCommentScanner extends EmRuleBasedScanner {

    public DFPropCommentScanner(ISharedTextColors manager, IPreferenceStore store) {
        super(manager, store);
        initialize();
    }

    @Override
    protected List<IRule> createRules() {
        ArrayList<IRule> rules = new ArrayList<IRule>();

        rules.add(new EndOfLineRule("#", getToken(DfColor.LINE_COMMENT)));

        return rules;
    }

}
