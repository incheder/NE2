package com.ninetoseven.series.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;

public class Util {
	
	private static int TIMEOUT = 30000;
	private static final String TAG = "NE2";
	private static final int NUMBER_TRYS = 3;
	public static DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(TIMEOUT,NUMBER_TRYS,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

	public static void screenSize(Context context)
	{
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
         Log.d(TAG, "width: "+dpWidth+" height: "+dpHeight);
	}
	
	public static File getImageStorageDir(Context context, String directoryName) {
	    // Get the directory for the app's private pictures directory. 
	    File file = new File(context.getExternalFilesDir(
	            Environment.DIRECTORY_PICTURES), directoryName);
	    if (!file.mkdirs()) {
	       // Log.e(LOG_TAG, "Directory not created");
	    	 return null;
	    }
	    else
	    {
	    	 String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
	 	    File mediaFile;
	 	        String mImageName="MI_"+ timeStamp +".jpg";
	 	        mediaFile = new File(file.getPath() + File.separator + mImageName);  
	 	    return mediaFile;
	    }
	   
	}
	
	public static void storeImage(Bitmap image, Context context, String directoryName) {
	    File pictureFile = getImageStorageDir(context, directoryName);
	    if (pictureFile == null) {
	        Log.d(TAG,
	                "Error creating media file, check storage permissions: ");// e.getMessage());
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
	
	
	
	

}
