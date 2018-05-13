package com.sscf.investment.setting.manager;

import android.content.res.Resources;
import android.text.TextUtils;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.setting.SettingConst;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import com.sscf.investment.utils.StringUtil;

/**
 * davidwei
 * 手机号，昵称，密码，短信验证码等格式验证
 */
public final class CheckTextFormatUtil {

    public static boolean checkCellphoneFormat(final ErrorTipsView errorTipsView, final String cellphone) {
        if (TextUtils.isEmpty(cellphone) || cellphone.length() != SettingConst.CELLPHONE_LENGTH
                || !cellphone.startsWith("1")) {
            errorTipsView.showErrorTips(R.string.error_tips_phone_number_format_error);
            return false;
        }
        return true;
    }

    public static boolean checkNicknameFormat(final ErrorTipsView errorTipsView, final String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            errorTipsView.showErrorTips(R.string.error_tips_nick_connt_empyt);
            return false;
        }

        final Resources resources = DengtaApplication.getApplication().getResources();
        final int NICKNAME_MIN_LENGTH = resources.getInteger(R.integer.nickname_min_length);
        final int NICKNAME_MAX_LENGTH = resources.getInteger(R.integer.nickname_max_length);
        final int nicknameLength = StringUtil.getTextCharactorLength(nickname);
        if (nicknameLength < NICKNAME_MIN_LENGTH
                || nicknameLength > NICKNAME_MAX_LENGTH) {
            errorTipsView.showErrorTips(DengtaApplication.getApplication().getString(R.string.error_tips_nickname_length_error,
                    NICKNAME_MIN_LENGTH, NICKNAME_MAX_LENGTH));
            return false;
        }
        return true;
    }

    public static boolean checkPasswordFormat(final ErrorTipsView errorTipsView, final String password) {
        if (TextUtils.isEmpty(password)) {
            errorTipsView.showErrorTips(R.string.error_tips_password_empty);
            return false;
        }
        final int passwordLength = password.length();
        if (passwordLength < SettingConst.PASSWORD_MIN_LENGTH
                || passwordLength > SettingConst.PASSWORD_MAX_LENGTH) {
            errorTipsView.showErrorTips(DengtaApplication.getApplication().getString(R.string.error_tips_password_length_error,
                    SettingConst.PASSWORD_MIN_LENGTH, SettingConst.PASSWORD_MAX_LENGTH));
            return false;
        }
        return true;
    }

    public static boolean checkRepeatedPasswordFormat(final ErrorTipsView errorTipsView, final String password, final String repeatedPassword) {
        if (TextUtils.isEmpty(repeatedPassword)) {
            errorTipsView.showErrorTips(R.string.error_tips_conform_password_empty);
            return false;
        }
        if (!password.equals(repeatedPassword)) {
            errorTipsView.showErrorTips(R.string.error_tips_password_repead_error);
            return false;
        }
        return true;
    }

    public static boolean checkSmsCodeFormat(final ErrorTipsView errorTipsView, final String smsCode) {
        if (TextUtils.isEmpty(smsCode) || smsCode.length() != SettingConst.SMS_CODE_LENGTH) {
            errorTipsView.showErrorTips(R.string.error_tips_sms_code_error);
            return false;
        }
        return true;
    }
}