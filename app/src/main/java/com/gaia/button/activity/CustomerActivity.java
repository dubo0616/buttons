package com.gaia.button.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gaia.button.R;

public class CustomerActivity extends BaseActivity  {

    private ImageView mBack;
    private String[] mPermissions = {Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        mBack = findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.cl_auto_paly).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions(mPermissions);
//                requestPermissions(mPermissions,true);
            }
        });
        findViewById(R.id.cl_auto_paly_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, "support@buttonsus.com");
                intent.setType("text/plain");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + "4006008802");
            intent.setData(data);
            startActivity(intent);
        }else{
            showTotast("没有拨打电话权限");
        }

    }
}
