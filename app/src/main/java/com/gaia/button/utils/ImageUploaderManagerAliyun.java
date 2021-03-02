package com.gaia.button.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.gaia.button.GaiaApplication;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.GetUploadTokenAliModel;
import com.gaia.button.net.NetConfig;
import com.gaia.button.net.user.IUploadCallback;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;

/**
 * 图片上传管理器
 * 上传 照片工具类
 */
public class ImageUploaderManagerAliyun implements IUserListener {
    public static final String TAG = ImageUploaderManagerAliyun.class.getSimpleName();
    private static ImageUploaderManagerAliyun sInstance = new ImageUploaderManagerAliyun();
    public static final String BUCKET_NAME = "buttons";
    public static final String END_POINT = "oss-cn-zhangjiakou.aliyuncs.com";
    public static final String URL_PRE_SERVER = "http://image-fengtangkeji.oss-cn-beijing.aliyuncs.com/";
    private GetUploadTokenAliModel uploadTokenAliModel;

    public OSSClient ossClientMy;

    /**
     * token过期时间
     */
    private long tokenExpiredTime = 0;
    /**
     * 上一个进度刷新的时间间隔
     */
    private long lastProressTime = 0;

    /**
     * 是否进行图片压缩(默认不进行压缩处理)
     */
    private boolean needCompress = false;
    /**
     * 是否正在获取上传凭证(防止不同的获取上传凭证)
     */
    private boolean isGettingToken = false;
    /**
     * 是否需要图片上传
     */
    private boolean isCancelled = false;

    private ImageUploaderManagerAliyun() {
    }

    private OSSClient refreshAliyunOssClient(Context context) {
        OSSCredentialProvider oSSStsTokenCredentialProvider = new OSSStsTokenCredentialProvider(uploadTokenAliModel.getAccessKeyId(), uploadTokenAliModel.getAccessKeySecret(), uploadTokenAliModel.getSecurityToken());
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(15000);
        clientConfiguration.setSocketTimeout(15000);
        clientConfiguration.setMaxConcurrentRequest(5);
        clientConfiguration.setMaxErrorRetry(2);
        OSSLog.enableLog();
        return new OSSClient(context, END_POINT, oSSStsTokenCredentialProvider, clientConfiguration);

    }

    public static ImageUploaderManagerAliyun getInstance() {
        return sInstance;
    }

    private String mUrl = "";
    /**
     * 图片处理回调
     */
    private IUploadCallback mCallback;
    /**
     * 第一步：获取图片上传凭证
     */
    public void startGetUploadToken(String url,IUploadCallback callback) {
        if (isGettingToken) {
            return;
        }
        mCallback = callback;
        mCallback.uploadStart("");
        mUrl = url;
        NetConfig.isGet = true;
        UserManager.getRequestHandler().requestGetUploadToken(this);
        isGettingToken = true;
    }

    /**
     * 上传图片列表
     *
     * @param url : 图片列表
     */
    private void startUploadImageFile(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, "headimg", url);
        putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

            }
        });
        OSSAsyncTask ossAsyncTask = ossClientMy.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult result) {
                if (!TextUtils.isEmpty(putObjectRequest.getObjectKey())) {
                    String url = "";
                    try {
                        url = ossClientMy.presignConstrainedObjectURL(BUCKET_NAME, putObjectRequest.getObjectKey(), 3600l* 1000*24*365 *10);
                        if(mCallback != null) {
                            mCallback.uploadSuccess(url);
                        }
                    } catch (ClientException e) {
                        e.printStackTrace();
                        if(mCallback != null) {
                            mCallback.uploadSuccess(url);
                        }
                    }

                }else{
                    if(mCallback != null) {
                        mCallback.uploadSuccess("");
                    }
                }
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException clientException, ServiceException serviceException) {
                if(mCallback != null) {
                    mCallback.uploadSuccess("");
                }
                if (clientException != null) {
                    // Local exception, such as a network exception
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    // Service exception
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }


            }
        });


    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        isGettingToken = false;
        if (requestTag == ConstantUtil.Net_Tag_User_GETOSS) {
            if (data instanceof GetUploadTokenAliModel) {
                uploadTokenAliModel = (GetUploadTokenAliModel) data;
                if (uploadTokenAliModel != null && !TextUtils.isEmpty(mUrl)) {
                    if (ossClientMy == null) {
                        ossClientMy = refreshAliyunOssClient(GaiaApplication.getInstance());
                    }
                    startUploadImageFile(mUrl);
                }
            }

        }
    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        isGettingToken = false;
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }


}
