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

import org.dbflute.emecha.eclipse.dfeditor.DFPropPartitions;
import org.dbflute.emecha.eclipse.text.rule.SingleLineSiegeRule;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * dfpropのパーティショニングを行うクラス。
 */
public class DFPropPartitionScanner extends RuleBasedPartitionScanner implements DFPropPartitions {

    public DFPropPartitionScanner() {

        IToken dfpComment = new Token(DFP_COMMENT);
        IToken tagPartition = new Token(DFP_PARTITIONING);
        IToken literalPartition = new Token(DFP_LITERAL);

        List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

        rules.add(new SingleLineSiegeRule("/*", "*/", tagPartition));
        rules.add(new SingleLineSiegeRule("\"", "\"", literalPartition));
        rules.add(new SingleLineSiegeRule("'", "'", literalPartition));

        rules.add(new EndOfLineRule("#", dfpComment));

        setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
    }

}
