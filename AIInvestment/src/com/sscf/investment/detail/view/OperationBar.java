package com.sscf.investment.detail.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseActivity;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.component.ui.widget.ToolsItem;
import com.sscf.investment.detail.MoreOperationDialog;
import com.sscf.investment.detail.entity.MoreOperationToolsItem;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.portfolio.DelPortDialog;
import com.sscf.investment.portfolio.PortfolioGroupManagerActivity;
import com.dengtacj.component.entity.db.GroupEntity;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import java.util.ArrayList;
import java.util.List;
import BEC.SecInfo;

/**
 * Created by liqf on 2015/7/29.
 */
public class OperationBar extends LinearLayout implements View.OnClickListener, DelPortDialog.DelDialogCallback {
    private OnOperationListener mOperationListener;
    private FrameLayout mLiveMsgLayout;
    private View mLiveRedDot;
    private View mChipLayout;
    private View mSmartStareLayout;
    private View mMoreLayout;
    private TextView mAddBtn;
    private TextView mRemoveBtn;

    private boolean mIsAdded = false;

    private SecInfo mSecInfo;
    public String mDtSecCode;
    public String mSecName;

    private Context mContext;

    public OperationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.detail_operation_bar, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLiveMsgLayout = (FrameLayout) findViewById(R.id.operation_bar_live_message_layout);
        mLiveMsgLayout.setOnClickListener(this);
        mLiveRedDot = mLiveMsgLayout.findViewById(R.id.live_message_red_dot);
        mChipLayout = findViewById(R.id.operation_bar_chip_layout);
        mChipLayout.setOnClickListener(this);
        mSmartStareLayout = findViewById(R.id.operation_bar_smart_stare_layout);
        mSmartStareLayout.setOnClickListener(this);
        mMoreLayout = findViewById(R.id.operation_bar_more_layout);
        mMoreLayout.setOnClickListener(this);
        mAddBtn = (TextView) findViewById(R.id.add);
        mAddBtn.setOnClickListener(this);
        mRemoveBtn = (TextView) findViewById(R.id.remove);
        mRemoveBtn.setOnClickListener(this);

