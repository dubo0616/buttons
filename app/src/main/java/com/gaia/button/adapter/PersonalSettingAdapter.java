package com.gaia.button.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gaia.button.R;
import com.gaia.button.model.PersonalDeviceModel;
import com.gaia.button.model.PersonalSettingModel;

import java.util.List;

public  class PersonalSettingAdapter extends RecyclerView.Adapter<PersonalSettingAdapter.ViewHolder> {

    public interface OnItemListener {
        void onItemClick(int position);
    }

    private List<PersonalSettingModel> mList;

    private OnItemListener mOnItemListener;

    public PersonalSettingAdapter(List<PersonalSettingModel> list,OnItemListener listener) {
        this.mList = list;
        this.mOnItemListener = listener;
    }

    @NonNull
    @Override
    public PersonalSettingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_personal_item_setting, parent, false);
        ViewHolder holder = new PersonalSettingAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonalSettingAdapter.ViewHolder holder, final int position) {

        PersonalSettingModel model = mList.get(position);
        holder.mTitle.setText(model.getName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mOnItemListener != null){
                mOnItemListener.onItemClick(position);
            }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private ImageView mArrow;
        private View mLine;
        private View root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            mTitle = itemView.findViewById(R.id.tv_name);
            mArrow = itemView.findViewById(R.id.iv_arrow);
            mLine = itemView.findViewById(R.id.v_line);
        }
    }
}