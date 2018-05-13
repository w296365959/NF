package com.sscf.investment.detail.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sscf.investment.R;
import com.dengtacj.component.router.CommonBeaconJump;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import BEC.IndustryCompare;
import BEC.IndustryCompareItem;

/**
 * Created by yorkeehuang on 2017/6/30.
 */

public class IndustryBenchmarkView extends RelativeLayout implements View.OnClickListener {

    private List<TextView> mTabs = new ArrayList<>(4);
    private TextView mUpdateDateView;
    private LinearLayout mEmptyView;

    private List<IndustryBenchmarkData> mOperationRevenueList = Collections.EMPTY_LIST;
    private List<IndustryBenchmarkData> mNetMarginList = Collections.EMPTY_LIST;
    private List<IndustryBenchmarkData> mPerShareEarningList = Collections.EMPTY_LIST;
    private List<IndustryBenchmarkData> mPeRatio = Collections.EMPTY_LIST;

    private ArrayList<IndustryCompare> mIndustryCompareList;

    private String mDtSecCode;
    private String mSecName;

    private static final int TAB_OPERATION_REVENUE = 0;
    private static final int TAB_NET_MARGIN = 1;
    private static final int TAB_PER_SHARE_EARNING = 2;
    private static final int TAB_PE_RATIO = 3;

    private int mCurrentTab = 0;

    private LinearLayout mIndustryBenchmarkList;

