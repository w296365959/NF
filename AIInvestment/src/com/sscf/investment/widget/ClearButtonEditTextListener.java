package com.sscf.investment.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public final class ClearButtonEditTextListener implements TextWatcher, View.OnClickListener, View.OnFocusChangeListener {
    private final EditText mEditText;
    private final View mClearButton;

    public ClearButtonEditTextListener(EditText editText, View clearButton) {
        this.mEditText = editText;
        this.mClearButton = clearButton;
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
        clearButton.setOnClickListener(this);
        mClearButton.setVisibility(mEditText.getText().length() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        mEditText.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mClearButton.setVisibility(mEditText.getText().length() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mClearButton.setVisibility(mEditText.getText().length() > 0 ? View.VISIBLE : View.GONE);
        } else {
            mClearButton.setVisibility(View.GONE);
        }
    }
}
