package com.gaia.button.net;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.utils.ConnectManager;
import com.gaia.button.utils.ContextHolder;
import com.gaia.button.utils.DcError;
import com.gaia.button.utils.StringConstant;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class OkHttpRequest implements Runnable {

    public OkHttpRequest() {
        super();
        client = getHttpClientInstance();
    }


    private String mUrl;
    private boolean mStop = false;
    private String mDownloadDstFilePath;
    private String mAccpetEcoding;
    private boolean mIsDownLoad = false;
    private String mContentType;
    private String mRequestMethod;
    private Object mRequestData;
    private Handler mCbkHandler;
    private int mRequestTag;
    // indicate whether support Resume broken transfer
    private boolean mIsSupportBt = true;
    private int mRepeatCount = 0;
    
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

    private OkHttpClient client;

    public OkHttpClient getHttpClientInstance() {
        if (client == null) {
            client = new OkHttpClient();
            client.setConnectTimeout(30, TimeUnit.SECONDS);
//            client.setWriteTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            mRepeatCount = 0;
//            try {
//                setCertificates(new InputStream[]{
//                        new Buffer().writeUtf8("").inputStream(),
//                        P2PApplication.getInstance().getAssets().open("xxx.cer")});
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return client;
    }

    public Handler getCbkHandler() {
        return this.mCbkHandler;
    }

    public void setCbkHandler(Handler cbkhandler) {
        this.mCbkHandler = cbkhandler;

    }

    public int getRequestTag() {
        return this.mRequestTag;
    }

    public void setRequestTag(int requestTag) {
        this.mRequestTag = requestTag;
    }

    public Object getRequestData() {
        return mRequestData;
    }

    public void setRequestData(Object requestdata) {
        this.mRequestData = requestdata;
    }

    public String getRequestMethod() {

        return mRequestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.mRequestMethod = requestMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public boolean getStop() {
        return mStop;
    }

    public void cancelRequest() {
        this.mStop = true;
    }

    public String getDownloadDstFilePath() {
        return mDownloadDstFilePath;
    }

    public void setDownloadDstFilePath(String downloadDstFilePath) {
        this.mDownloadDstFilePath = downloadDstFilePath;
        this.mIsDownLoad = true;
    }

    public String getAccpetEcoding() {
        return mAccpetEcoding;
    }

    public void setAccpetEcoding(String mAccpetEcoding) {
        this.mAccpetEcoding = mAccpetEcoding;
    }

    public boolean isIsDownLoad() {
        return mIsDownLoad;
    }

    public void setIsDownLoad(boolean isDownLoad) {
        this.mIsDownLoad = isDownLoad;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String contentType) {
        this.mContentType = contentType;
    }

    public void sendCbkMessage(Message msg) {

        if (this.mCbkHandler != null) {

            if (msg == null) {
                Message _msg = new Message();
                _msg.what = 9;

                mCbkHandler.sendMessage(_msg);
            } else {
                mCbkHandler.sendMessage(msg);
            }
        }
    }

    private void handleNetRequest() {
        Request postRequest = null;
//        Response getResponse = null;
        try {
            String token = "";
            AccountInfo user = PreferenceManager.getInstance().getAccountInfo();
            if (user != null && !TextUtils.isEmpty(user.getToken())) {
                token = user.getToken();
            }

            if (NetConfig.isGet) {
                Request.Builder builder= new Request.Builder().url(mUrl);
                if(!TextUtils.isEmpty(token)){
                    builder.addHeader("token",token);
                }
                Request request = builder.build();
                Call call = client.newCall(request);
                Response response  = call.execute();
                if (response.isSuccessful()) {
                    try {
                        handleSuccessEvent(response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                        handleErrorEvent(ContextHolder.getContext().getString(R.string.net_error_title),
                                DcError.DC_NET_GENER_ERROR);
                    }
                } else {
                    handleErrorEvent(ContextHolder.getContext().getString(R.string.net_error_title),
                            DcError.DC_NET_GENER_ERROR);
                }
            } else {
                RequestBody body = RequestBody.create(JSON, (String) mRequestData);
                 Request.Builder builder= new Request.Builder().url(mUrl).post(body);
                if(!TextUtils.isEmpty(token)){
                    builder.addHeader("token",token);
                }
                Request request = builder.build();
                //异步请求
//                client.newCall(request).enqueue(mHttpCallback);
                try {
                    //同步请求
                    Response response = client.newCall(request).execute();
                    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                    RequestBody.create(MEDIA_TYPE_PNG, new File(""));
                    if (response.isSuccessful()) {
                        try {
                            handleSuccessEvent(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                            handleErrorEvent(ContextHolder.getContext().getString(R.string.net_error_title),
                                    DcError.DC_NET_GENER_ERROR);
                        }
                    } else {
                        handleErrorEvent(ContextHolder.getContext().getString(R.string.net_error_title),
                                DcError.DC_NET_GENER_ERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handleErrorEvent(ContextHolder.getContext().getString(R.string.net_error_title), DcError.DC_NET_GENER_ERROR);
                }
            }

            NetConfig.isGet = false;
        } catch (Exception e) {
            e.printStackTrace();
            handleErrorEvent(ContextHolder.getContext().getString(R.string.net_error_title), DcError.DC_NET_GENER_ERROR);
            
        } finally {
            if (postRequest != null) {
                client.cancel(mRequestTag);
                postRequest = null;
            }
        }
    }


    private void handleErrorEvent(String netError, int errorCode) {

        NetMessage msg = new NetMessage();
        if (this.mIsDownLoad) {
            msg.setMessageType(NetMessage.NetMessageType.NetDownloadFailure);
            File file = new File(mDownloadDstFilePath + ".temp");
            file.delete();
        } else {
            msg.setMessageType(NetMessage.NetMessageType.NetFailure);
            msg.setErrorCode(errorCode);
        }
        // 判断本地是否有网络
        if(ConnectManager.checkNetworkState(ContextHolder.getContext()) == ConnectManager.TYPE_NET.NONE) {
            msg.setErrorCode(DcError.DC_NET_NONE);
            msg.setErrorString(ContextHolder.getContext().getString(R.string.net_error_local_none));
        } else {
            msg.setErrorString(netError);
        }
        msg.setRequestId(getRequestId());

        // create system message instance
        Message sysmsg = new Message();
        sysmsg.obj = msg;

        sendCbkMessage(sysmsg);
    }

    private void handleSuccessEvent(String responseStr) {
    	
        NetMessage msg = new NetMessage();
        if (this.mIsDownLoad) {
            msg.setMessageType(NetMessage.NetMessageType.NetDownloadSuccess);
        } else {
            msg.setMessageType(NetMessage.NetMessageType.NetSuccess);

            String decryptData = null;

            // 解密 decrypt responseStr
            // AES myaes = new AES();
            // decryptData = myaes.aesDecrypt(new String(responseStr));
            //
            // LogUtil.d("下行数据 === " + decryptData);
            // msg.setResponseString(decryptData.getBytes());

           BaseResult res = new BaseResult();
            try {
                NetProtocolHeader header = new JsonHelper().parserHeaderForHttp(responseStr);
                if (header != null && header.getErrorCode() == DcError.DC_NET_ERROR_DIALOG) {
                    //服务器特殊挡板
                    msg.setErrorCode(header.getErrorCode());
                    msg.setErrorString(header.getErrorDesc());
                    res.setErrorCode(header.getErrorCode());
                    res.setErrorString(header.getErrorDesc());
                    res.setServerErrorUrl(TextUtils.isEmpty(header.getInfo()) ? "" : header.getInfo());
                    msg.setResponseData(res);
                } else {
                    res = new JsonHelper().parserWithTag(this.mRequestTag, responseStr);
                    msg.setResponseData(res);
                    msg.setResponesStr(responseStr);
                }
            } catch (JSONException e) {
                // 捕获JSON异常
                e.printStackTrace();
                msg.setErrorCode(DcError.DC_JSON_PARSER_ERROR);
                msg.setErrorString(ContextHolder.getContext().getString(R.string.server_error));
            } catch (Exception ex) {
                // net request failed
                if (res != null) {
                    res.setErrorCode(DcError.DC_NET_DATA_ERROR);
                }
                msg.setErrorCode(DcError.DC_NET_DATA_ERROR);
                msg.setErrorString(ContextHolder.getContext().getString(R.string.net_error_title));
            } finally {

            }
        }
        msg.setRequestId(getRequestId());

        // create system message instance
        Message sysmsg = new Message();
        sysmsg.obj = msg;

        sendCbkMessage(sysmsg);
    }

    /*
     * @Transfer request cancel event to UI layer
     *
     * @Param cancelStr specify the cancel reason and not use yet
     */
    private void handleCancelEvent(String cancelStr) {

        NetMessage msg = new NetMessage();
        msg.setMessageType(NetMessage.NetMessageType.NetCancel);
        msg.setRequestId(getRequestId());

        // create system message instance
        Message sysmsg = new Message();
        sysmsg.obj = msg;

        sendCbkMessage(sysmsg);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        handleNetRequest();
    }

    public void setCertificates(InputStream... certificates) {
        setCertificates(certificates, null, null);
    }

    public TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) return null;
        try {

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e)

                {
                }
            }
            TrustManagerFactory trustManagerFactory = null;

            trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
            client.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }


    public class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }


        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }


        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

//    Callback mHttpCallback = new Callback() {
//
//        @Override
//        public void onResponse(Response response) throws IOException {
//            // TODO Auto-generated method stub
//            try {
//                handleSuccessEvent(response.body().string());
//            } catch (Exception e) {
//            	e.printStackTrace();
//                handleErrorEvent(e.getMessage(),
//                        DcError.DC_NET_GENER_ERROR);
//            }
//        }
//
//        @Override
//        public void onFailure(Request request, IOException e) {
//            // TODO Auto-generated method stub
//        	if (e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {//timeout exception
//        		checkChangeDomain(e);
//    		} else {
//    			handleException(e);
//    		}
//        }
//    };
    
    
//    private void checkChangeDomain(Exception e) {
//		// TODO Auto-generated method stub
//
//    	if (ConnectManager.isNetworkConnected(P2PApplication.getInstance().getApplicationContext()) && ConnectManager.checkNetStateByPing()) {//是否有网络连接//ping 判断网络是否畅通
//    		//net is ok change domain
//    		try {
//    			CrashDomainModel model = (CrashDomainModel) P2PApplication.getPreferences().getObject(ConstantUtil.SP_NAME_CRASH_KEY, "");
//    			if (model != null) {
//    				//change
//
//    				if (mRepeatCount == model.getList().size()) {
//    					handleException(e);
//    				} else {
//    					for (CrashDomainItemModel item : model.getList()) {
//    						String apiName =  item.getApi();
//    						String mName =  item.getM();
//
//    						if (mUrl.contains(apiName)) {//api-->api2-->api3
//    							continue;
//    						}
//    						mRepeatCount++;//0-->1 -->2
//    						ConstantUtil.NEW_API_NAME = apiName;
//    						ConstantUtil.NEW_M_NAME = mName;
//    						ConstantUtil.NEW_BAPI_URL = "https://" + ConstantUtil.NEW_API_NAME + ConstantUtil.NEW_API_URL_SUFFIX;
//    						this.setUrl(ConstantUtil.NEW_BAPI_URL);
//    						handleNetRequest();
//    						break;
//    					}
//    				}
//    			} else {
//    				handleException(e);
//    			}
//			} catch (Exception e2) {
//				e.printStackTrace();
//				handleException(e);
//			}
//
//    	} else {
//    		handleException(e);
//    	}
//	}
    
    private void handleException(Exception e) {
    	if (e instanceof SSLHandshakeException) {//ssl exception
			handleErrorEvent(e.getMessage(),
					DcError.DC_NET_SSL_ERROR);
		} else {
			handleErrorEvent(e.getMessage(),
					DcError.DC_NET_GENER_ERROR);
		}
    }

	@Override
	public String toString() {
		return String.valueOf(this.mRequestTag);
	}

	@Override
	public boolean equals(Object o) {
		try {
			if (o instanceof OkHttpRequest) {
				OkHttpRequest temp = (OkHttpRequest)o;
				return this.mRequestTag == temp.mRequestTag;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return this.mRequestTag;
	}

    /** 获取请求ID */
    public int getRequestId() {
        int requestId = 0;
        if (null != getRequestData()) {
            requestId = getRequestData().hashCode();
        } else {
            requestId = hashCode();
        }
        return requestId;
    }
}
