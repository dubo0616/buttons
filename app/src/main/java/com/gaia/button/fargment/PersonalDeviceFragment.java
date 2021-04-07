package com.gaia.button.fargment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.gaia.button.R;
import com.gaia.button.activity.MainActivity;
import com.gaia.button.activity.WebViewActivity;
import com.gaia.button.adapter.DevicesListAdapter;
import com.gaia.button.utils.BaseUtils;
import com.gaia.button.utils.Consts;
import com.gaia.button.view.DeceiveInfoDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersonalDeviceFragment extends BaseFragment implements DevicesListAdapter.IDevicesListAdapterListener {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<BluetoothDevice> mList = new ArrayList<>();
    private PersonalDeviceAdapter mDeviceAdapter;
    private boolean mRefreshFlag = false;
    private TextView mTvNodata, mTvAddDevice;
    private String mVersion, mName, mMac;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_personal_device, container, false);
            initView();
        }
        return mRootView;
    }

    public void setData(String v, String n, String m) {
        this.mVersion = v;
        this.mName = n;
        this.mMac = m;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sinit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initView() {
        mTvNodata = mRootView.findViewById(R.id.tv_nodata);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mRefreshFlag)
                    return;
                initData();
            }
        });
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list_msg);
        mTvAddDevice = mRootView.findViewById(R.id.tv_add_device);
        mTvAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("Tab", 1);
                    intent.putExtra(MainActivity.FORM_KEY, 1);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(null);
        mDeviceAdapter = new PersonalDeviceAdapter(mList);
        mRecyclerView.setAdapter(mDeviceAdapter);
    }

    @Override
    public void onBluetoothEnabled() {
        super.onBluetoothEnabled();
        initData();
    }

    private void initData() {
        Set<BluetoothDevice> listDevices = new HashSet<>();
        //todo 修改顶部设备列表，只获取当前已配对的设备，不显示配对的历史设备
        final Map<String, BluetoothDevice> audioConnected = getAudioConnectedAddress();
        for (BluetoothDevice b : audioConnected.values()) {
            listDevices.add(b);
        }
        ArrayList<BluetoothDevice> listBLEDevices = new ArrayList<>();

        for (BluetoothDevice device : listDevices) {
            if (!BaseUtils.isButtonDevice(device)) {
                continue;
            }
            if (device.getType() == BluetoothDevice.DEVICE_TYPE_DUAL
                    || device.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC
                    || device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                listBLEDevices.add(device);
            }
        }
        if (listBLEDevices != null && listBLEDevices.size() > 0) {
            mDeviceAdapter.setData(listBLEDevices);
            mTvNodata.setVisibility(View.GONE);
        } else {
            mTvNodata.setVisibility(View.VISIBLE);
        }
        mSwipeRefreshLayout.setRefreshing(false);

    }


    @Override
    public void onItemSelected(boolean itemSelected, BluetoothDevice device) {

    }

    public class PersonalDeviceAdapter extends RecyclerView.Adapter<PersonalDeviceAdapter.ViewHolder> {

        private List<BluetoothDevice> mList;

        public PersonalDeviceAdapter(List<BluetoothDevice> list) {
            this.mList = list;
        }

        public void setData(List<BluetoothDevice> list) {
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

            final BluetoothDevice model = mList.get(position);
//            Glide.with(mContext).load(model.getDevice_img()).into(holder.mDeviceIcon);
            holder.mDeviceName.setText(model.getName());
            holder.view.setOnClickListener(new View.OnClickListener() {
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

            holder.mLayoutLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mLayoutLink.setSelected(!holder.mLayoutLink.isSelected());
                    holder.mLayouHardUpgrade.setSelected(false);
                    holder.mLayouProductIntro.setSelected(false);
                    holder.mLayouDeviceInfo.setSelected(false);
                    SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(Consts.TRANSPORT_KEY, model.getType());
                    editor.putString(Consts.BLUETOOTH_NAME_KEY, model.getName());
                    editor.putString(Consts.BLUETOOTH_ADDRESS_KEY, model.getAddress());
                    editor.apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("Tab", 1);
                    intent.putExtra(MainActivity.FORM_KEY, 1);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            if (model.getName().endsWith("x") || model.getName().endsWith("X")) {
                holder.mDeviceIcon.setImageResource(R.drawable.icon_airx);
            } else {
                holder.mDeviceIcon.setImageResource(R.drawable.icon_air);
            }
            holder.mLayouDeviceInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(mMac) || !mMac.equals(model.getAddress())) {
                        displayShortToast("设备未连接");
                        return;
                    }
                    DeceiveInfoDialog dialog = new DeceiveInfoDialog(mContext);
                    dialog.show();
                    dialog.setData(mName, mMac, mVersion);
                    holder.mLayoutLink.setSelected(false);
                    holder.mLayouHardUpgrade.setSelected(false);
                    holder.mLayouProductIntro.setSelected(false);
                    holder.mLayouDeviceInfo.setSelected(!holder.mLayouDeviceInfo.isSelected());
                }
            });
            holder.mLayouHardUpgrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mLayoutLink.setSelected(false);
                    holder.mLayouHardUpgrade.setSelected(!holder.mLayouHardUpgrade.isSelected());
                    holder.mLayouProductIntro.setSelected(false);
                    holder.mLayouDeviceInfo.setSelected(false);
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    String url = "http://img.lovetoshare168.com/bz/v1/user_explain/1";
                    if (model.getName().endsWith("x") || model.getName().endsWith("X")) {
                        url = "http://img.lovetoshare168.com/bz/v1/user_explain/4";
                    }
                    intent.putExtra(WebViewActivity.URL_KEY, url);
                    intent.putExtra(WebViewActivity.TITLE_KEY, "商品说明");
                    startActivity(intent);
                }
            });
            holder.mLayouProductIntro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mLayoutLink.setSelected(false);
                    holder.mLayouHardUpgrade.setSelected(false);
                    holder.mLayouProductIntro.setSelected(!holder.mLayouProductIntro.isSelected());
                    holder.mLayouDeviceInfo.setSelected(false);
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    String url = "http://img.lovetoshare168.com/bz/v1/user_faq/1";
                    if (model.getName().endsWith("x") || model.getName().endsWith("X")) {
                        url = "http://img.lovetoshare168.com/bz/v1/user_faq/4";
                    }
                    intent.putExtra(WebViewActivity.URL_KEY, url);
                    intent.putExtra(WebViewActivity.TITLE_KEY, "商品问答");
                    startActivity(intent);
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
            private View view;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
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
