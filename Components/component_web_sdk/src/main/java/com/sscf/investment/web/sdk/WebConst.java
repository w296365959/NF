package com.sscf.investment.web.sdk;

import com.sscf.investment.web.CommonWebConst;

/**
 * Created by xuebinliu on 10/16/15.
 */
public final class WebConst extends CommonWebConst {
    // H5 JS调用本地消息
    public static final int MSG_H5_RELOAD = 1;      // 要求重新加载
    public static final int MSG_SET_PAGE_TYPE = 2;  // 设置页面类型
    public static final int MSG_FINISH_PAGE = 3;  //  关闭页面
    public static final int MSG_DOWNLOAD_FILE = 4;  //  下载文件
    public static final int MSG_OPEN_PDF_BY_SYSTEM = 5;  // 通过系统打开PDF文件
    public static final int MSG_SHARE = 6;  // 分享
    public static final int MSG_UPDATE_TITLE = 7;   // 更新标题
    public static final int MSG_SET_WEBVIEW_TITLE_BAR = 8;  // 设置webview的titlebar
    public static final int MSG_SET_REFRESH_BUTTON_ANIM = 9;  // 设置刷新按钮的动画
    public static final int MSG_SET_LONG_CLICK_ENABLED = 10;  // 设置页面是否允许长按操作
    public static final int MSG_SET_INPUT_VALUE = 11;  // 智能投顾界面，设置输入框
    public static final int MSG_SEND_INFO_TO_NATIVE = 12;  // web传递数据到native
    public static final int MSG_SHOW_DIALOG = 13; // 显示dialog
    public static final int MSG_SHOW_DELETE_COMMENT_DIALOG = 14; // 显示删除评论dialog
    public static final int MSG_SHOW_INPUT_DIALOG = 15; //显示输入对话框
    public static final int MSG_SET_COUPON = 16; //传递优惠券
    public static final int MSG_GET_GPS_LOCATION = 17; //传递GPS位置
    public static final int MSG_SET_SIGN_RESULT = 18; //传递购买增值业务签名结果
    public static final int MSG_SET_BACK_BUTTON_LISTENER_ENABLE = 19; // 设置监听
    public static final int MSG_H5_PAY_RESULT = 20; // H5支付结果

    public static final String ACCOUNT_DT_NICKNAME = "dtnickname";
    public static final String ACCOUNT_DT_ICON = "dtheadimgurl";
    public static final String ACCOUNT_DT_DUA = "DT-UA";
    public static final String ACCOUNT_DT_GUID = "DT-GUID";
    public static final String ACCOUNT_DT_IMEI = "IMEI";
    public static final String ACCOUNT_DT_CELLPHONE_STATE = "dtCellphoneState";
    public static final String ACCOUNT_DT_MEMBER_TYPE = "dtMemberType";
    public static final String ACCOUNT_DT_MEMBER_END_TIME = "dtMemberEndTime";
    public static final String ACCOUNT_DT_ID_NUM = "dtUserIDNumber";
    public static final String ACCOUNT_DT_REAL_NAME = "dtUserRealName";
}
