package com.sscf.investment.detail;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.sscf.investment.R;
import com.sscf.investment.common.entity.CommentDraftEntity;
import com.sscf.investment.common.entity.FeedDraftEntity;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.social.FeedRequestManager;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ConfirmDialog;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import BEC.E_FEED_TYPE;
import BEC.ReplyCommentInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liqf on 2016/9/10.
 */
@Route("CommentEditActivity")
public class CommentEditActivity extends BaseFragmentActivity implements TextWatcher {

    private static final int MAX_INPUT_LENGTH = 500;
    private static final int MIN_INPUT_LENGTH = 5;

    private static final int DISPLAY_TIPS_LENGTH = 400;

    private String mFeedId;
    private int mFeedType;
    private String mCommentNickName;
    private ReplyCommentInfo mReplyCommentInfo;

    @BindView(R.id.actionbar_right_button) TextView mSendBtn;
    @BindView(R.id.modifyMemoEditText) EditText mModifyMemoEditText;
    private String mDtSecCode;
    private String mSecName;

    private ConfirmDialog mExitConfirmDialog;

    @BindView(R.id.wordTips) TextView mWordTips;
    private long mAccountId;

    private int mType;
    public static final int TYPE_FEED = 0;
    public static final int TYPE_COMMENT = 1;

