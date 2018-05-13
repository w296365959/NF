package com.sscf.investment.main;

import BEC.*;

import android.app.Dialog;
import android.content.*;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.dengtacj.component.managers.IThemeManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.manager.ThemeManager;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.sscf.investment.main.presenter.MainPresenter;
import com.sscf.investment.push.PushClickReceiver;
import com.sscf.investment.scan.OcrResultActivity;
import com.sscf.investment.sdk.stat.StatManager;
import com.sscf.investment.sdk.utils.*;
import com.sscf.investment.setting.LoginActivity;
import com.sscf.investment.setting.SettingConst;
import com.dengtacj.component.entity.db.FavorItem;
import com.sscf.investment.setting.manager.RedDotManager;
import com.sscf.investment.splash.SplashDialog;
import com.sscf.investment.utils.*;
import com.dengtacj.component.router.CommonBeaconJump;
import com.sscf.investment.web.CommonWebConst;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ConfirmDialog;
import com.sscf.investment.widget.FullScreenAdDialog;
import com.sscf.investment.widget.LiveMessageView;

/**
 * Created by xuebinliu on 2015/7/22.
 *
 * 程序主界面
 */
public final class MainActivity extends BaseFragmentActivity {
    private static final String TAG = "MainActivity";

    private TabFragmentManager mTabManager = null;

    private View mStockPickRedDotView;
    private View mSettingRedDotView;

    private LiveMessageView mLiveMsgView;

    private SplashDialog mSplashDialog;
    private long mLastBackPressedTime = 0;

    private boolean mBeaconFailed;
    private boolean mUpgradeDialogNeedShow;
    private boolean mTicketErorrDialogNeedShow;
    private boolean mShouldShowFullScreenAd;

    private MainBroadcastReceiver mReceiver;

    private MainPresenter mPresenter;

    private static MainActivity instance;

    private int colorStatusBarNight, colorStatusBarNormal;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected int getDefaultTheme() {
        return R.style.theme_default;
    }

    @Override
    protected int getNightTheme() {
        return R.style.theme_night;
    }

    @Override
    protected boolean isRecoverNightModeConfigurationEnable() {
        return !DeviceUtil.isAfterAndroidN();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final long start = System.currentTimeMillis();
        DtLog.d(TAG, start + " MainActivity.onCreate start intent=" + getIntent());

        instance = this;

        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        dengtaApplication.mUiHandler.removeMessages(DengtaApplication.MSG_KILL_MAIN_PROCESS);

        // crash重启super会重建fragment，这里把savedInstanceState置为null，避免重建
        savedInstanceState = null;
        super.onCreate(savedInstanceState);

        dengtaApplication.getAccountManager().setMainActivity(this);

        getWindow().setBackgroundDrawable(null);

        setContentView(R.layout.main_layout);

        colorStatusBarNight = getResources().getColor(R.color.actionbar_bg_color_night);
        colorStatusBarNormal = getResources().getColor(R.color.actionbar_bg);
        DeviceUtil.enableTranslucentStatus(this, isNightTheme() ? colorStatusBarNight : colorStatusBarNormal);

        if (!dengtaApplication.isMainActivityRestarting()) {
            //正常启动
            mSplashDialog = showSplash();
        } else {
            //切换主题导致的重启
            dengtaApplication.setMainActivityRestarting(false);
        }

        initBottomTabs(dengtaApplication);
        initLiveMsg();
        registerBroadcastReceiver();
        dengtaApplication.setMainActivityInited(true);
        mPresenter = new MainPresenter(this);
        DtLog.d(TAG, "MainActivity.onCreate end spend " + (System.currentTimeMillis() - start));
    }

    private boolean isNightTheme() {
        final IThemeManager themeManager = (IThemeManager) ComponentManager.getInstance()
                .getManager(IThemeManager.class.getName());
        return themeManager.isNightTheme();
    }

