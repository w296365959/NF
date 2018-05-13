package com.sscf.investment.component.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import com.sscf.investment.sdk.utils.TimeUtils;
import com.sscf.investment.component.ui.R;
import java.io.File;

/**
 * Created by davidwei on 2015-08-07.
 *
 */
public final class PicPickerDialog extends Dialog implements View.OnClickListener, DialogInterface.OnCancelListener {

    private static final int REQUEST_PHOTOGRAPH = 1;// 拍照
    private static final int REQUEST_ALBUM = 2;// 从相册中选择
    private static final int REQUEST_CROP = 3;// 剪切
    private static final int REQUEST_VIDEO = 4;// 录像

    public static final String IMAGE_TYPE = "jpg";

    private final Activity mActivity;

    private final PickerCallback mCallback;

    private final Uri mPhotoUri;
    private final Uri mVideoUri;
    private final Uri mAvatarUri;

    private boolean mCrop = true;

    public static int PICKER_MODE_ALBUM = 1;            // 相册
    public static int PICKER_MODE_CAPTURE_IMAGE = 2;    // 拍照
    public static int PICKER_MODE_CAPTURE_VIDEO = 3;    // 录像

    public PicPickerDialog(final Activity activity, final PickerCallback callback, final int type) {
        super(activity, R.style.dialog_bottom_theme);
        setContentView(R.layout.dialog_picker_dialog);
        setCanceledOnTouchOutside(true);

        final View contentView = findViewById(R.id.dialogContentView);

        // TODO 看有没方式可以统一的
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final RectF frame = new RectF(
                            contentView.getX(),
                            contentView.getY(),
                            contentView.getX() + contentView.getWidth(),
                            contentView.getY() + contentView.getHeight());
                    if (!frame.contains(event.getX(), event.getY())) {
                        cancel();
                    }
                }
                return false;
            }
        });

        if (type == PICKER_MODE_ALBUM) {
            findViewById(R.id.modifyAccountPhotograph).setOnClickListener(this);
            findViewById(R.id.modifyAccountVideo).setVisibility(View.GONE);
            findViewById(R.id.modifyAccountAlbumPicker).setOnClickListener(this);
            findViewById(R.id.modifyAccountCancel).setOnClickListener(this);
        } else if (type == PICKER_MODE_CAPTURE_IMAGE) {
            findViewById(R.id.modifyAccountPhotograph).setOnClickListener(this);
            findViewById(R.id.modifyAccountVideo).setVisibility(View.GONE);
            findViewById(R.id.modifyAccountAlbumPicker).setVisibility(View.GONE);
            findViewById(R.id.modifyAccountCancel).setOnClickListener(this);
        } else if (type == PICKER_MODE_CAPTURE_VIDEO) {
            findViewById(R.id.modifyAccountPhotograph).setVisibility(View.GONE);
            findViewById(R.id.modifyAccountVideo).setOnClickListener(this);
            findViewById(R.id.modifyAccountAlbumPicker).setVisibility(View.GONE);
            findViewById(R.id.modifyAccountCancel).setOnClickListener(this);
        }

        this.mActivity = activity;
        mAvatarUri = Uri.fromFile(getAvatarFile());
        mVideoUri = Uri.fromFile(getVideoFile());
        mPhotoUri = Uri.fromFile(getPhotoFile());
        mCallback = callback;

        setOnCancelListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.modifyAccountVideo) {
            getVideoFromPhotograph();
            dismiss();
        } else if (id == R.id.modifyAccountPhotograph) {
            getPicFromPhotograph();
            dismiss();
        } else if (id == R.id.modifyAccountAlbumPicker) {
            getPicFromAlbum();
            dismiss();
        } else if (id == R.id.modifyAccountCancel) {
            dismiss();
            if (mCallback != null) {
                mCallback.onGetPicCancel();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            if (mCallback != null) {
                mCallback.onGetPicCancel();
            }
            return;
        }

        switch (requestCode) {
            case REQUEST_PHOTOGRAPH:
                if (mCrop) {
                    cropPic(mPhotoUri);
                } else {
                    if (mCallback != null) {
                        mCallback.onGetPicSuccess(mPhotoUri);
                    }
                }
                break;
            case REQUEST_VIDEO:
                if (mCallback != null) {
                    mCallback.onGetPicSuccess(mVideoUri);
                }
                break;
            case REQUEST_ALBUM:
                if (mCrop) {
                    cropPic(data.getData());
                } else {
                    if (mCallback != null) {
                        mCallback.onGetPicSuccess(data.getData());
                    }
                }
                break;
            case REQUEST_CROP:
                if (mCallback != null) {
                    Uri uri = data.getData();
                    uri = uri == null ? mAvatarUri : uri;
                    mCallback.onGetPicSuccess(uri);
                }
                break;
            default:
                break;
        }
    }

    public void setCrop(final boolean crop) {
        mCrop = crop;
    }

    /**
     * 调用系统拍照接口
     */
    private void getPicFromPhotograph() {
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        try {
            mActivity.startActivityForResult(cameraIntent, REQUEST_PHOTOGRAPH);
        } catch (Exception e) {
            e.printStackTrace();
            CommonToast.showToast(R.string.scan_camera_authority_error);
        }
    }

    /**
     * 调用系统拍照接口
     */
    private void getVideoFromPhotograph() {
        final Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoUri);
        try {
            mActivity.startActivityForResult(cameraIntent, REQUEST_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
            CommonToast.showToast(R.string.scan_camera_authority_error);
        }
    }

    private File getPhotoFile() {
        final File picDir = mActivity.getExternalFilesDir("pic");
        return new File(picDir, "photo_" + TimeUtils.getCurrentTimeString() + ".jpg");
    }

    private File getVideoFile() {
        final File picDir = mActivity.getExternalFilesDir("pic");
        return new File(picDir, "video_" + TimeUtils.getCurrentTimeString() + ".mp4");
    }

    private void cropPic(final Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mAvatarUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        try {
            mActivity.startActivityForResult(intent, REQUEST_CROP);
        } catch (Exception e) { // 不能切图
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.onGetPicError();
            }
        }
    }

    private File getAvatarFile() {
        final File picDir = mActivity.getExternalFilesDir("pic");
        return new File(picDir, "avatar.jpg");
    }

    private void getPicFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        mActivity.startActivityForResult(intent, REQUEST_ALBUM);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mCallback != null) {
            mCallback.onGetPicCancel();
        }
    }

    public interface PickerCallback {
        void onGetPicSuccess(final Uri uri);
        void onGetPicCancel();
        void onGetPicError();
    }
}