    private FeedRequestManager mFeedRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_comment_edit);
        ButterKnife.bind(this);

        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        mAccountId = accountManager.getAccountId();
        mCommentNickName = accountManager.getAccountInfo().nickname;

        mFeedRequestManager = DengtaApplication.getApplication().getFeedRequestManager();

        Intent intent = getIntent();
        mDtSecCode = intent.getStringExtra(DengtaConst.KEY_SEC_CODE);
        mSecName = intent.getStringExtra(DengtaConst.KEY_SEC_NAME);
        mFeedId = intent.getStringExtra(CommentListActivity.KEY_FEED_ID);
        mFeedType = intent.getIntExtra(CommentListActivity.KEY_FEED_TYPE, -1);
        mReplyCommentInfo = (ReplyCommentInfo) intent.getSerializableExtra(CommentListActivity.KEY_REPLY_COMMENT_INFO);

        if (TextUtils.isEmpty(mFeedId)) {
            mType = TYPE_FEED;
            StatisticsUtil.reportAction(StatisticsConst.COMMENT_EDIT_NEW_FEED);
        } else {
            mType = TYPE_COMMENT;
            StatisticsUtil.reportAction(StatisticsConst.COMMENT_EDIT_NEW_COMMENT);
        }

        initViews();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.comment_input_title);
        mSendBtn.setText(R.string.send);
        mSendBtn.setEnabled(false);

        if (mFeedType == E_FEED_TYPE.E_FT_STOCK_REVIEW && TextUtils.isEmpty(mFeedId)) {
            mModifyMemoEditText.setHint(getString(R.string.comment_hint_comment, mSecName));
        } else {
            mModifyMemoEditText.setHint(getString(R.string.comment_hint_invest, mReplyCommentInfo.getSReplyNickName()));
        }

        mModifyMemoEditText.addTextChangedListener(this);

        if (mType == TYPE_FEED) {
            loadFeedDraftFromCache();
        } else if (mType == TYPE_COMMENT) {
            loadCommentDraftFromCache();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    @OnClick(R.id.actionbar_back_button)
    public void onBackClicked() {
        exit();
    }

    @OnClick(R.id.actionbar_right_button)
    public void onSendClicked() {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final String comment = mModifyMemoEditText.getText().toString();

        if (isInputInvalid(comment)) {
            DengtaApplication.getApplication().showToast(getInputErrorStringId());
            return;
        }

        if (mType == TYPE_FEED) {
            mFeedRequestManager.postFeed(comment, mDtSecCode, mSecName, mFeedRequestManager);
        } else if (mType == TYPE_COMMENT) {
            mFeedRequestManager.postComment(comment, mFeedId, mFeedType, mCommentNickName, mReplyCommentInfo, mFeedRequestManager);
        }
        StatisticsUtil.reportAction(StatisticsConst.COMMENT_EDIT_SEND_CLICKED);

        saveDraft("");

        finish();
    }

    private void saveDraftAndExit(final boolean giveUp) {
        if (giveUp) {
            saveDraft("");
            finish();
            return;
        }

        final String comment = mModifyMemoEditText.getText().toString();

        if (isInputInvalid(comment)) {
            DengtaApplication.getApplication().showToast(getInputErrorStringId());
            return;
        }

        saveDraft(comment);

        finish();
    }

    private int getInputErrorStringId() {
        if (mType == TYPE_FEED) {
            return R.string.comment_edit_input_error_tips;
        } else {
            return R.string.comment_edit_max_input_error_tips;
        }
    }

    private boolean isInputInvalid(String comment) {
        int length = comment.length();
        if (mType == TYPE_FEED) {
            return length > MAX_INPUT_LENGTH || length < MIN_INPUT_LENGTH;
        } else {
            return length > MAX_INPUT_LENGTH;
        }
    }

    private void loadFeedDraftFromCache() {
        new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object[] params) {
                final FeedDraftEntity draft = FeedRequestManager.getFeedDraftEntityFromDb(mAccountId, mDtSecCode);
                if (draft != null) {
                    return draft.getContent();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String draft) {
                mModifyMemoEditText.setText(draft);
                mModifyMemoEditText.setSelection(draft == null ? 0 : draft.length());
            }
        }.executeOnExecutor(DengtaApplication.getApplication().defaultExecutor);
    }

    private void loadCommentDraftFromCache() {
        new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object[] params) {
                final long replyAccountId = mReplyCommentInfo.getIReplyAccountId();
                final CommentDraftEntity draft = FeedRequestManager.getCommentDraftEntityFromDb(mAccountId, mFeedId, replyAccountId);
                if (draft != null) {
                    return draft.getContent();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String draft) {
                mModifyMemoEditText.setText(draft);
                mModifyMemoEditText.setSelection(draft == null ? 0 : draft.length());
            }
        }.executeOnExecutor(DengtaApplication.getApplication().defaultExecutor);
    }

    private void saveDraft(final String comment) {
        if (mType == TYPE_FEED) {
            saveFeedToDraftCache(comment);
        } else if (mType == TYPE_COMMENT) {
            saveCommentToDraftCache(comment);
        }
    }

    private void saveFeedToDraftCache(final String comment) {
        DengtaApplication.getApplication().defaultExecutor.execute(new Runnable() {
            @Override
            public void run() {
                FeedRequestManager.saveFeedToDraftCache(comment, mAccountId, mDtSecCode);
            }
        });
    }

    private void saveCommentToDraftCache(final String comment) {
        DengtaApplication.getApplication().defaultExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final long replyAccountId = mReplyCommentInfo.getIReplyAccountId();
                FeedRequestManager.saveCommentToDraftCache(comment, mAccountId, mFeedId, replyAccountId);
            }
        });
    }

    private void exit() {
        if (TextUtils.isEmpty(mModifyMemoEditText.getText().toString())) {
            saveDraft("");
            finish();
        } else {
            showExitConfirmDialog();
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void showExitConfirmDialog() {
        if (isDestroy()) {
            return;
        }

        if (mExitConfirmDialog == null) {
            final ConfirmDialog dialog = new ConfirmDialog(this);
            dialog.setMessage(R.string.comment_edit_exit_dialog_message);
            dialog.setCancelButton(R.string.quit, v -> {
                dismissExitConfirmDialog();
                saveDraftAndExit(true);
            });
            dialog.setOkButton(R.string.save, v -> {
                dismissExitConfirmDialog();
                saveDraftAndExit(false);
            });
            mExitConfirmDialog = dialog;
        }
        mExitConfirmDialog.show();
    }

    private void dismissExitConfirmDialog() {
        if (mExitConfirmDialog != null) {
            mExitConfirmDialog.dismiss();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        final int length = s.toString().length();
        final int trimmedLength = s.toString().trim().length();
        enableSendButton(trimmedLength);

        if (length > DISPLAY_TIPS_LENGTH) {
            if (mWordTips.getVisibility() != View.VISIBLE) {
                mWordTips.setVisibility(View.VISIBLE);
            }
            if (length <= MAX_INPUT_LENGTH) {
                mWordTips.setText(getString(R.string.memo_edit_tips1, MAX_INPUT_LENGTH - length));
            } else {
                final Resources res = getResources();
                mWordTips.setText("");
                mWordTips.append(res.getString(R.string.memo_edit_tips2));
                final SpannableString count = new SpannableString(String.valueOf(length - MAX_INPUT_LENGTH));
                count.setSpan(getRedSpan(), 0, count.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mWordTips.append(count);
                mWordTips.append(res.getString(R.string.word));
            }
        } else {
            if (mWordTips.getVisibility() != View.INVISIBLE) {
                mWordTips.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void enableSendButton(int trimmedLength) {
        if (mType == TYPE_FEED) {
            mSendBtn.setEnabled(trimmedLength >= MIN_INPUT_LENGTH);
        } else {
            mSendBtn.setEnabled(trimmedLength > 0);
        }
    }

    private ForegroundColorSpan mRedSpan;

    private ForegroundColorSpan getRedSpan() {
        if (mRedSpan == null) {
            mRedSpan = new ForegroundColorSpan(getResources().getColor(R.color.stock_red_color));
        }
        return mRedSpan;
    }
}
