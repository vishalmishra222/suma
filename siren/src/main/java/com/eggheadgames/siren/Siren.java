package com.eggheadgames.siren;

import android.app.Activity;
import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * JSON format should be the following
 * {
 * "com.example.app": {
 * "minVersionName": "1.0.0.0"
 * }
 * }
 * <p>
 * OR
 * <p>
 * {
 * "com.example.app": {
 * "minVersionCode": 7,
 * }
 * }
 */

@SuppressWarnings({"WeakerAccess", "unused", "PMD.GodClass"})
public class Siren {
    @VisibleForTesting
    protected static final Siren sirenInstance = new Siren();
    @VisibleForTesting
    protected Context mApplicationContext;
    private ISirenListener mSirenListener;
    private WeakReference<Activity> mActivityRef;
    public static final String PREF_NAME = "UserPrefs";
    public static final int MODE = Context.MODE_PRIVATE;
    /**
     * Determines alert type during version code verification
     */
    private SirenAlertType versionCodeUpdateAlertType = SirenAlertType.OPTION;

    /**
     * Determines the type of alert that should be shown for major version updates: A.b.c
     */
    private SirenAlertType majorUpdateAlertType = SirenAlertType.OPTION;

    /**
     * Determines the type of alert that should be shown for minor version updates: a.B.c
     */
    private SirenAlertType minorUpdateAlertType = SirenAlertType.OPTION;

    /**
     * Determines the type of alert that should be shown for minor patch updates: a.b.C
     */
    private SirenAlertType patchUpdateAlertType = SirenAlertType.OPTION;

    /**
     * Determines the type of alert that should be shown for revision updates: a.b.c.D
     */
    private SirenAlertType revisionUpdateAlertType = SirenAlertType.OPTION;

    /**
     * Overrides the default localization of a user's device when presenting the update message and button titles in the alert.
     */
    private SirenSupportedLocales forceLanguageLocalization = null;

    @VisibleForTesting
    protected Siren() {
        // visible for testing
    }

    /**
     * @param context - you should use an Application mApplicationContext here in order to not cause memory leaks
     */
    public static Siren getInstance(Context context) {
        sirenInstance.mApplicationContext = context;
        return sirenInstance;
    }

    public void checkVersion(Activity activity, SirenVersionCheckType versionCheckType, String appDescriptionUrl) {

        mActivityRef = new WeakReference<>(activity);

        if (getSirenHelper().isEmpty(appDescriptionUrl)) {
            getSirenHelper().logError(getClass().getSimpleName(), "Please make sure you set correct path to app version description document");
            return;
        }

        if (versionCheckType == SirenVersionCheckType.IMMEDIATELY) {
            performVersionCheck(appDescriptionUrl);
        } else if (versionCheckType.getValue() <= getSirenHelper().getDaysSinceLastCheck(mApplicationContext)
                || getSirenHelper().getLastVerificationDate(mApplicationContext) == 0) {
            performVersionCheck(appDescriptionUrl);
        }

    }

    public void setMajorUpdateAlertType(@SuppressWarnings("SameParameterValue") SirenAlertType majorUpdateAlertType) {
        this.majorUpdateAlertType = majorUpdateAlertType;
    }

    public void setMinorUpdateAlertType(SirenAlertType minorUpdateAlertType) {
        this.minorUpdateAlertType = minorUpdateAlertType;
    }

    public void setPatchUpdateAlertType(SirenAlertType patchUpdateAlertType) {
        this.patchUpdateAlertType = patchUpdateAlertType;
    }

    public void setRevisionUpdateAlertType(SirenAlertType revisionUpdateAlertType) {
        this.revisionUpdateAlertType = revisionUpdateAlertType;
    }

    public void setSirenListener(ISirenListener sirenListener) {
        this.mSirenListener = sirenListener;
    }

    public void setVersionCodeUpdateAlertType(SirenAlertType versionCodeUpdateAlertType) {
        this.versionCodeUpdateAlertType = versionCodeUpdateAlertType;
    }

