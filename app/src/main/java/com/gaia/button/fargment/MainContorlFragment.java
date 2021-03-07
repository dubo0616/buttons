package com.gaia.button.fargment;

import android.app.DownloadManager;
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
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.gaia.button.GaiaApplication;
import com.gaia.button.R;
import com.gaia.button.activity.DeviceDiscoveryActivity;
import com.gaia.button.activity.InformationActivity;
import com.gaia.button.activity.MainActivity;
import com.gaia.button.activity.PersonnalActivity;
import com.gaia.button.activity.ServiceActivity;
import com.gaia.button.activity.UpgradeActivity;
import com.gaia.button.adapter.DevicesListAdapter;
import com.gaia.button.adapter.DevicesListTabsAdapter;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.gaia.MainGaiaManager;
import com.gaia.button.gaia.RemoteGaiaManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.model.UpdateModel;
import com.gaia.button.models.gatt.GATTServices;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.services.BluetoothService;
import com.gaia.button.services.GAIABREDRService;
import com.gaia.button.services.GAIAGATTBLEService;
import com.gaia.button.utils.BaseUtils;
import com.gaia.button.utils.Config;
import com.gaia.button.utils.Consts;
import com.gaia.button.utils.DensityUtil;
import com.gaia.button.utils.Utils;
import com.gaia.button.view.ArcSeekBarInner;
import com.gaia.button.view.ArcSeekBarOutter;
import com.gaia.button.view.DeceiveInfoDialog;
import com.gaia.button.view.GaiaPop;
import com.gaia.button.view.GaiaSoundModePop;
import com.gaia.button.view.UpdateInfoDialog;
import com.qualcomm.qti.libraries.gaia.GAIA;
import com.qualcomm.qti.libraries.gaia.GaiaUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;
import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_GetFirmwareVersion;

public class MainContorlFragment extends BaseFragment implements MainGaiaManager.MainGaiaManagerListener , IUserListener ,DevicesListFragment.DevicesListFragmentListener{
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
    private ConstraintLayout mDeviceReset;
    private TextView mTvContorlName;
    private TextView mStandard;
    private TextView mNoise;
    private TextView mAmbient;
    private ImageView mImageButtonIcon;
    private ImageView mImageViewWhiteBg,mImageViewGrayBg,mPersonal;
    private TextView mTvBatty,mTvScan,mTvConectDeviceName;
    private ImageView mIvSwitch;

    private MainGaiaManager mGaiaManager;

