package com.gaia.button.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gaia.button.R;

public class LoginBtn extends ConstraintLayout {
    private View mRootView;
    private ImageView mLoginIcon;
    private TextView mLoginText;
    public LoginBtn(@NonNull Context context) {
        this(context,null);
    }

    public LoginBtn(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginBtn(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        mRootView = LayoutInflater.from(context).inflate(R.layout.layout_login_btn,this);
        mLoginIcon = findViewById(R.id.iv_icon);
        mLoginText = findViewById(R.id.tv_text);
    }
    public void setBtnLoginIcon(int res){
        if(null != mLoginIcon){
            mLoginIcon.setImageResource(res);
        }
    }
    public void setBtnLoginTextStyle(int textcolor,int text){
        if(null != mLoginText){
            mLoginText.setText(text);
            mLoginText.setTextColor(textcolor);
        }
    }
    public void setBtnLoginBg(int bgcolor){
        if(null != mRootView){
            mRootView.setBackgroundResource(bgcolor);
        }
    }
}
