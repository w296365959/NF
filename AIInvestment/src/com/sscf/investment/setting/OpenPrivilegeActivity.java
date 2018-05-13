package com.sscf.investment.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.bonus.BonusPointManager;
import com.sscf.investment.bonus.BonusPointRequestManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.CommonDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import BEC.AccuPointErrCode;
import BEC.AccuPointPriviType;
import BEC.GetExChangePriviListRsp;
import BEC.PriviExchangeDesc;

/**
 * Created by yorkeehuang on 2016/12/21.
 */
@Route("OpenPrivilegeActivity")
public class OpenPrivilegeActivity extends BaseFragmentActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback {

    private static final int USER_POINT_CHANGE = 1;
    private static final int OPEN_POINT_PRIVI_RESULT = 2;

    private ListView mListView;

    private PrivilegeAdapter mAdapter;

    private TextView mBonusValueView;
    private TextView mCostAmountView;
    private CheckedTextView mAllSelectedCheck;
    private Button mBuyButton;

    private BroadcastReceiver mReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
        StatisticsUtil.reportAction(StatisticsConst.OPEN_PRIVILEGE_ACTIVITY_DISPLAY);
        setContentView(R.layout.activity_open_privilege);
        initView();
        mReceiver = new BonusPointChangeReceiver();
        IntentFilter filter = new IntentFilter(BonusPointManager.USER_POINT_CHANGE);
        filter.addAction(BonusPointManager.OPEN_POINT_PRIVI_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DengtaApplication.getApplication().getBonusPointManager().requestUserPointInfo();
        BonusPointRequestManager.requestExChangePriviList(this);
    }

    private void initView() {
        TextView actionbarTitle = (TextView) findViewById(R.id.actionbar_title);
        actionbarTitle.setText(R.string.pay_bonus_points);
        ImageView backView = (ImageView) findViewById(R.id.actionbar_back_button);
        backView.setOnClickListener(this);
        mBonusValueView = (TextView) findViewById(R.id.bonus_value);

        mListView = (ListView) findViewById(R.id.open_privilege_list);
        mAdapter = new PrivilegeAdapter(initSelectedTypes());
        mListView.setAdapter(mAdapter);

        mAllSelectedCheck = (CheckedTextView) findViewById(R.id.select_all);
        mAllSelectedCheck.setOnClickListener(this);

        mBuyButton = (Button) findViewById(R.id.buy_button);
        mBuyButton.setOnClickListener(this);

        mCostAmountView = (TextView) findViewById(R.id.cost_amount);
    }

    private int getDialogTitle(int privilegeType) {
        switch (privilegeType) {
            case AccuPointPriviType.E_ACCU_POINT_PRIVI_CHIP:
                return R.string.setting_cyq_dialog_title;
            case AccuPointPriviType.E_ACCU_POINT_PRIVI_KLINE:
                return R.string.setting_similar_k_line_dialog_title;
            case AccuPointPriviType.E_ACCU_POINT_PRIVI_HISTORY:
                return R.string.setting_sec_history_dialog_title;
            default:
                return R.string.setting_dk_signal_dialog_title;
        }
    }

    private void selectAll() {
        if(mAdapter.getCount() > 0) {
            boolean isSelectAll = !mAllSelectedCheck.isChecked();
            mAllSelectedCheck.setChecked(isSelectAll);
            int costAmount = mAdapter.selectAll(isSelectAll);
            mAdapter.notifyDataSetChanged();
            mBuyButton.setEnabled(isSelectAll);
            mCostAmountView.setEnabled(costAmount > 0);
            mCostAmountView.setText(String.format(getResources().getString(R.string.cost_amount_text), costAmount));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.select_all:
                selectAll();
                break;
            case R.id.buy_button:
                StatisticsUtil.reportAction(StatisticsConst.CLICK_OPEN_PRIVILEGE);
                if(NetUtil.isNetWorkConnected(getApplicationContext())) {
                    List<PriviExchangeDesc> selectedList = mAdapter.getSelectedExchangeDescList();
                    showLoadingDialog();
                    DengtaApplication.getApplication().getBonusPointManager().openPointPrivilege(selectedList);
                } else {
                    DengtaApplication.getApplication().showToast(R.string.network_error);
                }
                break;
            default:
                if(view.getTag() != null && view.getTag() instanceof Integer) {
                    int type = (int) view.getTag();
                    mAdapter.selectItem(type);
                    refreshViewState();
                }
        }
    }

