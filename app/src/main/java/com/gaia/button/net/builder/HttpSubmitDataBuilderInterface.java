package com.gaia.button.net.builder;

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


}