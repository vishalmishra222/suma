package com.app.dusmile.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.app.dusmile.R;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.receiver.CheckConnectivityReceiver;
import com.app.dusmile.unhandleException.TopExceptionHandler;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import io.fabric.sdk.android.Fabric;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by suma on 23/01/17.
 */

public class DusmileApplication extends Application {
   public static Context context;
    private static DusmileApplication mInstance;
    private Activity activity;
    private RequestQueue mRequestQueue;
    private static FirebaseAnalytics mFirebaseAnalytics;
    Request.Priority mPriority;
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Fabric.with(this, new Crashlytics());
            DusmileApplication.context = getApplicationContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                registerReceiver(new CheckConnectivityReceiver(),
                        new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler());
            //nuke();

            mInstance = this;
            FirebaseApp.initializeApp(getApplicationContext());
            this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

           // FirebaseInstanceId.getInstance().getToken();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Context getAppContext()
    {
        return DusmileApplication.context;
    }

    public static synchronized DusmileApplication getInstance() {
        if (mInstance == null) {
            mInstance = new DusmileApplication();
        }

        return mInstance;
    }

    public static FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }
        public static String getFirebaseToken(){

        String token = FirebaseInstanceId.getInstance().getToken();
        return token;
        }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            if(new Const().BASE_URL.startsWith("https:")) {
                mRequestQueue = Volley.newRequestQueue(context, hurlStack);
            }
            else {
               mRequestQueue = Volley.newRequestQueue(context);
            }
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);

    }



    public static SSLSocketFactory getSslSocketFactory(Context context) {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
           /* KeyStore trusted = KeyStore.getInstance("PKCS12");
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = context.getApplicationContext().getResources().openRawResource(R.raw.client);*/

                // Initialize the keystore with the provided trusted certificates
                // Provide the password of the keystore
                // trusted.load(in, "sumasoft123".toCharArray());
                SSLContext sslContext = SSLContext.getInstance("TLS");
               InputStream trustStoreStream = context.getResources().openRawResource(R.raw.server);
              /* KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                InputStream trustStoreStream = this.getResources().openRawResource(R.raw.server1);
                 trustStore.load(trustStoreStream, "changeit".toCharArray());*/

               CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509Certificate caCert = (X509Certificate) cf.generateCertificate(trustStoreStream);

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                ks.load(null); // You don't need the KeyStore instance to come from a file.
                ks.setCertificateEntry("sumasoft123", caCert);
                trustManagerFactory.init(ks);

                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                InputStream keyStoreStream = context.getResources().openRawResource(R.raw.myp12file);
                keyStore.load(keyStoreStream, "sumasoft123".toCharArray());
                keyManagerFactory.init(keyStore, "sumasoft123".toCharArray());

                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
                SSLSocketFactory sf = sslContext.getSocketFactory();
                return sf;
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }


    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkClientTrusted", e.toString());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }

    public static HurlStack hurlStack = new HurlStack() {
        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
            try {
                httpsURLConnection.setSSLSocketFactory(getSslSocketFactory(context));
                httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                System.setProperty("http.keepAlive", "false");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpsURLConnection;
        }
    };

    public static HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
                //HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
               // return hv.verify("localhost", session);
                return true;
            }
        };
    }


    public static void nuke() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }
}
