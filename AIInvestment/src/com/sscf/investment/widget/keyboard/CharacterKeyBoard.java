package com.sscf.investment.widget.keyboard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.sdk.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yorkeehuang on 2017/11/23.
 */

public class CharacterKeyBoard extends KeyBoard {

    private static final String TAG = CharacterKeyBoard.class.getSimpleName();

    private int mKeyHeight, mTextKeyWidth, mLineMarginTop;

    private static String[] firstLineCharacters = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"};

    private static String[] secondLineCharacters = {"a", "s", "d", "f", "g", "h", "j", "k", "l"};

    private static String[] thirdLineCharacters = {"z", "x", "c", "v", "b", "n", "m"};

    private List<List<KeyInfo>> mKeyLines = new ArrayList<>(4);

    public CharacterKeyBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int paddintTop = DeviceUtil.dip2px(context, 14);
        int paddintLeft = DeviceUtil.dip2px(context, 3);
        int paddingRight = paddintLeft;
        int paddintBottom = DeviceUtil.dip2px(context, 6);
        setPadding(paddintLeft, paddintTop, paddingRight, paddintBottom);

        mKeyHeight = DeviceUtil.dip2px(context, 42);
        mTextKeyWidth = DeviceUtil.dip2px(context, 30);
        mLineMarginTop = DeviceUtil.dip2px(context, 12);

