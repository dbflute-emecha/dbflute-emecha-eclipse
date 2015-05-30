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
import org.dbflute.emecha.eclipse.text.rule.SingleLineSiegeRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * ドキュメントのパーティショニングを行うスキャナー
 * @author schatten
 */
public class PMPartitionScanner extends RuleBasedPartitionScanner implements PMPartitions {

    public PMPartitionScanner() {

        IToken pmComment = new Token(PM_PARAMETER);

        List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

        rules.add(new SingleLineSiegeRule("/*", "*/", pmComment));

        setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
    }
}
