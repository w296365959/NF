package com.sscf.investment.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengtacj.component.router.CommonBeaconJump;
import com.dengtacj.thoth.MapProtoLite;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.utils.ImageLoaderUtils;
import com.sscf.investment.component.ui.widget.PicPickerDialog;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.NetworkConst;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.NetUtil;
import com.sscf.investment.sdk.utils.PlistUtils;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.setting.entity.AccountInfoExt;
import com.sscf.investment.setting.manager.AccountRequestManager;
import com.sscf.investment.setting.widgt.BaseLogoutActivity;
import com.sscf.investment.setting.widgt.ErrorTipsView;
import com.sscf.investment.setting.widgt.PlacePicker;
import com.sscf.investment.setting.widgt.SexPicker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import BEC.E_ACCOUNT_RET;
import BEC.E_FEED_USER_TYPE;
import BEC.E_GENDER_TYPE;
import BEC.FeedUserBaseInfo;
import BEC.ModifyAccountInfoRsp;

/**
 * davidwei
 * 用户信息界面
 */
public final class UserInfoActivity extends BaseLogoutActivity implements View.OnClickListener, DataSourceProxy.IRequestCallback, PicPickerDialog.PickerCallback,
        SexPicker.OnGetSexListener, PlacePicker.OnGetPlaceListener {
    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private static final String EXTRA_FEED_INFO = "feed_info";

    private TextView mNicknameView;
    private ImageView mAvatarView;
    private View mIconV;
    private ErrorTipsView mErrorTipsView;
    private BroadcastReceiver mAccountReceiver;

    private PicPickerDialog mPickerDialog;
    private TextView mSexView;
    private TextView mPlaceView;
    private TextView mProfileView;

    private FeedUserBaseInfo mFeedInfo;
    private boolean mSelf;

    ArrayList<String> mOptions1Items;
    ArrayList<ArrayList<String>> mOptions2Items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FeedUserBaseInfo feedInfo = (FeedUserBaseInfo) getIntent().getSerializableExtra(EXTRA_FEED_INFO);

        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        final AccountInfoEntity accountInfo = dengtaApplication.getAccountManager().getAccountInfo();

        boolean isSelf = false;
        if (accountInfo == null) {
            if (feedInfo == null) {
                finish();
                return;
            } else {
                isSelf = false;
                mFeedInfo = feedInfo;
            }
        } else {
            if (feedInfo == null) {
                isSelf = true;
            } else if (feedInfo.iAccountId == accountInfo.id) {
                isSelf = true;
            } else {
                isSelf = false;
                mFeedInfo = feedInfo;
            }
        }

        mSelf = isSelf;
        if (isSelf) {
            dengtaApplication.defaultExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<Map<String, Object>> placeList = PlistUtils.parsePlist(dengtaApplication, "area.plist");
                    final ArrayList<String> options1Items = new ArrayList<String>();
                    final ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

                    for (Map<String, Object> province : placeList) {
                        options1Items.add(province.get("province").toString());
                        options2Items.add((ArrayList<String>) province.get("cities"));
                    }
                    mOptions1Items = options1Items;
                    mOptions2Items = options2Items;
                }
            });
            // 需要更新用户信息
            dengtaApplication.getAccountManager().updateAccountInfoFromWeb();
        }

        setContentView(R.layout.activity_setting_user_info);

        initViews();
        registerAccountBroadcast();
        StatisticsUtil.reportAction(StatisticsConst.SETTING_USER_INFO_DISPLAY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.profile);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        findViewById(R.id.modifyAvatar).setOnClickListener(this);
        findViewById(R.id.modifyNickname).setOnClickListener(this);
        findViewById(R.id.modifySex).setOnClickListener(this);
        findViewById(R.id.modifyPlace).setOnClickListener(this);
        findViewById(R.id.modifyIntro).setOnClickListener(this);

        mNicknameView = (TextView) findViewById(R.id.nickname);
        mSexView = (TextView) findViewById(R.id.sex);
        mPlaceView = (TextView) findViewById(R.id.place);
        mProfileView = (TextView) findViewById(R.id.profile);
        mAvatarView = (ImageView) findViewById(R.id.accountAvatar);
        mIconV = findViewById(R.id.iconV);
        final View verificationView = findViewById(R.id.verificationLayout);
        verificationView.setOnClickListener(this);
        final TextView verificationTextView = (TextView) verificationView.findViewById(R.id.verification);

        String url = null;
        String verification = null;

        boolean isV = false;
        if (mSelf) {
            final AccountInfoExt accountInfoExt = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
            final AccountInfoEntity accountInfoEntity = accountInfoExt.accountInfo;

            mNicknameView.setText(accountInfoEntity.nickname);
            mSexView.setText(getGenderString(accountInfoExt.gender));
            mPlaceView.setText(accountInfoExt.province + accountInfoExt.city);
            url = accountInfoEntity.iconUrl;
            verification = accountInfoExt.verification;
            isV = accountInfoExt.isV();
        } else {
            mNicknameView.setText(mFeedInfo.sNickName);
            mSexView.setText(getGenderString(mFeedInfo.iGender));
            mPlaceView.setText(mFeedInfo.sProvince + mFeedInfo.sCity);
            url = mFeedInfo.sFaceUrl;
            verification = mFeedInfo.sVerifyDesc;
            isV = mFeedInfo.eUserType == E_FEED_USER_TYPE.E_FEED_USER_INVEST_V;
        }

        if (isV) {
            if (TextUtils.isEmpty(verification)) {
                verificationTextView.setText(R.string.verification_blank);
            } else {
                verificationTextView.setText(verification);
            }
            verificationView.setVisibility(View.VISIBLE);
            mIconV.setVisibility(View.VISIBLE);
        } else {
            verificationView.setVisibility(View.GONE);
            mIconV.setVisibility(View.GONE);
        }

        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();

        if (!TextUtils.isEmpty(url)) {
            ImageLoaderUtils.getImageLoader().displayImage(url, mAvatarView, new SimpleImageLoadingListener() {
                private static final int MAX_RETRY = 5;

                private int mRetry;

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    DtLog.d(TAG, "ImageLoader.onLoadingFailed : " + failReason.getCause());
                    if (mRetry <= MAX_RETRY) {
                        mRetry++;
                        ImageLoaderUtils.getImageLoader().displayImage(imageUri, mAvatarView, this);
                    }
                }
            });
        }

        mErrorTipsView = (ErrorTipsView) findViewById(R.id.errorTips);

        updateAccountInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPickerDialog != null) {
            mPickerDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfile();
    }

    private void updateProfile() {
        if (mSelf) {
            AccountInfoExt accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
            if (TextUtils.isEmpty(accountInfo.profile)) {
                mProfileView.setText(R.string.profile_blank);
            } else {
                mProfileView.setText(accountInfo.profile);
            }
        } else {
            if (TextUtils.isEmpty(mFeedInfo.sProfile)) {
                mProfileView.setText(R.string.profile_empty);
            } else {
                mProfileView.setText(mFeedInfo.sProfile);
            }
        }
    }

    @Override
    public void onGetPicSuccess(final Uri uri) {
        if (!NetUtil.isNetWorkConnected(this)) {
            mErrorTipsView.showErrorTips(R.string.error_tips_network_error);
            return;
        }

        DengtaApplication.getApplication().defaultExecutor.execute(new Runnable() {
            @Override
            public void run() {
                InputStream in = null;
                try {
                    in = getContentResolver().openInputStream(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO 提示失败
                }
                if (in != null) {
                    final byte[] avatarData = FileUtil.getByteArrayFromInputStream(in);
                    if (avatarData != null) {
                        if (isDestroy() || isFinishing()) {
                            return;
                        }
                        showLoadingDialog();
                        final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
                        AccountRequestManager.modifyAvatar(accountInfo.ticket, accountInfo.id,
                                avatarData, PicPickerDialog.IMAGE_TYPE, UserInfoActivity.this);
                    } else {
                        // TODO 提示失败
                    }
                }
            }
        });
    }

    @Override
    public void onGetPicCancel() {
    }

    @Override
    public void onGetPicError() {
        mErrorTipsView.showErrorTips(getString(R.string.error_tips_get_pic_failed));
    }

    private void updateAccountInfo() {
        if (mSelf) {
            final AccountInfoEntity accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfo();
            mNicknameView.setText(accountInfo.nickname);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLoginBroadcast();
    }

    private void registerAccountBroadcast() {
        if (mAccountReceiver == null) {
            mAccountReceiver = new AccountBroadcastReceiver();
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mAccountReceiver,
                    new IntentFilter(SettingConst.ACTION_UPDATE_ACCOUNT_INFO));
        }
    }

    private void unregisterLoginBroadcast() {
        if (mAccountReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mAccountReceiver);
        }
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (v.getId()) {
            case R.id.modifyAvatar:
                if (mSelf) {
                    showModifyAvatarDialog();
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_USER_INFO_CLICK_USER_ICON);
                }
                break;
            case R.id.modifyNickname:
                if (mSelf) {
                    startActivity(new Intent(this, ModifyNicknameActivity.class));
                }
                break;
            case R.id.modifySex:
                if (mSelf) {
                    final SexPicker sexPicker = new SexPicker(this);
                    sexPicker.setOnGetSexListener(this);
                    sexPicker.setSex(getGenderString(DengtaApplication.getApplication().getAccountManager().getAccountInfoExt().gender));
                    sexPicker.show();
                    StatisticsUtil.reportAction(StatisticsConst.A_ME_HOMEPAGE_PERSONAL_NICHENG_CLICK);
                }
                break;
            case R.id.modifyPlace:
                if (mSelf) {
                    if (mOptions2Items != null) {
                        final PlacePicker placePicker = new PlacePicker(this);
                        placePicker.setOnGetPlaceListener(this);
                        placePicker.initPlace(mOptions1Items, mOptions2Items);
                        final AccountInfoExt accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
                        placePicker.setPlace(accountInfo.province, accountInfo.city);
                        placePicker.show();
                        StatisticsUtil.reportAction(StatisticsConst.A_ME_HOMEPAGE_PERSONAL_AREA_CLICK);
                    }
                }
                break;
            case R.id.modifyIntro:
                if (mSelf) {
                    CommonBeaconJump.showActivity(this, ProfileEditActivity.class);
                }
                break;
            case R.id.verificationLayout:
                if (mSelf) {
                    DengtaApplication.getApplication().showToast(R.string.certification_toast);
                }
                break;
            case R.id.actionbar_back_button:
                finish();
                break;
            default:
                break;
        }
    }

    private void showModifyAvatarDialog() {
        if (isDestroy()) {
            return;
        }
        if (mPickerDialog == null) {
            mPickerDialog = new PicPickerDialog(this, this, PicPickerDialog.PICKER_MODE_ALBUM);
        }
        mPickerDialog.show();
    }

    private void modifyAvatarCallback(final boolean success, final EntityObject data) {
        final Object entity = data.getEntity();
        if (success && data != null) {
            final MapProtoLite packet = (MapProtoLite) entity;
            final int code = packet.read("", -1);
            switch (code) {
                case 0: // 上传头像成功
                    final ModifyAccountInfoRsp rsp = packet.read(NetworkConst.RSP, new ModifyAccountInfoRsp());
                    final AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
                    final AccountInfoExt accountInfoExt = accountManager.getAccountInfoExt();
                    accountInfoExt.accountInfo.iconUrl = rsp.stAccountInfo.sFaceUrl;
                    accountManager.updateLocalAccountInfo(accountInfoExt);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageLoaderUtils.getImageLoader().displayImage(accountInfoExt.accountInfo.iconUrl, mAvatarView);
                            DengtaApplication.getApplication().showToast(R.string.upload_head_image_success);
                        }
                    });
                    StatisticsUtil.reportAction(StatisticsConst.SETTING_USER_INFO_MODIFY_AVATAR_SUCCESS);
                    break;
                // ticket验证不过，删除用户信息，重新登录
                case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
                case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
                case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
                    DengtaApplication.getApplication().getAccountManager().removeAccountInfo();

                    startActivity(new Intent(this, LoginActivity.class));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DengtaApplication.getApplication().showToast(R.string.ticket_expired);
                            finish();
                        }
                    });
                    break;
                case E_ACCOUNT_RET.E_AR_MODIFY_FACE_ERROR:
                    // TODO
                default:
                    mErrorTipsView.showErrorTips(getString(R.string.error_tips_upload_avatar_failed));
                    break;
            }
        } else {
            mErrorTipsView.showErrorTips(getString(R.string.error_tips_upload_avatar_request_failed));
        }
        dismissLoadingDialog();
    }

    @Override
    public void callback(final boolean success, final EntityObject data) {
        switch (data.getEntityType()) {
            case EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO:
                modifyAvatarCallback(success, data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetSex(String sex) {
        final int gender = getGenderInt(sex);
        final AccountInfoExt accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
        if (accountInfo.gender != gender) {
            if (NetUtil.isNetWorkConnected(this)) {
                showLoadingDialog();
                AccountRequestManager.modifyGender(gender, accountInfo, new ModifyGenderCallback(gender));
                mSexView.setText(sex);
            } else {
                mErrorTipsView.showErrorTips(R.string.error_tips_network_error);
            }
        }
    }

    public int getGenderInt(final String gender) {
        final Resources res = getResources();
        int genderInt = E_GENDER_TYPE.E_GENDER_UNKOWN;
        if (res.getString(R.string.male).equals(gender)) {
            genderInt = E_GENDER_TYPE.E_GENDER_MALE;
        } else if (res.getString(R.string.female).equals(gender)) {
            genderInt = E_GENDER_TYPE.E_GENDER_FEMAIL;
        }
        return genderInt;
    }

    public String getGenderString(final int gender) {
        final Resources res = getResources();
        String genderString = null;
        switch (gender) {
            case E_GENDER_TYPE.E_GENDER_MALE:
                genderString = res.getString(R.string.male);
                break;
            case E_GENDER_TYPE.E_GENDER_FEMAIL:
                genderString = res.getString(R.string.female);
                break;
            case E_GENDER_TYPE.E_GENDER_UNKOWN:
            default:
                genderString = res.getString(R.string.secrecy);
                break;
        }
        return genderString;
    }

    private final class ModifyGenderCallback implements DataSourceProxy.IRequestCallback {
        private final int newGender;

        private ModifyGenderCallback(int gender) {
            newGender = gender;
        }

        @Override
        public void callback(boolean success, EntityObject data) {
            if (success) {
                if (data.getEntityType() != EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO) {
                    return;
                }
                final Object entity = data.getEntity();
                if (entity == null || !(entity instanceof MapProtoLite)) {
                    return;
                }

                final MapProtoLite packet = (MapProtoLite) entity;
                final int code = packet.read("", -1);
                switch (code) {
                    case 0: // 修改成功
                        final AccountInfoExt accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
                        accountInfo.gender = newGender;
                        DengtaApplication.getApplication().getAccountManager().updateLocalAccountInfo(accountInfo);
                        break;
                    // ticket验证不过，删除用户信息，重新登录
                    case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
                    case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
                    case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
                        DengtaApplication.getApplication().getAccountManager().removeAccountInfo();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.ticket_expired);
                                finish();
                            }
                        });
                        break;
                    default:
                        mErrorTipsView.showErrorTips(getString(R.string.error_tips_error_code, String.valueOf(code)));
                        break;
                }
            } else {
                mErrorTipsView.showErrorTips(R.string.error_tips_request_failed);
            }
            dismissLoadingDialog();
        }
    }

    @Override
    public void onGetPlace(String provice, String city) {
        final AccountInfoExt accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
        if (TextUtils.equals(provice, accountInfo.province) && TextUtils.equals(city, accountInfo.city)) {
            return;
        }

        if (NetUtil.isNetWorkConnected(this)) {
            showLoadingDialog();
            AccountRequestManager.modifyPlace(provice, city, accountInfo, new ModifyPlaceCallback(provice, city));
            mPlaceView.setText(provice + city);
        } else {
            mErrorTipsView.showErrorTips(R.string.error_tips_network_error);
        }
    }

    private final class ModifyPlaceCallback implements DataSourceProxy.IRequestCallback {
        private final String provice;
        private final String city;

        public ModifyPlaceCallback(String provice, String city) {
            this.provice = provice;
            this.city = city;
        }

        @Override
        public void callback(boolean success, EntityObject data) {
            if (success) {
                if (data.getEntityType() != EntityObject.ET_ACCOUNT_MODIFY_ACCOUNT_INFO) {
                    return;
                }
                final Object entity = data.getEntity();
                if (entity == null || !(entity instanceof MapProtoLite)) {
                    return;
                }

                final MapProtoLite packet = (MapProtoLite) entity;
                final int code = packet.read("", -1);
                switch (code) {
                    case 0: // 修改成功
                        final AccountInfoExt accountInfo = DengtaApplication.getApplication().getAccountManager().getAccountInfoExt();
                        accountInfo.province = provice;
                        accountInfo.city = city;
                        DengtaApplication.getApplication().getAccountManager().updateLocalAccountInfo(accountInfo);
                        break;
                    // ticket验证不过，删除用户信息，重新登录
                    case E_ACCOUNT_RET.E_AR_TICKET_LENGTH_ERROR:
                    case E_ACCOUNT_RET.E_AR_TICKET_EXPIRED:
                    case E_ACCOUNT_RET.E_AR_TICKET_SIGN_ERROR:
                        DengtaApplication.getApplication().getAccountManager().removeAccountInfo();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DengtaApplication.getApplication().showToast(R.string.ticket_expired);
                                finish();
                            }
                        });
                        break;
                    default:
                        mErrorTipsView.showErrorTips(getString(R.string.error_tips_error_code, String.valueOf(code)));
                        break;
                }
            } else {
                mErrorTipsView.showErrorTips(R.string.error_tips_request_failed);
            }
            dismissLoadingDialog();
        }
    }

    private final class AccountBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (SettingConst.ACTION_UPDATE_ACCOUNT_INFO.equals(action)) {
                updateAccountInfo();
            }
        }
    }

    public static void show(final Context context, final FeedUserBaseInfo feedInfo) {
        final Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(EXTRA_FEED_INFO, feedInfo);
        context.startActivity(intent);
    }
}