    public void setLanguageLocalization(SirenSupportedLocales localization) {
        forceLanguageLocalization = localization;
    }

    @VisibleForTesting
    protected void performVersionCheck(String appDescriptionUrl) {
        new LoadJsonTask(mApplicationContext).execute(appDescriptionUrl);
    }

    @VisibleForTesting
    protected void handleVerificationResults(String version) {
        try {
           JSONObject rootJson = new JSONObject(version);

            if (rootJson.isNull(getSirenHelper().getPackageName(mApplicationContext))) {
                throw new JSONException("field not found");
            } else {
                JSONObject appJson = rootJson.getJSONObject(getSirenHelper().getPackageName(mApplicationContext));

                //version name have higher priority then version code
                if (checkVersionName(appJson)) {
                    mApplicationContext.getSharedPreferences(PREF_NAME, MODE).edit().clear().commit();
                    return;
                }

                if(checkVersionCode(appJson)){
                    mApplicationContext.getSharedPreferences(PREF_NAME, MODE).edit().clear().commit();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            if (mSirenListener != null) {
                mSirenListener.onError(e);
            }
        }
    }

    @VisibleForTesting
    protected SirenAlertWrapper getAlertWrapper(SirenAlertType alertType, String appVersion) {
        Activity activity = mActivityRef.get();
        return new SirenAlertWrapper(activity, mSirenListener, alertType, appVersion, forceLanguageLocalization, getSirenHelper());
    }

    protected SirenHelper getSirenHelper() {
        return SirenHelper.getInstance();
    }

    private boolean checkVersionName(JSONObject appJson) throws JSONException {
        if (appJson.isNull(Constants.JSON_MIN_VERSION_NAME)) {
            return false;
        }
        getSirenHelper().setLastVerificationDate(mApplicationContext);

        String minVersionName = appJson.getString(Constants.JSON_MIN_VERSION_NAME);

        String currentVersionName = getSirenHelper().getVersionName(mApplicationContext);

        if (getSirenHelper().isEmpty(minVersionName) || getSirenHelper().isEmpty(currentVersionName) || getSirenHelper().isVersionSkippedByUser(mApplicationContext, minVersionName)) {
            return false;
        }
        SirenAlertType alertType = null;
        String[] minVersionNumbers = minVersionName.split("\\.");
        String[] currentVersionNumbers = currentVersionName.split("\\.");
        //noinspection ConstantConditions
        if (minVersionNumbers != null && currentVersionNumbers != null
                && minVersionNumbers.length == currentVersionNumbers.length) {
            int digitVerificationCode = checkVersionDigit(minVersionNumbers, currentVersionNumbers, 0);
            if (digitVerificationCode == 0) {
                digitVerificationCode = checkVersionDigit(minVersionNumbers, currentVersionNumbers, 1);
                if (digitVerificationCode == 0) {
                    digitVerificationCode = checkVersionDigit(minVersionNumbers, currentVersionNumbers, 2);
                    if (digitVerificationCode == 0) {
                        if (checkVersionDigit(minVersionNumbers, currentVersionNumbers, 3) == 1) {
                            alertType = revisionUpdateAlertType;
                        }
                    } else if (digitVerificationCode == 1) {
                        alertType = patchUpdateAlertType;
                    }
                } else if (digitVerificationCode == 1) {
                    alertType = minorUpdateAlertType;
                }
            } else if (digitVerificationCode == 1) {
                alertType = majorUpdateAlertType;
            }

            if (alertType != null) {

                showAlert(minVersionName, alertType);
                return true;
            }
        }
        return false;
    }

    private int checkVersionDigit(String[] minVersionNumbers, String[] currentVersionNumbers, int digitIndex) {
        if (minVersionNumbers.length > digitIndex) {
            if (getSirenHelper().isGreater(minVersionNumbers[digitIndex], currentVersionNumbers[digitIndex])) {
                return 1;
            } else if (getSirenHelper().isEquals(minVersionNumbers[digitIndex], currentVersionNumbers[digitIndex])) {
                return 0;
            }
        }
        return -1;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean checkVersionCode(JSONObject appJson) throws JSONException {
        if (!appJson.isNull(Constants.JSON_MIN_VERSION_CODE)) {
            int minAppVersionCode = appJson.getInt(Constants.JSON_MIN_VERSION_CODE);

            //save last successful verification date
            getSirenHelper().setLastVerificationDate(mApplicationContext);

            if (getSirenHelper().getVersionCode(mApplicationContext) < minAppVersionCode
                    && !getSirenHelper().isVersionSkippedByUser(mApplicationContext, String.valueOf(minAppVersionCode))) {
                showAlert(String.valueOf(minAppVersionCode), versionCodeUpdateAlertType);
                return true;
            }
        }
        return false;
    }

    private void showAlert(String appVersion, SirenAlertType alertType) {
        if (alertType == SirenAlertType.NONE) {
            if (mSirenListener != null) {
                mSirenListener.onDetectNewVersionWithoutAlert(getSirenHelper().getAlertMessage(mApplicationContext, appVersion, forceLanguageLocalization));
            }
        } else {
            getAlertWrapper(alertType, appVersion).show();
        }
    }

    private static class LoadJsonTask extends AsyncTask<String, Void, String> {
        private Context mContext;
        public LoadJsonTask(Context mApplicationContext) {
            mContext = mApplicationContext;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            HttpsURLConnection httpsURLConnection = null;
            int status =0;
            try {
                URL url = new URL(params[0]);
                if(params[0].contains("http://")) {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
//                connection.setConnectTimeout(120000);
//                connection.setReadTimeout(120000);
                    connection.connect();
                     status = connection.getResponseCode();
                }
                else
                {

                    httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("GET");
                    httpsURLConnection.setUseCaches(false);
                    httpsURLConnection.setAllowUserInteraction(false);
                    httpsURLConnection.setConnectTimeout(10000);
                    httpsURLConnection.setReadTimeout(10000);
                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                    //httpsURLConnection.setSSLSocketFactory(getSslSocketFactory(mContext));
                    httpsURLConnection.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
//                connection.setConnectTimeout(120000);
//                connection.setReadTimeout(120000);
                     httpsURLConnection.connect();
                    status = httpsURLConnection.getResponseCode();
                }
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = null;
                        if(params[0].contains("http://")) {
                            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                        }
                        else
                        {
                            br = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), "UTF-8"));
                        }
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (isCancelled()) {
                                br.close();
                                if(params[0].contains("http://")) {
                                    connection.disconnect();
                                }
                                else
                                {
                                    httpsURLConnection.disconnect();
                                }
                                return null;
                            }
                            sb.append(line).append('\n');
                        }
                        br.close();
                        return sb.toString();
                    default: /* ignore unsuccessful results */
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                if (Siren.sirenInstance.mSirenListener != null) {
                    Siren.sirenInstance.mSirenListener.onError(ex);
                }

            } finally {
                    if (connection != null || httpsURLConnection!=null) {
                        try {
                            if(params[0].contains("http://")) {
                                connection.disconnect();
                            }
                            else {
                                httpsURLConnection.disconnect();
                            }
                        } catch (Exception ex) {
                        ex.printStackTrace();
                        if (Siren.sirenInstance.mSirenListener != null) {
                            Siren.sirenInstance.mSirenListener.onError(ex);
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (Siren.sirenInstance.getSirenHelper().isEmpty(result)) {
                    if (Siren.sirenInstance.mSirenListener != null) {
                        Siren.sirenInstance.mSirenListener.onError(new NullPointerException());
                    }
                } else {
                    if (result.contains("\n")) {
                        result = result.replace("\n", "");
                    }
                    Siren.sirenInstance.handleVerificationResults(result);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
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

}
