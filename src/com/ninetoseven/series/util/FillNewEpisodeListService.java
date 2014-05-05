package com.ninetoseven.series.util;


import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
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
import com.ninetoseven.series.model.Episode;

public class FillNewEpisodeListService extends IntentService {

	private static final String TAG = "NE2";


	    
	
	public FillNewEpisodeListService() {
		super("SaveShowService");
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String error = null;
		//List<String> idList = intent.getStringArrayListExtra("idList");
		
		NewEpisodeDbHelper neDbHelper = new NewEpisodeDbHelper(getBaseContext());
		try
		{
			ArrayList<Episode> eList = new ArrayList<Episode>();
			SQLiteDatabase db = neDbHelper.getReadableDatabase();
			String[] projection={
					"showid",
					
				};
			
			String[] projectionEpisode={
					"showid",
					"showname",
					"number",
					"title",
					"airdate",
					"airtime",
					"image"
					
				};
			
			Cursor c = db.query(ShowEntry.TABLE_NAME, projection, null, null, null, null, null);
			if(c.moveToFirst())
			{
				List<String> idList = new ArrayList<String>();
				do
				{
					idList.add(c.getString(0));
				}
				while(c.moveToNext()); 
				
				
				for (int i = 0; i < idList.size(); i++)
				{
					String selection="showid='"+idList.get(i)+"'";
					
					Cursor n = db.query(NextEntry.TABLE_NAME, projectionEpisode, selection, null, null, null, null);
					if(n.moveToFirst())
					{
						Episode episode = new Episode();
						episode.setShowName(n.getString(1));
						episode.setNumber(n.getString(2));
						episode.setTitle(n.getString(3));
						episode.setAirdate(n.getString(4));
						episode.setAirtime(n.getString(5));
						episode.setImage(n.getString(6));
						eList.add(episode);
					}
					else//si no encontro un nuevo episodio de este id
					{
						//intentamos buscar su ultimo episodio
						Cursor u = db.query(LastEntry.TABLE_NAME, projectionEpisode, selection, null, null, null, null);
						if(u.moveToFirst())
						{
							Episode episode = new Episode();
							episode.setShowName(u.getString(1));
							episode.setNumber(u.getString(2));
							episode.setTitle(u.getString(3));
							episode.setAirdate(u.getString(4));
							episode.setAirtime(u.getString(5));
							episode.setImage(u.getString(6));
							eList.add(episode);
						}
						else//tampoco encontro un ultimo episodio
						{
							Log.e(TAG, "no hay eisodio nuevo ni ultimo del id: "+idList.get(i));
							
						}
					}
				}
				//tenemos eList con los nuevos episodios, ahora hay que mandarlo a la actividad
			
			}
			else
			{
				//no hay shows en la bd
				Log.e(TAG, "no hay shows en la bd");
			}
			
			
			
		
			
			
			if(db.isOpen())
			{
				db.close();
			}
		
			
				Intent localIntent = new Intent(Constants.BROADCAST_FILL_LIST)
				 // Puts the extra into the Intent
				.putParcelableArrayListExtra(Constants.EXTENDED_DATA_FILL_LIST, eList);				
				 // Broadcasts the Intent to receivers in this app.
				 LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
			
			
			 
			 
		}catch(SQLiteException e)
		{
			Log.e(TAG, "error: "+e.getMessage());
		}

	}
	
	
	public  final class Constants
	{
		 public static final String BROADCAST_FILL_LIST ="com.ninetoseven.series.util.FillNewEpisodeListService.BROADCAST_FILL_LIST";
		 public static final String BROADCAST_FILL_LIST_ERROR ="com.ninetoseven.series.util.FillNewEpisodeListService.BROADCAST_FILL_LIST_ERROR";
		 public static final String EXTENDED_DATA_FILL_LIST ="com.ninetoseven.series.util.FillNewEpisodeListService.EXTENDED_DATA_FILL_LIST";
		 public static final String EXTENDED_DATA_ERROR_FILL_LIST ="com.ninetoseven.series.util.FillNewEpisodeListService.EXTENDED_DATA_ERROR_FILL_LIST";
	}
	
}

