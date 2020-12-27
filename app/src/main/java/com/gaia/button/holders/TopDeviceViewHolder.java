package com.gaia.button.holders;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gaia.button.R;
import com.gaia.button.adapter.DevicesListAdapter;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.utils.Consts;

import java.util.ArrayList;
import java.util.List;

public class TopDeviceViewHolder extends RecyclerView.ViewHolder{

    private RecyclerView mRecyclerView;
    private final List<BluetoothDevice> mDevices ;
    private TDevicesListAdapter mDevicesListAdapter;
    private  DeviceViewHolder.IDeviceViewHolder mListener;
    private String arrdess;
    private Activity act;
    public TopDeviceViewHolder(@NonNull View itemView, Activity context, List<BluetoothDevice> list, DeviceViewHolder.IDeviceViewHolder listener) {
        super(itemView);
        this.act = context;
        mListener = listener;
        this.mDevices = list;
        Log.e("ddddd","======="+mDevices.size());
        mRecyclerView = itemView.findViewById(R.id.rv_devices_list);
        GridLayoutManager layoutManager = new GridLayoutManager(context,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mDevicesListAdapter = new TDevicesListAdapter();
        mRecyclerView.setAdapter(mDevicesListAdapter);
        arrdess = PreferenceManager.getInstance().getStringValue(PreferenceManager.CONNECT_ARRAESS);

    }

    public class TDevicesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public TDevicesListAdapter() {
        }


        @Override // RecyclerView.Adapter<DeviceViewHolder>
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_devices_item_item, parent, false);
            return new ItemDeviceViewHolder(view);

        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ItemDeviceViewHolder h = (ItemDeviceViewHolder) holder;
            final BluetoothDevice device = mDevices.get(position);
            if(device !=null) {
                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPref = act.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(Consts.TRANSPORT_KEY, device.getType());
                        editor.putString(Consts.BLUETOOTH_NAME_KEY, device.getName());
                        editor.putString(Consts.BLUETOOTH_ADDRESS_KEY, device.getAddress());
                        editor.apply();
                        act.setResult(Activity.RESULT_OK);
                        act.finish();
                    }
                });
                h.mLL.setVisibility(View.VISIBLE);
                h.mNodata.setVisibility(View.GONE);
                h.textViewDeviceName.setText(device.getName());
                if (device.getName().endsWith("X") || device.getName().endsWith("x")) {
                    h.imageView.setBackgroundResource(R.drawable.icon_airx);
                }else{
                    h.imageView.setBackgroundResource(R.drawable.icon_air);
                }
                if(device.getAddress().equals(arrdess)){
                    h.connect.setText("已连接");
                    h.connect.setSelected(true);
                }else{
                    h.connect.setText("未连接");
                    h.connect.setSelected(false);
                }

            }else{
                h.mNodata.setVisibility(View.VISIBLE);
                h.mLL.setVisibility(View.GONE);
            }

        }


        @Override // RecyclerView.Adapter<DeviceViewHolder>
        public int getItemCount() {
            return mDevices.size();
        }

        public class ItemDeviceViewHolder extends RecyclerView.ViewHolder {

            /**
             * The text view to display the device name.
             */
            private  TextView textViewDeviceName,connect;
            private ImageView imageView;
            private LinearLayout mLL;
            private ImageView mNodata;
            private View itemView;

            public ItemDeviceViewHolder(View rowView) {
                super(rowView);
                itemView = rowView;
                textViewDeviceName = (TextView) rowView.findViewById(R.id.tv_name);
                imageView = (ImageView) rowView.findViewById(R.id.iv_img);
                mNodata = (ImageView) rowView.findViewById(R.id.iv_no);
                connect = (TextView) rowView.findViewById(R.id.tv_connect);
                mLL = (LinearLayout) rowView.findViewById(R.id.ll_has);
            }

            public void refreshValues(String name, String address, boolean isSelected,
                                      Context context) {
                textViewDeviceName.setText(name);
            }

        }

    }

}

