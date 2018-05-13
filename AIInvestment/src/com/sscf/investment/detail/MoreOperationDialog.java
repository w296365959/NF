package com.sscf.investment.detail;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ActivityUtils;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.component.ui.widget.ToolsItem;
import com.sscf.investment.detail.view.OperationBar;
import com.sscf.investment.interpolator.ExpoEaseIn;
import com.sscf.investment.interpolator.ExpoEaseOut;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.portfolio.PortfolioGroupManagerActivity;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import java.util.List;

/**
 * Created by liqf on 2016/8/1.
 */
public final class MoreOperationDialog extends Dialog implements View.OnClickListener, DialogInterface.OnShowListener {
    private static final long ANIMATION_DURATION = 200;
    private final View contentFrame;
    private final View contentView;

    public MoreOperationDialog(Context context, List<ToolsItem> line1Items, List<ToolsItem> line2Items,
                               OperationBar.OnOperationListener operationListener, final String dtSecCode, final String secName) {
        super(context, R.style.dialog_share_theme);
        DeviceUtil.enableDialogTranslucentStatus(getWindow(), ContextCompat.getColor(context, R.color.black_40));
        setContentView(R.layout.dialog_more_operation);
        contentFrame = findViewById(R.id.more_operation_dialog_frame);
        contentFrame.setOnClickListener(this);
        contentView = findViewById(R.id.more_operation_dialog_layout);
        findViewById(R.id.cancel_button).setOnClickListener(this);
        setOnShowListener(this);
        initContentPanel(context, operationListener, line1Items, line2Items, dtSecCode, secName);
    }

    private void initContentPanel(Context context, OperationBar.OnOperationListener operationListener,
                                  final List<ToolsItem> line1Items, final List<ToolsItem> line2Items,
                                  final String dtSecCode, final String secName) {
        final RecyclerView mRecyclerView1 = (RecyclerView) findViewById(R.id.more_operation_line1);
        if(!line1Items.isEmpty()) {
            mRecyclerView1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView1.setAdapter(new MoreOpearationAdapter(context, line1Items, operationListener, dtSecCode, secName, this));
        } else {
            mRecyclerView1.setVisibility(View.GONE);
        }

        final RecyclerView mRecyclerView2 = (RecyclerView) findViewById(R.id.more_operation_line2);
        if(!line2Items.isEmpty()) {
            mRecyclerView2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView2.setAdapter(new MoreOpearationAdapter(context, line2Items, operationListener, dtSecCode, secName, this));
        } else {
            mRecyclerView2.setVisibility(View.GONE);
        }

        if(line1Items.isEmpty() || line2Items.isEmpty()) {
            findViewById(R.id.center_line).setVisibility(View.GONE);
        }
    }

    private void dismissWithAnimation(final View contentFrame, View contentView) {
        final ValueAnimator animatorCollapse = ObjectAnimator.ofFloat(contentView, "translationY", 0, contentView.getMeasuredHeight());
        animatorCollapse.setDuration(ANIMATION_DURATION);
        animatorCollapse.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        animatorCollapse.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                final Context context = getContext();
                if (ActivityUtils.isActivityDestroy(context)) {
                    return;
                }

                if (isShowing()) {
                    dismiss();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorCollapse.start();

        final ValueAnimator alphaAnimator = ValueAnimator.ofFloat(255, 0).setDuration(ANIMATION_DURATION);
        alphaAnimator.setEvaluator(new ExpoEaseOut(ANIMATION_DURATION));
        alphaAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            contentFrame.getBackground().setAlpha((int) value);
        });
        alphaAnimator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
            case R.id.more_operation_dialog_frame:
                dismissWithAnimation(contentFrame, contentView);
                break;
            default:
                break;
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        final ValueAnimator animatorExpand = ObjectAnimator.ofFloat(contentView, "translationY", contentView.getMeasuredHeight(), 0);
        animatorExpand.setDuration(ANIMATION_DURATION);
        animatorExpand.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
        animatorExpand.start();

