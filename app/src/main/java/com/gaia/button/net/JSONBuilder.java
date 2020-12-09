package com.gaia.button.net;

import android.os.Build;
import android.text.TextUtils;

import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.utils.StringConstant;
import com.gaia.button.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class JSONBuilder implements JsonBuilderInterface {
	/** 版本名称 */
	String versionName = "";

	String channelName = "";
//	/** 手机号 */
//	String phonenumber = "";
	/** IMEI号 */
	String imeiString = "";
//	/** SIM号 */
//	String simnumber = "";
	/** IMSI */
	String imsiString = "";
	/** OS */
	String osString = "";
	/** UA */
	String uaString = "";
	/** UUID */
	String udid = "";

	/** UUID */
	private static String localSalt = "762b21fc8da871dada41f58ebd0ab172";

	public JSONBuilder() {
	}



	private TreeMap<String, String> baseSignMap = new TreeMap<>();
	private TreeMap<String, String> signMap = new TreeMap<>();

	private JSONObject createJsonObject(JSONObject funBody) throws JSONException {
		JSONObject mJsonObject = new JSONObject();
		return mJsonObject;
	}

	@Override
	public String buildDefaultString(String funName) {

		String res = null;

		try {

			JSONObject jsonBody = new JSONObject();

			JSONObject funBody = new JSONObject();

			funBody.put(funName, jsonBody);

			JSONObject jsonObj = createJsonObject(funBody);


			res = jsonObj.toString();

		} catch (Exception e) {


		}

		return res;
	}

	/**
	 * 单接口网络请求参数
	 * @param funName
	 * @param params
	 * @return
	 */
	@Override
	public String buildDefaultString(String funName, HashMap<String, String> params) {

		String res = null;

		try {

			JSONObject jsonBody = new JSONObject();

			if(params != null) {

				for(String key : params.keySet()) {
					jsonBody.put(key, Utils.GetStringNoNil(params.get(key)));
				}
			}

			JSONObject funBody = new JSONObject();

			funBody.put(funName, jsonBody);

			JSONObject jsonObj = createJsonObject(funBody);


			res = jsonObj.toString();

		} catch (Exception e) {


		}

		return res;
	}

	@Override
	public Object buildGetGroupFinanceString(String funJsonString) {

		String res = null;
		try {
			JSONObject jsonObj = createJsonObject(null);
			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, new JSONObject(funJsonString));
			res = jsonObj.toString();
		} catch (Exception e) {
		}
		return res;
	}

	@Override
	public Object buildGetGroupOwerString(String funJsonString) {
		String res = null;
		try {
			JSONObject jsonObj = createJsonObject(null);
			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, new JSONObject(funJsonString));
			res = jsonObj.toString();
		} catch (Exception e) {
		}
		return res;
	}

    @Override
    public Object buildRequestUploadLog(String content) {
        String res = null;

        try {

//            JSONObject jsonBody = new JSONObject();
//            JSONArray array = new JSONArray(content);
//            jsonBody.put("data", array);
//            JSONObject funBody = new JSONObject();
//            funBody.put(ConstantUtil.SERVER_URL_NAME_USER_BEHAVIOR_LOG,
//                    jsonBody);
//			JSONObject jsonObj = createJsonObject(funBody);
//            jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//            res = jsonObj.toString();

        } catch (Exception e) {

        }
        return res;
    }


	/**
	 * 将json对象转换成Map
	 *
	 * @param jsonObject json对象
	 * @return Map对象
	 */
	@SuppressWarnings("unchecked")
	public static TreeMap<String, String> toTreeMap(JSONObject jsonObject)
	{
		TreeMap<String, String> result = new TreeMap<String, String>();
		Iterator<String> iterator = jsonObject.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext())
		{
			key = iterator.next();
			if (StringConstant.JSON_SALT.equals(key)) {
				continue;
			}
			try {
				value = jsonObject.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (!TextUtils.isEmpty(value)) {
				result.put(key, value);
			}
		}
		return result;
	}


	/**
	 * 创建一个按照key倒序排序的TreeMap对象，这个对象具有实时按序排列元素的能力
	 * @return
	 */
	public Map createTreeMap (Map<String, String> datas){
//		Map<String, String> map = new TreeMap<String, String>(
//				new Comparator<String>() {
//					public int compare(String obj1, String obj2) {
//						// 降序排序，TreeMap默认是升序
//						return obj2.compareTo(obj1);
//					}
//				});
		Map<String, String> map = new TreeMap<String, String>();
		map.put("platform", "android");
		map.put("mac", Utils.GetStringNoNil(imeiString));

		return map;
	}

