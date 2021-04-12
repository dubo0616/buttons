package com.gaia.button.holders;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gaia.button.R;

public class TopDeviceViewHolder extends RecyclerView.ViewHolder {

    /**
     * The text view to display the device name.
     */
    public TextView textViewDeviceName, connect;
    public ImageView imageView;
    public LinearLayout mLL;
    public ImageView mNodata;
    public View itemView;

    public TopDeviceViewHolder(@NonNull View rowView) {
        super(rowView);
        itemView = rowView;
        textViewDeviceName = (TextView) rowView.findViewById(R.id.tv_name);
        imageView = (ImageView) rowView.findViewById(R.id.iv_img);
        mNodata = (ImageView) rowView.findViewById(R.id.iv_no);
        connect = (TextView) rowView.findViewById(R.id.tv_connect);
        mLL = (LinearLayout) rowView.findViewById(R.id.ll_has);
    }


}

