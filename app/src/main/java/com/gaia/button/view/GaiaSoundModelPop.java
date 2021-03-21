package com.gaia.button.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.gaia.button.R;
import com.gaia.button.utils.DensityUtil;

/****
 * 显示流行摇滚等图片
 */
public class GaiaSoundModelPop {
    private PopupWindow popupWindow;
    private ImageView mImageView;
    public GaiaSoundModelPop(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow_sound, null);
        mImageView=root.findViewById(R.id.iv_sound);
        popupWindow = new PopupWindow(context);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(DensityUtil.dip2px(context,150));
        popupWindow.setContentView(root);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
    }
    public void setSoundStyle(int style){
        switch (style){
            case 0:
                mImageView.setImageResource(R.drawable.icon_sound_jh);
                break;
            case 1:
                mImageView.setImageResource(R.drawable.icon_sound_gd);
                break;
            case 2:
                mImageView.setImageResource(R.drawable.icon_sound_lx);
                break;
            case 3:
                mImageView.setImageResource(R.drawable.icon_sound_yg);
                break;
        }
    }

    public void show(View view, int x, int y, int graivty) {
        popupWindow.showAsDropDown(view, x, y, graivty);
    }

    public void disMiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

}
