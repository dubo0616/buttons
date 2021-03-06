/* ************************************************************************************************
 * Copyright 2017 Qualcomm Technologies International, Ltd.                                       *
 **************************************************************************************************/

package com.gaia.button.adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.gaia.button.R;
import com.gaia.button.holders.DeviceViewHolder;
import com.gaia.button.holders.TextViewHolder;
import com.gaia.button.holders.TopDeviceViewHolder;

import java.util.ArrayList;
import java.util.List;

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
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_devices_item_top, parent, false);
            return new TopDeviceViewHolder(view,mContext,mConnect,this);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_devices_item_text, parent, false);
            return new TextViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_devices_item, parent, false);
            return new DeviceViewHolder(view, this,mDevices);
        }

    }


    @Override // RecyclerView.Adapter<DeviceViewHolder>
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BluetoothDevice device = mDevices.get(position);
        if (holder instanceof DeviceViewHolder) {
            DeviceViewHolder h = (DeviceViewHolder) holder;
            String deviceName = device.getName();
            deviceName = (deviceName == null || deviceName.length() < 1) ? "Unknown" : deviceName;
            boolean isSelected = mSelectedItem == position;
            h.refreshValues(deviceName, device.getAddress(), isSelected, mListener.getContext(),position);
        }else if(holder instanceof TextViewHolder){
            TextViewHolder h = (TextViewHolder) holder;
        }else{

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 1;
        }else if(position == 1){
            return 2;
        }

        return super.getItemViewType(position);
    }

    @Override // RecyclerView.Adapter<DeviceViewHolder>
    public int getItemCount() {
        return mDevices.size();
    }

    @Override // DeviceViewHolder.IDeviceViewHolder
    public void onClickItem(int position,BluetoothDevice device) {
        if (mSelectedItem == position) {
            mSelectedItem = ITEM_NULL;
        } else {
            int previousItem = mSelectedItem;
            mSelectedItem = position;
            notifyItemChanged(previousItem);
        }
        notifyItemChanged(position);
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
        if(!hasAdd) {
            mDevices.add(null);
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

    /**
     * To completely reset the data set list and clear it completely.
     */
    public void reset() {
//        mDevices.clear();
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
        notifyDataSetChanged();
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