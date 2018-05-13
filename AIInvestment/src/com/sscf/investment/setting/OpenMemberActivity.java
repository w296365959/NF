package com.sscf.investment.setting;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.payment.LoadingOrderListener;
import com.dengtacj.component.managers.ISchemeManager;
import com.dengtacj.component.router.BeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.information.view.ImageBannerView;
import com.sscf.investment.main.manager.AdRequestManager;
import com.sscf.investment.payment.PaymentRequestManager;
import com.dengtacj.component.entity.payment.CommodityInfo;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.sdk.utils.NumberUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.widgt.FeeListView;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.CommonBaseRecyclerViewAdapter;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.component.ui.widget.CommonRecyclerViewHolder;
import java.util.ArrayList;
import BEC.DT_ACTIVITY_TYPE;
import BEC.DtActivityDetail;
import BEC.DtMemberFeeItem;
import BEC.E_DT_PAY_TIME_UNIT;
import BEC.E_DT_SUBJECT_TYPE;
import BEC.GetMemberFeeListRsp;

/**
 * Created by yorkeehuang on 2017/2/8.
 */
@Route("OpenMemberActivity")
public class OpenMemberActivity extends BaseFragmentActivity implements View.OnClickListener,
        DataSourceProxy.IRequestCallback, ImageBannerView.OnBannerClickListener, CommonDialog.OnDialogButtonClickListener {

    private static final String TAG = OpenMemberActivity.class.getSimpleName();

    private FeeListView mFeeListView;
    private ImageBannerView mBannerView;
    private ArrayList<DtActivityDetail> mBannerList;
    private PrivilegeDisplayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
        setContentView(R.layout.activity_open_member);
        initView();
        AdRequestManager.requestDtActivityList(DT_ACTIVITY_TYPE.T_ACTIVITY_OPEN_MEMBER, this, String.valueOf(DT_ACTIVITY_TYPE.T_ACTIVITY_OPEN_MEMBER));
        AdRequestManager.requestDtActivityList(DT_ACTIVITY_TYPE.T_ACTIVITY_MEMBER_PACK_AD, this, String.valueOf(DT_ACTIVITY_TYPE.T_ACTIVITY_MEMBER_PACK_AD));
    }

    private void initView() {
        TextView actionbarTitle = (TextView) findViewById(R.id.actionbar_title);
        actionbarTitle.setText(R.string.open_memeber);
        ((TextView) findViewById(R.id.member_instructions_info))
                .setText(R.string.member_instructions_info);

        View backView = findViewById(R.id.actionbar_back_button);
        backView.setOnClickListener(this);
        mFeeListView = (FeeListView) findViewById(R.id.fee_list);
        mFeeListView.setOnClickListener(this);
        mBannerView = (ImageBannerView) findViewById(R.id.image_banner_layout);

        RecyclerView displayView = (RecyclerView) findViewById(R.id.privilege_display_view);
        displayView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new PrivilegeDisplayAdapter(this);
        mAdapter.setItemClickable(true);
        displayView.addItemDecoration(new SpaceItemDecoration(DeviceUtil.dip2px(this, 20)));
        displayView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PaymentRequestManager.requestMemberFeeList(this);
        mBannerView.startLooperPic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBannerView.stopLooperPic();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.pay_button:
                if(TimeUtils.isFrequentOperation()) {
                    return;
                }

                DtMemberFeeItem feeItem = mFeeListView.getSelectedItem();
                StatisticsUtil.reportAction(StatisticsConst.CLICK_OPEN_MEMBER);
                if(feeItem != null) {
                    BeaconJump.showLoadingOrder(this, itemToCommodityInfo(feeItem), error -> {
                        switch (error) {
                            case LoadingOrderListener.NETWORK_ERROR:
                                showErrorDialog(OpenMemberActivity.this, (dialog, view, position) -> {
                                    dialog.dismiss();
                                    finish();
                                });
                                break;
                        }
                    });
                }
                break;
            default:
        }
    }

    private static void showErrorDialog(BaseFragmentActivity activity, CommonDialog.OnDialogButtonClickListener listener) {
        if (activity == null || activity.isDestroy()) {
            return;
        }
        activity.runOnUiThread(() -> {
            if (activity.isDestroy()) {
                return;
            }
            final CommonDialog dialog = new CommonDialog(activity);
            dialog.setMessage(activity.getString(R.string.network_error));
            dialog.addButton(R.string.ok);
            if(listener != null) {
                dialog.setButtonClickListener(listener);
            } else {
                dialog.setButtonClickListener((dialog1, view, position) -> {
                    dialog1.dismiss();
                });
            }
            dialog.show();
        });
    }

    private CommodityInfo itemToCommodityInfo(DtMemberFeeItem feeItem) {
        return new CommodityInfo(E_DT_SUBJECT_TYPE.E_DT_SUBJECT_MEMBER,
                feeItem.getIMonthNum(), getString(R.string.member_commodity_desc,
                String.valueOf(feeItem.getIMonthNum())), feeItem.getITotalMoney(),
                E_DT_PAY_TIME_UNIT.E_DT_PAY_TIME_MONTH, "");
    }

    private void showErrorDialog() {
        if (isDestroy()) {
            return;
        }
        runOnUiThread(() -> {
            if (isDestroy()) {
                return;
            }
            final CommonDialog dialog = new CommonDialog(OpenMemberActivity.this);
            dialog.setMessage(getString(R.string.network_error));
            dialog.addButton(R.string.ok);
            dialog.setButtonClickListener(this);
            dialog.show();
        });
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_MEMBER_FEE_LIST:
                handleGetMemberFeeList(success, data);
                break;
            case EntityObject.ET_GET_DT_ACTIVITY_LIST:
                handleGetActivityList(success, data);
                break;
            default:
        }
    }

    private void handleGetMemberFeeList(boolean success, EntityObject data) {
        if(success && data.getEntity() != null) {
            GetMemberFeeListRsp feeListRsp = (GetMemberFeeListRsp) data.getEntity();
            runOnUiThread(() -> mFeeListView.setData(feeListRsp.getVItem()));
            return;
        }
        showErrorDialog();
    }

    private void handleGetActivityList(boolean success, EntityObject data) {

        if(data.getExtra() != null) {
            int type = NumberUtil.parseInt((String) data.getExtra(), -1);
            if(type >= 0) {
                final ArrayList<DtActivityDetail> activityList = EntityUtil.entityToActivityList(success, data);
                DtLog.d(TAG, "handleGetActivityList() success = " + success + ", type = " + type
                        + (activityList != null ? ", activityList size = " + activityList.size() : ", activityList == null"));
                switch (type) {
                    case DT_ACTIVITY_TYPE.T_ACTIVITY_OPEN_MEMBER:
                        mBannerList = activityList;
                        runOnUiThread(() -> {
                            mBannerView.setData(ImageBannerView.getImageUrls(activityList));
                            mBannerView.setOnBannerClickListener(OpenMemberActivity.this);
                        });
                        break;
                    case DT_ACTIVITY_TYPE.T_ACTIVITY_MEMBER_PACK_AD:
                        runOnUiThread(() -> {
                            mAdapter.setData(activityList);
                            mAdapter.notifyDataSetChanged();
                        });
                        break;
                }

            }
        }
    }

    @Override
    public void onBannerClick(int position) {
        final ArrayList<DtActivityDetail> bannerList = mBannerList;
        final int size = bannerList == null ? 0 : bannerList.size();
        if (position < size) {
            DtActivityDetail banner = bannerList.get(position);
            if(goActivityUrl(banner)) {
                StatisticsUtil.reportAction(StatisticsConst.CLICK_MEMBER_BANNER);
            }
        }
    }

    private boolean goActivityUrl(DtActivityDetail activityDetail) {
        if (activityDetail != null) {
            DtLog.d(TAG, "goActivityUrl() url = " + activityDetail.sUrl);
            final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
            if (scheme != null) {
                return scheme.handleUrl(this, activityDetail.sUrl) == CommonConst.PROTOCAL_HANDLE_SUCCESS;
            }
        }
        return false;
    }

    @Override
    public void onDialogButtonClick(CommonDialog dialog, View view, int position) {
        dialog.dismiss();
        finish();
    }

    private class PrivilegeDisplayAdapter extends CommonBaseRecyclerViewAdapter<DtActivityDetail> {

        public PrivilegeDisplayAdapter(Context context) {
            super(context, null, R.layout.privilege_display_item);
        }

        @Override
        public void convert(CommonRecyclerViewHolder holder, DtActivityDetail data, int position) {
            final ImageView icon = holder.getView(R.id.icon);
            final String url = data.getSPicUrl();
            if(!TextUtils.isEmpty(url)) {
                ImageLoaderUtils.getImageLoader().displayImage(url, icon);
            }
            final TextView textView = holder.getView(R.id.name);
            textView.setText(data.getSName());
        }

        @Override
        protected void onItemClick(final View v, CommonRecyclerViewHolder holder, final int position) {
            DtActivityDetail data = getItem(position);
            if(goActivityUrl(data)) {
                // TODO 加统计
            }
        }
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) < 4) {
                outRect.bottom = mSpace;
            }
        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }
}



