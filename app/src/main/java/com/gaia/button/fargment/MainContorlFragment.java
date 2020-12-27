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
import android.provider.Settings;
import android.text.TextUtils;
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
import com.gaia.button.gaia.MainGaiaManager;
import com.gaia.button.gaia.RemoteGaiaManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.models.gatt.GATTServices;
import com.gaia.button.receivers.BREDRDiscoveryReceiver;
import com.gaia.button.receivers.BluetoothStateReceiver;
import com.gaia.button.services.BluetoothService;
import com.gaia.button.services.GAIABREDRService;
import com.gaia.button.services.GAIAGATTBLEService;
import com.gaia.button.utils.Consts;
import com.gaia.button.utils.DensityUtil;
import com.gaia.button.utils.Utils;
import com.gaia.button.view.ArcSeekBarInner;
import com.gaia.button.view.ArcSeekBarOutter;
import com.gaia.button.view.DeceiveInfoDialog;
import com.gaia.button.view.GaiaPop;
import com.gaia.button.view.GaiaSoundModePop;
import com.qualcomm.qti.libraries.gaia.GAIA;

import java.lang.ref.WeakReference;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;

public class MainContorlFragment extends BaseFragment implements MainGaiaManager.MainGaiaManagerListener {
    private View mRootView;
    private ArcSeekBarInner mArcSeekBarInner;
    private ArcSeekBarOutter mArcSeekBarOutter;
    private ImageView mVoicePlus, mVoiceDe;

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
    private final ServiceConnection mServiceConnection = new ActivityServiceConnection(this);
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
    private ConstraintLayout mDeviceContorl;
    private TextView mTvContorlName;
    private TextView mStandard;
    private TextView mNoise;
    private TextView mAmbient;
    private ImageView mImageButtonIcon;
    private ImageView mImageViewWhiteBg,mImageViewGrayBg;
    private TextView mTvBatty,mTvScan,mTvConectDeviceName;
    private ImageView mIvSwitch;

    private MainGaiaManager mGaiaManager;

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

    private @BluetoothService.Transport int mTransport = BluetoothService.Transport.UNKNOWN;

    private AudioManager mAudioManager;
    private boolean isClick =false;

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
    private GaiaSoundModePop mSoundPop;
    private void initView() {
//        mScrollView = mRootView.findViewById(R.id.sl_scroll);
        mDeviceUpdate = mRootView.findViewById(R.id.cl_device_update);
        mDeviceInfo = mRootView.findViewById(R.id.cl_device_info);
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
                startActivityForResult(new Intent(mContext, DeviceDiscoveryActivity.class),1000);
            }
        });
        mStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mService != null && mService.getDevice() != null) {
                    PreferenceManager.getInstance().setPlaymode(PreferenceManager.getInstance().getAccountInfo().getUserID(), 1);
                }
                setPlayMode("1");
            }
        });
        mNoise = mRootView.findViewById(R.id.tv_noise);
        mNoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mService != null && mService.getDevice() != null) {
                    PreferenceManager.getInstance().setPlaymode(PreferenceManager.getInstance().getAccountInfo().getUserID(), 2);
                }
                setPlayMode("2");
            }
        });
        mAmbient = mRootView.findViewById(R.id.tv_ambient);
        mAmbient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mService != null && mService.getDevice() != null) {
                    PreferenceManager.getInstance().setPlaymode(PreferenceManager.getInstance().getAccountInfo().getUserID(), 3);
                }
                setPlayMode("3");
            }
        });
        String type = PreferenceManager.getInstance().getPlaymode(PreferenceManager.getInstance().getAccountInfo().getUserID());
        setPlayMode(type);

        mDeviceContorl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPop == null) {
                    mPop = new GaiaPop(mContext, new GaiaPop.onItemClickListener() {
                        @Override
                        public void onItemClick(int position,String text) {
                            mTvContorlName.setText(text);
                            if(mSoundPop == null){
                                mSoundPop = new GaiaSoundModePop(getActivity());
                            }
                            mSoundPop.setSoundStyle(position);
                            mSoundPop.show(mDeviceContorl,0,-DensityUtil.dip2px(mContext,200),Gravity.TOP);
                            mDeviceContorl.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(mSoundPop != null) {
                                        mSoundPop.disMiss();
                                    }
                                }
                            },3000) ;
                        }
                    });
                }
                mPop.show(mTvContorlName,-DensityUtil.dip2px(mContext,10), DensityUtil.dip2px(mContext,25), Gravity.BOTTOM);
            }
        });
        mDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mService != null && mService.getConnectionState() == BluetoothService.State.CONNECTED && mService.isGaiaReady()) {
                    showDialog = true;
                   getInformationFromDevice();
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
        mArcSeekBarInner = mRootView.findViewById(R.id.arc_seek_bar);
        mArcSeekBarOutter = mRootView.findViewById(R.id.arc_seek_bar1);
        mVoicePlus = mRootView.findViewById(R.id.iv_void_p);
        mVoicePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = true;
//                if(mService != null && mService.isGaiaReady()) {
                    mProgress = mArcSeekBarInner.getProgress() + 1;
                    mArcSeekBarInner.setProgress(mProgress == maxVoice ? maxVoice : mProgress);
                    mArcSeekBarOutter.setProgress(mProgress == maxVoice ? maxVoice : mProgress);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mProgress, 0);
