/**
 * @author huzexin@duoku.com
 * @version CreateData�?012-5-10 3:46:54 PM
 */
package com.gaia.button.net;


public class NetManager {
    private static final Object mInstanceSync = new Object();
    private static NetManager mNetManagerInstance = null;

    private HttpImpl mHttpImpl = null;

    /**
     * Below API is Public API
     * @return
     * 	void
     */
    public static IHttpInterface getHttpConnect() {
        NetManager _manager = _getNetManager();
        return _manager.mHttpImpl;
    }

    /**
     * Below API is Private API
     * @return
     * 	void
     */
    private static NetManager _getNetManager() {

        synchronized (mInstanceSync) {

            if (mNetManagerInstance == null) {
                mNetManagerInstance = new NetManager();
            }
        }
        return mNetManagerInstance;
    }

    /**
     * Contruct
     */
    private NetManager() {
        mHttpImpl = new HttpImpl();
    }
}
