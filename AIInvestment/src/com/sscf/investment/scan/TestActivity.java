package com.sscf.investment.scan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.FileUtil;

import java.io.File;

/**
 * Created by yorkeehuang on 2017/5/22.
 */

public class TestActivity extends Activity implements View.OnClickListener {

    private static final String TAG = TestActivity.class.getSimpleName();
    private static final int REQUEST_CODE_GALLERY = 100;
    private static final int REQUEST_CODE_CAMERA = 101;

    private static final String TEMP_CAMERA_PIC_NAME = "camera_pic.tmp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.test_gallery_button).setOnClickListener(this);
        findViewById(R.id.test_camera_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_gallery_button:
                getImageFromGallery();
                break;
            case R.id.test_camera_button:
                getImageFromCamera();
                break;
            default:
        }
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    private void getImageFromCamera() {
        String status= Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                Uri uri = getImageFileUri();
                if(uri != null) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        boolean handled = false;
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:
                    handled = handleGalleryPic(data);
                    break;
                case REQUEST_CODE_CAMERA:
                    handled = handleCameraPic();
                    break;
                default:
            }
        }

        if(!handled) {
            DengtaApplication.getApplication().showToast("请重新选择图片");
        }
    }

    private boolean handleGalleryPic(Intent data) {
        if(data != null) {
            Uri uri = data.getData();
            Log.d(TAG, "uri = " + uri);
            if(uri != null) {
                Intent intent = new Intent(this, OcrResultActivity.class);
                intent.putExtra(OcrResultActivity.EXTRA_IMAGE_URI, uri);
                startActivity(intent);
                return true;
            }
        }
        return false;
    }

    private Uri getImageFileUri() {
        return Uri.fromFile(new File(FileUtil.getScanFileDir(), TEMP_CAMERA_PIC_NAME));
    }

    private boolean handleCameraPic() {
        Uri uri = getImageFileUri();
        Log.d(TAG, "uri = " + uri);
        if(uri != null) {
            Intent intent = new Intent(this, OcrResultActivity.class);
            intent.putExtra(OcrResultActivity.EXTRA_IMAGE_URI, uri);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
