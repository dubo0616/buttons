package com.gaia.button.activity;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.fargment.MainContorlFragment;
import com.gaia.button.fargment.MainDiscoveryFragment;
import com.gaia.button.fargment.MainProductFragment;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.view.PlayMoveLayout;

import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_GetUserInfo;
import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_WechatLogin;

public class MainActivity extends BaseActivity implements View.OnClickListener , PlayMoveLayout.MainContorlListener, IUserListener {

    public static final String FORM_KEY = "form_key";
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
    public final static String ACTION_PLAY_PAUSE = "play_pause";
    public final static String ACTION_PLAY_PRE = "play_pre";
    public final static String ACTION_PLAY_NEXT= "play_next";
    public final static String ACTION_PLAY_LARGE= "play_large";
    public final static String ACTION_PLAY_SMALLL= "play_small";
    private BroadcastReceiver mReceiverPlayCOntorl;
    private PlayMoveLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTab = getIntent().getIntExtra("Tab",0);
        initView();
        registerReceiver();
        initData();

    }
    private void initData(){
        UserManager.getRequestHandler().requestGetUserInfo(this, PreferenceManager.getInstance().getAccountInfo().getToken());

    }

    private void registerReceiver() {
        if (mReceiverPlayCOntorl == null) {
            mReceiverPlayCOntorl = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(ACTION_PLAY_PAUSE)) {
                    }
                }
            };
            final IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_PLAY_PAUSE);
            filter.addAction(ACTION_PLAY_PRE);
            filter.addAction(ACTION_PLAY_NEXT);
            filter.addAction(ACTION_PLAY_LARGE);
            filter.addAction(ACTION_PLAY_SMALLL);
            registerReceiver(mReceiverPlayCOntorl, filter);
        }
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
        if(PreferenceManager.getInstance().getAccountInfo() != null && !TextUtils.isEmpty(PreferenceManager.getInstance().getAccountInfo().getAvtorURL())){
            Glide.with(this).load(PreferenceManager.getInstance().getAccountInfo().getAvtorURL()).
                    apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.icon_personal)).into(mPersonal);
        }
        mPersonal.setOnClickListener(this);
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.v("TAG", "TextView: ACTION_DOWN" + MotionEvent.ACTION_DOWN);
                        break;
                    case MotionEvent.ACTION_UP:

                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        if(mCurFragment == mainDiscoveryFragment) {
                            intent.putExtra("type",0);
                        }else{
                            intent.putExtra("type",1);
                        }

                        startActivity(intent);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tab_left:
                switchToDiscovery();
                break;
            case R.id.iv_tab_center:
                switchToContorl();
                break;
            case R.id.iv_tab_right:
                switchToProduct();
                break;
            case R.id.iv_personal:
                Intent intent = new Intent(this, PersonnalActivity.class);
                if(!TextUtils.isEmpty(mainContorlFragment.getVersion())){
                    intent.putExtra("version",mainContorlFragment.getVersion());
                }
                if(mainContorlFragment.getConnectDevice() != null){
                    intent.putExtra("name",mainContorlFragment.getConnectDevice().getName());
                    intent.putExtra("mac",mainContorlFragment.getConnectDevice().getAddress());
                }
                startActivityForResult(intent,1000);

                break;
        }
    }

    private void switchToDiscovery() {
        cleanAllSelect();
        mTabLeft.setSelected(true);
        mSearchLayout.setVisibility(View.VISIBLE);
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
        cleanAllSelect();
        mTabCenter.setSelected(true);
        mSearchLayout.setVisibility(View.GONE);
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
        cleanAllSelect();
        mTabRight.setSelected(true);
        mSearchLayout.setVisibility(View.VISIBLE);
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
    protected void onDestroy() {
        if (layout != null) {
            layout.destory();
        }
        super.onDestroy();
        unregisterReceiver(mReceiverPlayCOntorl);

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
                if (resultCode == RESULT_OK && mCurFragment != mainContorlFragment) {
                    switchToContorl();
                    mainContorlFragment.onActivityResult(requestCode, resultCode, data);
                }
            break;
            case 1111:
                if(Build.VERSION.SDK_INT >= 23){
                    if(Settings.canDrawOverlays(MainActivity.this)){
                        initPlayLayout(true);
                    }else{
                        initPlayLayout(false);
                    }
                }


                break;
        }

    }
    private void initPlayLayout(boolean pers){
        if (layout == null) {
            hasAllow = true;
            layout = new PlayMoveLayout(this);
            layout.setListener(this);
        }
        layout.setAllow(pers);
        layout.show();
    }
    public void setPlayContorlLay(boolean show){
        if(show) {
            requestOverlayPermission();
        }else{
            if (layout != null) {
                layout.destory();
            }
        }
    }
    public void setPlayContorl(boolean show){
        if(layout != null) {
            layout.setPause(show);
        }
    }

    @Override
    public boolean sendControlCommand(int comm) {
        return mainContorlFragment.sendControlCommand(comm);
    }
    private boolean hasAllow = false;
    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent, 1111);
            } else {
                initPlayLayout(true);
            }
        }else{
            initPlayLayout(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(hasAllow){
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    initPlayLayout(false);
                } else {
                    initPlayLayout(true);
                }
            }else{
                initPlayLayout(true);
            }
        }
        if(PreferenceManager.getInstance().getAccountInfo() != null && !TextUtils.isEmpty(PreferenceManager.getInstance().getAccountInfo().getAvtorURL())){
            Glide.with(this).load(PreferenceManager.getInstance().getAccountInfo().getAvtorURL()).
                    apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.icon_personal)).into(mPersonal);
        }
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if(requestTag == Net_Tag_User_GetUserInfo) {
            if (data != null && data instanceof AccountInfo) {
                AccountInfo info = (AccountInfo) data;
                Log.e("HHHXXXXH","================"+info.toString());
                PreferenceManager.getInstance().save(info);
            }
        }
    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {

    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}