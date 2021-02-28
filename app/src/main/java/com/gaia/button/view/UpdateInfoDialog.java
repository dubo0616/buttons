package com.gaia.button.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gaia.button.R;

public class UpdateInfoDialog extends Dialog {
    private TextView mTvtitle, mTvContent, mTvSubContent;
    private OnConfirmClickListener mOnConfirmClickListener;

    public interface OnConfirmClickListener {
        void onConfirm();
    }

    public UpdateInfoDialog(@NonNull Context context, OnConfirmClickListener l) {
        this(context, 0);
        mOnConfirmClickListener = l;
    }

    public UpdateInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static UpdateInfoDialog getInstance(Context mContext, OnConfirmClickListener listener) {
        UpdateInfoDialog dialog = new UpdateInfoDialog(mContext, listener);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_info);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams layoutParams = null;
        if (dialogWindow != null) {
            layoutParams = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            layoutParams.x = 0;
            layoutParams.y = 0;
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(layoutParams);
        }
        initView();
    }

    private void initView() {
        mTvtitle = findViewById(R.id.tv_title);
        mTvContent = findViewById(R.id.tv_content);
        mTvSubContent = findViewById(R.id.tv_sub_content);
        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mOnConfirmClickListener != null){
                    mOnConfirmClickListener.onConfirm();;
                }
            }
        });
        findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mOnConfirmClickListener != null){
                    mOnConfirmClickListener.onConfirm();;
                }
            }
        });
    }

    public void setData(String title, String content, String subcontent) {
        mTvtitle.setText(title);
        mTvContent.setText(content);
        mTvSubContent.setText(subcontent);
    }
}
