package com.sscf.investment.sdk.main.manager;

import android.content.Context;
import com.sscf.investment.web.CommonWebConst;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import BEC.E_SHARE_TYPE;

/**
 * Created by davidwei on 2015/9/2.
 *
 * 管理各种url
 */
public final class WebUrlManager extends BaseUrlManager {

    public static final String TAG = WebUrlManager.class.getSimpleName();

    private static final String FROM_PARAMS = "dt_from=app";
    private static final String SEC_PARAMS = "secCode=%s&secName=%s&dt_from=app";
    private static final String SEC_SBT_PARAMS = "secCode=%s&secName=%s&dt_from=app&dt_sbt=%s";
    private static final String SHARE_PARAMS = "secCode=%s&secName=%s&dt_from=web";
    private static final String PLATE_LIST_PARAMS = "dt_from=app&webviewType=searchRefreshType&ranktype=%s";
    private static final String PLATE_CAPATAL_FLOW_LIST_PARAMS = "dt_from=app&webviewType=userActivitesType&ranktype=%s&flow=%s";
    private static final String OPEN_PRIVILEGE_PARAMS = "dt_from=app&introIndex=%s";
    private static final String SMART_DISGNOSIS_DETAIL_PARAMS = "seccode=%s&secname=%s&tabIndex=%s&dt_from=app&webviewType=userActivitesType&dt_page_type=11";
    private static final String SIMILAR_SHAPE_PARAMS = "seccode=%s&secname=%s&totalStartDate=%s&totalEndDate=%s&selectStartDate=%s&selectEndDate=%s&dt_from=app&webviewType=userActivitesType&dt_page_type=11";
    private static final String USE_COUPONS_PARAMS = "dt_from=app&type=%s&value=%s&number=%s&unit=%s&extra=%s&couponCode=%s";

    private static WebUrlManager instance;

    private WebUrlManager() {
    }

    public static WebUrlManager getInstance() {
        if (instance == null) {
            instance = new WebUrlManager();
        }
        return instance;
    }

