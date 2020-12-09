package com.jindan.p2p.user;

import android.text.TextUtils;

import com.jindan.p2p.http.builder.HttpSubmitDataManager;
import com.jindan.p2p.net.INetListener;
import com.jindan.p2p.net.NetManager;
import com.jindan.p2p.utils.ConstantUtil;
import com.jindan.p2p.utils.StringConstant;
import com.jindan.p2p.utils.Utils;

import java.util.HashMap;

/**
 * 接口代理
 *  <p>
 * 该类只建议提供给UserImpl类使用，外部其他类请通过{@link UserManager}的 getRequestHandler() 方法来使用)
 * Created by larry on 2017/4/12.
 */
public class UserRequestProxy implements IUserInterface {

    private static UserRequestProxy instance = new UserRequestProxy();
    private UserRequestProxy (){
        mObservers = new HashMap<Integer, IUserListener>();
        mRequestPageNums = new HashMap<Integer, Integer>();
    }
    public static UserRequestProxy getInstance() {
        return instance;
    }

    private HashMap<Integer, IUserListener> mObservers;
    /** 保存页面查询的起始位置 */
    private HashMap<Integer, Integer> mRequestPageNums;

    private INetListener iNetListener = null;

    private int mCurrentRequestId = 0;

    public INetListener getINetListener() {
        return iNetListener;
    }

    public void setINetListener(INetListener iNetListener) {
        this.iNetListener = iNetListener;
    }

    public IUserListener getObservers(int requestId){
        return mObservers.get(requestId);
    }

    public void addObserver(IUserListener observer, int requestId) {
        mObservers.put(requestId, observer);
    }

    public void removeObserver(int requestId) {
        mObservers.remove(requestId);
    }

    public HashMap<Integer, Integer> getRequestPageNums () {
        return mRequestPageNums;
    }

    public void handleSuccessResult(int requestId, final int requestType,
                                     final Object data, String responseStr) {
        IUserListener _listener = (IUserListener) getObservers(requestId);
        if (_listener != null) {
            _listener.onRequestSuccess(requestType, data);
            removeObserver(requestId);
        } else {
            _listener = (IUserListener) getObservers(requestType);
            if (_listener != null) {
                _listener.onRequestSuccess(requestType, data);
            }
            removeObserver(requestType);
        }
    }

    public void handleFailedResult(int requestId, final int requestType,
                                    final int errorCode, final String errorMsg, Object responseData) {
        IUserListener _listener = (IUserListener) getObservers(requestId);
        if (_listener != null) {
            _listener.onRequestError(requestType, errorCode, errorMsg, responseData);
            removeObserver(requestId);
        } else {
            _listener = (IUserListener) getObservers(requestType);
            if(_listener != null) {
                _listener.onRequestError(requestType, errorCode, errorMsg, responseData);
            }
            removeObserver(requestType);
        }
    }

