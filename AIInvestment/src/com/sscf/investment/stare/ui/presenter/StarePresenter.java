package com.sscf.investment.stare.ui.presenter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.dengtacj.component.entity.stare.StareGroupInfo;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.component.managers.ISmartStareManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.stare.ui.EventStareFragment;
import com.sscf.investment.stare.ui.PriceStareFragment;
import com.sscf.investment.stare.ui.SmartStockStareActivity;
import com.sscf.investment.stare.ui.StradegyStareFragment;
import com.sscf.investment.stare.ui.Submitable;
import com.sscf.investment.stare.ui.model.StareModel;
import com.sscf.investment.stare.ui.widget.StareSelectDialog;
import com.sscf.investment.stare.ui.widget.SubmitableFragment;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import BEC.E_SEC_STATUS;
import BEC.SecSimpleQuote;

/**
 * Created by yorkeehuang on 2017/9/11.
 */

public class StarePresenter implements StareSelectDialog.OnConfirmListener {
    private static final String TAG = StarePresenter.class.getSimpleName();

    private SmartStockStareActivity mActivity;
    private Fragment[] mFragments;
    private List<Submitable> mSubmitableList = new ArrayList<>();
    private StareModel mModel;
    final private StockDbEntity mStockEntity;
    private float mCurrentPrice = -1f;

    private List<StareGroupInfo> mTimeGroupInfos;
    private List<StareGroupInfo> mStrategyGroupInfos;

    public StarePresenter(@NonNull SmartStockStareActivity activity, @NonNull SubmitableFragment[] fragments, @NonNull StockDbEntity stockDbEntity) {
        mStockEntity = stockDbEntity;
        mActivity = activity;
        registerSubmitable(activity);
        mFragments = fragments;
        registerSubmitables(fragments);
        setPriceUpdownText(stockDbEntity.getFNow(), stockDbEntity.getFClose(), stockDbEntity.getITpFlag(), E_SEC_STATUS.E_SS_NORMAL);
        mModel = new StareModel(this);
        initValue(stockDbEntity);
    }

    private void initValue(StockDbEntity stockEntity) {
        for(Submitable submitable : mSubmitableList) {
            submitable.initValue(stockEntity);
        }
    }

    private void registerSubmitables(Submitable[] submitables) {
        for(Submitable submitable : submitables) {
            registerSubmitable(submitable);
        }
    }

    private void registerSubmitable(Submitable submitable) {
        mSubmitableList.add(submitable);
    }

    private PriceStareFragment getPriceStatreFragment() {
        return (PriceStareFragment)mFragments[0];
    }

    private EventStareFragment getEventStareFragment() {
        return (EventStareFragment)mFragments[1];
    }

    private StradegyStareFragment getStradegyStareFragment() {
        return (StradegyStareFragment)mFragments[2];
    }

    public void requestSimpleQuote(final String unicode) {
        mModel.requestSimpleQuote(unicode);
    }

    public void onGetSimpleQuote(SecSimpleQuote quote) {
        mActivity.runOnUiThread(() -> {
            setPriceUpdownText(quote.getFNow(), quote.getFClose(), quote.getITpFlag(), quote.getESecStatus());
            notifyCurrentPriceChange();
        });
    }

    public void notifyCurrentPriceChange() {
        getPriceStatreFragment().notifyCurrentPriceChange();
        getEventStareFragment().notifyCurrentPriceChange();
    }