        updateMoreRedDot();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerBroadcastReceiver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterBroadcastReceiver();
    }

    private LocalBroadcastReceiver mReceiver;

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_RED_DOT_STATE_CHANGED);
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_RED_DOT_STATE_CHANGED.equals(action)) {
                updateMoreRedDot();
            }
        }
    }

    private void updateMoreRedDot() {
//        final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
//        final boolean hasDot = redDotManager.getMoreOperationState();
//        mMoreRedDot.setVisibility(hasDot ? VISIBLE : INVISIBLE);
    }

    public void setSecInfo(SecInfo secInfo) {
        mSecInfo = secInfo;
        updateMoreRedDot();
    }

    @Override
    public void callback(boolean isConfirm, ArrayList<String> delArray) {
        if (isConfirm) {
            mOperationListener.onRemove();
            setIsAdded(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        if (mOperationListener == null) {
            return;
        }

        final Context context = getContext();
        if (context instanceof BaseActivity) {
            if (((BaseActivity)context).isDestroy()) {
                return;
            }
        }

        if (v == mLiveMsgLayout) {//直播
            mOperationListener.onLiveMsg();
            StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_LIVE_MSG_CLICKED);
        } else if (v == mChipLayout) {//筹码
            mOperationListener.onChip();
        } else if (v == mSmartStareLayout) {//盯盘
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager == null) {
                return;
            }
            final boolean isAdded = portfolioDataManager.isPortfolio(mDtSecCode);
            if (isAdded) {
                mOperationListener.onAlarm();
            } else {
                showAddPortfolioTipsDialog(() -> mOperationListener.onAlarm());
            }
            StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_SMART_STARE_ENTRANCE_CLICKED);
        } else if (v == mAddBtn) {
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager == null) {
                return;
            }
            StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_ADD_PORTFOLIO_CLICKED);
            List<StockDbEntity> allStockList = portfolioDataManager.getAllStockList(false, false);
            int size = allStockList.size();
            if (size < DengtaConst.MAX_PORTFOLIO_COUNT) {
                List<GroupEntity> allGroup = portfolioDataManager.getAllGroup(false, false);
                if (allGroup != null && allGroup.size() > 0) {
                    PortfolioGroupManagerActivity.show(getContext(), mDtSecCode, mSecName);
                } else {
                    mOperationListener.onAdd();
                    setIsAdded(true);
                }
            } else {
                DengtaApplication.getApplication().showToast(R.string.no_more_portfolio);
            }
        } else if (v == mRemoveBtn) {
            StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_REMOVE_PORTFOLIO_CLICKED);
            new DelPortDialog(getContext()).showDelDialog(this, getContext().getString(R.string.confirm_del_stock), null);
        } else if (v == mMoreLayout) {
            showMoreOperationDialog();
            mOperationListener.onMore();
        }
    }



    private void showAddPortfolioTipsDialog(Runnable callback) {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            return;
        }
        final List<StockDbEntity> allStockList = portfolioDataManager.getAllStockList(false, false);
        final int size = allStockList == null ? 0 : allStockList.size();
        if (size >= DengtaConst.MAX_PORTFOLIO_COUNT) {
            DengtaApplication.getApplication().showToast(R.string.no_more_portfolio);
            return;
        }

        final CommonDialog dialog = new CommonDialog(mContext);
        dialog.setMessage(R.string.operation_add_portfolio_hint);
        dialog.setTitle(R.string.operation_add_portfolio_title);
        dialog.addButton(R.string.cancel);
        dialog.addButton(R.string.ok);
        dialog.setButtonClickListener((commonDialog, view, position) -> {
            switch (position) {
                case 0:
                    commonDialog.dismiss();
                    break;
                case 1:
                    commonDialog.dismiss();
                    portfolioDataManager.addStock(mDtSecCode, mSecName);
                    callback.run();
                    break;
                default:
                    break;
            }
        });
        dialog.show();
    }

    public void setDtSecCode(String dtSecCode, String secName) {
        mDtSecCode = dtSecCode;
        mSecName = secName;

        if (!StockUtil.supportSecLiveMsg(mDtSecCode)) {
            mLiveMsgLayout.setVisibility(INVISIBLE);
        }

        if (!StockUtil.supportChipEntrance(mDtSecCode)) {
            mChipLayout.setVisibility(INVISIBLE);
        }
    }

    public void setOnOperationListener(final OnOperationListener listener) {
        mOperationListener = listener;
    }

    public void setIsAdded(final boolean isAdded) {
        mIsAdded = isAdded;
        if (mIsAdded) {
            mAddBtn.setVisibility(GONE);
            mRemoveBtn.setVisibility(VISIBLE);
        } else {
            mAddBtn.setVisibility(VISIBLE);
            mRemoveBtn.setVisibility(GONE);
        }
    }

    public void showLiveMsgRedDot(boolean show) {
        if (show) {
            mLiveRedDot.setVisibility(VISIBLE);
        } else {
            mLiveRedDot.setVisibility(INVISIBLE);
        }
    }

    public interface OnOperationListener {
        void onLiveMsg();
        void onShare();
        void onFigure();
        void onChip();
        void onAlarm();
        void onAdd();
        void onRemove();
        void onMore();
    }

    public void refreshAlarmBar() {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            return;
        }
        boolean isAdd = portfolioDataManager == null ? false : portfolioDataManager.isPortfolio(mDtSecCode);
        if (isAdd != mIsAdded) {
            mIsAdded = isAdd;
            setIsAdded(mIsAdded);
        }
    }

    private void showMoreOperationDialog() {
        // line1
        final ArrayList<ToolsItem> line1Items = new ArrayList<>();
        line1Items.add(MoreOperationToolsItem.createShareItem());
        if (StockUtil.supportFigure(mDtSecCode)) {
            line1Items.add(MoreOperationToolsItem.createStockFigureItem());
        }
        line1Items.add(MoreOperationToolsItem.createCommentItem());
        line1Items.add(MoreOperationToolsItem.createAdd2GroupItem());
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager != null) {
            final String memo = portfolioDataManager.getComment(mDtSecCode);
            line1Items.add(MoreOperationToolsItem.createMemoItem(!TextUtils.isEmpty(memo)));
        }
        line1Items.add(MoreOperationToolsItem.createKLineSettingItem());

        // line2
        final ArrayList<ToolsItem> line2Items = new ArrayList<>();
        if (StockUtil.isDirectorAddStock(mSecInfo)) {
            line2Items.add(MoreOperationToolsItem.createDirectionAddNuggetsItem());
        }
        if(StockUtil.isMarginStock(mSecInfo)) {
            line2Items.add(MoreOperationToolsItem.createMarginTrackingItem());
        }
        if (StockUtil.supportBSPoint(mDtSecCode)) {
            line2Items.add(MoreOperationToolsItem.createBSItem());
        }
        if (StockUtil.supportSimilarK(mDtSecCode)) {
            line2Items.add(MoreOperationToolsItem.createSimilarKItem());
        }
        if (StockUtil.supportSecHistory(mDtSecCode)) {
            line2Items.add(MoreOperationToolsItem.createSecHistoryItem());
        }

        new MoreOperationDialog(mContext, line1Items, line2Items, mOperationListener, mDtSecCode, mSecName).show();
    }
}
