package com.gaia.button.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.fargment.PersonalCollectFragment;
import com.gaia.button.fargment.PersonalDeviceFragment;
import com.gaia.button.fargment.PersonalSettingFragment;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.net.NetConfig;
import com.gaia.button.net.user.IUploadCallback;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.utils.FileUtils;
import com.gaia.button.utils.ImageUploaderManagerAliyun;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonnalActivity extends BaseActivity implements IUploadCallback<String> ,IUserListener{

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList();
    private ImageView mBackView;
    private ImageView mHead,mIvSigin;
    private TextView mTvSigin,mTvName;
    private String mVersion,mName,mMac;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mVersion = getIntent().getStringExtra("version");
        mName =  getIntent().getStringExtra("name");
        mMac =  getIntent().getStringExtra("mac");
        initData();
    }
    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, 2000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 2000:
                    Uri uri = data.getData();
                    String filePath = FileUtils.getFilePathByUri(this, uri);
                    if (!TextUtils.isEmpty(filePath)) {
                        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).bitmapTransform(new CircleCrop());
                        //将照片显示在 ivImage上
                        Glide.with(this).load(Uri.fromFile(new File(filePath))).apply(requestOptions1).into(mHead);
                    }
                    ImageUploaderManagerAliyun.getInstance().startGetUploadToken(filePath,this);
                    break;
                case 10000:
                    mTvSigin.setText(PreferenceManager.getInstance().getAccountInfo().getPerson_sign());
                    break;
                case 10001:
                    mTvName.setText(PreferenceManager.getInstance().getAccountInfo().getPerson_name());
                    break;
            }
        }
    }
    private void initView(){
        mBackView = findViewById(R.id.iv_back);
        mHead = findViewById(R.id.iv_head);
        mHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
        mTvName = findViewById(R.id.tv_name);
        mTvSigin = findViewById(R.id.tv_sgin);
        mIvSigin = findViewById(R.id.iv_sigin_mody);
        mIvSigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonnalActivity.this,SetNameSginActivity.class);
                intent.putExtra(SetNameSginActivity.TYPE,0);
                startActivityForResult(intent,10000);
            }
        });

        mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonnalActivity.this,SetNameSginActivity.class);
                intent.putExtra(SetNameSginActivity.TYPE,1);
                startActivityForResult(intent,10001);
            }
        });

        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TabLayout tab_layout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        PersonalPagerAdapter fragmentAdater = new  PersonalPagerAdapter(getSupportFragmentManager(),this,mFragmentList,mTitleList);
        viewPager.setAdapter(fragmentAdater);
        tab_layout.setupWithViewPager(viewPager);
        AccountInfo accountInfo = PreferenceManager.getInstance().getAccountInfo();
        if(accountInfo != null){
            if(!TextUtils.isEmpty(accountInfo.getAvtorURL())){
                Glide.with(this).load(accountInfo.getAvtorURL()).apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.icon_head)).into(mHead);
            }
            if(!TextUtils.isEmpty(accountInfo.isPerson_sign())){
                mTvSigin.setText(accountInfo.isPerson_sign());
            }
            if(!TextUtils.isEmpty(accountInfo.getPerson_name())){
                mTvName.setText(accountInfo.getPerson_name());
            }
        }
    }
    private void initData(){
        PersonalDeviceFragment fragment = new PersonalDeviceFragment();
        fragment.setData(mVersion,mName,mMac);
        mFragmentList.add(new PersonalCollectFragment());
        mFragmentList.add(fragment);
        mFragmentList.add(new PersonalSettingFragment());
        mTitleList.add(getString(R.string.personal_collect));
        mTitleList.add(getString(R.string.personal_device));
        mTitleList.add(getString(R.string.personal_setting));
        initView();

    }

    @Override
    public void uploadStart(String fileInfo) {
        showWaitDialog("正在上传头像");
    }

    @Override
    public void uploadProgress(String fileInfo) {

    }

    @Override
    public void uploadSuccess(String url) {
        Log.e("Head","============="+url);
        if(!TextUtils.isEmpty(url)) {
            PreferenceManager.getInstance().getAccountInfo().setAvtorURL(url);
            PreferenceManager.getInstance().setStringValue(PreferenceManager.ACC_LOGIN_AVTOR_URL, url);
            UserManager.getRequestHandler().requestUploadAvatar(this,url);
        }else{
            hideWaitDialog();
        }
    }

    @Override
    public void uploadFailed(String fileInfo) {
        hideWaitDialog();
        showTotast("头像上传失败");
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        hideWaitDialog();
        showTotast("头像上传成功");

    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        hideWaitDialog();
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}


 class PersonalPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragmentList;
    private List<String> list_Title;

    public PersonalPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, List<String> list_Title) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }

    /**
     * //此方法用来显示tab上的名字
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }
}