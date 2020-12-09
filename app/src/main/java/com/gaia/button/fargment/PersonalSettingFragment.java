package com.gaia.button.fargment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gaia.button.R;
import com.gaia.button.adapter.PersonalSettingAdapter;
import com.gaia.button.model.PersonalDeviceModel;
import com.gaia.button.model.PersonalSettingModel;

import java.util.ArrayList;
import java.util.List;

public class PersonalSettingFragment extends BaseFragment implements PersonalSettingAdapter.OnItemListener {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private List<PersonalSettingModel> mList = new ArrayList<>();
    private PersonalSettingAdapter mSettingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_personal_setting, container, false);
            initView();
        }
        return mRootView;
    }
    private void initView(){
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_msg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(null);
        String[] strings = getResources().getStringArray(R.array.string_array);
        for(int i= 0;i<strings.length;i++) {
            mList.add(new PersonalSettingModel(strings[i]));
        }
        mSettingAdapter = new PersonalSettingAdapter(mList,this);
        mRecyclerView.setAdapter(mSettingAdapter);
    }


    @Override
    public void onItemClick(int pos) {

    }
}
