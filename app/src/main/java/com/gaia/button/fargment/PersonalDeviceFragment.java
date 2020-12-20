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

import com.bumptech.glide.Glide;
import com.gaia.button.R;
import com.gaia.button.model.DeviceList;
import com.gaia.button.model.DeviceMode;
import com.gaia.button.model.PersonalDeviceModel;
import com.gaia.button.model.ProductModelList;
import com.gaia.button.net.NetConfig;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.view.GridSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_GetCollect;
import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_GetDevice;

public class PersonalDeviceFragment extends BaseFragment implements IUserListener {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<DeviceMode> mList = new ArrayList<>();
    private PersonalDeviceAdapter mDeviceAdapter;
    private boolean mRefreshFlag = false;
    private TextView mTvNodata;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_personal_device, container, false);
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mRefreshFlag)
                    return;
                initData();
            }
        });
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_msg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(null);
        mDeviceAdapter = new PersonalDeviceAdapter(mList);
        mRecyclerView.setAdapter(mDeviceAdapter);
    }
    private void initData(){
        if(mRefreshFlag){
            return;
        }
        mRefreshFlag = true;
        NetConfig.isGet = true;
        UserManager.getRequestHandler().requestGetDevice(PersonalDeviceFragment.this);

    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        mSwipeRefreshLayout.setRefreshing(false);
        mRefreshFlag = false;
        mSwipeRefreshLayout.setRefreshing(false);
        if(requestTag == Net_Tag_User_GetDevice){
            DeviceList list = (DeviceList) data;
            if(list != null && list.getData() != null && list.getData().size()>0){
                mDeviceAdapter.setData(list.getData());
                mTvNodata.setVisibility(View.GONE);
            }else{
                mTvNodata.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        mSwipeRefreshLayout.setRefreshing(false);
        mRefreshFlag = false;
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }

    private class PersonalDeviceAdapter extends RecyclerView.Adapter<PersonalDeviceAdapter.ViewHolder>{

        private List<DeviceMode> mList;
        public PersonalDeviceAdapter(List<DeviceMode> list){
            this.mList = list;
        }
        public void setData(List<DeviceMode> list){
            this.mList = list;
            notifyDataSetChanged();
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

            DeviceMode model = mList.get(position);
            Glide.with(mContext).load(model.getDevice_img()).into(holder.mDeviceIcon);
            holder.mDeviceName.setText(model.getDevice_name());
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
            private ImageView mDeviceIcon;
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
                mDeviceIcon = itemView.findViewById(R.id.iv_device_icon);
            }
        }
    }
}
