package com.sscf.investment.detail;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.sscf.investment.detail.fragment.*;
import com.sscf.investment.teacherYan.TalkFreeFragment;
import com.sscf.investment.teacherYan.TeacherYanArticleFragment;
import com.sscf.investment.teacherYan.TeacherYanCourseFragment;
import com.sscf.investment.teacherYan.TeacherYanWordFragment;

/**
 * Created by liqf on 2015/7/15.
 */
public class FragmentFactory {
    public static final String NEWS = "news";
    public static final String NOTICE = "notice";
    public static final String REPORT = "report";
    public static final String CAPITAL = "capital";
    public static final String DIAGNOSIS = "diagnosis";
    public static final String INTERACTION = "interaction";
    public static final String FINANCE = "finance";
    public static final String INTRO = "intro";
    public static final String CONSULTANT = "consultant";
    public static final String RELATED_STOCK_LIST = "related_stock_list";
    public static final String RANK_UP = "rank_up"; //涨幅榜
    public static final String RANK_DOWN = "rank_down"; //跌幅榜
    public static final String RANK_TURNOVER_RATE = "rank_turnover_rate"; //换手率榜
    public static final String HONGKONG_INTRO = "hongkong_intro";
    public static final String HONGKONG_FINANCE = "hongkong_finance";
    public static final String HONGKONG_SHAREHOLDER = "hongkong_shareholder";
    public static final String USA_INTRO = "usa_intro";
    public static final String USA_FINANCE = "usa_finance";
    public static final String FUND_INTRO = "fund_intro";
    public static final String NEW_THIRD_BOARD_INTRO = "new_third_board_intro";
    public static final String DENGTA_A_UP_AND_DOWN = "dengta_a_up_and_down";
    public static final String TEACHER_YAN_WORD = "teacher_yan_say";
    public static final String TEACHER_YAN_TALKFREE = "teacher_yan_talk_free";
    public static final String TEACHER_YAN_CURSE = "teacher_yan_curse";
    public static final String TEACHER_YAN_ARTICLE = "teacher_yan_article";

    public static Fragment createFragment(final String tag) {
        Fragment fragment = null;
        if (TextUtils.equals(tag, NEWS)) {
            fragment = new NewsFragment();
        } else if (TextUtils.equals(tag, NOTICE)){
            fragment = new NewsFragment();
        } else if (TextUtils.equals(tag, REPORT)) {
            fragment = new NewsFragment();
        } else if (TextUtils.equals(tag, CAPITAL)) {
            fragment = new CapitalFragment();
        } else if (TextUtils.equals(tag, DIAGNOSIS)) {
            fragment = new DiagnosisFragment();
        } else if (TextUtils.equals(tag, INTERACTION)) {
            fragment = new InteractionFragment();
        } else if (TextUtils.equals(tag, FINANCE)) {
            fragment = new FinanceFragment();
        } else if (TextUtils.equals(tag, INTRO)) {
            fragment = new IntroFragment();
        } else if (TextUtils.equals(tag, HONGKONG_INTRO)) {
            fragment = new HongKongIntroFragment();
        } else if (TextUtils.equals(tag, HONGKONG_FINANCE)) {
            fragment = new HongKongFinanceFragment();
        } else if (TextUtils.equals(tag, HONGKONG_SHAREHOLDER)) {
            fragment = new HongKongShareholderFragment();
        } else if (TextUtils.equals(tag, USA_INTRO)) {
            fragment = new USAIntroFragment();
        } else if (TextUtils.equals(tag, USA_FINANCE)) {
            fragment = new USAFinanceFragment();
        } else if (TextUtils.equals(tag, FUND_INTRO)) {
            fragment = new FundIntroFragment();
        } else if (TextUtils.equals(tag, NEW_THIRD_BOARD_INTRO)) {
            fragment = new NewThirdBoardIntroFragment();
        } else if (TextUtils.equals(tag, DENGTA_A_UP_AND_DOWN)) {
            fragment = new DengtaAUpAndDownFragment();
        } else if (TextUtils.equals(tag, RELATED_STOCK_LIST)) {
            fragment = new RelatedStockListFragment();
        } else if (TextUtils.equals(tag, CONSULTANT)) {
            fragment = new ConsultantFragment();
        } else if (TextUtils.equals(tag, RANK_UP) || TextUtils.equals(tag, RANK_DOWN) || TextUtils.equals(tag, RANK_TURNOVER_RATE)) {
            fragment = new RankFragment();
        } else if (TextUtils.equals(tag, TEACHER_YAN_WORD)) {
            fragment = new TeacherYanWordFragment();
        } else if (TextUtils.equals(tag, TEACHER_YAN_TALKFREE)) {
            fragment = new TalkFreeFragment();
        } else if (TextUtils.equals(tag, TEACHER_YAN_CURSE)) {
            fragment = new TeacherYanCourseFragment();
        } else if (TextUtils.equals(tag, TEACHER_YAN_ARTICLE)) {
            fragment = new TeacherYanArticleFragment();
        }

        return fragment;
    }
}
