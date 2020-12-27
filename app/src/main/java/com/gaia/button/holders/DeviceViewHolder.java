/* ************************************************************************************************
 * Copyright 2017 Qualcomm Technologies International, Ltd.                                       *
 **************************************************************************************************/

package com.gaia.button.holders;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.gaia.button.R;
import com.gaia.button.utils.Utils;

import java.util.Locale;

///**
// * <p>This view holder represents a device item display. It is used in a Devices list to display and update the
// * information of a device for the layout {@link R.layout#list_devices_item list_devices_item}.</p>
// */
public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * The text view to display the device name.
     */
    private final TextView textViewDeviceName;
    private  ImageView image_item_selected;
    /**
     * The text view to display the device address.
     */
//    private final TextView textViewDeviceAddress;
    /**
     * The text view to display the RSSI for the device.
     */
//    private final TextView textViewDeviceRssi;
    /**
//     * The text view to display the type of the device.
//     */
//    private final TextView textViewDeviceType;

    /**
     * The instance of the parent to interact with it as a listener.
     */
    private final IDeviceViewHolder mListener;

    /**
     * <p>The constructor which will instantiate the views to use for this holder.</p>
     *
     * @param rowView
     *              The main view which contains all the views this holder should use.
     * @param listener
     *          The instance of the parent to interact with it as a listener.
     */
    public DeviceViewHolder(View rowView, IDeviceViewHolder listener) {
        super(rowView);
        textViewDeviceName = (TextView) rowView.findViewById(R.id.tv_device_name);
        image_item_selected = (ImageView) rowView.findViewById(R.id.image_item_selected);
        mListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override // View.OnClickListener
    public void onClick(View v) {
        mListener.onClickItem(this.getAdapterPosition());
    }

    public void refreshValues(String name, String address,boolean isSelected,
                              Context context) {
        textViewDeviceName.setText(name);
        if(name.contains("BUTTONS")) {
            if (name.endsWith("X") || name.endsWith("x")) {
                image_item_selected.setBackgroundResource(R.drawable.icon_airx);
            } else {
                image_item_selected.setBackgroundResource(R.drawable.icon_air);

            }
        }else{
            image_item_selected.setBackgroundResource(R.drawable.icon_air);
        }
    }
    /**
     * The interface to allow this class to interact with its parent.
     */
    public interface IDeviceViewHolder {
        /**
         * This method is called when the user clicks on the main view of an item.
         *
         * @param position
         *              The position of the item in the list.
         */
        void onClickItem(int position);
    }
}
