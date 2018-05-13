package com.sscf.investment.widget.keyboard;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.SettingPref;
import com.sscf.investment.setting.SettingConst;

/**
 * Created by yorkeehuang on 2017/11/27.
 */

public class KeyBoardHelper implements View.OnClickListener, KeyBoard.OnKeyClickListener  {

    private Activity mActivity;
    private EditText mEdit;
    private NumberKeyBoard mNumberKeyBoard;
    private CharacterKeyBoard mCharacterKeyBoard;
    private View mKeyboardContainer;

    public KeyBoardHelper(@NonNull Activity activity, @NonNull EditText edit) {
        mActivity = activity;
        mEdit = edit;
        initEdit(activity, edit);
        mKeyboardContainer = activity.findViewById(R.id.keyboard_container);
        mNumberKeyBoard = (NumberKeyBoard) activity.findViewById(R.id.number_keyboard);
        mCharacterKeyBoard = (CharacterKeyBoard) activity.findViewById(R.id.character_keyboard);
        mNumberKeyBoard.setOnKeyClickListener(this);
        mCharacterKeyBoard.setOnKeyClickListener(this);
        showLastKeyboard();
    }

    private void showLastKeyboard() {
        getLastShowKeyboard().showKeyBoard();
    }

    public int getTop() {
        if(isKeyBoardShow()) {
            return mKeyboardContainer.getTop();
        }
        return 0;
    }

    public void hideLastKeyboard() {
        getLastShowKeyboard().hideKeyBoard();
    }

    public boolean onBackPressed() {
        if(isKeyBoardShow()) {
            hideLastKeyboard();
            return true;
        }
        return false;
    }

    private KeyBoard getLastShowKeyboard() {
        int keyboardState = SettingPref.getInt(SettingConst.KEY_KEYBOARD_STATE, SettingConst.KEY_KEYBOARD_STATE_NUMBER);
        switch (keyboardState) {
            case SettingConst.KEY_KEYBOARD_STATE_CHARACTER:
                return mCharacterKeyBoard;
            default:
                return mNumberKeyBoard;
        }
    }

    private void initEdit(Activity activity, EditText edit) {
        edit.setOnClickListener(this);
        try {
            Class<EditText> cls = EditText.class;
            java.lang.reflect.Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(edit, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(),0);
    }

    @Override
    public void onShortcutNumberKeyClick(String shortcut) {
        if(!TextUtils.isEmpty(shortcut)) {
            insert(shortcut);
        }
    }

    @Override
    public void onCharacterKeyClick(String character) {
        if(!TextUtils.isEmpty(character)) {
            insert(character);
        }
    }

    private void insert(String text) {
        int start = mEdit.getSelectionStart();
        int end = mEdit.getSelectionEnd();
        StringBuffer sb = new StringBuffer(mEdit.getText().toString());
        if (start >= 0) {
            if(end > start) {
                sb.delete(start, end);
                sb.insert(start, text);
                mEdit.setText(sb);
                mEdit.setSelection(start + text.length());
            } else {
                sb.insert(start, text);
                mEdit.setText(sb);
                int len = mEdit.getText() != null ? mEdit.getText().length() : 0;
                if(start + text.length() > len) {
                    mEdit.setSelection(len);
                } else {
                    mEdit.setSelection(start + text.length());
                }
            }
        }
    }

    @Override
    public void onHideKeyClick(KeyBoard keyBoard) {
        keyBoard.hideKeyBoard();
    }

    @Override
    public void onDeleteKeyClick() {
        if (mEdit.length() > 0) {
            int start = mEdit.getSelectionStart();
            int end = mEdit.getSelectionEnd();
            StringBuffer sb = new StringBuffer(mEdit.getText().toString());
            if (start >= 0) {
                if(end > start) {
                    sb = sb.delete(start, end);
                    mEdit.setText(sb);
                    mEdit.setSelection(start);
                } else if(start > 0) {
                    sb = sb.delete(start - 1, start);
                    mEdit.setText(sb);
                    mEdit.setSelection(start > 0 ? start - 1 : 0);
                }
            }
        }
    }

    @Override
    public void onSwitchNumberKeyClick() {
        mCharacterKeyBoard.hideKeyBoard();
        mCharacterKeyBoard.postDelayed(new Runnable() {
            @Override
            public void run() {
                SettingPref.putInt(SettingConst.KEY_KEYBOARD_STATE, SettingConst.KEY_KEYBOARD_STATE_NUMBER);
                mNumberKeyBoard.showKeyBoard();
            }
        }, 300);
    }

    @Override
    public void onSwitchCharacterClick() {
        mNumberKeyBoard.hideKeyBoard();
        mNumberKeyBoard.postDelayed(new Runnable() {
            @Override
            public void run() {
                SettingPref.putInt(SettingConst.KEY_KEYBOARD_STATE, SettingConst.KEY_KEYBOARD_STATE_CHARACTER);
                mCharacterKeyBoard.showKeyBoard();
            }
        }, 300);
    }

    @Override
    public void onSwitchSystemKeyClick(KeyBoard keyBoard) {
        keyBoard.hideKeyBoard();
        mCharacterKeyBoard.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSystemInput();
            }
        }, 300);
    }

    @Override
    public void onSpaceKeyClick() {
        insert(" ");
    }

    @Override
    public void onClearKeyClick() {
        mEdit.getText().clear();
    }

    @Override
    public void onConfirmKeyClick(KeyBoard keyBoard) {
        keyBoard.hideKeyBoard();
    }

    @Override
    public void onClick(View v) {
        if(v == mEdit && !isKeyBoardShow()) {
            mNumberKeyBoard.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLastKeyboard();
                }
            }, 300);
            hideSystemInput();
        }
    }

    private boolean isKeyBoardShow() {
        return mNumberKeyBoard.getVisibility() == View.VISIBLE
                || mCharacterKeyBoard.getVisibility() == View.VISIBLE;
    }

    private void showSystemInput() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideSystemInput() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdit.getWindowToken() , InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
