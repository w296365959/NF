package com.sscf.investment.web.sdk.widget;

import BEC.*;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IRedDotManager;
import com.sscf.investment.component.ui.widget.CommonToast;
import com.sscf.investment.component.ui.widget.SettingToolsLayout;
import com.sscf.investment.component.ui.widget.ToolsItem;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.utils.DeviceUtil;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.web.sdk.JsProxy;
import com.sscf.investment.web.sdk.R;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by davidwei on 2016/07/05
 *
 */
public final class IntelligentAnswerInputLayout extends LinearLayout implements View.OnClickListener, TextWatcher,
        View.OnTouchListener, RecognizerListener, DataSourceProxy.IRequestCallback {
    private static final String TAG = IntelligentAnswerInputLayout.class.getSimpleName();
    private ImageView mSwitchInputButton;
    private EditText mEditText;
    private View mInputCommitButton;
    private View mVoiceInputLayout;
    private View mToolsButton;
    private SettingToolsLayout mToolsLayout;
    private VoiceVolumeAnimView mLeftAnimView;
    private VoiceVolumeAnimView mRightAnimView;
    private TextView mVoiceStateTipsView;
    private View mInputToolsButtonRedDot;

    private DtWebView mWebView;

    private SpeechRecognizer mIat;
    private LinkedHashMap<String, String> mIatResults = new LinkedHashMap<>();

    public static final int INPUT_STATE_DEFAULT = 1;
    public static final int INPUT_STATE_TEXT = 2;
    public static final int INPUT_STATE_VOICE = 3;
    public static final int INPUT_STATE_TOOLS = 4;

    private int mInputState = INPUT_STATE_DEFAULT;

    private static final int VOICE_STATE_DEFAULT = 1;
    private static final int VOICE_STATE_RECORDING = 2;
    private static final int VOICE_STATE_RECOGNIZING = 3;
    private static final int VOICE_STATE_ERROR = 4;

    private int mVoiceState = VOICE_STATE_DEFAULT;

    private LinearLayout inputSuggestionLayout;
    private ArrayList<BEC.InputDefaultItem> inputDefaultItemList;
    private ArrayList<BEC.SuggestionItem> suggestionItemList;

    // 默认显示语的索引
    private int inputDefaultIndex = 0;

    public IntelligentAnswerInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mSwitchInputButton = (ImageView) findViewById(R.id.switchInputButton);
        mSwitchInputButton.setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.input);
        mEditText.addTextChangedListener(this);
        mEditText.setOnClickListener(this);
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    switchInputState(INPUT_STATE_TEXT);
                }
            }
        });
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    commit();
                    return true;
                }
                return false;
            }
        });

        mInputCommitButton = findViewById(R.id.inputCommitButton);
        mInputCommitButton.setOnClickListener(this);
        findViewById(R.id.voiceInputButton).setOnTouchListener(this);
        mLeftAnimView = (VoiceVolumeAnimView) findViewById(R.id.leftAnimView);
        mRightAnimView = (VoiceVolumeAnimView) findViewById(R.id.rightAnimView);
        mVoiceInputLayout = findViewById(R.id.voiceInputLayout);
        mVoiceStateTipsView = (TextView) findViewById(R.id.voiceStateTips);
        mToolsButton = findViewById(R.id.inputToolsButton);
        mToolsButton.setOnClickListener(this);
        mInputToolsButtonRedDot = findViewById(R.id.inputToolsButtonRedDot);
        final IRedDotManager redDotManager = (IRedDotManager) ComponentManager.getInstance()
                .getManager(IRedDotManager.class.getName());
        if (redDotManager != null) {
            mInputToolsButtonRedDot.setVisibility(redDotManager.getInputToolsButtonState() ? VISIBLE : INVISIBLE);
        }

        initToolsItem();

        final int MAX_VOLUMN = 30;
        mLeftAnimView.setMaxVolumn(MAX_VOLUMN);
        mRightAnimView.setMaxVolumn(MAX_VOLUMN);
        mLeftAnimView.setDirection(VoiceVolumeAnimView.DIRECTION_RIGHT_TO_LEFT);
        mRightAnimView.setDirection(VoiceVolumeAnimView.DIRECTION_LEFT_TO_RIGHT);

        final Context context = getContext().getApplicationContext();
        SpeechUtility.createUtility(context, "appid=574bb463");
        mIat = SpeechRecognizer.createRecognizer(context, null);

        inputSuggestionLayout = (LinearLayout) findViewById(R.id.input_suggestion_layout);
    }

    /**
     * 请求默认输入
     */
    private void reqInputDefault() {
        DtLog.d(TAG, "reqInputDefault");
        InputBoxDefaultReq req = new InputBoxDefaultReq();
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        if (accountManager != null) {
            req.stUserInfo = accountManager.getUserInfo();
        }
        req.iWantNum = 20;
        DataEngine.getInstance().request(EntityObject.ET_INTELLIGENT_INPUT_DEFAULT_TEXT, req, this);
    }

    /**
     * 刷新默认输入内容
     */
    private Runnable updateInputDefaultTextRunnable = new Runnable() {
        @Override
        public void run() {
            if (inputDefaultItemList != null && inputDefaultItemList.size() > 0) {
                if (inputDefaultIndex >= inputDefaultItemList.size()) {
                    inputDefaultIndex = 0;
                }

                InputDefaultItem item = inputDefaultItemList.get(inputDefaultIndex);

                if (!mEditText.isInEditMode()) {
                    mEditText.setHint(item.sSentence);

                    DtLog.d(TAG, "UpdateInputDefaultTextRunnable setHint text=" + item.sSentence);
                }

                inputDefaultIndex++;

                // 5s后轮换
                postDelayed(updateInputDefaultTextRunnable, 5000);
            } else {
                DtLog.w(TAG, "no input default text");
            }
        }
    };

    /**
     * 请求联想语
     */
    private Runnable reqInputSuggestionRunnable = new Runnable() {
        @Override
        public void run() {
            String query = getEditTextString(mEditText);
            if (TextUtils.isEmpty(query)) {
                DtLog.d(TAG, "reqInputSuggestionRunnable query=null");
                return;
            }

            DtLog.d(TAG, "reqInputSuggestionRunnable query=" + query);
            InputSuggestionReq req = new InputSuggestionReq();
            final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                    .getManager(IAccountManager.class.getName());
            if (accountManager != null) {
                req.stUserInfo = accountManager.getUserInfo();
            }
            req.sQuery = getEditTextString(mEditText);
            DataEngine.getInstance().request(EntityObject.ET_INTELLIGENT_INPUT_SUGGESTION_TEXT, req, IntelligentAnswerInputLayout.this::callback);
        }
    };

    /**
     * 显示联想语
     */
    private Runnable addInputSuggestionRunnable = new Runnable() {
        @Override
        public void run() {
            inputSuggestionLayout.removeAllViews();
            final ArrayList<BEC.SuggestionItem> itemList = suggestionItemList;
            final int size = itemList == null ? 0 : itemList.size();
            if (size <= 0) {
                return;
            }

            String org = getEditTextString(mEditText);
            if (size == 1 && org.equalsIgnoreCase(itemList.get(0).sSentence)) {
                // 联想语只有一条，且和现有输入语一致，则不显示联想语
                return;
            }

            for (int i = 0; i < size; i++) {
                TextView textView = getSuggestionTextView(itemList.get(i));
                inputSuggestionLayout.addView(textView);
            }
        }
    };

    /**
     * 清除联想语
     */
    private void removeInputSuggestion() {
        post(new Runnable() {
            @Override
            public void run() {
                suggestionItemList = null;

                if (inputSuggestionLayout != null) {
                    inputSuggestionLayout.removeAllViews();
                }
            }
        });
    }

    private TextView getSuggestionTextView(SuggestionItem item) {
        TextView textView = new TextView(getContext());
        textView.setText(item.sSentence);
        textView.setTextColor(getResources().getColor(R.color.default_text_color_100));
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        int h = (int)getResources().getDimension(R.dimen.intelligent_input_suggestion_text_height);
        int w = inputSuggestionLayout.getWidth();

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(w, h);
        textView.setLayoutParams(params);

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView)v;
                mEditText.setText(tv.getText());
                removeInputSuggestion();
            }
        });

        DtLog.d(TAG, "add suggestion text=" + item.sSentence);
        return textView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        inputDefaultIndex = 0;
        reqInputDefault();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            mEditText.clearFocus();
        }
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (!success || data.getEntity() == null) {
            DtLog.w(TAG, "callback request failed, type=" + data.getEntityType());
            return;
        }

        if (data.getEntityType() == EntityObject.ET_INTELLIGENT_INPUT_DEFAULT_TEXT) {
            InputBoxDefaultRsp rsp = (InputBoxDefaultRsp)data.getEntity();
            inputDefaultItemList = rsp.getVtInputDefaultItem();

            removeCallbacks(updateInputDefaultTextRunnable);
            post(updateInputDefaultTextRunnable);
        } else if (data.getEntityType() == EntityObject.ET_INTELLIGENT_INPUT_SUGGESTION_TEXT) {
            InputSuggestionRsp rsp = (InputSuggestionRsp)data.getEntity();
            suggestionItemList = rsp.getVtSuggestionItem();

            post(addInputSuggestionRunnable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (inputDefaultItemList != null) {
            inputDefaultItemList.clear();
            inputDefaultItemList = null;
        }

        suggestionItemList = null;

        boolean rsr = removeCallbacks(reqInputSuggestionRunnable);
        boolean rir = removeCallbacks(updateInputDefaultTextRunnable);

        DtLog.d(TAG, "onDetachedFromWindow removeCallbacks reqInputSuggestionRunnable result=" + rsr);
        DtLog.d(TAG, "onDetachedFromWindow removeCallbacks updateInputDefaultTextRunnable result=" + rir);
    }

    private String getEditTextString(EditText editText) {
        if (editText == null) {
            return "";
        }

        return editText.getText().toString().trim().replaceAll("\n", "");
    }

    private void initToolsItem() {
        mToolsLayout = (SettingToolsLayout) findViewById(R.id.settingToolsLayout);
        final int PAGE_COUNT = 1;
        final int COUNT_PER_LINE = 4;

        final ArrayList<ToolsItem> firstPagerItems = new ArrayList<>(8);
        // page1 line1
        firstPagerItems.add(IntelligentAnswerToolsItem.createIntelligentAnswerSchool());
        firstPagerItems.add(IntelligentAnswerToolsItem.createIntelligentDiagnosisItem());
        firstPagerItems.add(IntelligentAnswerToolsItem.createConditionSelection());
        firstPagerItems.add(IntelligentAnswerToolsItem.createBSSignal());

        // page1 line2
        firstPagerItems.add(IntelligentAnswerToolsItem.createCYQItem());
        firstPagerItems.add(IntelligentAnswerToolsItem.createSimilarKLineItem());
        firstPagerItems.add(IntelligentAnswerToolsItem.createSecHistoryItem());
        firstPagerItems.add(IntelligentAnswerToolsItem.createIntelligentAnswerHelp());

        final Iterator<ToolsItem> it = firstPagerItems.iterator();
        if (it.hasNext()) { // 第一个排除
            it.next();
        }
        // 去掉new
        while (it.hasNext()) {
            it.next().isNew = false;
        }

        final ArrayList<ToolsItem>[] items = new ArrayList[PAGE_COUNT];
        items[0] = firstPagerItems;

        // 设置间距
        final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mToolsLayout.getLayoutParams();
        params.topMargin = DeviceUtil.dip2px(getContext(), 8);
        params.bottomMargin = params.topMargin;
        mToolsLayout.setLayoutParams(params);

        mToolsLayout.init(PAGE_COUNT, COUNT_PER_LINE, items);
    }

    public void setWebView(final DtWebView webView) {
        this.mWebView = webView;
    }

    private void commit() {
        final String inputText = getEditTextString(mEditText);
        if (inputText.length() > 0) {
            JsProxy.entryWords(mWebView, inputText);
            mEditText.setText("");
            DeviceUtil.hideInputMethod(mEditText);
            DtLog.d(TAG, "entryWords input text=" + inputText);
        } else {
            if (inputDefaultItemList != null && inputDefaultItemList.size() > 0) {
                String hint = mEditText.getHint().toString();
                if (!TextUtils.isEmpty(hint)) {
                    JsProxy.entryWords(mWebView, hint);
                    mEditText.setText("");
                    DeviceUtil.hideInputMethod(mEditText);
                    DtLog.d(TAG, "entryWords hint text=" + inputText);
                }
            }
        }

        // 移除联想语
        removeInputSuggestion();

        switchInputState(INPUT_STATE_DEFAULT);

        // 更换提示语
        removeCallbacks(updateInputDefaultTextRunnable);
        post(updateInputDefaultTextRunnable);
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final int id = v.getId();
        if (id == R.id.inputCommitButton) {
            commit();
        } else if (id == R.id.switchInputButton) {
            if (mInputState != INPUT_STATE_VOICE) {
                switchInputState(INPUT_STATE_VOICE);
            } else {
                switchInputState(INPUT_STATE_TEXT);
            }
        } else if (id == R.id.input) {
            switchInputState(INPUT_STATE_TEXT);
        } else if (id == R.id.inputToolsButton) {
            if (mInputState != INPUT_STATE_TOOLS) {
                switchInputState(INPUT_STATE_TOOLS);
                StatisticsUtil.reportAction(StatisticsConst.INTELLIGENT_ANSWER_CLICK_TOOLS_MENU);
            } else {
                switchInputState(INPUT_STATE_TEXT);
            }

            final IRedDotManager redDotManager = (IRedDotManager) ComponentManager.getInstance()
                    .getManager(IRedDotManager.class.getName());
            if (redDotManager != null) {
                redDotManager.setInputToolsButtonState(false);
                mInputToolsButtonRedDot.setVisibility(redDotManager.getInputToolsButtonState() ? VISIBLE : INVISIBLE);
            }
        }
    }

    public void switchInputState(final int newState) {
        switch (newState) {
            case INPUT_STATE_TEXT:
                mSwitchInputButton.setImageResource(R.drawable.intelligent_answer_input_voice);
                mEditText.requestFocus();
                DeviceUtil.showInputMethod(mEditText);
                mVoiceInputLayout.setVisibility(GONE);
                mToolsLayout.setVisibility(GONE);
                break;
            case INPUT_STATE_VOICE:
                mSwitchInputButton.setImageResource(R.drawable.intelligent_answer_input_method);
                // 修改第一次进入的时候，先进入语音输入，然后马上点击输入框弹出输入法，点击输入框没有触发的问题
                mEditText.requestFocus();
                DeviceUtil.hideInputMethod(mEditText);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVoiceInputLayout.setVisibility(VISIBLE);
                        mToolsLayout.setVisibility(GONE);
                    }
                }, 100L);
                break;
            case INPUT_STATE_TOOLS:
                mSwitchInputButton.setImageResource(R.drawable.intelligent_answer_input_voice);
                // 修改第一次进入的时候，先进入语音输入，然后马上点击输入框弹出输入法，点击输入框没有触发的问题
                mEditText.requestFocus();
                DeviceUtil.hideInputMethod(mEditText);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVoiceInputLayout.setVisibility(GONE);
                        mToolsLayout.setVisibility(VISIBLE);
                    }
                }, 100L);
                break;
            case INPUT_STATE_DEFAULT:
                mSwitchInputButton.setImageResource(R.drawable.intelligent_answer_input_voice);
                mVoiceInputLayout.setVisibility(GONE);
                break;
            default:
                return;
        }
        mInputState = newState;
    }

    /**
     * 关闭输入法、语音输入框和工具栏
     */
    public void closeInputLayout() {
        DeviceUtil.hideInputMethod(mEditText);
        mVoiceInputLayout.setVisibility(GONE);
        mToolsLayout.setVisibility(GONE);
    }

    public void setInputText(final String text) {
        mEditText.setText(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        final String inputText = s.toString().trim().replaceAll("\n", "");
        if (inputText.length() > 0) {
            mInputCommitButton.setVisibility(VISIBLE);
            mToolsButton.setVisibility(GONE);
            mInputToolsButtonRedDot.setVisibility(INVISIBLE);

            mEditText.setSelection(getEditTextString(mEditText).length());

            // 延时1s请求联想语
            removeCallbacks(reqInputSuggestionRunnable);
            postDelayed(reqInputSuggestionRunnable, 500);
        } else {
            mInputCommitButton.setVisibility(GONE);
            mToolsButton.setVisibility(VISIBLE);

            final IRedDotManager redDotManager = (IRedDotManager) ComponentManager.getInstance()
                    .getManager(IRedDotManager.class.getName());
            if (redDotManager != null) {
                mInputToolsButtonRedDot.setVisibility(redDotManager.getInputToolsButtonState() ? VISIBLE : INVISIBLE);
            }

            // 移除联想语
            removeInputSuggestion();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setIatParams();
                final int code = mIat.startListening(this);
                DtLog.d(TAG, "startListening : " + code);
                break;
            case MotionEvent.ACTION_UP:
                mIat.stopListening();
                mLeftAnimView.setVisibility(INVISIBLE);
                mRightAnimView.setVisibility(INVISIBLE);
                if (mVoiceState == VOICE_STATE_ERROR) {
                    setVoiceState(VOICE_STATE_DEFAULT);
                } else {
                    setVoiceState(VOICE_STATE_RECOGNIZING);
                }

                DtLog.d(TAG, "stopListening");
                break;
            case MotionEvent.ACTION_CANCEL:
                mIat.cancel();
                mLeftAnimView.setVisibility(INVISIBLE);
                mRightAnimView.setVisibility(INVISIBLE);
                setVoiceState(VOICE_STATE_DEFAULT);
                DtLog.d(TAG, "cancel");
                break;
            default:
                break;
        }
        return true;
    }

    private void setIatParams() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "40000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
    }

    @Override
    public void onVolumeChanged(int volume, byte[] data) {
        DtLog.d(TAG, "onVolumeChanged volume : " + volume);
        mLeftAnimView.setVolumn(volume);
        mRightAnimView.setVolumn(volume);
    }

    @Override
    public void onBeginOfSpeech() {
        DtLog.d(TAG, "onBeginOfSpeech");
        post(new Runnable() {
            @Override
            public void run() {
                mLeftAnimView.reset();
                mLeftAnimView.setVisibility(VISIBLE);
                mRightAnimView.reset();
                mRightAnimView.setVisibility(VISIBLE);
                setVoiceState(VOICE_STATE_RECORDING);
            }
        });
    }

    @Override
    public void onEndOfSpeech() {
        DtLog.d(TAG, "onEndOfSpeech");
        post(new Runnable() {
            @Override
            public void run() {
                mLeftAnimView.setVisibility(INVISIBLE);
                mRightAnimView.setVisibility(INVISIBLE);
            }
        });
    }

    @Override
    public void onResult(RecognizerResult result, boolean islast) {
        DtLog.d(TAG, "onResult result : " + result);
        processResult(result);
        if (islast) {
            StringBuilder resultBuffer = new StringBuilder();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            DtLog.d(TAG, "onResult : " + resultBuffer);
            final String res = resultBuffer.toString();
            post(() -> {
                if (TextUtils.isEmpty(res)) {
                    CommonToast.showToast(R.string.voice_input_blank);
                } else {
                    mEditText.getText().append(res);
                }
                switchInputState(INPUT_STATE_VOICE);
                setVoiceState(VOICE_STATE_DEFAULT);
            });
        }
    }

    private void processResult(RecognizerResult results) {
        final String text = parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    @Override
    public void onError(final SpeechError error) {
        DtLog.d(TAG, "onError error : " + error);
        post(new Runnable() {
            @Override
            public void run() {
                switch (error.getErrorCode()) {
                    case 10118: // 排除没有任何输入
                        break;
                    case 20001: // 没网络
                        CommonToast.showToast(R.string.network_invalid_please_check);
                        break;
                    default:
                        CommonToast.showToast(R.string.voice_input_error_tips);
                        break;
                }
                mLeftAnimView.setVisibility(INVISIBLE);
                mRightAnimView.setVisibility(INVISIBLE);
                setVoiceState(VOICE_STATE_ERROR);
            }
        });
    }

    @Override
    public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        DtLog.d(TAG, "onEvent eventType : " + eventType);
    }

    private void setVoiceState(final int state) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setVoiceStateOnUI(state);
        } else {
            post(() -> setVoiceStateOnUI(state));
        }
    }

    private void setVoiceStateOnUI(final int state) {
        if (mVoiceState != state) {
            mVoiceState = state;
            onVoiceStateChange(state);
        }
    }

    private void onVoiceStateChange(final int state) {
        switch (state) {
            case VOICE_STATE_DEFAULT:
            case VOICE_STATE_ERROR:
                mVoiceStateTipsView.setText(R.string.voice_input_tips);
                break;
            case VOICE_STATE_RECORDING:
                mVoiceStateTipsView.setText(R.string.voice_input_recording);
                break;
            case VOICE_STATE_RECOGNIZING:
                mVoiceStateTipsView.setText(R.string.voice_input_recognizing);
                break;
            default:
                break;
        }
    }
}