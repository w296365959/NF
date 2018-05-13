package com.sscf.investment.detail.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sscf.investment.R;
import com.sscf.investment.utils.DengtaConst;

/**
 * Created by liqf on 2015/10/31.
 */
public class HongKongShareholderFragment extends Fragment {
    private View mContentView;
    private String mDtSecCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contextView = getActivity().getLayoutInflater().inflate(R.layout.fragment_shareholder_hongkong, container, false);
        mContentView = contextView;

        Bundle args = getArguments();
        if (args != null) {
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
        }

        initResources();

        return contextView;
    }

    private void initResources() {

    }
}
