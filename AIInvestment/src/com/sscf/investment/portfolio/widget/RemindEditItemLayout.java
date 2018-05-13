package com.sscf.investment.portfolio.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DataUtils;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import java.util.regex.Pattern;

public final class RemindEditItemLayout extends RelativeLayout implements TextWatcher, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = RemindEditItemLayout.class.getSimpleName();

    public static final int TYPE_PRICE_UP = 1;
    public static final int TYPE_PRICE_DOWN = 2;
    public static final int TYPE_RATIO_UP = 3;
    public static final int TYPE_RATIO_DOWN = 4;
    public static final int TYPE_NORMAL = 5;

    private int mType;

    private TextView mTextView;
    private EditText mEditText;
    private CheckBox mCheckBox;
    private TextView mHintTextView;
    private final int mFocusColor;
    private final int mUnfocusColor;

    private final int mHintGrayColor;
    private final int mHintRedColor;

    private String mLastInput = "";

    private float mTarget;

    private boolean mInputValid = true;

    /**
     * 区分是用户手动点击check还是edtitext获得焦点setChecked
     */
    private boolean mFocusSetChecked = false;

    private static final Pattern INPUT_PATTERN = Pattern.compile("^[0-9]{0,5}(\\.[0-9]{0,3})?$");

    public RemindEditItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFocusColor = ContextCompat.getColor(getContext(), R.color.default_text_color_100);
        mUnfocusColor = ContextCompat.getColor(getContext(), R.color.default_text_color_100);
        mHintGrayColor = ContextCompat.getColor(getContext(), R.color.default_text_color_80);
        mHintRedColor = 0xffe64b4b;
    }

    @Override
    protected void onFinishInflate() {
        mTextView = (TextView) findViewById(R.id.portfolioRemindTextView);
        mEditText = (EditText) findViewById(R.id.portfolioRemindEditText);
        mCheckBox = (CheckBox) findViewById(R.id.portfolioRemindCheckbox);
        mHintTextView = (TextView) findViewById(R.id.portfolioRemindTextHint);

        mCheckBox.setOnCheckedChangeListener(this);
        mEditText.setOnFocusChangeListener(this);
        mEditText.addTextChangedListener(this);
    }

    public void setData(final float target, final int type) {
        mTarget = target;
        mType = type;
        checkInputValid();
    }

    public void setValue(final int textId, final int hintId, final float value) {
        mTextView.setText(textId);
        mEditText.setHint(hintId);
        if (value > 0) {
            mEditText.setText(String.valueOf(value));
        }
    }

    public float getValue() {
        float value = -1f;
        if (mCheckBox.isChecked()) {
            try {
                value = Float.parseFloat(mEditText.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
                // TODO 格式不对
            }
        }
        return value;
    }

    public boolean isInputValid() {
        if (!mCheckBox.isChecked()) {
            return true;
        }
        return mInputValid;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mFocusSetChecked = true;
            mCheckBox.setChecked(true);
            checkInputValid();
        } else {
            if (mInputValid) {
                mHintTextView.setText("");
            }

            final String input = mEditText.getText().toString();
            if (input.length() == 0) {
                mCheckBox.setChecked(false);
            }
        }

        final boolean focused = hasFocus || mCheckBox.isChecked();
        mTextView.setTextColor(focused ? mFocusColor : mUnfocusColor);
        mEditText.setTextColor(focused ? mFocusColor : mUnfocusColor);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        DtLog.d(TAG, "beforeTextChanged s : " + s);
        mLastInput = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        DtLog.d(TAG, "onTextChanged s : " + s);
        final String input = s.toString();
        if (mLastInput.equals(input)) {
            return;
        }
        if (INPUT_PATTERN.matcher(input).matches()) {
            return;
        }
        mEditText.setText(mLastInput);

        final String text = mEditText.getText().toString();
        if (text.length() > 0) {
            mEditText.setSelection(text.length() );
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        DtLog.d(TAG, "afterTextChanged s : " + s);
        if (mEditText.isFocused()) {
            checkInputValid();
        }
    }

    private void checkInputValid() {
        final String input = mEditText.getText().toString();
        if (input.length() > 0 && mTarget > 0) {
            float inputNumber = -1;
            try {
                inputNumber = Float.parseFloat(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (inputNumber > 0) {
                mFocusSetChecked = true;
                mCheckBox.setChecked(true);
                switch (mType) {
                    case TYPE_PRICE_UP:
                        if (inputNumber <= mTarget) {
                            mHintTextView.setText(R.string.portfolio_remind_input_hint_1);
                            mHintTextView.setTextColor(mHintRedColor);
                            mInputValid = false;
                        } else {
                            if (mEditText.isFocused()) {
                                mHintTextView.setText(mHintTextView.getContext().getString(R.string.portfolio_remind_input_hint_2,
                                        DataUtils.rahToStr(inputNumber * 100 / mTarget - 100, 2)));
                                mHintTextView.setTextColor(mHintGrayColor);
                            }
                            mInputValid = true;
                        }
                        break;
                    case TYPE_PRICE_DOWN:
                        if (inputNumber >= mTarget) {
                            mHintTextView.setText(R.string.portfolio_remind_input_hint_3);
                            mHintTextView.setTextColor(mHintRedColor);
                            mInputValid = false;
                        } else {
                            if (mEditText.isFocused()) {
                                mHintTextView.setText(mHintTextView.getContext().getString(R.string.portfolio_remind_input_hint_4,
                                        DataUtils.rahToStr(100 - inputNumber * 100 / mTarget, 2)));
                                mHintTextView.setTextColor(mHintGrayColor);
                            }
                            mInputValid = true;
                        }
                        break;
                    case TYPE_RATIO_UP:
                        mHintTextView.setText("");
                        mInputValid = true;
                        break;
                    case TYPE_RATIO_DOWN:
                        mHintTextView.setText("");
                        mInputValid = true;
                        break;
                    case TYPE_NORMAL:
                        mHintTextView.setText("");
                        mInputValid = true;
                        break;
                    default:
                        break;
                }
            } else if (inputNumber <= 0) {
                mHintTextView.setText(R.string.portfolio_remind_input_hint_5);
                mHintTextView.setTextColor(mHintRedColor);
                mInputValid = false;
            }
        } else {
            if (mHintTextView != null) {
                mHintTextView.setText("");
            }
            mInputValid = true;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!mFocusSetChecked) {
                mEditText.requestFocus();
                checkInputValid();
                mEditText.setSelection(mEditText.length());
                DeviceUtil.showInputMethod(mEditText);
            }
        } else {
            mHintTextView.setText("");
            if (!mEditText.isFocused()) {
                mTextView.setTextColor(mUnfocusColor);
                mEditText.setTextColor(mUnfocusColor);
            }
        }
        mFocusSetChecked = false;
    }

}
