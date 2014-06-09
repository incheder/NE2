package com.ninetoseven.series.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;

public class Util {

	private static int TIMEOUT = 60000;
	private static final String TAG = "NE2";
	private static final int NUMBER_TRYS = 3;
	public static DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
			TIMEOUT, NUMBER_TRYS, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

	public static void screenSize(Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();

		float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
		float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
		Log.d(TAG, "width: " + dpWidth + " height: " + dpHeight);
	}

	public static File getImageStorageDir(Context context, String directoryName) {
		// Get the directory for the app's private pictures directory.
		File file = new File(
				context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				directoryName);
		if (!file.mkdirs()) {
			// Log.e(LOG_TAG, "Directory not created");
			return null;
		} else {
			String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm")
					.format(new Date());
			File mediaFile;
			String mImageName = "MI_" + timeStamp + ".jpg";
			mediaFile = new File(file.getPath() + File.separator + mImageName);
			return mediaFile;
		}

	}

	public static void storeImage(Bitmap image, Context context,
			String directoryName) {
		File pictureFile = getImageStorageDir(context, directoryName);
		if (pictureFile == null) {
			Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
		}
	}

	public static Date parseDate(String datestring) throws ParseException {
		Date d = new Date();
		if (datestring.endsWith("Z")) {
			try {
				SimpleDateFormat s = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss'Z'"); // spec for RFC3339
				d = s.parse(datestring);
			} catch (java.text.ParseException pe) {// try again with optional
													// decimals
				SimpleDateFormat s = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");// spec for RFC3339
															// (with fractional
															// seconds)
				s.setLenient(true);
				d = s.parse(datestring);
			}
			return d;
		}
		String firstpart = datestring.substring(0, datestring.lastIndexOf('-'));
		String secondpart = datestring.substring(datestring.lastIndexOf('-'));

		// step two, remove the colon from the timezone offset
		secondpart = secondpart.substring(0, secondpart.indexOf(':'))
				+ secondpart.substring(secondpart.indexOf(':') + 1);
		datestring = firstpart + secondpart;
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");// spec
																			// for
																			// RFC3339

		try {
			d = s.parse(datestring);
		} catch (java.text.ParseException pe) {// try again with optional
												// decimals
			s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");// spec
																		// for
																		// RFC3339
																		// (with
																		// fractional
																		// seconds)
			s.setLenient(true);
			d = s.parse(datestring);
		}
		return d;

	}
	
	public static long createCalendarEvent(Context context,Date date)
	{
		long eventID;
		long calID = 3;
		long startMillis = 0; 
		//long endMillis = 0;     
		//Calendar beginTime = Calendar.getInstance();
		//beginTime.set(2012, 9, 14, 7, 30);
		//startMillis = beginTime.getTimeInMillis();
		//Calendar endTime = Calendar.getInstance();
		//endTime.set(2012, 9, 14, 8, 45);
		//endMillis = endTime.getTimeInMillis();
		startMillis = date.getTime();
		
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startMillis);
		values.put(Events.TITLE, "titulo");
		values.put(Events.DESCRIPTION, "descripcion");
		values.put(Events.CALENDAR_ID, calID);
		values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
		values.put(Events.DURATION,"PT1H" );
		Uri uri = cr.insert(Events.CONTENT_URI, values);
		eventID = uri.getLastPathSegment()==null ? 0 : Long.parseLong(uri.getLastPathSegment());
		return eventID;
	
	}
	
	public static long addReminder(Context context, long id)
	{
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Reminders.MINUTES, 15);
		values.put(Reminders.EVENT_ID, id);
		values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		Uri uri = cr.insert(Reminders.CONTENT_URI, values);
		return uri.getLastPathSegment()==null ? 0 : Long.parseLong(uri.getLastPathSegment());
	}

}
