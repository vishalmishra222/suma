package top.wuhaojie.installerlibrary;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.SSLCertificateSocketFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

import top.wuhaojie.installerlibrary.utils.Utils;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by wuhaojie on 2016/7/25 22:17.
 */
public class AutoInstaller extends Handler {

    private static final String TAG = "AutoInstaller";
    private static volatile AutoInstaller mAutoInstaller;
    private Context mContext;
    private String mTempPath = Environment.getExternalStorageDirectory().toString();
    private MODE mMode = MODE.BOTH;
    File file;
    private OnStateChangedListener mOnStateChangedListener;

    private AutoInstaller(Context context) {
        mContext = context;
    }

    public static AutoInstaller getDefault(Context context) {
        if (mAutoInstaller == null) {
            synchronized (AutoInstaller.class) {
                if (mAutoInstaller == null) {
                    mAutoInstaller = new AutoInstaller(context);
                }
            }
        }
        return mAutoInstaller;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        mOnStateChangedListener = onStateChangedListener;
    }

    private boolean installUseRoot(String filePath) {
        if (TextUtils.isEmpty(filePath))
            throw new IllegalArgumentException("Please check apk file path!");
        boolean result = false;
        Process process = null;
        OutputStream outputStream = null;
        BufferedReader errorStream = null;
        try {
            process = Runtime.getRuntime().exec("su");
            outputStream = process.getOutputStream();

            String command = "pm install -r " + filePath + "\n";
            outputStream.write(command.getBytes());
            outputStream.flush();
            outputStream.write("exit\n".getBytes());
            outputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder msg = new StringBuilder();
            String line;
            while ((line = errorStream.readLine()) != null) {
                msg.append(line);
            }
            Log.d(TAG, "install msg is " + msg);
            if (!msg.toString().contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                outputStream = null;
                errorStream = null;
                process.destroy();
            }
        }
        return result;
    }

    private void installUseAS(String filePath) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT > M) {
            uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", new File(filePath));
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(intent);
                if (!isAccessibilitySettingsOn(mContext)) {
          //  toAccessibilityService();
            sendEmptyMessage(3);
        }
    }

    private void toAccessibilityService() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        mContext.startActivity(intent);
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + InstallAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }

    public void install(final String filePath) {
        if (TextUtils.isEmpty(filePath) || !filePath.endsWith(".apk"))
            throw new IllegalArgumentException("not a correct apk file path");
        new Thread(new Runnable() {
            @Override
            public void run() {

                sendEmptyMessage(1);

                switch (mMode) {
                    case BOTH:
                        if (!Utils.checkRooted() || !installUseRoot(filePath))
                            installUseAS(filePath);
                        break;
                    case ROOT_ONLY:
                        installUseRoot(filePath);
                        break;
                    case AUTO_ONLY:
                        installUseAS(filePath);
                }
                sendEmptyMessage(0);

            }
        }).start();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0:
                if (mOnStateChangedListener != null)
                    mOnStateChangedListener.onComplete();
                break;
            case 1:
                if (mOnStateChangedListener != null)
                    mOnStateChangedListener.onStart();
                break;

            case 3:
                if (mOnStateChangedListener != null)
                    mOnStateChangedListener.onNeed2OpenService();
                break;

        }
    }

    public void install(File file) {
        if (file == null)
            throw new IllegalArgumentException("file is null");
        //file = new File(mTempPath + File.separator + "update.apk");
        install(mTempPath + File.separator + "update.apk");
    }

    public void installFromUrl(final String httpUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendEmptyMessage(1);
                Activity activity = (Activity) mContext;
                activity.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void run() {
                        new DownloadFilesTask(mContext).execute(httpUrl);
                    }
                });

                //  File file = downLoadFile(httpUrl);
                //  install(file);
            }
        }).start();
    }