    private DevicesListAdapter mDevicesAdapter;

    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.e("FUCKCC", "Received potential GAIA packet: " + GaiaUtils.getHexadecimalStringFromBytes(scanRecord));
            if (mDevicesAdapter != null && device != null
                    && device.getName() != null && device.getName().length() > 0 && device.getName().contains("BUTTONS")) {

                mDevicesAdapter.add(device, rssi);
            }

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
    private MainActivity mAct;


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
        mDeviceReset = mRootView.findViewById(R.id.cl_device_reset);
        mTvContorlName = mRootView.findViewById(R.id.tv_contorl_name);
        mStandard = mRootView.findViewById(R.id.tv_standard);
        mImageButtonIcon = mRootView.findViewById(R.id.iv_device);
        mImageViewWhiteBg = mRootView.findViewById(R.id.iv_device_bg_white);
        mImageViewGrayBg = mRootView.findViewById(R.id.iv_device_bg);
        mTvBatty = mRootView.findViewById(R.id.tv_bttary);
        mTvScan = mRootView.findViewById(R.id.tv_scan);
        mTvConectDeviceName = mRootView.findViewById(R.id.tv_switch);
        mPersonal = mRootView.findViewById(R.id.iv_persion);
        mPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonnalActivity.class);
                if(!TextUtils.isEmpty(getVersion())){
                    intent.putExtra("version",getVersion());
                }
                if(getConnectDevice() != null){
                    intent.putExtra("name",getConnectDevice().getName());
                    intent.putExtra("mac",getConnectDevice().getAddress());
                }
                startActivityForResult(intent,1000);
            }
        });
        mIvSwitch = mRootView.findViewById(R.id.iv_switch);
        if(PreferenceManager.getInstance().getAccountInfo() != null && !TextUtils.isEmpty(PreferenceManager.getInstance().getAccountInfo().getAvtorURL())){
            Glide.with(this).load(PreferenceManager.getInstance().getAccountInfo().getAvtorURL()).
                    apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.icon_personal)).into(mPersonal);
        }
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
                setPlayMode("2");
            }
        });
        mAmbient = mRootView.findViewById(R.id.tv_ambient);
        mAmbient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            if(isDeviceReady()){
                                mGaiaManager.sendPlayModeCommand(position+1);
//                                mGaiaManager.getPlayModeCommand(position+1);
//                                mGaiaManager.getControlCommand(position+1);
                            }
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
                            },5000) ;
                        }
                    });
                }
                mPop.show(mTvContorlName,-DensityUtil.dip2px(mContext,10), DensityUtil.dip2px(mContext,25), Gravity.BOTTOM);
            }
        });
        mDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeviceReady()) {
                    showDialog = true;
                   getInformationFromDevice();
                }else{
                    displayShortToast("设备未连接");
                }
            }
        });
        mDeviceReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDeviceReady()) {
                    UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(getActivity(), new UpdateInfoDialog.OnConfirmClickListener() {
                        @Override
                        public void onConfirm() {
                                if(isDeviceReady()) {
                                    mGaiaManager.setDeviceReset();
                                }
                        }
                    });
                    infoDialog.show();
                    infoDialog.setData("设备重置","设备的所有设置将初始化,是否确认","");

                }else{
                    displayShortToast("设备未连接");
                }
            }
        });
        mDeviceUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BaseUtils.isWifiConnected(getActivity()) && (PreferenceManager.getInstance().getAccountInfo() != null && PreferenceManager.getInstance().getAccountInfo().getMobile_network() ==0)){
                    UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(getContext(), new UpdateInfoDialog.OnConfirmClickListener() {
                        @Override
                        public void onConfirm() {
                            download();
                        }
                    });
                    infoDialog.show();
                    infoDialog.setData("提示","当前使用移动网络,确认下载吗？","");
                    return;
                }
                download();
            }
        });
        mArcSeekBarInner = mRootView.findViewById(R.id.arc_seek_bar);
        mArcSeekBarOutter = mRootView.findViewById(R.id.arc_seek_bar1);
        mVoicePlus = mRootView.findViewById(R.id.iv_void_p);
        mVoicePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = true;
                if(isDeviceReady()) {
                    if (mProgress >= maxVoice) {
                        mProgress = maxVoice;
                    }
                    mProgress = mArcSeekBarInner.getProgress() + 1;
                    mArcSeekBarInner.setProgress(mProgress == maxVoice ? maxVoice : mProgress);
                    mArcSeekBarOutter.setProgress(mProgress == maxVoice ? maxVoice : mProgress);
                    PreferenceManager.getInstance().setIntValue(PreferenceManager.CONNECT_VOICE, mProgress);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mProgress, 0);
                } else {
                    displayShortToast("设备未连接");
                }
            }
        });
        mVoiceDe = mRootView.findViewById(R.id.iv_void_d);
        mVoiceDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = true;
                if(isDeviceReady()) {
                    if (mProgress <= minVoice) {
                        mProgress = minVoice;
                    }
                    mProgress = mArcSeekBarInner.getProgress() - 1;
                    mArcSeekBarInner.setProgress(mProgress == minVoice ? minVoice : mProgress);
                    mArcSeekBarOutter.setProgress(mProgress == minVoice ? minVoice : mProgress);
                    PreferenceManager.getInstance().setIntValue(PreferenceManager.CONNECT_VOICE,mProgress);
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
                if (mProgress >= maxVoice) {
                    mProgress = maxVoice;
                }
                if (mProgress <= minVoice) {
                    mProgress = minVoice;
                }
                if(mArcSeekBarOutter != null) {
                    mArcSeekBarOutter.setProgress(mArcSeekBarInner.getProgress());
                }
                isClick = false;
            }

            @Override
            public void onStartTrackingTouch(ArcSeekBarInner seekBar) {
            }

            @Override
            public void onStopTrackingTouch(ArcSeekBarInner seekBar) {
                PreferenceManager.getInstance().setIntValue(PreferenceManager.CONNECT_VOICE,mArcSeekBarInner.getProgress());
                mArcSeekBarOutter.setProgress(mArcSeekBarInner.getProgress());
            }
        });

