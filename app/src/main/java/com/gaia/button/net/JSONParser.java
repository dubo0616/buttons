package com.jindan.p2p.json;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jindan.p2p.json.model.ActiveDetailModel;
import com.jindan.p2p.json.model.ActivityFinalInfoModel;
import com.jindan.p2p.json.model.ActivityInfoModel;
import com.jindan.p2p.json.model.ActivityPopInfoModel;
import com.jindan.p2p.json.model.AdvertItemInfoModel;
import com.jindan.p2p.json.model.AdvertListInfoModel;
import com.jindan.p2p.json.model.AnnouncementMainModel;
import com.jindan.p2p.json.model.BankAccountModel;
import com.jindan.p2p.json.model.BankCardsListInfoModel;
import com.jindan.p2p.json.model.BankInfoModel;
import com.jindan.p2p.json.model.BankListInfoModel;
import com.jindan.p2p.json.model.BannerMainItemModel;
import com.jindan.p2p.json.model.BannerMainListModel;
import com.jindan.p2p.json.model.BindBankInfoModel;
import com.jindan.p2p.json.model.CouponModel;
import com.jindan.p2p.json.model.CouponsListInfo;
import com.jindan.p2p.json.model.CrashDomainItemModel;
import com.jindan.p2p.json.model.CrashDomainModel;
import com.jindan.p2p.json.model.CreditDetailInfoModel;
import com.jindan.p2p.json.model.DefaultPostDataUrlInfoModel;
import com.jindan.p2p.json.model.DiscoverGridModel;
import com.jindan.p2p.json.model.DiscoverListModel;
import com.jindan.p2p.json.model.DiscoverMiddleItemModel;
import com.jindan.p2p.json.model.ExclusiveListModel;
import com.jindan.p2p.json.model.FinanceIndexBiddingListItemListModel;
import com.jindan.p2p.json.model.FinanceIndexBiddingListModel;
import com.jindan.p2p.json.model.GoodsInfoModel;
import com.jindan.p2p.json.model.GoodsListInfoModel;
import com.jindan.p2p.json.model.HelpCommonModel;
import com.jindan.p2p.json.model.IncomeStatusInfoModel;
import com.jindan.p2p.json.model.IncomeStatusListInfoModel;
import com.jindan.p2p.json.model.IndexButtonMenuInfo;
import com.jindan.p2p.json.model.InvestConfigModel;
import com.jindan.p2p.json.model.InvestItemInfoModel;
import com.jindan.p2p.json.model.MainBiaoInfoModel;
import com.jindan.p2p.json.model.MainBiaoItemModel;
import com.jindan.p2p.json.model.MainBiaoListModel;
import com.jindan.p2p.json.model.MainGoodsListModel;
import com.jindan.p2p.json.model.MainIndexBiddingListModel;
import com.jindan.p2p.json.model.MainIndexBiddingModel;
import com.jindan.p2p.json.model.NewCreditModel;
import com.jindan.p2p.json.model.NewCreditModel.CreditItem;
import com.jindan.p2p.json.model.OwerListItemModel;
import com.jindan.p2p.json.model.OwerListModel;
import com.jindan.p2p.json.model.ProductImage;
import com.jindan.p2p.json.model.PromoteMainItemModel;
import com.jindan.p2p.json.model.PromoteMainListModel;
import com.jindan.p2p.json.model.PushNewsInfoModel;
import com.jindan.p2p.json.model.PushNewsListInfoModel;
import com.jindan.p2p.json.model.PushNewsTipInfoModel;
import com.jindan.p2p.json.model.PushNewsTipInfoModel.PushNewsTipModel;
import com.jindan.p2p.json.model.RechargeConfigModel;
import com.jindan.p2p.json.model.RechargeOrderIdModel;
import com.jindan.p2p.json.model.RedeemConfigModel;
import com.jindan.p2p.json.model.RedeemItemInfoModel;
import com.jindan.p2p.json.model.RedeemOrderInfoModel;
import com.jindan.p2p.json.model.RefreshTipsListModel;
import com.jindan.p2p.json.model.RegionInfoModel;
import com.jindan.p2p.json.model.ResourceModel;
import com.jindan.p2p.json.model.RestLoginPwdVerifyCodeModel;
import com.jindan.p2p.json.model.RewardInfoModel;
import com.jindan.p2p.json.model.RewardListInfoModel;
import com.jindan.p2p.json.model.SetNewsReadInfoModel;
import com.jindan.p2p.json.model.SettingMenuInfo;
import com.jindan.p2p.json.model.SoftUpdateInfoModel;
import com.jindan.p2p.json.model.TodayBiddingRecord;
import com.jindan.p2p.json.model.TotalAmountModel;
import com.jindan.p2p.json.model.UserBankCardModel;
import com.jindan.p2p.json.model.UserConfigNewModel;
import com.jindan.p2p.json.model.UserModel.UserInfo;
import com.jindan.p2p.json.model.WithdrawConfigModel;
import com.jindan.p2p.json.model.WithdrawOrderInfoModel;
import com.jindan.p2p.net.BaseResult;
import com.jindan.p2p.utils.ConstantUtil;
import com.jindan.p2p.utils.DcError;
import com.jindan.p2p.utils.LogUtil;
import com.jindan.p2p.utils.StringConstant;
import com.jindan.p2p.utils.ToastFactory;
import com.jindan.p2p.utils.Utils;
import com.jindan.p2p.view.model.HomeReadMeModel;
import com.jindan.p2p.view.model.LoginResource;

