package com.gaia.button.fargment;

import android.app.DownloadManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.gaia.button.R;
import com.gaia.button.activity.MainActivity;
import com.gaia.button.activity.PersonnalActivity;
import com.gaia.button.activity.UpgradeActivity;
import com.gaia.button.adapter.DevicesListAdapter;
import com.gaia.button.adapter.DevicesListTabsAdapter;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.gaia.MainGaiaManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.model.UpdateModel;
import com.gaia.button.model.UpgradeModel;
import com.gaia.button.net.NetConfig;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.services.BluetoothService;
import com.gaia.button.services.GAIABREDRService;
import com.gaia.button.services.GAIAGATTBLEService;
import com.gaia.button.utils.BaseUtils;
import com.gaia.button.utils.Config;
import com.gaia.button.utils.Consts;
import com.gaia.button.utils.DensityUtil;
import com.gaia.button.utils.DeviceType;
import com.gaia.button.utils.LogUtil;
import com.gaia.button.utils.ParseBluetoothAdData;
import com.gaia.button.utils.PlayControl;
import com.gaia.button.utils.PlayModel;
import com.gaia.button.utils.Utils;
import com.gaia.button.view.ArcSeekBarInner;
import com.gaia.button.view.ArcSeekBarOutter;
import com.gaia.button.view.DeceiveInfoDialog;
import com.gaia.button.view.GaiaPop;
import com.gaia.button.view.GaiaSoundModelPop;
import com.gaia.button.view.UpdateInfoDialog;
import com.qualcomm.qti.libraries.gaia.GAIA;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;
import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_GetFirmwareVersion;

public class MainContorlFragment extends BaseFragment implements MainGaiaManager.MainGaiaManagerListener, IUserListener, DevicesListFragment.DevicesListFragmentListener {
    private View mRootView;
    private ArcSeekBarInner mArcSeekBarInner;
    private ArcSeekBarOutter mArcSeekBarOutter;
    private ImageView mVoicePlus, mVoiceDe;

    /**
     * The tag to use for the logs.
     */
    private static final String TAG = "Service Activity";
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
    private ImageView mImageViewWhiteBg, mImageViewGrayBg, mPersonal, mUpdatePoint;
    private TextView mTvBatty, mTvScan, mTvConectDeviceName;
    private ImageView mIvSwitch;

    private MainGaiaManager mGaiaManager;

    private DevicesListAdapter mDevicesAdapter;
//    private UpdateModel mUpdateModel;
    private UpgradeModel upgradeModel;

    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            ParseBluetoothAdData.AdData adData = ParseBluetoothAdData.INSTANCE.parse(scanRecord);
//            Log.e("AdData", "Received potential GAIA packet: " + GaiaUtils.getHexadecimalStringFromBytes(adData.getManufacturerByte()));
            if (mDevicesAdapter != null && BaseUtils.isButtonDevice(device)) {
                mDevicesAdapter.addScans(device, rssi);
            }

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
    private boolean isClick = false;
    private MainActivity mAct;
    private ConstraintLayout mDeviceFrgmentContainer;
    private int mDeviceType = DeviceType.DEFAULT.getType();
    private int maxVoice, minVoice;

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
    private GaiaSoundModelPop mSoundPop;

