package com.gaia.button.fargment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gaia.button.R;
import com.gaia.button.adapter.ProductAdater;
import com.gaia.button.model.DiscoveryModel;
import com.gaia.button.model.ProductModel;
import com.gaia.button.view.GridSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainProductFragment extends BaseFragment implements ProductAdater.ProductAdapterOnclickListener {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mRefreshFlag = false;
    private ProductAdater mProductAdater;
    private List<ProductModel> mList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_product, container, false);
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
        mProductAdater = new ProductAdater(mList,this,0);
        mRecyclerView.setAdapter(mProductAdater);
    }
    private void initData(){

    }
    @Override
    public void searchKey(String key) {
        super.searchKey(key);
    }
    @Override
    public void onClickDetail(ProductModel model) {
        Toast.makeText(getActivity(),"详情",Toast.LENGTH_SHORT).show();
    }

}
