package com.sscf.investment.detail.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.detail.SecurityDetailActivity;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.utils.StockUtil;
import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.widget.RectBackgroundDrawable;
import java.util.List;

/**
 * Created by liqf on 2015/7/17.
 */
public class DetailActionBar extends RelativeLayout implements View.OnClickListener {
    public static final int MAX_LETTER_COUNT = 20;
    private static final String TAG = DetailActionBar.class.getSimpleName();
    public static final int ANIMATION_DURATION = 200;
    private ImageView mBackButton;
    private ImageView mSearchButton;
    private FrameLayout mIntelligentAnswerLayout;
    private View mContentLayout;
    private TextView mTitle;
    private TextView mSubTitle;
    private View mSubTitleLayout;
    private TextView mSubTitleAlternative;
    private OnOperationListener mOperationListener;
    private LinearLayout mAttrTags;
    private int mSubTitleColor;
    private int mSubTitleMargin;
    private int mSubTitlePadding;
    private int mAttrTextSize;
    private int mAttrHeight;

    private boolean mInited = false;
    private int mDrawingWidth;

    /**
     * 只有主标题而无副标题时的文字大小
     */
    private int mBigTitleTextSize;

    private String mDtSecCode;
    private String mSecName;

    public void setHasSubTitle(boolean hasSubTitle) {
        mHasSubTitle = hasSubTitle;

        if (!mHasSubTitle) {
            FrameLayout contentLayout = (FrameLayout) findViewById(R.id.content_layout);
            contentLayout.removeView(mTitle);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBigTitleTextSize);
            this.addView(mTitle, lp);
        }
    }

    private boolean mHasSubTitle;

    public DetailActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.detail_action_bar, this, true);

        initResources();

        setBackgroundColor(context);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!mInited) {
                    mDrawingWidth = getMeasuredWidth();
                    if (mDrawingWidth != 0) {
                        mInited = true;
                        getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                }
                return true;
            }
        });
    }

    private void setBackgroundColor(Context context) {
        int bgColor = ContextCompat.getColor(context, R.color.actionbar_bg);
        setBackgroundColor(bgColor);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mBackButton = (ImageView) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(this);

        mSearchButton = (ImageView) findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(this);

        mIntelligentAnswerLayout = (FrameLayout) findViewById(R.id.intelligent_answer_layout);
        mIntelligentAnswerLayout.setOnClickListener(this);

        mContentLayout = findViewById(R.id.content_layout);

        mTitle = (TextView) findViewById(R.id.stock_title);
        mSubTitle = (TextView) findViewById(R.id.sub_title);
        mSubTitleLayout = findViewById(R.id.sub_title_layout);
        mSubTitleAlternative = (TextView) findViewById(R.id.sub_title_alternative);

        mAttrTags = (LinearLayout) findViewById(R.id.attr_tags);
    }

    private void initResources() {
        Resources resources = getResources();
        mSubTitleMargin = resources.getDimensionPixelSize(R.dimen.sub_title_margin);
        mSubTitlePadding = resources.getDimensionPixelSize(R.dimen.sub_title_padding);
        mAttrTextSize = resources.getDimensionPixelSize(R.dimen.font_size_10);
        mAttrHeight = resources.getDimensionPixelSize(R.dimen.stock_subject_title_height);
        mBigTitleTextSize = resources.getDimensionPixelSize(R.dimen.font_size_22);

        TypedArray a = getContext().obtainStyledAttributes(new int[] {
            R.attr.actionbar_subtitle_text_color
        });
        mSubTitleColor = a.getColor(0, Color.GRAY);
        a.recycle();
    }

    @Override
    public void onClick(View v) {
        if (mOperationListener == null) {
            return;
        }

        if (v == mBackButton) {
            mOperationListener.onBack();
        } else if (v == mSearchButton) {
            final Context context = getContext();
            if (context instanceof SecurityDetailActivity) {
                ((SecurityDetailActivity) context).mClickSearch = true;
            }
            CommonBeaconJump.showSearch(context);
            StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_SEARCH_CLICKED);
        } else if (v == mIntelligentAnswerLayout) {
            if (isChineseStock()) {
                WebBeaconJump.showIntelligentAnswer(getContext(), mDtSecCode, mSecName);
            } else {
                WebBeaconJump.showIntelligentAnswer(getContext(), false);
            }
            StatisticsUtil.reportAction(StatisticsConst.SEC_DETAIL_INTELLIGENT_ANSWER_CLICKED);
        }
    }

    private boolean isChineseStock() {
        return StockUtil.isChineseMarket(mDtSecCode) && StockUtil.isStock(mDtSecCode);
    }

    /**
     * 中间的内容区随着左右翻页显示动画效果
     * @param offset [-1, 1]
     */
    public void setContentScrollX(float offset) {
        //左边的offset参数从0到1，右边的offset参数从-1到0
        //左边的位移从0到1/4，右边的位移从-1/4到0
        //左边:offset从0到0.5的过程中alpha从1到0，从0.5到1的过程中alpha为0；右边:offset从0到0.5的过程中alpha为0，offset从0.5到1的过程中alpha从0到1
        mContentLayout.setTranslationX(offset * mDrawingWidth / 4);

        float alpha;
        if (offset >= 0) { //左边
            if (offset <= 0.5f) {
                alpha = -2 * offset + 1;
            } else {
                alpha = 0;
            }
        } else { //右边
            float absOffset = Math.abs(offset);
            if (absOffset <= 0.5f) {
                alpha = -2 * absOffset + 1;
            } else {
                alpha = 0;
            }
        }
        DtLog.d(TAG, "setContentScrollX: offset = " + offset + ", alpha = " + alpha);
        mContentLayout.setAlpha(alpha);

        if (offset == 0) {
            mBackButton.setVisibility(VISIBLE);
            mSearchButton.setVisibility(VISIBLE);
            mIntelligentAnswerLayout.setVisibility(VISIBLE);
        } else {
            mBackButton.setVisibility(INVISIBLE);
            mSearchButton.setVisibility(INVISIBLE);
            mIntelligentAnswerLayout.setVisibility(INVISIBLE);
        }
    }

    public void setTitleText(final String text) {
        if (!mHasSubTitle) {
            mTitle.setText(text);
            return;
        }

        String trimmedText = text.toString().trim();
//        String trimmedText = "zhongguo安徽黄山海螺水泥工厂(123456)".toString().trim();
//        String trimmedText = "中国安徽黄山海螺水泥工厂(123456)".toString().trim();
//        String trimmedText = "中国安徽黄山海螺(123456)".toString().trim();
//        String trimmedText = "zhongguoanhuihuangshanghailuoshuini(123456)".toString().trim();

        int count = getCount(trimmedText);

        int splitPosition = 0;
        if (count > MAX_LETTER_COUNT) {
            for (int i = 0; i < trimmedText.length(); i++) {
                if (trimmedText.charAt(i) == '(') {
                    splitPosition = i;
                    break;
                }
            }
            String prevStr = "";
            int prevLen = 0;
            String postStr = trimmedText.substring(splitPosition, trimmedText.length());
            int postLen = getCount(postStr);

            prevLen = MAX_LETTER_COUNT - postLen;
            prevStr = getPrevString(trimmedText, prevLen);

            String finalStr = prevStr + postStr;
            mTitle.setText(finalStr);
        } else {
            mTitle.setText(trimmedText);
        }
    }

    public void setSubTitleAlternativeText(final String text) {
        mSubTitleAlternative.setText(text);
    }

    public void setDtSecCode(final String dtSecCode, final String secName) {
        mDtSecCode = dtSecCode;
        mSecName = secName;
    }

    private boolean mIsAnimating = false;
    private boolean mNeedShowSubTitleAlternative = false;
    public void showSubTitleAlternative(final boolean show) {
        if (show == mNeedShowSubTitleAlternative) {
            return;
        }

        mNeedShowSubTitleAlternative = show;
        if (mIsAnimating) {
            return;
        }

        playSubTitleSwitchAnimation();
    }

    private void playSubTitleSwitchAnimation() {
        final boolean prevShow = mNeedShowSubTitleAlternative;
        if (!mNeedShowSubTitleAlternative) {
            if (mSubTitleAlternative.getVisibility() == VISIBLE) {
                //播放向上翻的动画
                mSubTitleAlternative.setTranslationY(0);
                mIsAnimating = true;
                mSubTitleAlternative.animate().setDuration(ANIMATION_DURATION).translationY(mSubTitleLayout.getMeasuredHeight()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSubTitleAlternative.setVisibility(GONE);
                        mSubTitleLayout.setTranslationY(-mSubTitleLayout.getMeasuredHeight());
                        mSubTitleLayout.setVisibility(VISIBLE);
                        mSubTitleLayout.animate().translationY(0).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mIsAnimating = false;

                                if (mNeedShowSubTitleAlternative != prevShow) {
                                    playSubTitleSwitchAnimation();
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
        } else {
            if (mSubTitleAlternative.getVisibility() != VISIBLE) {
                //播放向下翻的动画
                mSubTitleLayout.setTranslationY(0);
                mIsAnimating = true;
                mSubTitleLayout.animate().setDuration(200).translationY(-mSubTitleLayout.getMeasuredHeight()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSubTitleLayout.setVisibility(GONE);
                        mSubTitleAlternative.setTranslationY(mSubTitleLayout.getMeasuredHeight());
                        mSubTitleAlternative.setVisibility(VISIBLE);
                        mSubTitleAlternative.animate().translationY(0).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mIsAnimating = false;

                                if (mNeedShowSubTitleAlternative != prevShow) {
                                    playSubTitleSwitchAnimation();
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
        }
    }

    private String getPrevString(String trimmedText, int maxPrevLen) {
        String prevStr;

        int count = 0;
        int splitPosition = 0;
        for (int i = 0; i < trimmedText.length(); i++) {
            char c = trimmedText.charAt(i);
            if (isAlphabet(c)) {
                count++;
            } else {
                count += 2;
            }
            if (count > maxPrevLen) {
                splitPosition = i;
                break;
            }
        }

        prevStr = trimmedText.substring(0, splitPosition) + "...";
//        prevStr = prevStr.substring(0, prevStr.length() - 1) + "...";

        return prevStr;
    }

    private int getCount(String trimmedText) {
        int count = 0;
        for (int i = 0; i < trimmedText.length(); i++) {
            char c = trimmedText.charAt(i);
            if (isAlphabet(c)) {
                count++;
            } else {
                count += 2;
            }
        }
        return count;
    }

    private boolean isAlphabet(char c) {
        if ((c <= 'Z' && c >= 'A') || (c <= 'z' && c >= 'a')
            || c == ' '
            || (c >= '0' && c <= '9')
            || (c == '(' || c == ')')) {
            return true;
        }
        return false;
    }

    public void setSubTitleText(final String text) {
        mSubTitle.setText(text);
    }

    public void setAttrTags(final List<String> attrTags) {
        mAttrTags.removeAllViews();

        if (attrTags == null || attrTags.size() == 0) {
            return;
        }

        int size = attrTags.size();
        for (int i = 0; i < size; i++) {
            String attrTag = attrTags.get(i);
            TextView textView = new TextView(getContext());
            textView.setTextColor(mSubTitleColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrTextSize);
            textView.setText(attrTag);
            textView.setPadding(mSubTitlePadding, 0, mSubTitlePadding, 0);
            RectBackgroundDrawable bgDrawable = new RectBackgroundDrawable(getContext());
            bgDrawable.setColor(mSubTitleColor);
            textView.setBackgroundDrawable(bgDrawable);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mAttrHeight);
            if (i != size - 1) {
                lp.setMargins(0, 0, mSubTitleMargin, 0);
            }
            mAttrTags.addView(textView, lp);
        }

        mAttrTags.setVisibility(VISIBLE);
    }

    public void setOnOperationListener(final OnOperationListener listener) {
        mOperationListener = listener;
    }

    public interface OnOperationListener {
        void onBack();
    }

}