import org.json.JSONArray;
import org.json.JSONObject;

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
            if(null != jsonObj) {
                //第一层为结果数据
                parserHeader(jsonObj, res);
                if(DcError.DC_Error == res.getErrorCode()) {
                    //第一层为接口名(接口名下为结果数据)
                    Iterator<String> it =  jsonObj.keys();
                    while (it.hasNext()) {
                        //查找每一个子节点
                        String key = it.next();
                        JSONObject jsonFun = jsonObj.getJSONObject(key);
                        if(null != jsonFun) {
                            parserHeader(jsonFun, res);
                            if(DcError.DC_Error != res.getErrorCode()) {
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

    /**
     * 表头数据解析
     * 说明: 兼容返回结果数据（带接口名、不带接口名的情况）
     *
     * @param jsonObj : JSON对象
     * @param res : 保存解析头部的结果
     */
    private void parserHeader(JSONObject jsonObj, NetProtocolHeader res) {
        try {
            if(null != jsonObj) {
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
	 * 
	 */
    private void parserHeader(JSONObject jsonObj, BaseResult res)
            throws Exception {
        int errorcode = -1;
        String errormsg = "";
        String commonServerErrorUrl = "";
        try {
            if(null != jsonObj) {
                errorcode = jsonObj.optInt(StringConstant.JSON_ERROR_CODE);
                errormsg = jsonObj.optString(StringConstant.JSON_ERROR_MESSAGE);
                if (errorcode != DcError.DC_OK) {
                    JSONObject data = jsonObj
                            .optJSONObject(StringConstant.JSON_DATA);
                    if (data != null) {
                        commonServerErrorUrl = data.optString(StringConstant.JSON_ERROR_SERVER_COMMON_URL);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("parserHeader :" + e.toString());
            throw new Exception(e);
        } finally {
            res.setErrorCode(errorcode);
            res.setErrorString(errormsg);
            res.setServerErrorUrl(commonServerErrorUrl);
        }
    }

    /**
     * tag 3 user register with phone number
     *
     * @param responseStr
     * @return
     * @throws Exception
     */
    public UserInfo parserUserRegistWithPhoneNumberResponse(String responseStr)
            throws Exception {
        UserInfo res = new UserInfo();
        try {

            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj
                    .optJSONObject(ConstantUtil.SERVER_URL_NAME_REGISTER);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                JSONObject data = funObject
                        .optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), UserInfo.class);
                    res.setUser_state(ConstantUtil.USER_STATE_LOGIN);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            throw new Exception(e);
        }
        return res;
    }

    /**
     * tag 4 User login
     *
     * @param responseStr
     * @return
     * @throws Exception
     */
    public UserInfo parserLoginBySmsResponse(String responseStr)
            throws Exception {
        LogUtil.d("parserLoginResponse--------responseStr-=" + responseStr);
        UserInfo res = new UserInfo();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj
                    .optJSONObject(ConstantUtil.SERVER_URL_NAME_LOGIN_SMS);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                JSONObject data = funObject
                        .optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    // parseUserInfoJson(res, data);
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), UserInfo.class);
                    res.setUser_state(ConstantUtil.USER_STATE_LOGIN);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;

    }

    /**
     * tag 4 User login
     *
     * @param responseStr
     * @return
     * @throws Exception
     */
    public UserInfo parserLoginByPwdResponse(String responseStr)
            throws Exception {
        LogUtil.d("parserLoginResponse--------responseStr-=" + responseStr);
        UserInfo res = new UserInfo();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj
                    .optJSONObject(ConstantUtil.SERVER_URL_NAME_LOGIN_PWD);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                JSONObject data = funObject
                        .optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    // parseUserInfoJson(res, data);
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), UserInfo.class);
                    res.setUser_state(ConstantUtil.USER_STATE_LOGIN);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;

    }

    /**
     * tag 4 User login
     *
     * @param responseStr
     * @return
     * @throws Exception
     */
    public UserInfo parserUserInfoResponse(String responseStr) throws Exception {
        LogUtil.d("parserLoginResponse--------responseStr-=" + responseStr);
        UserInfo res = new UserInfo();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj
                    .optJSONObject(ConstantUtil.SERVER_URL_NAME_USER_INFO);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                JSONObject data = funObject
                        .optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    // parseUserInfoJson(res, data);
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), UserInfo.class);
                    res.setUser_state(ConstantUtil.USER_STATE_LOGIN);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;

    }

    @Override
    public UserInfo parserDefaultRegistResponse(String responseString)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseResult parserLogout(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public ActiveDetailModel parserActiveDetail(String responseStr)
            throws Exception {

        ActiveDetailModel res = new ActiveDetailModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {//
                JSONObject data = new JSONObject(
                        obj.optString(StringConstant.JSON_DATA));
                if (data != null) {
                    res.setActiveUrl(data
                            .optString(StringConstant.JSON_ACTIVE_URL));
                    res.setActiveTitle(data
                            .optString(StringConstant.JSON_ACTIVE_TITLE));
                    res.setActiveContent(data
                            .optString(StringConstant.JSON_ACTIVE_CONTENT));
                    res.setIncreaseIncome(data
                            .optString(StringConstant.JSON_ACTIVE_INCREASE_INCOME));
                    res.setAwardMoney(data
                            .optString(StringConstant.JSON_ACTIVE_AWARD_MONEY));
                    res.setAwardRate(data
                            .optString(StringConstant.JSON_ACTIVE_AWARD_RATE));
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public ActivityInfoModel parserActivityNewInfo(String responseStr)
            throws Exception {

        ActivityInfoModel res = new ActivityInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {//
                JSONObject data = new JSONObject(
                        obj.optString(StringConstant.JSON_DATA));
                if (data != null) {

                    // "active_id": "2",
                    // "title": "8d4f91d16d3b52a8",
                    // "content":
                    // "67009ad883b78d60<font color=\"red\">40000</font>514372796743672c91d153ca<font color=\"red\">13</font>%653676ca7387",
                    // "url":
                    // "67009ad883b78d60<font color=\"red\">40000</font>514372796743672c91d153ca<font color=\"red\">13</font>%653676ca7387",
                    // "max_money": "5",
                    // "max_rate": "9.00",
                    // "person_count": "12321",
                    // "need_invite": "0",
                    // "user_active_money": "0",
                    // "user_active_rate": "0",
                    // "user_active_need_invite": "0",
                    // "user_active_invite": "0",
                    // "has_end": "0"

                    // new
                    // "data": {
                    // "active_id": "2",
                    // "title": "8d4f91d16d3b52a8",
                    // "content":
                    // "67009ad883b78d60<font color=\"red\">40000</font>514372796743672c91d153ca<font color=\"red\">13%</font>653676ca7387",
                    // "url":
                    // "http://m.jindanlicai.com/wechat_activity/sharepage?sh=326&ai=2&ck=e53c16d493aef7f7fe2e558fa6080b84",
                    // "max_money": 40000,
                    // "max_rate": "8.0",
                    // "person_count": "284",
                    // "active_progress": "20.0",
                    // "user_active_rate": "9.0",
                    // "has_end": "0"

                    res.setActivityId(data
                            .optString(StringConstant.JSON_ACTIVE_ID));
                    res.setTitle(data
                            .optString(StringConstant.JSON_ACTIVE_TITLE));
                    res.setContent(data
                            .optString(StringConstant.JSON_ACTIVE_CONTENT));
                    res.setUrl(data.optString(StringConstant.JSON_ACTIVE_URL));
                    res.setMaxMoney(data
                            .optString(StringConstant.JSON_ACTIVITY_MAXMONEY));
                    res.setMaxRate(data
                            .optString(StringConstant.JSON_ACTIVITY_MAXRATE));
                    res.setPersonCount(data
                            .optString(StringConstant.JSON_ACTIVITY_PERSON_COUNT));
                    // res.setNeedInvite(data
                    // .optString(StringConstant.JSON_ACTIVITY_NEED_PERSON));

                    res.setUserActiveRate(data
                            .optString(StringConstant.JSON_ACTIVITY_USER_ACTIVE_RATE));
                    res.setHasEnd(data
                            .optString(StringConstant.JSON_ACTIVITY_HAS_END));
                    res.setProgress(data.optString("active_progress"));
                    res.setStatus(data.optInt("status"));

                    res.setHasJoin(data.optString("has_join"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;

    }

    @Override
    public GoodsInfoModel parserGoodsInfo(String responseStr) throws Exception {

        GoodsInfoModel res = new GoodsInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {//
                JSONObject data = new JSONObject(
                        obj.optString(StringConstant.JSON_DATA));
                if (data != null) {

                    // "goods_id": "2",
                    // "title": "62db724c91d186cb7b2c1671f",
                    // "content":
                    // "99966b2162958d4476f453475e745316653676ca738781f3<font color=\"red\">8.5</font>%",
                    // "total_money": "500",
                    // "income_rate": "8.0",
                    // "start_time": "2011-12-17",
                    // "end_time": "1973-11-29",
                    // "total_member": "6545",
                    // "invest_progress": 39,
                    // "status": 0
                    res.setTitle(data
                            .optString(StringConstant.JSON_GOODS_TITLE));
                    res.setGoodsId(data.optString(StringConstant.JSON_GOODS_ID));
                    res.setTotalMoney(data
                            .optString(StringConstant.JSON_GOODS_MONEY));
                    res.setInvestPersonCount(data
                            .optString(StringConstant.JSON_GOODS_INVEST_COUNT));
                    res.setRate(data.optString(StringConstant.JSON_GOODS_RATE));
                    res.setStatus(data
                            .optString(StringConstant.JSON_GOODS_STATE));
                    res.setStatusText(data
                            .optString(StringConstant.JSON_GOODS_STATE_TEXT));
                    res.setStartTime(data
                            .optString(StringConstant.JSON_GOODS_START_TIME));
                    res.setContent(data
                            .optString(StringConstant.JSON_GOODS_CONTENT));
                    res.setEndTime(data
                            .optString(StringConstant.JSON_GOODS_END_TIME));
                    res.setInvestProgress(data
                            .optString(StringConstant.JSON_GOODS_INVEST_PROGRESS));

                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public GoodsInfoModel parserGoodsDetail(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        GoodsInfoModel res = new GoodsInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = new JSONObject(
                        obj.optString(StringConstant.JSON_DATA));
                if (data != null) {
                    res.setTitle(data
                            .optString(StringConstant.JSON_GOODS_TITLE));
                    res.setGoodsId(data.optString(StringConstant.JSON_GOODS_ID));
                    res.setTotalMoney(data
                            .optString(StringConstant.JSON_GOODS_MONEY));
                    res.setInvestPersonCount(data
                            .optString(StringConstant.JSON_GOODS_INVEST_COUNT));
                    res.setRate(data.optString(StringConstant.JSON_GOODS_RATE));
                    res.setStatus(data
                            .optString(StringConstant.JSON_GOODS_STATE));
                    res.setStatusText(data
                            .optString(StringConstant.JSON_GOODS_STATE_TEXT));
                    res.setStartTime(data
                            .optString(StringConstant.JSON_GOODS_START_TIME));
                    res.setContent(data
                            .optString(StringConstant.JSON_GOODS_CONTENT));
                    res.setEndTime(data
                            .optString(StringConstant.JSON_GOODS_END_TIME));
                    res.setInvestProgress(data
                            .optString(StringConstant.JSON_GOODS_INVEST_PROGRESS));
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public InvestItemInfoModel parserInvestInfo(String responseStr)
            throws Exception {

//        "createInvestOrder": {
//                        "error_code": 0,
//                                "message": "",
//                                "data": {
//                            "orderId": "xxxxxxxxxxx",
//                                    "desc_top": "<font color='#6a6d75' >6个月随心金蛋投资接口</font>",
//                                    "desc_bottom": "<font color='#6a6d75' >每日可获得</font><font color='#df4b3c'>0.05元</font><font color='#6a6d75' >收益</font>"
//                        }
//                    }

        InvestItemInfoModel res = new InvestItemInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_INVEST_CREATE);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), InvestItemInfoModel.class);
                    res.setErrorCode(0);
                }
            } else {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (isJsonNotEmpty(dataStr)) {
                    JSONObject jsonObject = new JSONObject(dataStr);
                    res.setOther(jsonObject.optString("lackMoney"));
                    res.setServerErrorUrl(jsonObject.optString("url"));
                    res.setInfo(jsonObject.optString("postContent"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public GoodsListInfoModel parserGoodsListInfo(String responseStr)
            throws Exception {

        GoodsListInfoModel result = new GoodsListInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, result);
            int errorCode = result.getErrorCode();
            if (errorCode == DcError.DC_OK) {//
                JSONArray array = obj.optJSONArray(StringConstant.JSON_DATA);

                // "goods_id": "66",
                // "title": "62db724c91d186cb7b2c33671f00b76d3b671f",
                // "content":
                // "99966295534781f3<font color=\"red\">8.5%</font>,7eed6295534781f3<font color=\"red\">9.0%</font>",
                // "start_time": "2015-01-26 16:00",
                // "end_time": "2015-05-22 11:46",
                // "income_rate": "8.0",
                // "total_member": "508",
                // "total_money": 100,
                // "is_status": "8ba48d2d4e2d",
                // "invest_progress": "90.19",
                // "status": 0

                ArrayList<GoodsInfoModel> list = new ArrayList<GoodsInfoModel>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    if (data != null) {
                        // res.setTitle(data.optString(StringConstant.JSON_GOODS_TITLE));
                        GoodsInfoModel res = new GoodsInfoModel();

                        res.setTitle(data
                                .optString(StringConstant.JSON_GOODS_TITLE));
                        res.setGoodsId(data
                                .optString(StringConstant.JSON_GOODS_ID));
                        res.setTotalMoney(data
                                .optString(StringConstant.JSON_GOODS_MONEY));
                        res.setInvestPersonCount(data
                                .optString(StringConstant.JSON_GOODS_INVEST_COUNT));
                        res.setRate(data
                                .optString(StringConstant.JSON_GOODS_RATE));
                        res.setStatus(data
                                .optString(StringConstant.JSON_GOODS_STATE));
                        res.setStatusText(data
                                .optString(StringConstant.JSON_GOODS_STATE_TEXT));
                        res.setStartTime(data
                                .optString(StringConstant.JSON_GOODS_START_TIME));
                        res.setContent(data
                                .optString(StringConstant.JSON_GOODS_CONTENT));
                        res.setEndTime(data
                                .optString(StringConstant.JSON_GOODS_END_TIME));
                        res.setInvestProgress(data
                                .optString(StringConstant.JSON_GOODS_INVEST_PROGRESS));
                        res.setOverTime(data.getString("next_timestamp"));
                        list.add(res);
                    }
                }
                result.setList(list);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return result;
    }

    @Override
    public IncomeStatusListInfoModel parserCVStatusListInfo(String responseStr)
            throws Exception {

        IncomeStatusListInfoModel result = new IncomeStatusListInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, result);
            int errorCode = result.getErrorCode();
            if (errorCode == DcError.DC_OK) {//

                // JSONObject json =
                // obj.optJSONObject(StringConstant.JSON_DATA);
                // result.setTotalMoney(json.optString("total_money"));

                JSONArray array = obj.optJSONArray(StringConstant.JSON_DATA);

                ArrayList<IncomeStatusInfoModel> list = new ArrayList<IncomeStatusInfoModel>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    if (data != null) {
                        // new
                        // "uci_id": "3",
                        // "money": "120.00",
                        // "content": "590d62954e2d",
                        // "content_bg_color": "#000000",
                        // "timestamp": "1431100800",
                        // "time": "2015-05-09"

                        IncomeStatusInfoModel res = new IncomeStatusInfoModel();
                        res.setIncomeId(data.optString("uci_id"));
                        res.setContent(data
                                .optString(StringConstant.JSON_INCOME_CREDIT_CONTENT));
                        res.setMoney(data
                                .optString(StringConstant.JSON_INCOME_CREDIT_MONEY));
                        res.setCreateTime(data
                                .optString(StringConstant.JSON_INCOME_CREDIT_TIME));
                        res.setContentBgColor(data
                                .optString("content_bg_color"));
                        res.setServerTime(data.optString("timestamp"));
                        list.add(res);
                    }
                }
                result.setList(list);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return result;
    }

    @Override
    public RewardListInfoModel parserRewardListInfo(String responseStr)
            throws Exception {

        RewardListInfoModel result = new RewardListInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, result);
            int errorCode = result.getErrorCode();
            if (errorCode == DcError.DC_OK) {//

                // JSONObject json =
                // obj.optJSONObject(StringConstant.JSON_DATA);
                // result.setTotalMoney(json.optString("total_money"));

                JSONArray array = obj.optJSONArray(StringConstant.JSON_DATA);

                ArrayList<RewardInfoModel> list = new ArrayList<RewardInfoModel>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    if (data != null) {
                        RewardInfoModel res = new RewardInfoModel();
                        res.setRecordId(data
                                .optString(StringConstant.JSON_REWARD_LIST_RECORD_ID));

                        res.setEndTime(data
                                .optString(StringConstant.JSON_REWARD_LIST_END_TIME));
                        res.setMoney(data
                                .optString(StringConstant.JSON_REWARD_LIST_MONEY));

                        res.setTime(data
                                .optString(StringConstant.JSON_REWARD_LIST_TIME));
                        res.setUserName(data.optString("user_name"));

                        res.setDescription(data.optString("discription"));

                        res.setStatus(data.optInt("status"));
                        list.add(res);
                    }
                }
                result.setList(list);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return result;
    }

    @Override
    public BaseResult parserTotalMoneyProfitInfo(String responseStr)
            throws Exception {
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = new JSONObject(
                        obj.optString(StringConstant.JSON_DATA));
                if (data != null) {

                    String money = data.optString("total_money");
                    String profit = data.optString("user_benefit");
                    res.setInfo(money + ";" + profit);

                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserVerifyPwdInfo(String responseStr) throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public CreditDetailInfoModel parserCreditDetailInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        CreditDetailInfoModel res = new CreditDetailInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = new JSONObject(obj.optString(StringConstant.JSON_DATA));
                if (data != null) {
                    res.setCreditId(data.optString(StringConstant.JSON_CREDIT_DETAIL_CREDIT_ID));
                    res.setBackMoney(data.optString(StringConstant.JSON_CREDIT_DETAIL_BACK_MONEY));
                    res.setBackTime(data.optString(StringConstant.JSON_CREDIT_DETAIL_BACK_TIME));
                    res.setBorrowerIDCard(data.optString(StringConstant.JSON_CREDIT_DETAIL_BORROWER_IDCARD));
                    res.setBorrowerMobile(data.optString(StringConstant.JSON_CREDIT_DETAIL_BORROWER_MOBILE));
                    res.setBorrowerName(data.optString(StringConstant.JSON_CREDIT_DETAIL_BORROWER_NAME));
                    res.setCreatedTime(data.optString(StringConstant.JSON_CREDIT_DETAIL_CREATED_TIME));
                    res.setCreditOrder(data.optString(StringConstant.JSON_CREDIT_DETAIL_CREDIT_ORDER));
                    res.setEndTime(data.optString(StringConstant.JSON_CREDIT_DETAIL_END_TIME));
                    res.setLenderIDCard(data.optString(StringConstant.JSON_CREDIT_DETAIL_LENDER_IDCARD));
                    res.setLenderMobile(data.optString(StringConstant.JSON_CREDIT_DETAIL_LENDER_MOBILE));
                    res.setLenderName(data.optString(StringConstant.JSON_CREDIT_DETAIL_LENDER_NAME));
                    res.setLoanPrincipal(data.optString(StringConstant.JSON_CREDIT_DETAIL_LOAN_PRINCIPAL));
                    res.setStagesNumber(data.optString(StringConstant.JSON_CREDIT_DETAIL_STAGES_NUM));
                    res.setStartTime(data.optString(StringConstant.JSON_CREDIT_DETAIL_START_TIME));
                    res.setSumIncome(data.optString(StringConstant.JSON_CREDIT_DETAIL_SUM_INCOME));
                    res.setBackDay(data.optString("back_day"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserTotalUserSumInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = new JSONObject(obj.optString(StringConstant.JSON_DATA));
                if (data != null) {
                    String profit = data.optString("user_sum");
                    res.setInfo(profit);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

//    @Override
//    public BaseResult parserPushRegisterInfo(String responseStr)
//            throws Exception {
//        // TODO Auto-generated method stub
//        BaseResult res = new BaseResult();
//        try {
//            JSONObject obj = new JSONObject(responseStr);
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_REGISTER_TOKEN);
//            if (funObject == null) {
//                JSONObject errObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_ERROR);
//                parserHeader(errObject, res);
//            } else {
//                parserHeader(funObject, res);
//            }
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//                res.setInfo("ok");
//            }
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//        return res;
//    }

    @Override
    public GoodsListInfoModel parserGoodsNewListInfo(String responseStr)
            throws Exception {

        GoodsListInfoModel result = new GoodsListInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, result);
            int errorCode = result.getErrorCode();
            if (errorCode == DcError.DC_OK) {//
                JSONArray array = obj.optJSONArray(StringConstant.JSON_DATA);

                ArrayList<GoodsInfoModel> list = new ArrayList<GoodsInfoModel>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    if (data != null) {
                        // res.setTitle(data.optString(StringConstant.JSON_GOODS_TITLE));
                        GoodsInfoModel res = new GoodsInfoModel();

                        res.setTitle(data.optString(StringConstant.JSON_GOODS_TITLE));
                        res.setGoodsId(data.optString(StringConstant.JSON_GOODS_ID));
                        res.setTotalMoney(data.optString(StringConstant.JSON_GOODS_MONEY));
                        res.setInvestPersonCount(data.optString(StringConstant.JSON_GOODS_INVEST_COUNT));
                        res.setRate(data.optString(StringConstant.JSON_GOODS_RATE));
                        res.setStatus(data.optString(StringConstant.JSON_GOODS_STATE));
                        res.setStatusText(data.optString(StringConstant.JSON_GOODS_STATE_TEXT));
                        res.setStartTime(data.optString(StringConstant.JSON_GOODS_START_TIME));
                        res.setContent(data.optString(StringConstant.JSON_GOODS_CONTENT));
                        res.setEndTime(data.optString(StringConstant.JSON_GOODS_END_TIME));
                        res.setInvestProgress(data.optString(StringConstant.JSON_GOODS_INVEST_PROGRESS));
                        list.add(res);
                    }
                }
                result.setList(list);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return result;
    }

    @Override
    public BaseResult parserFeedbackInfo(String responseStr) throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @SuppressWarnings("unused")
	private void parseUserInfoJson(UserInfo user, JSONObject userData) {

        Gson gson = new Gson();
        user = gson.fromJson(userData.toString(), UserInfo.class);

        user.setUser_state(ConstantUtil.USER_STATE_LOGIN);
        user.setErrorCode(0);
    }

    @Override
    public AdvertListInfoModel parserAdvertLatestInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub

        AdvertListInfoModel result = new AdvertListInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            parserHeader(obj, result);
            int errorCode = result.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                JSONArray array = obj.optJSONArray(StringConstant.JSON_DATA);

                ArrayList<AdvertItemInfoModel> list = new ArrayList<AdvertItemInfoModel>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    if (data != null) {
                        AdvertItemInfoModel res = new AdvertItemInfoModel();
                        res.setAdvertBgcolor(data.optString("advert_bgColor"));
                        res.setAdvertContent(data.optString("advert_text"));
                        res.setAdvertIcon(data.optString("advert_icon"));
                        res.setAdvertId(data.optInt("advert_id"));
                        res.setAdvertUrl(data.optString("advert_url"));
                        list.add(res);
                    }
                }
                result.setList(list);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return result;
    }

    @Override
    public BaseResult parserSetLoginPwd(String responseStr) throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_LOGINPWD_SET);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

//    @Override
//    public BannerMainListModel parserBannerListInfo(String responseStr)
//            throws Exception {
//        // TODO Auto-generated method stub
//        BannerMainListModel res = new BannerMainListModel();
//        try {
//            JSONObject obj = new JSONObject(responseStr);
//
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BANNER_MAIN);
//
//            parserHeader(funObject, res);
//
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//
//                String dataStr = funObject.optString(StringConstant.JSON_DATA);
//                if (!TextUtils.isEmpty(dataStr)) {
//                    // parseUserInfoJson(res, data);
//                    Gson gson = new Gson();
//                    ArrayList<BannerMainItemModel> list = gson.fromJson(
//                            dataStr,
//                            new TypeToken<List<BannerMainItemModel>>() { }.getType());
//                    res.setBannerMainItemModel(list);
//                    res.setErrorCode(0);
//                }
//            }
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//        return res;
//    }


//    @Override
//    public AnnouncementMainModel parserAnnouncementMainInfo(String responseStr)
//            throws Exception {
//        // TODO Auto-generated method stub
//        AnnouncementMainModel res = new AnnouncementMainModel();
//        try {
//            JSONObject obj = new JSONObject(responseStr);
//
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_ANNOUNCEMENT_MAIN);
//
//            parserHeader(funObject, res);
//
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//
//                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
//                if (data != null) {
//                    Gson gson = new Gson();
//                    res = gson.fromJson(data.toString(), AnnouncementMainModel.class);
//                    res.setErrorCode(0);
//                }
//            }
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//        return res;
//    }

    @Override
    public TotalAmountModel parserTotalAmountInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        TotalAmountModel res = new TotalAmountModel();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_TOTAL_AMOUNT);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), TotalAmountModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserRechargeOrderIdInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_INVEST_CREATE);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {

                    String orderId = data.optString(StringConstant.JSON_ORDER_ID);
                    try {
                        Gson gson = new Gson();
                        RedeemItemInfoModel dataObj = gson.fromJson(
                                data.toString(), RedeemItemInfoModel.class);
                        res.setOther(dataObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    res.setInfo(orderId);
                    res.setErrorCode(0);
                }
            } else {
                try {

                    ResourceModel.ImageResourceModel data = new ResourceModel().new ImageResourceModel();
                    JSONObject dataObject = funObject.optJSONObject(StringConstant.JSON_DATA);
                    data.setTitle(dataObject.optString("title"));
                    data.setDesc(dataObject.optString("desc"));
                    data.setUrl(dataObject.optString("url"));
                    data.setImg("");
                    res.setOther(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public MainGoodsListModel parserMainBiaoListInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        MainGoodsListModel res = new MainGoodsListModel();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BIAO_LIST_MAIN);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    ArrayList<MainBiaoInfoModel> list = gson.fromJson(dataStr,
                            new TypeToken<List<MainBiaoInfoModel>>() {
                            }.getType());
                    res.setList(list);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserBiaoDetailInfo(String responseStr) throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BIAO_DETAIL);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo(funObject.getString(StringConstant.JSON_DATA));
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public MainBiaoListModel parserBiaoHistoryListInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        MainBiaoListModel res = new MainBiaoListModel();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BIAO_HISTORY_LIST);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    ArrayList<MainBiaoItemModel> list = gson.fromJson(dataStr,
                            new TypeToken<List<MainBiaoItemModel>>() {
                            }.getType());
                    res.setList(list);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BankListInfoModel parserBankListInfo(String responseStr)
            throws Exception {

        BankListInfoModel res = new BankListInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BANK_LIST);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    ArrayList<BankInfoModel> list = gson.fromJson(dataStr,
                            new TypeToken<List<BankInfoModel>>() {
                            }.getType());
                    res.setList(list);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    /**
     * 绑卡优化解析
     */
    @Override
    public UserBankCardModel parserBindBankCard(String responseStr)
            throws Exception {

    	UserBankCardModel res = new UserBankCardModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BANK_BIND);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), UserBankCardModel.class);
                    res.setErrorCode(0);
                }
            } else {
                try {

                    JSONObject data = funObject
                            .optJSONObject(StringConstant.JSON_DATA);
                    if (data != null) {
                        // res.setBind_by_money_alert(data.getString("bind_by_money_alert"));
                        Gson gson = new Gson();
                        UserBankCardModel resError = gson.fromJson(data.toString(), UserBankCardModel.class);
                        resError.setErrorCode(errorCode);
                        resError.setErrorString(res.getErrorString());
                        res = resError;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public UserBankCardModel parserBindCardVerifyInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        UserBankCardModel res = new UserBankCardModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BANK_BIND_VERIFY);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), UserBankCardModel.class);
                }
                res.setErrorCode(0);
            } else {
                try {
                    JSONObject data = funObject
                            .optJSONObject(StringConstant.JSON_DATA);
                    if (data != null) {
                        Gson gson = new Gson();
                        UserBankCardModel resError = gson.fromJson(data.toString(), UserBankCardModel.class);
                        resError.setErrorCode(errorCode);
                        resError.setErrorString(res.getErrorString());
                        res = resError;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserSMSCode(String responseStr) throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_TAG_8);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserBindCardGetSMSCode(String responseStr)
            throws Exception {
        BaseResult res = new BaseResult();
        try {

            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BANK_BIND_GET_VERIFY_CODE);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public RedeemItemInfoModel parserRedeemNewInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        RedeemItemInfoModel res = new RedeemItemInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_PAY_REDEEM);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), RedeemItemInfoModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public RedeemItemInfoModel parserInvestNewInfo(String responseStr)
            throws Exception {
        RedeemItemInfoModel res = new RedeemItemInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = null;
            if (ConstantUtil.Is_Old_Version) {
                funObject = obj.optJSONObject("payInvest");
            } else {
                funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_PAY_INVEST);
            }
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), RedeemItemInfoModel.class);
                    res.setErrorCode(0);
                }
            } else {
//            	 "data": {
//               "redirectUrl": "jindanlicai://finance/home"
            	JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                	String url = data.optString("redirectUrl");
//                	res.setInfo(url);
                    res.setServerErrorUrl(url);
                    res.setErrorCode(errorCode);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public RedeemOrderInfoModel parserRedeemOrderIdInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        RedeemOrderInfoModel res = new RedeemOrderInfoModel();

        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_REDEEM_ORDERID);
            try {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr, RedeemOrderInfoModel.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res == null) {
                res = new RedeemOrderInfoModel();
            }
            parserHeader(funObject, res);
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserRedeemListInfo(String responseStr) throws Exception {

        BaseResult res = new BaseResult();

        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_LIST_HTML_DOM);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    res.setInfo(data);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;

    }

    @Override
    public BaseResult parserInvestListInfo(String responseStr) throws Exception {

        BaseResult res = new BaseResult();

        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_LIST_HTML_DOM);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    res.setInfo(data);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserInvestStatusListInfo(String responseStr)
            throws Exception {

        BaseResult res = new BaseResult();

        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_LIST_HTML_DOM);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    res.setInfo(data);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserIncomeStatusListInfo(String responseStr)
            throws Exception {

        BaseResult res = new BaseResult();

        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_LIST_HTML_DOM);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    res.setInfo(data);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserDiscoveryHtmlInfo(String responseStr)
            throws Exception {
        BaseResult res = new BaseResult();

        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_DISCOVERY);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    res.setInfo(data);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

