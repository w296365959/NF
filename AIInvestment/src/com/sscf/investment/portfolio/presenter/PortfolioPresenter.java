package com.sscf.investment.portfolio.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnLocalDataUpdateCallback;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.entity.db.GroupEntity;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.component.managers.IQuoteManager;
import com.sscf.investment.R;
import com.sscf.investment.comparator.StockDbEntityComparator;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.portfolio.PortfolioHeaderMarket;
import com.sscf.investment.portfolio.PortfolioStockFragment;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.widget.ConfirmCheckBoxDialog;
import com.sscf.investment.widget.ConfirmDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017-10-09.
 *
 */
public final class PortfolioPresenter implements Runnable, Handler.Callback,
        OnGetDataCallback<ArrayList<SecSimpleQuote>>, OnLocalDataUpdateCallback {
    private static final String TAG = PortfolioPresenter.class.getSimpleName();
    private static final int PERLOAD_NUM = 10;

    private static final int MSG_UPDATE_QUOTE = 1;
    private static final int MSG_UPDATE_LIST = 2;

    public static final int CHANGE_STATE_PERCENT = 0;       // 涨跌幅
    public static final int CHANGE_STATE_PRICE = 1;         // 涨跌额
    public static final int CHANGE_STATE_MARKET_VALUE = 2;  // 总市值
    private int mChangeState = CHANGE_STATE_PERCENT;

    private static final int SORT_TYPE_NONE = 0;    // 无排序
    private static final int SORT_TYPE_ASC = 1;      // 升序
    private static final int SORT_TYPE_DEC = 2;      // 降序
    private int mSortTypePrice = SORT_TYPE_NONE;     // 按价格排序
    private int mSortTypeUpDown = SORT_TYPE_NONE;    // 按涨跌幅排序

    private final PortfolioStockFragment mFragment;
    private PortfolioHeaderMarket mPortfolioHeaderMarket;

    private final IPortfolioDataManager mPortfolioDataManager;
    private final Handler mHandler;

    // 当前自选股票全集
    private List<StockDbEntity> mCurrentStockList;
    // 用于排序的
    private List<StockDbEntity> mCurrentSortStockList;

    private final PeriodicHandlerManager mPeriodicHandlerManager;

    public PortfolioPresenter(PortfolioStockFragment fragment, PortfolioHeaderMarket portfolioHeaderMarket) {
        mFragment = fragment;
        mPortfolioHeaderMarket = portfolioHeaderMarket;
        mHandler = new Handler(Looper.getMainLooper(), this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
        mPortfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
    }

    public void release() {
    }

    public void init() {
        if (mPortfolioDataManager != null) {
            // 初始化自选股数据
            DengtaApplication.getApplication().defaultExecutor.execute(this::initAsync);
        }
    }

    private void initAsync() {
        mCurrentStockList = mPortfolioDataManager.getAllStockFromCurrentGroup(false, true);
        mHandler.obtainMessage(MSG_UPDATE_LIST, mCurrentStockList).sendToTarget();
        refresh();
    }

    /**
     * 更新自选列表
     */
    public void updateData() {
        if (mPortfolioDataManager == null) {
            return;
        }

        // 分组发生变化，重新拉取数据
        mCurrentStockList = mPortfolioDataManager.getAllStockFromCurrentGroup(false, true);
        mHandler.obtainMessage(MSG_UPDATE_LIST, mCurrentStockList).sendToTarget();
        refresh();
    }

    /**
     * 开始刷新行情
     */
    public void refresh() {
        mPeriodicHandlerManager.runPeriodic();
    }

    /**
     * 停止刷新行情
     */
    public void stopRefresh() {
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void run() {
        if (mCurrentSortStockList == null) { // 没有排序的时候，拉取可见区域行情
            requestVisibleQuoteData();
        } else { // 有排序的时候，拉取全部自选股的行情
            requestAllQuoteData();
        }
        final int firstPosition = mFragment.getFirstVisiblePosition();
        if (firstPosition <= 10) { // 看不到指数就不拉取
            mPortfolioHeaderMarket.requestData();
        }
        mPeriodicHandlerManager.setDelay(DengtaSettingPref.getRefreshDelaySenconds());
    }

    private void requestAllQuoteData() {
        final IQuoteManager quoteManager = (IQuoteManager) ComponentManager.getInstance()
                .getManager(IQuoteManager.class.getName());
        if (quoteManager == null) {
            return;
        }

        final List<StockDbEntity> stockList = mCurrentStockList;
        final int size = stockList == null ? 0 : stockList.size();
        if (size <= 0) {
            return;
        }

        final ArrayList<String> dtCodes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dtCodes.add(stockList.get(i).getDtSecCode());
        }

        quoteManager.requestSimpleQuote(dtCodes, this);
    }

    private void requestVisibleQuoteData() {
        final IQuoteManager quoteManager = (IQuoteManager) ComponentManager.getInstance()
                .getManager(IQuoteManager.class.getName());
        if (quoteManager == null) {
            return;
        }

        int firstPosition = mFragment.getFirstVisiblePosition();
        int lastPosition = mFragment.getLastVisiblePosition();
        DtLog.d(TAG, "requestVisibleQuoteData : firstPosition = " + firstPosition + " , lastPosition = " + lastPosition);
        if (lastPosition <= 0) {
            return;
        }

        final List<StockDbEntity> stockList = mCurrentStockList;
        final int size = stockList == null ? 0 : stockList.size();
        if (size <= 0) {
            return;
        }
        firstPosition -= PERLOAD_NUM;
        if (firstPosition < 0 || firstPosition >= size) {
            firstPosition = 0;
        }
        lastPosition += PERLOAD_NUM;
        if (lastPosition >= size) {
            lastPosition = size - 1;
        }
        final ArrayList<String> dtCodes = new ArrayList<>(lastPosition - firstPosition + 1);
        for (int i = firstPosition; i <= lastPosition; i++) {
            dtCodes.add(stockList.get(i).getDtSecCode());
        }

        quoteManager.requestSimpleQuote(dtCodes, this);
    }

    /**
     * 行情回调
     */
    @Override
    public void onGetData(ArrayList<SecSimpleQuote> data) {
        DtLog.d(TAG, "onGetData : data = " + data);
        if (data != null) {
            final List<StockDbEntity> stockList = mCurrentStockList;
            if (stockList != null) {
                for (SecSimpleQuote quote : data) {
                    in:
                    for (StockDbEntity entity : stockList) {
                        if (TextUtils.equals(quote.sDtSecCode, entity.getDtSecCode())) {
                            entity.updateData(quote);
                            break in;
                        }
                    }
                }
                mHandler.sendEmptyMessage(MSG_UPDATE_QUOTE);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_QUOTE:
                if (mCurrentSortStockList == null) { // 如果没有排序就直接刷新adapter
                    mFragment.updateQuote();
                } else { // 如果有排序就重新排序
                    mHandler.obtainMessage(MSG_UPDATE_LIST, mCurrentStockList).sendToTarget();
                }
                break;
            case MSG_UPDATE_LIST:
                final List<StockDbEntity> stockList = (List<StockDbEntity>) msg.obj;
                final Comparator<StockDbEntity> comparator = getComparator();
                if (comparator != null) {
                    final List<StockDbEntity> stockSortList = new ArrayList<>(stockList);
                    Collections.sort(stockSortList, comparator);
                    mFragment.updatePortfolioList(stockSortList, mChangeState);
                    mCurrentSortStockList = stockSortList;
                } else {
                    mFragment.updatePortfolioList(stockList, mChangeState);
                    mCurrentSortStockList = null;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private Comparator<StockDbEntity> getComparator() {
        Comparator<StockDbEntity> comparator = null;
        switch (mSortTypePrice) {
            case SORT_TYPE_DEC:
                comparator = new StockDbEntityComparator(StockDbEntityComparator.SORT_TYPE.PRICE, false);
                break;
            case SORT_TYPE_ASC:
                comparator = new StockDbEntityComparator(StockDbEntityComparator.SORT_TYPE.PRICE, true);
                break;
            default:
                break;
        }
        if (mSortTypeUpDown != SORT_TYPE_NONE) {
            final boolean asc = mSortTypeUpDown == SORT_TYPE_ASC;
            StockDbEntityComparator.SORT_TYPE sortType = StockDbEntityComparator.SORT_TYPE.UP_DOWN_PERCENT;
            switch (mChangeState) {
                case CHANGE_STATE_PERCENT:
                    sortType = StockDbEntityComparator.SORT_TYPE.UP_DOWN_PERCENT;
                    break;
                case CHANGE_STATE_PRICE:
                    sortType = StockDbEntityComparator.SORT_TYPE.UP_DOWN_PRICE;
                    break;
                case CHANGE_STATE_MARKET_VALUE:
                    sortType = StockDbEntityComparator.SORT_TYPE.TOTOAL_MARKET_VALUE;
                    break;
                default:
                    break;
            }
            comparator = new StockDbEntityComparator(sortType, asc);
        }
        return comparator;
    }

    public void showDeleteDialog(final Activity activity, final StockDbEntity entity) {
        final IPortfolioDataManager portfolioDataManager = mPortfolioDataManager;
        if (portfolioDataManager == null) {
            return;
        }

        final ArrayList<String> delArray = new ArrayList<>(1);
        delArray.add(entity.getDtSecCode());

        final GroupEntity groupEntity = portfolioDataManager.getCurrentGroup();

        if (groupEntity == null) { // 默认分组
            final ConfirmDialog deleteDialog = new ConfirmDialog(activity);
            deleteDialog.setMessage(R.string.portfolio_edit_confirm_delete_tips1);
            deleteDialog.setOkButton(R.string.delete, v -> {
                final List<StockDbEntity> currentStockList = mCurrentStockList;
                if (currentStockList != null) {
                    if (currentStockList.remove(entity)) {
                        mFragment.updateItemPositionState();
                    }
                }
                final List<StockDbEntity> currentSortStockList =  mCurrentSortStockList;
                if (currentSortStockList != null) {
                    currentSortStockList.remove(entity);
                }
                deleteStockItems(delArray, true);
                deleteDialog.dismiss();
            });
            deleteDialog.show();
        } else {
            final ConfirmCheckBoxDialog deleteDialog = new ConfirmCheckBoxDialog(activity);
            deleteDialog.setMessage(R.string.portfolio_edit_confirm_delete_tips2);
            deleteDialog.setCheckBoxText(R.string.portfolio_edit_confirm_delete_tips3);
            deleteDialog.setOkButton(R.string.delete, v -> {
                final List<StockDbEntity> currentStockList = mCurrentStockList;
                if (currentStockList != null) {
                    if (currentStockList.remove(entity)) {
                        mFragment.updateItemPositionState();
                    }
                }
                final List<StockDbEntity> currentSortStockList =  mCurrentSortStockList;
                if (currentSortStockList != null) {
                    currentSortStockList.remove(entity);
                }
                deleteStockItems(delArray, deleteDialog.isCheck());
                deleteDialog.dismiss();
            });
            deleteDialog.show();
        }
    }

    private void deleteStockItems(final ArrayList<String> delArray, boolean deleteAll) {
        if (deleteAll) {
            mPortfolioDataManager.deleteStockFromAllGroup(delArray, true);
        } else {
            mPortfolioDataManager.deletePortfolioStockFromCurrentGroup(delArray, true);
        }
    }

    public void changePosition(int from, int to) {
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            return;
        }
        if (from != to) {
            final List<StockDbEntity> currentStockList = mCurrentStockList;
            final int min = Math.min(from, to);
            final int max = Math.max(from, to) + 1;
            final int itemSize = currentStockList.size();

            if (min >= itemSize || max > itemSize) {
                return;
            }

            final int size = max - min;

            ArrayList<Integer> changedBeforeTimes = new ArrayList<>();

            for (StockDbEntity item : currentStockList.subList(min, max)) {
                changedBeforeTimes.add(item.getIUpdateTime());
            }

            StockDbEntity item = currentStockList.remove(from);
            currentStockList.add(to, item);

            ArrayList<StockDbEntity> changedAfterItems = new ArrayList<>(size);
            changedAfterItems.addAll(currentStockList.subList(min, max));

            for (int i = 0; i < size; i++) {
                changedAfterItems.get(i).setIUpdateTime(changedBeforeTimes.get(i));
            }

            portfolioDataManager.updateStockList(changedAfterItems);
            updateData();
        }
    }

    /**
     * 点击持仓
     */
    public void clickPosition(final StockDbEntity entity) {
        if (mPortfolioDataManager == null) {
            return;
        }
        final boolean position = !entity.isPosition;
        entity.isPosition = position;
        mPortfolioDataManager.updatePositionStatus(entity.getDtSecCode(), position);
        mFragment.updateItemPositionState();
    }

    public void clickHeaderPrice() {
        int drawableId;
        switch (mSortTypePrice) {
            case SORT_TYPE_NONE:
                mSortTypePrice = SORT_TYPE_DEC;
                drawableId = R.drawable.arrow_down;
                break;
            case SORT_TYPE_DEC:
                mSortTypePrice = SORT_TYPE_ASC;
                drawableId = R.drawable.arrow_up;
                break;
            case SORT_TYPE_ASC:
                mSortTypePrice = SORT_TYPE_NONE;
                drawableId = R.drawable.right_corner;
                break;
            default:
                return;
        }
        mFragment.updateHeadPriceArrowIcon(drawableId);
        // 涨跌幅排序状态失效
        mSortTypeUpDown = SORT_TYPE_NONE;
        mFragment.updateHeadUpDownArrowIcon(R.drawable.right_corner);
        mHandler.obtainMessage(MSG_UPDATE_LIST, mCurrentStockList).sendToTarget();
    }

    public void clickHeadUpdown() {
        int drawableId;
        switch (mSortTypeUpDown) {
            case SORT_TYPE_NONE:
                mSortTypeUpDown = SORT_TYPE_DEC;
                drawableId = R.drawable.arrow_down;
                break;
            case SORT_TYPE_DEC:
                mSortTypeUpDown = SORT_TYPE_ASC;
                drawableId = R.drawable.arrow_up;
                break;
            case SORT_TYPE_ASC:
                mSortTypeUpDown = SORT_TYPE_NONE;
                drawableId = R.drawable.right_corner;
                break;
            default:
                return;
        }
        mFragment.updateHeadUpDownArrowIcon(drawableId);
        // 价格排序状态失效
        mSortTypePrice = SORT_TYPE_NONE;
        mFragment.updateHeadPriceArrowIcon(R.drawable.right_corner);
        mHandler.obtainMessage(MSG_UPDATE_LIST, mCurrentStockList).sendToTarget();
    }

    public boolean isSortingList() {
        return !(mSortTypePrice == SORT_TYPE_NONE && mSortTypeUpDown == SORT_TYPE_NONE);
    }

    public void clickItemChange() {
        switch (mChangeState) {
            case CHANGE_STATE_PERCENT:
                mChangeState++;
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_CHANGE_UPDOWN_PRICE);
                break;
            case CHANGE_STATE_PRICE:
                mChangeState++;
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_CHANGE_TOTOAL_MARKET_VALUE);
                break;
            case CHANGE_STATE_MARKET_VALUE:
                mChangeState = CHANGE_STATE_PERCENT;
                StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_STOCK_CLICK_CHANGE_UPDOWN_PERCENT);
                break;
            default:
                DtLog.w(TAG, "onClick press change");
                return;
        }
        mHandler.obtainMessage(MSG_UPDATE_LIST, mCurrentStockList).sendToTarget();
        mFragment.updateHeadChangeText(mChangeState);
    }

    @Override
    public void onDataUpdate() {
        updateData();
    }
}
