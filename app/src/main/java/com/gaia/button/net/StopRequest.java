package com.jindan.p2p.net;

public class StopRequest extends Throwable {

    private static final long serialVersionUID = -947318333299377787L;
    /**
     * final status.
     */
    public int mFinalStatus;

    /**
     * stop he current request.
     *
     * @param finalStatus finalStatus
     * @param message     message
     */
    public StopRequest(int finalStatus, String message) {
        super(message);
        mFinalStatus = finalStatus;
    }

    /**
     * stop the current request.
     *
     * @param finalStatus finalStatus
     * @param message     message
     * @param throwable   throwable
     */
    public StopRequest(int finalStatus, String message, Throwable throwable) {
        super(message, throwable);
        mFinalStatus = finalStatus;
    }
}