//        connectDevice();
        scanDevice();

    }
    private void download(){
        if(isDeviceReady()){
            UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(getActivity(), new UpdateInfoDialog.OnConfirmClickListener() {
                @Override
                public void onConfirm() {
                    //                    UserManager.getRequestHandler().requestAirUpdate(MainContorlFragment.this,mService.getDevice().getName(),getVersion());
//                            File file = new File("/storage/emulated/0/Download/WeiXin/ButtonsAirX_BT_FW_V1.3.1_20210204.bin");
//                            if(file.exists()){
//                                mService.enableUpgrade(true);
//                                mService.startUpgrade(file);
//                            }
                    startActivity(new Intent(mContext, UpgradeActivity.class));
                }
            });
            infoDialog.show();

//                    startActivity(new Intent(mContext, UpgradeActivity.class));
        }else{
            displayShortToast("设备未连接");
        }
    }
    private boolean isDeviceReady(){
        if(mService != null && mService.isGaiaReady()) {
            return true;
        }
        return false;
    }
    /****
     * 设置耳机模式降噪 环境音等
     * @param type
     */
    private void setPlayMode(String type){
        if(!isDeviceReady()){
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

    /****
     * 没有设备
     *
     */
    private void hasNoDevice(){
        mImageButtonIcon.setVisibility(View.INVISIBLE);
        mArcSeekBarInner.setVisibility(View.INVISIBLE);
        mImageViewWhiteBg.setVisibility(View.INVISIBLE);
        mArcSeekBarInner.setProgress(0);
        mArcSeekBarOutter.setProgress(0);
        mTvBatty.setVisibility(View.INVISIBLE);
        mTvScan.setVisibility(View.VISIBLE);
        mImageViewGrayBg.setVisibility(View.GONE);
        mTvScan.setText("设备未连接…");
        mTvConectDeviceName.setVisibility(View.INVISIBLE);
        mAct.setPlayContorlLay(false);
        PreferenceManager.getInstance().setStringValue(PreferenceManager.CONNECT_ARRAESS,"");
    }

    /****
     * 正在扫描设备
     */
    private void scanDevice(){
        mAct.setPlayContorlLay(false);
        hasNoDevice();
        mImageViewGrayBg.setVisibility(View.INVISIBLE);
        mTvScan.setText("正在搜索设备…");
    }

    /***
     * 连接设备ui处理
     */

    private void connectDevice(){
        mImageButtonIcon.setVisibility(View.VISIBLE);
        mArcSeekBarInner.setVisibility(View.VISIBLE);
        mImageViewWhiteBg.setVisibility(View.VISIBLE);
        mImageViewGrayBg.setVisibility(View.VISIBLE);
        mTvBatty.setVisibility(View.VISIBLE);;
        mTvScan.setVisibility(View.GONE);
        mTvConectDeviceName.setVisibility(View.VISIBLE);
        if( mService != null && mService.getDevice()!= null) {
            if (!TextUtils.isEmpty(mService.getDevice().getName()) && (mService.getDevice().getName().endsWith("X") || mService.getDevice().getName().endsWith("x"))) {
                mRootView.findViewById(R.id.cl_center).setVisibility(View.VISIBLE);
                mImageButtonIcon.setImageResource(R.drawable.icon_airx);
            } else {
                mRootView.findViewById(R.id.cl_center).setVisibility(View.GONE);
                mImageButtonIcon.setImageResource(R.drawable.icon_air);
            }
        }else{
            mImageButtonIcon.setImageResource(R.drawable.icon_airx);
        }
    }

    private int maxVoice,minVoice;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAct =  (MainActivity) getActivity();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        maxVoice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            minVoice = mAudioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
        }
        myRegisterReceiver();
        init();
    }

    /****
     * 发现新设备
     * @param device
     */
    @Override
    public void onDeviceFound(BluetoothDevice device) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
        // get the device Bluetooth address
        String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
        if(TextUtils.isEmpty(address)){
//            initDevice();
            return;
        }
        if(device != null && device.getAddress() != null && device.getAddress().startsWith("F4:0E")){
            if(device.getBondState() == BluetoothDevice.BOND_NONE){
                Intent intent = new Intent(mContext, DeviceDiscoveryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,1000);
              return;
            }
            showWaitDialog("正在连接设备...");
            if (mService == null) {
                saveDevice(device);
                startService();
            }
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
//        if (mService == null) {
//            saveDevice(device);
//            startService();
//        }
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

    /****
     * 初始化设备通信服务
     */
    private void initService() {
        mService.addHandler(mServiceHandler);
        if (mService.getDevice() == null) {
            // get the bluetooth information
            SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
            // get the device Bluetooth address
            String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
            String name = sharedPref.getString(Consts.BLUETOOTH_NAME_KEY, "");
            boolean done = mService.connectToDevice(address);
        }else{
            getInformationFromDevice();

        }
    }
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
                    SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
                    // get the device Bluetooth address
                    String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
                    if (mService != null && mService.getDevice() != null && mService.getDevice().getAddress().equals(address)) {
                        return;
                    }
                    showWaitDialog("正在连接设备...");
                    if (mService != null && mService.getDevice() != null) {
                        mService.disconnectDevice();
                        mService.disconnectDevice();
                        mService.removeHandler(mHandler);
                        mService = null;
                        mContext.unbindService(mServiceConnection);
                    }
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
        if (mService != null) {
            initService();
        }
        if(mService == null || !mService.isGaiaReady()){
            hasNoDevice();
        }
        checkEnableBt();
        if(PreferenceManager.getInstance().getAccountInfo() != null && !TextUtils.isEmpty(PreferenceManager.getInstance().getAccountInfo().getAvtorURL())){
            Glide.with(this).load(PreferenceManager.getInstance().getAccountInfo().getAvtorURL()).
                    apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.icon_personal)).into(mPersonal);
        }
    }
    /****
     * 音量变化广播
     */
    private void myRegisterReceiver() {
        MyVolumeReceiver mVolumeReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        mContext.registerReceiver(mVolumeReceiver, filter);
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if(requestTag == Net_Tag_User_GetFirmwareVersion){
            if(data instanceof UpdateModel){
                UpdateModel model = (UpdateModel) data;
                if(model.getIsUpdate() == 1 && !TextUtils.isEmpty(model.getUrl())){
                    checkUpdate(model.getUrl());
                }
            }

        }

    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {

    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }

    @Override
    public void getBondedDevices(DevicesListAdapter mDevicesListAdapter) {

    }

    @Override
    public void startScan(DevicesListAdapter mDevicesListAdapter) {
        mDevicesAdapter = mDevicesListAdapter;
        if(mDevicesAdapter != null) {
            getBondedDevices(mDevicesAdapter);
        }
        scanDevices(true);
    }

    @Override
    public void onItemSelected(boolean selected, BluetoothDevice device) {

    }

    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                if(PreferenceManager.getInstance().getIntValue(PreferenceManager.CONNECT_VOICE) != currVolume){
//                    currVolume = PreferenceManager.getInstance().getIntValue(PreferenceManager.CONNECT_VOICE);
//                }
                Log.e("UUUUU","currVolume==="+currVolume);
                isClick = true;
                mArcSeekBarInner.setProgress(currVolume);
                mArcSeekBarOutter.setProgress(currVolume);
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
            if (DEBUG) Log.i(TAG, "Start scan of LE devices: " + isScanning + " - start discovery of BR/EDR devices: " +
                    isDiscovering);
        } else if (mIsScanning) {
//            mDevicesListFragment.stopRefreshing();
            mIsScanning = false;
            mHandler.removeCallbacks(mStopScanRunnable);
            //noinspection deprecation
            mBtAdapter.stopLeScan(mLeScanCallback);
            //noinspection UnusedAssignment
            boolean isDiscovering = mBtAdapter.cancelDiscovery();
            if (DEBUG) Log.i(TAG, "Stop scan of LE devices - stop discovery of BR/EDR devices: " + isDiscovering);
        }
    }

    /***
     * 绑定耳机服务
     * @return
     */
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

    /****
     * 分发连接消息
     * @param msg
     */
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
                if(bondState == BluetoothDevice.BOND_BONDED ){
                    SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
                    // get the device Bluetooth address
                    String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
                    String name = sharedPref.getString(Consts.BLUETOOTH_NAME_KEY, "");
                    boolean done = mService.connectToDevice(address);
                    Log.e("VVVVV","address===sss==="+address);
                    if (mService == null) {
                        startService();
                    }

                }
                break;

            case BluetoothService.Messages.GAIA_PACKET:
                byte[] data = (byte[]) msg.obj;
                mGaiaManager.onReceiveGAIAPacket(data);
                break;

            case BluetoothService.Messages.GAIA_READY:
                hideWaitDialog();
                PreferenceManager.getInstance().setStringValue(PreferenceManager.CONNECT_ARRAESS,mService.getDevice().getAddress());
                if (DEBUG) Log.d(TAG, handleMessage + "GAIA_READY");
