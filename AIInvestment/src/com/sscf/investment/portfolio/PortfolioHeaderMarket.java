package com.sscf.investment.portfolio;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.callback.OnGetDataCallback;
import com.dengtacj.component.managers.IDataCacheManager;
import com.dengtacj.component.managers.IQuoteManager;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AppConfigRequestManager;
import com.sscf.investment.portfolio.widget.HorizontalRecyclerView;
import com.sscf.investment.sdk.entity.SecListItem;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.ProtoManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.SecListItemUtils;
import com.sscf.investment.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import BEC.ConfigDetail;
import BEC.E_CONFIG_TYPE;
import BEC.GetConfigRsp;
import BEC.IndexDetail;
import BEC.IndexListInfo;
import BEC.SecSimpleQuote;

/**
 * Created by xuebinliu on 2015/8/28.
 * 自选股界面行情布局
 */
public final class PortfolioHeaderMarket extends FrameLayout implements Handler.Callback, DataSourceProxy.IRequestCallback,
        HorizontalRecyclerView.OnItemScrollIndexChangedListener, OnGetDataCallback<ArrayList<SecSimpleQuote>> {
    private static final String TAG = PortfolioHeaderMarket.class.getSimpleName();

    private static final int MSG_REFRESH_UI = 1;

    private Handler mUpdateHandler;

    private ArrayList<String> mDtCodes;

    private File mIndexesFile;

    private RecyclerView mRecyclerView;
    private IndexAdapter mAdapter;

    private View mLeftArrow;
    private View mRighArrow;

    public PortfolioHeaderMarket(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRecyclerView = (RecyclerView) findViewById(R.id.indexListView);
        mRecyclerView.setItemAnimator(null);

        mRecyclerView.setOnTouchListener((v, event) -> {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    requestDisallowInterceptTouchEvent(false);
                    break;
                default:
                    break;
            }

            return false;
        });

        mLeftArrow = findViewById(R.id.leftArrow);
        mRighArrow = findViewById(R.id.rightArrow);
        ((HorizontalRecyclerView)mRecyclerView).setOnItemScrollIndexChangedListener(this);

        final Context context = getContext();
        mIndexesFile = FileUtil.getPortfolioIndexesFile(context);

        mUpdateHandler = new Handler(Looper.getMainLooper(), this);

        DengtaApplication.getApplication().defaultExecutor.execute(() -> {
            loadIndexesListFromLocal();
            if (NetUtil.isNetWorkConnected(getContext())) {
                return;
            }
            // 获取到dtcode后需要拉取一次行情
            requestData();
            // 非频繁更新的逻辑，添加时间控制
            final String KEY_LAST_REQUEST_PORFOLIO_HEADER_TIME = "key_last_request_porfolio_header_time";
            final long now = System.currentTimeMillis();
            final long lastRequestTime = SettingPref.getLong(KEY_LAST_REQUEST_PORFOLIO_HEADER_TIME, 0);
            if (now - lastRequestTime > DengtaConst.DAY_FOR_SECOND) {
                SettingPref.putLong(KEY_LAST_REQUEST_PORFOLIO_HEADER_TIME, now);
                AppConfigRequestManager.getPortofolioHeaderIndexesRequest(PortfolioHeaderMarket.this);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void loadIndexesListFromLocal() {
        final ArrayList<IndexDetail> indexInfos = (ArrayList<IndexDetail>) FileUtil.getObjectFromFile(mIndexesFile);
        final int size = indexInfos != null ? indexInfos.size() : 0;
        ArrayList<String> dtCodes = new ArrayList<>();
        final ArrayList<SecSimpleQuote> indexesList = new ArrayList<>();
        SecSimpleQuote secQuote;
        String dtCode;

        final IDataCacheManager dataCacheManager = (IDataCacheManager) ComponentManager.getInstance()
                .getManager(IDataCacheManager.class.getName());

        if (size == 0) { // 从默认配置里获得指数
            if (mIndexesFile.exists() && mIndexesFile.isFile()) {
                mIndexesFile.delete();
            }
            final String[] indexDtSecCodes = getResources().getStringArray(R.array.china_index_market_unicode);
            final String[] indexNames = getResources().getStringArray(R.array.china_index_market_name);
            final int length = Math.min(indexDtSecCodes.length, indexNames.length);

            for (int i = 0; i < length; i++) {
                dtCode = indexDtSecCodes[i];
                secQuote = dataCacheManager != null ? dataCacheManager.getSecSimpleQuote(dtCode) : null;
                if (secQuote == null) {
                    secQuote = new SecSimpleQuote();
                    secQuote.sDtSecCode = dtCode;
                    secQuote.sSecName = indexNames[i];
                }
                dtCodes.add(dtCode);
                indexesList.add(secQuote);
            }
        } else { // 从后台下发的文件里获得指数
            IndexDetail indexInfo;
            for (int i = 0; i < size; i++) {
                indexInfo = indexInfos.get(i);
                dtCode = indexInfo.sDtSecCode;
                secQuote = dataCacheManager != null ? dataCacheManager.getSecSimpleQuote(dtCode) : null;
                if (secQuote == null) {
                    secQuote = new SecSimpleQuote();
                    secQuote.sDtSecCode = dtCode;
                    secQuote.sSecName = indexInfo.sSecName;
                }
                dtCodes.add(dtCode);
                indexesList.add(secQuote);
            }
        }

        mUpdateHandler.obtainMessage(MSG_REFRESH_UI, indexesList).sendToTarget();

        mDtCodes = dtCodes;
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_CONFIG_PORTFOLIO_HEADER_INDEXES:
                if (success) {
                    final ArrayList<ConfigDetail> configs = ((GetConfigRsp) data.getEntity()).getVList();
                    for (ConfigDetail config : configs) {
                        if (config.iType == E_CONFIG_TYPE.E_CONFIG_OPTIONAL_INDEX) {
                            final IndexListInfo indexListInfo = new IndexListInfo();
                            if (ProtoManager.decode(indexListInfo, config.vData)) {
                                final ArrayList<IndexDetail> indexInfos = indexListInfo.vDtSecCode;
                                final int size = indexInfos != null ? indexInfos.size() : 0;
                                if (size > 0) {
                                    FileUtil.saveObjectToFile(indexInfos, mIndexesFile);
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    public void requestData() {
        DtLog.d(TAG, "requestData : mDtCodes = " + mDtCodes);
        if (mDtCodes != null) {
            final IQuoteManager quoteManager = (IQuoteManager) ComponentManager.getInstance()
                    .getManager(IQuoteManager.class.getName());
            if (quoteManager != null) {
                quoteManager.requestSimpleQuote(mDtCodes, this);
            }
        }
    }

    @Override
    public void onGetData(ArrayList<SecSimpleQuote> data) {
        DtLog.d(TAG, "onGetData : data = " + data);
        if (data != null) {
            mUpdateHandler.obtainMessage(MSG_REFRESH_UI, data).sendToTarget();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_REFRESH_UI:
                updateViews((ArrayList<SecSimpleQuote>) msg.obj);
                break;
            default:
                break;
        }
        return false;
    }

    private void updateViews(ArrayList<SecSimpleQuote> data) {
        if (mAdapter == null) {
            final Context context = getContext();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            final IndexAdapter adapter = new IndexAdapter(context, data, R.layout.portfolio_market_item);
            mRecyclerView.setAdapter(adapter);
            mAdapter = adapter;
            mRecyclerView.scrollToPosition(SettingPref.getInt(SettingConst.KEY_PORTFOLIO_INDEX_INDEX_CHANGE, 0));
        } else {
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemScrollIndexChanged(final int index) {
        SettingPref.putInt(SettingConst.KEY_PORTFOLIO_INDEX_INDEX_CHANGE, index);
    }

    @Override
    public void onStartItem(final boolean b) {
        mLeftArrow.setEnabled(!b);
    }

    @Override
    public void onEndItem(final boolean b) {
        mRighArrow.setEnabled(!b);
    }
}

final class IndexAdapter extends CommonBaseRecyclerViewAdapter<SecSimpleQuote> {
    private final ArrayList<SecListItem> mSecList;

    IndexAdapter(Context context, List<SecSimpleQuote> data, int itemLayoutId) {
        super(context, data, itemLayoutId);
        mSecList = SecListItemUtils.getSecListFromSecSimpleQuoteList(data);
        setItemClickable(true);
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(final ViewGroup parent, final int position) {
        final CommonRecyclerViewHolder holder = super.onCreateViewHolder(parent, position);
        final RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(parent.getWidth() / 3, ViewGroup.LayoutParams.MATCH_PARENT);
        holder.itemView.setLayoutParams(params);
        return holder;
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, SecSimpleQuote item, int position) {
        final SecSimpleQuote secQuote = getItem(position);
        if (secQuote != null) {
            holder.setText(R.id.portfolio_market_item_stock_name, secQuote.sSecName);

            TextAppearanceSpan span;

            final ImageView updownArrow = holder.getView(R.id.updownArrow);
            if (secQuote.fNow > 0 && secQuote.fClose > 0) {
                final float delta = secQuote.fNow - secQuote.fClose;
                if (delta > 0) {
                    span = StringUtil.getUpStyle();
                    updownArrow.setImageResource(R.drawable.price_up_arrow);
                    updownArrow.setVisibility(View.VISIBLE);
                } else if (delta < 0) {
                    span = StringUtil.getDownStyle();
                    updownArrow.setImageResource(R.drawable.price_down_arrow);
                    updownArrow.setVisibility(View.VISIBLE);
                } else {
                    span = StringUtil.getSuspensionStyle();
                    updownArrow.setVisibility(View.GONE);
                }
            } else {
                span = StringUtil.getSuspensionStyle();
                updownArrow.setVisibility(View.GONE);
            }

            final float now = secQuote.fNow > 0f ? secQuote.fNow : secQuote.fClose;

            final SpannableString indexText = new SpannableString(now > 0 ? StringUtil.getFormattedFloat(now, secQuote.iTpFlag) : "--");
            indexText.setSpan(span, 0, indexText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.setText(R.id.portfolio_market_item_stock_price, indexText);

            holder.setText(R.id.portfolio_market_item_stock_change, StringUtil.getUpDownStringSpannable(secQuote.fNow, secQuote.fClose));
        }
    }

    @Override
    protected void onItemClick(View v, CommonRecyclerViewHolder holder, int position) {
        final SecSimpleQuote secQuote = getItem(position);
        if (secQuote == null) {
            return;
        }
        String sSecName = secQuote.sSecName;

        if("上证指数".equals(sSecName)){
            StatisticsUtil.reportAction(StatisticsConst.A_AD_INDEX_DISPLAY_SHANGZHENG);
        }else if("深证成指".equals(sSecName)){
            StatisticsUtil.reportAction(StatisticsConst.A_AD_INDEX_DISPLAY_SHENZHENG);
        }else  if("创业板指".equals(sSecName)){
            StatisticsUtil.reportAction(StatisticsConst.A_AD_INDEX_DISPLAY_CHUANGYE);
        }
        CommonBeaconJump.showSecurityDetailActivity(mContext,
                secQuote.sDtSecCode, secQuote.sSecName, mSecList);
    }
}
