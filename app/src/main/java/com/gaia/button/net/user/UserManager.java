package com.gaia.button.net.user;

import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AccountInfo;

/**
 * 请求用户数据
 *
 * @author Administrator
 */
public final class UserManager {
    private static final Object mInstanceSync = new Object();
    private static UserManager mInstance;
    private static UserManager _getInstance() {
        synchronized (mInstanceSync) {
            if (mInstance == null) {
                mInstance = new UserManager();
            }
        }
        return mInstance;
    }

    private UserImpl mUserImpl = null;
    private AccountInfo mUserData = null;

    private UserManager() {
        mUserImpl = new UserImpl();
        mUserData = PreferenceManager.getInstance().getAccountInfo();
    }

    /**
     * 网络数据请求入口
     * @return
     */
    public static IUserInterface getRequestHandler() {
        return _getInstance()._getUserRequestHandler();
    }

    /**
     * 用户数据provider
     * 单个数据请求
     * @return
     */
    public static AccountInfo getUserDataHandler() {
        return _getInstance()._getUserDataHandler();
    }

    private IUserInterface _getUserRequestHandler() {
        return mUserImpl.getUserRequestProxy();
    }

    private AccountInfo _getUserDataHandler() {
        return mUserData;
    }

}