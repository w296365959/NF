package com.sscf.investment.payment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sscf.investment.R;

import java.util.List;

import BEC.RiskMatchResult;

/**
 * Created by yorkeehuang on 2017/9/21.
 */

public class RiskEvalInfoDialog extends Dialog {

    public RiskEvalInfoDialog(Context context, List<RiskMatchResult> matchResults) {
        super(context, com.sscf.investment.component.ui.R.style.dialog_center_theme);
        setContentView(R.layout.dialog_risk_eval_info);
        final LinearLayout contentView = (LinearLayout) findViewById(R.id.content);

        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final RectF frame = new RectF(
                            contentView.getX(),
                            contentView.getY(),
                            contentView.getX() + contentView.getWidth(),
                            contentView.getY() + contentView.getHeight());
                    if (!frame.contains(event.getX(), event.getY())) {
                        cancel();
                    }
                }
                return false;
            }
        });
        initView(getContext(), contentView, matchResults);
    }

    private void initView(Context context, LinearLayout contentView, List<RiskMatchResult> matchResults) {
        String[] info1Array = context.getResources().getStringArray(R.array.risk_eval_info1_array);
        String[] info2Array = context.getResources().getStringArray(R.array.risk_eval_info2_array);

        for(int i = 0, size = Math.min(matchResults.size(), info1Array.length); i<size; i++) {
            RiskMatchResult matchResult = matchResults.get(i);
            View itemView = View.inflate(getContext(), R.layout.layout_risk_eval_item, null);

            TextView titleView = (TextView) itemView.findViewById(R.id.risk_eval_title);
            titleView.setText(matchResult.getSMatchName() + "：");

            TextView resultView = (TextView) itemView.findViewById(R.id.risk_eval_result);
            String text;
            if(matchResult.getIMatchResult() > 0) {
                resultView.setTextColor(ContextCompat.getColor(context, R.color.risk_eval_nomatch_color));
                text = context.getString(R.string.risk_eval_nomatch);
            } else {
                resultView.setTextColor(ContextCompat.getColor(context, R.color.risk_eval_match_color));
                text = context.getString(R.string.risk_eval_match);
            }
            resultView.setText(text);

            TextView info1View = (TextView) itemView.findViewById(R.id.info1);
            info1View.setText(info1Array[i] + matchResult.getSUserAnswers());

            TextView info2View = (TextView) itemView.findViewById(R.id.info2);
            info2View.setText(info2Array[i] + matchResult.getSProductEval());
            contentView.addView(itemView);
        }
    }
}
