package com.sscf.investment.consultant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.search.SearchActivity;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.widget.BaseFragment;

/**
 * Created by Administrator on 2018/1/11 0011.
 */

public class TabFragmentConsultant extends BaseFragment implements View.OnClickListener {

    private View mIntelligentAnswerLayout;
    private View mIntellgentAnswerRedDotView;

    private LocalBroadcastReceiver mReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_consultant, null);
        enableStatusSet();
        initActionbar(rootView);
        return rootView;
    }

    private void initActionbar(View rootView) {
        mIntelligentAnswerLayout = rootView.findViewById(R.id.intelligent_answer_layout);
        mIntelligentAnswerLayout.setOnClickListener(this);
        final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
        mIntellgentAnswerRedDotView = rootView.findViewById(R.id.intellgentAnswerRedDot);
        mIntellgentAnswerRedDotView.setVisibility(redDotManager.getIntelligentAnswerRedDotState() ? View.VISIBLE : View.GONE);
        registerBroadcastReceiver();

        rootView.findViewById(R.id.actionbar_portfolio_search).setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterBroadcastReceiver();
    }

    @Override
    public void onClick(View v) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (v.getId()) {
            case R.id.intelligent_answer_layout:
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                final boolean state = redDotManager.getIntelligentAnswerRedDotState();
                if (state) {
                    redDotManager.setIntelligentAnswerRedDotState(false);
                }
                WebBeaconJump.showIntelligentAnswer(getContext(), state);
                StatisticsUtil.reportAction(StatisticsConst.INFORMATION_INTELLIGENT_ANSWER_CLICKED);
                break;
            case R.id.actionbar_portfolio_search:
                StatisticsUtil.reportAction(StatisticsConst.INFORMATION_SEARCH_CLICKED);
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            default:
                break;
        }
    }

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new LocalBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_RED_DOT_STATE_CHANGED);
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(DengtaApplication.getApplication()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private final class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_RED_DOT_STATE_CHANGED.equals(action)) {
                final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
                mIntellgentAnswerRedDotView.setVisibility(redDotManager.getIntelligentAnswerRedDotState() ? View.VISIBLE : View.GONE);
            }
        }
    }
}
