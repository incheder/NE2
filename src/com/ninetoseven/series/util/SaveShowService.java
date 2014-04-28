package com.ninetoseven.series.util;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ninetoseven.series.R;
import com.ninetoseven.series.db.LastEpisodeContract.LastEntry;
import com.ninetoseven.series.db.NewEpisodeDbHelper;
import com.ninetoseven.series.db.NextEpisodeContract.NextEntry;
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
		String error = null;
		Show show = intent.getParcelableExtra("show");
		
		NewEpisodeDbHelper neDbHelper = new NewEpisodeDbHelper(getBaseContext());
		try
		{
			SQLiteDatabase db = neDbHelper.getWritableDatabase();
			if(exist(db, show.getId(),ShowEntry.COLUMN_NAME_SHOW_ID,ShowEntry.TABLE_NAME))
			{
				//Toast.makeText(getApplicationContext(), R.string.show_already_saved, Toast.LENGTH_SHORT).show();
				error =getApplicationContext().getResources().getString(R.string.show_already_saved);
			}
			else
			{
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
				{//hubo un error
					//Toast.makeText(getApplicationContext(), R.string.error_insert_db, Toast.LENGTH_SHORT).show();
					error =getApplicationContext().getResources().getString(R.string.error_insert_db);
				
				}
			}
			
			
			//añadimos el nuevo episodio si es que no es NULL
			if(show.getNextepisode()!=null)
			{
				if(exist(db, show.getNextepisode().getNumber(),NextEntry.COLUMN_NAME_NUMBER , NextEntry.TABLE_NAME))
				{
					
				}
				else
				{
					ContentValues nextEp = new ContentValues();
					nextEp.put(NextEntry.COLUMN_NAME_SHOWNAME, show.getShowName());
					nextEp.put(NextEntry.COLUMN_NAME_SHOW_ID, show.getId());
					nextEp.put(NextEntry.COLUMN_NAME_NUMBER, show.getNextepisode().getNumber());
					nextEp.put(NextEntry.COLUMN_NAME_TITLE, show.getNextepisode().getTitle());
					nextEp.put(NextEntry.COLUMN_NAME_AIRDATE, show.getNextepisode().getAirdate());
					nextEp.put(NextEntry.COLUMN_NAME_AIRTIME, show.getNextepisode().getAirtime());
					if(db.insert(NextEntry.TABLE_NAME, null, nextEp)==-1)
					{
						//Toast.makeText(getApplicationContext(), R.string.error_insert_ep_db, Toast.LENGTH_SHORT).show();
						error =getApplicationContext().getResources().getString(R.string.error_insert_ep_db);
					}
				}
			}
			
			//añadimos el ultimo episodio si es que no es null
			if(show.getLatestepisode()!=null)
			{
				if(!exist(db, show.getLatestepisode().getNumber(),LastEntry.COLUMN_NAME_NUMBER , LastEntry.TABLE_NAME))
				{//si no existe
					ContentValues lastEp = new ContentValues();
					lastEp.put(NextEntry.COLUMN_NAME_SHOWNAME, show.getShowName());
					lastEp.put(LastEntry.COLUMN_NAME_SHOW_ID, show.getId());
					lastEp.put(LastEntry.COLUMN_NAME_NUMBER, show.getLatestepisode().getNumber());
					lastEp.put(LastEntry.COLUMN_NAME_TITLE, show.getLatestepisode().getTitle());
					lastEp.put(LastEntry.COLUMN_NAME_AIRDATE, show.getLatestepisode().getAirdate());
					lastEp.put(LastEntry.COLUMN_NAME_AIRTIME, show.getLatestepisode().getAirtime());
					if(db.insert(LastEntry.TABLE_NAME, null, lastEp)==-1)
					{
						//Toast.makeText(getApplicationContext(), R.string.error_insert_ep_db, Toast.LENGTH_SHORT).show();
						error =getApplicationContext().getResources().getString(R.string.error_insert_ep_db);
					}
				}
			}
			
			
			
			
			
			if(db.isOpen())
			{
				db.close();
			}
			if(error==null)//si no hay error actualizamos el grid
			{
				Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
				 // Puts the extra into the Intent
				 .putExtra(Constants.EXTENDED_DATA_STATUS, getApplicationContext().getResources().getString(R.string.show_saved));
				 // Broadcasts the Intent to receivers in this app.
				 LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
			}
			else//si hay error mostramos mensaje en cualquier actividad en la que se encuentre
			{
				Intent localIntent = new Intent(Constants.BROADCAST_ERROR)
				 // Puts the extra into the Intent
				 .putExtra(Constants.EXTENDED_DATA_ERROR, error);
				 // Broadcasts the Intent to receivers in this app.
				 LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
			}
			 
			 
		}catch(SQLiteException e)
		{
			Log.e(TAG, "error: "+e.getMessage());
		}

	}
	private boolean exist(SQLiteDatabase db,String id, String column, String table)
	{
		String[] projection={
				column	
			};
		String selection=column+"='"+id+"'";
		Cursor c = db.query(table, projection, selection, null, null, null, null);
		if(c.moveToFirst())
		{
			return true;
		}
		return false;
		
	}
	
	public  final class Constants
	{
		 public static final String BROADCAST_ACTION ="com.ninetoseven.series.util.SaveShowService.BROADCAST";
		 public static final String BROADCAST_ERROR ="com.ninetoseven.series.util.SaveShowService.ERROR";
		 public static final String EXTENDED_DATA_STATUS ="com.ninetoseven.series.util.SaveShowService.STATUS";
		 public static final String EXTENDED_DATA_ERROR ="com.ninetoseven.series.util.SaveShowService.EXTENDED_DATA_ERROR";
	}
	
}

