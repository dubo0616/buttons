package com.gaia.button.fargment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gaia.button.receivers.BREDRDiscoveryReceiver;
import com.gaia.button.receivers.BluetoothStateReceiver;
import com.gaia.button.utils.Consts;
import com.gaia.button.view.ProgressDialogUtil;

public  class BaseFragment extends Fragment  implements BREDRDiscoveryReceiver.BREDRDiscoveryListener, BluetoothStateReceiver.BroadcastReceiverListener{

    protected  Context mContext;
    protected final BREDRDiscoveryReceiver mDiscoveryReceiver = new BREDRDiscoveryReceiver(this);
    protected void searchKey(String key){

    }
    /**
     * The instance of the Bluetooth adapter used to retrieve paired Bluetooth devices.
     */
    protected  BluetoothAdapter mBtAdapter;
    /**
     * The Broadcast receiver we used to have information about the Bluetooth state on the device.
     */
    protected BroadcastReceiver mBluetoothStateReceiver;

    /**
     * To know if we are using the application in debug mode.
     */
    static final boolean DEBUG = Consts.DEBUG;
    /**
     * The instance of the Bluetooth adapter used to retrieve paired Bluetooth devices.
     */
    protected void sinit() {
        // Bluetooth adapter
        mBtAdapter = ((BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        // Register for broadcasts on BluetoothAdapter state change so that we can tell if it has been turned off.
        mBluetoothStateReceiver = new BluetoothStateReceiver(this);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
//        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    @Override
    public void onDeviceFound(BluetoothDevice device) {
    }

    @Override
    public void onBluetoothDisabled() {
    }

    @Override
    public void onBluetoothEnabled() {
    }
    private ProgressDialogUtil mPd;
    private ProgressDialogUtil getWaitPd() {
        if (mPd == null) {
            mPd = new ProgressDialogUtil(getActivity(), "加载中...");
        }
        return mPd;
    }
    protected void showTotast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
    public void showWaitDialog() {
        showWaitDialog("加载中...");
    }

    public void showWaitDialog(String title) {
        if (!getActivity().isFinishing()) {
            if (mPd == null) {
                mPd = new ProgressDialogUtil(getActivity(), title);
            }
            if (!mPd.isShowing()) {
                mPd.showDialog();
            }
        }
    }

    public void showWaitDialog(boolean isCanceled) {
        if (!getActivity().isFinishing()) {
            if (mPd == null) {
                mPd = new ProgressDialogUtil(getActivity(), "加载中...");

            }
            if (!mPd.isShowing()) {
                mPd.showDialog();
                mPd.setCanceledOnTouchOutside(isCanceled);
            }
        }
    }

    public void hideWaitDialog() {
        if (mPd != null && mPd.isShowing()) {
            mPd.hideDialog();
        }
    }

    // ====== PUBLIC METHODS =======================================================================

}
