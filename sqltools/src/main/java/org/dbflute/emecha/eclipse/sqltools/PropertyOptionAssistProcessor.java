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
 *
 */
public class PropertyOptionAssistProcessor extends OutsideSqlAssistProcessorBase {

    public PropertyOptionAssistProcessor(OutsideSqlAssistProcessorBase... processor) {
        super(processor);
    }

    private static List<OutsideSqlProposal> proposals;
    static {
        proposals = new ArrayList<PropertyOptionAssistProcessor.OutsideSqlProposal>();
        proposals.add(new FromDateProposal());
        proposals.add(new FromDateOptionProposal());
        proposals.add(new ToDateProposal());
        proposals.add(new ToDateOptionProposal());

        proposals.add(new LikeSearchProposal());
        proposals.add(new LikePrefixSearchProposal());
        proposals.add(new LikeContainSearchProposal());
        proposals.add(new LikeSuffixSearchProposal());
        proposals.add(new NoLikeSearchProposal());

        proposals.add(new ClassificationProposal());
        proposals.add(new ColumnReferenceProposal());

        proposals.add(new CommentProposal());
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

            int endIndex = offset - markOffset;
            int optionMerkIndex = line.lastIndexOf(':', endIndex - 1);
            if (optionMerkIndex < 0) {
                return;
            }
            int optionChainMerkIndex = line.lastIndexOf('|', endIndex - 1);
            if (optionChainMerkIndex > -1) {
                optionMerkIndex = optionChainMerkIndex;
            }
            int optionOffset = markOffset + optionMerkIndex + 1;

            String optionPrefix = line.substring(optionMerkIndex + 1, endIndex);
            int replacementLength = optionPrefix.length();

            Image icon = SQLAssistPlugin.getImageDescriptor("icons/listmark-rest.gif").createImage();
            for (OutsideSqlProposal proposal : proposals) {
                if (!proposal.getPropertyMark().startsWith(optionPrefix)) {
                    continue;
                }
                IContextInformation contextInformation = null;
                String additionalProposalInfo = proposal.getAdditionalProposalInfo();
                list.add(new CompletionProposal(proposal.getPropertyMark(), optionOffset, replacementLength, proposal
                        .getCursorPositionOffset(), icon, proposal.getDisplayString(), contextInformation, additionalProposalInfo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------
    //                                           Date Option
    //                                           -----------
    protected static class FromDateProposal extends OutsideSqlProposal {
        public FromDateProposal() {
            super("fromDate");
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "  ...<br>"
                + " where FORMALIZED_DATETIME &gt;=<br>" + "              /*pmb.fromFormalizedDate:<b>fromDate</B>*/'...'<br>"
                + "   and FORMALIZED_DATETIME &lt;<br>" + "              /*pmb.toFormalizedDate:toDate*/'...'<br>" + "<br>"
                + "-- !df:pmb!<br>" + "-- !!Date fromFormalizedDate:<b>fromDate</b>!!<br>" + "-- !!Date toFormalizedDate:toDate!!<br>"
                + "  ...<br>" + " where FORMALIZED_DATETIME &gt;=<br>" + "              /*pmb.fromFormalizedDate*/'...'<br>"
                + "   and FORMALIZED_DATETIME &lt;<br>" + "              /*pmb.toFormalizedDate*/'...'<br>" + "<br>" + "e.g. @Java<br>"
                + "Date fromDate = toDate(\"2009-10-26 12:34:56\")<br>" + "pmb.setFromFormalizedDate_FromDate(fromDate);<br>" + "<br>"
                + "e.g. @DisplaySQL<br>" + " where FORMALIZED_DATETIME &gt;= '2009-10-26 00:00:00'<br>"
                + "   and FORMALIZED_DATETIME &lt; '2009-10-30 00:00:00'";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }
    }

    protected static class ToDateProposal extends OutsideSqlProposal {
        public ToDateProposal() {
            super("toDate");
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "  ...<br>"
                + " where FORMALIZED_DATETIME &gt;=<br>" + "            /*pmb.fromFormalizedDate:fromDate*/'...'<br>"
                + "   and FORMALIZED_DATETIME &lt;<br>" + "            /*pmb.toFormalizedDate:<b>toDate</B>*/'...'<br>" + "<br>"
                + "-- !df:pmb!<br>" + "-- !!Date fromFormalizedDate:fromDate!!<br>" + "-- !!Date toFormalizedDate:<b>toDate</b>!!<br>"
                + "  ...<br>" + " where FORMALIZED_DATETIME &gt;=<br>" + "            /*pmb.fromFormalizedDate*/'...'<br>"
                + "   and FORMALIZED_DATETIME &lt;<br>" + "            /*pmb.toFormalizedDate*/'...'<br>" + "<br>" + "e.g. @Java<br>"
                + "pmb.setToFormalizedDate_ToDate(toDate(\"2009-10-29 12:34:56\"));<br>" + "<br>" + "e.g. @DisplaySQL<br>"
                + " where FORMALIZED_DATETIME &gt;= '2009-10-26 00:00:00'<br>" + "   and FORMALIZED_DATETIME &lt; '2009-10-30 00:00:00'";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }
    }

    protected static class FromDateOptionProposal extends OutsideSqlProposal {
        public FromDateOptionProposal() {
            super("fromDate(option)");
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "  ...<br>"
                + " where FORMALIZED_DATETIME &gt;=<br>" + "           /*pmb.fromFormalizedDate:<b>fromDate(option)</b>*/'...'<br>"
                + "   and FORMALIZED_DATETIME &lt;<br>" + "           /*pmb.toFormalizedDate:toDate(option)*/'...'<br>" + "<br>"
                + "-- !df:pmb!<br>" + "-- !!Date fromFormalizedDate:<b>fromDate(option)</b>!!<br>"
                + "-- !!Date toFormalizedDate:toDate(option)!!<br>" + "  ...<br>" + " where FORMALIZED_DATETIME &gt;=<br>"
                + "           /*pmb.fromFormalizedDate*/'...'<br>" + "   and FORMALIZED_DATETIME &lt;<br>"
                + "           /*pmb.toFormalizedDate*/'...'<br>" + "<br>" + "e.g. @Java<br>"
                + "Date fromDate = toDate(\"2009-10-03 12:34:56\")<br>" + "Date toDate = toDate(\"2009-12-14 12:34:56\")<br>"
                + "pmb.setFromFormalizedDate_FromDate(fromDate,<br>" + "              op -&gt; op.compareAsMonth());<br>"
                + "pmb.setToFormalizedDate_ToDate(toDate,<br>" + "              op -&gt; op.compareAsMonth());<br>" + "<br>"
                + "e.g. @DisplaySQL<br>" + " where FORMALIZED_DATETIME &gt;= '2009-10-01 00:00:00'<br>"
                + "   and FORMALIZED_DATETIME &lt; '2010-01-01 00:00:00'";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }
    }

    protected static class ToDateOptionProposal extends OutsideSqlProposal {
        public ToDateOptionProposal() {
            super("toDate(option)");
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "...<br>"
                + "where FORMALIZED_DATETIME &gt;=<br>" + "       /*pmb.fromFormalizedDate:fromDate(option)*/'...'<br>"
                + "  and FORMALIZED_DATETIME &lt;<br>" + "/*pmb.toFormalizedDate:<b>toDate(option)</b>*/'...'<br>" + "<br>"
                + "-- !df:pmb!<br>" + "-- !!Date fromFormalizedDate:fromDate(option)!!<br>"
                + "-- !!Date toFormalizedDate:<b>toDate(option)</b>!!<br>" + "  ...<br>" + " where FORMALIZED_DATETIME &gt;=<br>"
                + "       /*pmb.fromFormalizedDate*/'...'<br>" + "   and FORMALIZED_DATETIME &lt;<br>"
                + "       /*pmb.toFormalizedDate*/'...'<br>" + "<br>" + "e.g. @Java<br>"
                + "Date fromDate = toDate(\"2009-10-03 12:34:56\")<br>" + "Date toDate = toDate(\"2009-12-14 12:34:56\")<br>"
                + "pmb.setFromFormalizedDate_FromDate(fromDate,<br>" + "                          op -&gt; op..compareAsMonth());<br>"
                + "pmb.setToFormalizedDate_ToDate(toDate,<br>" + "                          op -&gt; op..compareAsMonth());<br>" + "<br>"
                + "e.g. @DisplaySQL<br>" + " where FORMALIZED_DATETIME &gt;= '2009-10-01 00:00:00'<br>"
                + "   and FORMALIZED_DATETIME &lt; '2010-01-01 00:00:00'";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }
    }

    // -----------------------------------------------------
    //                                    Like Search Option
    //                                    ------------------
    protected static class LikeSearchProposal extends OutsideSqlProposal {
        public LikeSearchProposal() {
            super("like");
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "...<br>" + "where<br>"
                + " MEMBER_NAME like /*pmb.memberName*/'<i>%f%oo%</i>'<br>" + "<br>" + "e.g @OutsideSql<br>" + "-- !df:pmb!<br>"
                + "-- !!String memberName:<b>like<b>!!<br>" + "...<br>" + "where<br>" + " MEMBER_NAME like /*pmb.memberName*/'foo'<br>"
                + "<br>" + "e.g. @Java<br>" + "pmb.setMemberName_LikeSearch(\"S\",op -&gt; op.likeContain());";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }
    }

    protected static class LikePrefixSearchProposal extends OutsideSqlProposal {
        public LikePrefixSearchProposal() {
            super("likePrefix");
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "...<br>" + "where<br>"
                + " MEMBER_NAME like /*pmb.memberName*/'<i>foo%</i>'<br>" + "<br>" + "e.g @OutsideSql<br>" + "-- !df:pmb!<br>"
                + "-- !!String memberName:<b>likePrefix<b>!!<br>" + "...<br>" + "where<br>"
                + " MEMBER_NAME like /*pmb.memberName*/'foo'<br>" + "<br>" + "e.g. @Java<br>" + "pmb.setMemberName_PrefixSearch(\"S\");";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }
    }

    protected static class LikeContainSearchProposal extends OutsideSqlProposal {
        public LikeContainSearchProposal() {
            super("likeContain");
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "...<br>" + "where<br>"
                + " MEMBER_NAME like /*pmb.memberName*/'<i>%foo%</i>'<br>" + "<br>" + "e.g @OutsideSql<br>" + "-- !df:pmb!<br>"
                + "-- !!String memberName:<b>likeContain<b>!!<br>" + "...<br>" + "where<br>"
                + " MEMBER_NAME like /*pmb.memberName*/'foo'<br>" + "<br>" + "e.g. @Java<br>" + "pmb.setMemberName_ContainSearch(\"S\");";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }
    }

    protected static class LikeSuffixSearchProposal extends OutsideSqlProposal {
        public LikeSuffixSearchProposal() {
            super("likeSuffix");
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "...<br>" + "where<br>"
                + " MEMBER_NAME like /*pmb.memberName*/'<i>%foo</i>'<br>" + "<br>" + "e.g @OutsideSql<br>" + "-- !df:pmb!<br>"
                + "-- !!String memberName:<b>likeSuffix<b>!!<br>" + "  ...<br>" + " where<br>"
                + " MEMBER_NAME like /*pmb.memberName*/'foo'<br>" + "<br>" + "e.g. @Java<br>" + "pmb.setMemberName_SuffixSearch(\"S\");";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }
    }

    protected static class NoLikeSearchProposal extends OutsideSqlProposal {
        public NoLikeSearchProposal() {
            super("noLike");
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>"
                + "-- !!List<$$Domain$$.Member> memberList:likeContain!!<br>" + "select ...<br>" + "  from MEMBER<br>" + "/*BEGIN*/<br>"
                + " where<br>" + "  /*IF pmb.member.memberId != null*/<br>" + "  member.MEMBER_ID = /*pmb.member.memberId*/3<br>"
                + "  /*END*/<br>" + "  /*FOR pmb.memberList*//*FIRST*/and (/*END*/<br>" + "    /*NEXT 'or '*/member.MEMBER_NAME like<br>"
                + "         /*#current.memberName*/'%S%'<br>" + "    or member.MEMBER_ACCOUNT =<br>"
                + "         /*#current.memberAccount:<b>notLike</b>*/'Pixy'<br>" + "  /*LAST*/)/*END*//*END*/<br>" + "/*END*/";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }
    }

    // -----------------------------------------------------
    //                                 Classification Option
    //                                 ---------------------
    protected static class ClassificationProposal extends OutsideSqlProposal {
        public ClassificationProposal() {
            super("cls()");
        }

        @Override
        public int getCursorPositionOffset() {
            return 4; // /*pmb.fieldName:cls([cursol position])*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "...<br>"
                + "where PAYMENT_COMPLETE_FLG =<br>" + "  /*pmb.paymentComplete:<b>cls</b>(<i>Flg</i>)*/0<br>" + "<br>" + "e.g. @Java<br>"
                + "pmb.setPaymentCompleteTrue_True();<br>" + "<br>" + "e.g @OutsideSql (Fixed Classification)<br>" + "-- !df:pmb!<br>"
                + "-- !!String paymentCompleteTrue:<b>cls</b>(<i>Flg.True</i>)!!<br>" + "...<br>"
                + "where PAYMENT_COMPLETE_FLG = /*pmb.paymentCompleteTrue*/0";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "cls([classification-name])";
        }
    }

    // -----------------------------------------------------
    //                               Column Reference Option
    //                               -----------------------
    protected static class ColumnReferenceProposal extends OutsideSqlProposal {
        public ColumnReferenceProposal() {
            super("ref()");
        }

        @Override
        public int getCursorPositionOffset() {
            return 4; // /*pmb.fieldName:ref([cursol position])*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "...<br>"
                + "where MEMBER_NAME like<br>" + "/*pmb.name:<b>ref</b>(<i>MEMBER.MEMBER_NAME</i>)*/'S%'<br>" + "<br>"
                + "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!String name:<b>ref</b>(<i>MEMBER.MEMBER_NAME</i>)!!<br>" + "...<br>"
                + "where MEMBER_NAME like /*pmb.name*/'S%'<br>" + "<br>" + "e.g. @Java<br>" + "pmb.setStatusCode_Formalized();";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "ref([table-name].[column-name])";
        }
    }

    // -----------------------------------------------------
    //                                        Comment Option
    //                                        --------------
    protected static class CommentProposal extends OutsideSqlProposal {
        public CommentProposal() {
            super("comment()");
        }

        @Override
        public int getCursorPositionOffset() {
            return 8; // /*pmb.fieldName:comment([cursol position])*/
        }

        private static final String INFO = "e.g @OutsideSql<br>" + "-- !df:pmb!<br>" + "-- !!AutoDetect!!<br>" + "...<br>" + "where<br>"
                + "MEMBER_ID = <br>/*pmb.memberId:<b>comment</b>(<i>ここにコメントを書く</i>)*/3<br>" + "<br>" + "e.g @OutsideSql<br>"
                + "-- !df:pmb!<br>" + "-- !!String memberId!!   // <i>ここにコメントを書く</i><br>" + "...<br>" + "where<br>" + "MEMBER_ID = <br>"
                + " /*pmb.memberId:<b>comment</b>(<i>ここにコメントを書く</i>)*/3";

        @Override
        public String getAdditionalProposalInfo() {
            return INFO;
        }

        @Override
        public String getDisplayString() {
            return "comment([comment])";
        }
    }

}
