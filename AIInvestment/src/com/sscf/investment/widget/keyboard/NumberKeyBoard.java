package com.sscf.investment.widget.keyboard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yorkeehuang on 2017/11/23.
 */

public class NumberKeyBoard extends KeyBoard {

    private static final String TAG = NumberKeyBoard.class.getSimpleName();

    private int mKeyHeight, mKeyWidth, mLineMarginBottom;

    private List<List<KeyInfo>> mKeyLines = new ArrayList<>(4);

    public NumberKeyBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int paddintTop = DeviceUtil.dip2px(context, 8);
        int paddintLeft = DeviceUtil.dip2px(context, 8);
        int paddingRight = paddintLeft;
        setPadding(paddintLeft, paddintTop, paddingRight, 0);

        mKeyHeight = DeviceUtil.dip2px(context, 48);
        mKeyWidth = DeviceUtil.dip2px(context, 62);
        mLineMarginBottom = DeviceUtil.dip2px(context, 6);
        initKeys();
    }

    private void initKeys() {
        // 第一行
        List<KeyInfo> lineKeyInfos = new ArrayList<>(5);
        lineKeyInfos.add(new TextKeyInfo(TextKeyInfo.KEY_TYPE_SHORTCUT_NUMBER, "600", 18));
        lineKeyInfos.add(new CharacterKeyInfo("1"));
        lineKeyInfos.add(new CharacterKeyInfo("2"));
        lineKeyInfos.add(new CharacterKeyInfo("3"));
        lineKeyInfos.add(new ImageKeyInfo(R.drawable.delete_icon, TextKeyInfo.KEY_TYPE_DELETE));
        mKeyLines.add(lineKeyInfos);

        // 第二行
        lineKeyInfos = new ArrayList<>(5);
        lineKeyInfos.add(new TextKeyInfo(TextKeyInfo.KEY_TYPE_SHORTCUT_NUMBER, "000", 18));
        lineKeyInfos.add(new CharacterKeyInfo("4"));
        lineKeyInfos.add(new CharacterKeyInfo("5"));
        lineKeyInfos.add(new CharacterKeyInfo("6"));
        lineKeyInfos.add(new TextKeyInfo(TextKeyInfo.KEY_TYPE_HIDE, "隐藏", 15));
        mKeyLines.add(lineKeyInfos);

        // 第三行
        lineKeyInfos = new ArrayList<>(5);
        lineKeyInfos.add(new TextKeyInfo(TextKeyInfo.KEY_TYPE_SHORTCUT_NUMBER, "002", 18));
        lineKeyInfos.add(new CharacterKeyInfo("7"));
        lineKeyInfos.add(new CharacterKeyInfo("8"));
        lineKeyInfos.add(new CharacterKeyInfo("9"));
        lineKeyInfos.add(new TextKeyInfo(TextKeyInfo.KEY_TYPE_CLEAR, "清空", 15));
        mKeyLines.add(lineKeyInfos);

        // 第四行
        lineKeyInfos = new ArrayList<>(5);
        lineKeyInfos.add(new TextKeyInfo(KeyInfo.KEY_TYPE_SHORTCUT_NUMBER, "300", 18));
        lineKeyInfos.add(new TextKeyInfo(KeyInfo.KEY_TYPE_SWITCH_CHARACTER, "ABC", 15));
        lineKeyInfos.add(new CharacterKeyInfo("0"));
        lineKeyInfos.add(new TextKeyInfo(KeyInfo.KEY_TYPE_SWITCH_SYSTEM, "系统", 15));
        lineKeyInfos.add(new TextKeyInfo(KeyInfo.KEY_TYPE_CONFIRM, "确定", 15));
        mKeyLines.add(lineKeyInfos);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for(int i=0; i< 4; i++) {
            initLineButtons(i);
        }
    }

    private void initLineButtons(int line) {
        List<KeyInfo> lineKeys = mKeyLines.get(line);

        int topId = line > 0 ? mKeyLines.get(line - 1).get(0).id : LayoutParams.PARENT_ID;

        for(int i=0, size=lineKeys.size(); i<size; i++) {
            KeyInfo keyInfo = lineKeys.get(i);
            View view =  createKeyView(keyInfo);
            LayoutParams lp = new LayoutParams(mKeyWidth, mKeyHeight);
            lp.horizontalChainStyle = LayoutParams.CHAIN_SPREAD_INSIDE;

            if(topId == LayoutParams.PARENT_ID) {
                lp.topToTop = LayoutParams.PARENT_ID;
            } else {
                lp.topToBottom = topId;
                lp.topMargin = mLineMarginBottom;
            }


            if(i == 0) {
                lp.leftToLeft = LayoutParams.PARENT_ID;
                lp.rightMargin = DeviceUtil.dip2px(getContext(), 11);
            } else {
                lp.leftToRight = lineKeys.get(i - 1).id;
            }

            if(i == size - 1) {
                lp.leftMargin = DeviceUtil.dip2px(getContext(), 11);
                lp.rightToRight = LayoutParams.PARENT_ID;
            } else {
                lp.rightToLeft = lineKeys.get(i + 1).id;
            }

            addView(view, lp);
        }
    }


    @Override
    protected int getLayoutRes(KeyInfo keyInfo) {
        switch (keyInfo.type) {
            case KeyInfo.KEY_TYPE_DELETE:
                return R.layout.image_key_view;
            default:
                return R.layout.text_key_view;
        }
    }

    @Override
    protected void setKeyBackground(View keyView, int type) {
        switch (type) {
            case KeyInfo.KEY_TYPE_HIDE:
            case KeyInfo.KEY_TYPE_DELETE:
            case KeyInfo.KEY_TYPE_SWITCH_CHARACTER:
            case KeyInfo.KEY_TYPE_SWITCH_SYSTEM:
            case KeyInfo.KEY_TYPE_CONFIRM:
            case KeyInfo.KEY_TYPE_CLEAR:
                keyView.setBackgroundResource(R.drawable.function_key_bg);
                break;
            default:
                keyView.setBackgroundResource(R.drawable.character_key_bg);
        }
    }

    @Override
    protected boolean isTextKey(KeyInfo keyInfo) {
        switch (keyInfo.type) {
            case KeyInfo.KEY_TYPE_DELETE:
                return false;
            default:
                return true;
        }
    }
}
