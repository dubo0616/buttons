package com.gaia.button.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gaia.button.R;

import java.util.List;
import com.gaia.button.model.ProductModel;
import com.gaia.button.utils.DensityUtil;

public class ProductAdater extends RecyclerView.Adapter<ProductAdater.ViewHolder> {

    private List<ProductModel> mList;
    private ProductAdapterOnclickListener mOnClickListener;
    private int mType;//0产品页1发现页
    private Context mContext;
    public ProductAdater(Context context,List<ProductModel> list, ProductAdapterOnclickListener listener,int type) {
        this.mList = list;
        this.mOnClickListener = listener;
        this.mType = type;
        this.mContext = context;
    }

    public void setData(List<ProductModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void addIndexList(List<ProductModel> list) {
        if(list == null){
            return;
        }
        for(ProductModel topdata :list){
            topdata.setTop(1);
            mList.add(0,topdata);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_item_one, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }


    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).getTop() ==1){
            return 1;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductModel model = mList.get(position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClickDetail(model);
                }
            }
        });

        if(model.getTop() ==1){
            Glide.with(mContext).load(model.getBanner_img()).into(holder.ivDetail);
        }else{
            RequestOptions requestOptions = new RequestOptions().bitmapTransform(new RoundedCorners(DensityUtil.dip2px(mContext,15)));
            Glide.with(mContext).load(model.getList_img()).apply(requestOptions).into(holder.ivDetail);
        }
        holder.mTvDetail.setText(model.getTitle());
        if(mType == 0) {
            holder.mTvPrice.setText("￥"+model.getPrice());
            if(!TextUtils.isEmpty(model.getDc_price())) {
                String off = "￥" + model.getDc_price();
                SpannableString ss = new SpannableString(off);
                //用删除线标记文本
                ss.setSpan(new StrikethroughSpan(), 0, off.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mTvSales.setText("");
                holder.mTvSales.append(ss);
                holder.mTvSales.setText(ss);
            }else{
                holder.mTvSales.setVisibility(View.GONE);
            }
        }else{
            holder.mTvPrice.setVisibility(View.GONE);
            holder.mLayoutSales.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private TextView mTvSales;
        private TextView mTvPrice;
        private TextView mTvDetail;
        private ConstraintLayout mLayoutSales;
        private ImageView ivDetail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            mTvSales = itemView.findViewById(R.id.tv_sales);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mTvDetail = itemView.findViewById(R.id.tv_detail);
            ivDetail = itemView.findViewById(R.id.iv_detail);
            mLayoutSales = itemView.findViewById(R.id.layout_sales);
        }
    }

    public interface ProductAdapterOnclickListener {
        void onClickDetail(ProductModel model);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){   // 布局是GridLayoutManager所管理
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果是Header、Footer的对象则占据spanCount的位置，否则就只占用1个位置
                    return mList.get(position).getTop() == 1 ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }
}