    @Override
    protected Map<Integer, String> getDefaultUrls() {
        final HashMap<Integer, String> urls = new HashMap<Integer, String>(100);
        urls.put(E_SHARE_TYPE.E_STOCKDETAIL_ICON, "http://sec.gushi.com:55553/weixin/shareIcon.jpg");
        urls.put(E_SHARE_TYPE.E_STOCK_DETAIL, "https://sec.gushi.com/stockDetail.html");
        urls.put(E_SHARE_TYPE.E_STOCK_PORTRAIT, "https://sec.gushi.com/stockPortrait.html");
        urls.put(E_SHARE_TYPE.E_CONC_DETAIL, "https://sec.gushi.com/stockDetail.html");
        urls.put(E_SHARE_TYPE.E_INDEX_DETAIL, "https://sec.gushi.com/stockDetail.html");
        urls.put(E_SHARE_TYPE.E_FUND_DETAIL, "https://sec.gushi.com/stockDetail.html");
        urls.put(E_SHARE_TYPE.E_FUTURES_DETAIL, "https://sec.gushi.com/stockDetail.html");
        urls.put(E_SHARE_TYPE.E_BIG_EVENT_REMINDER, "https://sec.gushi.com/eventReminder.html");
        urls.put(E_SHARE_TYPE.E_SIMILAR_K_LINE, "https://sec.gushi.com/similarK.html");
        urls.put(E_SHARE_TYPE.E_STRATEGY_LIST, "https://sec.gushi.com/strategyList.html");
        urls.put(E_SHARE_TYPE.E_ACTIVITY_LIST, "https://sec.gushi.com/activityList.html");
        urls.put(E_SHARE_TYPE.E_SEC_HISTORY, "https://sec.gushi.com/historyCheck.html");
        urls.put(E_SHARE_TYPE.E_DT_CHIP_DISTRIBUTION, "https://sec.gushi.com/chipDistribution.html");
        urls.put(E_SHARE_TYPE.E_SEC_LIVE, "https://sec.gushi.com/stockLive.html");
        urls.put(E_SHARE_TYPE.E_NEW_STOCK, "https://sec.gushi.com/newStockCenter.html");
        urls.put(E_SHARE_TYPE.E_LHB_STOCK, "https://sec.gushi.com/distinctionStockList.html");
        urls.put(E_SHARE_TYPE.E_URL_USER_AGREEMENT, "https://sec.gushi.com/userAgreement.html");
        urls.put(E_SHARE_TYPE.E_SMART_PICK_FAQ, "https://sec.gushi.com/selectEvaluateExp.html");
        urls.put(E_SHARE_TYPE.E_MARKET_WARNING, "https://sec.gushi.com/marketWarning.html");
        urls.put(E_SHARE_TYPE.E_DT_PORTFOLIO_LIVE, "https://sec.gushi.com/dtLive.html?type=stock");
        urls.put(E_SHARE_TYPE.E_DT_MARKET_LIVE, "https://sec.gushi.com/dtLive.html?type=market");
        urls.put(E_SHARE_TYPE.E_DT_LIVE_FAQ, "https://sec.gushi.com/faqDtLive.html");
        urls.put(E_SHARE_TYPE.E_DT_PRIVATE_TRACKING, "https://sec.gushi.com/privatizationTracking.html");
        urls.put(E_SHARE_TYPE.E_DT_PRIVATE_DETAIL, "https://sec.gushi.com/privatizationTrackingDetail.html");
        urls.put(E_SHARE_TYPE.E_SUSPENTION_DETAIL, "https://sec.gushi.com/suspentionDetail.html");
        urls.put(E_SHARE_TYPE.E_DIRECTION_ADD, "https://sec.gushi.com/directionalAddIssuance.html");
        urls.put(E_SHARE_TYPE.E_DIRECTION_DETAIL, "https://sec.gushi.com/directionalAddIssuanceDetail.html");
        urls.put(E_SHARE_TYPE.E_INTELLIGENT_ANSWER, "https://sec.gushi.com/InvestAdvise/intelligentAnswer.html");
        urls.put(E_SHARE_TYPE.E_AH_PREMIUM, "https://sec.gushi.com/ahPremium.html");
        urls.put(E_SHARE_TYPE.E_QRCODE_FAQ, "https://sec.gushi.com/faqQrCode.html");
        urls.put(E_SHARE_TYPE.E_QRCODE_URL, "https://sec.gushi.com/yun/Error.html");
        urls.put(E_SHARE_TYPE.E_PLATE_LIST_PAGE, "https://sec.gushi.com/plateRankList.html");
        urls.put(E_SHARE_TYPE.E_PLATE_FLOW_PAGE, "https://sec.gushi.com/plateFlowList.html");
        urls.put(E_SHARE_TYPE.E_PLATE_FLOW_PAGE, "https://sec.gushi.com/plateFlowList.html");
        urls.put(E_SHARE_TYPE.E_SELF_DAILY_REPORT, "https://sec.gushi.com/selfChooseDailyReport.html");
        urls.put(E_SHARE_TYPE.E_FEED_COMMENT_DETAIL, "https://sec.gushi.com/commentDetail.html");
        urls.put(E_SHARE_TYPE.E_FEED_OPINION_DETAIL, "https://sec.gushi.com/opinionDetail.html");
        urls.put(E_SHARE_TYPE.E_SMART_PICKING_CUSTOM, "https://sec.gushi.com/InvestAdvise/selectStockCondition.html");
        urls.put(E_SHARE_TYPE.E_SMART_PICKING_PORTFOLIO, "https://sec.gushi.com/optionalStrategy.html");
        urls.put(E_SHARE_TYPE.E_AH_PREMIUM_LIST, "https://sec.gushi.com/rankCommonList.html?dt_from=web&ranktype=AHStock");
        urls.put(E_SHARE_TYPE.E_AH_PREMIUM_FAQ, "https://sec.gushi.com/faqAhPremium.html");
        urls.put(E_SHARE_TYPE.E_OPEN_ACCOUNT, "https://sec.gushi.com/aShareOpenAccount.html");
        urls.put(E_SHARE_TYPE.E_DT_SINGLE_PRIVI_DETAIL, "https://sec.gushi.com/dtPrivilegeChipDistribution.html");
        urls.put(E_SHARE_TYPE.E_ACCU_POINT_INVITE_FRIEND, "https://sec.gushi.com/inviteFellow.html");
        urls.put(E_SHARE_TYPE.E_ACCU_POINT_FAQ, "https://sec.gushi.com/faqAccumulatePoints.html");
        urls.put(E_SHARE_TYPE.E_VIDEO_DETAIL, "https://sec.gushi.com/yiDetail.html");
        urls.put(E_SHARE_TYPE.E_VIDEO_LIST, "https://sec.gushi.com/yiList.html");
        urls.put(E_SHARE_TYPE.E_DT_PRIVI_DETAIL_NEW, "https://sec.gushi.com/dtPrivilegeNew.html?webviewType=userActivitesType&dt_page_type=11");
        urls.put(E_SHARE_TYPE.E_DT_INTEGRAL_DETAIL, "https://sec.gushi.com/integralDetail.html?webviewType=userActivitesType&dt_page_type=11");
        urls.put(E_SHARE_TYPE.E_DT_FAQ_SERVICE_PROTOCOL, "https://sec.gushi.com/faqServiceProtocol.html");
        urls.put(E_SHARE_TYPE.E_DT_BS_SINGAL, "https://sec.gushi.com/bsSignal.html");
        urls.put(E_SHARE_TYPE.E_SMART_DIAGNOSIS_SEARCH, "https://sec.gushi.com/intelligentDiagnosisSearch.html?dt_from=app&webviewType=userActivitesType&dt_page_type=11");
        urls.put(E_SHARE_TYPE.E_SMART_DISGNOSIS_DETAIL, "https://sec.gushi.com/intelligentDiagnosis.html");
        urls.put(E_SHARE_TYPE.E_SIMILAR_SHAPE, "https://sec.gushi.com/similarShape.html");
        urls.put(E_SHARE_TYPE.E_SIMILAR_SHAPE_FAQ, "https://sec.gushi.com/faqSimilarShape.html");
        urls.put(E_SHARE_TYPE.E_VALUE_ADDED_SERVICE_PAGE, "https://privi.wedengta.com/valueAdded/valueAddedService.html?dt_from=app&webviewType=userActivitesType&dt_page_type=11");
        urls.put(E_SHARE_TYPE.E_USER_PROTOCOL, "https://sec.gushi.com/upUserServiceProtocol.html?&webviewType=userActivitesType&dt_page_type=11");
        urls.put(E_SHARE_TYPE.E_RISK_WARNING_UP_URL, "https://dtpay.uptougu.com/upRiskWarning.html?ptype=privi&webviewType=userActivitesType&dt_page_type=11");
        urls.put(E_SHARE_TYPE.E_DTBG_FAQ, "https://sec.gushi.com/InvestAdvise/faqIntelligentAnswer.html");
        urls.put(E_SHARE_TYPE.E_DTBG_SCHOOL, "https://sec.gushi.com/InvestAdvise/brotherSchool.html");
        urls.put(E_SHARE_TYPE.E_FINANCE_TRACK, "https://sec.gushi.com/financingTracking.html");
        urls.put(E_SHARE_TYPE.E_MARKET_FINANCE, "https://sec.gushi.com/marketSecurityMargin.html");
        urls.put(E_SHARE_TYPE.E_YXT_AGREEMENT_UP_URL, "https://dtpay.uptougu.com/yiAgreement.html?ptype=privi");
        urls.put(E_SHARE_TYPE.E_MY_COUPONS_UP_URL, "https://dtpay.uptougu.com/myCoupons.html?ptype=privi");
        urls.put(E_SHARE_TYPE.E_USE_COUPONS_UP_URL, "https://dtpay.uptougu.com/myCouponsUse.html?ptype=privi");
        urls.put(E_SHARE_TYPE.E_HOT_READING, "https://sec.gushi.com/hotReading.html");
        urls.put(E_SHARE_TYPE.E_RISK_EVAL_UP_URL, "https://dtpay.uptougu.com/riskEvaluating.html?ptype=privi");
        urls.put(E_SHARE_TYPE.E_STOCK_MAP_URL, "https://privi.wedengta.com/stockMap.html");
        urls.put(E_SHARE_TYPE.E_INVEST_MAP_INTRO, "https://sec.gushi.com/investmentMapIntro.html");
        urls.put(E_SHARE_TYPE.E_INVEST_MAP_SEC, "https://sec.gushi.com/investmentMap.html");
        urls.put(E_SHARE_TYPE.E_INVEST_DNA, "https://sec.gushi.com/investmentDNA.html");
        urls.put(E_SHARE_TYPE.E_DIRECT_ADD_BREAK, "https://sec.gushi.com/directionalAddIssuanceLiftedDetail.html");
        urls.put(E_SHARE_TYPE.E_PROTOCOL_COLLECT_UP_URL, "https://sec.gushi.com/protocolCollection.html?ptype=privi");
        urls.put(E_SHARE_TYPE.E_INVEST_MANAGER, "https://sec.gushi.com/investmentManager.html?");
        urls.put(E_SHARE_TYPE.E_LHB_STOCK_DETAIL, "https://sec.gushi.com/distinctionStockDetail.html?dt_from=app");
        urls.put(E_SHARE_TYPE.E_NEW_VIDEO_LIVE_SCHEDULE, "https://sec.gushi.com/videoLiveSchedule.html?");
        urls.put(E_SHARE_TYPE.E_NEW_VIDEO_LIST, "https://sec.gushi.com/videoList.html?");
        urls.put(E_SHARE_TYPE.E_NEW_VIDEO_DETAIL, "https://sec.gushi.com/videoDetail.html?");
        urls.put(E_SHARE_TYPE.E_CHIP_AVG_COST_FAQ, "https://sec.gushi.com/faqChipAveCost.html");
        urls.put(E_SHARE_TYPE.E_KLINE_STRATEGY_FAQ, "https://sec.gushi.com/faqKLineStrategy.html");
        urls.put(E_SHARE_TYPE.E_AVG_LINE_STRATEGY_FAQ, "https://sec.gushi.com/faqAveLineStrategy.html");
        urls.put(E_SHARE_TYPE.E_PRIVI_COMM_INTRO, "https://sec.gushi.com/commonIntro.html?");
        urls.put(E_SHARE_TYPE.E_SIM_TREND_RANK, "https://privi.wedengta.com/findRankSimuTrend.html");
        urls.put(E_SHARE_TYPE.E_DT_HOT_RANK, "https://privi.wedengta.com/findRankDtHot.html");
        urls.put(E_SHARE_TYPE.E_BS_SIGNAL_RANK, "https://privi.wedengta.com/findRankBsSignal.html");
        urls.put(E_SHARE_TYPE.E_BS_SIGNAL_RANK, "https://privi.wedengta.com/findRankBsSignal.html");
        urls.put(E_SHARE_TYPE.E_INVEST_UPDOWN_STREAM, "https://sec.gushi.com/investmentUpdownStream.html");
        urls.put(E_SHARE_TYPE.E_MARKET_INDEX, "https://privi.wedengta.com/checkMarketIndex.html");
        urls.put(E_SHARE_TYPE.E_MARKET_SECTION, "https://privi.wedengta.com/checkMarketSection.html");
        urls.put(E_SHARE_TYPE.E_BOARD_SECRETARY, "https://sec.gushi.com/boardSecretary.html");
        urls.put(E_SHARE_TYPE.E_BREAKUP_FAQ, "http://sec.gushi.com/faqBreakUpIntro.html?dt_from=web");

        urls.put(E_SHARE_TYPE.E_TEACHER_YAN_ARTICLE, "http://sec.gushi.com/yanOpinionDetail.html");
        urls.put(E_SHARE_TYPE.E_TEACHER_YAN_AUDIO_PLAYER, "http://sec.gushi.com/audioPlay.html");
        urls.put(E_SHARE_TYPE.E_MORNING_REF, "http://sec.gushi.com/morningReference.html");
        return urls;
    }

