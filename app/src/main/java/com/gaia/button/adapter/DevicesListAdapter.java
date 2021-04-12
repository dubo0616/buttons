/* ************************************************************************************************
 * Copyright 2017 Qualcomm Technologies International, Ltd.                                       *
 **************************************************************************************************/

package com.gaia.button.adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.holders.DeviceViewHolder;
import com.gaia.button.holders.TextViewHolder;
import com.gaia.button.holders.TopDeviceViewHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;

/**
 * <p>This class allows management of the data set for a devices list.</p>
 */
public class DevicesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements DeviceViewHolder.IDeviceViewHolder {

    /**
     * The data managed by this adapter.
     */
    private  List<BluetoothDevice> mDevices = new ArrayList<>();
    private  List<BluetoothDevice> mConnect = new ArrayList<>();
    private List<BluetoothDevice> mScanList = new ArrayList<>();
    /**
     * When the list has no item selected it is identified by this value.
     */
    private static final int ITEM_NULL = -1;
    /**
     * To know which device is selected.
     */
    private int mSelectedItem = ITEM_NULL;
    /**
     * The listener for all user interactions.
     */
    private final IDevicesListAdapterListener mListener;

    /**
     * Default constructor to build a new instance of this adapter.
     */
    private Activity mContext;
    public DevicesListAdapter(Activity context,IDevicesListAdapterListener listener) {
        mListener = listener;
        mContext = context;
    }


    @Override // RecyclerView.Adapter<DeviceViewHolder>
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_devices_item_item, parent, false);
        return new TopDeviceViewHolder(view);

    }

    private String arrdess;
    @Override // RecyclerView.Adapter<DeviceViewHolder>
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BluetoothDevice device = mDevices.get(position);
        if (holder instanceof TopDeviceViewHolder) {
            TopDeviceViewHolder h = (TopDeviceViewHolder) holder;
            arrdess = PreferenceManager.getInstance().getStringValue(PreferenceManager.CONNECT_ARRAESS);
            Log.e("HHHHH","mDevices=="+mDevices.size() + device);
            if(device !=null) {
                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelected(true,device);
                    }
                });
                h.mLL.setVisibility(View.VISIBLE);
                h.mNodata.setVisibility(View.GONE);
                h.textViewDeviceName.setText(device.getName());
                if (!TextUtils.isEmpty(device.getName())&&(device.getName().endsWith("X") || device.getName().endsWith("x"))) {
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
                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
                h.mNodata.setVisibility(View.VISIBLE);
                h.mLL.setVisibility(View.GONE);
            }
        }
    }
    @Override // RecyclerView.Adapter<DeviceViewHolder>
    public int getItemCount() {
        return mDevices.size();
    }

    @Override // DeviceViewHolder.IDeviceViewHolder
    public void onClickItem(int position,BluetoothDevice device) {
        mListener.onItemSelected(hasSelection(),device);
    }

    /**
     * <p>To update the list with a new device or update the information of an existing one.</p>
     *
     * @param device
     *          The device to add or to update.
     * @param rssi
     *          The rssi which corresponds to the device.
     */
    boolean hasAdd = false;
    public void add(BluetoothDevice device, int rssi) {
        if(!hasAdd){
            mDevices.add(null);
            hasAdd = true;
        }
        synchronized (mDevices) {
            if(device != null) {
                boolean contained = mDevices.contains(device);
                if (!contained) {
                    mDevices.add(device);
                    notifyItemInserted(mDevices.size() - 1);
                } else {
                    int position = mDevices.indexOf(device);
                    notifyItemChanged(position);
                }
            }else{
                notifyDataSetChanged();
            }
        }
    }
    public void addScans(BluetoothDevice device, int rssi) {
        if(device != null) {
            mScanList.add(device);
        }
    }
    public BluetoothDevice getAirxDevice(){
        BluetoothDevice device = null;
       if(mScanList.size() == 0){
           return null;
       }
        for (int i = 0; i < mScanList.size(); i++) {
            BluetoothDevice mdevice = mScanList.get(i);
            if(mdevice == null){
                continue;
            }
            if (!TextUtils.isEmpty(device.getName()) && (device.getName().endsWith("X") || device.getName().endsWith("x"))) {
                device = mdevice;
                break;
            }

        }
       return device;
    }
    public BluetoothDevice getAirDevice(){
        BluetoothDevice device = null;
        if(mScanList.size() == 0){
            return null;
        }
        for (int i = 0; i < mScanList.size(); i++) {
            BluetoothDevice mdevice = mScanList.get(i);
            if(mdevice == null){
                continue;
            }
            if (!TextUtils.isEmpty(device.getName()) && (!device.getName().endsWith("X") && !device.getName().endsWith("x"))) {
                device = mdevice;
                break;
            }
        }
        return device;
    }
    /**
     * To completely reset the data set list and clear it completely.
     * todo 添加已配对的设备列表的适配器的刷新，更改状态
     */
    public void reset() {
        mDevices.clear();
        hasAdd = false;
        mSelectedItem = ITEM_NULL;
        notifyDataSetChanged();
    }

    /**
     * This method allows to know if the view has a selected item.
     *
     * @return true if the view has a selected item and false if none of the items is selected.
     */
    public boolean hasSelection() {
        return mSelectedItem >= 0 && mSelectedItem < mDevices.size();
    }

    /**
     * <p>To get the Device which is actually selected.</p>
     *
     * @return the selected device or null if there is no device selected.
     */
    public BluetoothDevice getSelectedItem() {
        if (hasSelection()) {
            return mDevices.get(mSelectedItem);
        }
        else {
            return null;
        }
    }

    /**
     * This method allows to define the data for the adapter.
     *
     * @param listDevices
     *            The list of devices to put on the RecyclerView.
     */

    public void setListDevices(ArrayList<BluetoothDevice> listDevices) {
        this.mConnect.clear();
        this.mConnect.addAll(listDevices);
        if(mConnect.size() == 0){
            mConnect.add(null);
        }

    }

    /**
     * This interface allows the adapter to communicate with the element which controls the RecyclerView. Such as a
     * fragment or an activity.
     */
    public interface IDevicesListAdapterListener {
        void onItemSelected(boolean itemSelected,BluetoothDevice device);

        Context getContext();
    }
}