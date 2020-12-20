package com.gaia.button.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaia.button.R;


public class ProgressDialogUtil extends Dialog {
	
	private ImageView animImageView;
	private AnimationDrawable animationDrawable;
	private TextView animTextView;

    public ProgressDialogUtil(Context context, String msg) {
        super(context, R.style.loading_dialog);
        setContentView(R.layout.layout_dialog_loading);
        
        animImageView = (ImageView) findViewById(R.id.dialogImage);
        
        animTextView = (TextView) findViewById(R.id.dialogText);
        
        animationDrawable = (AnimationDrawable) animImageView.getDrawable();
        
//        this.setCanceledOnTouchOutside(true);
        
        if (!TextUtils.isEmpty(msg)) {
        	animTextView.setText(msg);
        }
        
    }
    
    public void showDialog(){
    	try {
    		if (animationDrawable != null) {
    			animationDrawable.start();
    		}
    		this.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void hideDialog(){
    	try {
    		if(getContext() == null) return;
    		if (animationDrawable != null) {
    			animationDrawable.stop();
    		}
    		this.dismiss();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    
}
