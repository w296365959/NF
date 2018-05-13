package com.sscf.investment.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.mobeta.android.dslv.DragSortListView;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.setting.model.KLineSettingConfigure;
import com.sscf.investment.setting.model.KlineSettingConst;
import com.sscf.investment.setting.widgt.SexPicker;
import com.sscf.investment.utils.ViewUtils;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;
import com.sscf.investment.widget.SettingTwoTabSelectorView;

import java.util.ArrayList;
import java.util.List;

/**
 * davidwei
 * K线界面
 */
@Route("SettingKLineActivity")
public final class SettingKLineActivity extends BaseFragmentActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, SexPicker.OnGetSexListener{

    private KLineSettingManager mKlineSettingManager;

    private KLineSettingConfigure mKLineSettingConfigure;

    private DragSortListView mListView;

    private View headerView;

    private View mViewAddCandicator;

    private ArrayList<String> mCandicators;

    private CandicatorEditAdapter mAdapter;

    private TextView mTvIndicatorNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_k_line);
        mKlineSettingManager = DengtaApplication.getApplication().getKLineSettingManager();
        mKLineSettingConfigure = mKlineSettingManager.getKlineSettingConfigure();
        mCandicators = new ArrayList<>();

        initViews();
        //StatisticsUtil.reportAction(StatisticsConst.SETTING_K_LINE_DISPLAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.setting_k_line_settings);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mViewAddCandicator = findViewById(R.id.viewAddCandicator);
        mViewAddCandicator.setOnClickListener(this);
        initHeaderView();
        mListView = (DragSortListView) findViewById(android.R.id.list);
        mListView.addHeaderView(headerView);
        mListView.setDropListener((from, to) ->{
            if (from != to) {
                changePosition(from, to);
            }});
        mAdapter = new CandicatorEditAdapter(this,mCandicators, R.layout.item_setting_kline_candicator);
        mListView.setAdapter(mAdapter);

    }

    private void initHeaderView(){
        headerView = LayoutInflater.from(this).inflate(R.layout.activity_kline_setting_header, null, false);
        headerView.findViewById(R.id.ivSettingMa).setOnClickListener(this);
        headerView.findViewById(R.id.ivSettingDK).setOnClickListener(this);
        headerView.findViewById(R.id.ivSettingGap).setOnClickListener(this);
        headerView.findViewById(R.id.ivSettingMagicNine).setOnClickListener(this);
        headerView.findViewById(R.id.ivSettingBull).setOnClickListener(this);
        headerView.findViewById(R.id.ivSettingCandicatorNum).setOnClickListener(this);
        updateViews(headerView);
        final CheckBox switchBSCheckBox = ((CheckBox) headerView.findViewById(R.id.settingSwitchBS));
        switchBSCheckBox.setChecked(mKLineSettingConfigure.isDKOpen);
        switchBSCheckBox.setOnCheckedChangeListener(this);
        final CheckBox switchGapCheckBox = ((CheckBox) headerView.findViewById(R.id.settingSwitchGap));
        switchGapCheckBox.setChecked(mKLineSettingConfigure.isGapOpen);
        switchGapCheckBox.setOnCheckedChangeListener(this);
        final CheckBox switchMagicNineCheckBox = ((CheckBox) headerView.findViewById(R.id.settingSwitchMagicNine));
        switchMagicNineCheckBox.setChecked(mKLineSettingConfigure.isMagicNineOpen);
        switchMagicNineCheckBox.setOnCheckedChangeListener(this);
        final CheckBox switchBullBearCheckBox = ((CheckBox) headerView.findViewById(R.id.settingSwitchBullBear));
        switchBullBearCheckBox.setChecked(mKLineSettingConfigure.isBullBearOpen);
        switchBullBearCheckBox.setOnCheckedChangeListener(this);
        final CheckBox switchMACheckBox = ((CheckBox) headerView.findViewById(R.id.settingSwitchMa));
        switchMACheckBox.setChecked(mKLineSettingConfigure.isMAOpen);
        switchMACheckBox.setOnCheckedChangeListener(this);
    }

    private void updateAddView(){
        mViewAddCandicator.setVisibility(mCandicators.size() == mKLineSettingConfigure.allIndicators.size() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] strCandicators = mKLineSettingConfigure.showIndicators.split(",");
        mCandicators.clear();
        for (int i = 0 ; i < strCandicators.length ; i++){
            mCandicators.add(strCandicators[i]);
        }
        mAdapter.notifyDataSetChanged();
        updateViews(headerView);
    }

    private void changePosition(int from, int to) {
        if (from != to) {
            final int min = Math.min(from, to);
            final int max = Math.max(from, to) + 1;
            final int size = max - min;
            final List<String> data = mCandicators;
            if (min >= data.size()) {
                return;
            }

            ArrayList<String> changedBeforeTimes = new ArrayList<>();

            for (String item : data.subList(min, max)) {
                changedBeforeTimes.add(item);
            }

            String item = data.remove(from);
            data.add(to, item);
            mListView.moveCheckState(from, to);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateViews(View parentView) {
        // 默认复权选择
        ViewUtils.initSettingRightTextItem(parentView, R.id.settingRightsOffering, R.string.setting_rights_offering,
                getResources().getStringArray(R.array.setting_k_line_rights_offering_text_array),
                mKLineSettingConfigure.rightStatus,
                new SettingTwoTabSelectorView.OnTabSelectedListener() {
                    @Override
                    public void onLeftTabSelected() {
                        mKLineSettingConfigure.rightStatus = 0;
                    }

                    @Override
                    public void onRightTabSelected() {
                        mKLineSettingConfigure.rightStatus = 1;
                    }
                });
        // 默认K线样式选择
        ViewUtils.initSettingRightTextItem(parentView, R.id.settingKLineStyleType, R.string.setting_k_line_style_type,
                getResources().getStringArray(R.array.setting_k_line_style_type_text_array),
                mKLineSettingConfigure.klineStyle,
                new SettingTwoTabSelectorView.OnTabSelectedListener() {
                    @Override
                    public void onLeftTabSelected() {
                        StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_KLINE_SETTINGS_SWITCH);
                        mKLineSettingConfigure.klineStyle = 0;
                    }

                    @Override
                    public void onRightTabSelected() {
                        StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_KLINE_SETTINGS_SWITCH);
                        mKLineSettingConfigure.klineStyle = 1;
                    }
                });

        mTvIndicatorNum = (TextView) parentView.findViewById(R.id.tvIndicatorNum);
        mTvIndicatorNum.setText(mKLineSettingConfigure.indicatorNum + "个");
        updateAddView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StringBuffer sb = new StringBuffer();
        for (int i = 0 ; i < mCandicators.size() ; i++){
            sb.append(mCandicators.get(i) + (i == mCandicators.size() - 1 ? "" : ","));
        }
        mKLineSettingConfigure.showIndicators = sb.toString();
        mKlineSettingManager.saveConfigure();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSettingMa://MA设置
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_MA_SHIFT);
                startActivity(new Intent(this, SettingMAActivity.class));
                break;
            case R.id.ivSettingBull:
                Intent intent = new Intent();
                intent.setClass(this, SettingOBVActivity.class);
                intent.putExtra(SettingOBVActivity.KEY_CANDICATOR_TYPE, "牛熊转换");
                startActivity(intent);
                break;
            case R.id.ivSettingGap:
                Intent intent1 = new Intent();
                intent1.setClass(this, SettingOBVActivity.class);
                intent1.putExtra(SettingOBVActivity.KEY_CANDICATOR_TYPE, "跳空缺口");
                startActivity(intent1);
                break;
            case R.id.ivSettingMagicNine:
                startActivity(new Intent(this, SettingMagicNineActivity.class));
                break;
            case R.id.ivSettingDK:
                Intent intent2 = new Intent();
                intent2.setClass(this, SettingOBVActivity.class);
                intent2.putExtra(SettingOBVActivity.KEY_CANDICATOR_TYPE, "多空信号");
                startActivity(intent2);
                break;
            case R.id.viewAddCandicator://添加指标
                startActivity(new Intent(this, AddCandicatorActivity.class));
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_SET_UP_ADD);
                break;
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.ivSettingCandicatorNum:
                ArrayList<String> allIndicatorNum = new ArrayList<>();
                allIndicatorNum.add("1");
                allIndicatorNum.add("2");
                allIndicatorNum.add("3");
                allIndicatorNum.add("4");
                final SexPicker sexPicker = new SexPicker(this, allIndicatorNum);
                sexPicker.setOnGetSexListener(this);
                sexPicker.setSex(String.valueOf(mKLineSettingConfigure.indicatorNum));
                sexPicker.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.settingSwitchBS:// 日线多空信号开关
                mKLineSettingConfigure.isDKOpen = isChecked;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_STOCKDETAIL_SPEC_MORE_SWITCH);
                break;
            case R.id.settingSwitchGap://跳空缺口信号开关
                mKLineSettingConfigure.isGapOpen = isChecked;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_TIAOKONG_SWITCH);
                break;
            case R.id.settingSwitchMagicNine://日线神奇九转开关
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_JINZHUAN_SWITCH);
                mKLineSettingConfigure.isMagicNineOpen = isChecked;
                break;
            case R.id.settingSwitchBullBear://牛熊转换指标
                mKLineSettingConfigure.isBullBearOpen = isChecked;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_NIUXIONG_SUCCESS_CLICK);
                break;
            case R.id.settingSwitchMa:
                mKLineSettingConfigure.isMAOpen = isChecked;
                break;
        }
    }

    @Override
    public void onGetSex(String sex) {
        mKLineSettingConfigure.indicatorNum = Integer.valueOf(sex);
        mTvIndicatorNum.setText(mKLineSettingConfigure.indicatorNum + "个");
    }

    private final class CandicatorEditAdapter extends CommonAdapter<String> implements View.OnClickListener {
        public CandicatorEditAdapter(Context context, List<String> data, int itemLayoutId) {
            super(context, data, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder holder, String item, int position) {
            final TextView titleView = holder.getView(R.id.tvCandicatorName);
            titleView.setText(item);
            ImageView ivSettingCandicator = holder.getView(R.id.ivSettingCandicator);
            ivSettingCandicator.setOnClickListener(this);
            ivSettingCandicator.setImageResource(mKlineSettingManager.isCandicatorHasParams(item) ?
                    R.drawable.setting_kline_configure : R.drawable.setting_kline_configure_info);
            ivSettingCandicator.setTag(holder);

            View ivDelete = holder.getView(R.id.ivDeleteCandicator);
            ivDelete.setOnClickListener(this);
            ivDelete.setTag(holder);
        }

        @Override
        public void onClick(View v) {
            CommonViewHolder holder = (CommonViewHolder) v.getTag();
            if (null == holder){
                return;
            }
            final int position = holder.getPosition();
            switch (v.getId()) {
                case R.id.ivDeleteCandicator:
                    if (mCandicators.size() <= KlineSettingConst.MIN_SHOW_CANDICATORS_NUM){
                        return;
                    }
                    mCandicators.remove(position);
                    updateAddView();
                    notifyDataSetChanged();
                    break;
                case R.id.ivSettingCandicator:
                    goCandicatorSetting(mCandicators.get(position));
                default:
                    break;
            }
        }

        private void goCandicatorSetting(String candicator){
            Intent intent = new Intent();
            Class activity = null;

            if (candicator.equals("成交量")){
                activity = SettingAmountActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_VOLUME_SET_UP);
            }else if (candicator.equals("主力资金")){
                activity = SettingCapitalFlowActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_MAIN_CAPITAL_SET_UP);
            }else if (candicator.equals("MACD")){
                activity = SettingMACDActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_MACD_SET_UP);
            }else if (candicator.equals("KDJ")){
                activity = SettingKDJActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_KDJ_SET_UP);
            }else if (candicator.equals("RSI")){
                activity = SettingRSIActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_RSI_SET_UP);
            }else if (candicator.equals("BOLL")){
                activity = SettingBOLLActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_BOLL_SET_UP);
            }else if (candicator.equals("DMI")){
                activity = SettingDMIActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_DMI_SET_UP);
            }else if (candicator.equals("CCI")){
                activity = SettingCCIActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_CCL_SET_UP);
            }else if (candicator.equals("ENE")){
                activity = SettingENEActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_ENE_SET_UP);
            }else if (candicator.equals("DMA")){
                activity = SettingDMAActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_DMA_SET_UP);
            }else if (candicator.equals("EXPMA")){
                activity = SettingEXPMAActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_EXPMA_SET_UP);
            }else if (candicator.equals("VR")){
                activity = SettingVRActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_VR_SET_UP);
            }else if (candicator.equals("BBI")){
                activity = SettingBBIActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_BBL_SET_UP);
            }else if (candicator.equals("OBV")){
                activity = SettingOBVActivity.class;
                intent.putExtra(SettingOBVActivity.KEY_CANDICATOR_TYPE, "OBV");
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_OBV_SET_UP);
            }else if (candicator.equals("BIAS")){
                activity = SettingBIASActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_BIAS_SET_UP);
            }else if (candicator.equals("WR")){
                activity = SettingWRActivity.class;
                StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_WR_SET_UP);
            }else if (candicator.equals("横盘突破")){
                activity = SettingOBVActivity.class;
                intent.putExtra(SettingOBVActivity.KEY_CANDICATOR_TYPE, "横盘突破");

            }
            intent.setClass(SettingKLineActivity.this, activity);
            startActivity(intent);
        }
    }

}