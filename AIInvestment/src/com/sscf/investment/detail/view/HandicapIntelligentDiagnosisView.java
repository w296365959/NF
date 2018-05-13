package com.sscf.investment.detail.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import com.sscf.investment.R;
import com.sscf.investment.widget.RadarView;
import BEC.ConsultScore;

/**
 * Created by davidwei on 2017/04/26.
 */
public final class HandicapIntelligentDiagnosisView extends ConstraintLayout {
    private RadarView mRadarView;
    private TextView mScoreView;
    private TextView mScoreTitleView;
    private TextView mScoreDescription;

    public HandicapIntelligentDiagnosisView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRadarView = (RadarView) findViewById(R.id.radar_view);
        mScoreView = (TextView) findViewById(R.id.score);
        mScoreTitleView = (TextView) findViewById(R.id.score_title);
        mScoreDescription = (TextView) findViewById(R.id.score_description);
    }

    public void setData(final ConsultScore score) {
        mRadarView.setData(score);
        final int val = (int) score.fVal;
        if (val > 0) {
            mScoreView.setText(String.valueOf(val));
            mScoreTitleView.setText(R.string.score_title_text);
            mScoreDescription.setText(score.sScoreDesc);
        } else {
            mScoreView.setText("");
            mScoreTitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            mScoreTitleView.setText(R.string.no_score);
            mScoreDescription.setText("");
        }
    }
}
