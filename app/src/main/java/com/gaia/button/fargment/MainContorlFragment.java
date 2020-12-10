package com.gaia.button.fargment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.gaia.button.R;
import com.gaia.button.activity.DeviceDiscoveryActivity;
import com.gaia.button.activity.InformationActivity;
import com.gaia.button.activity.ServiceActivity;
import com.gaia.button.activity.UpgradeActivity;
import com.gaia.button.adapter.DevicesListAdapter;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.models.gatt.GATTServices;
import com.gaia.button.receivers.BREDRDiscoveryReceiver;
import com.gaia.button.receivers.BluetoothStateReceiver;
import com.gaia.button.services.BluetoothService;
import com.gaia.button.services.GAIABREDRService;
import com.gaia.button.services.GAIAGATTBLEService;
import com.gaia.button.utils.Consts;
import com.gaia.button.utils.DensityUtil;
import com.gaia.button.view.ArcSeekBarInner;
import com.gaia.button.view.ArcSeekBarOutter;
import com.gaia.button.view.GaiaPop;

import java.lang.ref.WeakReference;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;

public class MainContorlFragment extends BaseFragment implements BREDRDiscoveryReceiver.BREDRDiscoveryListener, BluetoothStateReceiver.BroadcastReceiverListener {
    private View mRootView;
    private ArcSeekBarInner mArcSeekBarInner;
    private ArcSeekBarOutter mArcSeekBarOutter;
    private ImageView mVoicePlus, mVoiceDe;
    private final BREDRDiscoveryReceiver mDiscoveryReceiver = new BREDRDiscoveryReceiver(this);
    /**
     * The tag to use for the logs.
     */
    private static final String TAG = "ServiceActivity";
    /**
     * The BLE service to communicate with any device.
     */
    BluetoothService mService;
    /**
     * The service connection object to manage the service bind and unbind.
     */
    private final ServiceConnection mServiceConnection = new ActivityServiceConnection(MainContorlFragment.this);
    /**
     * The handler used by the service to be linked to this activity.
     */
    private ActivityHandler mServiceHandler;
    /**
     * To know if this activity is in the pause state.
     */
    private boolean mIsPaused;
    private int mProgress;