//    @Override
//    public BaseResult parserSaftyHtmlInfo(String responseStr) throws Exception {
//        BaseResult res = new BaseResult();
//
//        try {
//            JSONObject obj = new JSONObject(responseStr);
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_SAFTY);
//            parserHeader(funObject, res);
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//                String data = funObject.optString(StringConstant.JSON_DATA);
//                if (!TextUtils.isEmpty(data)) {
//                    res.setInfo(data);
//                    res.setErrorCode(0);
//                }
//            }
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//        return res;
//    }

//    @Override
//    public PushNewsListInfoModel parserNewsPushListInfo(String responseStr)
//            throws Exception {
//        PushNewsListInfoModel res = new PushNewsListInfoModel();
//        try {
//            JSONObject obj = new JSONObject(responseStr);
//
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_NEWS_LIST);
//
//            parserHeader(funObject, res);
//
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//
//                String dataStr = funObject.optString(StringConstant.JSON_DATA);
//                if (!TextUtils.isEmpty(dataStr)) {
//                    Gson gson = new Gson();
//                    ArrayList<PushNewsInfoModel> list = gson.fromJson(dataStr,
//                            new TypeToken<List<PushNewsInfoModel>>() {
//                            }.getType());
//                    res.setList(list);
//                    res.setErrorCode(0);
//                }
//            }
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//        return res;
//    }

    /*
     * tag = 5 parser modify pwd token
     */
    public BaseResult parserModifyPwdResponse(String responseStr)
            throws Exception {
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_PAYPWD_SET);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    /*
     * tag = 5 parser modify pwd token
     */
    public BaseResult parserResetPayPwdResponse(String responseStr)
            throws Exception {
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_PAYPWD_RESET);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    /*
     * tag = 5 parser modify pwd token
     */
    public BaseResult parserResetLoginPwdResponse(String responseStr)
            throws Exception {
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_LOGINPWD_RESET);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserGetSmsCodeResetLoginPwdResponse(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_SMS_CODE_LOGINPWD_RESET);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject dataStr = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (dataStr != null) {
                    res.setInfo(dataStr.optString("user_name"));
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    /*
     * tag = 5 parser modify pwd token
     */
    public BaseResult parserVerifySmsCodeResetLoginPwdResponse(
            String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj
                    .optJSONObject(ConstantUtil.SERVER_URL_NAME_SMS_CODE_VERIFY_LOGINPWD_RESET);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
                JSONObject dataStr = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (dataStr != null) {
                    try {
                        res.setInfo(dataStr.optString("ck"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        RestLoginPwdVerifyCodeModel model = new RestLoginPwdVerifyCodeModel();
                        model.setCard_state(dataStr.optString("card_state"));
                        model.setUser_name(dataStr.optString("user_name"));
                        res.setOther(model);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserCheckIdCardResetPwdResponse(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_CHECK_IDCARD_RESET);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
                JSONObject dataStr = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (dataStr != null) {
                    res.setInfo(dataStr.optString("ck"));
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserCheckOldPwdResetPwdResponse(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_CHECK_PAY_PWD);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

//    @Override
//    public NewsCountModel parserNewsCountInfo(String responseStr)
//            throws Exception {
//        // TODO Auto-generated method stub
//        NewsCountModel res = new NewsCountModel();
//        try {
//            JSONObject obj = new JSONObject(responseStr);
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_NEWS_COUNT_UNREAD);
//            parserHeader(funObject, res);
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
//                if (data != null) {
//                    Gson gson = new Gson();
//                    res = gson.fromJson(data.toString(), NewsCountModel.class);
//                    res.setErrorCode(0);
//                }
//            }
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//        return res;
//    }

    @Override
    public BaseResult parserPreOrderInfo(String responseStr) throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_PRE_ORDER);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("ok");
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public ResourceModel parserImageResource(String responseStr) throws Exception {
        ResourceModel res = new ResourceModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_IMAGE_RESOURCE);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), ResourceModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public SoftUpdateInfoModel parserSoftUpdateInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        SoftUpdateInfoModel res = new SoftUpdateInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_CHECK_FOR_UPDATE);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), SoftUpdateInfoModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

//    @Override
//    public ActivityPopInfoModel parserActiveFinalInfo(String responseStr)
//            throws Exception {
//        // TODO Auto-generated method stub
//
//        ActivityPopInfoModel res = new ActivityPopInfoModel();
//        try {
//            JSONObject obj = new JSONObject(responseStr);
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_ACTIVITY_POP_INFO);
//            parserHeader(funObject, res);
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
//                if (data != null) {
//                    Gson gson = new Gson();
//                    res = gson.fromJson(data.toString(), ActivityPopInfoModel.class);
//                    res.setErrorCode(0);
//                }
//            }
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//        return res;
//    }

    @Override
    public ActivityFinalInfoModel parserActiveShareInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        ActivityFinalInfoModel res = new ActivityFinalInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_SHARE_INFO);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), ActivityFinalInfoModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserUploadLogInfo(String responseStr) throws Exception {
        BaseResult res = new BaseResult();

//        {"error_code":0,"data":[],"message":"log ok"}

        try {
            JSONObject obj = new JSONObject(responseStr);

            if (responseStr.contains(ConstantUtil.SERVER_URL_NAME_USER_BEHAVIOR_LOG)) {
                JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_USER_BEHAVIOR_LOG);

                parserHeader(funObject, res);

                int errorCode = res.getErrorCode();
                if (errorCode == DcError.DC_OK) {
                    res.setInfo("ok");
                }
            } else {
                if (responseStr.contains("\"error_code\":0")) {
                    JSONObject obj2 = new JSONObject(responseStr);
                    parserHeader(obj2, res);
                    int errorCode = res.getErrorCode();
                    if (errorCode == DcError.DC_OK) {
                        res.setInfo("ok");
                    }
                }
            }


        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
//            throw new Exception(e);
        }
        return res;
    }

    /**
     * 解析金额验证
     */
    @Override
    public BindBankInfoModel parserBindCardVerifyMoney(String responseStr)
            throws Exception {
        BindBankInfoModel res = new BindBankInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BIND_CARD_VERIFY_BY_MONEY);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                // res.setErrorCode(0);
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), BindBankInfoModel.class);
                }
                res.setErrorCode(0);
            } else {

                try {

                    JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                    if (data != null) {
                        Gson gson = new Gson();
                        BindBankInfoModel resError = gson.fromJson(data.toString(),
                                BindBankInfoModel.class);
                        resError.setErrorCode(errorCode);
                        resError.setErrorString(res.getErrorString());

                        res = resError;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BindBankInfoModel parserBindCardGetMoney(String responseStr)
            throws Exception {
        BindBankInfoModel res = new BindBankInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj
                    .optJSONObject(ConstantUtil.SERVER_URL_NAME_BIND_CARD_BY_MONEY);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), BindBankInfoModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    // 换绑卡
    @Override
    public UserBankCardModel parserBindCardNewGetMoney(String responseStr)
            throws Exception {
    	UserBankCardModel res = new UserBankCardModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_New_NAME_BANK_BIND);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), UserBankCardModel.class);
                }
                res.setErrorCode(0);
            } else {
            	try {
                    JSONObject data = funObject
                            .optJSONObject(StringConstant.JSON_DATA);
                    if (data != null) {
                        Gson gson = new Gson();
                        UserBankCardModel resError = gson.fromJson(data.toString(), UserBankCardModel.class);
                        resError.setErrorCode(errorCode);
                        resError.setErrorString(res.getErrorString());
                        res = resError;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }
    
    @Override
    public BindBankInfoModel parserBindCardStatus(String responseStr)
    		throws Exception {
    	BindBankInfoModel res = new BindBankInfoModel();
    	try {
    		JSONObject obj = new JSONObject(responseStr);
    		JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BANK_BIND_STATUS);
    		parserHeader(funObject, res);
    		int errorCode = res.getErrorCode();
    		if (errorCode == DcError.DC_OK) {
    			JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
    			if (data != null) {
    				Gson gson = new Gson();
    				res = gson.fromJson(data.toString(), BindBankInfoModel.class);
    			}
    			res.setErrorCode(0);
    		}
    	} catch (Exception e) {
    		LogUtil.e(e.toString());
    		e.printStackTrace();
    		throw new Exception(e);
    	}
    	return res;
    }

    @Override
    public ProductImage parserGetProductImage(String responseStr)
            throws Exception {

        ProductImage res = new ProductImage();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.GET_PRODUCT_IMAGE);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    JSONObject imageData = data.optJSONObject(StringConstant.JSON_DATA_720_IMAGE);
                    if (imageData != null) {
                        if(imageData.has("ad_id"))
                        res.setId(imageData.getInt("ad_id"));
                        if(imageData.has("image_url"))
                        res.setUrl(imageData.getString("image_url"));
                        if(imageData.has("start_time"))
                        res.setStartTime(imageData.getLong("start_time"));
                        if(imageData.has("end_time"))
                        res.setEndTime(imageData.getLong("end_time"));
                        if(imageData.has("open_url"))
                        res.setOpenUrl(imageData.getString("open_url"));
                    }
                }
                res.setErrorCode(0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public TodayBiddingRecord parserGetTodayBiddingRecord(String responseStr)
            throws Exception {
        TodayBiddingRecord res = new TodayBiddingRecord();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_TODAY_BIDDING_RECORD);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data, TodayBiddingRecord.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public OwerListModel parserOwerListInfo(String responseStr) throws Exception {
        OwerListModel res = new OwerListModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_OWER_LIST_INFO);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr, OwerListModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            // TODO: handle exception
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

//    @Override
//    public NovicePacksModel parserGetNovicePacks(String responseStr)
//            throws Exception {
//        // TODO Auto-generated method stub
//        NovicePacksModel res = new NovicePacksModel();
//        try {
//            JSONObject obj = new JSONObject(responseStr);
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NOVICE_PACKS);
//            parserHeader(funObject, res);
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//                String data = funObject.optString(StringConstant.JSON_DATA);
//                if (!TextUtils.isEmpty(data)) {
//                    Gson gson = new Gson();
//                    res = gson.fromJson(data, NovicePacksModel.class);
//                }
//                res.setErrorCode(0);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//        return res;
//    }

    @Override
    public BaseResult parserGroupHomeInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        res.setErrorCode(0);
        res.setInfo(responseStr);
        return res;
    }

    @Override
    public BaseResult parserGroupDiscoverInfo(String responseStr) throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        res.setErrorCode(0);
        res.setInfo(responseStr);
        return res;
    }

    @Override
    public BaseResult parserGroupOwerInfo(String responseStr) throws Exception {
    	// TODO Auto-generated method stub
    	BaseResult res = new BaseResult();
    	res.setErrorCode(0);
    	res.setInfo(responseStr);
    	return res;
    }
    
    @Override
    public BaseResult parserGroupFinanceInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        BaseResult res = new BaseResult();
        res.setErrorCode(0);
        res.setInfo(responseStr);
        return res;
    }

    @Override
    public BaseResult parserDiscoverGrid(String responseStr) throws Exception {
        DiscoverGridModel res = new DiscoverGridModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_DISCOVERY_GRID);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    ArrayList<ResourceModel.ImageResourceNewModel> list = gson.fromJson(data,
                            new TypeToken<List<ResourceModel.ImageResourceNewModel>>() {
                            }.getType());
                    res.setData(list);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserDiscoverList(String responseStr) throws Exception {
        DiscoverListModel res = new DiscoverListModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_DISCOVERY_LIST);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data, DiscoverListModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }


    @Override
    public BaseResult parserDiscoverMiddleMenu(String responseStr) throws Exception {
        DiscoverMiddleItemModel res = new DiscoverMiddleItemModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_DISCOVERY_MIDDLE_MENU);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data, DiscoverMiddleItemModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }



    @Override
    public BaseResult parserDiscoverBottomMenu(String responseStr) throws Exception {
        DiscoverMiddleItemModel res = new DiscoverMiddleItemModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_DISCOVERY_BOTTOM_MENU);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data, DiscoverMiddleItemModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

