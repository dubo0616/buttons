/* ************************************************************************************************
 * Copyright 2018 Qualcomm Technologies International, Ltd.                                       *
 **************************************************************************************************/

package com.gaia.button.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gaia.button.R;
import com.gaia.button.adapter.DevicesListAdapter;
import com.gaia.button.adapter.DevicesListTabsAdapter;
import com.gaia.button.fargment.DevicesListFragment;
import com.gaia.button.receivers.BREDRDiscoveryReceiver;
import com.gaia.button.services.BluetoothService;
import com.gaia.button.utils.Consts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * This activity controls the scan of available BLE devices to connect with one of them. This activity also shows the
 * bonded devices to let the user choose the exact device he would like to use.
 * This activity is the start activity of the application and will initiate the connection with the device before
 * starting the next activity.
 */
public class DeviceDiscoveryActivity extends BluetoothActivity implements
        DevicesListFragment.DevicesListFragmentListener, BREDRDiscoveryReceiver.BREDRDiscoveryListener {

    /**
     * For debug mode, the tag to display for logs.
     */
    private final static String TAG = "DeviceDiscoveryActivity";
    private final Handler mHandler = new Handler();
    /**
     * To know if the scan is running.
     */
    private boolean mIsScanning = false;
    /**
     * The callback called when a device has been scanned by the LE scanner.
     */
    private final LeScanCallback mLeScanCallback = new LeScanCallback();
    /**
     * The adapter which should be informed about scanned devices.
     */
    private DevicesListAdapter mDevicesAdapter;
    /**
     * The runnable to trigger to stop the scan once the scanning time is finished.
     */
    private final Runnable mStopScanRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
        }
    };
    /**
     * The broadcast receiver in order to get devices which had been discovered during scanning using
     * {@link BluetoothAdapter#startDiscovery() startDiscovery()}.
     */
    private final BREDRDiscoveryReceiver mDiscoveryReceiver = new BREDRDiscoveryReceiver(this);

    private DevicesListFragment mDevicesListFragment;

    // ------ OVERRIDE METHODS ----------------------------------------------------------------------------------------

    @SuppressWarnings("EmptyMethod") // does not need to be implemented at the moment
    @Override // ModelActivity
    public void onBluetoothEnabled() {
        super.onBluetoothEnabled();
        // start scan?
    }

    @Override // DevicesListFragmentListener
    public void startScan(DevicesListAdapter adapter) {
        mDevicesAdapter = adapter;
        scanDevices(true);
    }

    @Override // DevicesListFragmentListener
    public void onItemSelected(boolean selected) {
        onConnectButtonClicked();
    }
    private void onConnectButtonClicked() {
        stopScan();
        BluetoothDevice device = mDevicesListFragment.getSelectedDevice();
        if(device == null){
            return;
        }
        // keep information
        SharedPreferences sharedPref = getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Consts.TRANSPORT_KEY, device.getType());
        editor.putString(Consts.BLUETOOTH_NAME_KEY, device.getName());
        editor.putString(Consts.BLUETOOTH_ADDRESS_KEY, device.getAddress());
        editor.apply();
        setResult(RESULT_OK);
        finish();
    }

    @Override // DevicesListFragmentListener
    public void getBondedDevices(DevicesListAdapter adapter) {
        Set<BluetoothDevice> listDevices;

        if (mBtAdapter != null && mBtAdapter.isEnabled()) {
            listDevices = mBtAdapter.getBondedDevices();
        } else {
            listDevices = Collections.emptySet();
        }

        ArrayList<BluetoothDevice> listBLEDevices = new ArrayList<>();

        for (BluetoothDevice device : listDevices) {
            if (device.getType() == BluetoothDevice.DEVICE_TYPE_DUAL
                    || device.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC
                    || device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                listBLEDevices.add(device);
            }
        }
        adapter.setListDevices(listBLEDevices);
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {

        if (mDevicesAdapter != null && device != null
                && device.getName() != null && device.getName().length() > 0) {
            mDevicesAdapter.add(device, 0);
        }
    }


    // ------ ACTIVITY METHODS ----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        init();
    }

    @Override
    protected void onResumeFragments() {
        registerReceiver();
        super.onResumeFragments();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBtAdapter != null && mBtAdapter.isEnabled()) {
            scanDevices(false);
        }
        unregisterReceiver();
    }


    private void scanDevices(boolean scan) {
        assert mBtAdapter != null;

        if (scan && !mIsScanning) {
            mIsScanning = true;
            mHandler.postDelayed(mStopScanRunnable, Consts.SCANNING_TIME);
            //noinspection deprecation,UnusedAssignment
            boolean isScanning = mBtAdapter.startLeScan(mLeScanCallback);
            //noinspection UnusedAssignment
            boolean isDiscovering = mBtAdapter.startDiscovery();
            if (DEBUG) Log.i(TAG, "Start scan of LE devices: " + isScanning + " - start discovery of BR/EDR devices: " +
                    isDiscovering);
        } else if (mIsScanning) {
            mDevicesListFragment.stopRefreshing();
            mIsScanning = false;
            mHandler.removeCallbacks(mStopScanRunnable);
            //noinspection deprecation
            mBtAdapter.stopLeScan(mLeScanCallback);
            //noinspection UnusedAssignment
            boolean isDiscovering = mBtAdapter.cancelDiscovery();
            if (DEBUG) Log.i(TAG, "Stop scan of LE devices - stop discovery of BR/EDR devices: " + isDiscovering);
        }
    }

    /**
     * <p>The method to call to stop the scan of available devices. This method will then call the
     * {@link DeviceDiscoveryActivity#scanDevices(boolean) scanDevices} method with "false" as the argument.</p>
     */
    private void stopScan() {
        scanDevices(false);
    }
    /**
     * <p>This method is used to initialize all view components which will be used in this activity.</p>
     */
    private void init() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDevicesListFragment = DevicesListFragment.newInstance(DevicesListTabsAdapter.SCANNED_LIST_TYPE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.layout_container, mDevicesListFragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * <p>To register the bond state receiver to be aware of any bond state change.</p>
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mDiscoveryReceiver, filter);
    }

    /**
     * <p>To unregister the bond state receiver when the application is stopped or we don't need it anymore.</p>
     */
    private void unregisterReceiver() {
        unregisterReceiver(mDiscoveryReceiver);
    }


    // ------ INNER CLASS ---------------------------------------------------------------------------------------------

    /**
     * Callback for scan results.
     */
    private class LeScanCallback implements BluetoothAdapter.LeScanCallback {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            if(mDevicesAdapter != null) {
                getBondedDevices(mDevicesAdapter);
            }
            if (mDevicesAdapter != null && device != null
                    && device.getName() != null && device.getName().length() > 0 && device.getName().contains("BUTTONS")) {
                mDevicesAdapter.add(device, rssi);
            }

        }
    }

}