    @Override
    protected File getUrlFile(Context context) {
        return new File(context.getFilesDir(), "web_urls.bat");
    }

    private static String appendFromParams(final String url) {
        return appendParams(url, FROM_PARAMS);
    }

    private static String appendDefaultTabIndexParams(final String url, int index) {
        return appendParams(url, "defaultTabIndex=%s", index);
    }

    public String getShareIconUrl() {
        return getUrl(E_SHARE_TYPE.E_STOCKDETAIL_ICON);
    }

    public String getIntelligentAnswerUrl(final boolean state) {
        return appendParams(getUrl(E_SHARE_TYPE.E_INTELLIGENT_ANSWER), "dt_from=app&redDot=%s", state);
    }

    public String getCommentDetailUrl(final String feedId) {
        return appendParams(getUrl(E_SHARE_TYPE.E_FEED_COMMENT_DETAIL), "feedid=%s&dt_from=app", feedId);
    }

    public String getTeacherYanAudioPlayerUrl(final String audioId) {
        return appendParams(getUrl(E_SHARE_TYPE.E_TEACHER_YAN_AUDIO_PLAYER), "dt_from=web&iId=%s", audioId);
    }

    public String getTeacherYanActicleDetailUrl(final String articleId) {
        return appendParams(getUrl(E_SHARE_TYPE.E_TEACHER_YAN_ARTICLE), "dt_from=app&iID=%s", articleId);
    }

