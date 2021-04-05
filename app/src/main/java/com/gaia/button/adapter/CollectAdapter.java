package com.gaia.button.adapter;

import android.content.Context;
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
import com.gaia.button.model.ProductModel;
import com.gaia.button.utils.DensityUtil;

import java.util.List;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {

    private List<ProductModel> mList;
    private ProductAdapterOnclickListener mOnClickListener;
    private Context mContext;

    public CollectAdapter(Context context, List<ProductModel> list, ProductAdapterOnclickListener listener) {
        this.mList = list;
        this.mOnClickListener = listener;
        this.mContext = context;
    }

    public void setData(List<ProductModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void addIndexList(List<ProductModel> list) {
        if (list == null) {
            return;
        }
        for (ProductModel topdata : list) {
            topdata.setTop(1);
            mList.add(0, topdata);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_collect_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

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
        RequestOptions requestOptions = new RequestOptions().bitmapTransform(new RoundedCorners(DensityUtil.dip2px(mContext, 15)));
        Glide.with(mContext).load(model.getList_img()).apply(requestOptions).into(holder.ivDetail);
        holder.mTvDetail.setText(model.getTitle());
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private TextView mTvDetail;
        private ImageView ivDetail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            mTvDetail = itemView.findViewById(R.id.tv_detail);
            ivDetail = itemView.findViewById(R.id.iv_detail);
        }
    }

    public interface ProductAdapterOnclickListener {
        void onClickDetail(ProductModel model);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {   // 布局是GridLayoutManager所管理
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