    private void initView() {
        mDeviceUpdate = mRootView.findViewById(R.id.cl_device_update);
        mDeviceFrgmentContainer = mRootView.findViewById(R.id.id_device);
        mUpdatePoint = mRootView.findViewById(R.id.iv_point);
        mUpdatePoint.setVisibility(View.GONE);
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
                if (!TextUtils.isEmpty(getVersion())) {
                    intent.putExtra("version", getVersion());
                }
                if (getConnectDevice() != null) {
                    intent.putExtra("name", getConnectDevice().getName());
                    intent.putExtra("mac", getConnectDevice().getAddress());
                }
                startActivityForResult(intent, 1000);
            }
        });
        mIvSwitch = mRootView.findViewById(R.id.iv_switch);
        if (PreferenceManager.getInstance().getAccountInfo() != null && !TextUtils.isEmpty(PreferenceManager.getInstance().getAccountInfo().getAvtorURL())) {
            Glide.with(this).load(PreferenceManager.getInstance().getAccountInfo().getAvtorURL()).
                    apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.icon_personal)).into(mPersonal);
        }
        mIvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeviceFrgmentContainer.setVisibility(View.VISIBLE);
            }
        });
        mStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mService != null && mService.getDevice() != null) {
                    PreferenceManager.getInstance().setPlaymode(PreferenceManager.getInstance().getAccountInfo().getUserID(), 1);
                }
                setPlayMode(PlayModel.Standard.getValue());
            }
        });
        mNoise = mRootView.findViewById(R.id.tv_noise);
        mNoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayMode(PlayModel.NOISE.getValue());
            }
        });
        mAmbient = mRootView.findViewById(R.id.tv_ambient);
        mAmbient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayMode(PlayModel.AMBIENT.getValue());
            }
        });
        mDeviceContorl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPop == null) {
                    mPop = new GaiaPop(mContext, new GaiaPop.onItemClickListener() {
                        @Override
                        public void onItemClick(int position, String text) {
                            if (isDeviceReady()) {
                                mGaiaManager.setEQModeCommand(position + 1);
                            }
                            mTvContorlName.setText(text);
                            if (mSoundPop == null) {
                                mSoundPop = new GaiaSoundModelPop(getActivity());
                            }
                            mSoundPop.setSoundStyle(position);
                            mSoundPop.show(mDeviceContorl, 0, -DensityUtil.dip2px(mContext, 200), Gravity.TOP);
                            mDeviceContorl.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mSoundPop != null) {
                                        mSoundPop.disMiss();
                                    }
                                }
                            }, 5 * 1000);
                        }
                    });
                }
                mPop.show(mTvContorlName, -DensityUtil.dip2px(mContext, 10), DensityUtil.dip2px(mContext, 25), Gravity.BOTTOM);
            }
        });
        mDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeviceReady()) {
                    showDialog = true;
                    getInformationFromDevice();
                } else {
                    displayShortToast("设备未连接");
                }
            }
        });
        mDeviceReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeviceReady()) {
                    UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(getActivity(), new UpdateInfoDialog.OnConfirmClickListener() {
                        @Override
                        public void onConfirm() {
                            if (isDeviceReady()) {
                                mGaiaManager.setDeviceReset();
                            }
                        }
                    });
                    infoDialog.show();
                    infoDialog.setData("设备重置", "设备的所有设置将初始化,是否确认", "");

                } else {
                    displayShortToast("设备未连接");
                }
            }
        });
        mDeviceUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BaseUtils.isWifiConnected(getActivity()) && (PreferenceManager.getInstance().getAccountInfo() != null && PreferenceManager.getInstance().getAccountInfo().getMobile_network() != 0)) {
                    UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(getContext(), new UpdateInfoDialog.OnConfirmClickListener() {
                        @Override
                        public void onConfirm() {
                            download();
                        }
                    });
                    infoDialog.show();
                    infoDialog.setData("提示", "当前使用移动网络,确认下载吗？", "");
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
                if (isDeviceReady()) {
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
                if (isDeviceReady()) {
                    if (mProgress <= minVoice) {
                        mProgress = minVoice;
                    }
                    mProgress = mArcSeekBarInner.getProgress() - 1;
                    mArcSeekBarInner.setProgress(mProgress == minVoice ? minVoice : mProgress);
                    mArcSeekBarOutter.setProgress(mProgress == minVoice ? minVoice : mProgress);
                    PreferenceManager.getInstance().setIntValue(PreferenceManager.CONNECT_VOICE, mProgress);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mProgress, 0);
                } else {
                    displayShortToast("设备未连接");
                }
            }
        });
        mArcSeekBarInner.setOnProgressChangeListener(new ArcSeekBarInner.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(ArcSeekBarInner seekBar, int progress, boolean isUser) {
                if (!isClick) {
                    if (mProgress >= progress) {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mProgress, 0);
                    } else {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mProgress, 0);
                    }
                }

                mProgress = progress;
                if (mProgress >= maxVoice) {
                    mProgress = maxVoice;
                }
                if (mProgress <= minVoice) {
                    mProgress = minVoice;
                }
                if (mArcSeekBarOutter != null) {
                    mArcSeekBarOutter.setProgress(mArcSeekBarInner.getProgress());
                }
                isClick = false;
            }

            @Override
            public void onStartTrackingTouch(ArcSeekBarInner seekBar) {
            }

            @Override
            public void onStopTrackingTouch(ArcSeekBarInner seekBar) {
                PreferenceManager.getInstance().setIntValue(PreferenceManager.CONNECT_VOICE, mArcSeekBarInner.getProgress());
                mArcSeekBarOutter.setProgress(mArcSeekBarInner.getProgress());
            }
        });

