package com.sscf.investment.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.information.ConsultantOpinionFragment;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.OnRefreshStateChangeListener;
import com.sscf.investment.component.ui.widget.RefreshButton;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liqf on 2016/9/10.
 */
public class ExclusiveConsultantActivity extends BaseFragmentActivity {
    private static final String TAG = "ExclusiveConsultantActivity";
    private ConsultantOpinionFragment mFragment;
    private String mDtSecCode;
    private String mSecName;

    @BindView(R.id.actionbar_title) TextView mTitle;
    @BindView(R.id.refresh_button) RefreshButton mRefreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_exclusive_consultant);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mDtSecCode = intent.getStringExtra(DengtaConst.KEY_SEC_CODE);
        mSecName = intent.getStringExtra(DengtaConst.KEY_SEC_NAME);

        mTitle.setText(getString(R.string.consultant_opinion_title, mSecName));

        mFragment = new ConsultantOpinionFragment();
        Bundle args = new Bundle();
        args.putString(DengtaConst.KEY_SEC_CODE, mDtSecCode);
        args.putString(DengtaConst.KEY_SEC_NAME, mSecName);
        mFragment.setArguments(args);
        mFragment.setOnRefreshCompleteListener(new OnRefreshStateChangeListener() {
            @Override
            public void onRefreshStart() {
                mRefreshButton.startLoadingAnim();
            }

            @Override
            public void onRefreshComplete() {
                mRefreshButton.stopLoadingAnim();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, mFragment).commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    @OnClick(R.id.actionbar_back_button)
    public void onBackClicked() {
        finish();
    }

    @OnClick(R.id.refresh_button)
    public void onRefreshClicked() {
        mFragment.onReloadData();
    }

    public static void show(final Context context, final String dtSecCode, final String secName) {
        Intent intent = new Intent(context, ExclusiveConsultantActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DengtaConst.KEY_SEC_CODE, dtSecCode);
        intent.putExtra(DengtaConst.KEY_SEC_NAME, secName);
        context.startActivity(intent);
    }
}
