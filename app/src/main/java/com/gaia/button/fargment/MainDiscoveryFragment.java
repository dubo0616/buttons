package com.gaia.button.fargment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gaia.button.R;
import com.gaia.button.activity.WebViewActivity;
import com.gaia.button.adapter.DiscoveryAdapter;
import com.gaia.button.model.DiscoveryModel;

import java.util.ArrayList;
import java.util.List;

public class MainDiscoveryFragment extends BaseFragment implements DiscoveryAdapter.DiscoveryAdapterOnclickListener {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mRefreshFlag = false;
    private DiscoveryAdapter mDiscoveryAdapter;
    private List<DiscoveryModel> mList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_discovery, container, false);
            initView();
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initView(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mRefreshFlag)
                    return;
                mRefreshFlag = true;
                initData();
            }
        });
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_msg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(null);
        mList.add(new DiscoveryModel());
        mList.add(new DiscoveryModel());
        mList.add(new DiscoveryModel());
        mList.add(new DiscoveryModel());
        mDiscoveryAdapter = new DiscoveryAdapter(mList,this);
        mRecyclerView.setAdapter(mDiscoveryAdapter);
    }
    private void initData(){

    }

    @Override
    public void onClickDetail(DiscoveryModel model) {
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra(WebViewActivity.URL_KEY,"http://www.baidu.com");
        startActivity(intent);
        Toast.makeText(getActivity(),"详情",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickCollect(DiscoveryModel model) {
        Toast.makeText(getActivity(),"收藏",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickForward(DiscoveryModel model) {
        Toast.makeText(getActivity(),"转发",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void searchKey(String key) {
        super.searchKey(key);
    }
}
