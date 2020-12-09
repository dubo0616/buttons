package com.gaia.button.fargment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gaia.button.receivers.BluetoothStateReceiver;
import com.gaia.button.utils.Consts;

import static android.app.Activity.RESULT_OK;

public class BaseFragment extends Fragment {

    protected  Context mContext;
    protected void searchKey(String key){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    // ====== PROTECTED METHODS ====================================================================

    /**
     * To display a long toast inside this activity
     *
     * @param textID
     *              The ID of the text to display from the strings file.
     */
    void displayLongToast(int textID) {
        Toast.makeText(mContext, textID, Toast.LENGTH_LONG).show();
    }

    /**
     * To display a long toast inside this activity
     *
     * @param text
     *              The text to display.
     */
    void displayLongToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

    /**
     * To display a short toast inside this activity
     *
     * @param text
     *              The text to display.
     */
    void displayShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }



    // ====== PUBLIC METHODS =======================================================================

}