    private ConstraintLayout mDeviceUpdate;
    private ConstraintLayout mDeviceInfo;
    private ConstraintLayout mDeviceReset;
    private ConstraintLayout mDeviceContorl;
    private TextView mTvContorlName;
    private TextView mStandard;
    private TextView mNoise;
    private TextView mAmbient;
    private ImageView mImageButtonIcon;
    private ImageView mImageViewWhiteBg,mImageViewGrayBg;
    private TextView mTvBatty,mTvScan,mTvConectDeviceName;
    private ImageView mIvSwitch;
    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

        }
    };
    private final Handler mHandler = new Handler();
    /**
     * To know if the scan is running.
     */
    private boolean mIsScanning = false;

    private @BluetoothService.Transport
    int mTransport = BluetoothService.Transport.UNKNOWN;

    private AudioManager mAudioManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_contorl, container, false);
        }
        initView();
        return mRootView;
    }
    private GaiaPop mPop;
    private void initView() {
//        mScrollView = mRootView.findViewById(R.id.sl_scroll);
        mDeviceUpdate = mRootView.findViewById(R.id.cl_device_update);
        mDeviceInfo = mRootView.findViewById(R.id.cl_device_info);
        mDeviceReset = mRootView.findViewById(R.id.cl_device_reset);
        mDeviceContorl = mRootView.findViewById(R.id.cl_contorl_bottom);
        mTvContorlName = mRootView.findViewById(R.id.tv_contorl_name);
        mStandard = mRootView.findViewById(R.id.tv_standard);
        mImageButtonIcon = mRootView.findViewById(R.id.iv_device);
        mImageViewWhiteBg = mRootView.findViewById(R.id.iv_device_bg_white);
        mImageViewGrayBg = mRootView.findViewById(R.id.iv_device_bg);
        mTvBatty = mRootView.findViewById(R.id.tv_bttary);
        mTvScan = mRootView.findViewById(R.id.tv_scan);
        mTvConectDeviceName = mRootView.findViewById(R.id.tv_switch);
        mIvSwitch = mRootView.findViewById(R.id.iv_switch);
        mIvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DeviceDiscoveryActivity.class));
            }
        });
        mStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStandard.setSelected(true);
                mAmbient.setSelected(false);
                mNoise.setSelected(false);
            }
        });
        mNoise = mRootView.findViewById(R.id.tv_noise);
        mNoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStandard.setSelected(false);
                mAmbient.setSelected(false);
                mNoise.setSelected(true);
            }
        });
        mAmbient = mRootView.findViewById(R.id.tv_ambient);
        mAmbient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStandard.setSelected(false);
                mAmbient.setSelected(true);
                mNoise.setSelected(false);
            }
        });
        mDeviceContorl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPop == null) {
                    mPop = new GaiaPop(mContext, new GaiaPop.onItemClickListener() {
                        @Override
                        public void onItemClick(int position,String text) {
                            mTvContorlName.setText(text);
                        }
                    });
                }
                mPop.show(mTvContorlName,-DensityUtil.dip2px(mContext,10), DensityUtil.dip2px(mContext,25), Gravity.BOTTOM);
            }
        });
        mDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService != null && mService.isGaiaReady()){
                    startActivity(new Intent(mContext, InformationActivity.class));
                }else{
                    displayShortToast("设备未连接");
                }
            }
        });
        mDeviceUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService != null && mService.isGaiaReady()){
                    startActivity(new Intent(mContext, UpgradeActivity.class));
                }else{
                    displayShortToast("设备未连接");
                }
            }
        });
        mDeviceReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService != null && mService.isGaiaReady()){
                    startActivity(new Intent(mContext, UpgradeActivity.class));
                }else{
                    displayShortToast("设备未连接");
                }
            }
        });
        mArcSeekBarInner = mRootView.findViewById(R.id.arc_seek_bar);
        mArcSeekBarInner.setMaxValue(maxVoice);
        mArcSeekBarInner.setProgress(mProgress);
        mArcSeekBarOutter = mRootView.findViewById(R.id.arc_seek_bar1);
        mVoicePlus = mRootView.findViewById(R.id.iv_void_p);
        mVoicePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mService != null && mService.isGaiaReady()) {
                    mProgress = mProgress + 1;
                    mArcSeekBarInner.setProgress(mProgress == maxVoice ? maxVoice : mProgress);
                    mArcSeekBarOutter.setProgress(mProgress == maxVoice ? maxVoice : mProgress);
                    mAudioManager.adjustStreamVolume(AudioManager.FLAG_PLAY_SOUND, AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
//                }else{
//                    displayShortToast("设备未连接");
//                }
            }
        });
        mVoiceDe = mRootView.findViewById(R.id.iv_void_d);
        mVoiceDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mService != null && mService.isGaiaReady()) {
                    mProgress = mProgress - 1;
                    mArcSeekBarInner.setProgress(mProgress == minVoice ? minVoice : mProgress);
                    mArcSeekBarOutter.setProgress(mProgress == minVoice ? minVoice : mProgress);
                    mAudioManager.adjustStreamVolume(AudioManager.FLAG_PLAY_SOUND, AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
//                }else{
//                    displayShortToast("设备未连接");
//                }



            }
        });
        mProgress = mArcSeekBarInner.getProgress();
        mArcSeekBarInner.setOnProgressChangeListener(new ArcSeekBarInner.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(ArcSeekBarInner seekBar, int progress, boolean isUser) {
                if(mProgress >=progress){
                    mAudioManager.adjustStreamVolume(AudioManager.FLAG_PLAY_SOUND, AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                }else{
                    mAudioManager.adjustStreamVolume(AudioManager.FLAG_PLAY_SOUND, AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                }
                mProgress = progress;
                mArcSeekBarOutter.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(ArcSeekBarInner seekBar) {

            }

            @Override
            public void onStopTrackingTouch(ArcSeekBarInner seekBar) {

            }
        });
        connectDevice();
//        scanDevice();

    }
    private void hasNoDevice(){
        mImageButtonIcon.setVisibility(View.INVISIBLE);
        mArcSeekBarInner.setVisibility(View.INVISIBLE);
        mImageViewWhiteBg.setVisibility(View.INVISIBLE);
        mTvBatty.setVisibility(View.INVISIBLE);
        mTvScan.setVisibility(View.VISIBLE);
        mTvScan.setText("设备未连接…");
        mTvConectDeviceName.setVisibility(View.INVISIBLE);
    }
    private void scanDevice(){
        hasNoDevice();
        mImageViewGrayBg.setVisibility(View.INVISIBLE);
        mTvScan.setText("正在搜索设备…");
    }

    private void connectDevice(){
        mImageButtonIcon.setVisibility(View.VISIBLE);
        mArcSeekBarInner.setVisibility(View.VISIBLE);
        mImageViewWhiteBg.setVisibility(View.VISIBLE);
        mTvBatty.setVisibility(View.VISIBLE);;
        mTvScan.setVisibility(View.GONE);
        mTvConectDeviceName.setVisibility(View.VISIBLE);
    }

    private int maxVoice,minVoice;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        maxVoice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mProgress = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.e("TTT","mProgress==========="+mProgress);
        Log.e("TTT","maxVoice==========="+maxVoice);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            minVoice = mAudioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
        }

        init();
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Consts.TRANSPORT_KEY, BluetoothService.Transport.BLE);
        editor.putString(Consts.BLUETOOTH_ADDRESS_KEY, device.getAddress());
        editor.apply();
        if (mService == null) {
            startService();
        }

    }

    @Override
    public void onBluetoothDisabled() {
        checkEnableBt();
    }

    @Override
    public void onBluetoothEnabled() {
        scanDevices(true);
    }

    private void initService() {
        mService.addHandler(mServiceHandler);
        if (mService.getDevice() == null) {
            // get the bluetooth information
            SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);

            // get the device Bluetooth address
            String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
            boolean done = mService.connectToDevice(address);
            Log.e("TTTTT", "===========" + address);
            Log.e("TTTTT", "===========" + done);
        }
    }


    // Callback activated after the user responds to the enable Bluetooth dialogue.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Consts.ACTION_REQUEST_ENABLE_BLUETOOTH: {
                if (resultCode == RESULT_OK) {
                    onBluetoothEnabled();
                }
                break;
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // When the activity is resumed.
    @Override
    public void onResume() {
        super.onResume();
        mIsPaused = false;
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver();
        mContext.registerReceiver(mBluetoothStateReceiver, filter);
        checkEnableBt();
        if (mService != null) {
            initService();
        }
    }

    // When the activity is paused.
    @Override
    public void onPause() {
        super.onPause();
        mIsPaused = true;
        if (mBtAdapter != null && mBtAdapter.isEnabled()) {
            scanDevices(false);
        }
        mContext.unregisterReceiver(mBluetoothStateReceiver);
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
        } else if (mIsScanning) {
            mIsScanning = false;
            mHandler.removeCallbacks(mStopScanRunnable);
            //noinspection deprecation
            mBtAdapter.stopLeScan(mLeScanCallback);
            //noinspection UnusedAssignment
            boolean isDiscovering = mBtAdapter.cancelDiscovery();
        }
    }

    private boolean startService() {
        // get the bluetooth information
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);

        // get the device Bluetooth address
        String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
        if (address.length() == 0 || !BluetoothAdapter.checkBluetoothAddress(address)) {
            // no address, not possible to establish a connection
            return false;
        }

        // get the transport type
        int transport = sharedPref.getInt(Consts.TRANSPORT_KEY, BluetoothService.Transport.UNKNOWN);
        mTransport = transport == BluetoothService.Transport.BLE ? BluetoothService.Transport.BLE :
                transport == BluetoothService.Transport.BR_EDR ? BluetoothService.Transport.BR_EDR :
                        BluetoothService.Transport.UNKNOWN;
        if (mTransport == BluetoothService.Transport.UNKNOWN) {
            // transport unknown, not possible to establish a connection
            return false;
        }

        // get the service class to bind
        Class<?> serviceClass = mTransport == BluetoothService.Transport.BLE ? GAIAGATTBLEService.class :
                GAIABREDRService.class; // mTransport can only be BLE or BR EDR

        // bind the service
        Intent gattServiceIntent = new Intent(mContext, serviceClass);
        gattServiceIntent.putExtra(Consts.BLUETOOTH_ADDRESS_KEY, address); // give address to the service
        return mContext.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void handleMessageFromService(Message msg) {
        String handleMessage = "Handle a message from Bluetooth service: ";

        switch (msg.what) {
            case BluetoothService.Messages.CONNECTION_STATE_HAS_CHANGED:
                @BluetoothService.State int connectionState = (int) msg.obj;
//                refreshConnectionState(connectionState, true);
                if (DEBUG) {
                    String stateLabel = connectionState == BluetoothService.State.CONNECTED ? "CONNECTED"
                            : connectionState == BluetoothService.State.CONNECTING ? "CONNECTING"
                            : connectionState == BluetoothService.State.DISCONNECTING ? "DISCONNECTING"
                            : connectionState == BluetoothService.State.DISCONNECTED ? "DISCONNECTED"
                            : "UNKNOWN";
                    Log.d(TAG, handleMessage + "CONNECTION_STATE_HAS_CHANGED: " + stateLabel);
                }
//                if (connectionState == BluetoothService.State.CONNECTED) {
//                    getGeneralDeviceInformation();
//                }
                break;

            case BluetoothService.Messages.DEVICE_BOND_STATE_HAS_CHANGED:
                int bondState = (int) msg.obj;
//                refreshBondState(bondState, true);
                if (DEBUG) {
                    String bondStateLabel = bondState == BluetoothDevice.BOND_BONDED ? "BONDED"
                            : bondState == BluetoothDevice.BOND_BONDING ? "BONDING"
                            : "BOND NONE";
                    Log.d(TAG, handleMessage + "DEVICE_BOND_STATE_HAS_CHANGED: " + bondStateLabel);
                }
                break;

            case BluetoothService.Messages.GATT_SUPPORT:
                GATTServices gattServices = (GATTServices) msg.obj;
                if (!gattServices.gattServiceGaia.isSupported()) {
                    displayLongToast(R.string.toast_gaia_not_supported);
                }
//                if (mService != null && mService.isGattReady()) {
//                    enableGattFeatures(gattServices);
//                }
                if (DEBUG) Log.d(TAG, handleMessage + "GATT_SUPPORT");
                break;

            case BluetoothService.Messages.GAIA_PACKET:
                byte[] data = (byte[]) msg.obj;
//                mGaiaManager.onReceiveGAIAPacket(data);
                // no log as these will be logged by the GAIA manager
                break;

            case BluetoothService.Messages.GAIA_READY:
//                getGAIAFeatures();
                if (DEBUG) Log.d(TAG, handleMessage + "GAIA_READY");
                break;

            case BluetoothService.Messages.GATT_READY:
//                if (mService != null) {
//                    enableGattFeatures(mService.getGattSupport());
//                }
                if (DEBUG) Log.d(TAG, handleMessage + "GATT_READY");
                break;

            case BluetoothService.Messages.GATT_MESSAGE:
                @GAIAGATTBLEService.GattMessage int gattMessage = msg.arg1;
                Object content = msg.obj;
//                onReceiveGattMessage(gattMessage, content);
                if (DEBUG) Log.d(TAG, handleMessage + "GATT_MESSAGE > " + gattMessage);
                break;

            default:
                if (DEBUG)
                    Log.d(TAG, handleMessage + "UNKNOWN MESSAGE: " + msg.what);
                break;
        }
    }

    /**
     * <p>This method is called when the service has been bound to this activity.</p>
     */
    public void onServiceConnected() {

    }

    public void onServiceDisconnected() {

    }
    // ====== PRIVATE METHODS ======================================================================

    /**
     * To initialise objects used in this activity.
     */
    private void init() {
        mServiceHandler = new ActivityHandler(this);
        // Bluetooth adapter
        mBtAdapter = ((BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        // Register for broadcasts on BluetoothAdapter state change so that we can tell if it has been turned off.
        mBluetoothStateReceiver = new BluetoothStateReceiver(this);

    }

    /**
     * Display a dialog requesting Bluetooth to be enabled if it isn't already. Otherwise this method updates the
     * list to the list view. The list view needs to be ready when this method is called.
     */
    protected void checkEnableBt() {
        if (isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Consts.ACTION_REQUEST_ENABLE_BLUETOOTH);
        } else {
            onBluetoothEnabled();
        }
    }

    /**
     * The Broadcast receiver we used to have information about the Bluetooth state on the device.
     */
    private BroadcastReceiver mBluetoothStateReceiver;

    /**
     * To know if we are using the application in debug mode.
     */
    static final boolean DEBUG = Consts.DEBUG;
    /**
     * The instance of the Bluetooth adapter used to retrieve paired Bluetooth devices.
     */
    BluetoothAdapter mBtAdapter;

    private void stopScan() {
        scanDevices(false);
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
     * <p>This class is used to be informed of the connection state of the BLE service.</p>
     */
    private static class ActivityServiceConnection implements ServiceConnection {

        /**
         * The reference to this activity.
         */
        final WeakReference<MainContorlFragment> mActivity;

        /**
         * The constructor for this activity service connection.
         *
         * @param activity this activity.
         */
        ActivityServiceConnection(MainContorlFragment activity) {
            super();
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            MainContorlFragment parentActivity = mActivity.get();

            if (componentName.getClassName().equals(GAIAGATTBLEService.class.getName())) {
                parentActivity.mService = ((GAIAGATTBLEService.LocalBinder) service).getService();
            } else if (componentName.getClassName().equals(GAIABREDRService.class.getName())) {
                parentActivity.mService = ((GAIABREDRService.LocalBinder) service).getService();
            }

            if (parentActivity.mService != null) {
                parentActivity.initService();
                parentActivity.onServiceConnected(); // to inform subclass
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (componentName.getClassName().equals(GAIAGATTBLEService.class.getName())) {
                MainContorlFragment parentActivity = mActivity.get();
                parentActivity.mService = null;
                parentActivity.onServiceDisconnected(); // to inform subclass
            }
        }
    }

    private final Runnable mStopScanRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
        }
    };

    /**
     * <p>This method checks if Bluetooth is enabled on the phone.</p>
     *
     * @return true is the Bluetooth is enabled, false otherwise.
     */
    private boolean isBluetoothEnabled() {
        return mBtAdapter == null || !mBtAdapter.isEnabled();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.removeHandler(mHandler);
            mService = null;
            mContext.unbindService(mServiceConnection);
        }
    }

    private static class ActivityHandler extends Handler {

        /**
         * The reference to this activity.
         */
        final WeakReference<MainContorlFragment> mReference;

        /**
         * The constructor for this activity handler.
         *
         * @param activity this activity.
         */
        ActivityHandler(MainContorlFragment activity) {
            super();
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainContorlFragment activity = mReference.get();
            if (!activity.mIsPaused) {
                activity.handleMessageFromService(msg);
            }
        }
    }
}
