package com.sscf.investment.pdf;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.chenenyu.router.annotation.Route;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

/**
 * Created by davidwei on 2017-08-10.
 */
@Route("PdfViewerActivity")
public final class PdfViewerActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String uriString = getIntent().getStringExtra("extra_data");

        if (TextUtils.isEmpty(uriString)) {
            finish();
            return;
        }

        final Uri uri = Uri.parse(uriString);
        final PDFView pdfView = new PDFView(this, null);
        setContentView(pdfView);
        pdfView.fromUri(uri)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(0)
                .load();
    }
}