//                }else{
//                    displayShortToast("设备未连接");
//                }
            }
        });
        mVoiceDe = mRootView.findViewById(R.id.iv_void_d);
        mVoiceDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = true;
                if(mService != null && mService.isGaiaReady()) {
                    mProgress = mArcSeekBarInner.getProgress() - 1;
                    mArcSeekBarInner.setProgress(mProgress == minVoice ? minVoice : mProgress);
                    mArcSeekBarOutter.setProgress(mProgress == minVoice ? minVoice : mProgress);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mProgress, 0);                }else{
                    displayShortToast("设备未连接");
                }
            }
        });
        mArcSeekBarInner.setOnProgressChangeListener(new ArcSeekBarInner.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(ArcSeekBarInner seekBar, int progress, boolean isUser) {
                if(!isClick) {
                    if (mProgress >= progress) {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mProgress, 0);
                    } else {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mProgress, 0);
                    }
                }
                mProgress = progress;
                mArcSeekBarOutter.setProgress(progress);
                isClick = false;
            }

            @Override
            public void onStartTrackingTouch(ArcSeekBarInner seekBar) {

            }

            @Override
            public void onStopTrackingTouch(ArcSeekBarInner seekBar) {

            }
        });

        iv_paly_pause = mRootView.findViewById(R.id.iv_paly_pause);
        iv_paly_pause_small = mRootView.findViewById(R.id.iv_paly_pause_small);
        iv_pre = mRootView.findViewById(R.id.iv_pre);
        iv_next = mRootView.findViewById(R.id.iv_next);
        iv_small = mRootView.findViewById(R.id.iv_small);
        play_contorl = mRootView.findViewById(R.id.play_contorl);
        play_contorl_small = mRootView.findViewById(R.id.play_contorl_small);
        iv_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_paly_pause_small.setSelected(iv_paly_pause.isSelected());
                play_contorl_small.setVisibility(View.VISIBLE);
                play_contorl.setVisibility(View.GONE);
            }
        });
        play_contorl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        iv_paly_pause_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService != null && mService.isGaiaReady()){
                    mGaiaManager.sendControlCommand(iv_paly_pause_small.isSelected() ? 3 : 4);
                }
                iv_paly_pause_small.setSelected(!iv_paly_pause_small.isSelected());
            }
        });
        iv_paly_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService != null && mService.isGaiaReady()){
                    mGaiaManager.sendControlCommand(iv_paly_pause.isSelected() ? 3 : 4);
                }
                iv_paly_pause.setSelected(!iv_paly_pause.isSelected());
            }
        });
        iv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService != null && mService.isGaiaReady()){
                    iv_paly_pause.setSelected(false);
                    mGaiaManager.sendControlCommand(1);
                }
            }
        });
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService != null && mService.isGaiaReady()){
                    iv_paly_pause.setSelected(false);
                    mGaiaManager.sendControlCommand(2);
                }
            }
        });
