package com.gaia.button.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaia.button.R;

public class GaiaPop {

    private PopupWindow popupWindow;
    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int position,String text);
    }
    public GaiaPop(Context context,final onItemClickListener mListener) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow_style, null);
        popupWindow = new PopupWindow(context);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(root);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        final TextView tvjh = root.findViewById(R.id.tv_junheng);
        tvjh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(0,tvjh.getText().toString());
                }
                popupWindow.dismiss();
            }
        });
        final TextView tvgd = root.findViewById(R.id.tv_gudian);
        tvgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(1,tvgd.getText().toString());
                }
                popupWindow.dismiss();
            }
        });
        final TextView tvlx = root.findViewById(R.id.tv_liuxing);
        tvlx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(2,tvlx.getText().toString());
                }
                popupWindow.dismiss();
            }
        });
        final TextView tvyg = root.findViewById(R.id.tv_yaogun);
        tvyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(3,tvyg.getText().toString());
                }
                popupWindow.dismiss();
            }
        });

    }

    public void show(View view, int x, int y, int graivty) {
        popupWindow.showAsDropDown(view, x, y, graivty);
    }


}
