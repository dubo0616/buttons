package com.gaia.button.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.gaia.button.R;
import com.gaia.button.fargment.PersonalCollectFragment;
import com.gaia.button.fargment.PersonalDeviceFragment;
import com.gaia.button.fargment.PersonalSettingFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PersonnalActivity extends BaseActivity {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initData();
    }
    private void initView(){
        TabLayout tab_layout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        PersonalPagerAdapter fragmentAdater = new  PersonalPagerAdapter(getSupportFragmentManager(),this,mFragmentList,mTitleList);
        viewPager.setAdapter(fragmentAdater);
        tab_layout.setupWithViewPager(viewPager);
    }
    private void initData(){
        mFragmentList.add(new PersonalCollectFragment());
        mFragmentList.add(new PersonalDeviceFragment());
        mFragmentList.add(new PersonalSettingFragment());
        mTitleList.add(getString(R.string.personal_collect));
        mTitleList.add(getString(R.string.personal_device));
        mTitleList.add(getString(R.string.personal_setting));
        initView();

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