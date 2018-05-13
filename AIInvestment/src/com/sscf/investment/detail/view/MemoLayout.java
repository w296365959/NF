package com.sscf.investment.detail.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.detail.MemoEditActivity;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.router.CommonBeaconJump;

/**
 * Created by liqf on 2016/8/2.
 */
public class MemoLayout extends LinearLayout implements View.OnClickListener {
    private TextView mContentText;
    private String mDtSecCode;
    private String mSecName;

    public MemoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentText = (TextView) findViewById(R.id.text);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
            MemoEditActivity.show(getContext(), mDtSecCode, mSecName);
        } else {
            CommonBeaconJump.showLogin(getContext());
        }
    }

    public void setDtSecCode(final String dtSecCode, final String secName) {
        mDtSecCode = dtSecCode;
        mSecName = secName;
    }

    public void setComment(String comment) {
        if (TextUtils.isEmpty(comment)) {
            setVisibility(GONE);
            return;
        }
        mContentText.setText(comment);
        setVisibility(VISIBLE);
    }
}
