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
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.bonus.BonusPointManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.sdk.main.manager.WebUrlManager;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import java.util.List;
import BEC.AccuPointPriviType;
import BEC.AccuPointTaskItem;
import BEC.AccuPointTaskType;

/**
 * Created by yorkeehuang on 2016/12/19. 积分任务
 */
@Route("BonusPointsActivity")
public class BonusPointsActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final String TAG = BonusPointsActivity.class.getSimpleName();

    private TextView mBonusView;
    private TextView mBonusUnitView;
    private TextView mTodayBonusCountView;
    private TextView mLeftTaskCountView;
    private ViewGroup mBonusPanelView;
    private Button mLoginBtn;

    private TextView mChipDistributionView;
    private TextView mSimilarKlineView;
    private TextView mHistoryView;

    private LinearLayout mTaskListView;

    private BonusPointChangeReceiver mReceiver = null;

    private static final int USER_POINT_CHANGE = 1;
    private static final int TASK_CHANGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_points);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
        initView();
        if(mReceiver == null) {
            mReceiver = new BonusPointChangeReceiver();
        }
        initBroadcast();
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BonusPointManager.USER_POINT_CHANGE);
        filter.addAction(BonusPointManager.TASK_LIST_CHANGE);
        filter.addAction(BonusPointManager.DESC_CHANGE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DengtaApplication.getApplication().getBonusPointManager().requestUserPointInfo();
        DengtaApplication.getApplication().getBonusPointManager().requestTasks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
        }
    }

    private void initView() {
        TextView actionbarTitle = (TextView) findViewById(R.id.actionbar_title);
        actionbarTitle.setText(R.string.setting_bonus_points_text);
        ImageView backView = (ImageView) findViewById(R.id.actionbar_back_button);
        backView.setOnClickListener(this);
        mBonusPanelView = (ViewGroup) findViewById(R.id.bonus_panel);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mBonusView = (TextView) findViewById(R.id.bonus);
        mBonusUnitView = (TextView) findViewById(R.id.bonus_unit);
        mTodayBonusCountView = (TextView) findViewById(R.id.today_bonus_points_count);
        mLeftTaskCountView = (TextView) findViewById(R.id.left_task_count);
        ImageView faqBtn = (ImageView) findViewById(R.id.actionbar_faq_button);
        faqBtn.setOnClickListener(this);

        ImageView detailBtn = (ImageView) findViewById(R.id.actionbar_detail_button);
        detailBtn.setOnClickListener(this);

        mChipDistributionView = (TextView) findViewById(R.id.setting_chip_distribution);
        mSimilarKlineView = (TextView) findViewById(R.id.setting_similar_kline);
        mHistoryView = (TextView) findViewById(R.id.setting_sec_history);

        mChipDistributionView.setOnClickListener(this);
        mSimilarKlineView.setOnClickListener(this);
        mHistoryView.setOnClickListener(this);

        mTaskListView = (LinearLayout) findViewById(R.id.bonus_points_task_list);
    }


    private Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case USER_POINT_CHANGE:
                    refreshPoint();
                    break;
                case TASK_CHANGE:
                    refreshTask();
                    break;
                default:
            }
        }
    };

    private void refreshPoint() {
        BonusPointManager manager = DengtaApplication.getApplication().getBonusPointManager();
        if(DengtaApplication.getApplication().getAccountManager().isLogined()) {
            mBonusPanelView.setVisibility(View.VISIBLE);
            mLoginBtn.setVisibility(View.GONE);
            int bonus = manager.getAccuPoints();
            if(bonus != BonusPointManager.INVALID_INT_DATA) {
                mBonusView.setText(String.valueOf(bonus));
                mBonusUnitView.setVisibility(View.VISIBLE);
            } else {
                mBonusView.setText("--");
                mBonusUnitView.setVisibility(View.GONE);
            }

            int todayBonusCount = manager.getPointsDaily();
            if(todayBonusCount != BonusPointManager.INVALID_INT_DATA) {
                mTodayBonusCountView.setText(String.valueOf(todayBonusCount));
            } else {
                mTodayBonusCountView.setText("--");
            }

            int leftTaskCount = manager.getLeftTaskNum();
            if(leftTaskCount != BonusPointManager.INVALID_INT_DATA) {
                mLeftTaskCountView.setText(String.valueOf(leftTaskCount));
            } else {
                mLeftTaskCountView.setText("--");
            }
        } else {
            mBonusPanelView.setVisibility(View.INVISIBLE);
            mLoginBtn.setVisibility(View.VISIBLE);
            mLoginBtn.setOnClickListener(this);
        }
        refreshPrivilegeView(mChipDistributionView, manager.isPrivilegeOn(AccuPointPriviType.E_ACCU_POINT_PRIVI_CHIP));
        refreshPrivilegeView(mSimilarKlineView, manager.isPrivilegeOn(AccuPointPriviType.E_ACCU_POINT_PRIVI_KLINE));
        refreshPrivilegeView(mHistoryView, manager.isPrivilegeOn(AccuPointPriviType.E_ACCU_POINT_PRIVI_HISTORY));
    }

    private void refreshPrivilegeView(TextView view, boolean enable) {
        int res = -1;
        switch (view.getId()) {
            case R.id.setting_chip_distribution:
                if(enable) {
                    res = R.drawable.chip_dist_normal;
                } else {
                    res = R.drawable.chip_dist_disable;
                }
                break;
            case R.id.setting_similar_kline:
                if(enable) {
                    res = R.drawable.similar_kline_normal;
                } else {
                    res = R.drawable.similar_kline_disable;
                }
                break;
            case R.id.setting_sec_history:
                if(enable) {
                    res = R.drawable.history_normal;
                } else {
                    res = R.drawable.history_disable;
                }
                break;
            default:
        }
        if(res > 0) {
            view.setCompoundDrawablesWithIntrinsicBounds(0, res, 0, 0);
            if(enable) {
                view.setTextColor(getResources().getColor(R.color.default_text_color_100));
            } else {
                view.setTextColor(getResources().getColor(R.color.default_text_color_40));
            }
        }
    }

    private void refreshTask() {
        BonusPointManager manager = DengtaApplication.getApplication().getBonusPointManager();
        List<AccuPointTaskItem> taskList = manager.getTaskList();
        if(taskList != null && !taskList.isEmpty()) {
            boolean isLogined = DengtaApplication.getApplication().getAccountManager().isLogined();
            int viewCount = mTaskListView.getChildCount();
            int taskCount = taskList.size();
            for(int i=0; i<taskCount; i++) {
                View view;
                if(i < viewCount) {
                    view = mTaskListView.getChildAt(i);
                } else {
                    view = getLayoutInflater().inflate(R.layout.bonus_task_item, mTaskListView, false);
                    mTaskListView.addView(view);
                }
                AccuPointTaskItem task = taskList.get(i);
                TextView titleView = (TextView) view.findViewById(R.id.bonus_task_title);
                titleView.setText(task.getSName());
                ImageView iconView = (ImageView) view.findViewById(R.id.bonus_task_icon);
                ImageLoaderUtils.getImageLoader().displayImage(task.getSIcon(), iconView);

                TextView descriptionView = (TextView) view.findViewById(R.id.bonus_task_description);
                descriptionView.setText(task.getSDesc());
                TextView executeTask = (TextView) view.findViewById(R.id.execute_task_btn);
                boolean isTaskFinished = manager.isTaskFinished(task.getIType());
                // 未登录或者登陆后未完成任务则显示为高亮
                executeTask.setEnabled(!isLogined || (isLogined && !isTaskFinished));

                view.setTag(task.getIType());
                view.setOnClickListener(this);
                executeTask.setText("+" + task.getIGetPoints());

                View dividerLine = view.findViewById(R.id.divider_line);
                if(i == taskCount - 1) {
                    dividerLine.setVisibility(View.GONE);
                } else {
                    dividerLine.setVisibility(View.VISIBLE);
                }
            }
            if(taskCount < viewCount) {
                for(int i=taskCount; i<viewCount; i++) {
                    mTaskListView.removeViewAt(i);
                }
            }
        } else {
            mTaskListView.removeAllViewsInLayout();
        }
    }

    @Override
    public void onClick(View view) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (view.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_faq_button:
                showWeb(DengtaApplication.getApplication().getUrlManager().getAccumulatePointsFaqUrl());
                break;
            case R.id.actionbar_detail_button://积分明细
                if(DengtaApplication.getApplication().getAccountManager().isLogined()) {
                    StatisticsUtil.reportAction(StatisticsConst.A_ME_ACCUPOINT_DETAIL_PAGE);
                    showWeb(DengtaApplication.getApplication().getUrlManager().getIntegralDetail());
                } else {
                    CommonBeaconJump.showLogin(this);
                }
                break;
            case R.id.login_btn:
                CommonBeaconJump.showLogin(this);
                break;
            case R.id.setting_chip_distribution:
                showWeb(WebUrlManager.getInstance().getOpenChipDistributionPrivilegeUrl());
                break;
            case R.id.setting_similar_kline:
                showWeb(WebUrlManager.getInstance().getOpenSimilarKlinePrivilegeUrl());
                break;
            case R.id.setting_sec_history:
                showWeb(WebUrlManager.getInstance().getOpenHistoryPrivilegeUrl());
                break;
            default:
                if(view.getTag() != null && view.getTag() instanceof Integer) {
                    int taskType = (Integer) view.getTag();
                    DtLog.d(TAG, "onClick() taskType = " + taskType);
                    StatisticsUtil.reportAction(StatisticsConst.BONUS_TASK_CENTER_CLICK_TASK);
                    if(DengtaApplication.getApplication().getAccountManager().isLogined()) {
                        if(!DengtaApplication.getApplication().getBonusPointManager().isTaskFinished(taskType)) {
                            switch (taskType) {
                                case AccuPointTaskType.E_ACCU_POINT_TASK_BIND_PHONE:
                                    CommonBeaconJump.showBindCellphone(this);
                                    break;
                                case AccuPointTaskType.E_ACCU_POINT_TASK_INVITE:
                                    WebBeaconJump.showInviteFriends(this);
                                    break;
                                case AccuPointTaskType.E_ACCU_POINT_TASK_DO_RISK_EVAL:
                                    WebBeaconJump.showRiskEval(this);
                                    break;
                                default:
                                    DengtaApplication.getApplication().showToast(R.string.finish_task_hint_text);
                            }
                        } else {
                            DengtaApplication.getApplication().showToast(R.string.task_limited_text);
                        }
                    } else {
                        CommonBeaconJump.showLogin(this);
                    }
                }
        }
    }

    private void showWeb(String url) {
        if(!TextUtils.isEmpty(url)) {
            WebBeaconJump.showCommonWebActivity(this, url);
        }
    }

    private class BonusPointChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(BonusPointManager.USER_POINT_CHANGE.equals(intent.getAction())) {
                mUiHandler.sendEmptyMessage(USER_POINT_CHANGE);
            } else if(BonusPointManager.TASK_LIST_CHANGE.equals(intent.getAction())) {
                mUiHandler.sendEmptyMessage(TASK_CHANGE);
            }
        }
    }
}
