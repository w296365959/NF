package com.sscf.investment.scan;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.chenenyu.router.annotation.Route;
import com.sscf.investment.component.scan.R;
import com.sscf.investment.scan.camera.CameraManager;
import com.sscf.investment.scan.camera.Util;
import com.sscf.investment.scan.decoding.CaptureActivityHandler;
import com.sscf.investment.scan.decoding.InactivityTimer;
import com.sscf.investment.scan.view.ViewfinderView;
import com.sscf.investment.sdk.utils.FileUtil;
import com.sscf.investment.sdk.utils.StatisticsConst;
import com.sscf.investment.sdk.utils.StatisticsUtil;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.dengtacj.component.router.WebBeaconJump;
import com.sscf.investment.component.ui.widget.BaseActivity;
import com.sscf.investment.component.ui.widget.CommonDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

@Route("CaptureActivity")
public final class CaptureActivity extends BaseActivity implements Callback, View.OnClickListener,
        DialogInterface.OnCancelListener, Camera.PictureCallback {
    private static final String TAG = CaptureActivity.class.getSimpleName();
    private static final int REQUEST_CODE_GALLERY = 100;

    private int mMode;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceHolder surfaceHolder;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private boolean vibrate;
    private static final long VIBRATE_DURATION = 200L;

    private View mTakePictureButton;
    private View mGallaryButton;

    private CommonDialog mCameraErrorDialog;

    private OrientationEventListener mOrientationEventListener;

    private int mOrientation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_setting_scan);
        // 初始化 CameraManager
        CameraManager.init(getApplication());

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        mOrientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation != ORIENTATION_UNKNOWN) {
                    mOrientation = Util.roundOrientation(orientation, mOrientation);
                }
            }
        };

        initView();
        StatisticsUtil.reportAction(StatisticsConst.SCAN_DISPLAY);
    }

    private void initView() {
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        surfaceHolder = surfaceView.getHolder();

        findViewById(R.id.actionbar_back_button).setOnClickListener(this);
        View faqView = findViewById(R.id.actionbar_faq);

        faqView.setVisibility(View.VISIBLE);
        faqView.setOnClickListener(this);

        mTakePictureButton = findViewById(R.id.takePictureButton);
        mTakePictureButton.setOnClickListener(this);
        mGallaryButton = findViewById(R.id.gallaryButton);
        mGallaryButton.setOnClickListener(this);

        findViewById(R.id.gallaryButton).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mOrientationEventListener != null) {
            mOrientationEventListener.enable();
        }

        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }

        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:
                    handleGalleryPic(data);
                    break;
                default:
            }
        }
    }

    private boolean handleGalleryPic(Intent data) {
        if(data != null) {
            Uri uri = data.getData();
            Log.d(TAG, "uri = " + uri);
            if(uri != null) {
                setPictureFinishResultOk(uri);
                return true;
            }
        }
        return false;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
            }
        } catch (IOException ioe) {
            showCameraErrorDialog();
            return;
        } catch (RuntimeException e) {
            showCameraErrorDialog();
            return;
        }
    }

    private void showCameraErrorDialog() {
        if (isDestroy()) {
            return;
        }
        if (mCameraErrorDialog == null) {
            final CommonDialog dialog = new CommonDialog(this);
            dialog.setMessage(getString(R.string.scan_camera_authority_error));
            dialog.addButton(R.string.ok);
            mCameraErrorDialog = dialog;
        }
        mCameraErrorDialog.show();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public void handleDecode(final Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        vibrate();
        setScanFinishResultOk(obj.getText());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (handler != null) {
            handler.sendEmptyMessage(R.id.restart_preview);
        }
    }

    private void vibrate() {
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }

        final int id = v.getId();
        if (id == R.id.actionbar_back_button) {
            setFinishResultCancel();
        } else if (id == R.id.actionbar_faq) {
            WebBeaconJump.showCaptureFaqActivity(this);
        } else if (id == R.id.takePictureButton) {
            if (handler != null) {
                handler.removeMessages(R.id.auto_focus);
            }
            CameraManager.get().takePicture(this, mOrientation);
        } else if (id == R.id.gallaryButton) {
            getImageFromGallery();
        }
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        try {
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        final File file = new File(FileUtil.getExternalFilesDir(this, "pic"), "scan.jpg");
        FileUtil.saveByteArrayToFile(data, file);
        setPictureFinishResultOk(Uri.fromFile(file));
    }

    private void setPictureFinishResultOk(final Uri uri) {
        final Intent intent = new Intent();
        intent.setData(uri);
        setFinishResultOk(intent);
    }

    public void setScanFinishResultOk(final String res) {
        final Intent intent = new Intent();
        intent.putExtra("res", res);
        setFinishResultOk(intent);
    }

    private void setFinishResultOk(final Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setFinishResultCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