//                mGaiaManager.setRWCPMode(true);
                getInformationFromDevice();
                mAct.setPlayContorlLay(true);
                AccountInfo info = PreferenceManager.getInstance().getAccountInfo();
                if(info !=null){
                    if(info.getAutoplay() == 1){
                        mAct.setPlayContorl(false);
                        mGaiaManager.sendControlCommand(3);
                    }else{
                        mAct.setPlayContorl(true);
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

    /****
     * 播放控制 暂停上一首下一首
     * @param model
     * @return
     */
    public boolean sendControlCommand(int model){
        if(mService != null && mService.isGaiaReady()){
            mGaiaManager.sendControlCommand(model);
            return true;
        }
        return false;
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
            mArcSeekBarOutter.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(mProgress);
            mArcSeekBarOutter.setProgress(mProgress);
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
            mArcSeekBarOutter.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(0);
            mArcSeekBarOutter.setProgress(0);
//            connectDevice();
            hasNoDevice();
        }


    }
    private void refreshConnectionState(@BluetoothService.State int state) {
        hideWaitDialog();
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);

        String name = sharedPref.getString(Consts.BLUETOOTH_NAME_KEY, "");

        if (state == BluetoothService.State.CONNECTED) {
            if (mTvConectDeviceName != null) {
                mTvConectDeviceName.setText(name);
            }
            mArcSeekBarInner.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(PreferenceManager.getInstance().getIntValue(PreferenceManager.CONNECT_VOICE));
            mArcSeekBarOutter.setMaxValue(maxVoice);
            mArcSeekBarOutter.setProgress(PreferenceManager.getInstance().getIntValue(PreferenceManager.CONNECT_VOICE));
            String text = PreferenceManager.getInstance().getPlaySoundMode(PreferenceManager.getInstance().getAccountInfo().getUserID()+mService.getDevice().getAddress());
            if(!TextUtils.isEmpty(text)) {
                mTvContorlName.setText(text);
            }
            connectDevice();
        }else {
            hideWaitDialog();
            if (mTvConectDeviceName != null) {
                mTvConectDeviceName.setText("");
            }
            mArcSeekBarInner.setMaxValue(maxVoice);
            mArcSeekBarOutter.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(0);
            mArcSeekBarOutter.setProgress(0);
            hasNoDevice();
        }
    }

    public void onServiceDisconnected() {
        hideWaitDialog();
        hasNoDevice();

    }

    /**
     * <p>This method requests all device information which are displayed in this activity such as the RSSI or battery
     * levels, the API version, etc.</p>
     */
    private void getInformationFromDevice() {
        if (isDeviceReady()) {
            mGaiaManager.getInformation(MainGaiaManager.Information.BATTERY);
            mGaiaManager.getInformation(MainGaiaManager.Information.API_VERSION);
            mGaiaManager.getANC();
            mGaiaManager.getControlCommand();
            mGaiaManager.getPlayStatus();
            mGaiaManager.getPlayModeCommand();
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
        Log.e("HHHH","======="+level);
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
    public void getAncResult(boolean result) {
        mNoise.setSelected(result);
        mANcResult = result;
        onStandard();

    }
    private void onStandard(){
        if(!mbientResult && !mANcResult){
            mStandard.setSelected(true);
        }
    }
    boolean mbientResult = true;
    boolean mANcResult = true;
    @Override
    public void getAmbientResult(boolean result) {
        mAmbient.setSelected(result);
        mbientResult = result;
        onStandard();
    }

    @Override
    public void getPlayStatus(boolean result) {
        mAct.setPlayContorl(!result);
    }

    @Override
    public void getPlayModle(int result) {
        Log.e("HHHHHHHHH","=========="+result);
        switch (result){
            case 0:
                mTvContorlName.setText("均衡");
                break;
            case 1:
                mTvContorlName.setText("爵士");
                break;
            case 2:
                mTvContorlName.setText("流行");
                break;
            case 3:
                mTvContorlName.setText("摇滚");
                break;
        }
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
            mService.disconnectDevice();
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
    private boolean isLoading = false;

    /****
     * 检查固件升级
     * @param url
     */
    private void checkUpdate(String url){
        if (!isLoading) {
            isLoading = true;

            String apkName = Config.splitFilePath(url);
            int apkIndex = apkName.indexOf(".bin");
            if(apkIndex < 0){
                displayShortToast("文件格式错误");
                return;
            }
            final String fileName = apkName.substring(0, apkIndex) + ".bin";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName); // Environment.getExternalStoragePublicDirectory(String)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            // in order for this if to run, you must use the android 3.2 to
            // compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }

            DownloadManager  mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

            final long enqueue = mDownloadManager.enqueue(request);
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    isLoading = false;
                    File file = new File(Environment.DIRECTORY_DOWNLOADS + "/" + fileName);
                    if(mService != null && mService.isGaiaReady()) {
                        showWaitDialog("升级中...");
                        mService.enableUpgrade(true);
                        mService.startUpgrade(file);
                    }
                }
            };
            mContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } else {
            displayShortToast("正在下载...");
        }
    }

    private DevicesListFragment mDevicesListFragment;

    /****
     * 初始化设备扫描fragment
     */
    private void initDevice(){
        mDevicesListFragment = DevicesListFragment.newInstance(DevicesListTabsAdapter.SCANNED_LIST_TYPE);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.id_device, mDevicesListFragment);
        transaction.commitAllowingStateLoss();
    }
}
