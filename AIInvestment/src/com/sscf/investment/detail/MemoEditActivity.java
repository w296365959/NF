package com.sscf.investment.detail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.sscf.investment.R;
import com.dengtacj.request.QuoteRequestManager;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.utils.DengtaConst;
import com.sscf.investment.utils.PeriodicHandlerManager;
import com.sscf.investment.utils.StringUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;
import com.sscf.investment.widget.ConfirmDialog;
import com.sscf.investment.component.ui.widget.SwipeBackLayout;
import java.util.ArrayList;
import BEC.E_SEC_STATUS;
import BEC.QuoteSimpleRsp;
import BEC.SecQuote;
import BEC.SecSimpleQuote;

/**
 * Created by liqf on 2016/8/2.
 */
public class MemoEditActivity extends BaseFragmentActivity implements View.OnClickListener, Runnable,
        DataSourceProxy.IRequestCallback, Handler.Callback, TextWatcher {
    private static final int MAX_INPUT_LENGTH = 500;
    private static final int DISPLAY_TIPS_LENGTH = 400;

    private EditText mModifyMemoEditText;
    private String mDtSecCode;
    private ArrayList<String> mDtSecCodes = new ArrayList<>(1);
    private String mInitialComment;

    private TextView mStockPriceView;
    private TextView mStockRatioView;

    private PeriodicHandlerManager mPeriodicHandlerManager;

    private Handler mHandler;

    private ConfirmDialog mExitConfirmDialog;

    private TextView mWordTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                .getManager(IPortfolioDataManager.class.getName());
        if (portfolioDataManager == null) {
            finish();
            return;
        }
        final String dtSecCode = getIntent().getStringExtra(DengtaConst.KEY_SEC_CODE);
        if (TextUtils.isEmpty(dtSecCode)) {
            finish();
            return;
        }

        setContentView(R.layout.main_activity_memo_edit);

        mDtSecCode = dtSecCode;
        mDtSecCodes.add(dtSecCode);

        mInitialComment = portfolioDataManager.getComment(dtSecCode);
        if (mInitialComment == null) {
            mInitialComment = "";
        }

        initViews();

        mHandler = new Handler(this);
        mPeriodicHandlerManager = new PeriodicHandlerManager(this);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.actionbar_title)).setText(R.string.operation_memo_edit);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        TextView saveButton = (TextView) findViewById(R.id.actionbar_right_button);
        saveButton.setText(R.string.save);
        saveButton.setOnClickListener(this);

        mModifyMemoEditText = (EditText) findViewById(R.id.modifyMemoEditText);
        mModifyMemoEditText.setText(mInitialComment);
        mModifyMemoEditText.addTextChangedListener(this);
        final int length = mInitialComment == null ? 0 : mInitialComment.length();
        mModifyMemoEditText.setSelection(length);

        mWordTips = (TextView) findViewById(R.id.wordTips);

        ((TextView)findViewById(R.id.portfolioRemindStockTitle)).setText(getIntent().getStringExtra(DengtaConst.KEY_SEC_NAME));
        mStockPriceView = (TextView)findViewById(R.id.portfolioRemindStockPrice);
        mStockRatioView = (TextView)findViewById(R.id.portfolioRemindStockRatio);
        final SecQuote quote = DengtaApplication.getApplication().getDataCacheManager().getSecQuote(mDtSecCode);
        setPriceUpdownText(quote);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackLayout.attachSwipeLayout(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPeriodicHandlerManager.runPeriodic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPeriodicHandlerManager.stop();
    }

    @Override
    public void run() {
        QuoteRequestManager.getSimpleQuoteRequest(mDtSecCodes, this, null);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity == null) {
                return;
            }
            switch (data.getEntityType()) {
                case EntityObject.ET_GET_SIMPLE_QUOTE:
                    final ArrayList<SecSimpleQuote> secList = ((QuoteSimpleRsp) entity).vSecSimpleQuote;
                    final int size = secList == null ? 0 : secList.size();
                    if (size == 1) {
                        mHandler.obtainMessage(0, secList.get(0)).sendToTarget();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        final SecSimpleQuote quote = (SecSimpleQuote) msg.obj;
        setPriceUpdownText(quote);
        DengtaApplication.getApplication().getDataCacheManager().setSecSimpleQuote(quote);
        return true;
    }

    private void setPriceUpdownText(final SecQuote quote) {
        if (quote != null) {
            setPriceUpdownText(quote.fNow, quote.fClose, quote.iTpFlag, quote.eSecStatus);
        }
    }

    private void setPriceUpdownText(final SecSimpleQuote quote) {
        if (quote != null) {
            setPriceUpdownText(quote.fNow, quote.fClose, quote.iTpFlag, quote.eSecStatus);
        }
    }

    private void setPriceUpdownText(final float now, final float close, final int tpFlag, final int status) {
        TextAppearanceSpan span = null;

        final Resources resources = getResources();
        float currentPrice = 0f;

        final SpannableStringBuilder price = new SpannableStringBuilder(resources.getString(R.string.stock_list_head_new_price));
        price.append(' ');
        final int preLength = price.length();

        final SpannableStringBuilder updown = new SpannableStringBuilder(resources.getString(R.string.delta));
        updown.append(' ');
        final int updownPreLength = updown.length();

        if (status == E_SEC_STATUS.E_SS_SUSPENDED) {
            span = StringUtil.getSuspensionStyle();
            currentPrice = close;
            updown.append("--");
        } else {
            if (now <= 0) { // 停牌处理
                span = StringUtil.getSuspensionStyle();
                currentPrice = close;
                updown.append("--");
            } else {
                final float updownFlt = (now / close - 1) * 100;
                if (now > close) {
                    span = StringUtil.getUpStyle();
                    updown.append('+');
                } else if (now < close) {
                    span = StringUtil.getDownStyle();
                } else {
                    span = StringUtil.getSuspensionStyle();
                }
                currentPrice = now;
                updown.append(StringUtil.getFormatedFloat(updownFlt)).append('%');
            }
        }

        price.append(StringUtil.getFormattedFloat(currentPrice, tpFlag));

        price.setSpan(span, preLength, price.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mStockPriceView.setText(price);

        updown.setSpan(span, updownPreLength, updown.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mStockRatioView.setText(updown);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        switch (v.getId()) {
            case R.id.actionbar_right_button:
                save();
                break;
            case R.id.actionbar_back_button:
                exit();
                break;
            case R.id.ok:
                dismissExitConfirmDialog();
                save();
                break;
            case R.id.cancel:
                dismissExitConfirmDialog();
                finish();
                break;
            default:
                break;
        }
    }

    private void save() {
        final String comment = mModifyMemoEditText.getText().toString();
        if (comment.length() > MAX_INPUT_LENGTH) {
            DengtaApplication.getApplication().showToast(R.string.memo_edit_max_input_error_tips);
            return;
        }
        if (!TextUtils.equals(mInitialComment, comment)) {
            final IPortfolioDataManager portfolioDataManager = (IPortfolioDataManager) ComponentManager.getInstance()
                    .getManager(IPortfolioDataManager.class.getName());
            if (portfolioDataManager != null) {
                portfolioDataManager.setComment(mDtSecCode, comment, true);
            }
        }
        finish();
    }

    private void exit() {
        if (TextUtils.equals(mInitialComment, mModifyMemoEditText.getText().toString())) {
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
            dialog.setMessage(R.string.memo_edit_exit_dialog_message);
            dialog.setCancelButton(R.string.quit, this);
            dialog.setOkButton(R.string.save, this);
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

    private ForegroundColorSpan mRedSpan;

    private ForegroundColorSpan getRedSpan() {
        if (mRedSpan == null) {
            final ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.stock_red_color));
            mRedSpan = span;
        }
        return mRedSpan;
    }

    public static void show(final Context context, final String dtSecCode, final String secName) {
        Intent intent = new Intent(context, MemoEditActivity.class);
        intent.putExtra(DengtaConst.KEY_SEC_CODE, dtSecCode);
        intent.putExtra(DengtaConst.KEY_SEC_NAME, secName);
        context.startActivity(intent);
    }
}
