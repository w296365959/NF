package com.sscf.investment.teacherYan.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dengtacj.component.router.BeaconJump;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.LoadingDialog;
import com.sscf.investment.sdk.SDKManager;
import com.sscf.investment.sdk.utils.DownloadUtils;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.ThreadUtils;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.teacherYan.manager.CountNumUtil;
import com.sscf.investment.web.sdk.JsProxy;
import com.sscf.investment.web.sdk.widget.DtWebView;
import com.sscf.investment.widget.recyclerview.CommonRecyclerViewAdapter;

import java.io.File;
import java.lang.ref.WeakReference;

import BEC.InformationSpiderNews;
import butterknife.BindView;

/**
 * Created by LEN on 2018/4/24.
 */

public class AnalysisMarketAdapter extends CommonRecyclerViewAdapter implements Handler.Callback{

    private Handler mHandler;

    private static final int MSG_START_DOWNLOAD_PDF = 1;
    private static final int DOWNLOAD_FILE_SUCCESS = 2;
    private static final int DOWNLOAD_FILE_FAIL = 3;
    private LoadingDialog mLoadingDialog;
    private Runnable mLoadingRunnable;

    public AnalysisMarketAdapter(Context context) {
        super(context);
        mLoadingDialog = new LoadingDialog(context);
        mHandler = new Handler(this);
    }

    @Override
    protected CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
        return new AnalysisMarketHolder(mInflater.inflate(R.layout.item_analysis_market, parent, false));
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        switch (what){
            case MSG_START_DOWNLOAD_PDF:
                showLoadingDialog();
                final String downloadUrl = (String) msg.obj;
                final File pdfFile = FileUtil.getPdfFileByUrl(downloadUrl);
                SDKManager.getInstance().getDefaultExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        DownloadUtils.httpDownload(downloadUrl, pdfFile, new DownloadUtils.HttpDownloadCallback() {
                            @Override
                            public void onProgressUpdated(String url, float progress) {
                            }

                            @Override
                            public void onDownloadComplete(String url, boolean success) {
                                mHandler.obtainMessage(DOWNLOAD_FILE_SUCCESS, url).sendToTarget();
                            }
                        });
                    }
                });
                break;
            case DOWNLOAD_FILE_SUCCESS:
                if (null != mContext) {
                    dismissLoadingDialog();
                    BeaconJump.showPdfViewer(mContext,
                            Uri.fromFile(FileUtil.getPdfFileByUrl((String) msg.obj)).toString());
                }
                break;
            case DOWNLOAD_FILE_FAIL:
                break;
        }
        return true;
    }

    public void showLoadingDialog() {
        if (mContext == null) {
            return;
        }

        if (ThreadUtils.isMainThread()) {
            showLoadingDialogOnUI();
        } else {
            Runnable runnable = mLoadingRunnable;
            if (runnable == null) {
                runnable = () -> showLoadingDialogOnUI();
                mLoadingRunnable = runnable;
            }
            mHandler.post(runnable);
        }
    }

    private void showLoadingDialogOnUI() {
        if (null == mContext) {
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        mHandler.removeCallbacks(mLoadingRunnable);
        if (null == mContext) {
            return;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    final class AnalysisMarketHolder extends CommonRecyclerViewAdapter.CommonViewHolder {

        @BindView(R.id.tvAnalaysisTime) TextView mTvAnalysisTime;
        @BindView(R.id.title) TextView mTvAnalysisTitle;
        @BindView(R.id.tvSummary) TextView mTvAnalysisContent;
        @BindView(R.id.tvAnalysisPdf) View mViewPdf;

        public AnalysisMarketHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object itemData) {
            super.bindData(itemData);
            final InformationSpiderNews item = (InformationSpiderNews) itemData;
            if (null != item) {
                mTvAnalysisTime.setText(TimeUtils.transForDate(item.getSInformationTime()));
                mTvAnalysisTitle.setText(item.getSTitle());
                mTvAnalysisContent.setText(item.getSAbstracts());
                mViewPdf.setVisibility(item.getItype() == 1 ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public void onItemClicked() {
            super.onItemClicked();
            if (!TextUtils.isEmpty(((InformationSpiderNews) mItemData).getSContent())) {
                mHandler.obtainMessage(MSG_START_DOWNLOAD_PDF,
                        ((InformationSpiderNews) mItemData).getSContent()).sendToTarget();
                CountNumUtil.readAnalysisMarket(((InformationSpiderNews) mItemData).getIID());
            }
        }
    }
}
