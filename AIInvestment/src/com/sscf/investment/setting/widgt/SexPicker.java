package com.sscf.investment.setting.widgt;

import android.content.Context;
import android.text.TextUtils;
import com.bigkoo.pickerview.OptionsPickerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by davidwei on 2016-09-07
 *
 */
public final class SexPicker extends OptionsPickerView implements OptionsPickerView.OnOptionsSelectListener {
    private OnGetSexListener mOnGetSexListener;
    private final ArrayList<String> mOptions1Items;

    public SexPicker(Context context) {
        super(context);
        ArrayList<String> options1Items = new ArrayList<String>(3);
        mOptions1Items = options1Items;

        options1Items.add("男");
        options1Items.add("女");
        options1Items.add("保密");

        setPicker(options1Items);
        setTitle("选择性别");
        setCyclic(false);
        setOnoptionsSelectListener(this);
    }

    public SexPicker(Context context, ArrayList<String> options1Items) {
        super(context);
        mOptions1Items = options1Items;

        setPicker(options1Items);
        setCyclic(false);
        setOnoptionsSelectListener(this);
    }

    public void setSex(final String sex) {
        final ArrayList<String> options1Items = mOptions1Items;
        final int size = options1Items.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(sex, options1Items.get(i))) {
                setSelectOptions(i);
                return;
            }
        }
    }

    public void setOnGetSexListener(OnGetSexListener l) {
        mOnGetSexListener = l;
    }

    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        if (mOnGetSexListener != null) {
            mOnGetSexListener.onGetSex(mOptions1Items.get(options1));
        }
    }

    public interface OnGetSexListener {
        void onGetSex(String sex);
    }
}
