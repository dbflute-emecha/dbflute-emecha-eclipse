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
package org.dbflute.emecha.eclipse.sqltools.action;

import java.util.List;

import org.dbflute.emecha.eclipse.sqltools.SQLAssistPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;

/**
 * プロパティコメントを補完するプロセッサ
 */
public class PropertyCommentAssistProcessor extends OutsideSqlAssistProcessorBase {

    private Image icon;

    public PropertyCommentAssistProcessor(OutsideSqlAssistProcessorBase... processor) {
        super(processor);
        icon = SQLAssistPlugin.getImageDescriptor("icons/listmark-rest.gif").createImage();
    }

    protected void appendCompletionProposal(List<ICompletionProposal> list, ITextViewer viewer, int offset) {
        String prefix = null;

        try {
            IRegion offsetRegion = viewer.getDocument().getLineInformationOfOffset(offset);
            int markOffset = offsetRegion.getOffset();
            String line = viewer.getDocument().get(markOffset, offsetRegion.getLength());
            if (!line.startsWith("--")) {
                int index = line.indexOf("--");
                if (index == -1) {
                    return;
                }
                line = line.substring(index);
                markOffset += index;
            }
            if (markOffset + 2 > offset) {
                return;
            }

            String offsetString = line.substring(2, offset - markOffset);
            String suffixString = line.substring(offset - markOffset);
            markOffset += 2;
            int spaceCount = 0;
            boolean delimSpace = false;
            for (char c : offsetString.toCharArray()) {
                if (c != '-' && c != ' ') {
                    break;
                }
                if (c == ' ') {
                    delimSpace = true;
                }
                spaceCount++;
            }
            if (spaceCount > 0) {
                prefix = offsetString.substring(spaceCount);
                markOffset += spaceCount;
            } else {
                prefix = offsetString;
            }

            addFixedMarkProposal(list, prefix, suffixString, markOffset, delimSpace);
            addPropertyMarkProposal(list, prefix, suffixString, markOffset, delimSpace);
            addPackageMarkProposal(list, prefix, suffixString, markOffset, delimSpace);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private static final String[] DBFLUTE_MARK_OPTIONS = { "#df:entity#", "!!AutoDetect!!", "!df:pmb!", "!df:pmb extends Paging!",
            "+cursor+", "+domain+", "+scalar+", "#df:x#" };

    private void addFixedMarkProposal(List<ICompletionProposal> list, String prefix, String suffix, int offset, boolean delimSpace) {
        int suffixLength = 0;
        if (prefix.length() > 0) {
            char startMark = prefix.charAt(0);
            boolean endBres = false;
            for (char c : suffix.toCharArray()) {
                if (('a' <= c && 'z' >= c) || ('A' <= c && 'Z' >= c)) {
                    if (endBres)
                        break;
                    suffixLength++;
                    continue;
                }
                if (startMark == c) {
                    suffixLength++;
                    endBres = true;
                    continue;
                }
                break;
            }
        }

        int replacementLength = prefix.length() + suffixLength;
        for (String markString : DBFLUTE_MARK_OPTIONS) {
            if (prefix == null || prefix.isEmpty() || (prefix != null && markString.startsWith(prefix))) {
                String replacementString = delimSpace ? markString : " " + markString;
                IContextInformation contextInformation = null;
                String additionalProposalInfo = null;
                list.add(new CompletionProposal(replacementString, offset, replacementLength, replacementString.length(), icon, markString,
                        contextInformation, additionalProposalInfo));
            }
        }
    }

    private static final String[] ENTITY_PROPERTY_TYPES = { "String", "Integer", "Long", "BigDecimal", "LocalDate", "LocalDateTime", "LocalTime", "Date", "Timestamp", "Time" };
    private static final String[] PARAMETER_PROPERTY_TYPES = { "String", "Integer", "Long", "BigDecimal", "LocalDate", "LocalDateTime", "LocalTime", "Date", "Timestamp", "Time",
            "List<T>", "Map<K,V>" };

    private void addPropertyMarkProposal(List<ICompletionProposal> list, String prefix, String suffix, int offset, boolean delimSpace) {
        if (prefix != null && prefix.length() > 1) {
            String startMark = prefix.substring(0, 2);
            String[] propertyTypes = null;
            if ("!!".equals(startMark)) {
                propertyTypes = PARAMETER_PROPERTY_TYPES;
            } else if ("##".equals(startMark)) {
                propertyTypes = ENTITY_PROPERTY_TYPES;
            }
            if (propertyTypes != null) {
                int replacementLength = prefix.length();
                if (suffix.length() > 0 && suffix.charAt(0) == ' ') {
                    replacementLength++;
                }
                int endMark = suffix.indexOf(startMark);
                for (String markString : propertyTypes) {
                    if (!(startMark + markString).startsWith(prefix)) {
                        continue;
                    }
                    StringBuilder replacementString = new StringBuilder();
                    if (!delimSpace)
                        replacementString.append(" ");
                    replacementString.append(startMark);
                    replacementString.append(markString);
                    replacementString.append(" ");
                    int cursolOffset = replacementString.length();
                    if (endMark < 0) {
                        replacementString.append(startMark);
                    }
                    IContextInformation contextInformation = null;
                    String additionalProposalInfo = startMark + markString + " property-name" + startMark;
                    list.add(new CompletionProposal(replacementString.toString(), offset, replacementLength, cursolOffset, icon,
                            markString, contextInformation, additionalProposalInfo));
                }
            }
        }
    }

    private void addPackageMarkProposal(List<ICompletionProposal> list, String prefix, String suffix, int offset, boolean delimSpace) {
        if (prefix != null && prefix.length() > 1) {
            String startMark = prefix.substring(0, 2);
            if (!"!!".equals(startMark)) {
                return;
            }
            int replacementLength = prefix.length();
            if (suffix.length() > 0 && suffix.charAt(0) == ' ') {
                replacementLength++;
            }
            int endMark = suffix.indexOf(startMark);
            for (PackageRefMark mark : PackageRefMark.values()) {
                String markString = mark.getReplaceString();
                if (!(startMark + markString).startsWith(prefix)) {
                    continue;
                }
                StringBuilder replacementString = new StringBuilder();
                if (!delimSpace)
                    replacementString.append(" ");
                replacementString.append(startMark);
                replacementString.append(markString);
                replacementString.append(".");
                int cursolOffset = replacementString.length();
                replacementString.append(" ");
                if (endMark < 0) {
                    replacementString.append(startMark);
                }
                IContextInformation contextInformation = null;
                String additionalProposalInfo = mark.getAdditionalProposalInfo();
                list.add(new CompletionProposal(replacementString.toString(), offset, replacementLength, cursolOffset, icon, mark
                        .getDisplayString(), contextInformation, additionalProposalInfo));
            }
        }
    }

    private enum PackageRefMark {
        Domain {
            @Override
            public String getReplaceString() {
                return "$$Domain$$";
            }

            @Override
            public String getDisplayString() {
                return "DomainEntity Package";
            }

            @Override
            public String getAdditionalProposalInfo() {
                return "-- !df:pmb!<br><b>-- !!$$Domain$$.Member member!!</b><br>select ...<br>  from ...";
            }
        },
        Customize {
            @Override
            public String getReplaceString() {
                return "$$Customize$$";
            }

            @Override
            public String getDisplayString() {
                return "CustomizeEntity Package";
            }

            @Override
            public String getAdditionalProposalInfo() {
                return "-- !df:pmb!<br><b>-- !!$$Customize$$.SimpleMember member!!</b><br>select ...<br>  from ...";
            }
        },
        Pmb {
            @Override
            public String getReplaceString() {
                return "$$Pmb$$";
            }

            @Override
            public String getDisplayString() {
                return "ParameterBean Package";
            }

            @Override
            public String getAdditionalProposalInfo() {
                return "-- !df:pmb!<br><b>-- !!$$Pmb$$.SimpleMemberPmb memberPmb!!</b><br>select ...<br>  from ...";
            }
        },
        CDef {
            @Override
            public String getReplaceString() {
                return "$$CDef$$";
            }

            @Override
            public String getDisplayString() {
                return "CDef Package";
            }

            @Override
            public String getAdditionalProposalInfo() {
                return "-- !df:pmb!<br><b>-- !!$$CDef$$.MemberStatus status!!</b><br>select ...<br>  from ...";
            }
        };
        public abstract String getReplaceString();

        public abstract String getDisplayString();

        public abstract String getAdditionalProposalInfo();
    }

}
