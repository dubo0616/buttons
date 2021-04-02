package com.gaia.button.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gaia.button.R;
import com.gaia.button.model.DiscoveryModel;
import com.gaia.button.utils.DensityUtil;

import java.util.List;

public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.ViewHolder> {

    private List<DiscoveryModel> mList;
    private DiscoveryAdapterOnclickListener mOnClickListener;
    private Context mContext;

    public DiscoveryAdapter(Context context,List<DiscoveryModel> list,DiscoveryAdapterOnclickListener listener){
         this.mList = list;
         this.mOnClickListener = listener;
         this.mContext =context;
    }
    public void setData(List<DiscoveryModel> list){
        this.mList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_discovery_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DiscoveryModel model = mList.get(position);
        holder.detailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener != null){
                    mOnClickListener.onClickDetail(model);
                }
            }
        });
        holder.mDetailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener != null){
                    mOnClickListener.onClickDetail(model);
                }
            }
        });

        holder.mCollect.setVisibility(model.isCollect()?View.INVISIBLE:View.VISIBLE);
        holder.mCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener != null){
                    mOnClickListener.onClickCollect(model);
                }
            }
        });
        holder.mForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener != null){
                    mOnClickListener.onClickForward(model);
                }
            }
        });
        RequestOptions requestOptions = new RequestOptions().bitmapTransform(new RoundedCorners(DensityUtil.dip2px(mContext,20)));
        Glide.with(mContext).load(model.getList_img()).apply(requestOptions).into(holder.detailImg);
        holder.detailContent.setText(model.getTitle());
        if(!TextUtils.isEmpty(model.getCate_name())){
            holder.mFl.setText(model.getCate_name());
            holder.mFlL.setVisibility(View.VISIBLE);
        }else{
            holder.mFlL.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView detailImg;
        private TextView detailContent;
        private ImageView mCollect;
        private ImageView mForward;
        private TextView mDetailLink;
        private TextView mFl;
        private LinearLayout mFlL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            detailImg = itemView.findViewById(R.id.iv_detail_img);
            detailContent = itemView.findViewById(R.id.tv_detail_content);
            mCollect = itemView.findViewById(R.id.tv_collect);
            mForward = itemView.findViewById(R.id.tv_forward);
            mDetailLink = itemView.findViewById(R.id.tv_detail_link);
            mFlL = itemView.findViewById(R.id.ll_fl);
            mFl = itemView.findViewById(R.id.tv_fl);

        }
    }
    public interface DiscoveryAdapterOnclickListener{
        void onClickDetail(DiscoveryModel model);
        void onClickCollect(DiscoveryModel model);
        void onClickForward(DiscoveryModel model);
    }
}
