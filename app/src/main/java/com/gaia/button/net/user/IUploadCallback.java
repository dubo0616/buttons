package com.gaia.button.net.user;

/**
 * 文件上传回调接口
 */
public interface IUploadCallback<T> {

    /** 文件上传开始 */
    public void uploadStart(T fileInfo);
    /** 文件上传中 */
    public void uploadProgress(T fileInfo);
    /** 文件上传成功 */
    public void uploadSuccess(T fileInfo);
    /** 文件上传失败 */
    public void uploadFailed(T fileInfo);
}
