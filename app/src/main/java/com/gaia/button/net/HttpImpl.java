/**
 * @author huzexin@duoku.com
 * @version CreateData锛?012-5-10 3:46:54 PM
 */
package com.jindan.p2p.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.jindan.p2p.json.JsonHelper;
import com.jindan.p2p.net.INetListener.DownLoadStatus;
import com.jindan.p2p.net.NetMessage.NetMessageType;
import com.jindan.p2p.utils.ContextHolder;
import com.jindan.p2p.utils.DcError;
import com.jindan.p2p.utils.LogUtil;
import com.jindan.p2p.utils.SharePreferenceUtil;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static com.jindan.p2p.utils.SharePreferenceUtil.SPTOOL;

public class HttpImpl implements IHttpInterface {

    private ConcurrentHashMap<OkHttpRequest, INetListener> mRequestQuene;
    private volatile Handler mHandler;


    HttpImpl() {
        mRequestQuene = new ConcurrentHashMap<OkHttpRequest, INetListener>(10);
        mHandler = new HttpMsgHandler(ContextHolder.getContext().getMainLooper());
    }

    public int sendRequest(String url, int requestTag, Object bodydata,
                           INetListener listener) {
    	OkHttpRequest request = new OkHttpRequest();
        request.setUrl(url);

        LogUtil.d("上行地址=====" + url);
        LogUtil.d("上行数据=====" + bodydata);

        // 加密
        request.setRequestData(bodydata);
        request.setRequestTag(requestTag);
        request.setCbkHandler(mHandler);
        // add to task quene
        addTaskToQuene(request, listener);

        return getRequestHashCode(request);
    }