    public String getMorningRefUrl(final String morningRefId) {
        return appendParams(getUrl(E_SHARE_TYPE.E_MORNING_REF), "dt_from=app&iID=%s", morningRefId);
    }

    public String getOpinionDetailUrl(final String feedId) {
        return appendParams(getUrl(E_SHARE_TYPE.E_FEED_OPINION_DETAIL), "feedid=%s&dt_from=app", feedId);
    }

    public String getStockDetailUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_STOCK_DETAIL), SHARE_PARAMS, dtSecCode, secName);
    }

    public String getStockPortraitUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_STOCK_PORTRAIT), SEC_SBT_PARAMS, dtSecCode, secName, CommonWebConst.SWIPE_BACK_TYPE_FROM_LEFT_EDGE);
    }

    public String getSimilarKUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_SIMILAR_K_LINE), SEC_PARAMS, dtSecCode, secName);
    }

    public String getBreakUpUrl() {
        return appendParams(getUrl(E_SHARE_TYPE.E_BREAKUP_FAQ), "");
    }

    public String getMockTrend(String dtSecCode, String secName) {
        return appendDefaultTabIndexParams(getSimilarKUrl(dtSecCode, secName), 1);
    }

    public String getSecHistoryUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_SEC_HISTORY), SEC_PARAMS, dtSecCode, secName);
    }

    public String getSecChipUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_DT_CHIP_DISTRIBUTION), "secCode=%s&secName=%s&dt_from=app&dt_sbt=%s&defaultTabIndex=%s", dtSecCode, secName, CommonWebConst.SWIPE_BACK_TYPE_FROM_LEFT_EDGE, 0);
    }

    public String getSecChipUrl(String dtSecCode, String secName, int defaultTabIndex) {
        return appendParams(getUrl(E_SHARE_TYPE.E_DT_CHIP_DISTRIBUTION), "secCode=%s&secName=%s&dt_from=app&dt_sbt=%s&defaultTabIndex=%s", dtSecCode, secName, CommonWebConst.SWIPE_BACK_TYPE_FROM_LEFT_EDGE, defaultTabIndex);
    }

    public String getSecLiveUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_SEC_LIVE), SEC_PARAMS, dtSecCode, secName);
    }

    public String getBigEventUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_BIG_EVENT_REMINDER), SEC_PARAMS, dtSecCode, secName);
    }

    public String getConceptDetailUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_CONC_DETAIL), SHARE_PARAMS, dtSecCode, secName);
    }

    public String getIndexDetailUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_INDEX_DETAIL), SHARE_PARAMS, dtSecCode, secName);
    }

    public String getFundDetailUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_FUND_DETAIL), SHARE_PARAMS, dtSecCode, secName);
    }

    public String getFuturesDetailUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_FUTURES_DETAIL), SHARE_PARAMS, dtSecCode, secName);
    }

    public String getActivitiesUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_ACTIVITY_LIST));
    }

    public String getNewStockUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_NEW_STOCK));
    }

    public String getDragonTigerBillboardUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_LHB_STOCK));
    }

    public String getDragonTigerBillboardStockDetailUrl(String dtSecCode, String secName, String date) {
        return appendParams(getUrl(E_SHARE_TYPE.E_LHB_STOCK_DETAIL),
                "pageType=stockDetail&seccode=%s&secname=%s&date=%s", dtSecCode, secName, date);
    }

    public String getDragonTigerBillboardSaleDepartDetailUrl(String saleDepartName, String date) {
        return appendParams(getUrl(E_SHARE_TYPE.E_LHB_STOCK_DETAIL),
                "pageType=saleDetail&saleName=%s&date=%s", saleDepartName, date);
    }

    public String getUserAgreementUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_URL_USER_AGREEMENT));
    }

    /**
     * 智能选股，策略榜单的url
     */
    public String getPickStockStrategyUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_STRATEGY_LIST));
    }

    public String getPickStockFAQUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_SMART_PICK_FAQ));
    }

    /**
     * 大盘预警url
     */
    public String getMarketWarningUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_MARKET_WARNING));
    }

    public String getLiveMsgPortfolioUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DT_PORTFOLIO_LIVE));
    }

    public String getLiveMsgMarketUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DT_MARKET_LIVE));
    }

    /**
     * 灯塔直播的faq
     */
    public String getLiveFaqUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DT_LIVE_FAQ));
    }

    /**
     * 私有化列表
     */
    public String getPrivatizationTrackingListUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DT_PRIVATE_TRACKING));
    }

    /**
     * 个股私有化详情
     */
    public String getPrivatizationTrackingDetailUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_DT_PRIVATE_DETAIL), SEC_PARAMS, dtSecCode, secName);
    }

    /**
     * 个股停复牌详情
     */
    public String getSuspensionDetailUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_SUSPENTION_DETAIL), SEC_PARAMS, dtSecCode, secName);
    }

    /**
     * 个股定增详情
     */
    public String getDirectionAddDetailUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_DIRECTION_DETAIL), SEC_PARAMS, dtSecCode, secName);
    }

    /**
     * 个股定增详情
     */
    public String getDirectionAddListUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DIRECTION_ADD));
    }

    /**
     * ah股溢价个股url
     */
    public String getAhPremium(final String aDtCode, final String aName, final String hDtCode, final String hName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_AH_PREMIUM), "seccodeA=%s&secnameA=%s&seccodeH=%s&secnameH=%s&dt_from=app",  aDtCode, aName, hDtCode, hName);
    }

    public String getQrCodeFaqUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_QRCODE_FAQ));
    }

    public String getIndustryPlateListUrl() {
        return getPlateListUrl("industryPlateRankList");
    }

    public String getConceptPlateListUrl() {
        return getPlateListUrl("conceptPlateRankList");
    }

    public String getPlateListUrl(final String ranktype) {
        return appendParams(getUrl(E_SHARE_TYPE.E_PLATE_LIST_PAGE), PLATE_LIST_PARAMS, ranktype);
    }

    public String getCapitalFlowIndustryIncreaseListUrl() {
        return appendParams(getUrl(E_SHARE_TYPE.E_PLATE_FLOW_PAGE), PLATE_CAPATAL_FLOW_LIST_PARAMS, "industryFlowList", "in");
    }

    public String getCapitalFlowIndustryDecreaseListUrl() {
        return appendParams(getUrl(E_SHARE_TYPE.E_PLATE_FLOW_PAGE), PLATE_CAPATAL_FLOW_LIST_PARAMS, "industryFlowList", "out");
    }

    public String getCapitalFlowConceptIncreaseListUrl() {
        return appendParams(getUrl(E_SHARE_TYPE.E_PLATE_FLOW_PAGE), PLATE_CAPATAL_FLOW_LIST_PARAMS, "conceptFlowList", "in");
    }

    public String getCapitalFlowConceptDecreaseListUrl() {
        return appendParams(getUrl(E_SHARE_TYPE.E_PLATE_FLOW_PAGE), PLATE_CAPATAL_FLOW_LIST_PARAMS, "conceptFlowList", "out");
    }

    public String getPrivilegeUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DT_PRIVI_DETAIL_NEW));
    }

    /**
     * 扫描二维码的url
     */
    public String getQrcodeUrl() {
        return getUrl(E_SHARE_TYPE.E_QRCODE_URL);
    }

    /**
     * 自选日报的url
     */
    public String getPortfolioDailyReportUrl() {
        return getUrl(E_SHARE_TYPE.E_SELF_DAILY_REPORT);
    }

    public String getCustomizedStrategyUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_SMART_PICKING_CUSTOM));
    }

    public String getPortfolioStrategyUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_SMART_PICKING_PORTFOLIO));
    }

    public String getAHPremiumFaqUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_AH_PREMIUM_FAQ));
    }

    public String getAHPremiumListShareUrl() {
        return getUrl(E_SHARE_TYPE.E_AH_PREMIUM_LIST);
    }

    public String getOpenChipDistributionPrivilegeUrl() {
        return appendParams(getUrl(E_SHARE_TYPE.E_DT_SINGLE_PRIVI_DETAIL), OPEN_PRIVILEGE_PARAMS, "1");
    }

    public String getOpenSimilarKlinePrivilegeUrl() {
        return appendParams(getUrl(E_SHARE_TYPE.E_DT_SINGLE_PRIVI_DETAIL), OPEN_PRIVILEGE_PARAMS, "2");
    }

    public String getOpenHistoryPrivilegeUrl() {
        return appendParams(getUrl(E_SHARE_TYPE.E_DT_SINGLE_PRIVI_DETAIL), OPEN_PRIVILEGE_PARAMS, "3");
    }

    public String getInviteFriendsUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_ACCU_POINT_INVITE_FRIEND));
    }

    public String getAccumulatePointsFaqUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_ACCU_POINT_FAQ));
    }
    // 老视频相关接口，已废弃