//        connectDevice();
        scanDevice();

    }
    private void setPlayMode(String type){
        if(mService== null || !mService.isGaiaReady()){
//            displayShortToast("设备未连接");
            return;
        }
        switch (type) {
            case "1":
                mAmbient.setSelected(false);
                mNoise.setSelected(false);
                if (mStandard.isSelected()) {
                    mStandard.setSelected(false);
                } else {
                    mStandard.setSelected(true);
                    mGaiaManager.setAncControl(false);
                    mGaiaManager.setAmbientControl(false);
                }


                break;
            case "2":
                mStandard.setSelected(false);
                mAmbient.setSelected(false);
                mNoise.setSelected(!mNoise.isSelected());
                mGaiaManager.setAncControl(mNoise.isSelected());
                break;
            case "3":
                mStandard.setSelected(false);
                mAmbient.setSelected(!mAmbient.isSelected());
                mNoise.setSelected(false);
                mGaiaManager.setAmbientControl(mAmbient.isSelected());
                break;
        }
    }
    private void hasNoDevice(){
        mImageButtonIcon.setVisibility(View.INVISIBLE);
        mArcSeekBarInner.setVisibility(View.INVISIBLE);
        mImageViewWhiteBg.setVisibility(View.INVISIBLE);
        mTvBatty.setVisibility(View.INVISIBLE);
        mTvScan.setVisibility(View.VISIBLE);
        mImageViewGrayBg.setVisibility(View.GONE);
        mTvScan.setText("设备未连接…");
        mTvConectDeviceName.setVisibility(View.INVISIBLE);
        play_contorl.setVisibility(View.GONE);
        PreferenceManager.getInstance().setStringValue(PreferenceManager.CONNECT_ARRAESS,"");
    }
    private void scanDevice(){
        play_contorl.setVisibility(View.GONE);
        hasNoDevice();
        mImageViewGrayBg.setVisibility(View.INVISIBLE);
        mTvScan.setText("正在搜索设备…");
    }

    private void connectDevice(){
        mImageButtonIcon.setVisibility(View.VISIBLE);
        mArcSeekBarInner.setVisibility(View.VISIBLE);
        mImageViewWhiteBg.setVisibility(View.VISIBLE);
        mImageViewGrayBg.setVisibility(View.VISIBLE);
        mTvBatty.setVisibility(View.VISIBLE);;
        mTvScan.setVisibility(View.GONE);
        mTvConectDeviceName.setVisibility(View.VISIBLE);
        play_contorl.setVisibility(View.VISIBLE);
    }

    private int maxVoice,minVoice;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        maxVoice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mProgress = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            minVoice = mAudioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
        }
        myRegisterReceiver();
        init();
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        if(device != null && device.getName() != null &&device.getName().contains("BUTTONS")){
            if(mBtAdapter != null && mBtAdapter.isEnabled()){
               Set set = mBtAdapter.getBondedDevices();
               if(!set.contains(device)){
                   device.createBond();
                   return;
               }
            }
//
        }


        if (mService == null) {
            saveDevice(device);
            startService();
        }else{
            mService.connectToDevice(device.getAddress());
        }

    }
    private void saveDevice(BluetoothDevice device){
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Consts.TRANSPORT_KEY, device.getType());
        editor.putString(Consts.BLUETOOTH_NAME_KEY, device.getName());
        editor.putString(Consts.BLUETOOTH_ADDRESS_KEY, device.getAddress());
        editor.apply();
    }

    @Override
    public void onDeviceConnectSuccess(BluetoothDevice device) {
        if (mService == null) {
            saveDevice(device);
            startService();
        }else{
            mService.connectToDevice(device.getAddress());
        }
    }

    @Override
    public void onBluetoothDisabled() {
        checkEnableBt();
    }

    @Override
    public void onBluetoothEnabled() {
        if (mService == null) {
            startService();
        }
        scanDevices(true);
    }

    private void initService() {
        mService.addHandler(mServiceHandler);
        Log.e("HHHH","=============");
        if (mService.getDevice() == null) {
            // get the bluetooth information
            SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
            // get the device Bluetooth address
            String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
            String name = sharedPref.getString(Consts.BLUETOOTH_NAME_KEY, "");
            boolean done = mService.connectToDevice(address);
        }else{
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
            case 1000:
                if (resultCode == RESULT_OK) {
                    startService();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // When the activity is resumed.
    @Override
    public void onResume() {
        super.onResume();
        mIsPaused = false;
        registerReceiver();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mBluetoothStateReceiver, filter);
        checkEnableBt();
        if (mService != null) {
            initService();
        }
    }
    private void myRegisterReceiver() {
        MyVolumeReceiver mVolumeReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        mContext.registerReceiver(mVolumeReceiver, filter);
    }

    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                isClick = true;
                mArcSeekBarInner.setProgress(currVolume);
                //int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            }
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
        Log.e("NNNN","1111111");
        // get the bluetooth information
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);

        // get the device Bluetooth address
        String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
        if (address.length() == 0 || !BluetoothAdapter.checkBluetoothAddress(address)) {
            // no address, not possible to establish a connection
            return false;
        }
//        Log.e("NNNN","2222222222");
//        if(mService != null && mService.getDevice()!=null && mService.isGaiaReady() && mService.getDevice().getAddress().equals(address)){
//            return false;
//        }
        Log.e("NNNN","2222222222");
        // get the transport type
        int transport = sharedPref.getInt(Consts.TRANSPORT_KEY, BluetoothService.Transport.UNKNOWN);
        mTransport = transport == BluetoothService.Transport.BLE ? BluetoothService.Transport.BLE :
                transport == BluetoothService.Transport.BR_EDR ? BluetoothService.Transport.BR_EDR :
                        BluetoothService.Transport.UNKNOWN;
        if (mTransport == BluetoothService.Transport.UNKNOWN) {
            // transport unknown, not possible to establish a connection
            return false;
        }
        Log.e("NNNN","2333333333");
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
//                onConnectionStateChanged(connectionState);
                refreshConnectionState(connectionState);
                if (DEBUG) {
                    String stateLabel = connectionState == BluetoothService.State.CONNECTED ? "CONNECTED"
                            : connectionState == BluetoothService.State.CONNECTING ? "CONNECTING"
                            : connectionState == BluetoothService.State.DISCONNECTING ? "DISCONNECTING"
                            : connectionState == BluetoothService.State.DISCONNECTED ? "DISCONNECTED"
                            : "UNKNOWN";
                    Log.d(TAG, handleMessage + "CONNECTION_STATE_HAS_CHANGED: " + stateLabel);
                }


                break;

            case BluetoothService.Messages.DEVICE_BOND_STATE_HAS_CHANGED:
                int bondState = (int) msg.obj;
                if (DEBUG) {
                    String bondStateLabel = bondState == BluetoothDevice.BOND_BONDED ? "BONDED"
                            : bondState == BluetoothDevice.BOND_BONDING ? "BONDING"
                            : "BOND NONE";
                    Log.d(TAG, handleMessage + "DEVICE_BOND_STATE_HAS_CHANGED: " + bondStateLabel);
                }
                break;

            case BluetoothService.Messages.GAIA_PACKET:
                byte[] data = (byte[]) msg.obj;
                mGaiaManager.onReceiveGAIAPacket(data);
                break;

            case BluetoothService.Messages.GAIA_READY:
                PreferenceManager.getInstance().setStringValue(PreferenceManager.CONNECT_ARRAESS,mService.getDevice().getAddress());
                if (DEBUG) Log.d(TAG, handleMessage + "GAIA_READY");
                mGaiaManager.setRWCPMode(true);
                mGaiaManager.getInformation(MainGaiaManager.Information.BATTERY);
                mGaiaManager.getInformation(MainGaiaManager.Information.API_VERSION);
                AccountInfo info = PreferenceManager.getInstance().getAccountInfo();
                if(info !=null){
                    if(info.getAutoplay() == 1){
                        iv_paly_pause.setSelected(false);
                        mGaiaManager.sendControlCommand(3);
                    }else{
                        iv_paly_pause.setSelected(true);
                        mGaiaManager.sendControlCommand(4);
                    }
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mProgress, 0);
                }
                break;

            case BluetoothService.Messages.GATT_READY:
                if (DEBUG) Log.d(TAG, handleMessage + "GATT_READY");
                break;

            case BluetoothService.Messages.GATT_MESSAGE:
                @GAIAGATTBLEService.GattMessage int gattMessage = msg.arg1;
                Object content = msg.obj;
                onReceiveGattMessage(gattMessage, content);
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
        @GAIA.Transport int transport = mTransport == BluetoothService.Transport.BR_EDR ?
                GAIA.Transport.BR_EDR : GAIA.Transport.BLE;
        mGaiaManager = new MainGaiaManager(this, transport);

        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);

        String name = sharedPref.getString(Consts.BLUETOOTH_NAME_KEY, "");

        if (mService!= null && mService.getConnectionState() == BluetoothService.State.CONNECTED) {
            if (mTvConectDeviceName != null) {
                mTvConectDeviceName.setText(name);
            }
            mArcSeekBarInner.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(maxVoice/2);
            String text = PreferenceManager.getInstance().getPlaySoundMode(PreferenceManager.getInstance().getAccountInfo().getUserID()+mService.getDevice().getAddress());
            if(!TextUtils.isEmpty(text)) {
                mTvContorlName.setText(text);
            }
            connectDevice();
        }else {
            if (mTvConectDeviceName != null) {
                mTvConectDeviceName.setText("");
            }
            mArcSeekBarInner.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(0);
            hasNoDevice();
        }


    }
    private void refreshConnectionState(@BluetoothService.State int state) {

        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);

        String name = sharedPref.getString(Consts.BLUETOOTH_NAME_KEY, "");

        if (state == BluetoothService.State.CONNECTED) {
            if (mTvConectDeviceName != null) {
                mTvConectDeviceName.setText(name);
            }
            mArcSeekBarInner.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(maxVoice/2);
            String text = PreferenceManager.getInstance().getPlaySoundMode(PreferenceManager.getInstance().getAccountInfo().getUserID()+mService.getDevice().getAddress());
            if(!TextUtils.isEmpty(text)) {
                mTvContorlName.setText(text);
            }
            connectDevice();
        }else {
            if (mTvConectDeviceName != null) {
                mTvConectDeviceName.setText("");
            }
            mArcSeekBarInner.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(0);
            hasNoDevice();
        }
    }

    public void onServiceDisconnected() {
        hasNoDevice();

    }

    /**
     * <p>This method requests all device information which are displayed in this activity such as the RSSI or battery
     * levels, the API version, etc.</p>
     */
    private void getInformationFromDevice() {
        if (mService != null && mService.getConnectionState() == BluetoothService.State.CONNECTED
                && mService.isGaiaReady()) {
            mGaiaManager.getInformation(MainGaiaManager.Information.API_VERSION);
        }
    }
    // ====== PRIVATE METHODS ======================================================================

    /**
     * To initialise objects used in this activity.
     */
    private void init() {
        sinit();
        mServiceHandler = new ActivityHandler(this);

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

    @Override // MainGaiaManager.MainGaiaManagerListener
    public boolean sendGAIAPacket(byte[] packet) {
        return mService!= null && mService.sendGAIAPacket(packet);
    }


    @Override
    public void onGetLedControl(boolean activate) {

    }

    @Override
    public void onFeatureSupported(int feature) {

    }

    @Override
    public void onInformationNotSupported(int information) {

    }

    @Override
    public void onChargerConnected(boolean isConnected) {

    }

    @Override
    public void onGetBatteryLevel(int level) {
        mBatteryLevel = level;
        refreshBatteryLevel();
    }

    @Override
    public void onGetRSSILevel(int level) {

    }

    DeceiveInfoDialog mDialog;
    private boolean showDialog;
    private String mVersion="";
    public String getVersion(){
        return mVersion;

    }
    public BluetoothDevice getConnectDevice(){
        if (mService != null && mService.getConnectionState() == BluetoothService.State.CONNECTED) {
            return  mService.getDevice();
        }
        return null;
    }
    @Override
    public void onGetAPIVersion(int versionPart1, int versionPart2, int versionPart3) {
        StringBuilder sb = new StringBuilder(versionPart1);
        sb.append(versionPart2);
        sb.append(versionPart3);
        mVersion = sb.toString();
        if(!showDialog){
            return;
        }
        if(mService != null && mService.getDevice() != null) {
            if (mDialog == null) {
                mDialog = DeceiveInfoDialog.getInstance(getActivity());
            }
            mDialog.show();
            mDialog.setData(mService.getDevice().getName(),mService.getDevice().getAddress(),mVersion);
            showDialog = false;

        }
    }

    @Override
    public void onFeaturesDiscovered() {

    }

    @Override
    public void onSetAncResult(boolean result) {
//        if(!result){
//            displayShortToast("设置失败");
//        }else{
//            displayShortToast("设置成功");
//        }
    }

    @Override
    public void onSetAmbientResult(boolean result) {
//        if(!result){
//            displayShortToast("设置失败");
//        }else{
//            displayShortToast("设置成功");
//        }
    }

    private void onReceiveGattMessage(@GAIAGATTBLEService.GattMessage int gattMessage, Object content) {
        if (gattMessage == GAIAGATTBLEService.GattMessage.RSSI_LEVEL) {
            int rssi = (int) content;
            onGetRSSILevel(rssi);
        }
        else if (gattMessage == GAIAGATTBLEService.GattMessage.GATT_STATE) {
            @GAIAGATTBLEService.GattState int state = (int) content;
            switch (state) {
                case GAIAGATTBLEService.GattState.IN_USE_BY_SYSTEM:
                    break;
                case GAIAGATTBLEService.GattState.DISCOVERING_SERVICES:
                    break;
            }
        }
    }
    private int mBatteryLevel = -1;
    private void refreshBatteryLevel() {
        if(mTvBatty != null){
            mTvBatty.setText(Utils.getBatteryLevel(mBatteryLevel));
        }

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
    private int count = 0;
    private void onConnectionStateChanged(int connectionState) {
        if(count >=3){
            return;
        }
        count++;
        if (connectionState == BluetoothService.State.DISCONNECTED) {
            mService.reconnectToDevice();
        }
    }
    private ImageView iv_paly_pause,iv_pre,iv_next,iv_small,iv_paly_pause_small;
    private ConstraintLayout play_contorl,play_contorl_small;

}
