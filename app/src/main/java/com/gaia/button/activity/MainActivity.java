package com.gaia.button.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gaia.button.R;
import com.gaia.button.fargment.MainContorlFragment;
import com.gaia.button.fargment.MainDiscoveryFragment;
import com.gaia.button.fargment.MainProductFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int STATE_DISCOVERY = 0;
    private static final int STATE_CONTORL = 1;
    private static final int STATE_PRODUCT = 2;

    private ImageView mTabLeft, mTabCenter, mTabRight;
    private MainDiscoveryFragment mainDiscoveryFragment;
    private MainContorlFragment mainContorlFragment;
    private MainProductFragment mainProductFragment;

    // 底部按钮对应的fragment列表
    private SparseArray<Fragment> mBottomFragmentMap = new SparseArray<>();
    private Fragment mCurFragment;
    private ConstraintLayout mSearchLayout;
    private EditText mEditText;
    private ImageView mPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTabLeft = findViewById(R.id.iv_tab_left);
        mTabLeft.setOnClickListener(this);
        mTabCenter = findViewById(R.id.iv_tab_center);
        mTabCenter.setOnClickListener(this);
        mTabRight = findViewById(R.id.iv_tab_right);
        mTabRight.setOnClickListener(this);
        mTabLeft.setSelected(true);
        mSearchLayout = findViewById(R.id.layout_search);
        mEditText = findViewById(R.id.et_search);
        mPersonal = findViewById(R.id.iv_personal);
        mPersonal.setOnClickListener(this);
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
                    search(s.toString());
                }
            }
        });
        switchToDiscovery();
    }

    private void cleanAllSelect() {
        mTabLeft.setSelected(false);
        mTabCenter.setSelected(false);
        mTabRight.setSelected(false);
    }

    private void search(String key) {
        if(!TextUtils.isEmpty(key)) {
            if (mCurFragment == mainProductFragment) {
                mainProductFragment.searchKey(key);
            }else if(mCurFragment == mainDiscoveryFragment){
                mainDiscoveryFragment.searchKey(key);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tab_left:
                mSearchLayout.setVisibility(View.VISIBLE);
                cleanAllSelect();
                mTabLeft.setSelected(true);
                switchToDiscovery();
                break;
            case R.id.iv_tab_center:
                mSearchLayout.setVisibility(View.GONE);
                cleanAllSelect();
                mTabCenter.setSelected(true);
                switchToContorl();
                break;
            case R.id.iv_tab_right:
                mSearchLayout.setVisibility(View.VISIBLE);
                cleanAllSelect();
                mTabRight.setSelected(true);
                switchToProduct();
                break;
            case R.id.iv_personal:
                Intent intent = new Intent(this, PersonnalActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void switchToDiscovery() {
        if (mCurFragment != null && mCurFragment == mainDiscoveryFragment) {
            return;
        }
        if (mainDiscoveryFragment == null) {
            mainDiscoveryFragment = new MainDiscoveryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_container, mainDiscoveryFragment);
            transaction.commitAllowingStateLoss();
        }
        mCurFragment = mainDiscoveryFragment;
        mBottomFragmentMap.put(STATE_DISCOVERY, mCurFragment);
        changeTabState(STATE_DISCOVERY);
    }

    private void switchToContorl() {
        if (mCurFragment != null && mCurFragment == mainContorlFragment) {
            return;
        }
        if (mainContorlFragment == null) {
            mainContorlFragment = new MainContorlFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_container, mainContorlFragment);
            transaction.commitAllowingStateLoss();
        }
        mCurFragment = mainContorlFragment;
        mBottomFragmentMap.put(STATE_CONTORL, mCurFragment);
        changeTabState(STATE_CONTORL);
    }

    private void switchToProduct() {
        if (mCurFragment != null && mCurFragment == mainProductFragment) {
            return;
        }
        if (mainProductFragment == null) {
            mainProductFragment = new MainProductFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.layout_container, mainProductFragment);
            transaction.commitAllowingStateLoss();
        }
        mCurFragment = mainProductFragment;
        mBottomFragmentMap.put(STATE_PRODUCT, mCurFragment);
        changeTabState(STATE_PRODUCT);
    }

    private void changeTabState(int state) {
        int size = mBottomFragmentMap.size();
        for (int i = 0; i < size; i++) {
            int key = mBottomFragmentMap.keyAt(i);
            Fragment fragment = mBottomFragmentMap.valueAt(i);
            if (key == state) {
                if (fragment != null && fragment.isHidden()) {
                    getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
                }
            } else {
                if (fragment != null && !fragment.isHidden()) {
                    getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
                }
            }
        }
    }
}