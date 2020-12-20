package com.gaia.button.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;

public class SpalshActivity extends BaseActivity {
    private TextView mTvStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        initView();
    }
    private void startLoginMian() {
        Intent intent = new Intent(SpalshActivity.this, LoginMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startPer() {
        if (!PreferenceManager.getInstance().getFristInstall()) {
        }else{
            startLoginMian();
        }
    }

    private void initView() {
        mTvStart = findViewById(R.id.tv_start);
        mTvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginMian();
            }
        });
        startPer();
    }
}