        initKeys();
    }

    private void initKeys() {
        List<KeyInfo> firstLineKeyInfos = new ArrayList<>(firstLineCharacters.length);
        for(int i=0; i<firstLineCharacters.length; i++) {
            firstLineKeyInfos.add(new CharacterKeyInfo(firstLineCharacters[i]));
        }
        mKeyLines.add(firstLineKeyInfos);

        List<KeyInfo> secondKeyInfos = new ArrayList<>(secondLineCharacters.length);
        for(int i=0; i<secondLineCharacters.length; i++) {
            secondKeyInfos.add(new CharacterKeyInfo(secondLineCharacters[i]));
        }
        mKeyLines.add(secondKeyInfos);


        List<KeyInfo> thirdKeyInfos = new ArrayList<>(thirdLineCharacters.length + 2);
        thirdKeyInfos.add(new TextKeyInfo(KeyInfo.KEY_TYPE_HIDE, "隐藏", 15));
        for(int i=0; i<thirdLineCharacters.length; i++) {
            thirdKeyInfos.add(new CharacterKeyInfo(thirdLineCharacters[i]));
        }
        thirdKeyInfos.add(new ImageKeyInfo(R.drawable.delete_icon, KeyInfo.KEY_TYPE_DELETE));
        mKeyLines.add(thirdKeyInfos);

        List<KeyInfo> fourthKeyInfos = new ArrayList<>(4);
        fourthKeyInfos.add(new TextKeyInfo(KeyInfo.KEY_TYPE_SWITCH_SYSTEM, "系统", 15));
        fourthKeyInfos.add(new TextKeyInfo(KeyInfo.KEY_TYPE_SWITCH_NUMBER, "123", 15));
        fourthKeyInfos.add(new TextKeyInfo(KeyInfo.KEY_TYPE_SPACE, "空格", 15));
        fourthKeyInfos.add(new TextKeyInfo(KeyInfo.KEY_TYPE_CONFIRM, "确定", 15));
        mKeyLines.add(fourthKeyInfos);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        removeAllViews();
        initLineButtons(0, LayoutParams.PARENT_ID, 0, 0);
        initLineButtons(1, mKeyLines.get(0).get(0).id, DeviceUtil.dip2px(getContext(), 18),mLineMarginTop);
        initThirdLines();
        initFourthLines();
    }

    private void initLineButtons(int line, int topId, int marginLeftRight, int marginTop) {
        List<KeyInfo> lineKeys = mKeyLines.get(line);

        for(int i=0, size=lineKeys.size(); i<size; i++) {
            KeyInfo keyInfo = lineKeys.get(i);
            TextView textView = (TextView) createKeyView(keyInfo);
            LayoutParams lp = new LayoutParams(mTextKeyWidth, mKeyHeight);
            lp.horizontalChainStyle = LayoutParams.CHAIN_SPREAD_INSIDE;

            if(topId == LayoutParams.PARENT_ID) {
                lp.topToTop = LayoutParams.PARENT_ID;
            } else {
                lp.topToBottom = topId;
            }

            lp.topMargin = marginTop;

            if(i == 0) {
                lp.leftMargin = marginLeftRight;
                lp.leftToLeft = LayoutParams.PARENT_ID;
            } else {
                lp.leftToRight = lineKeys.get(i - 1).id;
            }

            if(i == size - 1) {
                lp.rightMargin = marginLeftRight;
                lp.rightToRight = LayoutParams.PARENT_ID;
            } else {
                lp.rightToLeft = lineKeys.get(i + 1).id;
            }

            addView(textView, lp);
        }
    }

    private void initThirdLines() {
        List<KeyInfo> secondLineKeys = mKeyLines.get(1);
        List<KeyInfo> thirdLineKeys = mKeyLines.get(2);

        KeyInfo aboveKeyInfo = secondLineKeys.get(0);
        KeyInfo keyInfo = thirdLineKeys.get(0);
        TextView textView = (TextView) createKeyView(keyInfo);
        LayoutParams lp = new LayoutParams(mKeyHeight, mKeyHeight);
        lp.horizontalChainStyle = LayoutParams.CHAIN_SPREAD_INSIDE;

        lp.topToBottom = aboveKeyInfo.id;
        lp.topMargin = mLineMarginTop;
        lp.leftToLeft = LayoutParams.PARENT_ID;
        addView(textView, lp);

        for(int i=1, size=thirdLineKeys.size(); i<size-1; i++) {
            aboveKeyInfo = secondLineKeys.get(i);
            keyInfo = thirdLineKeys.get(i);
            textView = (TextView) createKeyView(keyInfo);
            lp = new LayoutParams(mTextKeyWidth, mKeyHeight);
            lp.horizontalChainStyle = LayoutParams.CHAIN_SPREAD_INSIDE;
            lp.topToBottom = aboveKeyInfo.id;
            lp.topMargin = mLineMarginTop;
            lp.leftToLeft = aboveKeyInfo.id;
            lp.rightToRight = aboveKeyInfo.id;

            addView(textView, lp);
        }

        aboveKeyInfo = secondLineKeys.get(secondLineKeys.size() - 1);
        keyInfo = thirdLineKeys.get(thirdLineKeys.size() - 1);
        ImageView imageView = (ImageView) createKeyView(keyInfo);
        lp = new LayoutParams(mKeyHeight, mKeyHeight);
        lp.horizontalChainStyle = LayoutParams.CHAIN_SPREAD_INSIDE;
        lp.topToBottom = aboveKeyInfo.id;
        lp.topMargin = mLineMarginTop;
        lp.rightToRight = LayoutParams.PARENT_ID;

        addView(imageView, lp);
    }

    private void initFourthLines() {
        List<KeyInfo> thirdLineKeys = mKeyLines.get(2);
        List<KeyInfo> fourthLineKeys = mKeyLines.get(3);

        KeyInfo aboveKeyInfo = thirdLineKeys.get(0);
        KeyInfo keyInfo = fourthLineKeys.get(0);
        TextView textView = (TextView) createKeyView(keyInfo);
        LayoutParams lp = new LayoutParams(mKeyHeight, mKeyHeight);
        lp.leftToLeft = aboveKeyInfo.id;
        lp.rightToRight = aboveKeyInfo.id;
        lp.topMargin = mLineMarginTop;
        lp.topToBottom = aboveKeyInfo.id;
        addView(textView, lp);

        aboveKeyInfo = thirdLineKeys.get(1);
        keyInfo = fourthLineKeys.get(1);
        textView = (TextView) createKeyView(keyInfo);
        lp = new LayoutParams(DeviceUtil.dip2px(getContext(), 36), mKeyHeight);
        lp.rightToRight = aboveKeyInfo.id;
        lp.topMargin = mLineMarginTop;
        lp.topToBottom = aboveKeyInfo.id;
        addView(textView, lp);

        KeyInfo aboveLeftKeyInfo = thirdLineKeys.get(2);
        KeyInfo aboveRightKeyInfo = thirdLineKeys.get(6);
        keyInfo = fourthLineKeys.get(2);
        textView = (TextView) createKeyView(keyInfo);
        lp = new LayoutParams(computeKeyWidth(getContext(), 174f), mKeyHeight);
        lp.leftToLeft = aboveLeftKeyInfo.id;
        lp.rightToRight = aboveRightKeyInfo.id;
        lp.topMargin = mLineMarginTop;
        lp.topToBottom = aboveLeftKeyInfo.id;
        addView(textView, lp);

        aboveLeftKeyInfo = thirdLineKeys.get(7);
        keyInfo = fourthLineKeys.get(3);
        textView = (TextView) createKeyView(keyInfo);
        lp = new LayoutParams(computeKeyWidth(getContext(), 84f), mKeyHeight);
        lp.leftToLeft = aboveLeftKeyInfo.id;
        lp.rightToRight = LayoutParams.PARENT_ID;
        lp.topMargin = mLineMarginTop;
        lp.topToBottom = aboveLeftKeyInfo.id;
        addView(textView, lp);
    }

    private int computeKeyWidth(Context context, float dipValue) {
        float density = context.getResources().getDisplayMetrics().density;
        if(density < 3f && density > 2.5f) {
            return (int) (dipValue * 3f + 0.5f);
        }
        return DeviceUtil.dip2px(getContext(), dipValue);
    }

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
            case KeyInfo.KEY_TYPE_SWITCH_NUMBER:
            case KeyInfo.KEY_TYPE_SWITCH_SYSTEM:
            case KeyInfo.KEY_TYPE_CONFIRM:
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
