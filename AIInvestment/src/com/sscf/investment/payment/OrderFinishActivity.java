package com.sscf.investment.payment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.entity.payment.OrderInfo;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import BEC.E_DT_PAY_STATUS;

/**
 * Created by davidwei on 2017-02-08.
 */
@Route("OrderFinishActivity")
public final class OrderFinishActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView mTitleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final OrderInfo orderInfo = (OrderInfo) getIntent().getSerializableExtra(DengtaConst.EXTRA_ORDER_INFO);
        if (orderInfo == null) {
            DengtaApplication.getApplication().showToast(R.string.order_invalid);
            finish();
            return;
        }

        setContentView(R.layout.payment_order_finish_activity);
        initViews();
        switchFragment(orderInfo.status);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    private void initViews() {
        mTitleView = ((TextView) findViewById(R.id.actionbar_title));
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
    }

    public void switchFragment(final int status) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(status == E_DT_PAY_STATUS.E_DT_PAY_SUCCESS) {
            mTitleView.setText(R.string.pay_finish);
            fragmentTransaction.replace(R.id.container, new OrderFinishSuccessFragment());
        } else {
            mTitleView.setText(R.string.pay_unknown);
            fragmentTransaction.replace(R.id.container, new OrderFinishUnknownFragment());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
            case R.id.ok:
                finish();
                break;
            default:
                break;
        }
    }
}