    public void requestLogout(IUserListener observer) {
        //先发送用户退出请求
        if (UserManager.getUserDataHandler().getCurrentUserInfo() == null) {
            return;
        }
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_LOGOUT);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_LogOut, str, iNetListener);
        addObserver(observer, mCurrentRequestId);

        handleSuccessResult(mCurrentRequestId, ConstantUtil.Net_Tag_LogOut,
                UserManager.getUserDataHandler().getCurrentUserInfo(), "logout");

    }

    // new 510
    public void requestRegistWithPhoneNumber(IUserListener observer, String phonenumber, String msmcode) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_USER_PHONE, phonenumber);
        params.put(StringConstant.JSON_USER_PHONE_MSM_CODE, msmcode);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_REGISTER, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_NAME_DEFAULT + ConstantUtil.NEW_API_URL_SUFFIX,
                ConstantUtil.Net_Tag_RegistWithPhone, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    public void requestLoginBySms(IUserListener observer, String phonenum, String smscode) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_USER_PHONE, phonenum);
        params.put(StringConstant.JSON_USER_PHONE_MSM_CODE, smscode);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_LOGIN_SMS, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_NAME_DEFAULT + ConstantUtil.NEW_API_URL_SUFFIX,
                ConstantUtil.Net_Tag_UserLogin_Sms, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    public void requestLoginByPwd(IUserListener observer, String phonenum, String pwd) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_USER_PHONE, phonenum);
        params.put(StringConstant.JSON_PASSWORD, pwd);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_LOGIN_PWD, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_NAME_DEFAULT + ConstantUtil.NEW_API_URL_SUFFIX,
                ConstantUtil.Net_Tag_UserLogin_Pwd, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestSetLoginPwd(IUserListener observer, String newPwd,
                                   String oldPwd, String ck, String mobile) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(newPwd)) {
            params.put(StringConstant.JSON_PASSWORD, newPwd);
        }
        if (!TextUtils.isEmpty(oldPwd)) {
            params.put(StringConstant.JSON_PASSWORD_OLD, oldPwd);
        }
        if (!TextUtils.isEmpty(ck)) {
            params.put("ck", Utils.GetStringNoNil(ck));
        }
        if (!TextUtils.isEmpty(mobile)) {
            params.put(StringConstant.JSON_PHONE_NUMBER, mobile);
        }

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_LOGINPWD_SET, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_User_LoginPwd_Set, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestInvestOrderId(IUserListener observer, String iUnlockConfig, String money, String goodsId, int type,
                                     int fromType, String bId, int agreeMatchBidding, String regularAcceptDay, String couponId) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_RECHARGE_MONEY, Utils.GetStringNoNil(money));
        params.put(StringConstant.JSON_GOODS_ID, Utils.GetStringNoNil(goodsId));
        params.put(StringConstant.JSON_GOODS_TYPE, String.valueOf(type));
        params.put(StringConstant.JSON_INVEST_AGREE_NEXT_BID, String.valueOf(agreeMatchBidding));
        params.put(StringConstant.JSON_INVEST_REGULAR_ACCEPT_DAY, String.valueOf(regularAcceptDay));
        params.put(StringConstant.JSON_GOODS_FROM_TYPE, String.valueOf(fromType));
        params.put(StringConstant.JSON_IUNLOCK_CONFIG, Utils.GetStringNoNil(iUnlockConfig));
        params.put(StringConstant.JSON_INVEST_BID, Utils.GetStringNoNil(bId));
        params.put(StringConstant.JSON_INVEST_COUPONID, Utils.GetStringNoNil(couponId));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_INVEST_CREATE, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Invest_OrderId, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestInvestOrderIdV7(IUserListener observer, String iUnlockConfig, String money, String goodsId,
                                       int type, int fromType, String bId, int agreeMatchBidding, String couponId) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_RECHARGE_MONEY, Utils.GetStringNoNil(money));
        params.put(StringConstant.JSON_GOODS_ID, Utils.GetStringNoNil(goodsId));
        params.put(StringConstant.JSON_GOODS_TYPE, String.valueOf(type));
        params.put(StringConstant.JSON_INVEST_AGREE_NEXT_BID, String.valueOf(agreeMatchBidding));
        params.put(StringConstant.JSON_GOODS_FROM_TYPE, String.valueOf(fromType));
        params.put(StringConstant.JSON_IUNLOCK_CONFIG, Utils.GetStringNoNil(iUnlockConfig));
        params.put(StringConstant.JSON_INVEST_BID, Utils.GetStringNoNil(bId));
        params.put(StringConstant.JSON_INVEST_COUPONID, Utils.GetStringNoNil(couponId));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_INVEST_CREATE, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Invest_OrderId_V7, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestTotalAmountInfo(IUserListener observer) {
        // TODO Auto-generated method stub
        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Total_Amount);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Total_Amount, 0);
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_TOTAL_AMOUNT);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Total_Amount, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestBiaoHistoryListInfo(IUserListener observer, int pageNum, int pageSize) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_LIST_PAGE_NUM, String.valueOf(pageNum));
        params.put(StringConstant.JSON_LIST_PAGE_SIZE, String.valueOf(pageSize));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_BIAO_HISTORY_LIST, params);

        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Biao_History_List);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Biao_History_List, pageNum);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Biao_History_List, str, iNetListener, pageNum);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestBankList(IUserListener observer, int pageNum, int pageSize) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_LIST_PAGE_NUM, String.valueOf(pageNum));
        params.put(StringConstant.JSON_LIST_PAGE_SIZE, String.valueOf(pageSize));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_BANK_LIST, params);

        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Bank_List);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Bank_List, pageNum);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Bank_List, str,
                iNetListener, pageNum);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestBankListNew(IUserListener observer, int pageNum, int pageSize) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_LIST_PAGE_NUM, String.valueOf(pageNum));
        params.put(StringConstant.JSON_LIST_PAGE_SIZE, String.valueOf(pageSize));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_BANK_LIST_NEW, params);
        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Bank_List_New);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Bank_List_New, pageNum);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Bank_List_New, str,
                iNetListener, pageNum);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestBindBankcard(IUserListener observer, String username,
                                    String idCard, String bankcard, String bankphone, String bankcardid) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_USER_NAME, username);
        params.put(StringConstant.JSON_USER_ID_CARD, idCard);
        params.put(StringConstant.JSON_USER_BANK_CARD, bankcard);
        params.put(StringConstant.JSON_USER_BANK_MOBILE, bankphone);
        params.put(StringConstant.JSON_USER_BANK_ID, bankcardid);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_BANK_BIND, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Bind_BankCard,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 验证码验证
     */
    @Override
    public void requestBindBankCardVerify(IUserListener observer, String msmcode) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_USER_PHONE_MSM_CODE, msmcode);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_BANK_BIND_VERIFY, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_BindBandCard_Verify, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestBindBankGetVerifyCode(IUserListener observer) {
        // TODO Auto-generated method stub
        Object str = HttpSubmitDataManager.getBuilder()
                .buildDefaultString(ConstantUtil.SERVER_URL_NAME_BANK_BIND_GET_VERIFY_CODE);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_BindBandCard_Sms_Code, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestInvestPay(IUserListener observer, String pay_pwd, String orderId, int fromType, String pid) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_ORDER_ID, orderId);
        params.put(StringConstant.JSON_INVEST_PAY_PWD, pay_pwd);
        params.put(StringConstant.JSON_GOODS_FROM_TYPE, String.valueOf(fromType));
        params.put(StringConstant.JSON_GOODS_ID, Utils.GetStringNoNil(pid));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = null;
        if (ConstantUtil.Is_Old_Version) {
            str = HttpSubmitDataManager.getBuilder().buildDefaultString(ConstantUtil.SERVER_URL_NAME_PAYINVEST, params);
        } else {
            str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                    ConstantUtil.SERVER_URL_NAME_PAY_INVEST, params);
        }

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Invest_Commit,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);

    }

    @Override
    public void requestRechargeOrderId(IUserListener observer, String money) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_INVEST_MONEY, money);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_GET_RECHARGE_ORDERID, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Recharge_OrderId,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestRechargeConfirm(IUserListener observer, String orderId, String authSeq) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_ORDER_ID, orderId);
        params.put(StringConstant.JSON_RECHARGE_AUTHSEQ, authSeq);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_GET_RECHARGE_CONFIRM, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Recharge_Commit,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);

    }

    @Override
    public void requestRedeemOrderId(IUserListener observer, String money, String pId, int redeemType, int redeemAll) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_RECHARGE_MONEY, money);
        params.put(StringConstant.JSON_RECHARGE_TYPE_PID, pId);
        params.put(StringConstant.JSON_RECHARGE_REDEEMTYPE, String.valueOf(redeemType));
        if (ConstantUtil.Is_Old_Version) {
            params.put(StringConstant.JSON_RECHARGE_REDEEMALL, String.valueOf(redeemAll));
        } else {
            params.put(StringConstant.JSON_RECHARGE_ISWHOLE, String.valueOf(redeemAll));
        }

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_REDEEM_ORDERID, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Redeem_OrderId,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestRedeemOrderId(IUserListener observer, String money, String pId, int toType, int redeemType, int redeemAll) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_RECHARGE_MONEY, money);
        params.put(StringConstant.JSON_RECHARGE_TYPE_PID, pId);
        params.put(StringConstant.JSON_RECHARGE_REDEEMTYPE, String.valueOf(redeemType));
        if (ConstantUtil.Is_Old_Version) {
            params.put(StringConstant.JSON_RECHARGE_REDEEMALL, String.valueOf(redeemAll));
        } else {
            params.put(StringConstant.JSON_RECHARGE_ISWHOLE, String.valueOf(redeemAll));
        }
        if (toType > 0) {
            params.put(StringConstant.JSON_RECHARGE_TOTYPE, String.valueOf(toType));
        }

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_REDEEM_ORDERID, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Redeem_OrderId,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestSureRedeemOrder(IUserListener observer, String orderId) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_ORDER_ID, orderId);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_SURE_REDEEM_ORDER, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Sure_Redeem_Order,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestCreateWithdrawOrder(IUserListener observer, String money, String orderId, String bankSiteId) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        if(!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(bankSiteId)) {
            //大额提现方式
            params.put(StringConstant.JSON_REDEEM_TAKE_MONEY, "");
            params.put(StringConstant.JSON_ORDER_ID, orderId);
            params.put(StringConstant.JSON_BANK_SITE_ID, bankSiteId);
        } else {
            //小额提现只需要金额
            params.put(StringConstant.JSON_REDEEM_TAKE_MONEY, money);
            params.put(StringConstant.JSON_ORDER_ID, "");
            params.put(StringConstant.JSON_BANK_SITE_ID, "");
        }

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_CREATE_WITHDRAW_ORDER, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Create_Withdraw_Order,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestRedeemPay(IUserListener observer, String pay_pwd,
                                 String orderId, String pId) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_ORDER_ID, orderId);
        params.put(StringConstant.JSON_INVEST_PAY_PWD, pay_pwd);
        params.put(StringConstant.JSON_RECHARGE_TYPE_PID, pId);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_PAY_REDEEM, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Redeem_Commit,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 获取消息列表
     *
     * @param lastPublishTime: 最后一条消息的发布时间
     */
    @Override
    public void requestNewsPushList(IUserListener observer, String lastPublishTime) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_PUBLISH_TIME, lastPublishTime);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_GET_MESSAGE_LIST, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Get_Push_News_List_V6, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    public void requestUserNewestInfo(IUserListener observer) {
        // tag =9
        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_USER_INFO);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Get_UserInfo,
                str, iNetListener);
        if(null != observer)
            addObserver(observer, mCurrentRequestId);

    }

    public void requestSetPayPwd(IUserListener observer, String newpwd, String oldpwd, String ck) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_PASSWORD, newpwd);
        params.put(StringConstant.JSON_PASSWORD_OLD, oldpwd);
        params.put("ck", ck);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_PAYPWD_SET, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_SetPayPwd, str,
                iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * "mobile":手机号,
     * "action_type":类型, //0(支付密码)、1(登录)、2(注册)、3(重置登录密码, mobile 给空串)、
     *                      4(修改交易密码) 5 开户 openAccount、 6（交易密码验证）、7（更换银行卡预留手机号）
     * "verify_type":验证码类型 //1(短信)、2(语音)
     */
    public void requestVerifyCode(IUserListener observer, String phone, int action_type, int verifyType) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_USER_PHONE, phone);
        String actionTypeStr = "" + action_type;
        //action_type":类型, //1(登录)、2(注册)、 5 开户 openAccoun  7 更改银行手机号
        if (action_type == ConstantUtil.SMS_CODE_TYPE_LOGIN) {
            actionTypeStr = "login";
        } else if (action_type == ConstantUtil.SMS_CODE_TYPE_REGISTER) {
            actionTypeStr = "register";
        } else if (action_type == ConstantUtil.SMS_CODE_TYPE_ACCOUNT) {
            actionTypeStr ="openBankAccount";
        } else if (action_type == ConstantUtil.SMS_CODE_TYPE_CHANGE_BANK_PHONE) {
            actionTypeStr ="changeBankMobile";
        }
        params.put("actionType", actionTypeStr);
        params.put("verifyType", String.valueOf(verifyType));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_TAG_8, params);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_NAME_DEFAULT + ConstantUtil.NEW_API_URL_SUFFIX, ConstantUtil.Net_Tag_VerifyCode, str,
                iNetListener);
                addObserver(observer, mCurrentRequestId);
    }

    /**
     * 重置登陆密码
     */
    public void requestRetrieveLoginPwd(IUserListener observer, String mobile, String ck) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("ck", ck);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_LOGINPWD_RESET, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Retrieve_LoginPwd, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 重置登录密码--发送验证码
     */
    public void requestSmsCodeResetPwd(IUserListener observer, String phonenum, int verifyType) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_PHONE_NUMBER, phonenum);
        params.put("verifyType", String.valueOf(verifyType));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_SMS_CODE_LOGINPWD_RESET, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Get_Sms_Code_LoginPwd_Reset, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestChangeBankPhone(IUserListener observer, String mobile, String smsCode) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_PHONE_NUMBER, mobile);
        params.put(StringConstant.JSON_USER_PHONE_MSM_CODE_NEW, smsCode);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_CHANGE_BANK_MOBILE, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Change_Bank, str,
                iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 重置登录密码--验证验证码
     */
    public void requestSmsCodeVerifyResetPwd(IUserListener observer, String phonenum, String smsCode) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_PHONE_NUMBER, phonenum);
        params.put(StringConstant.JSON_USER_PHONE_MSM_CODE, smsCode);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_SMS_CODE_VERIFY_LOGINPWD_RESET, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Get_Sms_Code_Verify_LoginPwd_Reset, str,
                iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 重置登录密码--身份验证
     */
    public void requestCheckIdCardResetPwd(IUserListener observer, String phonenum, String idcard) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_PHONE_NUMBER, phonenum);
        params.put(StringConstant.JSON_USER_ID_CARD, idcard);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_CHECK_IDCARD_RESET, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Check_IdCard_LoginPwd_Reset, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestCheckOldPwdResetPwd(IUserListener observer, String oldPwd) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_PASSWORD_OLD, oldPwd);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_CHECK_PAY_PWD, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_Check_Old_Pwd,
                str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestPreOrdersInfo(IUserListener observer, String goodsId) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_GOODS_ID, goodsId);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_PRE_ORDER, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Pre_Order, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestCheckForUpdate(IUserListener observer, String platform,
                                      String version, int build, boolean isAuto) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put("platform", platform);
        params.put("version", version);
        params.put("build", String.valueOf(build));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_CHECK_FOR_UPDATE, params);

        addObserver(observer, ConstantUtil.Net_Tag_App_Update);
        mRequestPageNums.put(ConstantUtil.Net_Tag_App_Update, 0);

        if (isAuto) {// 自动升级
            mCurrentRequestId = NetManager.getHttpConnect()
                    .sendRequestNetCache(ConstantUtil.NEW_BAPI_NAME_DEFAULT + ConstantUtil.NEW_API_URL_SUFFIX,
                            ConstantUtil.Net_Tag_App_Update, str, iNetListener, 0);
        } else {
            mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                    ConstantUtil.NEW_BAPI_NAME_DEFAULT + ConstantUtil.NEW_API_URL_SUFFIX, ConstantUtil.Net_Tag_App_Update,
                    str, iNetListener);
        }
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestShareDetailInfo(IUserListener observer, String activeId, int type) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_AC_ID, activeId);
        params.put(StringConstant.JSON_TYPE, String.valueOf(type));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_SHARE_INFO, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Share_Detail_Info, str, iNetListener);
        addObserver(observer, mCurrentRequestId);

    }

    @Override
    public void requestNewBindBankcard(IUserListener observer, String bankcard,
                                       String bankphone, String bankcardid) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_USER_BANK_CARD, bankcard);
        params.put(StringConstant.JSON_USER_BANK_MOBILE, bankphone);
        params.put(StringConstant.JSON_USER_BANK_ID, bankcardid);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_New_NAME_BANK_BIND, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_NEW_Bind_BankCard, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestSplashAdImage(IUserListener observer) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.GET_PRODUCT_IMAGE_PARA, StringConstant.GET_PRODUCT_IMAGE_INDEX);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.GET_PRODUCT_IMAGE, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Product_Image, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 获取投资提现配置
     */
    @Override
    public void requestInvestAndRedeemConfig(IUserListener observer, String pId, String type, String bId) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_INVEST_BID, bId);
        params.put(StringConstant.JSON_CONFIG_PID, pId);
        params.put(StringConstant.JSON_CONFIG_TYPE, type);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_GET_INVEST_AND_REDEEM_CONFIG, params);

		addObserver(observer, ConstantUtil.Net_Tag_Request_User_Config);
		mRequestPageNums.put(ConstantUtil.Net_Tag_Request_User_Config, 0);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_User_Config, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 获取赎回配置
     */
    @Override
    public void requestRedeemConfig(IUserListener observer, String pId, String type) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_CONFIG_PID, pId);
        params.put(StringConstant.JSON_CONFIG_TYPE, type);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_GET_REDEEM_CONFIG, params);

		addObserver(observer, ConstantUtil.Net_Tag_Redeem_Config);
		mRequestPageNums.put(ConstantUtil.Net_Tag_Redeem_Config, 0);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Redeem_Config, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 获取V8版本充值配置
     */
    @Override
    public void requestRechargeConfig(IUserListener observer) {
        // TODO Auto-generated method stub
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(ConstantUtil.SERVER_URL_NAME_CONFIG_RECHARGE);
        addObserver(observer, ConstantUtil.Net_Tag_Request_Recharge_Config);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Request_Recharge_Config, 0);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Recharge_Config, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 获取提现配置
     */
    @Override
    public void requestWithdrawConfig(IUserListener observer) {
        // TODO Auto-generated method stub
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(ConstantUtil.SERVER_URL_NAME_GET_WITHDRAW_CONFIG);
        addObserver(observer, ConstantUtil.Net_Tag_Withdraw_Config);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Withdraw_Config, 0);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Withdraw_Config, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 投资页余额查询
     */
    @Override
    public void requestInvestBalance(IUserListener observer) {
        // TODO Auto-generated method stub
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(ConstantUtil.SERVER_URL_NAME_GET_INVEST_BALANCE);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Invest_Balance, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 获取V8版本投资配置
     */
    @Override
    public void requestInvestConfig(IUserListener observer,  String pId, String type, String bId) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_INVEST_BID, bId);
        params.put(StringConstant.JSON_CONFIG_PID, pId);
        params.put(StringConstant.JSON_CONFIG_TYPE, type);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_CONFIG_INVEST, params);

        addObserver(observer, ConstantUtil.Net_Tag_Request_Invest_Config);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Request_Invest_Config, 0);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Invest_Config, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 获取V8版本投资配置
     */
    @Override
    public void requestRedpacketListInfo(IUserListener observer, String money) {
        // TODO Auto-generated method stub
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_INVEST_MONEY, money);
        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_MY_COUPONS, params);

