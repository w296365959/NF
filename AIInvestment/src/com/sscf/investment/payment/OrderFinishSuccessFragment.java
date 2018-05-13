package com.sscf.investment.payment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.payment.OrderInfo;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.sscf.investment.R;
import com.sscf.investment.main.manager.AdRequestManager;
import com.sscf.investment.sdk.utils.EntityUtil;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.PaymentInfoUtils;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.widget.BaseFragment;
import BEC.DtActivityDetail;
import BEC.E_DT_SUBJECT_TYPE;

/**
 * Created by yorkeehuang on 2017/2/15.
 */

public final class OrderFinishSuccessFragment extends BaseFragment implements View.OnClickListener, DataSourceProxy.IRequestCallback {

    private OrderInfo mOrderInfo;
    private ImageView mAdView1;
    private ViewGroup mAdPanel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_order_finish_success, container, false);
        initView(root);
        ThreadUtils.runOnUiThreadDelay(this::updateMemberState, 2000L);
        return root;
    }

    private void initView(View rootView) {
        mOrderInfo = (OrderInfo) getActivity().getIntent().getSerializableExtra(DengtaConst.EXTRA_ORDER_INFO);
        initTips(rootView);
        if(mOrderInfo != null) {
            ((TextView) rootView.findViewById(R.id.commodity_info)).setText(mOrderInfo.name);
            PaymentInfoUtils.setAmountText((TextView) rootView.findViewById(R.id.order_amount), mOrderInfo.amount);
        }

        initAd(rootView);
        rootView.findViewById(R.id.ok).setOnClickListener(this);
    }

    private int getOrderType() {
        if(mOrderInfo != null) {
            return mOrderInfo.type;
        }
        return E_DT_SUBJECT_TYPE.E_DT_SUBJECT_MEMBER;
    }

    private void initTips(View rootView) {
        final TextView tipsView = (TextView) rootView.findViewById(R.id.tips);
        final Resources res = getResources();
        final SpannableString text = new SpannableString(res.getString(R.string.order_finish));
        text.setSpan(new ForegroundColorSpan(0xff10ca21), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(24, true), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tipsView.append(text);
        tipsView.append("\n");
        tipsView.append(getString(R.string.success_buy_commodity));
    }

    private void initAd(final View rootView) {
        mAdPanel = (ViewGroup) rootView.findViewById(R.id.ad_panel);
        mAdView1 = (ImageView) rootView.findViewById(R.id.ad_view1);
        AdRequestManager.requestPayAdList(getOrderType(), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                getActivity().finish();
                break;
            case R.id.ad_view1:
                DtActivityDetail detail = (DtActivityDetail)v.getTag();
                if(mOrderInfo != null) {
                    StatisticsUtil.reportAction(StatisticsConst.ORDER_FINISH_CLICK_BANNER,
                            StatisticsConst.KEY_SUBJECT_TYPE, String.valueOf(mOrderInfo.type));
                }

                if(detail != null && !TextUtils.isEmpty(detail.getSUrl())) {
                    final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                    if (scheme != null) {
                        scheme.handleUrl(getActivity(), detail.getSUrl());
                    }
                }
                break;
            default:
        }
    }

    private void updateMemberState() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager != null) {
            final OrderInfo orderInfo = mOrderInfo;
            if (orderInfo != null && orderInfo.type == E_DT_SUBJECT_TYPE.E_DT_SUBJECT_MEMBER) {
                accountManager.updateAccountInfoFromWeb();
            }
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_GET_DT_ACTIVITY_LIST:
                handleAd(success, data);
                break;
            default:
        }
    }

    private void handleAd(boolean success, EntityObject data) {
        final Activity activity = getActivity();
        if (activity != null) {
            final DtActivityDetail detail = EntityUtil.entityToDtActivityDetail(success, data);
            if (detail != null) {
                activity.runOnUiThread(() -> {
                    mAdPanel.setVisibility(View.VISIBLE);
                    mAdView1.setVisibility(View.VISIBLE);
                    ImageLoaderUtils.getImageLoader().displayImage(detail.getSPicUrl(), mAdView1);
                    mAdView1.setTag(detail);
                    mAdView1.setOnClickListener(this);
                });
            }
        }
    }
}