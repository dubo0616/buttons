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

import com.gaia.button.R;

import androidx.annotation.NonNull;

public class ShareDialog extends Dialog {
    private TextView tv_wechat, tv_wechats;
    private ShareDialogListener mShareDialogListener;
    public ShareDialog(@NonNull Context context,ShareDialogListener l) {
        this(context, 0);
        mShareDialogListener = l;
    }

    public ShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public interface ShareDialogListener{
        void onItemClick(int type);
    }
    public static ShareDialog getInstance(Context mContext,ShareDialogListener l) {
        ShareDialog dialog = new ShareDialog(mContext,l);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_info);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams layoutParams = null;
        if (dialogWindow != null) {
            layoutParams = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.BOTTOM);
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
        tv_wechat = findViewById(R.id.tv_wechat);
        tv_wechats = findViewById(R.id.tv_wechats);
        tv_wechats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mShareDialogListener != null){
                    mShareDialogListener.onItemClick(1);
                }
                dismiss();

            }
        });
        tv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mShareDialogListener != null){
                    mShareDialogListener.onItemClick(0);
                }
                dismiss();

            }
        });
        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setData(String name, String adress, String version) {
//        mTvName.setText(name);
//        mTvArddess.setText(adress);
//        mTvVersion.setText(version);
    }
}
