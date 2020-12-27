package com.gaia.button.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaia.button.R;
import com.gaia.button.adapter.DiscoveryAdapter;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.fargment.MainDiscoveryFragment;
import com.gaia.button.fargment.MainProductFragment;
import com.gaia.button.model.DiscoverList;
import com.gaia.button.model.DiscoveryModel;
import com.gaia.button.model.ProductModel;
import com.gaia.button.model.ProductModelList;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.view.WordWrapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_ArticleList;
import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_ProductList;

public class SearchActivity extends BaseActivity implements IUserListener {

    private EditText mEditText;
    private TextView mTvSearch;
    private List<String> strs = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Searchdapter mSearchdapter;
    private List<Object> mList = new ArrayList<>();
    private int mtype = 0;
    private TextView mTvHist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mtype = getIntent().getIntExtra("type",0);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_msg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(null);
        mSearchdapter = new Searchdapter(this,mList);
        mRecyclerView.setAdapter(mSearchdapter);
        mEditText = findViewById(R.id.et_search);
        mTvSearch = findViewById(R.id.iv_personal);
        wordWrapView=findViewById(R.id.wordWrapView);
        mTvHist = findViewById(R.id.tv_hist);
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput(mEditText);
                if (!TextUtils.isEmpty(mEditText.getText().toString())) {
                    addStr(mEditText.getText().toString());
                    saveStr();
                    showWaitDialog();
                    if (mtype == 0) {
                        UserManager.getRequestHandler().requestgetDiscover(SearchActivity.this, 1, mEditText.getText().toString());
                    } else {
                        UserManager.getRequestHandler().requestProductList(SearchActivity.this, 1, mEditText.getText().toString());

                    }
//                    Intent intent = new Intent();
//                    intent.putExtra("key",mEditText.getText().toString());
//                    setResult(RESULT_OK,intent);
                } else {
                    finish();
                }
            }
        });
        showSoftInput(mEditText);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    wordWrapView.setVisibility(View.GONE);
                    mTvHist.setVisibility(View.GONE);
                    mTvSearch.setText("搜索");
                }else{
                    wordWrapView.setVisibility(View.VISIBLE);
                    mTvHist.setVisibility(View.VISIBLE);
                    mList.clear();
                    mSearchdapter.notifyDataSetChanged();
                    mTvSearch.setText("取消");
                }
            }
        });
        String strp = PreferenceManager.getInstance().getSearchHistory(PreferenceManager.getInstance().getAccountInfo().getUserID());
        if(!TextUtils.isEmpty(strp)) {
           List<String> stringList = Arrays.asList(strp.split(","));
            //添加子view：TextView
            for (String str : stringList) {
                if(TextUtils.isEmpty(str) || "null".equals(str)){
                    continue;
                }
                addStr(str);
                final TextView tv = new TextView(SearchActivity.this);
                tv.setTextSize(14);//设置字体大小
                tv.setTextColor(Color.BLACK);
                tv.setText(str);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditText.setText(tv.getText().toString());
                        mEditText.setSelection(tv.getText().toString().length());
                        hideSoftInput(mEditText);
//                        Intent intent = new Intent();
                        addStr(tv.getText().toString());
                        saveStr();
                        if(mtype ==0){
                            UserManager.getRequestHandler().requestgetDiscover(SearchActivity.this, 1,mEditText.getText().toString());
                        }else{
                            UserManager.getRequestHandler().requestProductList(SearchActivity.this, 1,mEditText.getText().toString());

                        }
//                        intent.putExtra("key", tv.getText().toString());
//                        setResult(RESULT_OK, intent);
//                        finish();
                    }
                });
                tv.setPadding(15, 0, 15, 0);
                tv.setBackgroundResource(R.drawable.bg_search);//子view背景
                wordWrapView.addView(tv);
            }
        }
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        hideWaitDialog();
        if(requestTag == Net_Tag_User_ArticleList){
            DiscoverList list = (DiscoverList) data;
            if (list != null && list.getData() != null && list.getData().size() > 0) {
                mSearchdapter.setData(list.getData());
            }
        } else if(requestTag == Net_Tag_User_ProductList) {
            ProductModelList list = (ProductModelList) data;
            if (list != null && list.getData() != null && list.getData().size() > 0) {
                mSearchdapter.setPData(list.getData());
            }
        }
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


    public class Searchdapter extends RecyclerView.Adapter<Searchdapter.ViewHolder> {

        private List<Object> mList;
        private Context mContext;

        public Searchdapter(Context context, List<Object> list) {
            this.mList = list;
            this.mContext = context;
        }

        public void setData(ArrayList<DiscoveryModel> list) {
            if(list == null || list.size()== 0){
                return;
            }
            this.mList.clear();
            for (DiscoveryModel model :list){
                this.mList.add(model);
            }
            notifyDataSetChanged();
        }
        public void setPData(ArrayList<ProductModel> list) {
            if(list == null || list.size()== 0){
                return;
            }
            this.mList.clear();
            for (ProductModel model :list){
                this.mList.add(model);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Searchdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_search_item, parent, false);
            ViewHolder holder = new Searchdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull Searchdapter.ViewHolder holder, int position) {
            final Object model = mList.get(position);
            if(mtype == 0){
                final DiscoveryModel dis = (DiscoveryModel) model;
                Glide.with(mContext).load(dis.getList_img()).into(holder.detailImg);
                holder.detailContent.setText(dis.getCate_name());
                holder.mTvLink.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,WebViewActivity.class);
                        intent.putExtra(WebViewActivity.URL_KEY,dis.getDetailUrl());
                        intent.putExtra(WebViewActivity.TITLE_KEY,"文章详情");
                        startActivity(intent);
                    }
                });
            }else{
                final ProductModel dis = (ProductModel) model;
                Glide.with(mContext).load(dis.getList_img()).into(holder.detailImg);
                holder.detailContent.setText(dis.getTitle());
                holder.mTvLink.setText(dis.getPrice());
                holder.mTvLink.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(WebViewActivity.URL_KEY,dis.getDetailUrl());
                        intent.putExtra(WebViewActivity.TITLE_KEY,"产品详情");
                        startActivity(intent);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return null == mList ? 0 : mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView detailImg;
            private TextView detailContent;
            private TextView mTvLink;
            private View itemView;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.itemView = itemView;
                detailImg = itemView.findViewById(R.id.iv_detail_img);
                detailContent = itemView.findViewById(R.id.tv_detail_content);
                mTvLink = itemView.findViewById(R.id.tv_detail_link);

            }
        }

    }

    private void addStr(String key) {

        if (strs == null)
            return;
        if (strs.contains(key)) {
            return;
        }
        if (strs.size() < 5) {
            strs.add(key);
        } else {
            strs.remove(0);
            strs.add(0, key);
        }
    }
    private void saveStr() {
        if (strs == null || strs.size() == 0)
            return;
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            if (i == strs.size() - 1) {
                sb.append(str);
            } else {
                sb.append(str).append(",");
            }
            i++;
        }
        PreferenceManager.getInstance().setSearchHistoryString(PreferenceManager.getInstance().getAccountInfo().getUserID(),sb.toString());

    }
    private WordWrapView wordWrapView;
    private void hideSoftInput(EditText searchView) {
        if(searchView ==null)
            return;
        InputMethodManager inputMethodManager =(InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);

    }

    private void showSoftInput(EditText et) {
        if(et==null)
            return;
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.RESULT_SHOWN);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

}
