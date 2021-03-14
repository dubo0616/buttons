/* ************************************************************************************************
 * Copyright 2017 Qualcomm Technologies International, Ltd.                                       *
 **************************************************************************************************/

package com.gaia.button.fargment;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gaia.button.R;
import com.gaia.button.activity.DeviceDiscoveryActivity;
import com.gaia.button.adapter.DevicesListAdapter;
import com.gaia.button.adapter.DevicesListTabsAdapter;
import com.gaia.button.utils.Consts;

/**
 * A fragment to mange the display of a list of Bluetooth devices.
 */
public class DevicesListFragment extends BaseFragment implements DevicesListAdapter.IDevicesListAdapterListener {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_LIST_TYPE = "list_type";
    /**
     * The listener to trigger events from this fragment.
     */
    private DevicesListFragmentListener mListener;
    /**
     * To know which type of list is managed by this fragment:
     * {@link DevicesListTabsAdapter#SCANNED_LIST_TYPE SCANNED_LIST_TYPE} or
     * {@link DevicesListTabsAdapter#BONDED_LIST_TYPE BONDED_LIST_TYPE}.
     */
    private int mListType = -1;
    /**
     * The data set adapter for the Bluetooth devices managed here.
     */
    private DevicesListAdapter mDevicesListAdapter;
    /**
     * The layout which is in charge of the "pull to refresh" feature.
     */
    private SwipeRefreshLayout mRefreshLayout;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static DevicesListFragment newInstance(int type,DevicesListFragmentListener l) {
        DevicesListFragment fragment = new DevicesListFragment(l);
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    // default empty constructor, required for Fragment.
    public DevicesListFragment(DevicesListFragmentListener l) {
        this.mListener =l ;
    }

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.


    @Override // Fragment
    public void onResume() {
        super.onResume();
        registerReceiver();
        switch (mListType) {
            case DevicesListTabsAdapter.SCANNED_LIST_TYPE:
                mRefreshLayout.setRefreshing(true);
                mDevicesListAdapter.reset();
                mDevicesListAdapter.add(null,0);
                mListener.startScan(mDevicesListAdapter);
                break;
        }
        startService();

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }
    private void startService(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
        // get the device Bluetooth address
        String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
        if(!TextUtils.isEmpty(address)) {
            if (mListener != null && mListener instanceof MainContorlFragment) {
                MainContorlFragment fragment = (MainContorlFragment) mListener;
                fragment.startService();
            }
        }
    }
    /****
     * 发现新设备
     * @param device
     */
    @Override
    public void onDeviceFound(BluetoothDevice device) {
        startService();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListType = getArguments().getInt(ARG_LIST_TYPE);

        View rootView = inflater.inflate(R.layout.fragment_devices, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_devices_list);
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mDevicesListAdapter.reset();
                switch (mListType) {
                    case DevicesListTabsAdapter.SCANNED_LIST_TYPE:
                        mListener.startScan(mDevicesListAdapter);
                        break;

                    case DevicesListTabsAdapter.BONDED_LIST_TYPE:
                        mListener.getBondedDevices(mDevicesListAdapter);
                        break;
                }
            }
        });

        // use a linear layout manager for the recycler view
        LinearLayoutManager devicesListLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(devicesListLayoutManager);
        recyclerView.setHasFixedSize(true);

        // specify an adapter for the recycler view
        mDevicesListAdapter = new DevicesListAdapter(getActivity(),this);
        recyclerView.setAdapter(mDevicesListAdapter);
        rootView.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getView().findViewById(R.id.id_device).setVisibility(View.GONE);
            }
        });
        return rootView;
    }

    @Override // DevicesListAdapter.IDevicesListAdapterListener
    public void onItemSelected(boolean itemSelected,BluetoothDevice device) {
        mListener.onItemSelected(itemSelected,device);
    }
    /**
     * <p>To register the bond state receiver to be aware of any bond state change.</p>
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mDiscoveryReceiver, filter);
    }

    /**
     * <p>To unregister the bond state receiver when the application is stopped or we don't need it anymore.</p>
     */
    private void unregisterReceiver() {
        mContext.unregisterReceiver(mDiscoveryReceiver);
    }

    /**
     * <p>To know if this fragment has a device selected.</p>
     *
     * @return true if there is a selected device in its data set, false otherwise.
     */
    public boolean hasSelection() {
        return mDevicesListAdapter.hasSelection();
    }

    /**
     * <p>To inform the swipe refresh layout to stop to display the "on refresh" view.</p>
     */
    public void stopRefreshing() {
        mRefreshLayout.setRefreshing(false);
    }

    /**
     * <p>To get the selected device if there is one.</p>
     *
     * @return the selected device if there is one, null otherwise.
     */
    public BluetoothDevice getSelectedDevice() {
        return mDevicesListAdapter.getSelectedItem();
    }

    /**
     * <p>This method is called when this fragment is not in the view displayed by the user anymore.</p>
     * <p>The tabs view pager always keeps the previous, current and next fragments on, so the usual onResume and
     * onPause methods of Fragment are not called when this fragment is not displayed anymore. Unless it is not the
     * previous or the next anymore.</p>
     */
    @SuppressWarnings("EmptyMethod") // not need to be implemented at the moment
    public void onPauseFragment() {
    }

    /**
     * <p>This method is called when this fragment is displayed to the user.</p>
     * <p>The tabs view pager always keeps the previous, current and next fragments on, so the usual onResume and
     * onPause methods of Fragment are not called when this fragment is not displayed anymore. Unless it is not the
     * previous or the next anymore.</p>
     */
    @SuppressWarnings("EmptyMethod") // not need to be implemented at the moment
    public void onResumeFragment() {
    }

    /**
     * The listener triggered by events from this fragment.
      */
    public interface DevicesListFragmentListener {
        /**
         * <p>To ask to the listener to give the complete list of bonded devices.</p>
         *
         * @param mDevicesListAdapter
         *          The adapter the bonded devices have to be provided.
         */
        void getBondedDevices(DevicesListAdapter mDevicesListAdapter);

        /**
         * <p>To inform the listener that the user asked for the devices list to be refreshed.</p>
         *
         * @param mDevicesListAdapter
         *        The adapter to provide the devices.
         */
        void startScan(DevicesListAdapter mDevicesListAdapter);

        /**
         * <p>When the user selects or unselects an item this method is called.</p>
         *
         * @param selected
         *          true if a device had been selected, false otherwise.
         */
        void onItemSelected(boolean selected,BluetoothDevice device);
    }
}
