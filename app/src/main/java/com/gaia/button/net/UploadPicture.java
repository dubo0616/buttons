package com.jindan.p2p.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.jindan.p2p.json.model.UserBankCardModel;
import com.jindan.p2p.json.model.UserModel.UserInfo;
import com.jindan.p2p.net.BaseResult;
import com.jindan.p2p.net.INetListener;
import com.jindan.p2p.user.UserManager;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UploadPicture implements INetListener {

    private static final String DEFAULT_CHARSET_NAME = "UTF-8";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String BOUNDARY = "****************fD4fH3gL0hK7aI6"; // 数据分隔符
    private static final String TWOHYPHENTS = "--";
    private static final String CRLF = System.getProperty("line.separator");
    private static final int CONNECTION_OUTIME = 20000; // 8秒连接超时
    private static final int DATAACCESS_OUTTIME = 30000; // 30秒读数据超时
    private static String formDataName = "avatar";

    public static void getAccountInfo(int id, INetListener listener) {
        // HttpRequestExt request = new
        // HttpRequestExt(ApiUrlDef.GET_ACCOUNT_INFO);
        // request.setRequestId(id);
        // request.setCacheType(CacheType.EVERY_REQUEST);
        // request.post(listener);
        // LogUtil.e(ApiUrlDef.TAG, "获取个人基本信息");
    }

    /**
     * 更新昵称
     *
     * @param context
     * @param id
     * @param nickName
     * @param listener
     */
    public static void updateBaseInfo(Context context, int id, String nickName,
                                      INetListener listener) {
        // HttpRequestExt request = new
        // HttpRequestExt(ApiUrlDef.UPDATEBASEINFO);
        // request.setParam("nickName", nickName);
        // request.setRequestId(id);
        // request.setContext(context);
        // request.post(listener);
    }

    /**
     * 上传绑卡照片
     */
    public static void uploadUserBindcardPhoto(final Context context,
                                               final String filePath, final Handler handler) {

        formDataName = "card";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<String, String>();
                handleBindCardPostParams(params);
                String[] imagePaths = {filePath};

                LogUtil.e("userdata--url=" + ConstantUtil.SERVER_URL_TAG_42);
                String result = doMultipartPost(ConstantUtil.SERVER_URL_TAG_42,
                        params, imagePaths, true);

                try {
                    JSONObject jResult = new JSONObject(result);
                    int error_code = jResult.optInt(StringConstant.JSON_ERROR_CODE, -1);
                    String message = jResult.optString(StringConstant.JSON_ERROR_MESSAGE);
                    Message msg = new Message();
                    if (error_code == 0) {// success
                        JSONObject obj = jResult.optJSONObject("data");
                        String url = obj.optString("card_image");
                        UserBankCardModel bankCard = new UserBankCardModel();
                        try {
                            JSONObject bankcard = obj.optJSONObject("bank_card");
                            bankCard.setInfo(url);
                            bankCard.setBind_card_note(bankcard.optString("bind_card_note"));
                            bankCard.setBind_card_note_short(bankcard.optString("bind_card_note_short"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (TextUtils.isEmpty(url)) {
                            // 服务器异常情况
                            LogUtil.d("upload bindcard pic success but url is null go wangdong");
                            msg.what = 1;
                            msg.obj = "服务器异常，请稍后重试";
                            handler.sendMessage(msg);
                        } else {
                            msg.what = 0;
                            msg.obj = bankCard;
                            handler.sendMessage(msg);

                            // copy file to avatar folder
                            String avatarName = UploadPicture.getUserAvatarName(url);
                            String avatarPath = UploadPicture
                                    .getUserAvatarDir(UserManager.getUserDataHandler().getCurrentUserInfo().getUser_id());
                            String newPath = avatarPath + "/" + avatarName;
                            FileHelper.copyFile(filePath, newPath);

                        }

                    } else {// fail
                        // ToastFactory.showToast(context, message,
                        // Toast.LENGTH_LONG);
                        msg.what = 2;
                        msg.obj = message;
                        handler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d("upload avatar fail  go wangdong--exception is:"
                            + e.getMessage());
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = "服务器异常，请稍后重试";
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * 上传照片
     */
    public static void uploadUserAvatar(final Context context,
                                        final String filePath, final Handler handler) {
        formDataName = "avatar";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<String, String>();
                handlePostParams(params);
                String[] imagePaths = {filePath};
                String result = doMultipartPost(ConstantUtil.SERVER_URL_TAG_39,
                        params, imagePaths, true);

                try {
                    JSONObject jResult = new JSONObject(result);
                    int error_code = jResult.optInt(StringConstant.JSON_ERROR_CODE, -1);
                    String message = jResult.optString(StringConstant.JSON_ERROR_MESSAGE);
                    Message msg = new Message();
                    if (error_code == 0) {// success
                        JSONObject obj = jResult.optJSONObject("data");
                        String url = obj.optString("avatar_image");
                        if (TextUtils.isEmpty(url)) {
                            // 服务器异常情况
                            LogUtil.d("upload avatar success but url is null go wangdong");
                            msg.what = 1;
                            msg.obj = "服务器异常，请稍后重试";
                            handler.sendMessage(msg);
                        } else {
                            msg.what = 0;
                            msg.obj = url;
                            handler.sendMessage(msg);

                            // copy file to avatar folder
                            String avatarName = UploadPicture.getUserAvatarName(url);
                            String avatarPath = UploadPicture
                                    .getUserAvatarDir(UserManager
                                            .getUserDataHandler()
                                            .getCurrentUserInfo().getUser_id());
                            String newPath = avatarPath + "/" + avatarName;
                            FileHelper.copyFile(filePath, newPath);

                        }

                    } else {// fail
                        // ToastFactory.showToast(context, message,
                        // Toast.LENGTH_LONG);
                        msg.what = 2;
                        if (TextUtils.isEmpty(message)) {
                            message = "服务器异常，请稍后重试";
                        }
                        msg.obj = message;
                        handler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d("upload avatar fail  go wangdong--exception is:"
                            + e.getMessage());
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = "服务器异常，请稍后重试";
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    protected static void handleBindCardPostParams(Map<String, String> params) {
        // TODO Auto-generated method stub

        UserInfo user = UserManager.getUserDataHandler().getCurrentUserInfo();
        String _uid = "";
        String _token = "";
        if (user != null) {
            // _uid = user.getUserId() == null ? "" : user.getUserId();
            _token = user.getToken() == null ? "" : user.getToken();
        }

        // get user id from user interface
        // params.put(StringConstant.JSON_USER_ID, _uid);

        // get user token from user interface
        params.put(StringConstant.JSON_TOKEN, _token);
        // params.put(StringConstant.JSON_TOKEN, _token);

    }

    protected static void handlePostParams(Map<String, String> params) {
        // TODO Auto-generated method stub

        UserInfo user = UserManager.getUserDataHandler().getCurrentUserInfo();
        // String _uid = "";
        String _token = "";
        if (user != null) {
            // _uid = user.getUserId() == null ? "" : user.getUserId();
            _token = user.getToken() == null ? "" : user.getToken();
        }
        // get user id from user interface
        // params.put(StringConstant.JSON_USER_ID, _uid);
        // get user token from user interface
        params.put(StringConstant.JSON_TOKEN, _token);

    }

    public static String doMultipartPost(String url,
                                         Map<String, String> params, String[] imagePaths, boolean isIcon) {
        String result = null;
        HttpURLConnection conn = null;
        DataOutputStream output = null;
        BufferedReader input = null;
        try {
            URL baseUrl = new URL(url);
            conn = (HttpURLConnection) baseUrl.openConnection();
            conn.setConnectTimeout(CONNECTION_OUTIME); // 20秒连接超时
            conn.setReadTimeout(DATAACCESS_OUTTIME); // 30秒读数据超时
            conn.setDoInput(true); // 允许输入
            conn.setDoOutput(true); // 允许输出
            conn.setUseCaches(false); // 不使用Cache
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", DEFAULT_CHARSET_NAME);
            conn.setRequestProperty("Content-type", MULTIPART_FORM_DATA
                    + "; boundary=" + BOUNDARY);
            String token = "";
            UserInfo user = UserManager.getUserDataHandler().getCurrentUserInfo();
            if (user != null) {
                token = user.getToken();
            }
            conn.setRequestProperty("Cookie", "token=" + token);

            output = new DataOutputStream(conn.getOutputStream());

            addFormField(params, output); // 添加表单字段内容

            addImageContent(isIcon, imagePaths, output); // 添加图片内容

            String end_data = TWOHYPHENTS + BOUNDARY + TWOHYPHENTS + CRLF; // 数据结束符
            output.write(end_data.getBytes()); // 数据结束标志
            output.flush();

            int code = conn.getResponseCode();

            switch (code) {
                case HttpStatus.SC_OK:
                    input = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String oneLine;
                    while ((oneLine = input.readLine()) != null) {
                        response.append(oneLine);
                    }
                    result = response.toString();
                    break;
                case HttpStatus.SC_REQUEST_TIMEOUT:
                    result = "{result:-1,ret:'-1',msg:'网络请求超时，请检查网络连接情况！'}";
                    break;
                case HttpStatus.SC_REQUEST_TOO_LONG:
                case HttpStatus.SC_REQUEST_URI_TOO_LONG:
                    result = "{result:-1,ret:'-1',msg:'上传的图片超过大小限制，请选择合适的图片上传！'}";
                    break;
                default:
                    result = "{result:-1,ret:'-1',msg:'网络请求异常，请确认联网正常然后重试！'}";
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "{result:-1,ret:'-1',msg:'网络请求创建失败，请稍后重试！'}";
        } finally { // 统一释放资源
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    // 上传图片参数部分
    private static void addImageContent(boolean isIcon, String[] imagePaths,
                                        DataOutputStream output) {
        for (int i = 0; i < imagePaths.length; i++) {
            String imagePath = imagePaths[i];
            if (!TextUtils.isEmpty(imagePath)) {
                String type = BitmapTool.getTypeFromPath(imagePath);
                byte[] image = null;
                if (type.equals(BitmapTool.IMAGE_TYPE_JPG)
                        || type.equals(BitmapTool.IMAGE_TYPE_PNG)) {
                    Bitmap bmp = BitmapTool.loadResizedBitmap(imagePath,
                            BitmapTool.DEFAULT_WIDTH,
                            BitmapTool.DEFAULT_HEIGHT, true);
                    image = BitmapTool.compressImage(bmp, type);
                    if (bmp != null && !bmp.isRecycled()) {
                        bmp.recycle();
                        bmp = null;
                    }
                } else if (type.equals(BitmapTool.IMAGE_TYPE_GIF)) // 对于gif不进行压缩
                {
                    InputStream in;
                    try {
                        in = new FileInputStream(imagePath);
                        image = new byte[in.available()];
                        in.read(image);
                        in.close();
                    } catch (FileNotFoundException fe) {
                        fe.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                String name = isIcon ? "image" : "image"
                        + Integer.toString(i + 1);
                int pos = imagePath.lastIndexOf('/') + 1;
                String filename = imagePath.substring(pos);
                Log.i("zzz", "ImagePath: " + imagePath + "Image name: " + name
                        + "Image filename: " + filename);
                try {
                    name = new String(name.getBytes(DEFAULT_CHARSET_NAME));
                    filename = new String(
                            filename.getBytes(DEFAULT_CHARSET_NAME));
                } catch (UnsupportedEncodingException encodeException) {
                    encodeException.printStackTrace();
                }

                StringBuilder split = new StringBuilder();
                split.append(TWOHYPHENTS + BOUNDARY + CRLF);
                split.append("Content-Disposition: form-data;name=\"" + formDataName + "\";filename=\"" + filename + "\"" + CRLF);
                split.append("Content-Type: " + type + CRLF);
                split.append(CRLF);
                if (image != null) {
                    try {
                        output.write(split.toString().getBytes());
                        output.write(image, 0, image.length);
                        output.write(CRLF.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 上传的表单参数部分
    private static void addFormField(Map<String, String> params,
                                     DataOutputStream output) {
        StringBuilder splite = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) { // 构建表单字段内容
            splite.append(TWOHYPHENTS + BOUNDARY + CRLF);
            splite.append("Content-Disposition: form-data;name=\""
                    + entry.getKey() + "\"" + CRLF);
            splite.append(CRLF);
            splite.append(entry.getValue());
        }
        splite.append(CRLF);
        try {
            output.write(splite.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUserAvatarName(String avatarUrl) {

        if (TextUtils.isEmpty(avatarUrl)) {
            return "";
        }

        return avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1,
                avatarUrl.length());
    }

    public static String getUserAvatarDir(String userId) {
        return Config.userDir + userId + "/" + Config.userFolderNameAvatar;
    }

    @Override
    public void onNetResponse(int requestTag, BaseResult responseData,
                              int requestId, int errorCode, String responseStr) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDownLoadStatus(DownLoadStatus status, int requestId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDownLoadProgressCurSize(long curSize, long totalSize,
                                          int requestId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNetResponseErr(int requestTag, int requestId, int errorCode,
                                 String msg, Object responseData) {
        // TODO Auto-generated method stub

    }

}