    private void initBottomTabs(final DengtaApplication dengtaApplication) {
        mStockPickRedDotView = findViewById(R.id.tabStockPickRedDot);
        mSettingRedDotView = findViewById(R.id.tabSettingRedDot);

        dengtaApplication.mUiHandler.postDelayed(() -> {
                mTabManager = new TabFragmentManager(MainActivity.this, R.id.tab_content);

                final RedDotManager redDotManager = dengtaApplication.getRedDotManager();
                mSettingRedDotView.setVisibility(redDotManager.getMineState() ? View.VISIBLE : View.INVISIBLE);
                mStockPickRedDotView.setVisibility(redDotManager.getStockPickState() ? View.VISIBLE : View.INVISIBLE);
                mTabManager.updateSettingTab();

                if (dengtaApplication.isNewInstall()) {
                    switchTab(0);
                }
        }, 0);
    }

    public void switchTab(final int index) {
        if (mTabManager != null) {
            mTabManager.switchTab(index);
        }
    }

    private void initLiveMsg() {
        mLiveMsgView = (LiveMessageView) findViewById(R.id.live_message);
        mLiveMsgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int liveType = mLiveMsgView.getLiveType();
                WebBeaconJump.showDtLive(MainActivity.this, liveType);
                StatisticsUtil.reportAction(StatisticsConst.LIVE_MSG_CLICKED);
            }
        });
    }

    public boolean updateLiveMsg(final GetBoxLiveRsp rsp) {
        return mLiveMsgView.setData(rsp.sMsg, rsp.iMsgType, rsp.iTime, rsp.iLiveType);
    }


    @Override
    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
        DtLog.d(TAG, "main push onNewIntent");
        executeExtraIntent(intent);
    }

    protected int[] getThemeResourceIds() {
        int[] themeIds = new int[2];
        themeIds[0] = R.style.theme_default_no_translucent;
        themeIds[1] = R.style.theme_night_no_translucent;
        return themeIds;
    }

    private SplashDialog showSplash() {
        if (isDestroy()) {
            DtLog.e("Splash", "Activity isDestoryed");
            return null;
        }
        SplashDialog splashDialog = new SplashDialog(this, new SplashDialog.OnSplashDisappearedCallback() {

            @Override
            public void onSplashDisappearedAnimationStart() {
                if (isDestroy()) {
                    return;
                }

                DtLog.e(TAG, "onSplashDisappearedAnimationStart");
                // 针对LG手机无法正常修改statusbar颜色进行特殊处理
                DeviceUtil.enableTranslucentStatus(MainActivity.this,
                        isNightTheme() ? colorStatusBarNight : colorStatusBarNormal);
            }

            @Override
            public void onSplashDisappearedAnimationEnd() {
                if (isDestroy()) {
                    return;
                }
                DtLog.e(TAG, "onSplashDisappearedAnimationEnd");

                final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
                executeExtraIntent(getIntent());
                final UpgradeInfo upgradeInfo = dengtaApplication.getUpgradeManager().getUpgradeInfo();
                if (upgradeInfo != null) {
                    if (mUpgradeDialogNeedShow) {
                        showUpgradeDialog(upgradeInfo);
                        return;
                    } else if (mBeaconFailed) {
                        showOldVersionDialog();
                        return;
                    }
                }

                if (mTicketErorrDialogNeedShow) {
                    showTicketErrorDialog();
                } if (shouldShowAd()) {
                    showAd();
                }
            }
        });

        try {
            splashDialog.show();
        } catch (Exception e) {
            DtLog.e("Splash error : ", e.getMessage());
            e.printStackTrace();
        }

        splashDialog.setOnKeyListener((dialog, keyCode, event) ->{
                return true;
        });

        splashDialog.setOnDismissListener(dialog -> {
                mSplashDialog = null;
        });

        return splashDialog;
    }

    private void executeExtraIntent(final Intent currentIntent) {
        DtLog.d(TAG, "executeExtraIntent splash receive push message");
        Bundle extra = currentIntent.getExtras();
        if(extra != null && extra.getString("message") != null) {
            DengtaApplication.getApplication().getPushManager().clickNotification(extra.getString("message"));
        } else {
            String action = currentIntent.getAction();
            if (PushClickReceiver.ACTION_SHOW_WEB_PAGE.equals(action)) {
                String url = currentIntent.getStringExtra(CommonWebConst.URL_ADDR);
                FavorItem favorItem = (FavorItem) currentIntent.getSerializableExtra(CommonWebConst.EXTRA_NEWS);
                WebBeaconJump.showWebActivity(MainActivity.this, url, favorItem, Intent.FLAG_ACTIVITY_NEW_TASK);
            } else if (PushClickReceiver.ACTION_SHOW_SECURITY_DETAIL_PAGE.equals(action)) {
                String dtSecCode = currentIntent.getStringExtra(DengtaConst.KEY_SEC_CODE);
                String secName = currentIntent.getStringExtra(DengtaConst.KEY_SEC_NAME);
                CommonBeaconJump.showSecurityDetailActivity(this, dtSecCode, secName);
            } else if (PushClickReceiver.ACTION_SHOW_ACTIVITIES.equals(action)) {
                final String url = currentIntent.getStringExtra(CommonWebConst.URL_ADDR);
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                if (scheme != null) {
                    scheme.handleUrl(this, url);
                }
            } else if (Intent.ACTION_VIEW.equals(action)) { // 处理浏览器的beacon协议
                mBeaconFailed = false;
                final ISchemeManager scheme = (ISchemeManager) ComponentManager.getInstance().getManager(ISchemeManager.class.getName());
                if (scheme != null) {
                    mBeaconFailed = scheme.handleBeaconScheme(this, currentIntent.getData())
                            == CommonConst.PROTOCAL_ERROR_BEACON_NOT_SUPPORT;
                }
            } else if (OcrResultActivity.ACTION_SHOW_IMPORT_RESULT.equals(action)) {
                switchTab(0);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DtLog.d(TAG, "onResume ");
        mPresenter.setResume(true);
        if(mTabManager != null) {
            mTabManager.updateSettingTab();
        }
        mPresenter.refreshDelay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DtLog.d(TAG, "onPause ");
        mPresenter.setResume(false);
        mPresenter.stopRefresh();
        if (mSplashDialog != null && mSplashDialog.isShowing()) {
            mSplashDialog.stopDefaultSplash();
            mSplashDialog.dismiss();
        }
        StatManager.getInstance().save();
    }

    public TabFragmentManager getTabFragmentManager() {
        return mTabManager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DtLog.d(TAG, "onDestroy");

        instance = null;

        DeviceUtil.fixInputMethodManagerLeak(this);

        if (mSplashDialog != null && mSplashDialog.isShowing()) {
            mSplashDialog.dismiss();
        }

        unregisterBroadcastReceiver();

        mPresenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now - mLastBackPressedTime > 1000) {
            DengtaApplication.getApplication().showToast(R.string.press_back_one_more_time_to_exit);
            mLastBackPressedTime = now;
        } else {
            super.onBackPressed();
            DengtaApplication.getApplication().mUiHandler.sendEmptyMessageDelayed(DengtaApplication.MSG_KILL_MAIN_PROCESS, 1000);
        }
    }

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new MainBroadcastReceiver();
            final IntentFilter intentFilter = new IntentFilter(SettingConst.ACTION_RED_DOT_STATE_CHANGED);
            intentFilter.addAction(SettingConst.ACTION_UPGRADE_TIPS);
            intentFilter.addAction(SettingConst.ACTION_TICKET_ERROR);
            intentFilter.addAction(SettingConst.ACTION_FULL_SCREEN_AD);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, intentFilter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private final class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final RedDotManager redDotManager = DengtaApplication.getApplication().getRedDotManager();
            if (SettingConst.ACTION_RED_DOT_STATE_CHANGED.equals(action)) {
                mSettingRedDotView.setVisibility(redDotManager.getMineState() ? View.VISIBLE : View.INVISIBLE);
                mStockPickRedDotView.setVisibility(redDotManager.getStockPickState() ? View.VISIBLE : View.INVISIBLE);
            } else if (SettingConst.ACTION_UPGRADE_TIPS.equals(action)) { // 显示升级提示框
                mUpgradeDialogNeedShow = true;
            } else if (SettingConst.ACTION_TICKET_ERROR.equals(action)) {
                mTicketErorrDialogNeedShow = true;
            } else if(SettingConst.ACTION_FULL_SCREEN_AD.equalsIgnoreCase(action)) {
                mShouldShowFullScreenAd = true;
            }
        }
    }

    private void showTicketErrorDialog() {
        if (isDestroy()) {
            return;
        }

        final ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.setMessage(R.string.ticket_expired_tips);
        dialog.setOkButton(R.string.login_now, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                dialog.dismiss();
            }
        });
        dialog.setCancelButton(R.string.login_later, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showOldVersionDialog() {
        if (isDestroy()) {
            return;
        }

        final CommonDialog dialog = new CommonDialog(this);
        dialog.setMessage(R.string.beacon_not_support_upgrade);
        dialog.addButton(R.string.cancel);
        dialog.addButton(R.string.ok);
        dialog.setButtonClickListener(new CommonDialog.OnDialogButtonClickListener() {
            @Override
            public void onDialogButtonClick(CommonDialog dialog, View view, int position) {
                if (position == 1) {
                    CommonBeaconJump.showUpgrade(MainActivity.this);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 弹出升级提醒对话框
     */
    private void showUpgradeDialog(final UpgradeInfo upgradeInfo) {
        if (isDestroy()) {
            return;
        }

        final Dialog upgradeDialog = new Dialog(this, R.style.dialog_center_theme);
        upgradeDialog.setContentView(R.layout.upgrade_dialog_layout);

        final String upgradeTips = upgradeInfo.sText;
        if (!TextUtils.isEmpty(upgradeTips)) {
            final TextView contentView = (TextView) upgradeDialog.findViewById(R.id.upgrade_text_content);
            contentView.setMovementMethod(ScrollingMovementMethod.getInstance());
            contentView.setText(upgradeTips);
        }

        final Button cancelButton = (Button) upgradeDialog.findViewById(R.id.cancel);

        // 强制更新
        final int status = upgradeInfo.iStatus;
        if (status == 2) {
            cancelButton.setText(R.string.exit);
            upgradeDialog.setCancelable(false);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DtLog.d(TAG, "showUpgradeDialog upgrade_dialog_cancle_btn click");
                upgradeDialog.dismiss();
                // 强制更新
                if (status == 2) {
                    supportFinishAfterTransition();
                }
            }
        });

        upgradeDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DtLog.d(TAG, "showUpgradeDialog upgrade_dialog_ok_btn click");
                // 跳转到正在下载的界面
                CommonBeaconJump.showUpgrade(MainActivity.this);
                // 强制更新
                if (status != 2) {
                    upgradeDialog.dismiss();
                }
            }
        });
        upgradeDialog.show();
        DtLog.d(TAG, "showUpgradeDialog");
    }

    private boolean shouldShowAd() {
        return mShouldShowFullScreenAd;
    }

    private void showAd() {
        if (isDestroy()) {
            return;
        }

        final Bitmap adBmp = DengtaApplication.getApplication().getAdManager().getAdBitmap();
        if(adBmp != null) {
            FullScreenAdDialog dialog = new FullScreenAdDialog(this);
            dialog.setAdBmp(adBmp);
            dialog.setCanCancelOnTouchOutside(false);
            final DtActivityDetail detail = DengtaApplication.getApplication().getAdManager().getAdDetail();
            if(detail != null) {
                dialog.setDetail(detail);
                dialog.setCancelable(false);
                StatisticsUtil.reportAction(StatisticsConst.LIVE_MSG_CLICKED);
                dialog.show();
            }
        }
    }
}