//        addObserver(observer, ConstantUtil.Net_Tag_Request_Coupons_List_Info);
//        mRequestPageNums.put(ConstantUtil.Net_Tag_Request_Coupons_List_Info, 0);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Coupons_List_Info, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }



    /**
     * 请求设置消息已读状态
     *
     * @param messageId: 消息ID
     */
    @Override
    public void requestNewsReadStatus(IUserListener observer, String messageId) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_MESSAGE_ID, messageId);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_SET_MESSAGE_READ, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Set_News_Is_Read, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /** 请求获当日债权列表 */
    @Override
    public void requestGetTodayCreditList(IUserListener observer, String direct, int offsetId) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.FUN_TODAY_CREDIT_DIRECT, direct);
        params.put(StringConstant.FUN_TODAY_CREDIT_OFFSETID, String.valueOf(offsetId));

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_GET_TODAY_CREDIT, params);

        addObserver(observer, ConstantUtil.Net_Tag_Request_Today_Credit);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Request_Today_Credit, 0);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Today_Credit, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 银行托管电子账户开户
     * 说明: V8+
     *
     * @param observer: 应答监听者
     * @param username: 用户名
     * @param id_card: 身份证号
     * @param bank_card: 银行卡号
     */
    @Override
    public void requestOpenBankAccount(IUserListener observer, String username, String id_card,
                                       String bank_card, String phone_num, String sms_code, String bankCode) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_DEPOSIT_USERNAME, username);
        params.put(StringConstant.JSON_DEPOSIT_ID_CARD, id_card);
        params.put(StringConstant.JSON_DEPOSIT_BANK_CARD, bank_card);
        params.put(StringConstant.JSON_DEPOSIT_BANK_CODE, bankCode);
        params.put(StringConstant.JSON_USER_PHONE, phone_num);
        params.put(StringConstant.JSON_SMS_CODE, sms_code);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_OPEN_BANK_ACCOUNT, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Open_Bank_Account, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 获取设置交易密码签名信息
     */
    @Override
    public void requestGetSetPasswordSign(IUserListener observer) {
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_GET_SET_PASSWORD_SIGN);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Get_Set_Password_Sign, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     *
     * 获取银行地区
     * @param observer
     * @param regionId
     */
    @Override
    public void requestGetRegionInfo(IUserListener observer, String regionId) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_CASH_REGION_ID, regionId);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_REGION, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Get_Region, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     *
     * 获取银行网点
     * @param observer
     * @param regionId
     * @param keyword
     */
    @Override
    public void requestGetBankSiteInfo(IUserListener observer, String regionId, String keyword) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_CASH_REGION_ID, regionId);
        params.put(StringConstant.JSON_CASH_KEYWORD, keyword);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_BANKSITE, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Get_Bank_Site, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestGetHtmlDocInfo(IUserListener observer, String name) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_GET_HTML_DOC_NAME, name);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_GET_HTML_DOC, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Get_Html_Doc, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestSetDotClick(IUserListener observer, String dotType, String dotId) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_SET_DOT_CLICK_TYPE, dotType);
        params.put(StringConstant.JSON_SET_DOT_CLICK_ID, dotId);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_SET_DOT_CLICK, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Set_Dot_Click, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestGetSettingMenu(IUserListener observer) {
        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Setting_Menu);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Setting_Menu, 0);
        Object str = HttpSubmitDataManager.getBuilder()
                .buildDefaultString(ConstantUtil.SERVER_URL_SETTING_MENU);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Setting_Menu, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestExclusiveData(IUserListener observer) {
        addObserver(observer, ConstantUtil.Net_Tag_Request_Exclusive_List);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Request_Exclusive_List, 0);
        Object str = HttpSubmitDataManager.getBuilder()
                .buildDefaultString(ConstantUtil.SERVER_URL_EXCLUSIVE_LIST);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Request_Exclusive_List, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestHomeData(IUserListener observer, String functionJsonString) {
        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Group_Home);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Group_Home, 0);
        Object str = HttpSubmitDataManager.getBuilder()
                .buildGetGroupFinanceString(functionJsonString);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Group_Home, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestHomeDataBD(IUserListener observer, String functionJsonString) {
        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Group_Home_BD);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Group_Home_BD, 0);
        Object str = HttpSubmitDataManager.getBuilder()
                .buildGetGroupFinanceString(functionJsonString);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Group_Home_BD, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestDiscoverData(IUserListener observer, String functionJsonString) {
        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Group_Discover);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Group_Discover, 0);
        Object str = HttpSubmitDataManager.getBuilder()
                .buildGetGroupFinanceString(functionJsonString);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Group_Discover, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestOwerData(IUserListener observer, String functionJsonString) {
        // TODO Auto-generated method stub
        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Group_Ower);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Group_Ower, 0);
        Object str = HttpSubmitDataManager.getBuilder()
                .buildGetGroupOwerString(functionJsonString);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Group_Ower, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestOwerMenuList(IUserListener observer, String functionJsonString) {
        // TODO Auto-generated method stub
        // cash key
        Object str = HttpSubmitDataManager.getBuilder()
                .buildGetGroupOwerString(functionJsonString);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Ower_List, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestHelpCommonInfo(IUserListener observer) {
        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Help_Common);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Help_Common, 0);
        Object str = HttpSubmitDataManager.getBuilder()
                .buildDefaultString(ConstantUtil.SERVER_URL_HELP_COMMON);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Help_Common, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }

    /**
     * 请求V6版本理财页面数据
     */
    @Override
    public void requestFinanceData(IUserListener observer, String functionJsonString) {
        // cash key
        addObserver(observer, ConstantUtil.Net_Tag_Group_Finance_v6);
        mRequestPageNums.put(ConstantUtil.Net_Tag_Group_Finance_v6, 0);
        Object str = HttpSubmitDataManager.getBuilder()
                .buildGetGroupFinanceString(functionJsonString);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequestNetCache(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Group_Finance_v6, str, iNetListener, 0);
        addObserver(observer, mCurrentRequestId);
    }


    /**
     * 统一请求的方法
     * 只有function name 没有参数
     * 比如请求修改交易密码的数据，
     * @param observer
     * @param funName
     */
    @Override
    public void requestDefaultPostDataAndUrl(IUserListener observer, String funName) {

        Object str = HttpSubmitDataManager.getBuilder()
                .buildDefaultString(funName);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Get_PostData_Url, str, iNetListener);
        addObserver(observer, mCurrentRequestId);



    }

    @Override
    public void requestLoginSource(IUserListener observer) {
        Object str = HttpSubmitDataManager.getBuilder()
                .buildDefaultString(ConstantUtil.SERVER_URL_GET_LOGIN_SOURCE);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL,
                ConstantUtil.Net_Tag_Get_LoginSource_Url, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

}
