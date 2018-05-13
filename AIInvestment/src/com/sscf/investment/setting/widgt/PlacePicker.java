package com.sscf.investment.setting.widgt;

import android.content.Context;
import android.text.TextUtils;
import com.bigkoo.pickerview.OptionsPickerView;
import java.util.ArrayList;

/**
 * Created by davidwei on 2016-09-07
 *
 */
public class PlacePicker extends OptionsPickerView {
    OnGetPlaceListener mOnGetPlaceListener;

    ArrayList<String> mOptions1Items;
    ArrayList<ArrayList<String>> mOptions2Items;

    public PlacePicker(Context context) {
        super(context);
    }

    public void initPlace(final ArrayList<String> options1Items, final ArrayList<ArrayList<String>> options2Items) {
        mOptions1Items = options1Items;
        mOptions2Items = options2Items;

        //三级联动效果
        setPicker(options1Items, options2Items, true);

        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        setTitle("选择城市");
        setCyclic(false, false, false);
        //设置默认选中的三级项目

        setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if (mOnGetPlaceListener != null) {
                    //返回的分别是三个级别的选中位置
                    mOnGetPlaceListener.onGetPlace(options1Items.get(options1), options2Items.get(options1).get(option2));
                }
            }
        });
    }

    public void setPlace(final String provice, final String city) {
        final ArrayList<String> options1Items = mOptions1Items;
        final int size = options1Items.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(provice, options1Items.get(i))) {
                final ArrayList<String> cites = mOptions2Items.get(i);
                final int subSize = cites.size();
                for (int j = 0; j < subSize; j++) {
                    if (TextUtils.equals(city, cites.get(j))) {
                        setSelectOptions(i, j);
                        return;
                    }
                }
                return;
            }
        }
    }

    public void setOnGetPlaceListener(final OnGetPlaceListener l) {
        mOnGetPlaceListener = l;
    }

    public interface OnGetPlaceListener {
        void onGetPlace(String provice, String city);
    }
}