//    @Override
//    public RateCompareModel parserRateCompareInfo(String responseStr)
//            throws Exception {
//        // TODO Auto-generated method stub
//        RateCompareModel res = new RateCompareModel();
//        try {
//            JSONObject obj = new JSONObject(responseStr);
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_RATE_COMPARE);
//            parserHeader(funObject, res);
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//                String data = funObject.optString(StringConstant.JSON_DATA);
//                if (!TextUtils.isEmpty(data)) {
//                    Gson gson = new Gson();
//                    res = gson.fromJson(data, RateCompareModel.class);
//                }
//                res.setErrorCode(0);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//        return res;
//    }


    @Override
    public HelpCommonModel parserHelpCommonInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        HelpCommonModel res = new HelpCommonModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_HELP_COMMON);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data, HelpCommonModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

	@Override
	public CrashDomainModel parserCrashDataInfo(String responseStr) throws Exception {
		// TODO Auto-generated method stub
		CrashDomainModel res = new CrashDomainModel();
        try {
            JSONObject funObject = new JSONObject(responseStr);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK || errorCode == DcError.DC_OK_200) {
//            	{"error_code":200,"message":"","data":[{"api":"api2","m":"m2"}]}
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    ArrayList<CrashDomainItemModel> list = gson.fromJson(data,
                            new TypeToken<List<CrashDomainItemModel>>() {
                            }.getType());
                    res.setList(list);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
	}
	
	@Override
    public PromoteMainListModel parserPromoteListInfo(String responseStr)
            throws Exception {
        // TODO Auto-generated method stub
        PromoteMainListModel res = new PromoteMainListModel();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_HOME_PROMOTE);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                	
                	if (dataStr.equals("[]")) {
                		res.setList(new ArrayList<PromoteMainItemModel>());
                        res.setErrorCode(0);
                	} else {
                		Gson gson = new Gson();
                		ArrayList<PromoteMainItemModel> list = gson.fromJson(
                				dataStr,
                				new TypeToken<List<PromoteMainItemModel>>() {
                				}.getType());
                		res.setList(list);
                		res.setErrorCode(0);
                	}
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

	@Override
	public MainIndexBiddingListModel parserMainIndexBiddingInfo(String responseStr)
			throws Exception {
		MainIndexBiddingListModel res = new MainIndexBiddingListModel();
	        try {
	            JSONObject obj = new JSONObject(responseStr);

	            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_HOME_INDEXBIDDING);

	            parserHeader(funObject, res);

	            int errorCode = res.getErrorCode();
	            if (errorCode == DcError.DC_OK) {

	                String dataStr = funObject.optString(StringConstant.JSON_DATA);
	                if (!TextUtils.isEmpty(dataStr)) {
	                	if (dataStr.equals("[]")) {
	                		res.setList(new ArrayList<MainIndexBiddingModel>());
	                		res.setErrorCode(0);
	                	} else {
	                		 Gson gson = new Gson();
	 	                    ArrayList<MainIndexBiddingModel> list = gson.fromJson(dataStr,
	 	                            new TypeToken<List<MainIndexBiddingModel>>() {
	 	                            }.getType());
	 	                    res.setList(list);
	 	                    res.setErrorCode(0);
	                	}
	                }
	            }
	        } catch (Exception e) {
	            LogUtil.e(e.toString());
	            e.printStackTrace();
	            throw new Exception(e);
	        }
	        return res;
	}
	
	@Override
	public FinanceIndexBiddingListModel parserFinanceIndexBiddingInfo(String responseStr)
			throws Exception {
		FinanceIndexBiddingListModel res = new FinanceIndexBiddingListModel();
	        try {
	            JSONObject obj = new JSONObject(responseStr);

	            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_FINANCE_PRODUCTLIST);

	            parserHeader(funObject, res);

	            int errorCode = res.getErrorCode();
	            if (errorCode == DcError.DC_OK) {

	                String dataStr = funObject.optString(StringConstant.JSON_DATA);
	                if (!TextUtils.isEmpty(dataStr)) {
	                	if (dataStr.equals("[]")) {
	                		res.setList(new ArrayList<FinanceIndexBiddingListItemListModel>());
	                		res.setErrorCode(0);
	                	} else {
	                		 Gson gson = new Gson();
	 	                    ArrayList<FinanceIndexBiddingListItemListModel> list = gson.fromJson(dataStr,
	 	                            new TypeToken<List<FinanceIndexBiddingListItemListModel>>() {
	 	                            }.getType());
	 	                    res.setList(list);
	 	                    res.setErrorCode(0);
	                	}
	                }
	            }
	        } catch (Exception e) {
	            LogUtil.e(e.toString());
	            e.printStackTrace();
	            throw new Exception(e);
	        }
	        return res;
	}

	@Override
	public UserConfigNewModel parserUserConfigNewInfo(String responseStr)
			throws Exception {
		UserConfigNewModel res = new UserConfigNewModel();
		try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_USER_CONFIG);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data, UserConfigNewModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
        
