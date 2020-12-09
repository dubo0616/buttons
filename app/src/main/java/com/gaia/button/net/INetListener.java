package com.gaia.button.net;


public interface INetListener {

    public static enum DownLoadStatus {
        EDlsInit, EDlsDownLoading, EDlsDownLoadComplete, EDlsDownLoadErr,
    };

    /**
     * @param responseData The response data with local object, need convert to specify
     *                     Class instance according to requestType
     * @param errorCode    TODO
     * @param requestTag   Specify the request type, such like 8 stands for register, 10
     *                     stands for get user info.
     * @param requestId    The request id.
     */
    void onNetResponse(int requestTag, BaseResult responseData,
                       int requestId, int errorCode, String responseStr);

    void onDownLoadStatus(DownLoadStatus status, int requestId);

    void onDownLoadProgressCurSize(long curSize, long totalSize, int requestId);

    /**
     * @param requestTag
     * @param requestId
     * @param errorCode  Refer NetError
     * @param msg
     */
    void onNetResponseErr(int requestTag, int requestId,
                          int errorCode, String msg, Object responseData);

}
