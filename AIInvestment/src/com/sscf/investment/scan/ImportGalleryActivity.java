package com.sscf.investment.scan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.chenenyu.router.annotation.Route;
import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.managers.IScanManager;
import com.sscf.investment.R;
import com.sscf.investment.component.ui.widget.BaseFragmentActivity;

/**
 * Created by yorkeehuang on 2017/7/19.
 */
@Route("ImportGalleryActivity")
public class ImportGalleryActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final String TAG = ImportGalleryActivity.class.getSimpleName();

    private static final int REQUEST_CODE_GALLERY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_gallery);

        ((TextView)findViewById(R.id.actionbar_title)).setText(R.string.import_gallery_text);
        findViewById(R.id.actionbar_back_button).setOnClickListener(this);

        findViewById(R.id.import_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_button:
                finish();
                break;
            case R.id.import_button:
                getImageFromGallery();
                break;
            default:
        }
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
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

    private void setPictureFinishResultOk(final Uri uri) {
        final IScanManager scanManager = (IScanManager) ComponentManager.getInstance()
                .getManager(IScanManager.class.getName());
        if (scanManager != null) {
            final Intent intent = new Intent();
            intent.setData(uri);
            scanManager.handleResult(this, intent);
        }
    }
}
