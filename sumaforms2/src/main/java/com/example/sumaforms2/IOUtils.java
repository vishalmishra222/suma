/**
 *
 * @author vCleen
 *
 */

package com.example.sumaforms2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
	public static String Tag = "IOUtils";

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

	public static String get_Current_day() {
		String separator = System.getProperty("line.separator");

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		java.sql.Date d = new java.sql.Date(0);
		//String dayOfTheWeek = sdf.format(d);

		String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());


		return dayOfWeek;
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

}
