package com.sscf.investment.portfolio;

import com.sscf.investment.widget.BaseFragment;

/**
 * Created by yorkeehuang on 2018/1/4.
 */

public class PortfolioOrMarketSubFragment extends BaseFragment {
    private PortfolioOrMarketDisplayer mParentDisplayer;


    public void setParentDisplayer(PortfolioOrMarketDisplayer displayer) {
        mParentDisplayer = displayer;
    }

    private void releaseParentDisplayer() {
        mParentDisplayer = null;
    }

    protected void setGroupTitle(String groupTitle) {
        if(mParentDisplayer != null) {
            mParentDisplayer.setGroupTitle(groupTitle);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releaseParentDisplayer();
    }
}
