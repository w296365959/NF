package com.sscf.investment.web.sdk.widget;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.web.sdk.R;

/**
 * Created by yorkeehuang on 2017/1/16.
 */
@Route("CommonUnTransparentWebActivity")
public class CommonUnTransparentWebActivity extends CommonWebActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }
}