//        connectDevice();
        scanDevice();
        initDevice();

    }

    private void download() {
        if (isDeviceReady()) {
//            if (mUpdateModel == null || mUpdateModel.getIsUpdate() == 2) {
//                displayShortToast("已经是最新版本");
//                return;
//            }

            if (upgradeModel == null || Utils.compareVersion(upgradeModel.data.binVersion, mVersion) != 1) {
                displayShortToast("已经是最新版本");
                return;
            }
            UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(getActivity(), new UpdateInfoDialog.OnConfirmClickListener() {
                @Override
                public void onConfirm() {
//                    if (mUpdateModel != null) {
//                        if (mUpdateModel.getIsUpdate() == 1 && !TextUtils.isEmpty(mUpdateModel.getUrl())) {
//                            checkUpdate(mUpdateModel.getUrl());
//                        }
//                    }
                    if (upgradeModel != null) {
                        if (Utils.compareVersion(upgradeModel.data.binVersion, mVersion) == 1 && !TextUtils.isEmpty(upgradeModel.data.binPath)) {
//                        if (!TextUtils.isEmpty(upgradeModel.data.binPath)) {
//                            checkUpdate(upgradeModel.data.binPath);
//                        }
                        Intent intent = new Intent(mContext, UpgradeActivity.class);
                        intent.putExtra("onLineVersion", upgradeModel.data.binVersion);
                        intent.putExtra("deviceVersion", mVersion);
                        intent.putExtra("downUrl", upgradeModel.data.binPath);
                        intent.putExtra("upgradeDevice", upgradeModel.data.deviceName);
                        startActivity(intent);
                        }
                    }
                }
            });
            infoDialog.show();
        } else {
            displayShortToast("设备未连接");
        }
    }

    private boolean isDeviceReady() {
        if (mService != null && mService.isGaiaReady()) {
            return true;
        }
        return false;
    }

    /****
     * 设置耳机模式降噪 环境音等
     * @param type
     */
    private void setPlayMode(int type) {
        if (!isDeviceReady()) {
            displayShortToast("设备未连接");
            return;
        }
        switch (type) {
            case 1:
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
            case 2:
                mStandard.setSelected(false);
                mAmbient.setSelected(false);
                mNoise.setSelected(!mNoise.isSelected());
                mGaiaManager.setAncControl(mNoise.isSelected());
                break;
            case 3:
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
    private void hasNoDevice() {
        mImageButtonIcon.setVisibility(View.INVISIBLE);
        mArcSeekBarInner.setVisibility(View.INVISIBLE);
        mImageViewWhiteBg.setVisibility(View.INVISIBLE);
        mArcSeekBarInner.setProgress(0);
        mArcSeekBarOutter.setProgress(0);
        mTvBatty.setVisibility(View.INVISIBLE);
        mTvScan.setVisibility(View.VISIBLE);
        mImageViewGrayBg.setVisibility(View.GONE);
        mUpdatePoint.setVisibility(View.GONE);
        mAmbient.setSelected(false);
        mStandard.setSelected(false);
        mNoise.setSelected(false);
        if (mSoundPop != null) {
            mSoundPop.setSoundStyle(0);
        }
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mTvScan.setText("设备未连接…");
        mTvConectDeviceName.setVisibility(View.INVISIBLE);
        mAct.setPlayContorlLay(false);
        PreferenceManager.getInstance().setStringValue(PreferenceManager.CONNECT_ARRAESS, "");
        mDeviceType = DeviceType.DEFAULT.getType();
    }

    /****
     * 正在扫描设备
     */
    private void scanDevice() {
        mAct.setPlayContorlLay(false);
        hasNoDevice();
        mImageViewGrayBg.setVisibility(View.INVISIBLE);
        mTvScan.setText("正在搜索设备…");
    }

    /***
     * 连接设备ui处理
     */
    private void connectDevice() {
        mImageButtonIcon.setVisibility(View.VISIBLE);
        mArcSeekBarInner.setVisibility(View.VISIBLE);
        mImageViewWhiteBg.setVisibility(View.VISIBLE);
        mImageViewGrayBg.setVisibility(View.VISIBLE);
        mTvBatty.setVisibility(View.VISIBLE);
        mTvScan.setVisibility(View.GONE);
        mDeviceFrgmentContainer.setVisibility(View.GONE);
        mTvConectDeviceName.setVisibility(View.VISIBLE);
        updatePlayModelUI();
    }

    /****
     * 更新播放模式uI
     */
    private void updatePlayModelUI() {
        if (isDeviceReady()) {
            //5 airx
            if (mDeviceType == DeviceType.AIRX.getType()) {
                mRootView.findViewById(R.id.cl_center).setVisibility(View.VISIBLE);
                mImageButtonIcon.setImageResource(R.drawable.icon_airx);
            } else {
                mRootView.findViewById(R.id.cl_center).setVisibility(View.GONE);
                mImageButtonIcon.setImageResource(R.drawable.icon_air);
            }
        } else {
            mImageButtonIcon.setImageResource(R.drawable.icon_airx);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAct = (MainActivity) getActivity();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        maxVoice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            minVoice = mAudioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
        }
        myRegisterReceiver();
        init();
    }

    private void saveDevice(BluetoothDevice device) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Consts.TRANSPORT_KEY, device.getType());
        editor.putString(Consts.BLUETOOTH_NAME_KEY, device.getName());
        editor.putString(Consts.BLUETOOTH_ADDRESS_KEY, device.getAddress());
        editor.apply();
    }
    @Override
    public void onBluetoothDisabled() {
        checkEnableBt();
    }

    @Override
    public void onBluetoothEnabled() {
        if (mService == null) {
//            startService();
        }
//        scanDevices(true);
    }

    /****
     * 初始化设备通信服务
     */
    private void initService() {
        mService.addHandler(mServiceHandler);
        if (mService.getDevice() == null) {
            conntService();
        } else {
            getInformationFromDevice();

        }
    }
    private void conntService(){
        // get the bluetooth information
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);
        // get the device Bluetooth address
        String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
        String name = sharedPref.getString(Consts.BLUETOOTH_NAME_KEY, "");
        boolean done = mService.connectToDevice(address);
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
        mContext.registerReceiver(mBluetoothStateReceiver, filter);
        if (mService != null) {
            initService();
        }
        if (mService == null || !mService.isGaiaReady()) {
            hasNoDevice();
        }
        checkEnableBt();
        if (PreferenceManager.getInstance().getAccountInfo() != null && !TextUtils.isEmpty(PreferenceManager.getInstance().getAccountInfo().getAvtorURL())) {
            Glide.with(this).load(PreferenceManager.getInstance().getAccountInfo().getAvtorURL()).
                    apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.icon_personal)).into(mPersonal);
        }
    }
    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if (requestTag == Net_Tag_User_GetFirmwareVersion) {
//            if (data instanceof UpdateModel) {
//                mUpdateModel = (UpdateModel) data;
//                if (mUpdateModel != null && mUpdateModel.getIsUpdate() == 1) {
//                    mUpdatePoint.setVisibility(View.VISIBLE);
//                }
//            }
            if(data instanceof UpgradeModel){
                upgradeModel = (UpgradeModel) data;
                if(upgradeModel != null && Utils.compareVersion(upgradeModel.data.binVersion, mVersion) == 1){
                    mUpdatePoint.setVisibility(View.VISIBLE);
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
        Set<BluetoothDevice> listDevices = new HashSet<>();

        //todo 修改顶部设备列表，只获取当前已配对的设备，不显示配对的历史设备
        final Map<String, BluetoothDevice> audioConnected = getAudioConnectedAddress();
        for (BluetoothDevice b:audioConnected.values()) {
            listDevices.add(b);
        }
//        if (mBtAdapter != null && mBtAdapter.isEnabled()) {
//            listDevices = mBtAdapter.getBondedDevices();
//        } else {
//            listDevices = Collections.emptySet();
//        }
        ArrayList<BluetoothDevice> listBLEDevices = new ArrayList<>();

        for (BluetoothDevice device : listDevices) {
            if (!BaseUtils.isButtonDevice(device)) {
                continue;
            }
            if (device.getType() == BluetoothDevice.DEVICE_TYPE_DUAL
                    || device.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC
                    || device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                listBLEDevices.add(device);
            }
        }
        mDevicesListAdapter.setListDevices(listBLEDevices);
    }
    public Map<String, BluetoothDevice> getAudioConnectedAddress() {
        Map<String, BluetoothDevice> result = new HashMap<>();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(mBtAdapter, (Object[]) null);

            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Set<BluetoothDevice> devices = mBtAdapter.getBondedDevices();
                for (BluetoothDevice bondedDevice : devices) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(bondedDevice, (Object[]) null);
                    if (isConnected) {
                        result.put(bondedDevice.getAddress(), bondedDevice);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void startScan(DevicesListAdapter mDevicesListAdapter) {
        mDevicesAdapter = mDevicesListAdapter;
        if (mDevicesAdapter != null) {
            getBondedDevices(mDevicesAdapter);
        }
        scanDevices(true);
    }

    @Override
    public void onItemSelected(boolean selected, BluetoothDevice device) {
        if (device == null) {
            return;
        }
        saveDevice(device);
        if (isDeviceReady() && mService.getDevice().getAddress().equals(device.getAddress())) {
            showTotast("设备已连接");
            PreferenceManager.getInstance().setStringValue(PreferenceManager.CONNECT_ARRAESS, mService.getDevice().getAddress());
            String deviceCode;
            int type;
            if(device.getName().contains("X")){//5
                deviceCode = "AirX";
                type = 5;
            }else{
                deviceCode = "Air_V1";
                type = 4;
            }
            NetConfig.isGet = true;

            UserManager.getRequestHandler().requestAirUpdate(this, deviceCode, getVersion(), type);
            mDeviceFrgmentContainer.setVisibility(View.GONE);
            return;
        }
        showWaitDialog("正在连接设备...");
        if (mService != null && mService.getDevice() != null) {
            mService.disconnectDevice();
            mService.removeHandler(mHandler);
            mService = null;
            mContext.unbindService(mServiceConnection);
        }
        startService();
    }

    @Override
    protected void changeVolume() {
        int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                if(PreferenceManager.getInstance().getIntValue(PreferenceManager.CONNECT_VOICE) != currVolume){
//                    currVolume = PreferenceManager.getInstance().getIntValue(PreferenceManager.CONNECT_VOICE);
//                }
        if (isDeviceReady()) {
            isClick = true;
            mArcSeekBarInner.setProgress(currVolume);
            mArcSeekBarOutter.setProgress(currVolume);
        }
        super.changeVolume();
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
            mDevicesListFragment.stopRefreshing();
            mIsScanning = false;
            mHandler.removeCallbacks(mStopScanRunnable);
            //noinspection deprecation
            mBtAdapter.stopLeScan(mLeScanCallback);
            //noinspection UnusedAssignment
            boolean isDiscovering = mBtAdapter.cancelDiscovery();
        }
    }

    /***
     * 绑定耳机服务
     * @return
     */
    public boolean startService() {

        // get the bluetooth information
        SharedPreferences sharedPref = mContext.getSharedPreferences(Consts.PREFERENCES_FILE, Context.MODE_PRIVATE);

        // get the device Bluetooth address
        String address = sharedPref.getString(Consts.BLUETOOTH_ADDRESS_KEY, "");
        if (address.length() == 0 || !BluetoothAdapter.checkBluetoothAddress(address)) {
            // no address, not possible to establish a connection
            return false;
        }
        //todo   此处屏蔽设备类型判断，只用BE/EDR模式即可
        // get the transport type
//        int transport = sharedPref.getInt(Consts.TRANSPORT_KEY, BluetoothService.Transport.UNKNOWN);
//        mTransport = transport == BluetoothService.Transport.BLE ? BluetoothService.Transport.BLE :
//                transport == BluetoothService.Transport.BR_EDR ? BluetoothService.Transport.BR_EDR :
//                        BluetoothService.Transport.UNKNOWN;
//        if (mTransport == BluetoothService.Transport.UNKNOWN) {
//            // transport unknown, not possible to establish a connection
//            return false;
//        }
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
                if(connectionState == BluetoothService.State.CONNECTION_LOST){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            conntService();
                        }
                    }, 2000);
                }else {
                    onServiceConnected();
                    refreshConnectionState(connectionState);
                }
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
                if (bondState == BluetoothDevice.BOND_BONDED) {
                    conntService();
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
                PreferenceManager.getInstance().setStringValue(PreferenceManager.CONNECT_ARRAESS, mService.getDevice().getAddress());
                if (DEBUG) Log.d(TAG, handleMessage + "GAIA_READY");
                getInformationFromDevice();
                showPlayContorl();
                break;
            case BluetoothService.Messages.GATT_READY:
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

    private void showPlayContorl(){
        if(mAct != null) {
            mAct.setPlayContorlLay(true);
        }
        setAutoPlay();
    }
    private void setAutoPlay(){
        AccountInfo info = PreferenceManager.getInstance().getAccountInfo();
        if (info != null) {
            boolean autoplay = info.getAutoplay() == 1;
            mAct.setPlayContorl(autoplay);
            mGaiaManager.sendPlayControlCommand(autoplay ? PlayControl.PLAY.getValue() : PlayControl.PAUSE.getValue());
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mProgress, 0);
        }
    }
    /****
     * 播放控制 暂停上一首下一首
     * @param model
     * @return
     */
    public boolean sendPlayControlCommand(int model) {
        if (mService != null && mService.isGaiaReady()) {
            mGaiaManager.sendPlayControlCommand(model);
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
        Log.d(TAG, (mService == null) + ":service的结果和获取到的状态:"+mService.getConnectionState());
        if (mService != null && mService.getConnectionState() == BluetoothService.State.CONNECTED) {
            if (mTvConectDeviceName != null) {
                mTvConectDeviceName.setText(name);
            }
            mArcSeekBarInner.setMaxValue(maxVoice);
            mArcSeekBarOutter.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(mProgress);
            mArcSeekBarOutter.setProgress(mProgress);
            String text = PreferenceManager.getInstance().getPlaySoundMode(PreferenceManager.getInstance().getAccountInfo().getUserID() + mService.getDevice().getAddress());
            if (!TextUtils.isEmpty(text)) {
                mTvContorlName.setText(text);
            }
            connectDevice();
            Log.d(TAG,"service连接成功--serviceCOnnected");
            String deviceCode;
            int type;
            if(text.contains("X")){//5
                deviceCode = "AirX";
                type = 5;
            }else{
                deviceCode = "Air_V1";
                type = 4;
            }
            NetConfig.isGet = true;

            UserManager.getRequestHandler().requestAirUpdate(this, deviceCode, getVersion(), type);
        } else {
            Log.d(TAG,"service连接失败--无设备");
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

        mDevicesListFragment.refreshAdapter();

        if (state == BluetoothService.State.CONNECTED) {
            if (mTvConectDeviceName != null) {
                mTvConectDeviceName.setText(name);
            }
            PreferenceManager.getInstance().setStringValue(PreferenceManager.CONNECT_ARRAESS, mService.getDevice().getAddress());

            mArcSeekBarInner.setMaxValue(maxVoice);
            mArcSeekBarInner.setProgress(PreferenceManager.getInstance().getIntValue(PreferenceManager.CONNECT_VOICE));
            mArcSeekBarOutter.setMaxValue(maxVoice);
            mArcSeekBarOutter.setProgress(PreferenceManager.getInstance().getIntValue(PreferenceManager.CONNECT_VOICE));
            String text = PreferenceManager.getInstance().getPlaySoundMode(PreferenceManager.getInstance().getAccountInfo().getUserID() + mService.getDevice().getAddress());
            if (!TextUtils.isEmpty(text)) {
                mTvContorlName.setText(text);
            }
            connectDevice();
        } else {
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
//            mGaiaManager.getInformation(MainGaiaManager.Information.BATTERY);
//            mGaiaManager.getInformation(MainGaiaManager.Information.API_VERSION);
            mGaiaManager.getBattery();
            mGaiaManager.getDeviceVersion();
            mGaiaManager.getANC();
            mGaiaManager.getControlCommand();
            mGaiaManager.getPlayStatus();
            mGaiaManager.getEQModeCommand();
            mGaiaManager.getGetDeviceType();
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

    @Override // MainGaiaManager.MainGaiaManagerListener
    public boolean sendGAIAPacket(byte[] packet) {
        return mService != null && mService.sendGAIAPacket(packet);
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
        Log.d(TAG,"电量" + level);
        mBatteryLevel = level;
        refreshBatteryLevel();
    }

    @Override
    public void onGetRSSILevel(int level) {

    }

    DeceiveInfoDialog mDialog;
    private boolean showDialog;
    private String mVersion = "0.0.1";

    public String getVersion() {
        return mVersion;

    }

    public BluetoothDevice getConnectDevice() {
        if (mService != null && mService.getConnectionState() == BluetoothService.State.CONNECTED) {
            return mService.getDevice();
        }
        return null;
    }

    @Override
    public void onGetAPIVersion(int versionPart1, int versionPart2, int versionPart3) {
        String version = Utils.hex10To16(versionPart2) + Utils.hex10To16(versionPart3);
        version = version.replace("", ".").substring(3, 8);
        StringBuilder sb = new StringBuilder(versionPart1);
        sb.append(".").append(versionPart2);
        sb.append(".").append(versionPart3);
//        mVersion = sb.toString();
        Log.d(TAG,"版本" + version);
        mVersion = version;
        if (!showDialog) {
            return;
        }
        if (isDeviceReady()) {
            if (mDialog == null) {
                mDialog = DeceiveInfoDialog.getInstance(getActivity());
            }
            mDialog.show();
            mDialog.setData(mService.getDevice().getName(), mService.getDevice().getAddress(), mVersion);
            showDialog = false;

        }
    }

    @Override
    public void onFeaturesDiscovered() {

    }

    @Override
    public void getAncResult(boolean result) {
        Log.d(TAG,"anc" + result);
        mNoise.setSelected(result);
        mANcResult = result;
        onStandard();

    }

    private void onStandard() {
        if (!mbientResult && !mANcResult) {
            mStandard.setSelected(true);
        }
    }

    boolean mbientResult = true;
    boolean mANcResult = true;

    @Override
    public void getAmbientResult(boolean result) {
        Log.d(TAG,"amb" + result);
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
        switch (result) {
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

    @Override
    public void getDeviceType(int type, String name) {
        mDeviceType = type;
        updatePlayModelUI();
//        UserManager.getRequestHandler().requestAirUpdate(this, mService.getDevice().getName(), getVersion(), type);
        String deviceCode;
        if(type == DeviceType.AIRX.getType()){//5
            deviceCode = "AirX";
        }else{
            deviceCode = "Air_V1";
        }
        NetConfig.isGet = true;

        UserManager.getRequestHandler().requestAirUpdate(this, deviceCode, getVersion(), type);
    }

    private void onReceiveGattMessage(@GAIAGATTBLEService.GattMessage int gattMessage, Object content) {
        if (gattMessage == GAIAGATTBLEService.GattMessage.RSSI_LEVEL) {
            int rssi = (int) content;
            onGetRSSILevel(rssi);
        } else if (gattMessage == GAIAGATTBLEService.GattMessage.GATT_STATE) {
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
        if (mTvBatty != null) {
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
        unRegisterReceiver();
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

    private boolean isLoading = false;

    /****
     * 检查固件升级
     * @param url
     */
    private void checkUpdate(String url) {
        if (!isLoading) {
            isLoading = true;

            String apkName = Config.splitFilePath(url);
            int apkIndex = apkName.indexOf(".bin");
            if (apkIndex < 0) {
                displayShortToast("文件格式错误");
                return;
            }
            final String fileName = apkName.substring(0, apkIndex) + ".bin";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName); // Environment.getExternalStoragePublicDirectory(String)
            request.setDestinationInExternalPublicDir("data/data/" + getActivity().getPackageName() + "/", fileName); // Environment.getExternalStoragePublicDirectory(String)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            // in order for this if to run, you must use the android 3.2 to
            // compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }

            DownloadManager mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

            final long enqueue = mDownloadManager.enqueue(request);
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    isLoading = false;
//                    File file = new File(Environment.DIRECTORY_DOWNLOADS + "/" + fileName);
                    File file = new File("data/data/" + getActivity().getPackageName() + "/" + fileName);
                    if (mService != null && mService.isGaiaReady()) {
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
    private void initDevice() {
        mDevicesListFragment = DevicesListFragment.newInstance(DevicesListTabsAdapter.SCANNED_LIST_TYPE, this);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.id_device, mDevicesListFragment);
        transaction.commitAllowingStateLoss();
    }

    public boolean isCanBack() {
        if (mDeviceFrgmentContainer.getVisibility() == View.VISIBLE) {
            mDeviceFrgmentContainer.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

}
