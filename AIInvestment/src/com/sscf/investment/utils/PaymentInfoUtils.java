package com.sscf.investment.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.dengtacj.component.entity.payment.OrderInfo;

import BEC.AccuPointUserRiskType;
import BEC.CheckUserCouponRsp;
import BEC.DtPayItem;
import BEC.E_DT_SUBJECT_RISK_LEVEL;
import BEC.UserRiskEvalResult;

/**
 * Created by yorkeehuang on 2017/2/13.
 */
public class PaymentInfoUtils {

    public static void setAmountText(TextView textView, int amount) {
        if (amount < 0) {
            amount = 0;
        }
        textView.setText(String.format("￥%.2f", amount / 100d));
    }

    public static void setPriceText(TextView textView, int amount) {
        if (amount < 0) {
            amount = 0;
        }
        SpannableStringBuilder sb = new SpannableStringBuilder();
        final SpannableString text = new SpannableString("￥");
        text.setSpan(new AbsoluteSizeSpan(12, true), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append(text);
        sb.append(String.format("%.2f", amount / 100d));
        textView.setText(sb);
    }

    public static String getValueText(int value) {
        if(value >= 0) {
            return String.format("￥%.2f", value / 100d);
        } else {
            return String.format("-￥%.2f", -value / 100d);
        }
    }


    public static OrderInfo itemToOrderInfo(final DtPayItem item) {
        return new OrderInfo(item.sDtPayOrderId, item.sDesc, item.iTotalAmount, item.iType, item.iThirdPaySource, item.iH5OpenType);
    }

    public static boolean hasCoupon(CheckUserCouponRsp rsp) {
        return rsp.getICouponNum() > 0;
    }

    public static boolean shouldRiskEval(CheckUserCouponRsp rsp) {
        return shouldRiskEval(rsp.getStRiskResult());
    }

    public static boolean shouldRiskEval(UserRiskEvalResult riskEval) {
        int level = riskEval.getISubjectRiskLevel();
        int userRiskType = riskEval.getIUserRiskType();
        if(level != E_DT_SUBJECT_RISK_LEVEL.E_DT_SUBJECT_RISK_NO
                && (userRiskType == AccuPointUserRiskType.E_USER_RISK_NO_EVAL
                || userRiskType == AccuPointUserRiskType.E_USER_RISK_EVAL_INVALID)) {
            return true;
        } else {
            return false;
        }
    }
}
