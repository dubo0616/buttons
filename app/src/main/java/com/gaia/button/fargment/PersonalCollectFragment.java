package com.gaia.button.fargment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gaia.button.R;
import com.gaia.button.activity.WebViewActivity;
import com.gaia.button.adapter.CollectAdapter;
import com.gaia.button.adapter.ProductAdater;
import com.gaia.button.model.ProductModel;
import com.gaia.button.model.ProductModelList;
import com.gaia.button.net.NetConfig;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.view.GridSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_GetCollect;
import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_ProductList;

public class PersonalCollectFragment extends BaseFragment implements CollectAdapter.ProductAdapterOnclickListener , IUserListener {

    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CollectAdapter mProductAdater;
    private boolean mRefreshFlag = false;
    private List<ProductModel> mList = new ArrayList<>();
    private int mPage =1;
    private TextView mTvNodata;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_personal_collect, container, false);
            initView();
        }
        return mRootView;
    }
    private void initView(){
        mTvNodata = mRootView.findViewById(R.id.tv_nodata);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mRefreshFlag)
                    return;
                mPage=1;
                initData();
            }
        });
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_msg);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.addItemDecoration(new GridSpaceItemDecoration(2, 0,0));
        mProductAdater = new CollectAdapter(getActivity(),mList,this);
        mRecyclerView.setAdapter(mProductAdater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData(){
        if(mRefreshFlag){
            return;
        }
        mRefreshFlag = true;
        NetConfig.isGet = true;
        UserManager.getRequestHandler().requestGetCollectIUserListener(PersonalCollectFragment.this);

    }
    @Override
    public void onClickDetail(ProductModel model) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.URL_KEY,model.getDetailUrl());
        startActivity(intent);
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        mSwipeRefreshLayout.setRefreshing(false);
        mRefreshFlag = false;
        mSwipeRefreshLayout.setRefreshing(false);
        if(requestTag == Net_Tag_User_GetCollect){
            ProductModelList list = (ProductModelList) data;
            if(list != null && list.getData() != null && list.getData().size()>0){
                if(mPage == 1){
                    mList.clear();
                }
                mList.addAll(list.getData());
                mProductAdater.setData(mList);
                mTvNodata.setVisibility(View.GONE);
            }else{
                if(mPage == 1) {
                    mTvNodata.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(requestTag == Net_Tag_User_GetCollect){
            if(mPage == 1) {
                mTvNodata.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
