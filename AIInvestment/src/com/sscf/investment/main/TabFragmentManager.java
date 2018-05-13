package com.sscf.investment.main;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import com.sscf.investment.R;
import com.sscf.investment.consultant.TabFragmentConsultant;
import com.sscf.investment.discover.TabFragmentDiscover;
import com.sscf.investment.information.TabFragmentInformation;
import com.sscf.investment.portfolio.TabFragmentPortfolioOrMarket;
import com.sscf.investment.setting.TabFragmentSetting;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;

/**
 * Created by xuebinliu on 2015/7/22.
 *
 * 管理Tab fragment切换
 */
public class TabFragmentManager implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "TabFragmentManager";

    // our tab fragments
    public final Fragment[] mTabFragments;

    public final RadioButton[] mTabButtons;

    // tab fragment所属的Activity
    private MainActivity mMainActivity;

    // fragment的显示区域id
    private int mTabFragmentContentId;

    // 当前tab页面索引
    private int mCurrentTab = -1;

    // 用于把tab切换通知到关系的模块
    private OnCheckedChangedListener mOnCheckedChangedListener;

    private TabFragmentPortfolioOrMarket tabFragmentPortfolioOrMarket;
    private TabFragmentInformation tabFragmentInformation;
    private TabFragmentConsultant tabFragmentConsultant;
    private TabFragmentDiscover tabFragmentDiscover;
    private TabFragmentSetting tabFragmentSetting;

    public TabFragmentManager(MainActivity mainActivity, int fragmentContentId) {
        DtLog.d(TAG, "new TabFragmentManager fragmentActivity=" + mainActivity + ", fragmentContentId=" + fragmentContentId);

        this.mMainActivity = mainActivity;
        this.mTabFragmentContentId = fragmentContentId;

        final int TAB_SIZE = 4;

        mTabButtons = new RadioButton[TAB_SIZE];
        mTabButtons[0] = (RadioButton) mMainActivity.findViewById(R.id.tab_rb_portfolio);
        mTabButtons[1] = (RadioButton) mMainActivity.findViewById(R.id.tab_rb_stock_pick);
//        mTabButtons[2] = (RadioButton) mMainActivity.findViewById(R.id.tab_rb_consultant);
        mTabButtons[2] = (RadioButton) mMainActivity.findViewById(R.id.tab_rb_information);
        mTabButtons[3] = (RadioButton) mMainActivity.findViewById(R.id.tab_rb_setting);

        RadioButton radioButton = null;
        for (int i = 0; i < TAB_SIZE; i++) {
            radioButton = mTabButtons[i];
            radioButton.setChecked(false);
            radioButton.setOnCheckedChangeListener(this);
            // tag里保存index
            radioButton.setTag(i);
        }

        mTabFragments = new Fragment[TAB_SIZE];
        mTabFragments[0] = tabFragmentPortfolioOrMarket = new TabFragmentPortfolioOrMarket();//自选
        mTabFragments[1] = tabFragmentDiscover = new TabFragmentDiscover();//选股
//        mTabFragments[2] = tabFragmentConsultant = new TabFragmentConsultant();
        mTabFragments[2] = tabFragmentInformation = new TabFragmentInformation();//资讯
        mTabFragments[3] = tabFragmentSetting = new TabFragmentSetting();

        // 启动默认显示第一页
        switchTab(0);
    }

    public void updateSettingTab() {
        boolean isMember = DengtaApplication.getApplication().getAccountManager().isMember();
        int res = isMember ?
                R.drawable.tab_icon_setting_vip_selector
                : R.drawable.tab_icon_setting_selector;
        int textColorRes = isMember ? R.color.main_bottom_tab_bar_vip_text_selector
                : R.color.main_bottom_tab_bar_text_selector;
        RadioButton settingButton = mTabButtons[3];
//        settingButton.setTextColor(AppCompatResources.getColorStateList(mMainActivity, textColorRes));
        settingButton.setTextColor(AppCompatResources.getColorStateList(mMainActivity, textColorRes));
        settingButton.setCompoundDrawablesWithIntrinsicBounds(0, res, 0, 0);
    }

    public void switchTab(final int tabIndex) {
        if (tabIndex < mTabButtons.length) {
            mTabButtons[tabIndex].setChecked(true);
        }
    }

    public TabFragmentPortfolioOrMarket getTabFragmentPortfolioOrMarket() {
        return tabFragmentPortfolioOrMarket;
    }

    public TabFragmentInformation getTabFragmentInformation() {
        return tabFragmentInformation;
    }

    public TabFragmentSetting getTabFragmentSetting() {
        return tabFragmentSetting;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        DtLog.d(TAG, "onCheckedChanged");
        if (isChecked) {
            final int lastTab = mCurrentTab;
            if (lastTab >= 0) {
                mTabButtons[lastTab].setChecked(false);
            }
            final Integer checkedIndex = (Integer) buttonView.getTag();

            showFragment(checkedIndex, lastTab);

            // notify owner
            if(null != mOnCheckedChangedListener){
                mOnCheckedChangedListener.OnCheckedChanged(buttonView.getId(), checkedIndex);
            }

            mCurrentTab = checkedIndex;

            switch (checkedIndex) {
                case 0://自选
                    break;
                case 1://选股
                    StatisticsUtil.reportAction(StatisticsConst.A_MACD_CLICK);
                    break;
                case 2://资讯
                    StatisticsUtil.reportAction(StatisticsConst.INFORMATION_DISPLAY);
                    break;
                case 3://我的

                    break;
                default:
                    break;
            }
        }
    }

    private void showFragment(final int currentIndex, final int lastIndex) {
        if (mMainActivity.isDestroy()) {
            return;
        }
        final FragmentTransaction ft = obtainFragmentTransaction();
        if (lastIndex >= 0) {
            final Fragment lastFragment = mTabFragments[lastIndex];
            if (lastFragment != null) {
                ft.hide(lastFragment);
            }
        }
        final Fragment currentFragment = mTabFragments[currentIndex];
        if (currentFragment.isAdded()) {
            ft.show(currentFragment);
        } else {
            ft.add(mTabFragmentContentId, currentFragment);
            currentFragment.setUserVisibleHint(true);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 获取一个带动画的FragmentTransaction
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(){
        FragmentTransaction ft = mMainActivity.getSupportFragmentManager().beginTransaction();
        // 设置切换动画
//        if(index > mCurrentTab){
//            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
//        }else{
//            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
//        }
        return ft;
    }

    public void setOnCheckedChangedListener(OnCheckedChangedListener onCheckedChangedListener) {
        this.mOnCheckedChangedListener = onCheckedChangedListener;
    }

    /**
     *  监听tab切换
     */
    interface OnCheckedChangedListener{
        void OnCheckedChanged(int checkedId, int index);
    }
}