    private synchronized void doNetCachResponse(int requestTag, INetListener listener, int requestId) {
        if (requestTag <= 0) {
            return;
        }
        //读取网络缓存
        String cacheData = SPTOOL.getString(SharePreferenceUtil.SP_FILE_NETCACHE, requestTag + "", "");
        if (!TextUtils.isEmpty(cacheData)) {
            try {
                BaseResult result = new JsonHelper().parserWithTag(requestTag, cacheData);
                result.setCache(true);
                listener.onNetResponse(requestTag, result,
                        requestId, 0, cacheData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int sendDownLoadRequest(String url, String filepath,
                                   INetListener listener) {

        // create a new request and set request param
        // and then add the request to quene
        OkHttpRequest request = new OkHttpRequest();
        request.setUrl(url);
        request.setDownloadDstFilePath(filepath);
        request.setCbkHandler(mHandler);

        // add to task quene
        addTaskToQuene(request, listener);

        return getRequestHashCode(request);
    }

    public void cancelRequestById(int requestId) {

        Iterator<Entry<OkHttpRequest, INetListener>> iter = mRequestQuene
                .entrySet().iterator();

        while (iter.hasNext()) {
            Entry<OkHttpRequest, INetListener> entry = iter.next();

            OkHttpRequest request = entry.getKey();

            // check if current request match specify request id
            if (requestId == getRequestHashCode(request)) {

                // set end flag to specify request
                request.cancelRequest();
            }
        }
    }

    public void cancelAllRequest() {
        Iterator<Entry<OkHttpRequest, INetListener>> iter = mRequestQuene
                .entrySet().iterator();

        while (iter.hasNext()) {
            Entry<OkHttpRequest, INetListener> entry = iter.next();

            OkHttpRequest request =  entry.getKey();

            request.cancelRequest();
        }

        // remove all request in the quene
        mRequestQuene.clear();
    }

    /**
     * Add task to the task quene
     *
     * @param request
     * @param listener
     */
    private void addTaskToQuene(OkHttpRequest request, INetListener listener) {
    	if (mRequestQuene.containsKey(request)) {
    		LogUtil.e("zsz", "addTaskToQuene---same-request-----return");
    		return;
    	}
        mRequestQuene.put(request, listener);
        HttpClientHelper.execute(request);
    }

    /**
     * Remove task from the task quene
     *
     * @param request
     */
    private void removeTask(OkHttpRequest request) {

        boolean _exist = mRequestQuene.containsKey(request);

        if (_exist) {
            mRequestQuene.remove(request);
        }
    }

    /*
     * @ get net listener by net request id
     */
    private Entry<OkHttpRequest, INetListener> findRequestListenerById(
            int requestid) {

        Entry<OkHttpRequest, INetListener> resEntry = null;
        Iterator<Entry<OkHttpRequest, INetListener>> iter = mRequestQuene
                .entrySet().iterator();

        while (iter.hasNext()) {
            Entry<OkHttpRequest, INetListener> entry = iter.next();

            OkHttpRequest request = entry.getKey();

            // check if current request match specify request id
            if (requestid == getRequestHashCode(request)) {
                resEntry = entry;
                break;
            }
        }

        return resEntry;
    }

    private int getRequestHashCode(OkHttpRequest request) {
        if(request == null) {
            return 0;
        }
        return request.getRequestId();
    }

    private final class HttpMsgHandler extends Handler {
        public HttpMsgHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            boolean res = msg.obj.getClass().equals(NetMessage.class);
            if (res) {
                NetMessage netmsg = (NetMessage) msg.obj;
                NetMessageType netmsgtype = netmsg.getMessageType();

                switch (netmsgtype) {
                    case NetSuccess: {
                        doNetSucessResponse(netmsg);
                    }
                    break;

                    case NetFailure: {
                        doNetFailureResponse(netmsg);
                    }
                    break;

                    case NetDownloadling: {
                        doDownloadProgressResponse(netmsg);
                    }
                    break;

                    case NetDownloadFailure: {
                        doDownLoadFailureResponse(netmsg);
                    }
                    break;

                    case NetDownloadSuccess: {
                        doDownloadSuccessResponse(netmsg);
                    }
                    break;

                    case NetCancel: {
                        doCancelResponse(netmsg);
                    }
                    break;

                    default:
                        break;

                }
            }
        }

        private void doNetSucessResponse(NetMessage message) {
            int requestid = message.getRequestId();
            Entry<OkHttpRequest, INetListener> entry = HttpImpl.this
                    .findRequestListenerById(requestid);

            if (entry != null) {

                INetListener _listener = entry.getValue();
                OkHttpRequest _request = entry.getKey();
                int _requestid = getRequestHashCode(_request);

                BaseResult res = message.getResponseData();

                if (res == null) {
                    int errcode = message.getErrorCode();
                    _listener.onNetResponseErr(_request.getRequestTag(),
                            _requestid, errcode, message.getErrorString(), res);
                } else {
                    res.setCache(false);
                    int errcode = res.getErrorCode();
                    if (errcode == DcError.DC_OK || errcode == DcError.DC_OK_200) {
                        _listener.onNetResponse(_request.getRequestTag(), res,
                                _requestid, 0, message.getResponesStr());
                    } else if (errcode == DcError.DC_NET_ERROR_DIALOG) {
                        _listener.onNetResponseErr(_request.getRequestTag(),
                                _requestid, errcode, res.getErrorString(),  message.getResponseData());
                    } else {
                        _listener.onNetResponseErr(_request.getRequestTag(),
                                _requestid, errcode, res.getErrorString(), res);
                    }
                }
                removeTask(entry.getKey());
            }
        }

        private void doNetFailureResponse(NetMessage message) {
            int requestid = message.getRequestId();
            Entry<OkHttpRequest, INetListener> entry = HttpImpl.this
                    .findRequestListenerById(requestid);

            if (entry != null) {
                INetListener _listener = entry.getValue();
                OkHttpRequest _request = entry.getKey();
                // need handle the net error for more detail
                if (message.getErrorCode() == DcError.DC_NET_ERROR_DIALOG) {
                    _listener.onNetResponseErr(_request.getRequestTag(), requestid,
                			message.getErrorCode(), message.getErrorString(), message.getResponseData());
                } else {
                	_listener.onNetResponseErr(_request.getRequestTag(), requestid,
                			message.getErrorCode(), message.getErrorString(), message.getResponseData());
                }
                removeTask(entry.getKey());
            }

        }

        private void doDownLoadFailureResponse(NetMessage message) {
            int requestid = message.getRequestId();
            Entry<OkHttpRequest, INetListener> entry = HttpImpl.this
                    .findRequestListenerById(requestid);

            if (entry != null) {
                INetListener _listener = entry.getValue();
                _listener.onDownLoadStatus(DownLoadStatus.EDlsDownLoadErr,
                        requestid);
                removeTask(entry.getKey());
            }

        }

        private void doDownloadProgressResponse(NetMessage message) {
            int requestid = message.getRequestId();
            Entry<OkHttpRequest, INetListener> entry = HttpImpl.this
                    .findRequestListenerById(requestid);
            if (entry != null) {
                INetListener _listener = entry.getValue();
                _listener.onDownLoadProgressCurSize(message.getCurentSize(),
                        message.getTotalSize(), requestid);

            }
        }

        private void doDownloadSuccessResponse(NetMessage message) {
            int requestid = message.getRequestId();
            Entry<OkHttpRequest, INetListener> entry = HttpImpl.this
                    .findRequestListenerById(requestid);

            if (entry != null) {
                INetListener _listener = entry.getValue();
                _listener.onDownLoadStatus(DownLoadStatus.EDlsDownLoadComplete,
                        requestid);
                removeTask(entry.getKey());
            }

        }

        private void doCancelResponse(NetMessage message) {
            int requestid = message.getRequestId();
            Entry<OkHttpRequest, INetListener> entry = HttpImpl.this
                    .findRequestListenerById(requestid);

            if (entry != null) {
                // do nothing for now
                removeTask(entry.getKey());
            }
        }

    }

    @Override
    public int sendRequestNetCache(String url, int requestTag, Object bodydata,
                                   INetListener listener, int offset) {
        OkHttpRequest request = new OkHttpRequest();
        request.setUrl(url);

        LogUtil.d("上行地址=====" + url);
        LogUtil.d("上行数据=====" + bodydata);
        LogUtil.e("zsz", "sendRequestNetCache---offset==" + offset);

        // 加密
//        AES myaes = new AES();
//        String encryptData = myaes.aesEncrypt(bodydata);
//        request.setRequestData(encryptData);
        request.setRequestData(bodydata);
        request.setRequestTag(requestTag);
        // request.setRequestData(bodydata);
        request.setCbkHandler(mHandler);
        // add to task quene
        addTaskToQuene(request, listener);

        if (offset == 0) {
            doNetCachResponse(requestTag, listener, getRequestHashCode(request));
        }

        return getRequestHashCode(request);
    }

}
