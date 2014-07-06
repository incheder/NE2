package com.ninetoseven.series.activitys;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ninetoseven.series.R;
import com.ninetoseven.series.db.LastEpisodeContract.LastEntry;
import com.ninetoseven.series.db.NewEpisodeDbHelper;
import com.ninetoseven.series.db.NextEpisodeContract.NextEntry;
import com.ninetoseven.series.db.ReminderContract.ReminderEntry;
import com.ninetoseven.series.db.ShowContract.ShowEntry;
import com.ninetoseven.series.model.Episode;
import com.ninetoseven.series.parser.EpisodeInfoParser;
import com.ninetoseven.series.util.Util;
import com.ninetoseven.series.util.VolleySingleton;

public class SplashActivity extends Activity{
	//private static final String TAG = "NE2";
	RequestQueue queue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		queue = VolleySingleton.getInstance(this).getRequestQueue();
		new SearchNewEpisodes(this).execute();
	}
	
	private class SearchNewEpisodes extends AsyncTask<Void, Void, List<String>> 
	{
		Context context;
		public SearchNewEpisodes(Context context) {
			this.context=context;
		}
		@Override
		protected List<String> doInBackground(Void... params) {
		
			NewEpisodeDbHelper neDbHelper = NewEpisodeDbHelper.getInstance(context);
			try
			{
				SQLiteDatabase db = neDbHelper.getReadableDatabase();
				String[] projection={"showid"};
				Cursor c = db.query(ShowEntry.TABLE_NAME, projection, null, null, null, null, null);
				if(c.moveToFirst())
				{
					List<String> idList = new ArrayList<String>();
					do
					{
						idList.add(c.getString(0));
					}
					while(c.moveToNext()); 
					return idList;
				}
				
				if(db.isOpen())
				{
					db.close();
				}
				
			}
			catch(SQLiteException e)
			{
				//Log.e(TAG, "error: "+e.getMessage());
			}
			return null;
			
			
		}
		
		@Override
		protected void onPostExecute(List<String> result)
		{
			super.onPostExecute(result);
			if(result!=null)
			{
				for (String id : result)
				{
					downloadEpisodes(id);
				}
				
			}
		
			startActivity(new Intent(getApplicationContext(),MainActivity.class));
			SplashActivity.this.finish();
			
		}
		
		
		
	}
	
	private void downloadEpisodes(final String id) {
		StringRequest request = new StringRequest("http://services.tvrage.com/myfeeds/episodeinfo.php?key=zCBJPmzNSndLMGeApDyD&sid="+id
				, new Listener<String>() {
			
			@Override
			public void onResponse(String response) {
				//manejamos la respuesta, parseandola
				
				if(response!=null)
				{
					 Episode[] arrayE= new EpisodeInfoParser(response).parse();
					
					 new SaveEpisode(id).execute(arrayE);
				
				}
				else
				{
					//Log.e(TAG, "response null");
				}
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
			//manjemos el error
				//Log.e(TAG, "volley error: "+error.getMessage());
				
			}
		});
		request.setRetryPolicy(Util.retryPolicy);
		queue.add(request);
		
	}
	
	private class SaveEpisode extends AsyncTask<Episode[], Void, Void>
	{
		String id;
		public SaveEpisode(String id) {
			this.id=id;
		}
		@Override
		protected Void doInBackground(Episode[]... params) {
			NewEpisodeDbHelper neDbHelper = NewEpisodeDbHelper.getInstance(getBaseContext());
			try
			{
				SQLiteDatabase db = neDbHelper.getWritableDatabase();
				Episode[] episodios = params[0];
				
				//añadimos el nuevo episodio si es que no es NULL
				if(episodios[1]!=null)
				{
					if(!exist(db, episodios[1].getNumber(),NextEntry.COLUMN_NAME_NUMBER , NextEntry.TABLE_NAME,id))// si el episodio no esta en la base
					{

						ContentValues nextEp = new ContentValues();
						
						nextEp.put(NextEntry.COLUMN_NAME_NUMBER, episodios[1].getNumber());
						nextEp.put(NextEntry.COLUMN_NAME_TITLE, episodios[1].getTitle());
						nextEp.put(NextEntry.COLUMN_NAME_AIRDATE, episodios[1].getAirdate());
						nextEp.put(NextEntry.COLUMN_NAME_AIRTIME, episodios[1].getAirtime());
						nextEp.put(NextEntry.COLUMN_NAME_TEXT_AIRTIME, episodios[1].getTextAirtime());
						
						db.update(NextEntry.TABLE_NAME, nextEp, "showid="+id, null);//solo hay un episodio nuevo por serie por eso se actualiza no se agrega
						
						if(getReminder(db,id).equals("1"))//a�adir recordatorio 
						{
							ContentValues reminder = new ContentValues();
							try {
								if(episodios[1].getAirtime()!=null && !episodios[1].getAirtime().equals(""))
								{
									Date date = Util.parseDate(episodios[1].getAirtime());
									long eventId = Util.createCalendarEvent(getApplicationContext(), date,episodios[1].getShowName(),episodios[1].getTitle());
									Util.addReminder(getApplicationContext(), eventId);
									
									reminder.put(ReminderEntry.COLUMN_NAME_STATUS,"1");
									reminder.put(ReminderEntry.COLUMN_NAME_AIRTIME, episodios[1].getAirtime());
									reminder.put(ReminderEntry.COLUMN_NAME_EVENT_ID, eventId);
									reminder.put(ReminderEntry.COLUMN_NAME_SHOW_ID, episodios[1].getShowId());
								}
								
								
							} catch (ParseException e) {
								//Log.e(TAG, e.getMessage());
							}
							
						}

						
						
					}
					
				}
				
				
				//a�adimos el ultimo episodio si es que no es null
				if(episodios[0]!=null)
				{
					if(!exist(db, episodios[0].getNumber(),LastEntry.COLUMN_NAME_NUMBER , LastEntry.TABLE_NAME,id))
					{//si no existe
						ContentValues lastEp = new ContentValues();
						
						lastEp.put(LastEntry.COLUMN_NAME_NUMBER, episodios[0].getNumber());
						lastEp.put(LastEntry.COLUMN_NAME_TITLE, episodios[0].getTitle());
						lastEp.put(LastEntry.COLUMN_NAME_AIRDATE, episodios[0].getAirdate());
						lastEp.put(LastEntry.COLUMN_NAME_AIRTIME, episodios[0].getAirtime());
						lastEp.put(LastEntry.COLUMN_NAME_IMAGE,episodios[0].getImage());
						lastEp.put(LastEntry.COLUMN_NAME_TEXT_AIRTIME,episodios[0].getTextAirtime());
						db.update(LastEntry.TABLE_NAME, lastEp, "showid="+id, null);
						
					}
				}
				
				
				
				
				
				if(db.isOpen())
				{
					db.close();
				}
				
				 
			} 
			
				catch(SQLiteException e)
			{
				//Log.e(TAG, "error: "+e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			
		}
	}
	
	private boolean exist(SQLiteDatabase db,String target, String column, String table,String showid)
	{
		String[] projection={
				column	
			};
		String selection=column+"='"+target+"'"+"AND "+NextEntry.COLUMN_NAME_SHOW_ID+" ='"+showid+"'";
		Cursor c = db.query(table, projection, selection, null, null, null, null);
		if(c.moveToFirst())
		{
			return true;
		}
		return false;
		
	}
	
	private String getReminder(SQLiteDatabase db,String id)
	{
		String[] projection={
				ReminderEntry.COLUMN_NAME_STATUS	
			};
		String selection=ReminderEntry.COLUMN_NAME_SHOW_ID+"='"+id+"'";
		Cursor c = db.query(ReminderEntry.TABLE_NAME, projection, selection, null, null, null, null);
		if(c.moveToFirst())
		{
			return c.getString(0);
		}
		return "1";
		
	}

}
