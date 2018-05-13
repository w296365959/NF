package com.sscf.investment.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;

/**
 * Created by LEN on 2018/4/10.
 */

public class SettingMagicNineActivity extends BaseFragmentActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_magicnine);
        initView();
    }

    private void initView(){
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText(R.string.setting_magicnine);
        TextView tvTitle = (TextView) findViewById(R.id.actionbar_title);
        tvTitle.setText(R.string.setting_magicnine);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
