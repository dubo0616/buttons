package com.jindan.p2p.http.builder;

import java.util.HashMap;
import com.jindan.p2p.json.JsonManager;
import com.jindan.p2p.net.NetConfig;
import com.jindan.p2p.utils.ConstantUtil;


public class SubmitDataBuilder implements HttpSubmitDataBuilderInterface {

    @Override
    public Object buildDefaultString(String funName) {

        Object res = null;

        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {

            // res = FormManager.getFormBuilder().requestSetPayPwd(newpwd, oldpwd);

        } else {

            res = JsonManager.getJsonBuilder().buildDefaultString(funName);

        }

        return res;

    }

    /**
     * 组合网络请求接口参数
     * @param funName
     * @param params
     * @return
     */
    @Override
    public Object buildDefaultString(String funName, HashMap<String, String> params) {

        Object res = null;

        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {

//            res = FormManager.getFormBuilder().buildDefaultString();

        } else {

            res = JsonManager.getJsonBuilder().buildDefaultString(funName, params);

        }

        return res;
    }


    @Override
    public Object buildGetGroupOwerString(String funJsonString) {

        Object res = null;

        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {

        } else {

            res = JsonManager.getJsonBuilder().buildGetGroupOwerString(funJsonString);

        }

        return res;

    }


    @Override
    public Object buildGetGroupFinanceString(String funJsonString) {
        // TODO Auto-generated method stub
        Object res = null;

        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {

            // res = FormManager.getFormBuilder().buildRequestBankListString(pageNum, pageSize);

        } else {

            res = JsonManager.getJsonBuilder().buildGetGroupFinanceString(funJsonString);

        }

        return res;

    }