//    public String getLiveDetailUrl(String videoId) {
//        return appendParams(getUrl(E_SHARE_TYPE.E_VIDEO_DETAIL), VIDEO_DETAIL_PARAMS, videoId, "0");
//    }
//
//    public String getVodDetailUrl(String videoId) {
//        return appendParams(getUrl(E_SHARE_TYPE.E_VIDEO_DETAIL), VIDEO_DETAIL_PARAMS, videoId, "1");
//    }
//    public String getVideoListUrl(String category) {
//        return appendParams(getUrl(E_SHARE_TYPE.E_VIDEO_LIST), VIDEO_LIST_PARAMS, category);
//    }

    public String getBSUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DT_BS_SINGAL));
    }

    public String getBSUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_DT_BS_SINGAL), SEC_PARAMS, dtSecCode, secName);
    }

    public String getIntegralDetail() {
        return getUrl(E_SHARE_TYPE.E_DT_INTEGRAL_DETAIL);
    }

    public String getFaqServiceProtocol() {
        return getUrl(E_SHARE_TYPE.E_DT_FAQ_SERVICE_PROTOCOL);
    }

    public String getUpUserProtocol() {
        return getUrl(E_SHARE_TYPE.E_USER_PROTOCOL);
    }

    public String getUpRiskWarning() {
        return getUrl(E_SHARE_TYPE.E_RISK_WARNING_UP_URL);
    }

    public String getYxtAgreement() {
        return getUrl(E_SHARE_TYPE.E_YXT_AGREEMENT_UP_URL);
    }

    public String getMyCoupons() {
        return getUrl(E_SHARE_TYPE.E_MY_COUPONS_UP_URL);
    }

    public String getUseCoupons(int type, int value, int number, int unit, String extra, String couponCode) {
        return appendParams(getUrl(E_SHARE_TYPE.E_USE_COUPONS_UP_URL), USE_COUPONS_PARAMS, type, value, number, unit, extra, couponCode);
    }

    public String getIntelligentDiagnosisDetailUrl(String secCode, String secName, int tabIndex) {
        return appendParams(getUrl(E_SHARE_TYPE.E_SMART_DISGNOSIS_DETAIL), SMART_DISGNOSIS_DETAIL_PARAMS, secCode, secName, String.valueOf(tabIndex));
    }

    public String getIntelligentDiagnosisSearchUrl() {
        return getUrl(E_SHARE_TYPE.E_SMART_DIAGNOSIS_SEARCH);
    }

    public String getSimilarShapeUrl() {
        return getUrl(E_SHARE_TYPE.E_SIMILAR_SHAPE);
    }

    public String getSimilarShapeUrl(String seccode, String secname, String totalStartDate, String totalEndDate, String selectStartDate, String selectEndDate) {
        return appendParams(getUrl(E_SHARE_TYPE.E_SIMILAR_SHAPE),
                SIMILAR_SHAPE_PARAMS, seccode, secname, totalStartDate, totalEndDate, selectStartDate, selectEndDate);
    }

    public String getSimilarShapeFaqUrl() {
        return getUrl(E_SHARE_TYPE.E_SIMILAR_SHAPE_FAQ);
    }

    public String getValueAddedServiceUrl() {
        return getUrl(E_SHARE_TYPE.E_VALUE_ADDED_SERVICE_PAGE);
    }

    public String getIntelligentAnswerSchoolUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DTBG_SCHOOL));
    }

    public String getIntelligentAnswerHelpUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DTBG_FAQ));
    }

    public String getCommonIntroUrl(final int type) {
        return appendParams(getUrl(E_SHARE_TYPE.E_PRIVI_COMM_INTRO), "type=%s&dt_from=app&dt_sbt=2", type);
    }

    public String getMarginTrackingUrl(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_FINANCE_TRACK), "secCode=%s&secName=%s&defaultTabIndex=%s&dt_from=app", dtSecCode, secName, 0);
    }

    public String getMarginTrackingUrl(String dtSecCode, String secName, int defaultTabIndex) {
        return appendParams(getUrl(E_SHARE_TYPE.E_FINANCE_TRACK), "secCode=%s&secName=%s&defaultTabIndex=%s&dt_from=app", dtSecCode, secName, defaultTabIndex);
    }

    public String getMarketMarginUrl() {
        return getUrl(E_SHARE_TYPE.E_MARKET_FINANCE);
    }

    public String getHotSpotDetailUrl(final String id) {
        return appendFromParams(appendParams(getUrl(E_SHARE_TYPE.E_HOT_READING), "id=%s&dt_sbt=2", id));
    }

    public String getRiskEvalUrl() {
        return appendParams(getUrl(E_SHARE_TYPE.E_RISK_EVAL_UP_URL), "dt_sbt=0&dt_from=app");
    }

    public String getRiskEvalUrl(int type) {
        return appendParams(getUrl(E_SHARE_TYPE.E_RISK_EVAL_UP_URL), "type=%s&dt_sbt=0&dt_from=app", type);
    }

    public String getIndustrialChainUrl(final String dtSecCode, final String secName) {
        return appendDefaultTabIndexParams(appendParams(getUrl(E_SHARE_TYPE.E_INVEST_MAP_SEC), SEC_PARAMS, dtSecCode, secName), 2);
    }

    public String getDirectAddBreak(final String id, final String dtSecCode, final String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_DIRECT_ADD_BREAK),
                "sId=%s&secCode=%s&secName=%s&dt_from=app", id, dtSecCode, secName);
    }

    public String getProtocalCollectUp(String orderId, String riskLevel, int riskSuitable) {
        return appendParams(getUrl(E_SHARE_TYPE.E_PROTOCOL_COLLECT_UP_URL),
                "orderId=%s&risk_level=%s&risk_suitable=%s&dt_sbt=0", orderId, riskLevel, riskSuitable);
    }

    public String getChipAvgCostFaqUrl() {
        return getUrl(E_SHARE_TYPE.E_CHIP_AVG_COST_FAQ);
    }

    public String getKLineStrategyFaqUrl() {
        return getUrl(E_SHARE_TYPE.E_KLINE_STRATEGY_FAQ);
    }

    public String getAvgLineStrategyFaqUrl() {
        return getUrl(E_SHARE_TYPE.E_AVG_LINE_STRATEGY_FAQ);
    }

    public String getMockTrendRankUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_SIM_TREND_RANK));
    }

    public String getHotStockRankUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_DT_HOT_RANK));
    }

    public String getBSSignalRankUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_BS_SIGNAL_RANK));
    }

    public String getUpstreamProductUrl(final String dtSecCode, final String secName) {
        return appendFromParams(appendParams(getUrl(E_SHARE_TYPE.E_INVEST_UPDOWN_STREAM),
                "secCode=%s&secName=%s&streamFrom=0&streamType=0", dtSecCode, secName));
    }

    public String getDownstreamProductUrl(final String dtSecCode, final String secName) {
        return appendFromParams(appendParams(getUrl(E_SHARE_TYPE.E_INVEST_UPDOWN_STREAM),
                "secCode=%s&secName=%s&streamFrom=1&streamType=0", dtSecCode, secName));
    }

    public String getMarketChangeIndexUrl() {
        return appendFromParams(appendParams(getUrl(E_SHARE_TYPE.E_MARKET_INDEX), "dt_sbt=2"));
    }

    public String getMarketChangeStateUrl(final int index) {
        return appendFromParams(appendParams(getUrl(E_SHARE_TYPE.E_MARKET_SECTION),
                "eStatChangeRange=%s&isSt=false", index));
    }

    public String getPaymentFaqUrl(final int type) {
        return appendFromParams(appendParams(getUrl(E_SHARE_TYPE.E_PAYMENT_FAQ),
                "type=%s", type));
    }

    public String getPortfolioEventUrl() {
        return appendFromParams(getUrl(E_SHARE_TYPE.E_SELF_CHOOSE_EVENT));
    }

    public String getBoardSecretary(String dtSecCode, String secName) {
        return appendParams(getUrl(E_SHARE_TYPE.E_BOARD_SECRETARY), SEC_PARAMS, dtSecCode, secName);
    }
}
