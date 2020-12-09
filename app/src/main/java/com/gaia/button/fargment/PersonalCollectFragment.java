package com.gaia.button.fargment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gaia.button.R;
import com.gaia.button.adapter.ProductAdater;
import com.gaia.button.model.ProductModel;
import com.gaia.button.view.GridSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PersonalCollectFragment extends BaseFragment implements ProductAdater.ProductAdapterOnclickListener{

    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProductAdater mProductAdater;
    private List<ProductModel> mList = new ArrayList<>();
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setEnabled(false);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                initData();
//            }
//        });
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_msg);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.addItemDecoration(new GridSpaceItemDecoration(2, 0,0));
        mList.add(new ProductModel());
        mList.add(new ProductModel());
        mList.add(new ProductModel());
        mList.add(new ProductModel());
        mList.add(new ProductModel());
        mList.add(new ProductModel());
        mList.add(new ProductModel());
        mList.add(new ProductModel());
        mProductAdater = new ProductAdater(mList,this,1);
        mRecyclerView.setAdapter(mProductAdater);
    }
    private void initData(){

    }
    @Override
    public void onClickDetail(ProductModel model) {

    }
}
