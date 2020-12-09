package com.jindan.p2p.http.builder;

import java.util.HashMap;

public interface HttpSubmitDataBuilderInterface {

    /**
     * 无参数请求
     */
    Object buildDefaultString(String funName);

    Object buildDefaultString(String funName, HashMap<String, String> params);

    /**
     * 获取首页组合接口
     *
     * @param funJsonString
     * @return
     */
    Object buildGetGroupFinanceString(String funJsonString);

    /**
     * 获取我的页组合接口
     *
     * @param funJsonString
     * @return
     */
    Object buildGetGroupOwerString(String funJsonString);

    Object buildRequestUploadLog(String content);

//
//    /**
//     * tag 1 Default regist
//     */
//    Object buildDefaultRegistString();
//
//    /*
//     * tag 2 regist with user name and password
//     */
//    Object buildUserRegistString(String username, String password);
//
//    /*
//     * tag 2 regist with user name and password
//     */
//    Object buildRegistWithPhoneString(String phone, String msmcode);
//
//    /*
//     * tag 4 login
//     */
//    Object buildLoginStringBySms(String phone, String msmcode);
//
//    /*
//     * tag 4 login
//     */
//    Object buildLoginStringByPwd(String phone, String pwd);
//
//    /*
//     * tag 2 regist with user name and password
//     */
//    Object buildGetSMSCodeString(String phonenumber, int type, int verifyType);
//
//    /*
//     * tag 5 modify password
//     */
//    Object buildRetrievePwdString();
//
//    /*
//     * tag 5 modify password
//     */
//    Object buildSetPayPwd(String newpwd, String oldpwd, String ck);
//
//    /*
//     * tag 5 modify password
//     */
//    Object buildSetLoginPwd(String newPwd, String oldPwd, String ck, String mobile);
//
//    /*
//     * tag 19 Build active info
//     */
//    Object buildRequestGoodsListString(String funName, int pageNum, int pageSize);
//
//    String buildGetUrlString(String url, HashMap<String, String> params);
//
//    /*
//     * tag 10 regist with user name and password
//     */
//    Object buildBindBankString(String funName, String username, String idCard, String bankcard, String bankphone, String bankcardid);
//
//    /*
//     * tag 20 binkcard bind status
//     */
//    Object buildBindBankStatusString(String funName);
//
//    /**
//     * 新的绑卡
//     *
//     * @param funName
//     * @param bankcard
//     * @param bankphone
//     * @param bankcardid
//     * @return
//     */
//    Object buildNewBindBankString(String funName, String bankcard, String bankphone, String bankcardid);
//
//    /*
//     * tag 10 regist with user name and password
//     * ConstantUtil.SERVER_URL_NAME_BANK_BIND_VERIFY, orderId, msmcode
//     */
//    Object buildBindBankCardVerifyString(String funName, String msmcode);
//
//    /**
//     * 验证金额
//     *
//     * @param funName
//     * @param orderId
//     * @param msmcode
//     * @return
//     */
//    Object buildBindBankCardVerifyMoney(String funName, String orderId, String msmcode);
//
//    /*
//     * tag 9 Build get user info request request user info
//     */
//    Object buildRequestUserNewestInfoString();
//
//    /*
//     * tag 11 Build active info
//     */
////    Object buildRequestActiveDetailInfoString(String activeId);
//
//    /*
//     * tag 12 Build active info
//     */
////    Object buildRequestNewActivityInfoString();
//
//    /*
//     * tag 13 Build active info
//     */
////    Object buildRequestNewGoodsInfoString();
//
//    /*
//     * tag 14 Build active info
//     */
//    Object buildRequestGoodsDetailInfoString(String goodsId);
//
//    /*
//     * tag 15 Build active info
//     */
////    Object buildRequestInvestString(String money, String pay_pwd, String goodsId);
//
//    /*
//     * tag 16 Build active info
//     */
////    Object buildRequestRedeemString(String money, String pay_pwd);
//
//    /*
//     * tag 17 Build active info
//     */
//    Object buildRequestRedeemListString(int pageNum, int pageSize);
//
//    /*
//     * tag 18 Build active info
//     */
//    Object buildRequestInvestListString(int pageNum, int pageSize);
//
//    /*
//     * tag 20 Build active info
//     */
//    Object buildRequestBankListString(String funName, int pageNum, int pageSize);
//
//    /*
//     * tag 21 Build active info
//     */
//    Object buildRequestInvestStatusListString(int pageNum, int pageSize);
//
//    /*
//     * tag 22 Build active info
//     */
//    Object buildRequestIncomeStatusListString(int pageNum, int pageSize);
//
//    /*
//     * tag 23 Build active info
//     */
////    Object requestTotalMoneyProfitInfo();
//
//    /*
//     * tag 23 Build active info
//     */
////    Object requestTotalUserSumInfo();
//
//    /*
//     * tag 24 Build active info
//     */
////    Object buildRequestRewardListString(int pageNum, int pageSize);
//
//    /*
//     * tag 25 Build active info
//     */
//    Object buildRequestInvestOrderIdString(String iUnlockConfig, String money, String goodsId, int type, int fromType, String bId, int refuseMatchBidding, String regularAcceptDay);
//
//    /*
//     * tag 26 Build active info
//     */
//    Object buildRequestInvestPayString(String pay_pwd, String orderId, int fromType);
//
//    /*
//     * tag 27 Build active info
//     */
//    Object buildRequestRedeemOrderIdString(String money, String pId, int toType, int redeemType, int redeemAll);
//
//    Object buildRequestSureRedeemOrderString(String orderId);
//
//    Object buildRequestCreateWithdrawOrderString(String money, String orderId, String bankSiteId);
//
//    /*
//     * tag 28 Build active info
//     */
//    Object buildRequestRedeemPayString(String pay_pwd, String orderId, String pId);
//
//    /*
//     * tag 29 modify password
//     */
////    Object buildVerifyPayPwd(String pay_pwd);
//
//    /*
//     * tag 30 modify password
//     */
////    Object buildRequestCreditDetail(String creditId, String orderId);
//
//    /**
//     * "platform" : 平台, "version" : 用户可见版本号, "build" : 检查更新版本号
//     */
//    Object buildRequestCheckForUpdate(String platform, String version, int build);
//
//    /*
//     * tag 32 regist with user name and password
//     */
////    Object buildRrequestBindBankCardNewString(String funName, String orderId, String msmcode);
//
//    /*
//     * tag 33 regist with user name and password
//     */
//    Object buildBindCardGetSMSCodeString(String funName);
//
//    /*
//     * tag 22 Build active info
//     */
//    Object buildRequestNewsPushListString(int pageNum, int pageSize, int type, int lastNewsId);
//
//    /*
//     * tag 33 regist with user name and password
//     */
//    Object buildBindFeedbackString(String message, String email);
//
//    /*
//     * tag 23 Build active info
//     */
//    Object requestActiveFinalInfo();
//
//    /*
//     * tag 23 Build active info
//     */
//    Object requestActiveFinalDetailInfo(String activeId, int type);
//
//    /*
//     * tag 22 Build active info
//     */
//    Object buildRequestDiscoveryHtmlString();
//
//    /*
//     * tag 22 Build active info
//     */
//    Object buildRequestSaftyHtmlString();
//
//    Object requestSmsCodeResetPwd(String phonenumber, int verifyType);
//
//    Object requestSmsCodeVerifyResetPwd(String phonenumber, String smsCode);
//
//    Object requestChangeBankPhone(String mobile, String smsCode);
//
//    Object requestCheckIdCardResetPwd(String phonenumber, String idCard);
//
//    Object requestCheckOldPwdResetPwd(String oldPwd);
//
//    Object requestPreOrder(String goodsId);
//

//
//    Object buildRetrieveLoginPwd(String mobile, String ck);
//
//    Object buildGetImageReource(String funName, ResourceModel.Type... resourceCode);
//
//
//    /**
//     * 重新发送金额
//     *
//     * @param funName
//     * @param orderId
//     * @return
//     */
//
//    Object buildBindBankCardGetMoney(String funName, String orderId);
//
//    /**
//     * 获取产品图
//     *
//     * @param funName
//     * @return
//     */
//    Object buildGetProductImage(String funName);
//
//    /**
//     * 获取首页今日标的记录
//     *
//     * @param funName
//     * @return
//     */
//    Object buildGetTodayBiddingRecord(String funName, int limit, int offset);
//
//
//    /**
//     * 获取我的页面列表数据
//     *
//     * @param funName
//     * @return
//     */
//    Object buildGetOwerListInfo(String funName);
//
//    /**
//     * 获取首页新手礼包
//     *
//     * @param funName
//     * @return
//     */
//    Object buildGetNovicePacks(String funName);
//
//    /**
//     * 获取发现页组合接口
//     *
//     * @param funJsonString
//     * @return
//     */
//    Object buildGetGroupDiscoverString(String funJsonString);
//
//
//    /**
//     * 获取投资提现配置接口
//     *
//     * @param pId
//     * @return
//     */
//    Object buildGetInvestAndRedeemConfigString(String pId, String type, String bId);
//
//    /**
//     * 获取赎回配置接口
//     *
//     * @param pId
//     * @param type
//     * @return
//     */
//    Object buildGetRedeemConfigString(String pId, String type);
//
//    /**
//     * 获取提现配置接口
//     *
//     * @return
//     */
//    Object buildGetWithdrawConfigString();
//
//    /**
//     * 投资页余额查询
//     * @return
//     */
//    Object buildGetInvestBalanceString();
//
//    /**
//     * 获取配置新的接口
//     * 充值，投资，提现，赎回都可以使用
//     * @return
//     */
//    Object buildGetNewInvestAndRedeemConfigString(String funName, String pId, String type, String bId);
//
//
//    /**
//     * 获取消息中心消息列表请求数据
//     *
//     */
//	Object buildRequestNewsPushListString(String lastPublishTime);
//	/**
//	 * 获取设置消息是否已读数据
//	 *
//	 * @param messageId: 消息ID
//	 */
//	Object buildSetNewsIsReadString(String messageId);
//	/**
//	 * 获取消息Tip数据
//	 *
//	 */
//	Object buildGetMessageTipString();
//
//	/**
//	 * 获取下拉刷新的文案
//	 * @return
//	 */
//	Object buildGetRefreshTipString();
//
//	Object buildGetTodayCreditListString(String funName, String direct,	int offsetId);
//
//
//    /*
//    * tag 27 Build active info
//    */
//    Object buildRequestRechargeOrderIdString(String money);
//
//
//    /*
//     * tag 26 Build active info
//     */
//    Object buildRequestRechargeConfirmString(String orderId, String authSeq);
//    /**
//     * 获取银行托管开户请求
//     * @return
//     */
//    Object buildOpenBankAccountString(String funName, String username, String id_card, String bank_card, String phone_num, String sms_code, String bankCode);
//    /**
//     * 获取设置交易密码签名请求
//     * 说明: 因为签名信息会根据参数和参数值不同而变化，因此进行交易请求前都需要
//     * 获取相应的交易签名
//     *
//     * @param funName: 接口名称
//     */
//    Object buildGetSetPasswordSignString(String funName);
//
//    /**
//     *
//     * @param funName
//     * @param regionId
//     * @return
//     */
//    Object buildGetRegionString(String funName, String regionId);
//
//    /**
//     *
//     * @param funName
//     * @param regionId
//     * @return
//     */
//    Object buildGetBankSiteString(String funName, String regionId, String keyword);
//
//    /**
//     *
//     * @param funName
//     * @param name
//     * @return
//     */
//    Object buildGetHtmlDocString(String funName, String name);
//
//    /**
//     *
//     * @param funName
//     * @param dotType
//     * @param dotId
//     * @return
//     */
//    Object buildSetDotClickString(String funName, String dotType, String dotId);
//

//    @Override
//    public Object buildVerifyPayPwd(String pay_pwd) {
//        // TODO Auto-generated method stub
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            res = FormManager.getFormBuilder().buildVerifyPayPwd(pay_pwd);
//
//        } else {
//
//            res = JsonManager.getJsonBuilder().buildVerifyPayPwd(pay_pwd);
//
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildRequestCreditDetail(String creditId, String orderId) {
//        // TODO Auto-generated method stub
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            res = FormManager.getFormBuilder().buildRequestCreditDetail(
//                    creditId, orderId);
//
//        } else {
//
//            res = JsonManager.getJsonBuilder().buildVerifyPayPwd(creditId);
//
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildRrequestBindBankCardNewString(String funName,
//                                                     String orderId, String msmcode) {
//        Object res = null;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            // res =
//            // FormManager.getFormBuilder().buildRrequestBindBankCardNewString(orderId,
//            // msmcode);
//
//        } else {
//
//            res = JsonManager.getJsonBuilder().buildVerifyPayPwd("");
//
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object requestTotalUserSumInfo() {
//        // TODO Auto-generated method stub
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().requestTotalUserSumInfo();
//        } else {
//            res = JsonManager.getJsonBuilder().requestTotalMoneyProfitInfo();
//        }
//        return res;
//    }
//
//    @Override
//    public Object requestTotalMoneyProfitInfo() {
//
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().requestTotalMoneyProfitInfo();
//        } else {
//            res = JsonManager.getJsonBuilder().requestTotalMoneyProfitInfo();
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestRewardListString(int pageNum, int pageSize) {
//
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestRewardListString(
//                    pageNum, pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestRewardListString(
//                    pageNum, pageSize);
//        }
//        return res;
//    }
//    @Override
//    public Object buildRequestInvestString(String money, String pay_pwd,
//                                           String goodsId) {
//
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestInvestString(money,
//                    pay_pwd, goodsId);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestInvestString(money,
//                    pay_pwd, goodsId);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestRedeemString(String money, String pay_pwd) {
//
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestRedeemString(money,
//                    pay_pwd);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestRedeemString(money,
//                    pay_pwd);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestActiveDetailInfoString(String activeId) {
//
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder()
//                    .buildRequestActiveDetailInfoString(activeId);
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRequestActiveDetailInfoString(activeId);
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildRequestNewActivityInfoString() {
//
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder()
//                    .buildRequestNewActivityInfoString();
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRequestNewActivityInfoString();
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildRequestNewGoodsInfoString() {
//
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestNewGoodsInfoString();
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestNewGoodsInfoString();
//        }
//
//        return res;
//    }
}