package com.ninetoseven.series.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class Util {
	
	private static final String TAG = "NE2";

	public static void screenSize(Context context)
	{
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
         Log.d(TAG, "width: "+dpWidth+" height: "+dpHeight);
	}

}
