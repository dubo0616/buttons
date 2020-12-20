package com.gaia.button.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.view.WordWrapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private EditText mEditText;
    private TextView mTvSearch;
    private List<String> strs = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mEditText = findViewById(R.id.et_search);
        mTvSearch = findViewById(R.id.iv_personal);
        wordWrapView=findViewById(R.id.wordWrapView);
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput(mEditText);
                if(!TextUtils.isEmpty(mEditText.getText().toString())){
                    addStr(mEditText.getText().toString());
                    saveStr();
                    Intent intent = new Intent();
                    intent.putExtra("key",mEditText.getText().toString());
                    setResult(RESULT_OK,intent);
                }
                finish();
            }
        });
        showSoftInput(mEditText);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mTvSearch.setText("搜索");
                }else{
                    mTvSearch.setText("取消");
                }
            }
        });
        String strp = PreferenceManager.getInstance().getSearchHistory(PreferenceManager.getInstance().getAccountInfo().getUserID());
        if(!TextUtils.isEmpty(strp)) {
           List<String> stringList = Arrays.asList(strp.split(","));
            //添加子view：TextView
            for (String str : stringList) {
                if(TextUtils.isEmpty(str) || "null".equals(str)){
                    continue;
                }
                addStr(str);
                final TextView tv = new TextView(SearchActivity.this);
                tv.setTextSize(14);//设置字体大小
                tv.setTextColor(Color.BLACK);
                tv.setText(str);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftInput(mEditText);
                        Intent intent = new Intent();
                        addStr(tv.getText().toString());
                        saveStr();
                        intent.putExtra("key", tv.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                tv.setPadding(15, 0, 15, 0);
                tv.setBackgroundResource(R.drawable.bg_search);//子view背景
                wordWrapView.addView(tv);
            }
        }
    }

    private void addStr(String key) {
        if (strs == null)
            return;
        if (strs.size() < 5) {
            strs.add(key);
        } else {
            strs.remove(0);
            strs.add(0,key);
        }
    }
    private void saveStr() {
        if (strs == null || strs.size() == 0)
            return;
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            if (i == strs.size() - 1) {
                sb.append(str);
            } else {
                sb.append(str).append(",");
            }
            i++;
        }
        PreferenceManager.getInstance().setSearchHistoryString(PreferenceManager.getInstance().getAccountInfo().getUserID(),sb.toString());

    }
    private WordWrapView wordWrapView;
    private void hideSoftInput(EditText searchView) {
        if(searchView ==null)
            return;
        InputMethodManager inputMethodManager =(InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);

    }

    private void showSoftInput(EditText et) {
        if(et==null)
            return;
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.RESULT_SHOWN);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

}
