package com.sscf.investment.detail.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dengtacj.component.entity.db.FavorItem;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.StockUtil;
import com.sscf.investment.widget.AttitudeColorView;
import java.util.ArrayList;
import java.util.List;
import BEC.E_MARKET_TYPE;
import BEC.E_NEWS_TYPE;
import BEC.NewsDesc;
import BEC.TagInfo;

/**
 * Created by davidwei on 2017/11/28
 */
public final class NewsListAdapter extends BaseAdapter implements View.OnClickListener {
    private ArrayList<String> mClickedNewsIDs = new ArrayList<>();

    private int mNewsType;

    private final Context mContext;
    private List<NewsDesc> mNewsListData;
    private LayoutInflater mInflater;

    private String mSecUniCode;
    private boolean mIsHongKongOrUsa;
    private boolean mIsEmbeded;

    private int mColorBase;
    private int mColorTitle;

    private int mColorRelatedStock;
    private int mRelatedStockTextSize;
    private int mTagMargin;

    public NewsListAdapter(Context context, boolean isEmbeded, String secUnicode, int newsType) {
        mNewsType = newsType;
        mContext = context;
        mSecUniCode = secUnicode;
        mIsHongKongOrUsa = StockUtil.isHongKongOrUSA(mSecUniCode);
        mIsEmbeded = isEmbeded;

        initResources();
    }

    private void initResources() {
        Resources resources = DengtaApplication.getApplication().getResources();
        mColorTitle = ContextCompat.getColor(mContext, R.color.default_text_color_100);
        mColorBase = ContextCompat.getColor(mContext, R.color.default_text_color_60);

        mColorRelatedStock = resources.getColor(R.color.tag_relatedStock);
        mRelatedStockTextSize = resources.getDimensionPixelSize(R.dimen.font_size_12);
        mTagMargin = resources.getDimensionPixelSize(R.dimen.tag_margin);
    }

    public void setListData(List<NewsDesc> newsDesc) {
//            mClickedNewsIDs = new ArrayList<>();

        mNewsListData = newsDesc;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getCount() {
        if (mNewsListData != null) {
            return mNewsListData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mNewsListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return null;
        }

        NewsDesc newsDesc = mNewsListData.get(position);

        if (mInflater == null) {
            mInflater = LayoutInflater.from(mContext);
        }

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.news_item, null);
            if (mIsEmbeded) { //TODO 为了出现点击的ripple效果，暂时注释掉这一行，会导致GPU过度绘制，以后再解决
//                    convertView.setBackground(mBgTransparent);
            }
            viewHolder = new ViewHolder();
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mSummary = (TextView) convertView.findViewById(R.id.summary);
            viewHolder.mTime = (TextView) convertView.findViewById(R.id.news_time);
            viewHolder.mTime.setTextColor(mColorBase);
            viewHolder.mSource = (TextView) convertView.findViewById(R.id.news_source);
            viewHolder.mAttitude = (AttitudeColorView) convertView.findViewById(R.id.attitude_view);

            viewHolder.mPosition = position;
            convertView.setTag(viewHolder);
            convertView.setOnClickListener(this);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
        }

        viewHolder.mTitle.setText(newsDesc.getSTitle());
        if (mClickedNewsIDs.contains(newsDesc.getSNewsID())) {
            viewHolder.mTitle.setTextColor(mColorBase);
        } else {
            viewHolder.mTitle.setTextColor(mColorTitle);
        }

        String description = newsDesc.getSDescription();
        if (!TextUtils.isEmpty(description)) {
            viewHolder.mSummary.setText(description);
            viewHolder.mSummary.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mSummary.setVisibility(View.GONE);
        }

        String timeStr;
        if (mNewsType == E_NEWS_TYPE.NT_NEWS && !mIsHongKongOrUsa) {
            timeStr = TimeUtils.timeStamp2Date(newsDesc.getITime() * DengtaConst.MILLIS_FOR_SECOND);
        } else {
            timeStr = TimeUtils.timeStamp2DateWithoutMinute(newsDesc.getITime() * DengtaConst.MILLIS_FOR_SECOND);
        }
        viewHolder.mTime.setText(timeStr);

        viewHolder.mSource.setText(newsDesc.getSFrom());

        ArrayList<TagInfo> tagInfos = newsDesc.getVtTagInfo();
        viewHolder.mAttitude.setData(tagInfos);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mNewsListData == null || mNewsListData.size() == 0;
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();
        int position = viewHolder.mPosition;
        NewsDesc item = (NewsDesc) getItem(position);

        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
        long accountId = 0;
        if (accountInfo != null) {
            accountId = accountInfo.id;
        }

        final FavorItem favorItem = new FavorItem(accountId, item.sNewsID, item.eNewsType, item.sTitle,
                item.sDtInfoUrl, DengtaConst.MILLIS_FOR_SECOND * item.iTime);

        WebBeaconJump.showWebActivity(mContext, item.getSDtInfoUrl(), favorItem);

        mClickedNewsIDs.add(item.getSNewsID());
        viewHolder.mTitle.setTextColor(mColorBase);

        int marketType = StockUtil.getMarketType(mSecUniCode);
        if (marketType == E_MARKET_TYPE.E_MT_SZ || marketType == E_MARKET_TYPE.E_MT_SH) {
            if (mNewsType == E_NEWS_TYPE.NT_NEWS) {//新闻
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_NEWS_CLICKED);
            } else if (mNewsType == E_NEWS_TYPE.NT_ANNOUNCEMENT) {//公告
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_ANNOUNCEMENT_CLICKED);
            } else if (mNewsType == E_NEWS_TYPE.NT_REPORT) {//研报
                StatisticsUtil.reportAction(StatisticsConst.SECURITY_DETAIL_REPORT_CLICKED);
            }
        }
    }
}

final class ViewHolder {
    public TextView mTitle;
    public TextView mSummary;
    public TextView mTime;
    public TextView mSource;
    public AttitudeColorView mAttitude;
    public int mPosition;
}