        final ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0, 255).setDuration(ANIMATION_DURATION);
        alphaAnimator.setEvaluator(new ExpoEaseIn(ANIMATION_DURATION));
        alphaAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            contentFrame.getBackground().setAlpha((int) value);
        });
        alphaAnimator.start();
    }
}

final class MoreOpearationAdapter extends CommonBaseRecyclerViewAdapter<ToolsItem> {
    private final OperationBar.OnOperationListener mOperationListener;
    private final String mDtSecCode;
    private final String mSecName;
    private final Dialog mDialog;

    MoreOpearationAdapter(final Context context, final List<ToolsItem> items, final OperationBar.OnOperationListener operationListener,
                                 final String dtSecCode, final String secName, final Dialog dialog) {
        super(context, items, R.layout.dialog_more_operation_item);
        mOperationListener = operationListener;
        mDtSecCode = dtSecCode;
        mSecName = secName;
        mDialog = dialog;
        setItemClickable(true);
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, ToolsItem item, int position) {
        final TextView textView = holder.getView(R.id.textView);
        textView.setText(item.textId);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, item.drawableId, 0, 0);
        holder.getView(R.id.newIcon).setVisibility(item.isNew ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        final ToolsItem item = getItem(position);
        if (item == null) {
            return;
        }

        switch (item.textId) {
            case R.string.operation_comment:
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_COMMENT_CLICKED);
                CommonBeaconJump.showCommentList(mContext, mDtSecCode, mSecName);
                break;
            case R.string.operation_similar_k:
                WebBeaconJump.showSimilarKLine(mContext, mDtSecCode, mSecName);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_SIMILAR_K_CLICKED);
                break;
            case R.string.operation_margin_tracking:
                WebBeaconJump.showMarginTracking(mContext, mDtSecCode, mSecName);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_MARGIN_TRACKING_CLICKED);
                break;
            case R.string.operation_history:
                WebBeaconJump.showSecHistory(mContext, mDtSecCode, mSecName);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_HISTORY_CLICKED);
                break;
            case R.string.operation_memo_edit:
            case R.string.operation_memo_add:
                if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
                    final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                            .getManager(IPortfolioDataManager.class.getName());
                    if (portfolioDataManager != null) {
                        final boolean isAdded = portfolioDataManager.isPortfolio(mDtSecCode);
                        if (isAdded) {
                            MemoEditActivity.show(mContext, mDtSecCode, mSecName);
                        } else {
                            showAddPortfolioTipsDialog(() -> MemoEditActivity.show(mContext, mDtSecCode, mSecName));
                        }
                    }
                } else {
                    CommonBeaconJump.showLogin(mContext);
                }
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_MEMO_CLICKED);
                break;
            case R.string.operation_figure:
                mOperationListener.onFigure();
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_FIGURE_CLICKED);
                break;
            case R.string.operation_share:
                mOperationListener.onShare();
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_SHARE_CLICKED);
                break;
            case R.string.operation_kline_setting:
                CommonBeaconJump.showKLineSetting(mContext);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_KLINE_SETTING_CLICKED);
                break;
            case R.string.operation_add_group:
                final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                        .getManager(IPortfolioDataManager.class.getName());
                if (portfolioDataManager == null) {
                    break;
                }
                final List<StockDbEntity> allStockList = portfolioDataManager.getAllStockList(false, false);
                final int size = allStockList == null ? 0 : allStockList.size();
                if (size >= DengtaConst.MAX_PORTFOLIO_COUNT) {
                    DengtaApplication.getApplication().showToast(R.string.no_more_portfolio);
                    break;
                }
                PortfolioGroupManagerActivity.show(mContext, mDtSecCode, mSecName);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_ADD_TO_GROUP_CLICKED);
                break;
            case R.string.operation_dk:
                WebBeaconJump.showBS(mContext, mDtSecCode, mSecName);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_BS_SIGNAL_CLICKED);
                break;
            case R.string.direction_add_nuggets:
                WebBeaconJump.showDirectionAddDetail(mContext, mDtSecCode, mSecName);
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_MORE_OPERATION_INVESTMENT_DIRECTION_ADD_NUGGETS);
                break;
            default:
                break;
        }

        mDialog.dismiss();
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
}
