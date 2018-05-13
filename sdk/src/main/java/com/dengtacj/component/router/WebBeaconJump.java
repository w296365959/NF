package com.dengtacj.component.router;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.chenenyu.router.IRouter;
import com.chenenyu.router.Router;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.db.FavorItem;
import com.dengtacj.component.managers.IAccountManager;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.web.CommonWebConst;
import BEC.AccuPointPriviType;
import BEC.DtLiveType;
import BEC.E_NEWS_TYPE;

/**
 * Created by davidwei on 2017/09/01
 */
public final class WebBeaconJump {

    public static void showFullScreenWebActivity(final Context context, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDR, url);
            Router.build("FullScreenWebActivity").with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTeacherYanArticleActivity(final Context context, final String url, String title, String articleId) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDR, url);
            bundle.putString(CommonWebConst.KEY_SHARE_TITLE, title);
            bundle.putString(CommonWebConst.KEY_ARTICLE_ID, articleId);
            Router.build("TeacherYanArticleWebActivity").with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCommonWebActivity(final Context context, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //http://47.96.141.250:55553/findDetail.html?id=99999_337
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDR, url);
            Router.build("CommonWebActivity").with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showContentWebActivityForResult(final Context context, final int requestCode, final String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.CONTENT, content);
            Router.build("ContentWebActivity").requestCode(requestCode).with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCommonWebActivityForResult(final Context context, final int requestCode, final String url) {
        showCommonWebActivityForResult(context, requestCode, true, url);
    }
    public static void showCommonWebActivityForResult(final Context context, final int requestCode, boolean supportTheme, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDR, url);
            bundle.putBoolean(CommonWebConst.SUPPORT_THEME, supportTheme);
            Router.build("CommonWebActivity").requestCode(requestCode).with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCommonUntransparentWebActivity(final Context context, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        try {
            Bundle bundle = new Bundle();
            bundle.putString(CommonWebConst.URL_ADDR, url);
            Router.build("CommonUnTransparentWebActivity").with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showIntelligentAnswer(final Context context, final boolean state) {
        showIntelligentAnswer(context, state, null, null);
    }

    public static void showIntelligentAnswer(final Context context, final String dtCode, final String secName) {
        showIntelligentAnswer(context, false, dtCode, secName);
    }

    public static void showIntelligentAnswer(final Context context, final boolean state, final String dtCode, final String secName) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebConst.URL_ADDR, WebUrlManager.getInstance().getIntelligentAnswerUrl(state));
        bundle.putString(CommonConst.KEY_SEC_CODE, dtCode);
        bundle.putString(CommonConst.KEY_SEC_NAME, secName);
        CommonBeaconJump.showActivity(context, "IntelligentAnswerWebActivity", bundle);
    }

    public static void showCaptureFaqActivity(final Context context) {
        showCommonWebActivity(context, WebUrlManager.getInstance().getQrCodeFaqUrl());
    }

    public static void showCommentDetail(final Context context, final String feedId) {
        final String url = WebUrlManager.getInstance().getCommentDetailUrl(feedId);
        showCommonWebActivity(context, url);
    }

    public static void showTeacherYanArticleDetail(final Context context, final String articleID, String title) {
        final String url = WebUrlManager.getInstance().getTeacherYanActicleDetailUrl(articleID);
        showTeacherYanArticleActivity(context, url, title, articleID);
    }

    public static void showStockLive(final Context context, final String secCode, final String secName) {
        final String url = WebUrlManager.getInstance().getSecLiveUrl(secCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showStockPortrait(final Context context, final String secCode, final String secName) {
        showWebActivity(context, WebUrlManager.getInstance().getStockPortraitUrl(secCode, secName),
                CommonWebConst.WT_PORTRAIT, CommonWebConst.SWIPE_BACK_TYPE_FROM_LEFT_EDGE);
    }

    public static void showStockBigEventRemind(final Context context, final String secCode, final String secName) {
        final String url = WebUrlManager.getInstance().getBigEventUrl(secCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showSimilarKLine(final Context context) {
        final String url = WebUrlManager.getInstance().getCommonIntroUrl(AccuPointPriviType.E_ACCU_POINT_PRIVI_KLINE);
        showCommonWebActivity(context, url);
    }

    public static void showSimilarKLine(final Context context, final String secCode, final String secName) {
        final String url = WebUrlManager.getInstance().getSimilarKUrl(secCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showMockTrend(final Context context, final String secCode, final String secName) {
        final String url = WebUrlManager.getInstance().getMockTrend(secCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showSecHistory(final Context context) {
        final String url = WebUrlManager.getInstance().getCommonIntroUrl(AccuPointPriviType.E_ACCU_POINT_PRIVI_HISTORY);
        showCommonWebActivity(context, url);
    }

    public static void showSecHistory(final Context context, final String secCode, final String secName) {
        final String url = WebUrlManager.getInstance().getSecHistoryUrl(secCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showCYQ(final Context context, final String secCode, final String secName) {
        final String url = WebUrlManager.getInstance().getSecChipUrl(secCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showCYQ(final Context context) {
        final String url = WebUrlManager.getInstance().getCommonIntroUrl(AccuPointPriviType.E_ACCU_POINT_PRIVI_CHIP);
        showCommonWebActivity(context, url);
    }

    public static void showCYQ(final Context context, final String secCode, final String secName, final int defaultTabIndex) {
        final String url = WebUrlManager.getInstance().getSecChipUrl(secCode, secName, defaultTabIndex);
        showCommonWebActivity(context, url);
    }

    public static void showPrivatizationTrackingDetail(final Context context, final String secCode, final String secName) {
        final String url = WebUrlManager.getInstance().getPrivatizationTrackingDetailUrl(secCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showDirectionAddDetail(final Context context, final String secCode, final String secName) {
        final String url =  WebUrlManager.getInstance().getDirectionAddDetailUrl(secCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showSuspensionDetail(final Context context, final String secCode, final String secName) {
        final String url =  WebUrlManager.getInstance().getSuspensionDetailUrl(secCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showAhPremiumDetail(final Context context, final String aDtCode, final String aName, final String hDtCode, final String hName) {
        final String url = WebUrlManager.getInstance().getAhPremium(aDtCode, aName, hDtCode, hName);
        showCommonWebActivity(context, url);
    }

    public static void showMarketWarning(final Context context) {
        final String url = WebUrlManager.getInstance().getMarketWarningUrl();
        showCommonWebActivity(context, url);
    }

    public static void showPlateList(final Context context, final String ranktype) {
        final String url = WebUrlManager.getInstance().getPlateListUrl(ranktype);
        showCommonWebActivity(context, url);
    }

    public static void showIndustryPlateList(final Context context) {
        final String url = WebUrlManager.getInstance().getIndustryPlateListUrl();
        showCommonWebActivity(context, url);
    }

    public static void showConceptPlateList(final Context context) {
        final String url = WebUrlManager.getInstance().getConceptPlateListUrl();
        showCommonWebActivity(context, url);
    }

    public static void showNewShare(final Context context) {
        final String url = WebUrlManager.getInstance().getNewStockUrl();
        showCommonWebActivity(context, url);
    }

    public static void showDragonTigerBillboard(final Context context) {
        final String url = WebUrlManager.getInstance().getDragonTigerBillboardUrl();
        showCommonWebActivity(context, url);
    }

    public static void showPrivatizationTrackingList(final Context context) {
        final String url = WebUrlManager.getInstance().getPrivatizationTrackingListUrl();
        showCommonWebActivity(context, url);
    }

    public static void showDirectionAddListList(final Context context) {
        final String url = WebUrlManager.getInstance().getDirectionAddListUrl();
        showCommonWebActivity(context, url);
    }

    public static void showActivities(final Context context) {
        final String url = WebUrlManager.getInstance().getActivitiesUrl();
        showCommonWebActivity(context, url);
    }

    public static void showPrivilege(final Context context) {
        final String url = WebUrlManager.getInstance().getPrivilegeUrl();
        showCommonWebActivity(context, url);
    }

    public static void showValueAddedService(final Context context) {
        final String url = WebUrlManager.getInstance().getValueAddedServiceUrl();
        showCommonWebActivity(context, url);
    }

    public static void showPortfolioDailyReport(final Context context) {
        final String url = WebUrlManager.getInstance().getPortfolioDailyReportUrl();
        showCommonWebActivity(context, url);
    }

    public static void showInvestmentAdviserDetail(final Context context, final String feedId) {
        final String url = WebUrlManager.getInstance().getOpinionDetailUrl(feedId);
        showCommonWebActivity(context, url);
    }

    public static void showCustomizedStrategy(final Context context) {
        final String url = WebUrlManager.getInstance().getCustomizedStrategyUrl();
        showCommonWebActivity(context, url);
    }

    public static void showPortfolioStrategy(final Context context) {
        final String url = WebUrlManager.getInstance().getPortfolioStrategyUrl();
        showCommonWebActivity(context, url);
    }

    public static void showInviteFriends(final Context context) {
        final String url = WebUrlManager.getInstance().getInviteFriendsUrl();
        showCommonWebActivity(context, url);
    }

    public static void showBS(final Context context) {
        final String url = WebUrlManager.getInstance().getCommonIntroUrl(AccuPointPriviType.E_ACCU_POINT_PRIVI_BSSIGNAL);
        showCommonWebActivity(context, url);
    }

    public static void showBS(final Context context, String dtSecCode, String secName) {
        final String url = WebUrlManager.getInstance().getBSUrl(dtSecCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showIntelligentDiagnosisDetail(final Context context, String dtSecCode, String secName) {
        showIntelligentDiagnosisDetail(context, dtSecCode, secName, 0);
    }

    public static void showIntelligentDiagnosisDetail(final Context context, String dtSecCode, String secName, int tabIndex) {
        final String url = WebUrlManager.getInstance().
                getIntelligentDiagnosisDetailUrl(dtSecCode, secName, tabIndex);
        showCommonWebActivity(context, url);
    }

    public static void showIntelligentDiagnosisSearch(final Context context) {
        final String url = WebUrlManager.getInstance().getIntelligentDiagnosisSearchUrl();
        showCommonWebActivity(context, url);
    }

    public static void showIntelligentAnswerSchool(final Context context) {
        final String url = WebUrlManager.getInstance().getIntelligentAnswerSchoolUrl();
        showCommonWebActivity(context, url);
    }

    public static void showIntelligentAnswerHelp(final Context context) {
        final String url = WebUrlManager.getInstance().getIntelligentAnswerHelpUrl();
        showCommonWebActivity(context, url);
    }

    public static void showMarginTracking(final Context context, String dtSecCode, String secName) {
        final String url = WebUrlManager.getInstance().getMarginTrackingUrl(dtSecCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showMarginTracking(final Context context) {
        final String url = WebUrlManager.getInstance().getCommonIntroUrl(AccuPointPriviType.E_ACCU_POINT_FINANCE_TRACK);
        showCommonWebActivity(context, url);
    }

    public static void showMarginTracking(final Context context, final String dtSecCode, String secName, final int defaultTabIndex) {
        final String url = WebUrlManager.getInstance().getMarginTrackingUrl(dtSecCode, secName, defaultTabIndex);
        showCommonWebActivity(context, url);
    }

    public static void showMarketMargin(final Context context) {
        final String url = WebUrlManager.getInstance().getMarketMarginUrl();
        showCommonWebActivity(context, url);
    }

    public static void showIndustrialChain(final Context context, final String dtSecCode, final String secName) {
        final String url = WebUrlManager.getInstance().getIndustrialChainUrl(dtSecCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showDirectAddBreak(final Context context, final String id, final String dtSecCode, final String secName) {
        final String url = WebUrlManager.getInstance().getDirectAddBreak(id, dtSecCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showDragonTigerBillboardStockDetail(final Context context, String dtSecCode, String secName, String date) {
        final String url = WebUrlManager.getInstance().getDragonTigerBillboardStockDetailUrl(dtSecCode, secName, date);
        showCommonWebActivity(context, url);
    }

    public static void showDragonTigerBillboardSaleDepartDetail(final Context context, String saleDepartName, String date) {
        final String url = WebUrlManager.getInstance().getDragonTigerBillboardSaleDepartDetailUrl(saleDepartName, date);
        showCommonWebActivity(context, url);
    }

    public static void showLiveMsgFAQ(final Context context) {
        final String url = WebUrlManager.getInstance().getLiveFaqUrl();
        showCommonWebActivity(context, url);
    }

    public static void showNewBigEvent(final Context context, String dtSecCode, String secName) {
        final String url = WebUrlManager.getInstance().getBigEventUrl(dtSecCode, secName);
        showCommonWebActivity(context, url);
    }

    public static void showWebActivity(final Context context, final String url, final int type) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebConst.URL_ADDR, url);
        bundle.putInt(CommonWebConst.WEB_TYPE, type);
        CommonBeaconJump.showActivity(context, "WebActivity", bundle);
    }

    public static void showWebActivity(final Context context, final String url, final int type, final int swipeBackType) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommonWebConst.URL_ADDR, url);
        bundle.putInt(CommonWebConst.WEB_TYPE, type);
        bundle.putInt(CommonWebConst.SWIPE_BACK_TYPE, swipeBackType);
        CommonBeaconJump.showActivity(context, "WebActivity", bundle);
    }

    public static void showWebActivity(final Context context, final String url, final FavorItem favorItem) {
        showWebActivity(context, url, favorItem, 0);
    }

    public static void showWebActivity(final Context context, final String url, final FavorItem favorItem, final int flags) {
        if (favorItem == null) {
            return;
        }

        final Bundle bundle = new Bundle();
        bundle.putString(CommonWebConst.URL_ADDR, url);
        bundle.putSerializable(CommonWebConst.EXTRA_NEWS, favorItem);

        switch (favorItem.getFavorType()) {
            case E_NEWS_TYPE.NT_NEWS:
                bundle.putInt(CommonWebConst.WEB_TYPE, CommonWebConst.WT_NEWS);
                break;
            case E_NEWS_TYPE.NT_REPORT:
                bundle.putInt(CommonWebConst.WEB_TYPE, CommonWebConst.WT_REPORT);
                break;
            case E_NEWS_TYPE.NT_ANNOUNCEMENT:
                bundle.putInt(CommonWebConst.WEB_TYPE, CommonWebConst.WT_ANNONCEMENT);
                break;
            case E_NEWS_TYPE.NT_DISC_NEWS:
                bundle.putInt(CommonWebConst.WEB_TYPE, CommonWebConst.WT_DISCOVERY_MARKET);
                break;
            case E_NEWS_TYPE.NT_NEWS_TEACHER_YAN:
                bundle.putInt(CommonWebConst.WEB_TYPE, CommonWebConst.WT_TEACHER_YAN);
                break;
        }

        try {
            bundle.putString(CommonWebConst.URL_ADDR, url);
            IRouter router = Router.build("WebActivity");
            if (flags > 0) {
                router.addFlags(flags);
            }
            router.with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showPickStockFAQ(final Context context) {
        final String url = WebUrlManager.getInstance().getPickStockFAQUrl();
        showCommonWebActivity(context, url);
    }

    public static void showFeedback(final Context context) {
        showWebActivity(context, CommonWebConst.URL_FEEDBACK, CommonWebConst.WT_FEEDBACK);
    }

    public static void showDtLive(final Context context) {
        showDtLive(context, DtLiveType.E_LIVE_MARKET);
    }

    public static void showDtLive(final Context context, int type) {
        final Bundle bundle = new Bundle();
        bundle.putInt(CommonWebConst.KEY_LIVE_TYPE, type);
        CommonBeaconJump.showActivity(context, "LiveMsgActivity", bundle);
    }

    public static void showPortfolioLive(final Context context) {
        showDtLive(context, DtLiveType.E_LIVE_PORTFOLIO);
    }

    public static void showImportGallery(final Context context) {
        try {
            Bundle bundle = new Bundle();
            Router.build("ImportGalleryActivity").with(bundle).go(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showRiskEval(final Context context, int type) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        if (accountManager.isLogined()) {
            final String url = WebUrlManager.getInstance().getRiskEvalUrl(type);
            showCommonWebActivity(context, url);
        } else {
            CommonBeaconJump.showLogin(context);
        }
    }

    public static void showRiskEval(final Context context) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager == null) {
            return;
        }
        if (accountManager.isLogined()) {
            final String url = WebUrlManager.getInstance().getRiskEvalUrl();
            WebBeaconJump.showCommonWebActivity(context, url);
        } else {
            CommonBeaconJump.showLogin(context);
        }
    }

    public static void showThirdPartyNews(final Context context, final String url, final FavorItem favorItem) {
        final Bundle bundle = new Bundle();
        bundle.putString(CommonWebConst.URL_ADDR, url);
        bundle.putSerializable(CommonWebConst.EXTRA_NEWS, favorItem);
        CommonBeaconJump.showActivity(context, "ThirdPartyNewsWebActivity", bundle);
    }

    public static void showMockTrendRank(final Context context) {
        final String url = WebUrlManager.getInstance().getMockTrendRankUrl();
        WebBeaconJump.showCommonWebActivity(context, url);
    }

    public static void showHotStockRank(final Context context) {
        final String url = WebUrlManager.getInstance().getHotStockRankUrl();
        WebBeaconJump.showCommonWebActivity(context, url);
    }

    public static void showBSSignalRank(final Context context) {
        final String url = WebUrlManager.getInstance().getBSSignalRankUrl();
        WebBeaconJump.showCommonWebActivity(context, url);
    }

    public static void showUpstreamProduct(final Context context, final String dtSecCode, final String secName) {
        final String url = WebUrlManager.getInstance().getUpstreamProductUrl(dtSecCode, secName);
        WebBeaconJump.showCommonWebActivity(context, url);
    }

    public static void showDownstreamProduct(final Context context, final String dtSecCode, final String secName) {
        final String url = WebUrlManager.getInstance().getDownstreamProductUrl(dtSecCode, secName);
        WebBeaconJump.showCommonWebActivity(context, url);
    }

    public static void showMarketChangeIndex(final Context context) {
        final String url = WebUrlManager.getInstance().getMarketChangeIndexUrl();
        WebBeaconJump.showCommonWebActivity(context, url);
    }

    public static void showMarketChangeStat(final Context context, final int index) {
        final String url = WebUrlManager.getInstance().getMarketChangeStateUrl(index);
        WebBeaconJump.showCommonWebActivity(context, url);
    }
}
