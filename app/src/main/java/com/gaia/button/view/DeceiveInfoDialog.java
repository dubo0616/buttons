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

public class DeceiveInfoDialog extends Dialog {
    private TextView mTvName,mTvArddess,mTvVersion;
    public DeceiveInfoDialog(@NonNull Context context) {
        this(context,0);
    }

    public DeceiveInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static DeceiveInfoDialog getInstance(Context mContext) {
        DeceiveInfoDialog dialog = new DeceiveInfoDialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device_info);
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
    private void initView(){
        mTvName = findViewById(R.id.tv_name);
        mTvArddess = findViewById(R.id.tv_address);
        mTvVersion = findViewById(R.id.tv_version);
        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void setData(String name,String adress,String version){
        mTvName.setText("耳机名称："+name);
        mTvArddess.setText("mac地址："+adress);
        mTvVersion.setText("系统版本："+version);
    }
}
