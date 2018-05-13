package com.sscf.investment.stare.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.entity.db.StockDbEntity;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.CommonConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.stare.ui.presenter.StarePresenter;
import com.sscf.investment.stare.ui.widget.SubmitableFragment;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;

/**
 * Created by yorkeehuang on 2017/9/11.
 */
@Route("SmartStockStareActivity")
public class SmartStockStareActivity extends BaseFragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, Submitable {

    private static final String TAG = SmartStockStareActivity.class.getSimpleName();

    private static final int TAB_SIZE = 3;

    private StarePresenter mPresenter;

    private RadioButton[] mTabs = new RadioButton[TAB_SIZE];

    private SubmitableFragment[] mFragments = new SubmitableFragment[TAB_SIZE];

    private CheckBox mAiAlertCheckbox;

    // 当前tab页面索引
    private int mCurrentTab = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        final String unicode = getIntent().getStringExtra(CommonConst.KEY_SEC_CODE);
        final String name = getIntent().getStringExtra(CommonConst.KEY_SEC_NAME);
        DtLog.d(TAG, "onCreate() unicode = " + unicode + ", name = " + name);
        if(TextUtils.isEmpty(unicode)) {
            finish();
            return;
        } else {
            DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
            setContentView(R.layout.activity_smart_stock_stare);
            initView(name);
            if(attachPresenter(unicode)) {
                initTabs();
                mPresenter.requestSimpleQuote(unicode);
            } else {
                finish();
                return;
            }
        }
    }

    private boolean attachPresenter(String unicode) {
        if(!TextUtils.isEmpty(unicode)) {
            IPortfolioDataManager manager = (IPortfolioDataManager) ComponentManager.getInstance().getManager(IPortfolioDataManager.class.getName());
            if(manager != null) {
                StockDbEntity stockDbEntity = manager.getStockDbEntity(unicode);
                if(stockDbEntity != null) {
                    SubmitableFragment[] fragments = initFragments();
                    mPresenter = new StarePresenter(this, fragments, stockDbEntity);
                    for(SubmitableFragment fragment : fragments) {
                        fragment.setPresenter(mPresenter);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void initTabs() {
        mTabs[0] = (RadioButton) findViewById(R.id.price_tab);
        mTabs[1] = (RadioButton) findViewById(R.id.event_tab);
        mTabs[2] = (RadioButton) findViewById(R.id.strategy_tab);

        for(int i=0, size=mTabs.length; i<size; i++) {
            RadioButton tab = mTabs[i];
            tab.setOnCheckedChangeListener(this);
            tab.setTag(i);
        }
        mTabs[0].setChecked(true);
    }

    private void initView(String name) {
        initActionbar();

        ((TextView) findViewById(R.id.stock_stare_title)).setText(name);
        mAiAlertCheckbox = initCheckboxItem(R.id.stock_stare_ai_remind,
                R.string.smart_stock_stare_ai_remind_title,
                R.string.smart_stock_stare_ai_remind_sub_title);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll);
        scrollView.post(() -> scrollView.scrollTo(0, 0));
    }

    private void initActionbar() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.smart_stock_stare_actionbar_title);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        final TextView rightButton = (TextView) findViewById(R.id.actionbar_right_button);
        rightButton.setOnClickListener(this);
        rightButton.setText(R.string.finish);
    }

    private SubmitableFragment[] initFragments() {
        for(int i=0, size=mTabs.length; i<size; i++) {
            SubmitableFragment fragment = createFragment(i);
            if(fragment != null) {
                mFragments[i] = fragment;
            }
        }
        return mFragments;
    }

    private SubmitableFragment createFragment(int index) {
        switch (index) {
            case 0:
                return new PriceStareFragment();
            case 1:
                return new EventStareFragment();
            case 2:
                return new StradegyStareFragment();
            default:
        }
        return null;
    }

    private CheckBox initCheckboxItem(int itemId, int title, int subTitle) {
        View item = findViewById(itemId);
        TextView titleView = (TextView) item.findViewById(R.id.smart_stock_stare_title);
        titleView.setText(title);
        TextView subTitleView = (TextView) item.findViewById(R.id.smart_stock_stare_sub_title);
        subTitleView.setText(subTitle);
        CheckBox checkBox = (CheckBox) item.findViewById(R.id.smart_stock_stare_checkbox);
//        ImageView memberView = (ImageView) item.findViewById(R.id.member);
//        if(showMember) {
//            memberView.setVisibility(View.VISIBLE);
//            memberView.setOnClickListener(this);
//            checkBox.setClickable(true);
//            checkBox.setOnCheckedChangeListener(this);
//        } else {
//            memberView.setVisibility(View.GONE);
//        }
        return checkBox;
    }

    public void setStockPrice(CharSequence price) {
        ((TextView)findViewById(R.id.stock_stare_price)).setText(price);
    }

    public void setStockRatio(CharSequence ratio) {
        ((TextView)findViewById(R.id.stock_stare_ratio)).setText(ratio);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView instanceof RadioButton) {
            if(isChecked) {
                setCheck(buttonView);
            }
        }
    }

    private boolean isMember() {
        IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance().getManager(IAccountManager.class.getName());
        if(accountManager != null) {
            return accountManager.isMember();
        }
        return false;
    }

    private void setCheck(CompoundButton selected) {
        if(selected != null && selected.getTag() != null && selected.getTag() instanceof Integer) {
            int index = (Integer) selected.getTag();
            for(int i=0, size=mTabs.length; i<size; i++) {
                if(i == index) {
                    final int lastTab = mCurrentTab;
                    showFragment(i, lastTab);
                    mCurrentTab = i;
                } else {
                    RadioButton tab = mTabs[i];
                    tab.setChecked(false);
                }
            }
        }
    }

    private void showFragment(final int currentIndex, final int lastIndex) {
        if (isDestroy()) {
            return;
        }
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (lastIndex >= 0) {
            final Fragment lastFragment = mFragments[lastIndex];
            if (lastFragment != null) {
                ft.hide(lastFragment);
                lastFragment.setUserVisibleHint(false);
            }
        }
        final Fragment currentFragment = mFragments[currentIndex];
        if (currentFragment.isAdded()) {
            ft.show(currentFragment);
        } else {
            ft.add(R.id.tab_content, currentFragment);
        }
        currentFragment.setUserVisibleHint(true);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                if(mPresenter.cancel()) {
                    finish();
                }
                break;
            case R.id.actionbar_right_button:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        switch (mPresenter.submit()) {
            case Submitable.RESULT_INVALID:
                // TODO 提示数据错误
                break;
            case Submitable.RESULT_SHOULD_SUBMIT:
                finish();
                break;
            default:
                finish();
        }

    }

    @Override
    public void initValue(StockDbEntity stockEntity) {
        mAiAlertCheckbox.setChecked(stockEntity.isAiAlert());
    }

    @Override
    public int checkInput(StockDbEntity stockEntity) {
        int result = Submitable.RESULT_NOCHANGE;
        boolean isAiAlert = mAiAlertCheckbox.isChecked();
        if(isAiAlert != stockEntity.isAiAlert()) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }

        return result;
    }

    @Override
    public int submit(StockDbEntity stockEntity) {
        int result = checkInput(stockEntity);

        if(result == Submitable.RESULT_SHOULD_SUBMIT) {
            stockEntity.setAiAlert(mAiAlertCheckbox.isChecked());
        }

        return result;
    }

    @Override
    public void onBackPressed() {
        if(mPresenter.cancel()) {
            super.onBackPressed();
        }
    }
}
