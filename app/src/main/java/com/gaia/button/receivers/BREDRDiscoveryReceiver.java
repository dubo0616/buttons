/* ************************************************************************************************
 * Copyright 2017 Qualcomm Technologies International, Ltd.                                       *
 **************************************************************************************************/

package com.gaia.button.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * <p>This class allows reception of information from the system about Bluetooth devices which have been found during a
 * device discovery.</p>
 * <p>This receiver should be used with the following intent filter:
 * {@link BluetoothDevice#ACTION_FOUND ACTION_FOUND}.</p>
 */
public class BREDRDiscoveryReceiver extends BroadcastReceiver {
    /**
     * The listener to dispatch events from this receiver.
     */
    private final BREDRDiscoveryListener mListener;

    /**
     * <p>The constructor of this class.</p>
     *
     * @param listener The listener to inform of broadcast events from this receiver.
     */
    public BREDRDiscoveryReceiver(BREDRDiscoveryListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = device.getName();
            Log.d("aaa", "device name: " + name);
            int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
            switch (state) {
                case BluetoothDevice.BOND_NONE:
                    Log.d("aaa", "BOND_NONE 删除配对");
                    break;
                case BluetoothDevice.BOND_BONDING:
                    Log.d("aaa", "BOND_BONDING 正在配对");
                    break;
                case BluetoothDevice.BOND_BONDED:
                    if(mListener != null){
                        mListener.onDeviceConnectSuccess(device);
                    }
                    Log.d("aaa", "BOND_BONDED 配对成功");
                    break;
            }
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null) {
                mListener.onDeviceFound(device);
            }
        }
    }

    /**
     * <p>The listener for the {@link BREDRDiscoveryReceiver BREDRDiscoveryReceiver} receiver.</p>
     */
    public interface BREDRDiscoveryListener {
        /**
         * <p>The method to dispatch a found device to a listener of this receiver.</p>
         *
         * @param device
         *          The device which had been found.
         */
        void onDeviceFound(BluetoothDevice device);
        void onDeviceConnectSuccess(BluetoothDevice device);
    }

}
