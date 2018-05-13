package com.sscf.investment.portfolio;

/**
 * Created by xuebinliu on 11/11/15.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.sscf.investment.R;

import java.util.ArrayList;

/**
 * 删除自选对话框
 */
public final class DelPortDialog extends Dialog {

    private DelDialogCallback mOwner;
    private ArrayList<String> mDelCodeList;

    public DelPortDialog(Context context) {
        super(context, R.style.dialog_center_theme);
        setContentView(R.layout.confirm_del_dialog_layout);
    }

    /**
     * 弹出删除自选对话框
     */
    public void showDelDialog(DelDialogCallback owner, String content, ArrayList<String> delCodeList) {
        mOwner = owner;
        mDelCodeList = delCodeList;

        final View contentView = findViewById(R.id.dialogContentView);
        setCanceledOnTouchOutside(true);
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

        final TextView messageView = (TextView) findViewById(R.id.confirm_del_text_content);
        messageView.setText(content);

        findViewById(R.id.upgrade_dialog_cancle_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwner.callback(false, null);
                dismiss();
            }
        });

        findViewById(R.id.upgrade_dialog_ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwner.callback(true, mDelCodeList);
                dismiss();
            }
        });

        show();
    }

    public interface DelDialogCallback {
        /**
         * 用户点击响应
         * @param isConfirm true删除
         */
        void callback(boolean isConfirm, ArrayList<String> delArray);
    }
}
