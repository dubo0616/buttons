package com.gaia.button.utils;


public final class DcError
{

    public static final int DC_Error = -1; // 错误
    public static final int DC_OK = 0; // 正常
    public static final int DC_OK_200 = 200; // 正常

    /**
     * 服务器端网络错误
     */
    public static final int DC_SERVER_NET_ERROR = 500;

    /** 老系统账户进入新版标的详情，标的信息不存在 */
    public static final int DC_OID_GOODS_FOR_EXCLUSIVE_ERROR = 206;

    // 添加两个错误码
    public static final int DC_JSON_PARSER_ERROR = 80000; // json数据解析异常

    /** 登录状态失效(重新登录) */
    public static final int DC_LOGOUT_ERROR = 1000;
    /** 资产异常 */
    public static final int DC_ABNORMAL_ERROR = 1001;
    /** 用户未注册 Alert弹窗 */
    public static final int DC_NOT_REGISTER = 1003;
    /** 用户首次投资 */
    public static final int DC_FIRST_INVEST = 1004;
    /** 统一挡板弹窗 Alert弹窗 */
    public static final int DC_BAFFLE_ERROR = 1005;
    /** 未设置登录密码 */
    public static final int DC_NO_LOGIN_PWD = 1007;
    /** 有效时间内重复发验证码 当做正常情况，无Toast提示，直接倒计时 */
    public static final int DC_SEND_REPEATEDLY_ERROR = 1010;

    /** 提招牌金蛋账户时 8降7 废弃 */
    @Deprecated
    public static final int DC_REDEEM_CHARGE_ACCOUNT = 1500;
    /** 提现即收手续费又提招牌金蛋 8降7 废弃 */
    @Deprecated
    public static final int DC_REDEEM_SPECIALTY_AND_CHARGE = 1501;
    /** 提现收取手续费时 8降7 废弃 */
    @Deprecated
    public static final int DC_REDEEM_CHARGE = 1502;
    /** 超过本月最大投资次数 */
    public static final int DC_INVEST_COUNT_MAX = 2002;



    /** 用户未绑卡 */
    public static final int DC_NO_BIND_CARD = 4003;
    /** 银行预留手机号不符 */
    public static final int DC_BANK_RESERVE_PHONE = 4004;
    /** 绑卡验证页面 */
    public static final int DC_CHECK_BANK_STATUS = 4006;
    /** 更改手机号错误 */
    public static final int DC_CHANGE_BANK_MOBILE_ERROR = 4007;
    /** 需要设置自动投标 旧版本 */
    public static final int DC_NO_SET_AUTO_INVEST = 4008;
    /** 大额提现受限 */
    public static final int DC_LARGE_WITHDRAW_LIMIT_ERROR = 4009;

    /** 未设置加息金蛋提现时间 */
    public static final int DC_NO_INCREASE_TIME = 4010;
    /** 投资时余额不足 */
    public static final int DC_INVEST_NO_BALANCE = 4011;
    /** 标的不存在 */
    public static final int DC_INVEST_PID_NOT_EXIST = 4012;
    /** 需要设置自动投标 新版本 */
    public static final int DC_NO_SET_AUTO_INVEST_NEW = 4013;
    /** 大额充值受限 */
    public static final int DC_LARGE_CHARGE_LIMIT_ERROR = 4101;
    /** 账户被锁定 Alert */
    public static final int DC_ACCOUNT_LOCK = 6001;
    /** 未开通银联支付 */
    public static final int DC_NO_OPEN_UNION_PAY = 7005;


    /** 服务器特殊挡板弹窗(服务器内部网络问题) */
    public static final int DC_NET_ERROR_DIALOG = 9999;
    // 网络错误码定义
    public static final int DC_NET_TIME_OUT = 90000; // 网络超时
    public static final int DC_NET_DATA_ERROR = 90001; // 网络传输数据错误
    public static final int DC_NET_GENER_ERROR = 90002; // 网络通用错误标识
    public static final int DC_NET_NEED_BUY = 90003; // 网络超时
    
    public static final int DC_NET_SSL_ERROR = 90004; // SSL验证错误
    public static final int DC_NET_NONE = 90005; // 本地网络未连接
}