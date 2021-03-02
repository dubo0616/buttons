package com.gaia.button.fargment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.gaia.button.GaiaApplication;
import com.gaia.button.R;
import com.gaia.button.activity.PhoneSetPassActivity;
import com.gaia.button.activity.WebViewActivity;
import com.gaia.button.adapter.DiscoveryAdapter;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.DiscoverList;
import com.gaia.button.model.DiscoveryModel;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.view.ShareDialog;
import com.squareup.okhttp.internal.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_ArticleCollect;
import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_ArticleList;

public class MainDiscoveryFragment extends BaseFragment implements DiscoveryAdapter.DiscoveryAdapterOnclickListener, IUserListener {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mRefreshFlag = false;
    private DiscoveryAdapter mDiscoveryAdapter;
    private List<DiscoveryModel> mList = new ArrayList<>();
    private int mPage =1;
    private TextView mTvNodata;
    private ShareDialog mShareDialog;
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
        mTvNodata = mRootView.findViewById(R.id.tv_nodata);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mRefreshFlag)
                    return;
                mPage = 1;
                initData();
            }
        });
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_msg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mPage++;
                initData();
            }
        });
        mDiscoveryAdapter = new DiscoveryAdapter(getActivity(),mList,this);
        mRecyclerView.setAdapter(mDiscoveryAdapter);
    }
    private void initData(){
        if(mRefreshFlag){
            return;
        }
        mRefreshFlag = true;
//        showWaitDialog();
        UserManager.getRequestHandler().requestgetDiscover(MainDiscoveryFragment.this, mPage,"");

    }

    @Override
    public void onClickDetail(DiscoveryModel model) {
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra(WebViewActivity.URL_KEY,model.getDetailUrl());
        intent.putExtra(WebViewActivity.TITLE_KEY,"文章详情");
        startActivity(intent);
    }
    String type ="";
    @Override
    public void onClickCollect(DiscoveryModel model) {
        type = model.getIs_collect();
        UserManager.getRequestHandler().requestCollect(MainDiscoveryFragment.this,model.getId());

    }

    @Override
    public void onClickForward(final DiscoveryModel model) {
        if(mShareDialog == null){
            mShareDialog = ShareDialog.getInstance(getActivity(), new ShareDialog.ShareDialogListener() {
                @Override
                public void onItemClick(int type) {
                    share(model,type);
                }
            });
        }
        mShareDialog.show();
    }
    private void share(DiscoveryModel model ,int type){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl =model.getDetailUrl();
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title =model.getTitle();
        msg.description = model.getTitle();
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = msg;
        req.scene = type == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
//
//        Glide.with(getActivity()).asBitmap().load(model.getList_img()).into(new SimpleTarget<Bitmap>(64, 64) {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
////                ( (ImageView)mRootView.findViewById(R.id.test)).setImageBitmap(resource);
////                int bytes = resource.getByteCount();
////                ByteBuffer buf = ByteBuffer.allocate(bytes);
////                resource.copyPixelsToBuffer(buf);
////                byte[] byteArray = buf.array();
////                Log.e("HHHH","111111111111d"+byteArray.length);
////                msg.thumbData = byteArray;
//                GaiaApplication.getWeinAPIHandler(getActivity()).sendReq(req);
//            }
//
//            @Override
//            public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                super.onLoadFailed(errorDrawable);
//                GaiaApplication.getWeinAPIHandler(getActivity()).sendReq(req);
//            }
//        });
        GaiaApplication.getWeinAPIHandler(getActivity()).sendReq(req);
    }

    @Override
    public void searchKey(String key) {
        super.searchKey(key);
        if(mRefreshFlag)
            return;
        mPage = 1;
        UserManager.getRequestHandler().requestgetDiscover(MainDiscoveryFragment.this, mPage,key);
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        hideWaitDialog();
        mRefreshFlag = false;
        mSwipeRefreshLayout.setRefreshing(false);
        if(requestTag == Net_Tag_User_ArticleList){
            DiscoverList list = (DiscoverList) data;
            if(list != null && list.getData() != null && list.getData().size()>0){
                if(mPage == 1){
                    mList.clear();
                }
                mList.addAll(list.getData());
                mDiscoveryAdapter.setData(mList);
                mTvNodata.setVisibility(View.GONE);
            }else{
                if(mPage == 1) {
                    mList.clear();
                    mDiscoveryAdapter.notifyDataSetChanged();
                    mTvNodata.setVisibility(View.VISIBLE);
                }
            }
        } else if(requestTag == Net_Tag_User_ArticleCollect){
            Toast.makeText(getActivity(),"操作成功",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        hideWaitDialog();
        mRefreshFlag = false;
        mSwipeRefreshLayout.setRefreshing(false);
         if(requestTag == Net_Tag_User_ArticleCollect){
            Toast.makeText(getActivity(),"失败",Toast.LENGTH_SHORT).show();
        }
        if(requestTag == Net_Tag_User_ArticleList){
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