    @Override
    public Object buildRequestUploadLog(String content) {
        // TODO Auto-generated method stub

        Object res = null;

        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {

            // res = FormManager.getFormBuilder().buildRequestIncomeStatusListString(pageNum, pageSize);

        } else {

            res = JsonManager.getJsonBuilder().buildRequestUploadLog(content);

        }

        return res;

    }

//
//    @Override
//    public Object buildDefaultRegistString() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public Object buildUserRegistString(String username, String password) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public Object buildRegistWithPhoneString(String phone, String msmcode) {
//        // TODO Auto-generated method stub
//
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            res = FormManager.getFormBuilder().buildRegistWithPhoneString(
//                    phone, msmcode);
//
//        } else {
//
//            res = JsonManager.getJsonBuilder().buildRegistWithPhoneString(
//                    phone, msmcode);
//
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildLoginStringBySms(String phone, String msmcode) {
//        // TODO Auto-generated method stub
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            res = FormManager.getFormBuilder().buildLoginString(phone, msmcode);
//
//        } else {
//
//            res = JsonManager.getJsonBuilder().buildLoginBySmsString(phone,
//                    msmcode);
//
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildLoginStringByPwd(String phone, String pwd) {
//        // TODO Auto-generated method stub
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            res = FormManager.getFormBuilder().buildLoginString(phone, pwd);
//
//        } else {
//
//            res = JsonManager.getJsonBuilder()
//                    .buildLoginByPwdString(phone, pwd);
//
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildRetrievePwdString() {
//
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            res = FormManager.getFormBuilder().buildRetrievePwdString();
//
//        } else {
//
//            res = JsonManager.getJsonBuilder().buildRetrievePwdString();
//
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildSetPayPwd(String newpwd, String oldpwd, String ck) {
//
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            res = FormManager.getFormBuilder().requestSetPayPwd(newpwd, oldpwd);
//
//        } else {
//
//            res = JsonManager.getJsonBuilder().requestSetPayPwd(newpwd, oldpwd,
//                    ck);
//
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildGetSMSCodeString(String phonenumber, int type,
//                                        int verifyType) {
//
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            res = FormManager.getFormBuilder().buildRequestVerifyCodeString(
//                    phonenumber);
//
//        } else {
//
//            res = JsonManager.getJsonBuilder().buildRequestVerifyCodeString(
//                    phonenumber, type, verifyType);
//
//        }
//
//        return res;
//    }
//
//    @SuppressWarnings({ "rawtypes", "null" })
//    @Override
//    public String buildGetUrlString(String url, HashMap<String, String> params) {
//
//        StringBuilder res = new StringBuilder();
//
//        res.append(url + "?");
//
//        if (params != null || !params.isEmpty()) {
//            Set set = params.keySet();
//            Iterator it = set.iterator();
//            while (it.hasNext()) {
//                String key = (String) it.next();
//                res.append(key + "=" + params.get(key) + "&");
//            }
//        }
//
//        return res.toString();
//    }
//
//    @Override
//    public Object buildBindBankString(String funName, String username,
//                                      String idCard, String bankcard, String bankphone, String bankcardid) {
//
//        Object res = null;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            // res =
//            // FormManager.getFormBuilder().buildBindBankCardString(username,
//            // idCard, bankcard, bankphone, bankcardid, smscode);
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildBindBankCardString(funName,
//                    username, idCard, bankcard, bankphone, bankcardid);
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildBindBankStatusString(String funName) {
//
//    	Object res = null;
//
//    	if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//    	} else {
//    		res = JsonManager.getJsonBuilder().buildBindBankStatusString(funName);
//    	}
//
//    	return res;
//    }
//
//    @Override
//    public Object buildRequestUserNewestInfoString() {
//
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            res = FormManager.getFormBuilder()
//                    .buildRequestUserNewestInfoString();
//
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRequestUserNewestInfoString();
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildRequestGoodsDetailInfoString(String goodsId) {
//
//        Object res;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder()
//                    .buildRequestGoodsDetailInfoString(goodsId);
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRequestGoodsDetailInfoString(goodsId);
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildRequestRedeemListString(int pageNum, int pageSize) {
//
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestRedeemListString(
//                    pageNum, pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestRedeemListString(
//                    pageNum, pageSize);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestInvestListString(int pageNum, int pageSize) {
//
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestInvestListString(
//                    pageNum, pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestInvestListString(
//                    pageNum, pageSize);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestGoodsListString(String funName, int pageNum,
//                                              int pageSize) {
//
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestGoodsListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestGoodsListString(
//                    funName, pageNum, pageSize);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestInvestOrderIdString(String iUnlockConfig, String money, String goodsId, int type, int fromType, String bId, int refuseMatchBidding, String regularAcceptDay) {
//        // TODO Auto-generated method stub
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestInvestOrderIdString(
//                    money, goodsId);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestInvestOrderIdString(iUnlockConfig,
//                    money, goodsId, type, fromType, bId, refuseMatchBidding, regularAcceptDay);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestInvestPayString(String pay_pwd, String orderId, int fromType) {
//        // TODO Auto-generated method stub
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestInvestNewString(
//                    pay_pwd, orderId);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestInvestPayString(
//                    pay_pwd, orderId, fromType);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestRedeemOrderIdString(String money, String pId, int toType, int redeemType, int redeemAll) {
//        // TODO Auto-generated method stub
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestRedeemOrderIdString(
//                    money);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestRedeemOrderIdString(
//                    money, pId, toType, redeemType, redeemAll);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestSureRedeemOrderString(String orderId) {
//        // TODO Auto-generated method stub
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestRedeemOrderIdString(
//                    orderId);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestSureRedeemOrderString(orderId);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestCreateWithdrawOrderString(String money, String orderId, String bankSiteId) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestCreateWithdrawOrderString(money, orderId, bankSiteId);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestRedeemPayString(String pay_pwd, String orderId, String pId) {
//        // TODO Auto-generated method stub
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestRedeemNewString(
//                    pay_pwd, orderId);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestRedeemNewString(
//                    pay_pwd, orderId, pId);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestNewsPushListString(int pageNum, int pageSize,
//                                                 int type, int lastNewsId) {
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildRequestNewsPushListString(
//                    pageNum, pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestNewsPushListString(
//                    pageNum, pageSize, type, lastNewsId);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildBindFeedbackString(String message, String email) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildBindFeedbackString(message,
//                    email);
//        } else {
//            // res =
//            // JsonManager.getJsonBuilder().buildRequestIncomeStatusListString(pageNum,
//            // pageSize);
//        }
//        return res;
//    }
//
//    @Override
//    public Object requestActiveFinalInfo() {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildActiveFinalInfoString();
//        } else {
//            // res =
//            // JsonManager.getJsonBuilder().buildRequestIncomeStatusListString(pageNum,
//            // pageSize);
//        }
//        return res;
//    }
//
//    @Override
//    public Object requestActiveFinalDetailInfo(String activeId, int type) {
//        // TODO Auto-generated method stub
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder().buildActiveFinalDetailString(
//                    activeId, type);
//        } else {
//            res = JsonManager.getJsonBuilder().buildActiveFinalDetailString(
//                    activeId, type);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildSetLoginPwd(String newPwd, String oldPwd, String ck,
//                                   String mobile) {
//
//        Object res = null;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            // res = FormManager.getFormBuilder().requestSetPayPwd(newpwd,
//            // oldpwd);
//
//        } else {
//
//            res = JsonManager.getJsonBuilder().requestSetLoginPwd(newPwd,
//                    oldPwd, ck, mobile);
//
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildRequestBankListString(String funName, int pageNum,
//                                             int pageSize) {
//
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestBankListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestBankListString(
//                    funName, pageNum, pageSize);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildBindBankCardVerifyString(String funName, String msmcode) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestBankListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRrequestBindBankCardVerifyString(funName, msmcode);
//        }
//        return res;
//    }
//
//    /**
//     * 获取验证码
//     */
//    @Override
//    public Object buildBindCardGetSMSCodeString(String funName) {
//        // TODO Auto-generated method stub
//        Object res = null;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildBindCardGetSMSCodeString(orderId);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestVerifyCodeString(funName);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestInvestStatusListString(int pageNum, int pageSize) {
//
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder()
//                    .buildRequestInvestStatusListString(pageNum, pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRequestInvestStatusListString(pageNum, pageSize);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestIncomeStatusListString(int pageNum, int pageSize) {
//
//        Object res;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            res = FormManager.getFormBuilder()
//                    .buildRequestIncomeStatusListString(pageNum, pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRequestIncomeStatusListString(pageNum, pageSize);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestDiscoveryHtmlString() {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestIncomeStatusListString();
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRequestDiscoveryHtmlString();
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestSaftyHtmlString() {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestIncomeStatusListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRequestSaftyHtmlString();
//        }
//        return res;
//    }
//
//    @Override
//    public Object requestSmsCodeResetPwd(String phonenumber, int verifyType) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestIncomeStatusListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().requestSmsCodeResetPwd(
//                    phonenumber, verifyType);
//        }
//        return res;
//    }
//
//    @Override
//    public Object requestSmsCodeVerifyResetPwd(String phonenumber,
//                                               String smsCode) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestIncomeStatusListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().requestSmsCodeVerifyResetPwd(
//                    phonenumber, smsCode);
//        }
//        return res;
//    }
//
//    @Override
//    public Object requestChangeBankPhone(String mobile, String smsCode) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestIncomeStatusListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().requestChangeBankPhone(
//                    mobile, smsCode);
//        }
//        return res;
//    }
//
//    @Override
//    public Object requestCheckIdCardResetPwd(String phonenumber, String idCard) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestIncomeStatusListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().requestCheckIdCardResetPwd(
//                    phonenumber, idCard);
//        }
//        return res;
//    }
//
//    @Override
//    public Object requestCheckOldPwdResetPwd(String oldPwd) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestIncomeStatusListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().requestCheckOldPwdResetPwd(
//                    oldPwd);
//        }
//        return res;
//    }
//
//    @Override
//    public Object requestPreOrder(String goodsId) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestIncomeStatusListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().requestPreOrder(goodsId);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestCheckForUpdate(String platform, String version,
//                                             int build) {
//        // TODO Auto-generated method stub
//        Object res = null;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            // res = FormManager.getFormBuilder().buildRequestCheckForUpdate();
//
//        } else {
//
//            try {
//                res = JsonManager.getJsonBuilder().buildRequestCheckForUpdate(platform, version, build);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } catch (StackOverflowError e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//        return res;
//    }
//
//
//    @Override
//    public Object buildRetrieveLoginPwd(String mobile, String ck) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res = FormManager.getFormBuilder().buildRetrieveLoginPwd(mobile);
//        } else {
//            res = JsonManager.getJsonBuilder().buildRetrieveLoginPwd(mobile, ck);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildBindBankCardVerifyMoney(String funName, String orderId,
//                                               String money) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestBankListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRrequestBindBankCardVerifyMoney(funName, orderId,
//                            money);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildBindBankCardGetMoney(String funName, String orderId) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestBankListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildRrequestBindBankCardGetMoney(funName, orderId);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildNewBindBankString(String funName, String bankcard,
//                                         String bankphone, String bankcardid) {
//        Object res = null;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            // res =
//            // FormManager.getFormBuilder().buildBindBankCardString(username,
//            // idCard, bankcard, bankphone, bankcardid, smscode);
//
//        } else {
//            res = JsonManager.getJsonBuilder()
//                    .buildNewStringBindBankCardString(funName, bankcard,
//                            bankphone, bankcardid);
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildGetProductImage(String funName) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestBankListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetProductImage(funName);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildGetTodayBiddingRecord(String funName, int limit, int offset) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestBankListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetTodayBiddingRecord(funName, limit, offset);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildGetOwerListInfo(String funName) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestBankListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetOwerListInfo(funName);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildGetNovicePacks(String funName) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//            // res =
//            // FormManager.getFormBuilder().buildRequestBankListString(pageNum,
//            // pageSize);
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetNovicePacks(funName);
//        }
//        return res;
//    }
//
//    public Object buildGetImageReource(String funName, ResourceModel.Type... resourceCode) {
//        // TODO Auto-generated method stub
//        Object res = null;
//
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//            // res =
//            // FormManager.getFormBuilder().buildBindBankCardString(username,
//            // idCard, bankcard, bankphone, bankcardid, smscode);
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetImageResourceString(funName, resourceCode);
//        }
//
//        return res;
//    }
//
//    @Override
//    public Object buildGetGroupDiscoverString(String funJsonString) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetGroupFinanceString(funJsonString);
//        }
//        return res;
//    }
//
//
//	@Override
//	public Object buildGetInvestAndRedeemConfigString(String pId, String type, String bId) {
//		// TODO Auto-generated method stub
//		Object res = null;
//		if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//		} else {
//			res = JsonManager.getJsonBuilder().buildGetInvestAndRedeemConfigString(pId, type, bId);
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetRedeemConfigString(String pId, String type) {
//		// TODO Auto-generated method stub
//		Object res = null;
//		if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//		} else {
//			res = JsonManager.getJsonBuilder().buildGetRedeemConfigString(pId, type);
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetWithdrawConfigString() {
//		// TODO Auto-generated method stub
//		Object res = null;
//		if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//		} else {
//			res = JsonManager.getJsonBuilder().buildGetWithdrawConfigString();
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetInvestBalanceString() {
//		// TODO Auto-generated method stub
//		Object res = null;
//		if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//		} else {
//			res = JsonManager.getJsonBuilder().buildGetInvestBalanceString();
//		}
//		return res;
//	}
//
//    @Override
//    public Object buildGetNewInvestAndRedeemConfigString(String funName, String pId, String type, String bId) {
//        // TODO Auto-generated method stub
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetNewInvestAndRedeemConfigString(funName, pId, type, bId);
//        }
//        return res;
//    }
//
//    /**
//     * 生成获取消息列表请求数据
//     *
//     */
//	@Override
//	public Object buildRequestNewsPushListString(String lastPublishTime) {
//    	Object res = null;
//    	if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//    	} else {
//    		res = JsonManager.getJsonBuilder().buildGetNewsPushListString(lastPublishTime);
//    	}
//    	return res;
//	}
//
//    /**
//     * 生成设置消息是否已读请求数据
//     *
//     */
//	@Override
//	public Object buildSetNewsIsReadString(String messageId) {
//    	Object res = null;
//    	if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//    	} else {
//    		res = JsonManager.getJsonBuilder().buildSetNewsIsReadString(messageId);
//    	}
//    	return res;
//	}
//
//	/** 生成获取消息Tip请求数据 */
//	@Override
//	public Object buildGetMessageTipString() {
//    	Object res = null;
//    	if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//    	} else {
//    		res = JsonManager.getJsonBuilder().buildGetMessageTipString();
//    	}
//    	return res;
//	}
//
//	/** 生成获取下拉刷新Tip请求数据 */
//	@Override
//	public Object buildGetRefreshTipString() {
//		Object res = null;
//		if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//		} else {
//			res = JsonManager.getJsonBuilder().buildGetRefreshTipString();
//		}
//		return res;
//	}
//
//	/** 生成获取下拉刷新Tip请求数据 */
//	@Override
//	public Object buildGetTodayCreditListString(String funName, String direct, int offsetId) {
//		Object res = null;
//		if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//		} else {
//			res = JsonManager.getJsonBuilder().buildGetTodayCreditListString(funName, direct, offsetId);
//		}
//		return res;
//	}
//
//    @Override
//
//    public Object buildRequestRechargeOrderIdString(String money) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetRechargeOrderIdString(money);
//        }
//        return res;
//    }
//
//    public Object buildOpenBankAccountString(String serverUrlNameOpenBankAccount, String username, String id_card, String bank_card, String phone_num, String sms_code, String bankCode) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildOpenBankAccountString(username, id_card, bank_card, phone_num, sms_code, bankCode);
//        }
//        return res;
//    }
//
//    /** 生成获取设置交易签名接口请求 */
//    @Override
//    public Object buildGetSetPasswordSignString(String funName) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetSetPasswordSignString(funName);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildRequestRechargeConfirmString(String orderId, String authSeq) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetRechargeConfirmString(orderId, authSeq);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildGetRegionString(String funName, String regionId) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetRegionString(funName, regionId);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildGetBankSiteString(String funName, String regionId, String keyword) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetRegionSiteString(funName, regionId, keyword);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildGetHtmlDocString(String funName, String name) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildGetHtmlDocString(funName, name);
//        }
//        return res;
//    }
//
//    @Override
//    public Object buildSetDotClickString(String funName, String dotType, String dotId) {
//        Object res = null;
//        if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.FORM) {
//
//        } else {
//            res = JsonManager.getJsonBuilder().buildSetDotClickString(funName, dotType, dotId);
//        }
//        return res;
//    }
//



}
