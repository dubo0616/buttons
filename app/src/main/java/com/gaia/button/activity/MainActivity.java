package com.gaia.button.activity;

import androidx.annotation.Nullable;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gaia.button.R;
import com.gaia.button.fargment.MainContorlFragment;
import com.gaia.button.fargment.MainDiscoveryFragment;
import com.gaia.button.fargment.MainProductFragment;

import javax.xml.transform.Result;

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
    private TextView mEditText;
    private ImageView mPersonal;
    private int mTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTab = getIntent().getIntExtra("Tab",0);
        initView();
    }

    private void initView() {
        mTabLeft = findViewById(R.id.iv_tab_left);
        mTabLeft.setOnClickListener(this);
        mTabCenter = findViewById(R.id.iv_tab_center);
        mTabCenter.setOnClickListener(this);
        mTabRight = findViewById(R.id.iv_tab_right);
        mTabRight.setOnClickListener(this);
        mSearchLayout = findViewById(R.id.layout_search);
        mEditText = findViewById(R.id.et_search);
        mPersonal = findViewById(R.id.iv_personal);
        mPersonal.setOnClickListener(this);
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.v("TAG", "TextView: ACTION_DOWN" + MotionEvent.ACTION_DOWN);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(mCurFragment == mainDiscoveryFragment) {
                            startActivityForResult(new Intent(MainActivity.this, SearchActivity.class), 1000);
                        }else{
                            startActivityForResult(new Intent(MainActivity.this, SearchActivity.class), 1001);

                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        switch (mTab){
            case 0:
                switchToDiscovery();
                mSearchLayout.setVisibility(View.VISIBLE);
                mTabLeft.setSelected(true);
                break;
            case 1:
                switchToContorl();
                mSearchLayout.setVisibility(View.GONE);
                mTabCenter.setSelected(true);
                break;
            case 2:
                switchToProduct();
                mSearchLayout.setVisibility(View.VISIBLE);
                mTabRight.setSelected(true);
                break;
        }

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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private long exitTime = 0;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1000:
            if(resultCode == RESULT_OK && mCurFragment == mainDiscoveryFragment){
                if(data != null){
                    String key = data.getStringExtra("key");
                    mainDiscoveryFragment.searchKey(key);
                }

            }
            break;
            case 1001:
                if(resultCode == RESULT_OK && mCurFragment == mainProductFragment){
                    if(data != null){
                        String key = data.getStringExtra("key");
                        mainProductFragment.searchKey(key);
                    }

                }
                break;
        }

    }
}