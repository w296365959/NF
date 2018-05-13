package com.sscf.investment.detail.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.utils.StringUtil;

import BEC.SeniorExecutive;

/**
 * Created by yorkeehuang on 2017/6/29.
 */

public class ExecutiveProfileView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = ExecutiveProfileView.class.getSimpleName();

    private TextView mNameView;
    private TextView mAgeView;
    private TextView mEducationView;
    private TextView mDutyView;

    private TextView mDurationView;
    private TextView mShareAmountView;
    private TextView mPaidView;
    private TextView mProfileView;

    private View mProfileDetail;

    private Drawable mTriangleOpenDrawable;
    private Drawable mTriangleCloseDrawable;

    private boolean mIsOpen = false;

    private SeniorExecutive mSeniorExecutive;

    public ExecutiveProfileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTriangleOpenDrawable = ContextCompat.getDrawable(context, R.drawable.triangle_item_open);
        mTriangleCloseDrawable = ContextCompat.getDrawable(context, R.drawable.triangle_item_close);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.executive_info).setOnClickListener(this);
        mNameView = (TextView) findViewById(R.id.name);
        mAgeView = (TextView) findViewById(R.id.age);
        mEducationView = (TextView) findViewById(R.id.education);
        mDutyView = (TextView) findViewById(R.id.duty);
        mDurationView = (TextView) findViewById(R.id.duration);
        mShareAmountView = (TextView) findViewById(R.id.share_amount);
        mPaidView = (TextView) findViewById(R.id.paid);
        mProfileView = (TextView) findViewById(R.id.profile);
        mProfileDetail = findViewById(R.id.profile_detail);
    }

    public void setData(SeniorExecutive seniorExecutive) {
        mNameView.setText(seniorExecutive.getSName());
        mSeniorExecutive = seniorExecutive;
        int age = seniorExecutive.getIAge();
        if(age > 0 && age < 200) {
            mAgeView.setText(String.valueOf(age));
        } else {
            mAgeView.setText("--");
        }

        String edu = seniorExecutive.getSEdu();
        if(!TextUtils.isEmpty(edu)) {
            mEducationView.setText(edu);
        } else {
            mEducationView.setText("--");
        }

        String business = seniorExecutive.getSBusiness();
        if(!TextUtils.isEmpty(business)) {
            mDutyView.setText(business);
        } else {
            mDutyView.setText("--");
        }

        mDurationView.setText(seniorExecutive.getSTimeofOffice());
        float holdNum = seniorExecutive.getFHoldNum();
        if(holdNum > 0) {
            if(holdNum > 10000) {
                mShareAmountView.setText(StringUtil.getFormatedFloat(holdNum / 10000) + "亿股");
            } else {
                mShareAmountView.setText(StringUtil.getFormatedFloat(holdNum) + "万股");
            }
        } else {
            mShareAmountView.setText("--");
        }

        float pay = seniorExecutive.getFPay();
        if(pay > 0) {
            if(pay > 10000) {
                mPaidView.setText("薪酬" + StringUtil.getFormatedFloat(pay / 10000) + "亿");
            } else {
                mPaidView.setText("薪酬" + StringUtil.getFormatedFloat(pay) + "万");
            }
        } else {
            mPaidView.setText("薪酬 --");
        }

        mProfileView.setText("简介:" + seniorExecutive.getSIntroduce());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.executive_info:
                if(mIsOpen) {
                    mProfileDetail.setVisibility(View.GONE);
                    mNameView.setCompoundDrawablesWithIntrinsicBounds(null, null, mTriangleOpenDrawable, null);
                } else {
                    mProfileDetail.setVisibility(View.VISIBLE);
                    mNameView.setCompoundDrawablesWithIntrinsicBounds(null, null, mTriangleCloseDrawable, null);
                }
                mIsOpen = !mIsOpen;
                break;
            default:
        }
    }
}
