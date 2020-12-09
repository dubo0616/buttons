package com.jindan.p2p.net;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class HttpClientHelper {
    private static HttpClient httpClient;
    private static HttpParams params = null;

    private HttpClientHelper() {

    }

    // --------------------------Http相关-----------------------
    public static synchronized HttpClient getHttpClient() {
        if (null == httpClient) {
            params = new BasicHttpParams();

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params,
                    HTTP.UTF_8);
            HttpProtocolParams.setUseExpectContinue(params, true);

            // 设置连接管理器的超时
            ConnManagerParams.setTimeout(params, 300);
            // 设置连接超时
            HttpConnectionParams.setConnectionTimeout(params, 300);
            // 设置socket超时
            HttpConnectionParams.setSoTimeout(params, 300);

            // 设置http https支持
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLTrustAllSocketFactory.getSocketFactory(), 443));

            ClientConnectionManager conManager = new ThreadSafeClientConnManager(
                    params, schReg);

            httpClient = new DefaultHttpClient(conManager, params);

        }
        return httpClient;
    }

    public static HttpGet getHttpGetRequest(String uri) {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.getParams().setBooleanParameter(
                CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
        return httpGet;
    }

    public static HttpPost getHttpPostRequest(String uri, boolean newServer) {

        HttpPost httpPost = new HttpPost(uri);

        httpPost.setHeader("Accept", "*/*");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        if (newServer == true) {
            httpPost.addHeader("Accept-Encoding", "gzip");
        } else {
            httpPost.addHeader("Accept-Encoding", "\"\"");
        }

//        String token = "";
//    	UserInfo user  = UserManager.getRequestHandler().getCurrentUserInfo();
//    	if (user != null) {
//    		token = user.getToken();
//    	}
//    	httpPost.setHeader("Cookie", "token=" + token);

//        httpPost.addHeader("encrypttype", "1");
//        httpPost.getParams().setBooleanParameter(
//                CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
        return httpPost;

    }


    // --------------------------线程相关-----------------------
    private static ThreadPoolExecutor threadPoolExecutor;
    private static int CORE_POOL_SIZE = 3;
    private static int MAX_POOL_SIZE = 128;
    private static int KEEP_ALIVE_TIME = 1;
    private static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(12);

    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        public Thread newThread(Runnable r) {
            // TODO Auto-generated method stub
            return new Thread(r, "thread  no" + integer.getAndIncrement());
        }
    };

    static {
        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue,
                threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

    }

    public static void execute(Runnable task) {

        threadPoolExecutor.execute(task);
    }

    public static void shutdownHttpConnection() {
        if (httpClient != null) {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public static class SSLTrustAllSocketFactory extends SSLSocketFactory {

        private static final String TAG = "SSLTrustAllSocketFactor";
        private SSLContext mCtx;

        public class SSLTrustAllManager implements X509TrustManager {

            private X509TrustManager defaultTrustManager;
            private X509TrustManager localTrustManager;

            public SSLTrustAllManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
                TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                var4.init((KeyStore) null);
                defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
                this.localTrustManager = localTrustManager;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                try {
                    defaultTrustManager.checkServerTrusted(chain, authType);
                } catch (CertificateException ce) {
                    if (localTrustManager != null) {
                        localTrustManager.checkServerTrusted(chain, authType);
                    }
                }
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                try {
                    defaultTrustManager.checkServerTrusted(chain, authType);
                } catch (CertificateException ce) {
                    if (localTrustManager != null) {
                        localTrustManager.checkServerTrusted(chain, authType);
                    }
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

        }

        private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }

        public SSLTrustAllSocketFactory(KeyStore truststore) throws Throwable {
            super(truststore);
            try {
//                 sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
//                 mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

                mCtx = SSLContext.getInstance("TLS");
                mCtx.init(new KeyManager[]{}, new TrustManager[]{new SSLTrustAllManager(null)},
                        new SecureRandom());
//                setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException {
            return mCtx.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return mCtx.getSocketFactory().createSocket();
        }

        public static SSLSocketFactory getSocketFactory() {
            try {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);

                SSLSocketFactory factory = new SSLTrustAllSocketFactory(keyStore);
                return factory;
            } catch (Throwable e) {
                Log.d(TAG, e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

    }
}
