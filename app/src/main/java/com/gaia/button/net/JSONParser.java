package com.gaia.button.net;

import android.content.Intent;
import android.text.TextUtils;

import com.gaia.button.GaiaApplication;
import com.gaia.button.activity.LoginMainActivity;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.model.DeviceList;
import com.gaia.button.model.DiscoverList;
import com.gaia.button.model.DiscoveryModel;
import com.gaia.button.model.ProductModelList;
import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.utils.DcError;
import com.gaia.button.utils.StringConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONParser implements JsonParserInterface {

    /**
     * 表头数据解析
     * 说明: 兼容返回结果数据（带接口名、不带接口名的情况）
     *
     * @param responseString : 应答报文
     * @return : 头部解析结果
     * @throws Exception
     */
    public NetProtocolHeader parserHeader(String responseString)
            throws Exception {
        NetProtocolHeader res = new NetProtocolHeader();
        res.setErrorCode(DcError.DC_Error);
        res.setErrorDesc("");
        try {
            JSONObject jsonObj = new JSONObject(responseString);
            if (null != jsonObj) {
                //第一层为结果数据
                parserHeader(jsonObj, res);
                if (DcError.DC_Error == res.getErrorCode()) {
                    //第一层为接口名(接口名下为结果数据)
                    Iterator<String> it = jsonObj.keys();
                    while (it.hasNext()) {
                        //查找每一个子节点
                        String key = it.next();
                        JSONObject jsonFun = jsonObj.getJSONObject(key);
                        if (null != jsonFun) {
                            parserHeader(jsonFun, res);
                            if (DcError.DC_Error != res.getErrorCode()) {
                                //解析成功
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public BaseResult parserLoginSms(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        String url = "";
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = obj.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr, AccountInfo.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserLoginPwd(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        String url = "";
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = obj.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr, AccountInfo.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserForgetPwd(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        String url = "";
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = obj.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr, AccountInfo.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserSetPwd(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        String url = "";
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserLoginSendCode(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        String url = "";
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserArticleList(String responseStr) throws Exception {
        DiscoverList res = new DiscoverList();
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                if (!TextUtils.isEmpty(responseStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(responseStr, DiscoverList.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserArticleCollect(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        String url = "";
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserProductList(String responseStr) throws Exception {
        ProductModelList res = new ProductModelList();
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                if (!TextUtils.isEmpty(responseStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(responseStr, ProductModelList.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserGetCollect(String responseStr) throws Exception {
        ProductModelList res = new ProductModelList();
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                if (!TextUtils.isEmpty(responseStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(responseStr, ProductModelList.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserGetDevice(String responseStr) throws Exception {
        DeviceList res = new DeviceList();
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                if (!TextUtils.isEmpty(responseStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(responseStr, DeviceList.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return res;
    }

    @Override
    public BaseResult parserLoginout(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        String url = "";
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserWeixinLogin(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        String url = "";
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK || errorCode == 1200) {
                if (errorCode == DcError.DC_OK) {
                    String dataStr = obj.optString(StringConstant.JSON_DATA);
                    if (!TextUtils.isEmpty(dataStr)) {
                        Gson gson = new Gson();
                        res = gson.fromJson(dataStr, AccountInfo.class);
                    }
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserWeixinLoginBindphone(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        String url = "";
        try {

            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = obj.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr, AccountInfo.class);
                }

                res.setErrorCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }


    /**
     * 表头数据解析
     * 说明: 兼容返回结果数据（带接口名、不带接口名的情况）
     *
     * @param jsonObj : JSON对象
     * @param res     : 保存解析头部的结果
     */
    private void parserHeader(JSONObject jsonObj, NetProtocolHeader res) {
        try {
            if (null != jsonObj) {
                res.setErrorCode(jsonObj.optInt(StringConstant.JSON_ERROR_CODE, DcError.DC_Error));
                res.setErrorDesc(jsonObj.optString(StringConstant.JSON_ERROR_MESSAGE));
                res.setInfo(jsonObj.optString(StringConstant.JSON_DATA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 表头数据解析
     */
    private void parserHeader(JSONObject jsonObj, BaseResult res)
            throws Exception {
        int errorcode = -1;
        String errormsg = "";
        String commonServerErrorUrl = "";
        try {
            if (null != jsonObj) {
                errorcode = jsonObj.optInt(StringConstant.JSON_ERROR_CODE);
                errormsg = jsonObj.optString(StringConstant.JSON_ERROR_MESSAGE);
                if (errorcode != DcError.DC_OK) {
                    if(errorcode == 1100){
                        GaiaApplication.getInstance().clearActivities();
                        Intent intent = new Intent(GaiaApplication.getInstance().getApplicationContext(), LoginMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        GaiaApplication.getInstance().getApplicationContext().startActivity(intent);
                    }
                    JSONObject data = jsonObj
                            .optJSONObject(StringConstant.JSON_DATA);
                    if (data != null) {
                        commonServerErrorUrl = data.optString(StringConstant.JSON_ERROR_SERVER_COMMON_URL);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            res.setErrorCode(errorcode);
            res.setErrorString(errormsg);
            res.setServerErrorUrl(commonServerErrorUrl);
        }
    }

}
