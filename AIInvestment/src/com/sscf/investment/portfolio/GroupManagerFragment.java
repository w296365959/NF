package com.sscf.investment.portfolio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ocr.entity.Stock;
import com.sscf.investment.component.ui.widget.InputDialog;
import com.sscf.investment.sdk.main.manager.CallbackManager;
import com.sscf.investment.main.DengtaApplication;
import com.dengtacj.component.entity.db.GroupEntity;
import com.sscf.investment.setting.LoginActivity;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqf on 2016/6/15.
 */
public final class GroupManagerFragment extends Fragment implements View.OnClickListener {
    public static final int MAX_INPUT_LENGTH = 12;
    public static final int MODE_SWITCH_GROUP = 0;
    public static final int MODE_SELECT_GROUP = 1;
    public static final int MODE_IMPORT_SELECT_GROUP = 2;
    public static String KEY_OPERATION_MODE = "operation_mode";

    private int mOperationMode = MODE_SWITCH_GROUP;
    private String mDtSecCode;
    private String mSecName;

    private View mContextView;
    private ListView mGroupListView;
    private GroupManagerAdapter mGroupManagerAdapter;
    private IPortfolioDataManager mPortfolioDataManager;
    private TextView mTitle;
    private View mEditButton;
    private CallbackManager.DtCallback mPortfolioChangeCallback = obj -> getActivity().runOnUiThread(() -> reloadData());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        mPortfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());

        if (mPortfolioDataManager == null) {
            if (activity != null) {
                activity.finish();
                return null;
            }
        }

        Bundle args = getArguments();
        ArrayList<Stock> stockList = null;
        if (args != null) {
            mOperationMode = args.getInt(KEY_OPERATION_MODE);
            mDtSecCode = args.getString(DengtaConst.KEY_SEC_CODE);
            mSecName = args.getString(DengtaConst.KEY_SEC_NAME);
            if(mOperationMode == MODE_IMPORT_SELECT_GROUP) {
                stockList = args.getParcelableArrayList(DengtaConst.KEY_STOCK_LIST);
            }
        }

        mContextView = inflater.inflate(R.layout.portfolio_group_manager, null);

        mTitle = (TextView) mContextView.findViewById(R.id.group_manager_title);
        if (mOperationMode == MODE_SWITCH_GROUP) {
            mTitle.setText(R.string.portfolio_group_mine);
        } else if (mOperationMode == MODE_SELECT_GROUP
                || mOperationMode == MODE_IMPORT_SELECT_GROUP) {
            mTitle.setText(R.string.portfolio_group_select);
        }

        mContextView.findViewById(R.id.close_button).setOnClickListener(this);
        mContextView.findViewById(R.id.new_button).setOnClickListener(this);
        mEditButton = mContextView.findViewById(R.id.edit_button);
        if (mOperationMode == MODE_SWITCH_GROUP) {
            mEditButton.setOnClickListener(this);
        } else if (mOperationMode == MODE_SELECT_GROUP
                || mOperationMode == MODE_IMPORT_SELECT_GROUP) {
            mEditButton.setVisibility(View.GONE);
        }

        mGroupListView = (ListView) mContextView.findViewById(R.id.group_listview);
        mGroupManagerAdapter = new GroupManagerAdapter(getActivity(), mOperationMode, stockList, mDtSecCode, mSecName);
        mGroupListView.setAdapter(mGroupManagerAdapter);

        CallbackManager.getInstance().regCallback(CallbackManager.CM_TYPE_PORTFOLIO_DATA_CHANGE, mPortfolioChangeCallback);

        return mContextView;
    }

    private void setGroupId(int groupId) {
        BaseFragmentActivity activity = (BaseFragmentActivity) getActivity();
        if(activity != null && !activity.isDestroy()) {
            Intent data = new Intent();
            data.putExtra("groupId", groupId);
            activity.setResult(Activity.RESULT_OK, data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        reloadData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        CallbackManager.getInstance().unRegCallback(CallbackManager.CM_TYPE_PORTFOLIO_DATA_CHANGE, mPortfolioChangeCallback);
    }

    private void reloadData() {
        List<GroupEntity> allGroup = mPortfolioDataManager.getAllGroup(false, true);
        ArrayList<Integer> groupIds = new ArrayList<>();
        // 添加默认分组id
        groupIds.add(IPortfolioDataManager.SYSTEM_GROUP_ID);
        // 添加用户分组id
        if (allGroup != null && allGroup.size() > 0) {
            for (GroupEntity groupEntity : allGroup) {
                groupIds.add(groupEntity.getCreateTime());
            }
            if (mOperationMode == MODE_SWITCH_GROUP) {
                mEditButton.setVisibility(View.VISIBLE);
            } else {
                mEditButton.setVisibility(View.GONE);
            }
        } else {
            mEditButton.setVisibility(View.GONE);
        }
        mGroupManagerAdapter.setData(groupIds);
        mGroupManagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        int id = v.getId();
        switch (id) {
            case R.id.close_button:
                activity.finish();
                break;
            case R.id.new_button:
                if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
                    addNewGroup();
                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.edit_button:
                if (DengtaApplication.getApplication().getAccountManager().isLogined()) {
                    activity.startActivity(new Intent(activity, PortfolioGroupEditActivity.class));
                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private void addNewGroup() {
        List<GroupEntity> allGroup = mPortfolioDataManager.getAllGroup(false, false);
        if (allGroup != null && allGroup.size() >= DengtaConst.MAX_GROUP_COUNT) {
            DengtaApplication.getApplication().showToast(R.string.portfolio_add_group_max);
            return;
        }

        final InputDialog inputDialog = new InputDialog(getActivity());
        inputDialog.setMaxInputLength(MAX_INPUT_LENGTH);
        inputDialog.setTitle(R.string.portfolio_add_group);
        inputDialog.setInputHint(R.string.portfolio_add_group_hint);
        inputDialog.setOkButton(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = inputDialog.getInputText().trim();
                inputDialog.dismiss();
                int checkResult = mPortfolioDataManager.checkGroupNameValid(groupName);
                final DengtaApplication application = DengtaApplication.getApplication();
                if (checkResult == IPortfolioDataManager.GROUP_NAME_VALID) {
                    application.showToast(R.string.portfolio_new_group_success);
                    mPortfolioDataManager.addGroup(0, 0, groupName, true);
                    reloadData();
                    StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_GROUP_MANAGER_NEW);
                } else if (checkResult == IPortfolioDataManager.GROUP_NAME_INVALID_TOO_LONG) {
                    application.showToast(R.string.portfolio_group_name_too_long);
                } else if (checkResult == IPortfolioDataManager.GROUP_NAME_INVALID_ALREADY_EXIST) {
                    application.showToast(R.string.portfolio_group_name_already_exist);
                } else if (checkResult == IPortfolioDataManager.GROUP_NAME_INVALID_CHAR_NOT_ALLOWED) {
                    application.showToast(R.string.portfolio_group_name_invalid_char);
                }
            }
        });
        inputDialog.show();
    }

    final class GroupManagerAdapter extends BaseAdapter implements View.OnClickListener {
        private final int mOperationMode;
        private final String mDtSecCode;
        private final String mSecName;
        private ArrayList<Integer> mGroupIds = new ArrayList<>();
        private final LayoutInflater mInflater;
        private final int mHighlightColor;
        private final int mNonHighlightColor;
        private final Activity mActivity;

        public GroupManagerAdapter(Activity activity, int operationMode, ArrayList<Stock> stockList, String dtSecCode, String secName) {
            mActivity = activity;
            mOperationMode = operationMode;
            mDtSecCode = dtSecCode;
            mSecName = secName;
            mInflater = LayoutInflater.from(activity);
            mHighlightColor = ContextCompat.getColor(activity, R.color.default_text_color_100);
            mNonHighlightColor = ContextCompat.getColor(activity, R.color.default_text_color_60);
        }

        public void setData(ArrayList<Integer> groupIds) {
            mGroupIds = groupIds;
            if (mGroupIds == null) {
                mGroupIds = new ArrayList<>();
            }
        }

        @Override
        public int getCount() {
            return mGroupIds.size();
        }

        @Override
        public Object getItem(int position) {
            return mGroupIds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String groupName = mPortfolioDataManager.getUserGroupName(mGroupIds.get(position));

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.portfolio_group_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mGroupId = mGroupIds.get(position);
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.group_name);
                viewHolder.mAlreadyAddedText = (TextView) convertView.findViewById(R.id.already_added);
                viewHolder.mCheckMark = (ImageView) convertView.findViewById(R.id.check_mark);
                convertView.setTag(viewHolder);
                convertView.setOnClickListener(this);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.mGroupId = mGroupIds.get(position);
            viewHolder.mTitle.setText(groupName);

            viewHolder.mAlreadyAddedText.setVisibility(View.GONE);
            List<GroupEntity> allGroup = mPortfolioDataManager.getAllGroup(false, false);
            if (mOperationMode == GroupManagerFragment.MODE_SELECT_GROUP) {
                //复选模式
                if (IPortfolioDataManager.SYSTEM_GROUP_ID == mGroupIds.get(position) && mPortfolioDataManager.isPortfolio(mDtSecCode)) {
                    viewHolder.mTitle.setTextColor(mNonHighlightColor);
                    viewHolder.mAlreadyAddedText.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mTitle.setTextColor(mHighlightColor);
                    viewHolder.mAlreadyAddedText.setVisibility(View.GONE);
                    for (GroupEntity groupEntity : allGroup) {
                        String name = groupEntity.getName();
                        if (TextUtils.equals(name, groupName) && groupEntity.isHaveStock(false, mDtSecCode)) {
                            viewHolder.mTitle.setTextColor(mNonHighlightColor);
                            viewHolder.mAlreadyAddedText.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
            } else if (mOperationMode == GroupManagerFragment.MODE_SWITCH_GROUP
                    || mOperationMode == GroupManagerFragment.MODE_IMPORT_SELECT_GROUP) { //切换模式
                viewHolder.mTitle.setTextColor(mHighlightColor);
                final GroupEntity currentGroup = mPortfolioDataManager.getCurrentGroup();
                final GroupEntity group = mPortfolioDataManager.getUserGroup(mGroupIds.get(position));

                if (currentGroup == group) { // 两个相同的对象，说明是同一个组
                    viewHolder.mCheckMark.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mCheckMark.setVisibility(View.GONE);
                }
            }

            return convertView;
        }

        @Override
        public void onClick(View v) {
            ViewHolder viewHolder = (ViewHolder) v.getTag();
            switch (mOperationMode) {
                case GroupManagerFragment.MODE_SWITCH_GROUP:
                    mPortfolioDataManager.changeCurrentGroup(viewHolder.mGroupId);
                    StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_GROUP_MANAGER_SWITCH);
                    break;
                case GroupManagerFragment.MODE_SELECT_GROUP:
                    mPortfolioDataManager.addStock2Group(viewHolder.mGroupId, mDtSecCode, mSecName);
                    DengtaApplication.getApplication().showToast(R.string.portfolio_add_group_success);
                    StatisticsUtil.reportAction(StatisticsConst.PORTFOLIO_GROUP_MANAGER_ADD);
                    break;
                case GroupManagerFragment.MODE_IMPORT_SELECT_GROUP:
                    setGroupId(viewHolder.mGroupId);
                    break;
                default:
            }
            mActivity.finish();
        }
    }
}

final class ViewHolder {
    public int mGroupId;
    public TextView mTitle;
    public TextView mAlreadyAddedText;
    public ImageView mCheckMark;
}