    private float setPriceUpdownText(final float now, final float close, final int tpFlag, final int status) {
        DtLog.d(TAG, "setPriceUpdownText now : " + now);
        DtLog.d(TAG, "setPriceUpdownText close : " + close);
        DtLog.d(TAG, "setPriceUpdownText status : " + status);
        TextAppearanceSpan span = null;

        final Resources resources = mActivity.getResources();
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
        mActivity.setStockPrice(price);

        updown.setSpan(span, updownPreLength, updown.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mActivity.setStockRatio(updown);

        return currentPrice;
    }

    public float getCurrentPrice() {
        return mCurrentPrice;
    }

    public boolean cancel() {
        int result = Submitable.RESULT_NOCHANGE;
        for(Submitable submitable : mSubmitableList) {
            switch (submitable.checkInput(mStockEntity)) {
                case Submitable.RESULT_INVALID:
                    result = Submitable.RESULT_INVALID;
                    break;
                case Submitable.RESULT_SHOULD_SUBMIT:
                    result = Submitable.RESULT_SHOULD_SUBMIT;
                    continue;
                default:
                    continue;
            }
        }

        if(result == Submitable.RESULT_SHOULD_SUBMIT) {
            CommonDialog cancelDialog = new CommonDialog(mActivity);
            cancelDialog.setTitle(R.string.prompt_title);
            cancelDialog.setMessage(R.string.cancel_smart_stare_dialog_msg);
            cancelDialog.addButton(R.string.cancel);
            cancelDialog.addButton(R.string.save);
            cancelDialog.setButtonClickListener((dialog, view, position) -> {
                switch (position) {
                    case 1:
                        submit();
                        break;
                    default:
                }

                if(mActivity != null && !mActivity.isDestroy()) {
                    dialog.dismiss();
                    mActivity.finish();
                }
            });

            cancelDialog.show();
            return false;
        }
        return true;
    }

    public int submit() {
        int result = Submitable.RESULT_NOCHANGE;
        if(mStockEntity != null) {
            for(Submitable submitable : mSubmitableList) {
                switch (submitable.submit(mStockEntity)) {
                    case Submitable.RESULT_INVALID:
                        result = Submitable.RESULT_INVALID;
                        break;
                    case Submitable.RESULT_SHOULD_SUBMIT:
                        result = Submitable.RESULT_SHOULD_SUBMIT;
                        continue;
                    default:
                        continue;
                }
            }
        }

        if(result == Submitable.RESULT_SHOULD_SUBMIT) {
            IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance().getManager(IPortfolioDataManager.class.getName());
            if(portfolioDataManager != null) {
                portfolioDataManager.updateReminderInfo(mStockEntity);
                StatisticsUtil.reportAction(StatisticsConst.SMART_STARE_CHANGE_PARAMS);
            }
        }

        return result;
    }

    public void showSelectDialog(StareGroupInfo groupInfo, Set<Integer> selectedItems, int type) {
        if(groupInfo != null) {
            StareSelectDialog dialog = new StareSelectDialog(mActivity, type, selectedItems, groupInfo);
            dialog.setOnConfirmListener(this);
            dialog.show();
        }
    }

    public List<StareGroupInfo> getTimeGroupList() {
        if(mTimeGroupInfos == null) {
            ISmartStareManager manager = (ISmartStareManager) ComponentManager.getInstance().getManager(ISmartStareManager.class.getName());
            if(manager != null) {
                mTimeGroupInfos = manager.getStareTimeList();
            }
        }
        return mTimeGroupInfos;
    }

    public List<StareGroupInfo> getStrategyGroupList() {
        if(mStrategyGroupInfos == null) {
            ISmartStareManager manager = (ISmartStareManager) ComponentManager.getInstance().getManager(ISmartStareManager.class.getName());
            if(manager != null) {
                mStrategyGroupInfos = manager.getStrategyGroupList();
            }
        }
        return mStrategyGroupInfos;
    }

    public StareGroupInfo getStrategyGroupByTitle(String title) {
        List<StareGroupInfo> groupList = getStrategyGroupList();
        for(StareGroupInfo groupInfo : groupList) {
            if(TextUtils.equals(groupInfo.groupTitle, title)) {
                return groupInfo;
            }
        }
        return null;
    }

    private boolean isTimeGroup(String groupTitle) {
        List<StareGroupInfo> groupInfos = getTimeGroupList();
        for(StareGroupInfo groupInfo : groupInfos) {
            if(TextUtils.equals(groupInfo.groupTitle, groupTitle)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onConfirm(StareGroupInfo groupInfo, Set<Integer> selectedItems) {
        if(isTimeGroup(groupInfo.groupTitle)) {
            getPriceStatreFragment().setSelectedTime(groupInfo, selectedItems);
        } else {
            getStradegyStareFragment().setSelectedResult(groupInfo, selectedItems);
        }
    }
}