//	/**
//	 * 拼接Sign
//	 * @param map
//	 * @return
//	 * @throws Exception
//     */
//	private Map putSign(Map map) throws Exception {
//		if(map != null) {
//			map.put("appkey", ConstantUtil.APP_KEY);
//			StringBuilder sb = new StringBuilder();
//			Set<String> keySet = map.keySet();
//			Iterator<String> iter = keySet.iterator();
//			while (iter.hasNext()) {
//				String key = iter.next();
//				sb.append(key).append("=").append(map.get(key)).append("&");
//			}
//			sb.delete(sb.length() - 1, sb.length());
//			LogUtil.e("TAG", "putSign: " + sb);
//			String md5String = Md5Tools.toMd5(StringUtils.getBase64(sb.toString()).getBytes("utf-8"), false);
//			LogUtil.e("TAG", "putSign md5: " + md5String);
//			map.put("sign", md5String);
//			// appkey在生成sign之后需要从接口参数中剔除。
//			map.remove("appkey");
//		}
//		return map;
//	}


//	@Override
//	public Object buildGetGroupDiscoverString(String funJsonString) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, new JSONObject(funJsonString));
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	/*
//	 * tag 2 User register
//	 */
//	public String buildUserRegistString(String username, String password) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//			jsonObj.put(StringConstant.JSON_USER_NAME, Utils.GetStringNoNil(username));
//			jsonObj.put(StringConstant.JSON_PASSWORD, Utils.GetStringNoNil(password));
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	/*
//	 * tag 3 User register
//	 */
//	public String buildRegistWithPhoneString(String phone, String msmcode) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_USER_PHONE, Utils.GetStringNoNil(phone));
//			jsonBody.put(StringConstant.JSON_USER_PHONE_MSM_CODE, Utils.GetStringNoNil(msmcode));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_REGISTER, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	/*
//	 * tag 4 User login
//	 */
//	public String buildLoginBySmsString(String phone, String msmcode) {
//
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_USER_PHONE, Utils.GetStringNoNil(phone));
//			jsonBody.put(StringConstant.JSON_USER_PHONE_MSM_CODE, Utils.GetStringNoNil(msmcode));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_LOGIN_SMS, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	/*
//	 * tag 4 User login
//	 */
//	public String buildLoginByPwdString(String phone, String pwd) {
//
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_USER_PHONE, Utils.GetStringNoNil(phone));
//			jsonBody.put(StringConstant.JSON_PASSWORD, Utils.GetStringNoNil(pwd));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_LOGIN_PWD, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public String buildRequestVerifyCodeString(String phonenumber, int type,
//			int verifyType) {
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_USER_PHONE, Utils.GetStringNoNil(phonenumber));
//
//			String actionTypeStr = "" + type;
//			//action_type":类型, //1(登录)、2(注册)、 5 开户 openAccoun  7 更改银行手机号
//			if (type == ConstantUtil.SMS_CODE_TYPE_LOGIN) {
//				actionTypeStr = "login";
//			} else if (type == ConstantUtil.SMS_CODE_TYPE_REGISTER) {
//				actionTypeStr = "register";
//			} else if (type == ConstantUtil.SMS_CODE_TYPE_ACCOUNT) {
//				actionTypeStr ="openBankAccount";
//			} else if (type == ConstantUtil.SMS_CODE_TYPE_CHANGE_BANK_PHONE) {
//				actionTypeStr ="changeBankMobile";
//			}
//			jsonBody.put("actionType", actionTypeStr);
//			jsonBody.put("verifyType", String.valueOf(verifyType));
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_TAG_8, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			return jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	public Object buildRequestInvestOrderIdString(String iUnlockConfig, String money, String goodsId, int type, int fromType, String bId, int refuseMatchBidding, String regularAcceptDay) {
//		String res = null;
//
//		try {
//
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_RECHARGE_MONEY, Utils.GetStringNoNil(money));
//			jsonBody.put(StringConstant.JSON_GOODS_ID, Utils.GetStringNoNil(goodsId));
//			jsonBody.put(StringConstant.JSON_GOODS_TYPE, type);
//
//			if (ConstantUtil.Is_Old_Version) {
//				jsonBody.put(StringConstant.JSON_INVEST_REFUSEMATCH, refuseMatchBidding);
//			} else {
//				jsonBody.put(StringConstant.JSON_INVEST_AGREE_NEXT_BID, refuseMatchBidding);
//				jsonBody.put(StringConstant.JSON_INVEST_REGULAR_ACCEPT_DAY, regularAcceptDay);
//			}
//
//			jsonBody.put(StringConstant.JSON_GOODS_FROM_TYPE, fromType);
//
//			jsonBody.put(StringConstant.JSON_IUNLOCK_CONFIG, Utils.GetStringNoNil(iUnlockConfig));
//
//			jsonBody.put(StringConstant.JSON_INVEST_BID, Utils.GetStringNoNil(bId));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_INVEST_CREATE, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public String requestSetLoginPwd(String newPwd, String oldPwd, String ck,
//			String mobile) {
//		String res = null;
//
//		try {
//
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			if (!TextUtils.isEmpty(newPwd)) {
//				jsonBody.put(StringConstant.JSON_PASSWORD, Utils.GetStringNoNil(newPwd));
//			}
//			if (!TextUtils.isEmpty(oldPwd)) {
//				jsonBody.put(StringConstant.JSON_PASSWORD_OLD, Utils.GetStringNoNil(oldPwd));
//			}
//			if (!TextUtils.isEmpty(ck)) {
//				jsonBody.put("ck", Utils.GetStringNoNil(ck));
//			}
//			if (!TextUtils.isEmpty(mobile)) {
//				jsonBody.put(StringConstant.JSON_PHONE_NUMBER, Utils.GetStringNoNil(mobile));
//			}
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_LOGINPWD_SET, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public String buildRequestGoodsListString(String funName, int pageNum,
//			int pageSize) {
//
//		String res = null;
//
//		try {
//
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_LIST_PAGE_NUM, pageNum);
//			jsonBody.put(StringConstant.JSON_LIST_PAGE_SIZE, pageSize);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public String buildRequestBankListString(String funName, int pageNum,
//			int pageSize) {
//
//		String res = null;
//
//		try {
//
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_LIST_PAGE_NUM, pageNum);
//			jsonBody.put(StringConstant.JSON_LIST_PAGE_SIZE, pageSize);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//
//	@Override
//	public String buildBindBankCardString(String funName, String username,
//			String idCard, String bankcard, String bankphone, String bankcardid) {
//
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_USER_NAME, Utils.GetStringNoNil(username));
//			jsonBody.put(StringConstant.JSON_USER_ID_CARD, Utils.GetStringNoNil(idCard));
//			jsonBody.put(StringConstant.JSON_USER_BANK_CARD, Utils.GetStringNoNil(bankcard));
//			jsonBody.put(StringConstant.JSON_USER_BANK_MOBILE, Utils.GetStringNoNil(bankphone));
//			jsonBody.put(StringConstant.JSON_USER_BANK_ID, Utils.GetStringNoNil(bankcardid));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public String buildBindBankStatusString(String funName) {
//
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public String buildRrequestBindBankCardVerifyString(String funName, String msmcode) {
//
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_USER_PHONE_MSM_CODE, Utils.GetStringNoNil(msmcode));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	/**
//	 * 也可以做发送金额的json拼装
//	 */
//	@Override
//	public Object buildRequestVerifyCodeString(String funName) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public Object buildRequestInvestPayString(String pay_pwd, String orderId, int fromType) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_ORDER_ID, Utils.GetStringNoNil(orderId));
//			jsonBody.put(StringConstant.JSON_INVEST_PAY_PWD, Utils.GetStringNoNil(pay_pwd));
//			jsonBody.put(StringConstant.JSON_GOODS_FROM_TYPE, fromType);
//
//			JSONObject funBody = new JSONObject();
//
//			if (ConstantUtil.Is_Old_Version) {
//				funBody.put("payInvest", jsonBody);
//			} else {
//				funBody.put(ConstantUtil.SERVER_URL_NAME_PAY_INVEST, jsonBody);
//			}
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public Object buildRequestRedeemNewString(String pay_pwd, String orderId, String pId) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_ORDER_ID, Utils.GetStringNoNil(orderId));
//			jsonBody.put(StringConstant.JSON_INVEST_PAY_PWD, Utils.GetStringNoNil(pay_pwd));
//			jsonBody.put(StringConstant.JSON_RECHARGE_TYPE_PID, Utils.GetStringNoNil(pId));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_PAY_REDEEM, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public Object buildRequestRedeemOrderIdString(String money, String pId, int toType, int redeemType, int isWhole) {
//
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put(StringConstant.JSON_RECHARGE_MONEY, Utils.GetStringNoNil(money));
//
//			jsonBody.put(StringConstant.JSON_RECHARGE_TYPE_PID, Utils.GetStringNoNil(pId));
//			jsonBody.put(StringConstant.JSON_RECHARGE_REDEEMTYPE, redeemType);
//
//			if (ConstantUtil.Is_Old_Version) {
//				jsonBody.put(StringConstant.JSON_RECHARGE_REDEEMALL, isWhole);
//			} else {
//				jsonBody.put(StringConstant.JSON_RECHARGE_ISWHOLE, isWhole);
//			}
//			if (toType > 0) {
//				jsonBody.put(StringConstant.JSON_RECHARGE_TOTYPE, toType);
//			}
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_REDEEM_ORDERID, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildRequestSureRedeemOrderString(String orderId) {
//
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put(StringConstant.JSON_ORDER_ID, orderId);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_SURE_REDEEM_ORDER, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	/**
//	 * 提现接口
//	 * 说明：提现支持小额提现和大额提现
//	 *
//	 * @param money：提现金额(大额提现为空)
//	 * @param orderId：订单ID(大额提现必须)
//	 * @param bankSiteId: 支行ID(大额提现必须)
//	 *
//	 */
//	@Override
//	public Object buildRequestCreateWithdrawOrderString(String money, String orderId, String bankSiteId) {
//
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			if(!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(bankSiteId)) {
//				//大额提现方式
//				jsonBody.put(StringConstant.JSON_REDEEM_TAKE_MONEY, "");
//				jsonBody.put(StringConstant.JSON_ORDER_ID, orderId);
//				jsonBody.put(StringConstant.JSON_BANK_SITE_ID, bankSiteId);
//			} else {
//				//小额提现只需要金额
//				jsonBody.put(StringConstant.JSON_REDEEM_TAKE_MONEY, Utils.GetStringNoNil(money));
//				jsonBody.put(StringConstant.JSON_ORDER_ID, "");
//				jsonBody.put(StringConstant.JSON_BANK_SITE_ID, "");
//			}
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_CREATE_WITHDRAW_ORDER, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public String buildRequestRedeemListString(int pageNum, int pageSize) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put("name", ConstantUtil.SERVER_URL_NAME_REDEEM_LIST);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_LIST_HTML_DOM, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public String buildRequestInvestListString(int pageNum, int pageSize) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put("name", ConstantUtil.SERVER_URL_NAME_INVEST_LIST);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_LIST_HTML_DOM, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildRequestInvestStatusListString(int pageNum, int pageSize) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put("name", ConstantUtil.SERVER_URL_NAME_INVEST_STATUS);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_LIST_HTML_DOM, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildRequestIncomeStatusListString(int pageNum, int pageSize) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put("name", ConstantUtil.SERVER_URL_NAME_INCOME_STATUS);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_LIST_HTML_DOM, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public String buildRequestGoodsDetailInfoString(String goodsId) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_GOODS_ID, Utils.GetStringNoNil(goodsId));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_BIAO_DETAIL, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public Object buildRequestDiscoveryHtmlString() {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_DISCOVERY, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public Object buildRequestSaftyHtmlString() {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_SAFTY, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public Object buildRequestNewsPushListString(int pageNum, int pageSize,
//			int type, int lastNewsId) {
//
//		String res = null;
//
//		try {
//
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_LIST_PAGE_NUM, pageNum);
//			jsonBody.put(StringConstant.JSON_LIST_PAGE_SIZE, pageSize);
//			jsonBody.put("type", type);
//
//			jsonBody.put("last_notification_id", lastNewsId);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_NEWS_LIST, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public String buildRequestUserNewestInfoString() {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_USER_INFO, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public String requestSetPayPwd(String newpwd, String oldpwd, String cookie) {
//		String res = null;
//
//		try {
//
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put(StringConstant.JSON_PASSWORD, Utils.GetStringNoNil(newpwd));
//			jsonBody.put(StringConstant.JSON_PASSWORD_OLD, Utils.GetStringNoNil(oldpwd));
//			jsonBody.put("ck", Utils.GetStringNoNil(cookie));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_PAYPWD_SET, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	/*
//	 * 充值支付密码
//	 */
//	public String buildRetrievePwdString() {
//
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_PAYPWD_RESET, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object requestSmsCodeResetPwd(String phonenum, int verifyType) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put(StringConstant.JSON_PHONE_NUMBER, Utils.GetStringNoNil(phonenum));
//
//			jsonBody.put("verifyType", String.valueOf(verifyType));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_SMS_CODE_LOGINPWD_RESET,
//					jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object requestSmsCodeVerifyResetPwd(String phonenum, String smsCode) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put(StringConstant.JSON_PHONE_NUMBER, Utils.GetStringNoNil(phonenum));
//
//			jsonBody.put(StringConstant.JSON_USER_PHONE_MSM_CODE, Utils.GetStringNoNil(smsCode));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(
//					ConstantUtil.SERVER_URL_NAME_SMS_CODE_VERIFY_LOGINPWD_RESET,
//					jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object requestChangeBankPhone(String mobile, String smsCode) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_PHONE_NUMBER, Utils.GetStringNoNil(mobile));
//			jsonBody.put(StringConstant.JSON_USER_PHONE_MSM_CODE_NEW, Utils.GetStringNoNil(smsCode));
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_CHANGE_BANK_MOBILE,
//					jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object requestCheckIdCardResetPwd(String phonenum, String idCard) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_PHONE_NUMBER, Utils.GetStringNoNil(phonenum));
//			jsonBody.put(StringConstant.JSON_USER_ID_CARD, Utils.GetStringNoNil(idCard));
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_CHECK_IDCARD_RESET,
//					jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object requestCheckOldPwdResetPwd(String oldPwd) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put(StringConstant.JSON_PASSWORD_OLD, Utils.GetStringNoNil(oldPwd));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_CHECK_PAY_PWD, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object requestPreOrder(String goodsId) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put(StringConstant.JSON_GOODS_ID, Utils.GetStringNoNil(goodsId));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_PRE_ORDER, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildRequestCheckForUpdate(String platform, String version,
//			int build) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put("platform", Utils.GetStringNoNil(platform));
//			jsonBody.put("version", Utils.GetStringNoNil(version));
//			jsonBody.put("build", build);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_CHECK_FOR_UPDATE, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildActiveFinalDetailString(String activeId, int type) {
//		String res = null;
//
//		try {
//
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put("ac_id", Utils.GetStringNoNil(activeId));
//			jsonBody.put("type", type);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_SHARE_INFO, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildRetrieveLoginPwd(String content, String ck) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			jsonBody.put("mobile", Utils.GetStringNoNil(content));
//            jsonBody.put("ck", Utils.GetStringNoNil(ck));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_LOGINPWD_RESET, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public String buildRrequestBindBankCardVerifyMoney(String funName,
//			String orderId, String money) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_ORDERID_BANK, Utils.GetStringNoNil(orderId));
//			jsonBody.put(StringConstant.JSON_CONSUME_MONEY, Utils.GetStringNoNil(money));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildRrequestBindBankCardGetMoney(String funName,
//			String orderId) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_ORDERID_BANK, Utils.GetStringNoNil(orderId));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public String buildNewStringBindBankCardString(String funName,
//			String bankcard, String bankphone, String bankcardid) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_USER_BANK_CARD, Utils.GetStringNoNil(bankcard));
//			jsonBody.put(StringConstant.JSON_USER_BANK_MOBILE, Utils.GetStringNoNil(bankphone));
//			jsonBody.put(StringConstant.JSON_USER_BANK_ID, Utils.GetStringNoNil(bankcardid));
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public String buildGetImageResourceString(String funName,
//			ResourceModel.Type... resourceCode) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONArray jsonArray = new JSONArray();
//			for(int i=0; i<resourceCode.length; i++) {
//				jsonArray.put(resourceCode[i].name());
//			}
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_RESOURCE_CODE, jsonArray);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public Object buildGetProductImage(String funName) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.GET_PRODUCT_IMAGE_PARA, StringConstant.GET_PRODUCT_IMAGE_INDEX);
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName,jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//
//		return res;
//	}
//
//	@Override
//	public Object buildGetTodayBiddingRecord(String funName, int limit, int offset) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_LIST_PAGE_NUM, offset);
//			jsonBody.put(StringConstant.JSON_LIST_PAGE_SIZE, limit);
//
//			JSONObject funBody = new JSONObject();
//			funBody.put(funName,jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//
//	public Object buildGetOwerListInfo(String funName) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetNovicePacks(String funName) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//
//			JSONObject jsonBody = new JSONObject();
//
//			JSONObject funBody = new JSONObject();
//
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//
//
//	@Override
//	public Object buildGetInvestAndRedeemConfigString(String pId, String type, String bId) {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_CONFIG_PID, Utils.GetStringNoNil(pId));
//			jsonBody.put(StringConstant.JSON_CONFIG_TYPE, Utils.GetStringNoNil(type));
//			jsonBody.put(StringConstant.JSON_INVEST_BID, Utils.GetStringNoNil(bId));
//
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_GET_INVEST_AND_REDEEM_CONFIG, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	public Object buildGetNewInvestAndRedeemConfigString(String funName, String pId, String type, String bId) {
//
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_INVEST_BID, Utils.GetStringNoNil(bId));
//			jsonBody.put(StringConstant.JSON_CONFIG_PID, Utils.GetStringNoNil(pId));
//			jsonBody.put(StringConstant.JSON_CONFIG_TYPE, Utils.GetStringNoNil(type));
//
//			JSONObject funBody = new JSONObject();
//			funBody.put(funName, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetRedeemConfigString(String pId, String type) {
//
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_CONFIG_PID, Utils.GetStringNoNil(pId));
//			jsonBody.put(StringConstant.JSON_CONFIG_TYPE, Utils.GetStringNoNil(type));
//
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_GET_REDEEM_CONFIG, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetWithdrawConfigString() {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_GET_WITHDRAW_CONFIG, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetInvestBalanceString() {
//		String res = null;
//
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_GET_INVEST_BALANCE, jsonBody);
//
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//
//			res = jsonObj.toString();
//
//		} catch (Exception e) {
//			LogUtil.e(e.toString());
//		}
//		return res;
//	}
//
//	/**
//	 * 生成获取消息列表请求字符串
//	 *
//	 */
//	@Override
//	public Object buildGetNewsPushListString(String lastPublishTime) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_PUBLISH_TIME,
//					Utils.GetStringNoNil(lastPublishTime));
//			funBody.put(StringConstant.FUN_GET_MESSAGE_LIST, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	/**
//	 * 生成设置消息已读请求字符串
//	 *
//	 * @param messageId: 消息ID
//	 */
//	@Override
//	public Object buildSetNewsIsReadString(String messageId) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_MESSAGE_ID, Utils.GetStringNoNil(messageId));
//			funBody.put(StringConstant.FUN_SET_MESSAGE_READ, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	/**
//	 * 生成获取消息Tip请求数据字符串
//	 *
//	 */
//	@Override
//	public Object buildGetMessageTipString() {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_GET_MESSAGE_TIP, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	/**
//	 * 生成获取下拉刷新Tip请求数据字符串
//	 *
//	 */
//	@Override
//	public Object buildGetRefreshTipString() {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_GET_REFRESH_TIP, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//
//
//	/**
//	 * 生成获取下拉刷新Tip请求数据字符串
//	 *
//	 */
//	@Override
//	public Object buildGetTodayCreditListString(String funName, String direct, int offsetId) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			jsonBody.put(StringConstant.FUN_TODAY_CREDIT_DIRECT, Utils.GetStringNoNil(direct));
//			jsonBody.put(StringConstant.FUN_TODAY_CREDIT_OFFSETID, offsetId);
//			funBody.put(funName, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetRechargeOrderIdString(String money) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_INVEST_MONEY, money);
//			funBody.put(ConstantUtil.SERVER_URL_NAME_GET_RECHARGE_ORDERID, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	/** 生成银行托管请求数据字符串 */
//	@Override
//	public Object buildOpenBankAccountString(String username, String id_card, String bank_card, String phone_num, String sms_code, String bankCode) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_DEPOSIT_USERNAME, Utils.GetStringNoNil(username));
//			jsonBody.put(StringConstant.JSON_DEPOSIT_ID_CARD, Utils.GetStringNoNil(id_card));
//			jsonBody.put(StringConstant.JSON_DEPOSIT_BANK_CARD, Utils.GetStringNoNil(bank_card));
//
//			jsonBody.put(StringConstant.JSON_DEPOSIT_BANK_CODE, Utils.GetStringNoNil(bankCode));
//
//			jsonBody.put(StringConstant.JSON_USER_PHONE, Utils.GetStringNoNil(phone_num));
//			jsonBody.put(StringConstant.JSON_SMS_CODE, Utils.GetStringNoNil(sms_code));
//
//			funBody.put(ConstantUtil.SERVER_URL_NAME_OPEN_BANK_ACCOUNT, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	/** 生成获取设置交易密码签名接口请求 */
//	@Override
//	public Object buildGetSetPasswordSignString(String funName) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			funBody.put(funName, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetRechargeConfirmString(String orderId, String authSeq) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_ORDER_ID, orderId);
//			jsonBody.put(StringConstant.JSON_RECHARGE_AUTHSEQ, authSeq);
//			funBody.put(ConstantUtil.SERVER_URL_NAME_GET_RECHARGE_CONFIRM, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetRegionString(String funName, String regionId) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_REGION, jsonBody);
//			jsonBody.put(StringConstant.JSON_CASH_REGION_ID, regionId);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetRegionSiteString(String funName, String regionId, String keyword) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_NAME_BANKSITE, jsonBody);
//			jsonBody.put(StringConstant.JSON_CASH_REGION_ID, regionId);
//			jsonBody.put(StringConstant.JSON_CASH_KEYWORD, keyword);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildGetHtmlDocString(String funName, String name) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			funBody.put(ConstantUtil.SERVER_URL_GET_HTML_DOC, jsonBody);
//			jsonBody.put(StringConstant.JSON_GET_HTML_DOC_NAME, name);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//
//	@Override
//	public Object buildSetDotClickString(String funName, String dotType, String dotId) {
//		String res = null;
//		try {
//			JSONObject jsonObj = createJsonObject();
//			JSONObject jsonBody = new JSONObject();
//			JSONObject funBody = new JSONObject();
//			jsonBody.put(StringConstant.JSON_SET_DOT_CLICK_TYPE, dotType);
//			jsonBody.put(StringConstant.JSON_SET_DOT_CLICK_ID, dotId);
//			funBody.put(ConstantUtil.SERVER_URL_SET_DOT_CLICK, jsonBody);
//			jsonObj.put(StringConstant.JSON_STRUCTNAME_FUNCTION, funBody);
//			res = jsonObj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}

//	@Override
//	public String buildRequestActiveDetailInfoString(String activeId) {
//		return null;
//	}
//
//	@Override
//	public String buildRequestNewActivityInfoString() {
//		return null;
//	}
//
//	@Override
//	public String buildRequestNewGoodsInfoString() {
//		return null;
//	}
//
//	@Override
//	public String requestTotalMoneyProfitInfo() {
//		return null;
//	}
//	@Override
//	public Object buildVerifyPayPwd(String pay_pwd) {
//		return null;
//	}
//	@Override
//	public String buildRequestInvestString(String money, String pay_pwd, String goodsId) {
//		return null;
//	}
//
//	@Override
//	public String buildRequestRedeemString(String money, String pay_pwd) {
//		return null;
//	}
//
//	@Override
//	public Object buildRequestRewardListString(int pageNum, int pageSize) {
//		return null;
//	}
}
