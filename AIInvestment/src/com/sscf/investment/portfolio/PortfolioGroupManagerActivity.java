package com.sscf.investment.portfolio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.content.ContextCompat;
import com.sscf.investment.R;
import com.sscf.investment.component.ocr.entity.Stock;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;

import java.util.ArrayList;

/**
 * Created by liqf on 2016/6/22.
 */
public class PortfolioGroupManagerActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceUtil.enableTranslucentStatus(this, ContextCompat.getColor(getApplicationContext(), R.color.actionbar_bg));

        Intent intent = getIntent();
        int operationMode = GroupManagerFragment.MODE_SWITCH_GROUP;
        String secCode = null;
        String secName = null;
        if (intent != null) {
            operationMode = intent.getIntExtra(GroupManagerFragment.KEY_OPERATION_MODE, GroupManagerFragment.MODE_SWITCH_GROUP);
            secCode = intent.getStringExtra(DengtaConst.KEY_SEC_CODE);
            secName = intent.getStringExtra(DengtaConst.KEY_SEC_NAME);
        }

        GroupManagerFragment groupManagerFragment = new GroupManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GroupManagerFragment.KEY_OPERATION_MODE, operationMode);
        if(operationMode == GroupManagerFragment.MODE_IMPORT_SELECT_GROUP) {
            ArrayList<Stock> stockList = intent.getParcelableArrayListExtra(DengtaConst.KEY_STOCK_LIST);
            bundle.putParcelableArrayList(DengtaConst.KEY_STOCK_LIST, stockList);
        } else {
            bundle.putString(DengtaConst.KEY_SEC_CODE, secCode);
            bundle.putString(DengtaConst.KEY_SEC_NAME, secName);
        }
        groupManagerFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, groupManagerFragment).commit();
    }



    /**
     * 选择分组加入的模式
     * @param context
     * @param secCode
     * @param secName
     */
    public static void show(final Context context, final String secCode, final String secName) {
        Intent intent = new Intent(context, PortfolioGroupManagerActivity.class);
        intent.putExtra(GroupManagerFragment.KEY_OPERATION_MODE, GroupManagerFragment.MODE_SELECT_GROUP);
        intent.putExtra(DengtaConst.KEY_SEC_CODE, secCode);
        intent.putExtra(DengtaConst.KEY_SEC_NAME, secName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void show(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, PortfolioGroupManagerActivity.class);
        intent.putExtra(GroupManagerFragment.KEY_OPERATION_MODE, GroupManagerFragment.MODE_IMPORT_SELECT_GROUP);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 切换分组的模式
     */
    public static void show(final Context context) {
        Intent intent = new Intent(context, PortfolioGroupManagerActivity.class);
        intent.putExtra(GroupManagerFragment.KEY_OPERATION_MODE, GroupManagerFragment.MODE_SWITCH_GROUP);
        context.startActivity(intent);
    }
}
