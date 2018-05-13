package com.sscf.investment.widget.keyboard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.IdiUtils;

/**
 * Created by yorkeehuang on 2017/11/24.
 */

public abstract class KeyBoard extends ConstraintLayout implements View.OnClickListener, View.OnTouchListener, Handler.Callback {

    private static final String TAG = KeyBoard.class.getSimpleName();
    private int mKeyBoardHeight;
    protected OnKeyClickListener mOnKeyClickListener;
    private Handler mUIHandler;

    public KeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mKeyBoardHeight = getResources().getDimensionPixelSize(R.dimen.keyboard_height);
        mUIHandler = new Handler(this);
    }

    public void setOnKeyClickListener(OnKeyClickListener listener) {
        mOnKeyClickListener = listener;
    }

    protected View createKeyView(KeyInfo keyInfo) {
        View keyView = View.inflate(getContext(), getLayoutRes(keyInfo), null);
        initKeyView(keyInfo, keyView);
        return keyView;
    }

    protected abstract int getLayoutRes(KeyInfo keyInfo);

    private void initKeyView(KeyInfo keyInfo, View keyView) {
        keyView.setTag(keyInfo);
        keyView.setId(keyInfo.id);
        keyView.setOnClickListener(this);
        if(keyInfo.type == KeyInfo.KEY_TYPE_DELETE) {
            keyView.setOnTouchListener(this);
        }
        setInfo(keyInfo, keyView);
        setKeyBackground(keyView, keyInfo.type);
    }

    protected void setInfo(KeyInfo keyInfo, View keyView) {
        if(isTextKey(keyInfo)) {
            TextKeyInfo textKeyInfo = (TextKeyInfo) keyInfo;
            TextView textView = (TextView) keyView;
            textView.setText(textKeyInfo.text);
            textView.setTextSize(textKeyInfo.textSize);
        } else {
            ImageKeyInfo imageKeyInfo = (ImageKeyInfo) keyInfo;
            ImageView imageView = (ImageView) keyView;
            imageView.setImageResource(imageKeyInfo.iconRes);
        }
    }

    protected abstract void setKeyBackground(View keyView, int type);

    protected abstract boolean isTextKey(KeyInfo keyInfo);

    public void showKeyBoard() {
        if(getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(this,
                    "translationY",
                    mKeyBoardHeight, 0f).setDuration(300).start();
        }
    }

    public void hideKeyBoard() {
        if(getVisibility() == View.VISIBLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this,
                    "translationY",
                    0f, mKeyBoardHeight).setDuration(300);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }
    }

    @Override
    public void onClick(View v) {
        DtLog.d(TAG, "onClick()");
        if(v.getTag() != null && v.getTag() instanceof KeyInfo) {
            if(mOnKeyClickListener != null) {
                KeyInfo keyInfo = (KeyInfo) v.getTag();
                switch (keyInfo.type) {
                    case KeyInfo.KEY_TYPE_CHARACTER:
                        mOnKeyClickListener.onCharacterKeyClick(((CharacterKeyInfo) keyInfo).text);
                        break;
                    case KeyInfo.KEY_TYPE_HIDE:
                        mOnKeyClickListener.onHideKeyClick(this);
                        break;
                    case KeyInfo.KEY_TYPE_DELETE:
                        mOnKeyClickListener.onDeleteKeyClick();
                        break;
                    case KeyInfo.KEY_TYPE_SWITCH_NUMBER:
                        mOnKeyClickListener.onSwitchNumberKeyClick();
                        break;
                    case KeyInfo.KEY_TYPE_SWITCH_CHARACTER:
                        mOnKeyClickListener.onSwitchCharacterClick();
                        break;
                    case KeyInfo.KEY_TYPE_SWITCH_SYSTEM:
                        mOnKeyClickListener.onSwitchSystemKeyClick(this);
                        break;
                    case KeyInfo.KEY_TYPE_SPACE:
                        mOnKeyClickListener.onSpaceKeyClick();
                        break;
                    case KeyInfo.KEY_TYPE_CONFIRM:
                        mOnKeyClickListener.onConfirmKeyClick(this);
                        break;
                    case KeyInfo.KEY_TYPE_SHORTCUT_NUMBER:
                        mOnKeyClickListener.onShortcutNumberKeyClick(((TextKeyInfo) keyInfo).text);
                        break;
                    case KeyInfo.KEY_TYPE_CLEAR:
                        mOnKeyClickListener.onClearKeyClick();
                        break;
                    default:
                }
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        if(v.getTag() != null && v.getTag() instanceof KeyInfo) {
            if(mOnKeyClickListener != null) {
                KeyInfo keyInfo = (KeyInfo) v.getTag();
                switch (keyInfo.type) {
                    case KeyInfo.KEY_TYPE_DELETE:
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mUIHandler.removeMessages(KeyInfo.KEY_TYPE_DELETE);
                                mUIHandler.sendEmptyMessageDelayed(KeyInfo.KEY_TYPE_DELETE, 500);
                                break;
                            case MotionEvent.ACTION_UP:
                                mUIHandler.removeMessages(KeyInfo.KEY_TYPE_DELETE);
                                break;
                            default:
                        }
                        break;
                }
            }
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUIHandler.removeMessages(KeyInfo.KEY_TYPE_DELETE);
    }

    public boolean handleMessage(Message msg) {
        DtLog.d(TAG, "handleMessage() msg.what = " + msg.what);
        switch (msg.what) {
            case KeyInfo.KEY_TYPE_DELETE:
                if(mOnKeyClickListener != null) {
                    mOnKeyClickListener.onDeleteKeyClick();
                }
                mUIHandler.sendEmptyMessageDelayed(KeyInfo.KEY_TYPE_DELETE, 50);
                break;
            default:
        }
        return true;
    }

    class KeyInfo {
        public static final int KEY_TYPE_CHARACTER = 1;
        public static final int KEY_TYPE_HIDE = 2;
        public static final int KEY_TYPE_DELETE = 3;
        public static final int KEY_TYPE_SWITCH_NUMBER = 4;
        public static final int KEY_TYPE_SWITCH_CHARACTER = 5;
        public static final int KEY_TYPE_SWITCH_SYSTEM = 6;
        public static final int KEY_TYPE_SPACE = 7;
        public static final int KEY_TYPE_CONFIRM = 8;
        public static final int KEY_TYPE_SHORTCUT_NUMBER = 9;
        public static final int KEY_TYPE_CLEAR = 10;

        int id;
        int type;

        KeyInfo(int type) {
            this.type = type;
            this.id = IdiUtils.generateViewId();
        }
    }

    class TextKeyInfo extends KeyInfo {
        String text;
        int textSize;

        TextKeyInfo(int type, String text, int textSize) {
            super(type);
            this.text = text;
            this.textSize = textSize;
        }
    }

    class CharacterKeyInfo extends TextKeyInfo {

        CharacterKeyInfo(String text) {
            super(KEY_TYPE_CHARACTER, text, 20);
        }
    }

    class ImageKeyInfo extends KeyInfo {
        int iconRes;
        ImageKeyInfo(int iconRes, int type) {
            super(type);
            this.iconRes = iconRes;
        }
    }

    interface OnKeyClickListener {
        void onShortcutNumberKeyClick(String shortcut);

        void onCharacterKeyClick(String character);

        void onHideKeyClick(KeyBoard keyBoard);

        void onDeleteKeyClick();

        void onSwitchNumberKeyClick();

        void onSwitchCharacterClick();

        void onSwitchSystemKeyClick(KeyBoard keyBoard);

        void onSpaceKeyClick();

        void onClearKeyClick();

        void onConfirmKeyClick(KeyBoard keyBoard);
    }
}
