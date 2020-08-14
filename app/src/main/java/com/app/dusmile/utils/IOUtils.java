/**
 *
 * @author vCleen
 *
 */

package com.app.dusmile.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dusmile.R;
import com.app.dusmile.activity.DusmileBaseActivity;
import com.app.dusmile.activity.SplashActivity;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.constant.MarshMallowPermission;
import com.app.dusmile.preferences.UserPreference;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class IOUtils {
	static Camera camera;
	public static final String DATE_TIME_FORMATE = "dd/MM/yyyy HH:mm";
	private static final String TAG = "DSS";
	private static ProgressDialog proDialog = null;
	private static ProgressDialog statusDialog = null;
	static Dialog dialogGoogleLoading = null;
	private static TextView tv_msg;
	private static LinearLayout parent_layout;
	private static View toastRoot;
	public static String Tag = "IOUtils";
	static MarshMallowPermission marshMallowPermission;

	public static int generateRandomNumber(int length) {
		Random r = new Random();
		String number = "";
		int counter = 0;
		while (counter++ < length)
			number += r.nextInt(9);

		return Integer.parseInt(number);

	}


	public static int getHieghtOfScreen(Activity mContext) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		(mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		// int densi=(int) mContext.getResources().getDisplayMetrics().density;
		// System.out.println(densi+"--------densi");
		// return width*densi;
		return width;

	}

	public static void printLogError(String msg) {
		try {
			Log.e("Dusmile", msg);
		} catch (Exception e) {

		}
	}

	public static void printLogDebug(String msg) {
		try {
			Log.d("Dusmile", msg);
		} catch (Exception e) {

		}
	}


	public static void printLogInfo(String msg) {
		try {
			Log.i("Dusmile", msg);
		} catch (Exception e) {

		}
	}

	public static void startLoadingPleaseWait(Context mContext) {
		try {

			if (mContext != null) {
				if (proDialog == null)
					proDialog = ProgressDialog.show(mContext, null, "Please wait...");
				proDialog.setCancelable(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startLoading(Context context, String message) {
		try {
			if (context != null) {
				if (proDialog == null)
					proDialog = ProgressDialog.show(context, null, message);
				proDialog.setCancelable(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startUpdateStatusLoading(Context context, String message) {
		try {
			if (context != null) {
				if (statusDialog == null)
					statusDialog = ProgressDialog.show(context, null, message);
				statusDialog.setCancelable(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stopLoading() {
		try {
			if (proDialog != null)
				proDialog.dismiss();
			proDialog = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stopUpdateStatusLoading() {
		try {
			if (statusDialog != null)
				statusDialog.dismiss();
			statusDialog = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the device has Internet connection.
	 *
	 * @return <code>true</code> if the phone is connected to the Internet.
	 */
	public static boolean isInternetPresent(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(
				Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}


	public static void hideKeyBoard(Context context, EditText myEditText) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
	}

	public static void hideKeyBoard(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
	}

	public static boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}


	public static void myToast(String msg, Context context) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void myToastLong(String msg, Context context) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (; ; ) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static void saveLog() {
		try {

			Calendar c = Calendar.getInstance();
			System.out.println("Current time => " + c.getTime());

			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			String formattedDate = df.format(c.getTime());

			String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
			File dir = new File(SD_CARD_PATH + "/" + AppConstant.ROOT_DIRECTORY + "/Logs");
			File file = new File(dir, getCurrentDay() + "(" + formattedDate + ").txt");

			Process process = Runtime.getRuntime().exec("logcat -d" + file.getAbsolutePath());
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			StringBuilder log = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				log.append(line);
			}

		} catch (IOException e) {
		}
	}


	@SuppressLint("MissingPermission")
	public static void check_file(Context context) {
		Calendar c = Calendar.getInstance();
		// System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());

		String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
		File file = new File(SD_CARD_PATH + "/" + AppConstant.ROOT_DIRECTORY + "/Logs/" + get_Current_day() + "(" + formattedDate + ").txt");

		if (file.exists()) {

			if (file.exists()) {
				//file is older than a week
				//file.delete();
				deleteOldFiles();
				//	System.out.println("File Already Exists !!");
			}


		} else {
			try {
				deleteOldFiles();
				//System.out.println("File Created");

				File Folder = new File(SD_CARD_PATH + "/" + AppConstant.ROOT_DIRECTORY);
				if (Folder.exists()) {
					//System.out.println("NBFC folder allready exists");
				} else {
					Folder.mkdir();
				}

				File Folder1 = new File(SD_CARD_PATH + "/" + AppConstant.ROOT_DIRECTORY + "/Logs/");
				if (Folder1.exists()) {
					//System.out.println("HERO/Logs folder already exists");
				} else {
					Folder1.mkdir();
				}

				file.createNewFile();

				marshMallowPermission = new MarshMallowPermission((Activity) context);

				if (!marshMallowPermission.checkPermissionForState()) {
					marshMallowPermission.requestPermissionForReadPhoneState();
				} else {
					FileOutputStream fOut = null;
					try {
						fOut = new FileOutputStream(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

					String serviceName = Context.TELEPHONY_SERVICE;
					TelephonyManager m_telephonyManager = (TelephonyManager) context.getSystemService(serviceName);
					String IMEI;
					IMEI = m_telephonyManager.getDeviceId();

					int Megapixel = 0;
					try {
						Megapixel = get_Camera_MegaPixel();
					} catch (Exception e) {
						e.printStackTrace();
					}


					StringBuilder content = new StringBuilder();
					content.append("\n");
					content.append("*****************************************************************");
					content.append("\n");
					content.append("\n");
					content.append("\n");
					content.append("App Build Version	:  v " + AppConstant.VERSION + " - " + AppConstant.BUILD);
					content.append("\n");
					content.append("\n");
					content.append("API LEVEL 			: " + Build.VERSION.SDK);
					content.append("\n");
					content.append("\n");
					content.append("MANUFACTURER 		: " + Build.MANUFACTURER);
					content.append("\n");
					content.append("\n");
					content.append("Mobile Model No. 	: " + Build.MODEL);
					content.append("\n");
					content.append("\n");
					content.append("Android OS Version	: " + AppConstant.AndroidVersion);
					content.append("\n");
					content.append("\n");
					content.append("IMEI NO.			: " + IMEI);
					content.append("\n");
					content.append("\n");
					content.append("Camera in Megapixel	: " + String.valueOf(Megapixel + 1) + " Megapixel");
					content.append("\n");
					content.append("\n");
					content.append("*****************************************************************");
					content.append("\n");

					try {
						myOutWriter.append(content);
						myOutWriter.close();
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("File Written");
				}

				//System.out.println("File Data = " + readLog(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}

	public static int get_Camera_MegaPixel() {
		int Megapixel = 0;
		releaseCameraAndPreview();
		camera = Camera.open(0);    // For Back Camera
		android.hardware.Camera.Parameters params = camera.getParameters();
		List sizes = params.getSupportedPictureSizes();
		Camera.Size result = null;

		ArrayList<Integer> arrayListForWidth = new ArrayList<Integer>();
		ArrayList<Integer> arrayListForHeight = new ArrayList<Integer>();

		for (int i = 0; i < sizes.size(); i++) {
			result = (Camera.Size) sizes.get(i);
			arrayListForWidth.add(result.width);
			arrayListForHeight.add(result.height);
			/*  Log.d("PictureSize", "Supported Size: " + result.width + "height : " + result.height);*/
			/*System.out.println("BACK PictureSize Supported Size: " + result.width + "height : " + result.height);*/
		}
		if (arrayListForWidth.size() != 0 && arrayListForHeight.size() != 0) {
           /* System.out.println("Back max W :"+ Collections.max(arrayListForWidth));              // Gives Maximum Width
            System.out.println("Back max H :"+Collections.max(arrayListForHeight));                 // Gives Maximum Height
            System.out.println("Back Megapixel :"+( ((Collections.max(arrayListForWidth)) * (Collections.max(arrayListForHeight))) / 1024000 ) );
*/
			Megapixel = (((Collections.max(arrayListForWidth)) * (Collections.max(arrayListForHeight))) / 1024000);

		}
		camera.release();

		arrayListForWidth.clear();
		arrayListForHeight.clear();

		return Megapixel;

	}

	private static void releaseCameraAndPreview() {

		if (camera != null) {
			camera.release();
			camera = null;
		}
	}


	public static StringBuilder getLog() {

		StringBuilder myLog = null;

		try {


			//  Process process = Runtime.getRuntime().exec("logcat -d  *:e ");
			Process process = Runtime.getRuntime().exec("logcat -d");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			myLog = new StringBuilder();
			String line;
			String separator = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null) {
				myLog.append(line);
				myLog.append(separator);
			}

			clearLog();

		} catch (IOException e) {

			e.printStackTrace();
		}

		return myLog;
	}

	public static void clearLog() {
		try {
			Process process = new ProcessBuilder()
					.command("logcat", "-c")
					.redirectErrorStream(true)
					.start();
		} catch (IOException e) {
		}
	}

	public static void appendLog(String text) {

		//System.out.println("In Append Log");

		try {
			if (text != null) {
				String separator = System.getProperty("line.separator");

				String SD_CARD_PATH = Environment.getExternalStorageDirectory()
						.toString();


				File Folder = new File(SD_CARD_PATH + "/" + AppConstant.ROOT_DIRECTORY);
				if (Folder.exists()) {
					//System.out.println("NBFC folder allready exists");
				} else {
					Folder.mkdir();
				}

				File Folder1 = new File(SD_CARD_PATH + "/" + AppConstant.ROOT_DIRECTORY + "/Logs/");
				if (Folder1.exists()) {
					//System.out.println("NBFC/Logs folder already exists");
				} else {
					Folder1.mkdir();
				}


				Calendar c = Calendar.getInstance();
				//System.out.println("Current time => " + c.getTime());

				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				String formattedDate = df.format(c.getTime());


				File logFile = new File(SD_CARD_PATH + "/" + AppConstant.ROOT_DIRECTORY + "/Logs/" + get_Current_day() + "(" + formattedDate + ").txt");


				try {
					//BufferedWriter for performance, true to set append to file flag
					BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));


					// buf.append(separator);

					buf.newLine();

					buf.append(text);

					buf.newLine();
					buf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public static void deleteOldFiles() {
		String SD_CARD_PATH = Environment.getExternalStorageDirectory()
				.toString();
		java.util.Date oldestAllowedFileDate = DateUtils.addDays(new java.util.Date(), -5); //minus days from current date
		File targetDir = new File(SD_CARD_PATH + "/" + AppConstant.ROOT_DIRECTORY + "/Logs/");

		File[] contents = targetDir.listFiles();

		if (contents == null) {
			System.out.println("The Folder is Empty");
		}
// Folder is empty
		else if (contents.length == 0) {

			System.out.println("The Folder is Empty");

		}
// Folder contains files
		else {


			try {
				Iterator<File> filesToDelete = FileUtils.iterateFiles(targetDir, new AgeFileFilter(oldestAllowedFileDate), null);
				//if deleting subdirs, replace null above with TrueFileFilter.INSTANCE
				while (filesToDelete.hasNext()) {
					FileUtils.deleteQuietly(filesToDelete.next());

					System.out.println("Files Deleted");
				}  //I don't want an exception if a file is not deleted. Otherwise use filesToDelete.next().delete() in a try/catch

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public static void cancelDialogLoading() {
		try {
			if (dialogGoogleLoading != null) {
				dialogGoogleLoading.dismiss();
				dialogGoogleLoading = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getCurrentDay() {
		String separator = System.getProperty("line.separator");

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date(0);
		//String dayOfTheWeek = sdf.format(d);

		String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());


		return dayOfWeek;
	}

	public static void showAlert(final Context mContext, String msg) {

		AlertDialog.Builder al = new AlertDialog.Builder(mContext);
		al.setCancelable(false);
		al.setTitle("Offeriez");
		al.setMessage(msg);

		al.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//startActivity(new Intent(BASE_CONTEXT,MyAccount.class));

				((Activity) mContext).finish();

			}
		});


		al.show();

	}


	public static void createLog() {
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());

		String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
		File logFile = new File(SD_CARD_PATH + "/" + AppConstant.ROOT_DIRECTORY + "/Logs/" + get_Current_day() + "(" + formattedDate + ").txt");

	}

	public static boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static String get_Current_day() {
		String separator = System.getProperty("line.separator");

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		java.sql.Date d = new java.sql.Date(0);
		//String dayOfTheWeek = sdf.format(d);

		String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());


		return dayOfWeek;
	}



	public static SweetAlertDialog showProgressDialog(Context context,String message,SweetAlertDialog pDialog) {
        if(pDialog!=null)
        {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(message);
            pDialog.setCancelable(false);
            pDialog.show();
        }

		return pDialog;
	}

	public static void hideProgressDialog(SweetAlertDialog pDialog) {
		if (pDialog!=null && pDialog.isShowing())
			pDialog.cancel();
	}

    /*public static void exportDatabase(Context mContext)
    {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + mContext.getPackageName() + "/databases/DealerSalesAuditTable";
                String backupDBPath = "/dss_"+DateTimeUtil.getCurrentDate()+".db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);
				if(!backupDB.exists())
					backupDB.mkdirs();

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }

    }*/

	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress();
						//boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						boolean isIPv4 = sAddr.indexOf(':')<0;

						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
								return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
							}
						}
					}
				}
			}
		} catch (Exception ex) { } // for now eat exceptions
		return "";
	}


    public static String getUserAgent(Context mContext)
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String version = String.valueOf(Build.VERSION.SDK_INT);
        String versionRelease = Build.VERSION.RELEASE;
        String device_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        return "Company Name - "+manufacturer+"/ Mobile - "+model+"/ Android Version - "+version+"/ Device ID - "+device_id;
    }

	public static boolean createDirIfNotExists(String path) {
		boolean ret = true;
		try {
			File file = new File(Environment.getExternalStorageDirectory(), path);
			if (!file.exists()) {
				if (!file.mkdirs()) {
					Log.e("File Log :: ", "Problem creating Image folder");
					ret = false;
				}
			}
		}catch (Exception ex)
		{
			ex.printStackTrace();
			ret = false;
		}
		return ret;
	}

	@SuppressLint("LongLogTag")
	public static String getCompleteAddressString(Context m_context, double LATITUDE, double LONGITUDE) {
		String strAdd = "";
		Geocoder geocoder = new Geocoder(m_context, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
			if (addresses != null) {
				if(addresses.size()!=0)
				{
					Address returnedAddress = addresses.get(0);
					StringBuilder strReturnedAddress = new StringBuilder("");

					for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
						strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
					}
					strAdd = strReturnedAddress.toString();
				}else
				{
					Log.v("My Current location address", "No Address returned!");
				}
			}
//            else {
//                Log.v("My Current location address", "No Address returned!");
//            }
		} catch (Exception e) {
			e.printStackTrace();

//            Log.v("My Current location address", "Cannot get Address!");
		}
		return strAdd;
	}

	private void deleteFile(String inputPath, String inputFile) {
		try {
			// delete the original file
			new File(inputPath + inputFile).delete();


		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}
	}

	public static void copyFile(String inputPath, String inputFile, String outputPath) {

		InputStream in = null;
		OutputStream out = null;
		try {

			//create output directory if it doesn't exist
			File dir = new File (outputPath);
			if (!dir.exists())
			{
				dir.mkdirs();
			}


			in = new FileInputStream(inputPath + inputFile);
			out = new FileOutputStream(outputPath + inputFile);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;

			// write the output file (You have now copied the file)
			out.flush();
			out.close();
			out = null;

		}  catch (FileNotFoundException fnfe1) {
			Log.e("tag", fnfe1.getMessage());
		}
		catch (Exception e) {
			Log.e("tag", e.getMessage());
		}

	}

	public static boolean numValidator(String number)
	{
		Pattern pattern;
		Matcher matcher;
		final String NUMERIC_PATTERN = "^[1-9]\\d*$";
		pattern = Pattern.compile(NUMERIC_PATTERN);
		matcher = pattern.matcher(number);
		return matcher.matches();
	}

    public boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

	public static String encodeFileToBase64Binary(File fileName) throws IOException {
		byte[] bytes = loadFile(fileName);
		byte[] encoded = Base64.encode(bytes,Base64.DEFAULT);
		String encodedString = new String(encoded);
		return encodedString;
	}

	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}

	public static void printLongLog(String veryLongString){
		int maxLogSize = 1000;
		for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
			int start = i * maxLogSize;
			int end = (i+1) * maxLogSize;
			end = end > veryLongString.length() ? veryLongString.length() : end;
			Log.v(TAG, veryLongString.substring(start, end));
			System.out.println(veryLongString.substring(start, end));
		}
	}

    public static String getBitMapFromPath(String path)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        String encodedString="";
        System.gc();
        try
        {
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            encodedString=BitMapToString(bitmap);

            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }

        }catch (Exception e)
        {
            System.gc();
            e.printStackTrace();
        }catch(OutOfMemoryError e)
        {
            System.gc();
            e.printStackTrace();
        }
        return encodedString;
    }

    public static String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);

        byte [] b=baos.toByteArray();

        String temp=null;

        try{

            System.gc();

            temp= Base64.encodeToString(b, Base64.DEFAULT);

        }catch(Exception e){

            e.printStackTrace();

        }catch(OutOfMemoryError e){

            baos=new  ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
            b=baos.toByteArray();

            temp=Base64.encodeToString(b, Base64.DEFAULT);

            Log.e("EWN", "Out of memory error catched");

        }

        return temp;

    }

	/*public static void writeToFile(String data,Context context) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(Const.ROOT_DIRECTORY + "/" + Const.APP_LOG_PATH + "/"+"logs.txt", Context.MODE_PRIVATE));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		}
		catch (IOException e) {
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}
*/
	public String readFromFile(Context context) {

		String ret = "";

		try {
			InputStream inputStream = context.openFileInput("config.txt");

			if ( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ( (receiveString = bufferedReader.readLine()) != null ) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		}
		catch (FileNotFoundException e) {
			Log.e("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("login activity", "Can not read file: " + e.toString());
		}

		return ret;
	}


   /* public static void writeStringAsFile(final String fileContents, String fileName, Context context) {

        try {
            FileWriter out = new FileWriter(new File(Const.ROOT_DIRECTORY, "Demo.txt"));
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            //Logger.logError(TAG, e);
        }
    }*/

    public static String readFileAsString(String fileName,Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            //Logger.logError(TAG, e);
        } catch (IOException e) {
           // Logger.logError(TAG, e);
        }

        return stringBuilder.toString();
    }


	public static void showSuccessMessage(final Context mContext, String title, String succesMsg, final FragmentActivity activity){
		try {
			if(mContext!=null) {
				SweetAlertDialog sw = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
				Window window = sw.getWindow();
				window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				sw.setContentText(succesMsg);
				sw.setTitleText(title)
						.setConfirmText("OK")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
								activity.finish();
								activity.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
							}
						})
						.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void showWarningMessage(final Context mContext,final String title,final String succesMsg, final Activity activity){
		try {
			if(mContext!=null && activity!=null) {
				SweetAlertDialog sw = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
				Window window = sw.getWindow();
				window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				sw.setContentText(succesMsg);
				sw.setTitleText(title)
						.setConfirmText("OK")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
								/*Intent i = new Intent(mContext, DusmileBaseActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								AppConstant.isAvilableJobs = true;
								AppConstant.isAssinedJobs = false;
								AppConstant.isCompletedJobs = false;
								mContext.startActivity(i);
								activity.finish();*/

							}
						})
						.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void showOfflineSuccessMessage(final Context mContext, String title, String succesMsg,final Activity activity){
		try {
			if(mContext!=null) {
				SweetAlertDialog sw = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
				Window window = sw.getWindow();
				window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				sw.setContentText(succesMsg);
				sw.setTitleText(title)
						.setConfirmText("OK")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
								activity.finish();
								IOUtils.stopLoading();
								IOUtils.stopUpdateStatusLoading();
								/*AppConstant.isAvilableJobs = true;
								AppConstant.isAssinedJobs = false;
								AppConstant.isCompletedJobs = false;
							    activity.finish();
							    activity.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);*/

							}
						})
						.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void showInfoMessage(Context mContext,String message){
		try {
			new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
					.setTitleText(message)
					.setConfirmText("OK")
					.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog) {
							sweetAlertDialog.dismissWithAnimation();

						}
					})
					.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void showWarningMessage(Context mContext,String message){
		try {
			if (mContext != null) {
				SweetAlertDialog sw = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
				Window window = sw.getWindow();
				window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				sw.setContentText(message);
				sw.setTitleText(mContext.getString(R.string.app_name));
				sw.setConfirmText("OK")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();

							}
						})
						.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void showLogoutMessage(final Context mContext, String message, final Activity activity){
		try {
			if(mContext!=null) {
				new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
						.setTitleText(message).setCancelText("No")
						.setConfirmText("Yes")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
								UserPreference.clearAllPreference(mContext);
								Intent intent = new Intent(mContext, SplashActivity.class);
								mContext.startActivity(intent);
								activity.finish();
						/*int currentapiVersion = android.os.Build.VERSION.SDK_INT;
						if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
							activity.finishAffinity();
						}else{
							ActivityCompat.finishAffinity(activity);
						}*/
								AppConstant.isAvilableJobs = true;
								AppConstant.isCompletedJobs = false;
								AppConstant.isAssinedJobs = false;
								MyDynamicToast.successMessage(mContext, "You are successfully logged out");

							}
						})
						.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismiss();
							}
						})
						.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void showExitAppMessage(final Context mContext, String message, final Activity activity){
		try {
			if (mContext != null) {
				new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
						.setTitleText(message).setCancelText("No")
						.setConfirmText("Yes")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
								activity.finish();
								int currentapiVersion = android.os.Build.VERSION.SDK_INT;
								if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
									activity.finishAffinity();
								} else {
									ActivityCompat.finishAffinity(activity);
								}
								AppConstant.isAvilableJobs = true;
								AppConstant.isCompletedJobs = false;
								AppConstant.isAssinedJobs = false;
							}
						})
						.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismiss();
							}
						})
						.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void showErrorMessage(Context mContext,String message){
		try {
			if (mContext != null) {
				new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
						.setTitleText(message)
						.setConfirmText("OK")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();

							}
						})
						.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void showSubmitMessage(final Context mContext, String title, String succesMsg, final Activity activity){
		try {
			if(mContext!=null) {
				SweetAlertDialog sw = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
				Window window = sw.getWindow();
				window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				sw.setContentText(succesMsg);
				sw.setTitleText(title)
						.setConfirmText("OK")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
								/*Intent i = new Intent(mContext, DusmileBaseActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								AppConstant.isAvilableJobs = true;
								mContext.startActivity(i);*/
								activity.finish();
								activity.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
							}
						})
						.show();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public static void showSubmitErrorMessage(final Context mContext, String title, String successMsg, final Activity activity){
		try {
			if (mContext != null) {
				SweetAlertDialog sw = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
				Window window = sw.getWindow();
				window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				sw.setContentText(successMsg);
				sw.setTitleText(title)
						.setConfirmText("OK")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
								Intent i = new Intent(mContext, DusmileBaseActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								AppConstant.isAvilableJobs = true;
								mContext.startActivity(i);
								activity.finish();

							}
						})
						.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void showExitFormMessage(final Context mContext, String message, final Activity activity){
		try {
			if (mContext != null) {
				new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
						.setTitleText(message).setCancelText("No")
						.setConfirmText("Yes")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismissWithAnimation();
								activity.finish();
								activity.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
							}
						})
						.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismiss();
							}
						})
						.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public static String getAdherence(String auditedScore , String wieghtage ){

		String adherence="0";
		try {
			if (!TextUtils.isEmpty(auditedScore) && !auditedScore.equalsIgnoreCase("null") && !TextUtils.isEmpty(wieghtage) && !wieghtage.equalsIgnoreCase("null")){
				double score = Double.parseDouble(auditedScore);

				double wieght = Double.parseDouble(wieghtage);

				double adh = (score / wieght) * 100.0f;

				double roundedAuditedScore = Math.round(adh * 100.0) / 100.0;

				//Double d = new Double(roundedAuditedScore);
				//int per = d.intValue();
				adherence = String.valueOf(roundedAuditedScore);
			}else {
				return "0";
			}

		}catch (Exception e){
			e.printStackTrace();
			adherence="0";
		}

		return adherence;

	}

	public static Bitmap drawableToBitmap (Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable)drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public static InputStream bitmapToInputStream(Bitmap bitmap) {
		int size = bitmap.getHeight() * bitmap.getRowBytes();
		ByteBuffer buffer = ByteBuffer.allocate(size);
		bitmap.copyPixelsToBuffer(buffer);
		return new ByteArrayInputStream(buffer.array());
	}


	public static long getStorageFreeSpace(){
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
		long megAvailable = bytesAvailable / (1024 * 1024);
		Log.e("","Available MB : "+megAvailable);
		System.out.println("Available MB : "+megAvailable);
		return megAvailable;
	}

	public static void turnGPSOn(Context ctx)
	{
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		ctx.sendBroadcast(intent);

		String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if(!provider.contains("gps")){ //if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			ctx.sendBroadcast(poke);


		}
	}
	// automatic turn off the gps
	public static void turnGPSOff(Context ctx)
	{
		String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if(provider.contains("gps")){ //if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			ctx.sendBroadcast(poke);
		}
	}

    public static String randomString(char[] characterSet, int length) {
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }

	/*public  static Boolean writeLogs(String fcontent){
		try {

			String fpath = Const.ROOT_DIRECTORY + "/" + Const.APP_LOG_PATH + "/"+"logs.txt";
			OutputStreamWriter outStreamWriter = null;
			FileOutputStream outStream = null;
			File file = new File(fpath);

			// If file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			outStream = new FileOutputStream(file,true) ;
			outStreamWriter = new OutputStreamWriter(outStream);

			outStreamWriter.append(fcontent);
			outStreamWriter.flush();

			Log.d("Suceess","Sucess");
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}*/

	public static boolean isGoogleMapsInstalled()
	{
		try
		{
			ApplicationInfo info = DusmileApplication.getAppContext().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
			return true;
		}
		catch(PackageManager.NameNotFoundException e)
		{
			return false;
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public static void sendUserToLogin(Context context, Activity activity)
	{
		UserPreference.clearAllPreference(context);
		Intent i = new Intent(context,SplashActivity.class);
		context.startActivity(i);
		activity.finish();;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
			activity.finishAffinity();
		}else{
			ActivityCompat.finishAffinity(activity);
		}

	}


	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
		Map<String, Object> retMap = new HashMap<>();

		if(json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while(keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if(value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if(value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<>();
		for(int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if(value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if(value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}


	public static int getAge (int _year, int _month, int _day) {

		GregorianCalendar cal = new GregorianCalendar();
		int y, m, d, a;

		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH);
		d = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(_year, _month, _day);
		a = y - cal.get(Calendar.YEAR);
		if ((m < cal.get(Calendar.MONTH))
				|| ((m == cal.get(Calendar.MONTH)) && (d < cal
				.get(Calendar.DAY_OF_MONTH)))) {
			--a;
		}
		if(a < 0)
			throw new IllegalArgumentException("Age < 0");
		return a;
	}


	public static boolean containsDigit(String s) {
		if (s != null && !s.isEmpty()) {
			for (char c : s.toCharArray()) {
				if (Character.isDigit(c)) {
					return true;
				}
			}
		}
		return false;
	}


	public static void geocodeAddress(String addressStr, Geocoder gc) {
		Address address = null;
		List<Address> addressList = null;
		double latitude = 0.0;
		double longitude = 0.0;
		try {
			if (!TextUtils.isEmpty(addressStr)) {
				addressList = gc.getFromLocationName(addressStr, 5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != addressList && addressList.size() > 0) {
			address = addressList.get(0);
		}
		if (null != address && address.hasLatitude()
				&& address.hasLongitude()) {
			latitude = address.getLatitude();
			longitude = address.getLongitude();
		}

	}





	public static void printFields(Class objClass) throws Exception {
		Class[] classes = objClass.getDeclaredClasses();

		for(Class innerClass: classes){
			Field[] fields = innerClass.getDeclaredFields();
			for(Field field : fields){
				String name = field.getName();
				Log.i(name , ": ");
			}
		}

	}

	public static String getDateDifference(Date startDate, Date endDate){
		long different = endDate.getTime() - startDate.getTime();
     	long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		long elapsedDays = different / daysInMilli;
		return ""+elapsedDays;

	}

	public static String getDateHourDifference(Date startDate, Date endDate){
		long different = endDate.getTime() - startDate.getTime();
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		long diffHours = different / hoursInMilli;
		long diffMin = different / minutesInMilli;
		return ""+diffMin;

	}

	public static File getLogFile()
	{
		String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());
		File logFile = new File(SD_CARD_PATH + "/"+AppConstant.ROOT_DIRECTORY+"/Logs/"+get_Current_day()+"("+formattedDate+").txt");
		return logFile;
	}

	public static String getCurrentTimeStamp()
	{
		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}

	public static Date getCurrentDate()
	{
		Date date = null;
		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());
		try {
			date = df.parse(formattedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}


	public static String getRelativeDate(String date, Context context) {
		SimpleDateFormat formatter;
		String formattedDate = "";

		try {
			if (!TextUtils.isEmpty(date)) {
				if (date.contains("/")) {
					formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
				} else {
					formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
				}
				Date last = null;
				try {
					last = formatter.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar calendar = Calendar.getInstance(Locale.US);
				calendar.setTime(last);

				formattedDate = (String) android.text.format.DateUtils.getRelativeDateTimeString(

						context, // Suppose you are in an activity or other Context subclass

						last.getTime(), // The time to display
						android.text.format.DateUtils.DAY_IN_MILLIS, // The resolution. This will display only minutes
						// (no "3 seconds ago"

						(android.text.format.DateUtils.WEEK_IN_MILLIS) + (android.text.format.DateUtils.WEEK_IN_MILLIS), // The maximum resolution at which the time will switch
						// to default date instead of spans. This will not
						// display "3 weeks ago" but a full date instead

						android.text.format.DateUtils.FORMAT_ABBREV_ALL); // Eventual flags*/
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return date;
	}

	public static Bitmap mark(Bitmap src, String lat, String longi , String time) {
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
		Canvas newCanvas = new Canvas(result);
		newCanvas.drawBitmap(src, 0, 0, null);
		Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintText.setColor(Color.RED);
		paintText.setTextSize(43.0f);
		paintText.setStyle(Paint.Style.FILL);
		Rect rectText = new Rect();
		paintText.getTextBounds(lat, 0, lat.length(), rectText);

		newCanvas.drawText(lat,
				5, rectText.height()+10, paintText);
		Paint paintText1 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintText1.setColor(Color.RED);
		paintText1.setTextSize(43.0f);
		paintText1.setStyle(Paint.Style.FILL);
		Rect rectText1 = new Rect();
		paintText1.getTextBounds(longi, 0, longi.length(), rectText1);

		newCanvas.drawText(longi,
				5, rectText1.height()+50, paintText1);

		Paint paintText2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintText2.setColor(Color.RED);
		paintText2.setTextSize(23.0f);
		paintText2.setStyle(Paint.Style.FILL);
		Rect rectText2 = new Rect();
		paintText2.getTextBounds(time, 0, time.length(), rectText2);

		newCanvas.drawText(time,
				5, rectText2.height()+120, paintText2);

		return result;
	}

	public static void deleteImagesFolder(String jobId)
	{
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobId);
			if (directory.isDirectory()) {
				String[] children = directory.list();
				for (int i = 0; i < children.length; i++) {
					new File(directory, children[i]).delete();
				}
				directory.delete();
				IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Deleted images from folder");
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
