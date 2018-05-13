package com.sscf.investment.setting;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.widgt.SeekBarLayout;

/**
 * Created by LEN on 2018/3/21.
 */

public class SettingOBVActivity  extends BaseFragmentActivity implements View.OnClickListener {

    public static final String KEY_CANDICATOR_TYPE = "key_candicator_type";

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_obv);
        type = getIntent().getStringExtra(KEY_CANDICATOR_TYPE);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(type);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        ((TextView) findViewById(R.id.title)).setText(type);

        new Handler().postDelayed(new Runnable() { // 解决三星s6 edge更换字体后字体被截断的问题
            @Override
            public void run() {
                ((TextView) findViewById(R.id.intro)).setText(getIntroductionResId());
            }
        }, 0L);
    }

    private int getIntroductionResId(){
        if (type.equals("OBV")){
            return R.string.setting_obv_intro;
        }else if (type.equals("横盘突破")){
            return R.string.setting_break_intro;
        }else if (type.equals("牛熊转换")){
            return R.string.setting_bull_intro;
        }else if (type.equals("跳空缺口")){
            return R.string.setting_gap_intro;
        }else if (type.equals("多空信号")){
            return R.string.setting_dk_intro;
        }
        return R.string.setting_obv_intro;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
        }
    }
}