/*    public File downLoadFile(String httpUrl) {
        if (TextUtils.isEmpty(httpUrl)) throw new IllegalArgumentException();
        File file = new File(mTempPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(mTempPath + File.separator + "update.apk");
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
                inputStream = null;
                outputStream = null;
            }
        }
        return file;
    }*/


    public enum MODE {
        ROOT_ONLY,
        AUTO_ONLY,
        BOTH
    }

    public interface OnStateChangedListener {
        void onStart();

        void onComplete();

        void onNeed2OpenService();
    }

    public static class Builder {

        private MODE mode = MODE.BOTH;
        private Context context;
        private OnStateChangedListener onStateChangedListener;
        private String directory = Environment.getExternalStorageDirectory().getAbsolutePath();

        public Builder(Context c) {
            context = c;
        }

        public Builder setMode(MODE m) {
            mode = m;
            return this;
        }

        public Builder setOnStateChangedListener(OnStateChangedListener o) {
            onStateChangedListener = o;
            return this;
        }

        public Builder setCacheDirectory(String path) {
            directory = path;
            return this;
        }

        public AutoInstaller build() {
            AutoInstaller autoInstaller = new AutoInstaller(context);
            autoInstaller.mMode = mode;
            autoInstaller.mOnStateChangedListener = onStateChangedListener;
            autoInstaller.mTempPath = directory;
            return autoInstaller;
        }

    }

    class DownloadFilesTask extends AsyncTask<String, Integer, File> {

        // declare the dialog as a member field of your activity
        ProgressDialog mProgressDialog;
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        DownloadFilesTask(Context context1) {
            this.context = context1;
            // instantiate it within the onCreate method
            mProgressDialog = new ProgressDialog(context);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                mProgressDialog.setMessage("File is downloading");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                // take CPU lock to prevent CPU from going off if the user
                // presses the power button during download
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        getClass().getName());
                mWakeLock.acquire();
                mProgressDialog.show();
            } catch (Exception e) {

            }
        }

        protected File doInBackground(String... param) {
            if (TextUtils.isEmpty(param[0])) throw new IllegalArgumentException();
            file = new File(mTempPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(mTempPath + File.separator + "update.apk");

            if (file.exists()) {
                file.delete();
            }
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            HttpURLConnection connection = null;
            HttpsURLConnection httpsURLConnection = null;
            try {
                URL url = new URL(param[0]);
                if (param[0].contains("http://")) {
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(10 * 1000);
                        connection.setReadTimeout(10 * 1000);
                        connection.connect();
                        int fileLength = connection.getContentLength();
                        inputStream = connection.getInputStream();
                        outputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[4084];
                        // int len, i = 0;
                        long total = 0;
                        int count;
                        while ((count = inputStream.read(buffer)) != -1) {
                            if (isCancelled()) {
                                inputStream.close();
                                return null;
                            }
                            total += count;
                            // publishing the progress....
                            if (fileLength > 0) // only if total length is known
                                publishProgress((int) (total * 100 / fileLength));
                            outputStream.write(buffer, 0, count);
                            // outputStream.write(buffer, 0, len);
                            //publishProgress((int) ((i / (float) len) * 100));
                            //i++;
                        }
                       // outputStream.close();
                    } catch (Exception e) {
                        e.toString();
                    } finally {
                        try {
                            if (inputStream != null)
                                inputStream.close();
                            if (outputStream != null)
                                outputStream.close();
                            if (connection != null)
                                connection.disconnect();
                        } catch (IOException e) {
                            inputStream = null;
                            outputStream = null;
                        }
                    }
                } else {
                    try {
                        httpsURLConnection = (HttpsURLConnection) url.openConnection();
                        httpsURLConnection.setConnectTimeout(10 * 1000);
                        httpsURLConnection.setReadTimeout(10 * 1000);
                        httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                        //httpsURLConnection.setSSLSocketFactory(getSslSocketFactory(mContext));
                        httpsURLConnection.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                        httpsURLConnection.connect();
                        int fileLength = connection.getContentLength();
                        inputStream = httpsURLConnection.getInputStream();
                        outputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[4084];
                        // int len, i = 0;
                        long total = 0;
                        int count;
                        while ((count = inputStream.read(buffer)) != -1) {
                            if (isCancelled()) {
                                inputStream.close();
                                return null;
                            }
                            total += count;
                            // publishing the progress....
                            if (fileLength > 0) // only if total length is known
                                publishProgress((int) (total * 100 / fileLength));
                            outputStream.write(buffer, 0, count);
                            // outputStream.write(buffer, 0, len);
                            //publishProgress((int) ((i / (float) len) * 100));
                            //i++;
                        }
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        e.toString();
                    } finally {
                        try {
                            if (inputStream != null)
                                inputStream.close();
                            if (outputStream != null)
                                outputStream.close();
                            if (httpsURLConnection != null)
                                httpsURLConnection.disconnect();
                        } catch (IOException e) {
                            inputStream = null;
                            outputStream = null;
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;
        }


        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);

            if (progress[0] == 100)
                mProgressDialog.setMessage("Installing new version ... Please Wait.");
        }

        protected void onPostExecute(File result) {
            try {
                mWakeLock.release();
                mProgressDialog.dismiss();
                // showDialog("Downloaded " + result + " bytes");
                if (result.length() != 0) {
                    install(result);
                }
            } catch (Exception e) {

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
