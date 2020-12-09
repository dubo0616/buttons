package com.gaia.button.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gaia.button.R;

import java.util.List;
import com.gaia.button.model.ProductModel;

public class ProductAdater extends RecyclerView.Adapter<ProductAdater.ViewHolder> {

    private List<ProductModel> mList;
    private ProductAdapterOnclickListener mOnClickListener;
    private int mType;//0产品页1发现页

    public ProductAdater(List<ProductModel> list, ProductAdapterOnclickListener listener,int type) {
        this.mList = list;
        this.mOnClickListener = listener;
        this.mType = type;
    }

    public void setData(List<ProductModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_item, parent, false);
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
//        holder.mDetailLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnClickListener != null) {
//                    mOnClickListener.onClickDetail(model);
//                }
//            }
//        });


//        holder.detailImg.setImageResource(model.getImgUrl());
//        holder.mTvDetail.setText(model.getDetail());
        if(mType == 0) {
//            holder.mTvPrice.setText(model.getPrice());
//            holder.mTvSales.setText(model.getSales());
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            mTvSales = itemView.findViewById(R.id.tv_sales);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mTvDetail = itemView.findViewById(R.id.tv_detail);
            mLayoutSales = itemView.findViewById(R.id.layout_sales);
        }
    }

    public interface ProductAdapterOnclickListener {
        void onClickDetail(ProductModel model);
    }
}
