package com.ninetoseven.series.activitys;

import java.util.ArrayList;
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
import com.ninetoseven.series.db.LastEpisodeContract.LastEntry;
import com.ninetoseven.series.db.NewEpisodeDbHelper;
import com.ninetoseven.series.db.NextEpisodeContract.NextEntry;
import com.ninetoseven.series.db.ShowContract.ShowEntry;
import com.ninetoseven.series.model.Episode;
import com.ninetoseven.series.parser.EpisodeInfoParser;
import com.ninetoseven.series.util.Util;
import com.ninetoseven.series.util.VolleySingleton;

public class SplashActivity extends Activity{
	private static final String TAG = "NE2";
	RequestQueue queue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		
			NewEpisodeDbHelper neDbHelper = new NewEpisodeDbHelper(getBaseContext());
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
				Log.e(TAG, "error: "+e.getMessage());
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
				startActivity(new Intent(getApplicationContext(),MainActivity.class));
			}
			
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
					Log.e(TAG, "response null");
				}
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
			//manjemos el error
				Log.e(TAG, "volley error: "+error.getMessage());
				
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
			NewEpisodeDbHelper neDbHelper = new NewEpisodeDbHelper(getBaseContext());
			try
			{
				SQLiteDatabase db = neDbHelper.getWritableDatabase();
				Episode[] episodios = params[0];
				
				//a�adimos el nuevo episodio si es que no es NULL
				if(episodios[1]!=null)
				{
					if(!exist(db, episodios[1].getNumber(),NextEntry.COLUMN_NAME_NUMBER , NextEntry.TABLE_NAME))
					{
						
					
					
						
						ContentValues nextEp = new ContentValues();
						
						nextEp.put(NextEntry.COLUMN_NAME_NUMBER, episodios[1].getNumber());
						nextEp.put(NextEntry.COLUMN_NAME_TITLE, episodios[1].getTitle());
						nextEp.put(NextEntry.COLUMN_NAME_AIRDATE, episodios[1].getAirdate());
						nextEp.put(NextEntry.COLUMN_NAME_AIRTIME, episodios[1].getAirtime());
						
					db.update(NextEntry.TABLE_NAME, nextEp, "showid="+id, null);
						{
							
						}
					}
					
				}
				
				
				//a�adimos el ultimo episodio si es que no es null
				if(episodios[0]!=null)
				{
					if(!exist(db, episodios[0].getNumber(),LastEntry.COLUMN_NAME_NUMBER , LastEntry.TABLE_NAME))
					{//si no existe
						ContentValues lastEp = new ContentValues();
						
						lastEp.put(LastEntry.COLUMN_NAME_NUMBER, episodios[0].getNumber());
						lastEp.put(LastEntry.COLUMN_NAME_TITLE, episodios[0].getTitle());
						lastEp.put(LastEntry.COLUMN_NAME_AIRDATE, episodios[0].getAirdate());
						lastEp.put(LastEntry.COLUMN_NAME_AIRTIME, episodios[0].getAirtime());
						lastEp.put(LastEntry.COLUMN_NAME_IMAGE,episodios[0].getImage());
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
				Log.e(TAG, "error: "+e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			
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

}
