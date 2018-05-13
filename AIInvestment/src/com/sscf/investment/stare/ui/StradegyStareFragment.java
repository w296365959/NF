package com.sscf.investment.stare.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dengtacj.component.entity.db.StockDbEntity;
import com.dengtacj.component.entity.stare.StareGroupInfo;
import com.dengtacj.component.entity.stare.StareItemInfo;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.stare.ui.widget.SelectedResultFlowLayout;
import com.sscf.investment.stare.ui.widget.StareSelectDialog;
import com.sscf.investment.stare.ui.widget.SubmitableFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import BEC.E_STARE_STRATEGY_TYPE;

/**
 * Created by yorkeehuang on 2017/9/11.
 */

public class StradegyStareFragment extends SubmitableFragment implements View.OnClickListener {

    private List<GroupUI> mGroupUiList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_stare_strategy, container, false);
        initView((ViewGroup) view);
        return view;
    }

    private void initView(ViewGroup rootView) {
        List<StareGroupInfo> stareGroupInfos = mPresenter.getStrategyGroupList();
        if(stareGroupInfos != null) {
            mGroupUiList.clear();
            for(StareGroupInfo stareGroupInfo : stareGroupInfos) {
                View groupView = View.inflate(getContext(), R.layout.stare_strategy_group, null);
                rootView.addView(groupView);
                GroupUI groupUI = new GroupUI(stareGroupInfo.groupTitle, stareGroupInfo.straType, groupView);
                groupUI.groupTitle.setText(stareGroupInfo.groupTitle);
                groupUI.helpView.setOnClickListener(this);
                groupUI.helpView.setTag(groupUI);
                groupUI.selectExtandView.setTag(groupUI);
                groupUI.selectExtandView.setOnClickListener(this);
                mGroupUiList.add(groupUI);
            }
        }
    }

    @Override
    public void initFragmentValue(StockDbEntity stockEntity) {
        List<Integer> strategyList = stockEntity.getStrategyList();
        if(strategyList != null && !strategyList.isEmpty()) {
            Set<Integer> strategySet = new HashSet<>(strategyList.size());
            for(Integer strategy : strategyList) {
                strategySet.add(strategy);
            }

            for(GroupUI groupUI : mGroupUiList) {
                StareGroupInfo groupInfo = mPresenter.getStrategyGroupByTitle(groupUI.groupName);
                if(groupInfo != null && groupInfo.allItems != null) {
                    Set<Integer> selectedItems = new HashSet<>();
                    for(StareItemInfo itemInfo : groupInfo.allItems) {
                        if(strategySet.contains(itemInfo.value)) {
                            selectedItems.add(itemInfo.value);
                        }
                    }
                    setSelectedResult(selectedItems, groupInfo.allItems, groupUI);
                }
            }
        }
    }

    @Override
    public int checkFragment(StockDbEntity stockEntity) {
        int result = Submitable.RESULT_NOCHANGE;
        List<Integer> values = new ArrayList<>();
        for(GroupUI groupUI : mGroupUiList) {
            values.addAll(groupUI.resultLayout.getValues());
        }

        String resultText = StockDbEntity.convertIntegerList2Text(values);
        if(!TextUtils.equals(resultText, stockEntity.getStrategyId())) {
            result = Submitable.RESULT_SHOULD_SUBMIT;
        }
        return result;
    }

    @Override
    public void submitFragment(StockDbEntity stockEntity) {
        List<Integer> values = new ArrayList<>();
        for(GroupUI groupUI : mGroupUiList) {
            values.addAll(groupUI.resultLayout.getValues());
        }

        String resultText = StockDbEntity.convertIntegerList2Text(values);
        stockEntity.setStrategyId(resultText);
    }

    @Override
    public void onClick(View v) {
        GroupUI groupUi = (GroupUI) v.getTag();
        if(groupUi == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.select_extand:
                StareGroupInfo groupInfo = mPresenter.getStrategyGroupByTitle(groupUi.groupName);
                if(groupInfo != null) {
                    mPresenter.showSelectDialog(groupInfo, groupUi.resultLayout.getValueSet(), StareSelectDialog.LIST_LAYOUT_TYPE_VERTICAL);
                }
                break;
            case R.id.help_icon:
                String url = null;
                switch (groupUi.straType) {
                    case E_STARE_STRATEGY_TYPE.E_EST_KLINE_STRA:
                        url = DengtaApplication.getApplication().getUrlManager().getKLineStrategyFaqUrl();
                        break;
                    case E_STARE_STRATEGY_TYPE.E_EST_AVGLINE_STRA:
                        url = DengtaApplication.getApplication().getUrlManager().getAvgLineStrategyFaqUrl();
                        break;
                    default:
                }
                if(url != null) {
                    WebBeaconJump.showCommonWebActivity(getContext(), url);
                }
                break;
            default:
        }
    }

    public void setSelectedResult(StareGroupInfo groupInfo, Set<Integer> selectedItems) {
        for(GroupUI groupUI : mGroupUiList) {
            if(TextUtils.equals(groupInfo.groupTitle, groupUI.groupName)) {
                setSelectedResult(selectedItems, groupInfo.allItems, groupUI);
                break;
            }
        }
    }

    private void setSelectedResult(Set<Integer> selectedItems, List<StareItemInfo> allItems, GroupUI groupUI) {
        if(groupUI != null) {
            groupUI.resultLayout.clearAll();
            boolean isEmpty = true;
            for(int i=0, size=allItems.size(); i<size; i++) {
                StareItemInfo itemInfo = allItems.get(i);
                if(selectedItems.contains(itemInfo.value)) {
                    isEmpty = false;
                    groupUI.resultLayout.addItem(itemInfo.value, itemInfo.name);
                }
            }

            if(isEmpty) {
                groupUI.emptyView.setVisibility(View.VISIBLE);
                groupUI.resultLayout.setVisibility(View.GONE);
            } else {
                groupUI.emptyView.setVisibility(View.GONE);
                groupUI.resultLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private class GroupUI {
        public String groupName;
        public int straType;
        public TextView groupTitle;
        public View helpView;
        public TextView selectExtandView;

        public SelectedResultFlowLayout resultLayout;

        public TextView emptyView;

        public GroupUI(String name, int type, View rootView) {
            groupName = name;
            straType = type;
            groupTitle = (TextView) rootView.findViewById(R.id.group_title);
            helpView = rootView.findViewById(R.id.help_icon);
            selectExtandView = (TextView) rootView.findViewById(R.id.select_extand);
            resultLayout = (SelectedResultFlowLayout) rootView.findViewById(R.id.selected_result_list);
            emptyView = (TextView) rootView.findViewById(R.id.empty);
        }
    }
}
