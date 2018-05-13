package com.sscf.investment.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.setting.manager.KLineSettingManager;
import com.sscf.investment.widget.CommonAdapter;
import com.sscf.investment.widget.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEN on 2018/4/8.
 */

public class AddCandicatorActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ListView mListView;

    private KLineSettingManager mKLineSettingManager;

    private AddCandicatorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candicators);
        mKLineSettingManager = DengtaApplication.getApplication().getKLineSettingManager();
        initView();
    }

    private void initView(){
        TextView mTv = (TextView) findViewById(R.id.actionbar_title);
        mTv.setText(R.string.setting_k_line_settings);
        mTv = (TextView) findViewById(R.id.actionbar_right_button);
        mTv.setText(R.string.finish);
        mTv.setOnClickListener(this);

        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.list_add_candicator);
        mAdapter = new AddCandicatorAdapter(this, mKLineSettingManager.getUndisplayCandicators());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this, true);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.actionbar_right_button:
                mKLineSettingManager.addDisplayCandicator(mAdapter.getSelectedCandicators());
                finish();
                break;
        }
    }

    final class AddCandicatorAdapter extends CommonAdapter<String> implements AdapterView.OnItemClickListener {

        private ArrayList<String> selectedCandicators;

        public ArrayList<String> getSelectedCandicators() {
            return selectedCandicators;
        }

        public AddCandicatorAdapter(Context context, List<String> datas) {
            super(context, datas, R.layout.item_add_candicators);
            selectedCandicators = new ArrayList<>();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            StatisticsUtil.reportAction(StatisticsConst.A_INDIVIDUAL_SHARE_SET_UP_INDEX_CLICK);
            if (isChecked(position)){
                selectedCandicators.remove(getItem(position));
            }else{
                selectedCandicators.add(getItem(position));
            }
            notifyDataSetChanged();
        }

        private boolean isChecked(int position){
            for (int i = 0 ; i < selectedCandicators.size() ; i++){
                if (getItem(position).equals(selectedCandicators.get(i))){
                    return true;
                }
            }
            return false;
        }

        @Override
        public void convert(CommonViewHolder holder, String item, int position) {
            final TextView itemView = holder.getView(R.id.tvCandicatorName);
            itemView.setText(item);
            holder.getView(R.id.ivSeletected).setVisibility(isChecked(position) ? View.VISIBLE : View.GONE);
        }
    }
}
