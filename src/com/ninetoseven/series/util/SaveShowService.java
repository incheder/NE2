package com.ninetoseven.series.util;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.ninetoseven.series.R;
import com.ninetoseven.series.db.NewEpisodeDbHelper;
import com.ninetoseven.series.db.ShowContract.ShowEntry;
import com.ninetoseven.series.model.Show;

public class SaveShowService extends IntentService {

	private static final String TAG = "NE2";

	public SaveShowService() {
		super("SaveShowService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		//String data = intent.getDataString();
		Show show = intent.getParcelableExtra("show");
		
		NewEpisodeDbHelper neDbHelper = new NewEpisodeDbHelper(getBaseContext());
		SQLiteDatabase db = neDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ShowEntry.COLUMN_NAME_SHOW_ID, show.getId());
		values.put(ShowEntry.COLUMN_NAME_SHOWNAME, show.getShowName());
		values.put(ShowEntry.COLUMN_NAME_LINK, show.getShowLink());
		values.put(ShowEntry.COLUMN_NAME_SEASONS, show.getSeasons());
		values.put(ShowEntry.COLUMN_NAME_IMAGE, show.getImage());
		values.put(ShowEntry.COLUMN_NAME_STARTED, show.getStarted());
		values.put(ShowEntry.COLUMN_NAME_ENDED, show.getEnded());
		values.put(ShowEntry.COLUMN_NAME_STATUS, show.getStatus());
		values.put(ShowEntry.COLUMN_NAME_SUMARY, show.getSummary());
		values.put(ShowEntry.COLUMN_NAME_RUNTIME, show.getRuntime());
		values.put(ShowEntry.COLUMN_NAME_NETWORK, show.getNetwork());
		values.put(ShowEntry.COLUMN_NAME_AIRTIME, show.getAirtime());
		values.put(ShowEntry.COLUMN_NAME_AIRDAY, show.getAirday());
		values.put(ShowEntry.COLUMN_NAME_TIMEZONE, show.getTimezone());
		if(db.insert(ShowEntry.TABLE_NAME,null, values)==-1)
		{
			Toast.makeText(getApplicationContext(), R.string.error_insert_db, Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	public  final class Constants
	{
		 public static final String BROADCAST_ACTION ="com.example.android.threadsample.BROADCAST";
		 public static final String EXTENDED_DATA_STATUS ="com.example.android.threadsample.STATUS";
	}
	
}

