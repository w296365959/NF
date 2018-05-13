package com.sscf.investment.portfolio;

import BEC.*;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.R;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.portfolio.widget.RemindCheckboxItemLayout;
import com.sscf.investment.portfolio.widget.RemindEditItemLayout;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import java.util.ArrayList;

/**
 * Created by xuebinliu on 2015/8/5.
 *
 * 提醒设置
 */
@Route("PortfolioRemindActivity")
public final class PortfolioRemindActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {
    private static final String TAG = "PortfolioRemindActivity";

    private StockDbEntity mStockEntity;
    private RemindEditItemLayout mStockPriceUpView;
    private RemindEditItemLayout mStockPriceDownView;
    private RemindEditItemLayout mStockRatioUpView;
    private RemindEditItemLayout mStockRatioDownView;
    private RemindCheckboxItemLayout mAiRemindLayout;
    private RemindCheckboxItemLayout mPushAnnouncementLayout;
    private RemindCheckboxItemLayout mPushResearchLayout;

    private float mCurrentPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_portfolio_remind);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
        final String unicode = getIntent().getStringExtra(CommonConst.KEY_SEC_CODE);
        mStockEntity = portfolioDataManager.getStockDbEntity(unicode);
        if (mStockEntity == null) {
            finish();
            return;
        }

        initViews();

        final ArrayList<String> unicodes = new ArrayList<String>(1);
        unicodes.add(unicode);
        QuoteRequestManager.getSimpleQuoteRequest(unicodes, this, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(mStockPriceUpView);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.portfolio_remind_title);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        final TextView rightButton = (TextView) findViewById(R.id.actionbar_right_button);
        rightButton.setOnClickListener(this);
        rightButton.setText(R.string.finish);

        ((TextView)findViewById(R.id.portfolioRemindStockTitle)).setText(mStockEntity.getSzName());

        setPriceUpdownText(mStockEntity.getFNow(), mStockEntity.getFClose(), mStockEntity.getITpFlag(), E_SEC_STATUS.E_SS_NORMAL);

        mStockPriceUpView = (RemindEditItemLayout) findViewById(R.id.portfolioRemindStockPriceUpLayout);
        mStockPriceUpView.setValue(R.string.portfolio_remind_stock_price_up_title,
                R.string.portfolio_remind_stock_price_hint, mStockEntity.getHighPrice());

        mStockPriceDownView = (RemindEditItemLayout) findViewById(R.id.portfolioRemindStockPriceDownLayout);
        mStockPriceDownView.setValue(R.string.portfolio_remind_stock_price_down_title,
                R.string.portfolio_remind_stock_price_hint, mStockEntity.getLowPrice());

        mStockRatioUpView = (RemindEditItemLayout) findViewById(R.id.portfolioRemindStockRatioUpLayout);
        mStockRatioUpView.setValue(R.string.portfolio_remind_stock_ratio_up_title,
                R.string.portfolio_remind_stock_ratio_up_hint, mStockEntity.getIncreasePer());

        mStockRatioDownView = (RemindEditItemLayout) findViewById(R.id.portfolioRemindStockRatioDownLayout);
        mStockRatioDownView.setValue(R.string.portfolio_remind_stock_ratio_down_title,
                R.string.portfolio_remind_stock_ratio_down_hint, mStockEntity.getDecreasesPer());

        mStockPriceUpView.setData(mCurrentPrice, RemindEditItemLayout.TYPE_PRICE_UP);
        mStockPriceDownView.setData(mCurrentPrice, RemindEditItemLayout.TYPE_PRICE_DOWN);
        mStockRatioUpView.setData(mCurrentPrice, RemindEditItemLayout.TYPE_RATIO_UP);
        mStockRatioDownView.setData(mCurrentPrice, RemindEditItemLayout.TYPE_RATIO_DOWN);

        initAnnouncementResearch();
    }

    private void initAnnouncementResearch() {
        final String dtSecCode = mStockEntity.getDtSecCode();
        mPushAnnouncementLayout = (RemindCheckboxItemLayout) findViewById(R.id.portfolioRemindPushAnnouncementLayout);
        mPushResearchLayout = (RemindCheckboxItemLayout) findViewById(R.id.portfolioRemindResearchReportLayout);
        mAiRemindLayout = (RemindCheckboxItemLayout) findViewById(R.id.portfolioRemindAiRemind);
        int marketType = StockUtil.getMarketType(dtSecCode);
        mPushAnnouncementLayout.setValue(R.string.portfolio_remind_announcement_title, mStockEntity.isPushAnnouncement());
        mPushResearchLayout.setValue(R.string.portfolio_remind_research_report_title, mStockEntity.isPushResearch());
        mAiRemindLayout.setValue(R.string.portfolio_remind_ai_remind_title, mStockEntity.isAiAlert());

        final boolean isStock = StockUtil.isStock(dtSecCode);
        if (isStock) {
            switch (marketType) {
                case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
                case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                case E_MARKET_TYPE.E_MT_HK: //港股
                    mPushAnnouncementLayout.setVisibility(View.VISIBLE);
                    mPushResearchLayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.portfolioLine).setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

        if (isStock || StockUtil.isFund(dtSecCode)) {
            switch (marketType) {
                case E_MARKET_TYPE.E_MT_SH: //上海证券交易所
                case E_MARKET_TYPE.E_MT_SZ: //深圳证券交易所
                    if(!StockUtil.isChineseStockB(dtSecCode)) {
                        mAiRemindLayout.setVisibility(View.VISIBLE);
                        findViewById(R.id.portfolioRemindAiRemindIntro).setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setPriceUpdownText(final float now, final float close, final int tpFlag, final int status) {
        DtLog.d(TAG, "setPriceUpdownText now : " + now);
        DtLog.d(TAG, "setPriceUpdownText close : " + close);
        DtLog.d(TAG, "setPriceUpdownText status : " + status);
        TextAppearanceSpan span = null;

        final Resources resources = getResources();
        float currentPrice = 0f;

        final SpannableStringBuilder price = new SpannableStringBuilder(resources.getString(R.string.stock_list_head_new_price));
        price.append(' ');
        final int preLength = price.length();

        final SpannableStringBuilder updown = new SpannableStringBuilder(resources.getString(R.string.delta));
        updown.append(' ');
        final int updownPreLength = updown.length();

        if (status == E_SEC_STATUS.E_SS_SUSPENDED) {
            span = StringUtil.getSuspensionStyle();
            currentPrice = close;
            updown.append("--");
        } else {
            if (now <= 0) { // 停牌处理
                span = StringUtil.getSuspensionStyle();
                currentPrice = close;
                updown.append("--");
            } else {
                final float updownFlt = (now / close - 1) * 100;
                if (now > close) {
                    span = StringUtil.getUpStyle();
                    updown.append('+');
                } else if (now < close) {
                    span = StringUtil.getDownStyle();
                } else {
                    span = StringUtil.getSuspensionStyle();
                }
                currentPrice = now;
                updown.append(StringUtil.getFormatedFloat(updownFlt)).append('%');
            }
        }

        mCurrentPrice = currentPrice;

        price.append(StringUtil.getFormattedFloat(currentPrice, tpFlag));

        price.setSpan(span, preLength, price.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ((TextView)findViewById(R.id.portfolioRemindStockPrice)).setText(price);

        updown.setSpan(span, updownPreLength, updown.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ((TextView)findViewById(R.id.portfolioRemindStockRatio)).setText(updown);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_right_button:
                clickFinishButton();
                break;
            default:
                break;
        }
    }

    private void clickFinishButton() {
        if (!mStockPriceUpView.isInputValid() || !mStockPriceDownView.isInputValid()) {
            return;
        }

        boolean success = false;
        if (mStockEntity.getHighPrice() != mStockPriceUpView.getValue()) {
            mStockEntity.setHighPrice(mStockPriceUpView.getValue());
            success = true;
        }

        if (mStockEntity.getLowPrice() != mStockPriceDownView.getValue()) {
            mStockEntity.setLowPrice(mStockPriceDownView.getValue());
            success = true;
        }

        if (mStockEntity.getIncreasePer() != mStockRatioUpView.getValue()) {
            mStockEntity.setIncreasePer(mStockRatioUpView.getValue());
            success = true;
        }

        if (mStockEntity.getDecreasesPer() != mStockRatioDownView.getValue()) {
            mStockEntity.setDecreasesPer(mStockRatioDownView.getValue());
            success = true;
        }

        if (mStockEntity.isPushAnnouncement() != mPushAnnouncementLayout.isChecked()) {
            mStockEntity.setPushAnnouncement(mPushAnnouncementLayout.isChecked());
            success = true;
        }

        if (mStockEntity.isPushResearch() != mPushResearchLayout.isChecked()) {
            mStockEntity.setPushResearch(mPushResearchLayout.isChecked());
            success = true;
        }

        if (mStockEntity.isAiAlert() != mAiRemindLayout.isChecked()) {
            mStockEntity.setAiAlert(mAiRemindLayout.isChecked());
            success = true;
        }

        if (success) {
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager != null) {
                portfolioDataManager.updateReminderInfo(mStockEntity);
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_EDIT_REMIND_SETTING_SUCCESS);
            }
        }

        finish();
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        DtLog.d(TAG, "callback success : " + success);
        if (success) {
            switch (data.getEntityType()) {
                case EntityObject.ET_GET_SIMPLE_QUOTE:
                    final ArrayList<SecSimpleQuote> quotes = ((QuoteSimpleRsp) data.getEntity()).vSecSimpleQuote;
                    DtLog.d(TAG, "callback quotes : " + quotes);
                    if (quotes != null && quotes.size() > 0) {
                        final SecSimpleQuote quote = quotes.get(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateViews(quote);
                            }
                        });
                    }
                    return;
                default:
                    break;
            }
        }
    }

    private void updateViews(final SecSimpleQuote quote) {
        DtLog.d(TAG, "updateViews quote : " + quote);
        setPriceUpdownText(quote.fNow, quote.fClose, quote.iTpFlag, quote.eSecStatus);
        mStockPriceUpView.setData(mCurrentPrice, RemindEditItemLayout.TYPE_PRICE_UP);
        mStockPriceDownView.setData(mCurrentPrice, RemindEditItemLayout.TYPE_PRICE_DOWN);
        mStockRatioUpView.setData(mCurrentPrice, RemindEditItemLayout.TYPE_RATIO_UP);
        mStockRatioDownView.setData(mCurrentPrice, RemindEditItemLayout.TYPE_RATIO_DOWN);
    }
}