    private void refreshViewState() {
        boolean selectedAll = true;
        boolean hasSelected = false;
        int costAmount = 0;
        List<PriviExchangeDesc> priviExchangeDescList = mAdapter.getDatas();
        for(PriviExchangeDesc priviExchangeDesc : priviExchangeDescList) {
            if(mAdapter.isTypeSelected(priviExchangeDesc.getIPriviType())) {
                hasSelected = true;
                costAmount += priviExchangeDesc.getIPriviPoints();
            } else {
                selectedAll = false;
            }
        }
        mAllSelectedCheck.setChecked(selectedAll);
        mBuyButton.setEnabled(hasSelected);
        mCostAmountView.setEnabled(costAmount > 0);
        mCostAmountView.setText(String.format(getResources().getString(R.string.cost_amount_text), costAmount));
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_EXCHANGE_PRIVILEGE_LIST:
                handleGetExchangePrivilegeList(success, data);
                break;
        }
    }

    private void handleGetExchangePrivilegeList(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            GetExChangePriviListRsp rsp = (GetExChangePriviListRsp) data.getEntity();
            runOnUiThread(() -> updateExchagePrivilegeList(rsp.getVItem()));
        }
    }

    private class PrivilegeAdapter extends BaseAdapter {

        private Set<Integer> mSelectedTypes;

        private List<PriviExchangeDesc> mPriviExchangeDescList = Collections.EMPTY_LIST;


        PrivilegeAdapter(Set<Integer> selectedTypes) {
            mSelectedTypes = selectedTypes;
        }

        List<PriviExchangeDesc> getDatas() {
            return mPriviExchangeDescList;
        }

        void selectItem(int type) {
            if(type > 0) {
                if(mSelectedTypes.contains(type)) {
                    mSelectedTypes.remove(type);
                } else {
                    mSelectedTypes.add(type);
                }
                notifyDataSetChanged();
            }
        }

        boolean isTypeSelected(int type) {
            return mSelectedTypes.contains(type);
        }

        int selectAll(boolean isSelectAll) {
            if(mPriviExchangeDescList != null && !mPriviExchangeDescList.isEmpty()) {
                mSelectedTypes.clear();
                int amount = 0;
                if(isSelectAll) {
                    for(PriviExchangeDesc priviExchangeDesc : mPriviExchangeDescList) {
                        amount += priviExchangeDesc.getIPriviPoints();
                        mSelectedTypes.add(priviExchangeDesc.getIPriviType());
                    }
                }
                return amount;
            }
            return 0;
        }

        List<Integer> getSelectedTypes() {
            List selectedTypes = new ArrayList();
            for(Iterator<Integer> it=mSelectedTypes.iterator(); it.hasNext();) {
                int type = it.next();
                selectedTypes.add(type);
            }
            return selectedTypes;
        }

        List<PriviExchangeDesc> getSelectedExchangeDescList() {
            List<PriviExchangeDesc> selectedExchangeDescList = new ArrayList<>(mSelectedTypes.size());
            for(PriviExchangeDesc exchangeDesc : mPriviExchangeDescList) {
                if(mSelectedTypes.contains(exchangeDesc.getIPriviType())) {
                    selectedExchangeDescList.add(exchangeDesc);
                }
            }
            return selectedExchangeDescList;
        }

        void setData(List<PriviExchangeDesc> data) {
            mPriviExchangeDescList = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mPriviExchangeDescList.size();
        }

        @Override
        public PriviExchangeDesc getItem(int position) {
            return mPriviExchangeDescList.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(OpenPrivilegeActivity.this).inflate(R.layout.privilege_list_item_layout, null);
                convertView.setOnClickListener(OpenPrivilegeActivity.this);
            }
            bindView(getItem(position), convertView);
            return convertView;
        }

        private void bindView(PriviExchangeDesc exchangeDesc, View convertView) {
            convertView.setTag(exchangeDesc.getIPriviType());
            ImageView iconView = (ImageView) convertView.findViewById(R.id.privilege_icon);
            ImageLoaderUtils.getImageLoader().displayImage(exchangeDesc.getSPriviIcon(), iconView);
            TextView titleView = (TextView)convertView.findViewById(R.id.privilege_title);
            titleView.setText(exchangeDesc.getSTitleDesc());
            TextView costView = (TextView)convertView.findViewById(R.id.privilege_cost);
            costView.setText(String.valueOf(exchangeDesc.getIPriviPoints()));
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check);
            checkBox.setChecked(mSelectedTypes.contains(exchangeDesc.getIPriviType()));
        }
    }

    private void refreshUserPointInfo() {
        mBonusValueView.setText(String.valueOf(DengtaApplication.getApplication().getBonusPointManager().getAccuPoints()));
    }

    private Handler mUIHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case USER_POINT_CHANGE:
                    refreshUserPointInfo();
                    break;
                case OPEN_POINT_PRIVI_RESULT:
                    dismissLoadingDialog();
                    switch (msg.arg1) {
                        case AccuPointErrCode.E_ACCU_POINT_SUCC:
                            StatisticsUtil.reportAction(StatisticsConst.OPEN_PRIVILEGE_SUCCESS);
                            showBuySuccessDialog();
                            break;
                        case AccuPointErrCode.E_ACCU_POINT_NO_ENOUGH_POINT:
                            showNotEnoughPointDialog();
                            break;
                        default:
                            DengtaApplication.getApplication().showToast("积分兑换失败，请稍后重试");
                            break;
                    }
                    break;
                default:
            }
        }
    };

    private Set<Integer> initSelectedTypes() {
        int[] types = getIntent().getIntArrayExtra(CommonConst.EXTRA_PRIVILEGE_TYPE);
        Set<Integer> selectedTypes = new HashSet<>(types.length);;
        if(types != null && types.length > 0) {
            for(Integer type : types) {
                selectedTypes.add(type);
            }
        }
        return selectedTypes;
    }

    private void updateExchagePrivilegeList(List<PriviExchangeDesc> priviExchangeDescList) {
        mAdapter.setData(priviExchangeDescList);
        refreshViewState();
    }

    private void showNotEnoughPointDialog() {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(R.string.prompt_title);
        String msg = getApplicationContext().getString(R.string.not_enough_point_msg);
        dialog.setMessage(msg);
        dialog.addButton(R.string.cancel);
        dialog.addButton(R.string.ok);
        dialog.setButtonClickListener((dialog1, view, position) -> {
            switch (position) {
                case 0:
                    dialog.dismiss();
                    break;
                case 1:
                    CommonBeaconJump.showOpenMember(OpenPrivilegeActivity.this);
                    dialog.dismiss();
                    break;
                default:
            }
        });
        dialog.show();
    }

    private void showBuySuccessDialog() {
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle(R.string.prompt_title);
        StringBuilder sb = new StringBuilder(getApplicationContext().getString(R.string.buy_success_msg));
        List<Integer> types = mAdapter.getSelectedTypes();
        for(int type : types) {
            sb.append(getString(getDialogTitle(type))).append("、");
        }
        dialog.setMessage(sb.substring(0, sb.length() - 1) + "的特权功能！");
        dialog.addButton(R.string.ok);
        dialog.setButtonClickListener((dialog1, view, position) -> {
            switch (position) {
                case 0:
                    finish();
                    break;
                default:
            }
        });
        dialog.show();
    }

    private class BonusPointChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(BonusPointManager.USER_POINT_CHANGE.equals(intent.getAction())) {
                mUIHandler.sendEmptyMessage(USER_POINT_CHANGE);
            } else if(BonusPointManager.OPEN_POINT_PRIVI_RESULT.equals(intent.getAction())) {
                Message msg = mUIHandler.obtainMessage(OPEN_POINT_PRIVI_RESULT);
                msg.arg1 = intent.getIntExtra(BonusPointManager.NOTIFY_EXTRA_INT_DATA, BonusPointManager.OPEN_PRIVILEGE_FAILED);
                mUIHandler.sendMessage(msg);
            }
        }
    }
}
