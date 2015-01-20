/*
 *
 */
package org.dbflute.emecha.eclipse.sqltools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;

/**
 * パラメータコメント内の補完を行うプロセッサ
 */
public class ParameterCommentAssistProcessor extends OutsideSqlAssistProcessorBase {
    private static List<ParameterCommentProposal> proposals = new ArrayList<ParameterCommentProposal>();
    static {
        proposals.add(new IfEndCommentProposal());
        proposals.add(new IfElseEndCommentProposal());
        //proposals.add(new IfCommentProposal());
        proposals.add(new BeginEndCommentProposal());
        //proposals.add(new BeginCommentProposal());
        proposals.add(new ForEndCommentProposal());
        proposals.add(new ForFullSpecCommentProposal());
        proposals.add(new FirstCommentProposal());
        proposals.add(new NextCommentProposal());
        proposals.add(new LastCommentProposal());

        proposals.add(new EndCommentProposal());
    }

    private Image icon;

    public ParameterCommentAssistProcessor(OutsideSqlAssistProcessorBase... processor) {
        super(processor);
        icon = SQLAssistPlugin.getImageDescriptor("icons/listmark-note.gif").createImage();
    }

    /**
     * {@inheritDoc}
     * @see org.dbflute.emecha.eclipse.sqltools.OutsideSqlAssistProcessorBase#appendCompletionProposal(java.util.List, org.eclipse.jface.text.ITextViewer, int)
     */
    @Override
    protected void appendCompletionProposal(List<ICompletionProposal> list, ITextViewer viewer, int offset) {
        try {
            IRegion offsetRegion = viewer.getDocument().getLineInformationOfOffset(offset);
            int markOffset = offsetRegion.getOffset();
            String line = viewer.getDocument().get(markOffset, offsetRegion.getLength());
            int cursol = offset - markOffset;
            int start = line.lastIndexOf("/*", cursol);
            if (start == -1) {
                return;
            }
            markOffset += start;
            int cursolOffset = cursol - start;
            if (cursolOffset < 2) {
                return;
            }
            String markPrefix = line.substring(start + 2, cursol);
            int end = line.indexOf("*/", cursol) + 2;
            if (end == -1) {
                return;
            }
            String target = line.substring(start, end);

            for (ParameterCommentProposal proposal : proposals) {
                if (!proposal.getPropertyMark().startsWith(markPrefix)) {
                    continue;
                }
                String replacementString = proposal.getReplaceString(target, cursolOffset, start);

                IContextInformation contextInformation = null;
                String additionalProposalInfo = proposal.getAdditionalProposalInfo();
                list.add(new CompletionProposal(replacementString, markOffset, target.length(), proposal.getCursorPositionOffset(), icon,
                        proposal.getDisplayString(), contextInformation, additionalProposalInfo));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static abstract class ParameterCommentProposal extends OutsideSqlProposal {
        public ParameterCommentProposal(String propertyMark) {
            super(propertyMark);
        }

        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            return getPropertyMark();
        }
    }

    // -----------------------------------------------------
    //                                    Variable Parameter
    //                                    ------------------

    // -----------------------------------------------------
    //                                          IF Parameter
    //                                          ------------
    protected static class IfCommentProposal extends ParameterCommentProposal {
        public IfCommentProposal() {
            super("IF");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            int index = targetComment.indexOf("pmb.");
            if (index > 0) {
                return "/*IF pmb." + targetComment.substring(index + 4);
            }
            return "/*IF pmb." + targetComment.substring(cursolOffset);
        }

        @Override
        public int getCursorPositionOffset() {
            return 9; // /*IF pmb.[cursol position]*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "  ...<br>" + " where<br>"
                + "   <b>/*IF</b> pmb.memberId != null<b>*/</b><br>" + "   MEMBER_ID = /*pmb.memberId*/3<br>" + "   /*END*/";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "IF";
        }
    }

    protected static class IfEndCommentProposal extends ParameterCommentProposal {
        public IfEndCommentProposal() {
            super("IF");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            int index = targetComment.indexOf("pmb.");
            if (index > 0) {
                return "/*IF pmb." + targetComment.substring(index + 4) + "/*END*/";
            }
            return "/*IF pmb." + targetComment.substring(cursolOffset) + "/*END*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 9; // /*IF pmb.[cursol position]*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "  ...<br>" + " where<br>"
                + "   <b>/*IF</b> pmb.memberId != null<b>*/</b><br>" + "   MEMBER_ID = /*pmb.memberId*/3<br>" + "   <b>/*END*/</b>";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "IF - END";
        }
    }

    protected static class IfElseEndCommentProposal extends ParameterCommentProposal {
        public IfElseEndCommentProposal() {
            super("IF");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            String space = getSpaceIndent(indent);
            int index = targetComment.indexOf("pmb.");
            String suffix;
            if (index > 0) {
                suffix = targetComment.substring(index + 4);
            } else {
                suffix = targetComment.substring(cursolOffset);
            }
            return "/*IF pmb." + suffix + DEFAULT_LINE_DELIMEITER + space + "-- ELSE" + DEFAULT_LINE_DELIMEITER + space + "/*END*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 9; // /*IF pmb.[cursol position]*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "<b>/*IF</b> pmb.isPaging()<b>*/</b><br>"
                + "select member.MEMBER_ID<br>" + "     , member.MEMBER_NAME<br>" + "     , ...<br>" + "<b>-- ELSE</b> select count(*)<br>"
                + "<b>/*END*/</b><br>" + "  from ...";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "IF - ELSE - END";
        }
    }

    // -----------------------------------------------------
    //                                       BEGIN Parameter
    //                                       ---------------
    protected static class BeginCommentProposal extends ParameterCommentProposal {
        public BeginCommentProposal() {
            super("BEGIN");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            return "/*BEGIN*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 9; // /*BEGIN*/[cursol position]
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "select * from MEMBER<br>" + "<b>/*BEGIN*/</b><br>" + " where<br>"
                + "   /*IF pmb.memberId != null*/<br>" + "   MEMBER_ID = /*pmb.memberId*/3<br>" + "   /*END*/<br>"
                + "   /*IF pmb.memberName != null*/<br>" + "   MEMBER_NAME like /*pmb.memberName*/'S%'<br>" + "   /*END*/<br>"
                + "   /*FOR pmb.memberAccountList*/<br>" + "   and MEMBER_ACCOUNT = /*#current*/'foo'/*END*/<br>" + "   /*END*/<br>"
                + "/*END*/<br>" + "order by MEMBER_ID";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "BEGIN";
        }
    }

    protected static class BeginEndCommentProposal extends ParameterCommentProposal {
        public BeginEndCommentProposal() {
            super("BEGIN");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            return "/*BEGIN*//*END*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 9; // /*BEGIN*/[cursol position]/*END*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "select * from MEMBER<br>" + "<b>/*BEGIN*/</b><br>" + " where<br>"
                + "   /*IF pmb.memberId != null*/<br>" + "   MEMBER_ID = /*pmb.memberId*/3<br>" + "   /*END*/<br>"
                + "   /*IF pmb.memberName != null*/<br>" + "   MEMBER_NAME like /*pmb.memberName*/'S%'<br>" + "   /*END*/<br>"
                + "   /*FOR pmb.memberAccountList*/<br>" + "   and MEMBER_ACCOUNT = /*#current*/'foo'/*END*/<br>" + "   /*END*/<br>"
                + "<b>/*END*/</b><br>" + "order by MEMBER_ID";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "BEGIN - END";
        }
    }

    // -----------------------------------------------------
    //                                         FOR Parameter
    //                                         -------------

    protected static class ForEndCommentProposal extends ParameterCommentProposal {
        public ForEndCommentProposal() {
            super("FOR");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            int index = targetComment.indexOf("pmb.");
            String suffix;
            if (index > 0) {
                suffix = targetComment.substring(index + 4);
            } else {
                suffix = targetComment.substring(cursolOffset);
            }
            return "/*FOR pmb." + suffix + "/*END*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 10; // /*FOR pmb.[cursol position]*//*END*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "   <b>/*FOR</b> pmb.memberAccountList<b>*/</b><br>"
                + "   and MEMBER_ACCOUNT = /*#current*/'foo'/*END*/<br>" + "   <b>/*END*/</b><br>";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "FOR - END";
        }
    }

    protected static class ForFullSpecCommentProposal extends ParameterCommentProposal {
        public ForFullSpecCommentProposal() {
            super("FOR");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            String space = getSpaceIndent(indent);
            int index = targetComment.indexOf("pmb.");
            String suffix;
            if (index > 0) {
                suffix = targetComment.substring(index + 4);
            } else {
                suffix = targetComment.substring(cursolOffset);
            }
            return "/*FOR pmb." + suffix + DEFAULT_LINE_DELIMEITER + space + "  /*FIRST*//*END*/" + DEFAULT_LINE_DELIMEITER + space
                    + "  /*NEXT ''*/" + DEFAULT_LINE_DELIMEITER + space + "  /*LAST*//*END*/" + DEFAULT_LINE_DELIMEITER + space + "/*END*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 10; // /*FOR pmb.[cursol position]*//*END*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "/*BEGIN*/<br>" + "where<br>" + "  /*IF pmb.memberId != null*/<br>"
                + "  member.MEMBER_ID = /*pmb.memberId*/3<br>" + "  /*END*/<br>" + "  <b>/*FOR</b> pmb.memberNameList<b>*/</b><br>"
                + "    <b>/*FIRST*/</b>and (<b>/*END*/</b><br>"
                + "    <b>/*NEXT</b> 'or '<b>*/</b><br>member.MEMBER_NAME like /*#current*/'S%'<br>"
                + "    <b>/*LAST*/</b>)<b>/*END*/</b><br>" + "  <b>/*END*/</b><br>" + "/*END*/";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "FOR - FIRST - NEXT - LAST - END";
        }
    }

    protected static class FirstCommentProposal extends ParameterCommentProposal {
        public FirstCommentProposal() {
            super("FIRST");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            return "/*FIRST*//*END*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 9; // /*FIRST*/[cursol position]/*END*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "/*BEGIN*/<br>" + "where<br>" + "  /*IF pmb.memberId != null*/<br>"
                + "  member.MEMBER_ID = /*pmb.memberId*/3<br>" + "  /*END*/<br>" + "  /*FOR pmb.memberNameList*/<br>"
                + "    <b>/*FIRST*/</b>and (<b>/*END*/</b><br>" + "    /*NEXT 'or '*/<br>member.MEMBER_NAME like /*#current*/'S%'<br>"
                + "    /*LAST*/)/*END*/<br>" + "  /*END*/<br>" + "/*END*/";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "FIRST - END";
        }
    }

    protected static class NextCommentProposal extends ParameterCommentProposal {
        public NextCommentProposal() {
            super("NEXT");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {

            return "/*NEXT '" + targetComment.substring(cursolOffset, targetComment.length() - 2) + "'*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 8; // /*NEXT '[cursol position]'*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "/*BEGIN*/<br>" + "where<br>" + "  /*FOR pmb.memberNameList*/<br>"
                + "    <b>/*NEXT</b> 'or '<b>*/</b><br>member.MEMBER_NAME like /*#current*/'S%'<br>" + "  /*END*/<br>" + "/*END*/";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "NEXT 'SQL-expression'";
        }
    }

    protected static class LastCommentProposal extends ParameterCommentProposal {
        public LastCommentProposal() {
            super("LAST");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            return "/*LAST*//*END*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 8; // /*LAST*/[cursol position]/*END*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "/*BEGIN*/<br>" + "where<br>" + "  /*IF pmb.memberId != null*/<br>"
                + "  member.MEMBER_ID = /*pmb.memberId*/3<br>" + "  /*END*/<br>" + "  /*FOR pmb.memberNameList*/<br>"
                + "    /*FIRST*/and (/*END*/<br>" + "    /*NEXT 'or '*/member.MEMBER_NAME like /*#current*/'S%'<br>"
                + "    <b>/*LAST*/</b>)<b>/*END*/</b><br>" + "  /*END*/<br>" + "/*END*/";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "LAST - END";
        }
    }

    // -----------------------------------------------------
    //                                         END Parameter
    //                                         -------------

    protected static class EndCommentProposal extends ParameterCommentProposal {
        public EndCommentProposal() {
            super("END");
        }

        @Override
        public String getReplaceString(String targetComment, int cursolOffset, int indent) {
            return "/*END*/";
        }

        @Override
        public int getCursorPositionOffset() {
            return 7; // /*END*/[cursol position]
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "  ...<br>" + " where<br>" + "   /*IF pmb.memberId != null*/<br>"
                + "   MEMBER_ID = /*pmb.memberId*/3<br>" + "   <b>/*END*/</b>";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "END";
        }
    }

}