    private View.OnClickListener mIndustryBenchmarkItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if(tag != null && tag instanceof IndustryBenchmarkData) {
                IndustryBenchmarkData data = (IndustryBenchmarkData) tag;
                CommonBeaconJump.showSecurityDetailActivity(getContext(), data.secCode, data.name);
            }
        }
    };

    public IndustryBenchmarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCurrentStockInfo(String dtSecCode, String secName) {
        mDtSecCode = dtSecCode;
        mSecName = secName;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mIndustryBenchmarkList = (LinearLayout) findViewById(R.id.industry_benchmark_list);
        mEmptyView = (LinearLayout) findViewById(R.id.industry_benchmark_empty);
        mUpdateDateView = (TextView) findViewById(R.id.industry_benchmark_update_date);
        mTabs.add((TextView) findViewById(R.id.tab0));
        mTabs.add((TextView) findViewById(R.id.tab1));
        mTabs.add((TextView) findViewById(R.id.tab2));
        mTabs.add((TextView) findViewById(R.id.tab3));

        mCurrentTab = 0;
        mTabs.get(mCurrentTab).setSelected(true);
        for(int i=0, size=mTabs.size(); i<size; i++) {
            TextView tab = mTabs.get(i);
            tab.setOnClickListener(this);
            tab.setTag(i);
        }
    }

    @Override
    public void onClick(View v) {
        switchToTab(v);
    }

    private void switchToTab(View selectedView) {
        for(View tab : mTabs) {
            tab.setSelected(selectedView == tab);
            switchTab((Integer) selectedView.getTag());
        }
    }

    public void setDatas(ArrayList<IndustryCompare> industryCompareList) {
        convertData(industryCompareList);
        forceDraw();
    }

    private void forceDraw() {
        switchTab(mCurrentTab, true);
    }

    private void switchTab(int index) {
        switchTab(index, false);
    }

    private void switchTab(int index, boolean forceDraw) {
        if(mCurrentTab != index || forceDraw) {
            mCurrentTab = index;
            IndustryCompare industryCompare = getIndustryCompare();
            if(industryCompare != null) {
                mUpdateDateView.setText(industryCompare.getSUpdateTime());
            } else {
                mUpdateDateView.setText("");
            }
            mIndustryBenchmarkList.removeAllViews();
            List<IndustryBenchmarkData> datas = getSelectedTabData();
            if(datas.isEmpty()) {
                mEmptyView.setVisibility(VISIBLE);
                mIndustryBenchmarkList.setVisibility(GONE);
            } else {
                mEmptyView.setVisibility(GONE);
                mIndustryBenchmarkList.setVisibility(VISIBLE);
                for(IndustryBenchmarkData data : datas) {
                    View item = View.inflate(getContext(), R.layout.layout_industry_benchmark_item, null);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.layout_industry_benchmark_item_height));
                    mIndustryBenchmarkList.addView(item, lp);
                    setData(data, item);
                }
            }

        }
    }

    private void setData(IndustryBenchmarkData data, View itemView) {
        TextView noView = (TextView) itemView.findViewById(R.id.no);

        if(data.type == IndustryBenchmarkData.TYPE_AVG) {
            noView.setVisibility(INVISIBLE);
        } else {
            noView.setVisibility(VISIBLE);
            if(data.no > 0) {
                noView.setText(String.valueOf(data.no));
            }
        }

        ((TextView) itemView.findViewById(R.id.name)).setText(data.name);

        IndustryBenchmarkItemBar itemBar = (IndustryBenchmarkItemBar) itemView.findViewById(R.id.bar);
        switch (data.type) {
            case IndustryBenchmarkData.TYPE_AVG:
                itemBar.setBarColorRes(R.color.industry_benchmark_average_bar_color);
                break;
            case IndustryBenchmarkData.TYPE_MINE:
                itemBar.setBarColorRes(R.color.industry_benchmark_mine_bar_color);
                break;
            default:
                itemBar.setBarColorRes(R.color.industry_benchmark_default_bar_color);
                break;
        }
        itemBar.setPercent(data.percent);

        ((TextView) itemView.findViewById(R.id.value)).setText(data.value);
        itemView.setTag(data);
        itemView.setOnClickListener(mIndustryBenchmarkItemClickListener);
    }

    private IndustryCompare getIndustryCompare() {
        final int currentTab = mCurrentTab;
        final List<IndustryCompare> industryCompareList = mIndustryCompareList;
        if(industryCompareList != null && industryCompareList.size() > currentTab) {
            return industryCompareList.get(currentTab);
        }
        return null;
    }

    private List<IndustryBenchmarkData> getSelectedTabData() {
        switch(mCurrentTab) {
            case TAB_OPERATION_REVENUE:
                return mOperationRevenueList;
            case TAB_NET_MARGIN:
                return mNetMarginList;
            case TAB_PER_SHARE_EARNING:
                return mPerShareEarningList;
            case TAB_PE_RATIO:
                return mPeRatio;
            default:
                return Collections.EMPTY_LIST;
        }
    }

    private void convertData(IndustryCompare industryCompare, List<IndustryBenchmarkData> industryBenchmarkDataList) {
        if(industryCompare != null) {
            ArrayList<IndustryCompareItem> compareItemList = industryCompare.getVtCompItem();
            if(compareItemList != null && !compareItemList.isEmpty() && industryBenchmarkDataList != null) {
                double maxValue = findMaxValue(compareItemList);
                for(int i=0, size=compareItemList.size(); i<size; i++) {
                    IndustryCompareItem compareItem = compareItemList.get(i);
                    double dValue = compareItem.getDValue();
                    float percent = (float)(dValue / maxValue);
                    if(i == 0) {
                        industryBenchmarkDataList.add(new IndustryBenchmarkData(compareItem.getIOrder(),
                                IndustryBenchmarkData.TYPE_AVG, compareItem.getSName(), compareItem.getSDtSecCode(), percent, compareItem.getSValue()));
                    } else if(TextUtils.equals(mDtSecCode, compareItem.getSDtSecCode())) {
                        industryBenchmarkDataList.add(new IndustryBenchmarkData(compareItem.getIOrder(),
                                IndustryBenchmarkData.TYPE_MINE, mSecName, compareItem.getSDtSecCode(), percent, compareItem.getSValue()));
                    } else {
                        industryBenchmarkDataList.add(new IndustryBenchmarkData(compareItem.getIOrder(), compareItem.getSName(), compareItem.getSDtSecCode(), percent, compareItem.getSValue()));
                    }
                }
            }
        }
    }

    private void convertData(ArrayList<IndustryCompare> industryCompareList) {
        if(industryCompareList != null) {
            final int size = industryCompareList.size();
            mIndustryCompareList = industryCompareList;
            if(size > TAB_OPERATION_REVENUE) {
                mOperationRevenueList = new ArrayList<>();
                convertData(industryCompareList.get(TAB_OPERATION_REVENUE), mOperationRevenueList);
            }

            if(size > TAB_NET_MARGIN) {
                mNetMarginList = new ArrayList<>();
                convertData(industryCompareList.get(TAB_NET_MARGIN), mNetMarginList);
            }

            if(size > TAB_PER_SHARE_EARNING) {
                mPerShareEarningList = new ArrayList<>();
                convertData(industryCompareList.get(TAB_PER_SHARE_EARNING), mPerShareEarningList);
            }

            if(size > TAB_PE_RATIO) {
                mPeRatio = new ArrayList<>();
                convertData(industryCompareList.get(TAB_PE_RATIO), mPeRatio);
            }
        }
    }

    private double findMaxValue(ArrayList<IndustryCompareItem> compareItems) {
        double maxValue = 0d;
        for(IndustryCompareItem compareItem : compareItems) {
            final double dValue = compareItem.getDValue();
            if(dValue > maxValue) {
                maxValue = dValue;
            }
        }
        return maxValue;
    }

    private class IndustryBenchmarkData {
        public static final int TYPE_OTHER = 0;
        public static final int TYPE_AVG = 1;
        public static final int TYPE_MINE = 2;
        public int no;
        public int type;
        public String name;
        public String secCode;
        public float percent;
        public String value;

        public IndustryBenchmarkData(int no, int type, String name, String secCode, float percent, String value) {
            this.no = no;
            this.type = type;
            this.name = name;
            this.secCode = secCode;
            this.percent = percent;
            this.value = value;
        }

        public IndustryBenchmarkData(int no, String name, String secCode, float percent, String value) {
            this(no, TYPE_OTHER, name, secCode, percent, value);
        }
    }
}