//		UserConfigListModel res = new UserConfigListModel();
//		
//		
//		UserInfo user = DataManager.getUserDbHandler().getLoginUser();
//		if (user == null) {
//			return res;
//		}
//		
//		try {
//            JSONObject obj = new JSONObject(responseStr);
//
//            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_USER_CONFIG);
//
//            parserHeader(funObject, res);
//
//            int errorCode = res.getErrorCode();
//            if (errorCode == DcError.DC_OK) {
//            	Gson gson = new Gson();
//            	JSONObject dataObject = funObject.optJSONObject(StringConstant.JSON_DATA);
//            	if (dataObject != null) {
//            		ArrayList<UserConfigNewModel> list = new ArrayList<UserConfigNewModel>();
//            		
//            		try {
//            			UserConfigNewModel first = gson.fromJson(dataObject.optString("0"), UserConfigNewModel.class);//活期
//            			first.setState(Enum.UserConfigStatus.CONFIG_CURRENT.getValue());
//            			first.setUserId(user.getUser_id());
//            			list.add(first);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//            		try {
//						
//            			UserConfigNewModel sec = gson.fromJson(dataObject.optString("1"), UserConfigNewModel.class);//新手标
//            			sec.setState(Enum.UserConfigStatus.CONFIG_NEWBIE.getValue());
//            			sec.setUserId(user.getUser_id());
//            			list.add(sec);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//            		
//            		JSONObject lastObject = dataObject.optJSONObject("3");
//            		String investPlaceholder = "";
//            		String investHint = "";
//            		
//            		String investAlert = "";
//            		String investTips  = "";
//            		
//            		try {
//            			JSONObject lastInvestObject = lastObject.optJSONObject("invest");
//            			investPlaceholder = lastInvestObject.optString("investPlaceholder");
//            			investHint        = lastInvestObject.optString("investHint");
//            			investAlert       = lastInvestObject.optString("investAlert");
//            			investTips       = lastInvestObject.optString("investTips");
//            			
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//            		
//            		try {
//            			UserConfigNewModel lastCurrent = gson.fromJson(lastObject.optString("redeemCurrent"), UserConfigNewModel.class);
//            			lastCurrent.setInvestPlaceholder(investPlaceholder);
//            			lastCurrent.setInvestHint(investHint);
//            			lastCurrent.setInvestAlert(investAlert);
//            			lastCurrent.setInvestTips(investTips);
//            			lastCurrent.setState(Enum.UserConfigStatus.CONFIG_DHB_TO_CURRENT.getValue());
//            			lastCurrent.setUserId(user.getUser_id());
//            			list.add(lastCurrent);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//            		
//            		try {
//						
//            			UserConfigNewModel lastBank = gson.fromJson(lastObject.optString("redeemBank"), UserConfigNewModel.class);
//            			lastBank.setInvestPlaceholder(investPlaceholder);
//            			lastBank.setInvestHint(investHint);
//            			lastBank.setInvestAlert(investAlert);
//            			lastBank.setInvestTips(investTips);
//            			lastBank.setState(Enum.UserConfigStatus.CONFIG_DHB_TO_BANK.getValue());
//            			lastBank.setUserId(user.getUser_id());
//            			list.add(lastBank);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//            		
//            		try {
//						
//            			UserConfigNewModel lastQuik = gson.fromJson(lastObject.optString("redeemQuick"), UserConfigNewModel.class);
//            			lastQuik.setInvestPlaceholder(investPlaceholder);
//            			lastQuik.setInvestHint(investHint);
//            			lastQuik.setInvestAlert(investAlert);
//            			lastQuik.setInvestTips(investTips);
//            			lastQuik.setState(Enum.UserConfigStatus.CONFIG_DHB_TO_QIUK.getValue());
//            			lastQuik.setUserId(user.getUser_id());
//            			list.add(lastQuik);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//            		res.setList(list);
//            	}
//            }
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//            e.printStackTrace();
//            throw new Exception(e);
//        }
//		return res;
	}

	/**
	 * 解析获取消息列表应答报文体
	 * 
	 */
	@Override
	public BaseResult parserGetNewsListInfo(String responseStr) throws Exception {
        PushNewsListInfoModel res = new PushNewsListInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(StringConstant.FUN_GET_MESSAGE_LIST);
            //解析表头数据
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (isJsonNotEmpty(dataStr)) {
                    Gson gson = new Gson();
                    ArrayList<PushNewsInfoModel> list = gson.fromJson(dataStr,
                            new TypeToken<List<PushNewsInfoModel>>() {
                            }.getType());
                    res.setList(list);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
	}

	/**
	 * 解析设置消息已读应答报文体
	 * 
	 */
	@Override
	public BaseResult parserSetNewsReadInfo(String responseStr) throws Exception{
		SetNewsReadInfoModel res = new SetNewsReadInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(StringConstant.FUN_SET_MESSAGE_READ);
            //解析表头数据
            parserHeader(funObject, res);
            //解析报文体
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (isJsonNotEmpty(dataStr)) {
                	//保存消息ID
                	JSONObject bodyObject = funObject.getJSONObject(StringConstant.JSON_DATA);
                	String messageId = bodyObject.getString(StringConstant.JSON_MESSAGE_ID);
                    if(!TextUtils.isEmpty(messageId)) {
                    	res.setMessageId(messageId);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
		return res;
	}

	/**
	 * 解析获取消息Tip应答报文体
	 * 
	 */
	@Override
	public BaseResult parserGetMessageTipInfo(String responseStr) throws Exception {
		PushNewsTipInfoModel res = new PushNewsTipInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_MESSAGE_TIP);
            //解析表头数据
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (isJsonNotEmpty(dataStr)) {
                    Gson gson = new Gson();
                    PushNewsTipModel tip = gson.fromJson(dataStr, PushNewsTipModel.class);
                    res.setData(tip);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }
	
	/**
	 * 解析获取消息Tip应答报文体
	 * 
	 */
	@Override
	public BaseResult parserGetRefreshTipInfo(String responseStr) throws Exception {
		RefreshTipsListModel res = new RefreshTipsListModel();
		try {
			JSONObject obj = new JSONObject(responseStr);
			JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_REFRESH_TIP);
			//解析表头数据
			parserHeader(funObject, res);
			int errorCode = res.getErrorCode();
			if (errorCode == DcError.DC_OK) {
				String dataStr = funObject.optString(StringConstant.JSON_DATA);
				if (isJsonNotEmpty(dataStr)) {
					Gson gson = new Gson();
					res = gson.fromJson(dataStr, RefreshTipsListModel.class);
					res.setErrorCode(DcError.DC_OK);
				}
			}
		} catch (Exception e) {
			LogUtil.e(e.toString());
			e.printStackTrace();
			throw new Exception(e);
		}
		return res;
	}

	/**
	 * 是否是空的JSON数据 说明: 确认JSON字符串是否是空的JSON字符串，防止GSON解析出空对象
	 * 
	 * 返回值: boolean: true: 不为空；false: 为空
	 */
	private boolean isJsonNotEmpty(String dataStr) {
		return !TextUtils.isEmpty(dataStr)
				&& !dataStr.equals(StringConstant.JSON_ARRAY_EMPTY)
				&& !dataStr.equals(StringConstant.JSON_EMPTY);
	}

	@Override
	public BaseResult parserGetNewCreditInfo(String responseStr) throws Exception {
		NewCreditModel res = new NewCreditModel();
		try {
			JSONObject obj = new JSONObject(responseStr);
			JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_NEW_CREDIT);
			//解析表头数据
			parserHeader(funObject, res);
			int errorCode = res.getErrorCode();
			if (errorCode == DcError.DC_OK) {
				String dataStr = funObject.optString(StringConstant.JSON_DATA);
				if (isJsonNotEmpty(dataStr)) {
					Gson gson = new Gson();
                    ArrayList<CreditItem> list = gson.fromJson(dataStr,
                            new TypeToken<List<CreditItem>>() {
                            }.getType());
                    res.setList(list);
					res.setErrorCode(DcError.DC_OK);
				}
			}
		} catch (Exception e) {
			LogUtil.e(e.toString());
			e.printStackTrace();
			throw new Exception(e);
		}
		return res;
	}
	
	
	@Override
	public BaseResult parserGetCreditListInfo(String responseStr) throws Exception {
		NewCreditModel res = new NewCreditModel();
		try {
			JSONObject obj = new JSONObject(responseStr);
			JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_TODAY_CREDIT);
			//解析表头数据
			parserHeader(funObject, res);
			int errorCode = res.getErrorCode();
			if (errorCode == DcError.DC_OK) {
				String dataStr = funObject.optString(StringConstant.JSON_DATA);
				if (isJsonNotEmpty(dataStr)) {
					if (isJsonNotEmpty(dataStr)) {
						Gson gson = new Gson();
						res = gson.fromJson(dataStr, NewCreditModel.class);
						res.setErrorCode(DcError.DC_OK);
					}
				}
			}
		} catch (Exception e) {
			LogUtil.e(e.toString());
			e.printStackTrace();
			throw new Exception(e);
		}
		return res;
	}

    @Override
    public RechargeOrderIdModel parserGetRechargeOrderIdInfo(String responseStr) throws Exception {
        RechargeOrderIdModel res = new RechargeOrderIdModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_RECHARGE_ORDERID);
            //解析表头数据
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (isJsonNotEmpty(dataStr)) {
                    if (isJsonNotEmpty(dataStr)) {
                        Gson gson = new Gson();
                        res = gson.fromJson(dataStr, RechargeOrderIdModel.class);
                        res.setErrorCode(DcError.DC_OK);
                    }
                }
            } else {
                JSONObject dataObj = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (null != dataObj) {
                    res.setInfo(dataObj.optString("tips"));
                    res.setServerErrorUrl(dataObj.optString("url"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    /**
     * 用户开户应答解析
     *
     * @param responseStr: 应答结果字符串
     */
    @Override
    public BaseResult parserOpenBankAccount(String responseStr) throws Exception {
        BankAccountModel res = new BankAccountModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_OPEN_BANK_ACCOUNT);
            //解析表头数据
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (isJsonNotEmpty(dataStr)) {
                    if (isJsonNotEmpty(dataStr)) {
                        Gson gson = new Gson();
                        res = gson.fromJson(dataStr, BankAccountModel.class);
                        res.setErrorCode(DcError.DC_OK);
                    }
                }
            } else {
                try {
                    JSONObject dataObj = funObject.optJSONObject(StringConstant.JSON_DATA);
                    String leadPageUrl = dataObj.optString("leadPageUrl");
//                    res.setInfo(leadPageUrl);
                    res.setServerErrorUrl(leadPageUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    /** 解析获取设置密码和签名 */
    @Override
    public BaseResult parserGetSetPasswordSign(String responseStr) throws Exception {
        BankAccountModel res = new BankAccountModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_GET_SET_PASSWORD_SIGN);
            //解析表头数据
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (isJsonNotEmpty(dataStr)) {
                    if (isJsonNotEmpty(dataStr)) {
                        Gson gson = new Gson();
                        res = gson.fromJson(dataStr, BankAccountModel.class);
                        res.setErrorCode(DcError.DC_OK);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public RedeemItemInfoModel parserRechargeConfirmInfo(String responseStr)
            throws Exception {
        RedeemItemInfoModel res = new RedeemItemInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_RECHARGE_CONFIRM);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), RedeemItemInfoModel.class);
                    res.setErrorCode(0);
                }
            } else {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    if (TextUtils.isEmpty(data.optString(ConstantUtil.KEY_OLD_PHONE))) {
                        res.setInfo(data.optString("tips"));
                    } else {
                        res.setInfo(data.optString(ConstantUtil.KEY_OLD_PHONE));
                    }
                    res.setServerErrorUrl(data.optString("url"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public WithdrawOrderInfoModel parserWithdrawOrderInfo(String responseStr) throws Exception {
        WithdrawOrderInfoModel res = new WithdrawOrderInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_CREATE_WITHDRAW_ORDER);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK || errorCode == DcError.DC_LARGE_WITHDRAW_LIMIT_ERROR) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), WithdrawOrderInfoModel.class);
                    res.setErrorCode(errorCode);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserSureWithdrawOrderInfo(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        res.setErrorCode(0);
        res.setInfo(responseStr);
        return res;
    }

    @Override
    public BaseResult parserSureRedeemOrderInfo(String responseStr) throws Exception {
        RedeemItemInfoModel res = new RedeemItemInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_SURE_REDEEM_ORDER);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), RedeemItemInfoModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserGetRedeemConfig(String responseStr) throws Exception {
        RedeemConfigModel res = new RedeemConfigModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_REDEEM_CONFIG);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), RedeemConfigModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public RechargeConfigModel parserRechargeConfigInfo(String responseStr) throws Exception {
        RechargeConfigModel res = new RechargeConfigModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_CONFIG_RECHARGE);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), RechargeConfigModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserGetWithdrawConfig(String responseStr) throws Exception {
        WithdrawConfigModel res = new WithdrawConfigModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_WITHDRAW_CONFIG);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), WithdrawConfigModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public InvestConfigModel parserInvestConfigInfo(String responseStr) throws Exception {

        InvestConfigModel res = new InvestConfigModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_CONFIG_INVEST);

            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data.toString(), InvestConfigModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }


    @Override
    public BaseResult parserChangeBankPhoneInfo(String responseStr) throws Exception {

        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_CHANGE_BANK_MOBILE);

            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserInvestBalance(String responseStr) throws Exception {

        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_INVEST_BALANCE);

            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setErrorCode(0);
                JSONObject data = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (data != null) {
                    res.setInfo(data.optString(ConstantUtil.KEY_BALANCE));
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserRegionInfo(String responseStr) throws Exception {

        RegionInfoModel res = new RegionInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_REGION);

            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setErrorCode(0);
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (isJsonNotEmpty(dataStr)) {
                    Gson gson = new Gson();
                    ArrayList<RegionInfoModel.BankItemModel> list = gson.fromJson(dataStr,
                            new TypeToken<List<RegionInfoModel.BankItemModel>>() {
                            }.getType());
                    res.setList(list);
                    res.setErrorCode(DcError.DC_OK);
                }
            }

        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }


    @Override
    public BaseResult parserRegionSiteInfo(String responseStr) throws Exception {

        RegionInfoModel res = new RegionInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BANKSITE);

            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setErrorCode(0);
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (isJsonNotEmpty(dataStr)) {
                    Gson gson = new Gson();
                    ArrayList<RegionInfoModel.BankItemModel> list = gson.fromJson(dataStr,
                            new TypeToken<List<RegionInfoModel.BankItemModel>>() {
                            }.getType());
                    res.setList(list);
                    res.setErrorCode(DcError.DC_OK);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserAwardRate(String responseStr) throws Exception {

        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_AWARD_RATE);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                res.setInfo("0");
                JSONObject dataStr = funObject.optJSONObject(StringConstant.JSON_DATA);
                if (dataStr != null) {
                    res.setInfo(dataStr.optString("awardRate"));
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserGetHtmlDoc(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_GET_HTML_DOC);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    res.setInfo(dataStr);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BankCardsListInfoModel parserBankListNewInfo(String responseStr)
            throws Exception {

        BankCardsListInfoModel res = new BankCardsListInfoModel();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_BANK_LIST_NEW);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr.toString(), BankCardsListInfoModel.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public IndexButtonMenuInfo parserGetIndexButtonMenu(String responseStr) throws Exception {

        IndexButtonMenuInfo res = new IndexButtonMenuInfo();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_GET_INDEX_BUTTON_MENU);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    ArrayList<IndexButtonMenuInfo.IndexButtonMenu> list = gson.fromJson(dataStr,
                            new TypeToken<List<IndexButtonMenuInfo.IndexButtonMenu>>() {
                            }.getType());
                    res.setData(list);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserSetDotClick(String responseStr) throws Exception {

        BaseResult res = new BaseResult();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_SET_DOT_CLICK);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    res.setInfo(dataStr);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }


    @Override
    public BaseResult parserSettingMenu(String responseStr) throws Exception {

        SettingMenuInfo res = new SettingMenuInfo();
        try {
            JSONObject obj = new JSONObject(responseStr);

            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_SETTING_MENU);

            parserHeader(funObject, res);

            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr.toString(), SettingMenuInfo.class);
                    res.setErrorCode(0);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserExclusiveList(String responseStr) throws Exception {
        ExclusiveListModel res = new ExclusiveListModel();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_EXCLUSIVE_LIST);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String data = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(data, ExclusiveListModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserCouponsList(String responseStr) throws Exception {
        CouponsListInfo res = new CouponsListInfo();
        try {
            JSONObject obj = new JSONObject(responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_MY_COUPONS);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    ArrayList<CouponModel> list = gson.fromJson(dataStr,
                            new TypeToken<List<CouponModel>>() {
                            }.getType());
                    res.setCoupons(list);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult  parserPopWindowUrl(String responseStr) throws Exception {

        BaseResult res = new BaseResult();
        String url ="";
        try {

            JSONObject obj = new JSONObject(responseStr);

            LogUtil.d("dubo","result============="+responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_POPWINDOW_URL);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                JSONObject json = new JSONObject(dataStr);
                LogUtil.d("dubo","result============="+dataStr);
                url = json.getString("url");
                LogUtil.d("dubo","result============="+url);
                res.setInfo(url);
                res.setErrorCode(errorCode);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }
    @Override
    public BaseResult  parserHomeReadMe(String responseStr) throws Exception {

        BaseResult res = new BaseResult();
        String url ="";
        try {

            JSONObject obj = new JSONObject(responseStr);

            LogUtil.d("dubo","result============="+responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_NAME_GET_README);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {
                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr, HomeReadMeModel.class);
                }
                res.setErrorCode(0);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserDefaultPostDataAndUrl(String responseStr) throws Exception {
        BaseResult res = new BaseResult();
        res.setErrorCode(0);
        res.setInfo(responseStr);
        return res;
    }

    @Override
    public BaseResult parserFunNameJson(String responseStr,String funName, Class<DefaultPostDataUrlInfoModel> model) throws Exception {
        BaseResult res = new BaseResult();
        String url ="";
        try {

            JSONObject obj = new JSONObject(responseStr);

            LogUtil.d("dubo","result============="+responseStr);
            JSONObject funObject = obj.optJSONObject(funName);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr, model);
                }
                res.setErrorCode(0);
                res.setErrorCode(errorCode);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;
    }

    @Override
    public BaseResult parserLoginSourceJson(String responseStr) throws Exception {
        LogUtil.e("TAG","==============="+responseStr);

        BaseResult res = new BaseResult();
        String url ="";
        try {

            JSONObject obj = new JSONObject(responseStr);

            LogUtil.d("dubo","result============="+responseStr);
            JSONObject funObject = obj.optJSONObject(ConstantUtil.SERVER_URL_GET_LOGIN_SOURCE);
            parserHeader(funObject, res);
            int errorCode = res.getErrorCode();
            if (errorCode == DcError.DC_OK) {

                String dataStr = funObject.optString(StringConstant.JSON_DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    Gson gson = new Gson();
                    res = gson.fromJson(dataStr, LoginResource.class);
                }
                res.setErrorCode(0);
                res.setErrorCode(errorCode);
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        return res;

    }


}
