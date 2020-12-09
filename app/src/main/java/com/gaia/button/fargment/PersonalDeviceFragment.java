package com.gaia.button.fargment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gaia.button.R;
import com.gaia.button.model.PersonalDeviceModel;
import com.gaia.button.view.GridSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PersonalDeviceFragment extends BaseFragment {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<PersonalDeviceModel> mList = new ArrayList<>();
    private PersonalDeviceAdapter mDeviceAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_personal_device, container, false);
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
//                if(mRefreshFlag)
//                    return;
//                mRefreshFlag = true;
//                initData();
//            }
//        });
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_msg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(null);
        mList.add(new PersonalDeviceModel());
        mList.add(new PersonalDeviceModel());
        mList.add(new PersonalDeviceModel());
        mList.add(new PersonalDeviceModel());
        mDeviceAdapter = new PersonalDeviceAdapter(mList);
        mRecyclerView.setAdapter(mDeviceAdapter);
    }

    private class PersonalDeviceAdapter extends RecyclerView.Adapter<PersonalDeviceAdapter.ViewHolder>{

        private List<PersonalDeviceModel> mList;
        public PersonalDeviceAdapter(List<PersonalDeviceModel> list){
            this.mList = list;
        }
        @NonNull
        @Override
        public PersonalDeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_personal_device_item, parent, false);
            PersonalDeviceAdapter.ViewHolder holder = new PersonalDeviceAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final PersonalDeviceAdapter.ViewHolder holder, int position) {

            PersonalDeviceModel model = mList.get(position);
            holder.mArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = holder.mBottomInfo.getVisibility() == View.GONE;
                    if (!show) {
                        holder.mLine.setVisibility(View.VISIBLE);
                        holder.mBottomInfo.setVisibility(View.GONE);
                        holder.mArrow.setImageResource(R.drawable.icon_personal_device);
                    } else {
                        holder.mLine.setVisibility(View.GONE);
                        holder.mBottomInfo.setVisibility(View.VISIBLE);
                        holder.mArrow.setImageResource(R.drawable.icon_personal_device_down);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private ConstraintLayout mBottomInfo;
            private ImageView mArrow;
            private LinearLayout mLayoutLink;
            private LinearLayout mLayouDeviceInfo;
            private LinearLayout mLayouHardUpgrade;
            private LinearLayout mLayouProductIntro;
            private View mLine;
            private TextView mDeviceName;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mBottomInfo = itemView.findViewById(R.id.layout_control);
                mArrow = itemView.findViewById(R.id.iv_arrow);
                mLayoutLink = itemView.findViewById(R.id.ll_link);
                mLayouDeviceInfo = itemView.findViewById(R.id.ll_device_info);
                mLayouHardUpgrade = itemView.findViewById(R.id.ll_hard_upgrade);
                mLayouProductIntro = itemView.findViewById(R.id.ll_product_intro);
                mLine = itemView.findViewById(R.id.v_line);
                mDeviceName = itemView.findViewById(R.id.tv_device_name);
            }
        }
    }
}
