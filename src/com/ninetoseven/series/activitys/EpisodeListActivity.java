package com.ninetoseven.series.activitys;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.ninetoseven.series.R;
import com.ninetoseven.series.db.NewEpisodeDbHelper;
import com.ninetoseven.series.db.ReminderContract.ReminderEntry;
import com.ninetoseven.series.model.ListEp;
import com.ninetoseven.series.parser.EpisodeListParser;
import com.ninetoseven.series.util.ReminderAlertDialog;
import com.ninetoseven.series.util.Util;
import com.ninetoseven.series.util.VolleySingleton;

public class EpisodeListActivity extends ActionBarActivity{
	
	private static final String TAG = "NE2";
	private static final String RUTA = "http://services.tvrage.com/myfeeds/episode_list.php?key=zCBJPmzNSndLMGeApDyD&sid=";
	private MyAdapter adapter;
	private ViewPager viewPager;
	private RequestQueue queue;
	private static String imageShow;
	private ProgressBar pbLoading;
	private ListEp lista;
	private String airtime,id,showName,title;
	private AdView adView;
	private GoogleAnalytics ga;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queue = VolleySingleton.getInstance(this).getRequestQueue();
		setContentView(R.layout.episode_list_activity);
		adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().
	    		addTestDevice("E0041374D0D56B134E69FEED0194E481").
	    		build();
	    adView.loadAd(adRequest);
	    ga =GoogleAnalytics.getInstance(getApplicationContext());//.newTracker("UA-52427110-1").enableAutoActivityTracking(true);
	    ga.enableAutoActivityReports(getApplication());
	    ga.newTracker("UA-52427110-1").enableAutoActivityTracking(true);
		pbLoading = (ProgressBar)findViewById(R.id.pbLoadingList);
		viewPager = (ViewPager)findViewById(R.id.pager);
		id =getIntent().getExtras().getString("showid");
		imageShow =getIntent().getExtras().getString("image");
		showName = getIntent().getExtras().getString("showName");
		title = getIntent().getExtras().getString("title");
		airtime = getIntent().getExtras().getString("airtime");
		getSupportActionBar().setTitle(showName);
		if(savedInstanceState==null)
		{
			readShow(RUTA+id);
		}
		else
		{
			
			lista = savedInstanceState.getParcelable("lista");
			Log.d(TAG, "lista create: "+lista.getListaEpisodios().size());
			adapter = new MyAdapter(getSupportFragmentManager(),lista);
			viewPager.setAdapter(adapter);
			pbLoading.setVisibility(View.GONE);
		}
		
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		adView.resume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		adView.pause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		adView.destroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "lista save: "+lista.getListaEpisodios().size());
		outState.putParcelable("lista", lista);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.calendar, menu);	
		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int ide = item.getItemId();
		if (ide == R.id.calendar) {
			//try {
				//if(airtime!=null && !airtime.equals(""))
				{
					new GetReminder(airtime).execute(id);
					//Date date = Util.parseDate(airtime);
					//long eventId = Util.createCalendarEvent(this, date);
					//Util.addReminder(this, eventId);
				}
				
				
			//}
			//catch (ParseException e) {
				Log.d(TAG,"airtime: "+airtime);
			//}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void readShow(String url)
	{
		
		
		StringRequest request = new StringRequest(url, new Listener<String>() {
			
			@Override
			public void onResponse(String response) {
				//manejamos la respuesta, parseandola
				
				if(response!=null)
				{
//					Log.d(TAG, "response :"+response);
					lista = new EpisodeListParser(response).parse();
					Log.d(TAG, "lista size :"+lista.getListaEpisodios().size());
					adapter = new MyAdapter(getSupportFragmentManager(),lista);
					viewPager.setAdapter(adapter);
					pbLoading.setVisibility(View.GONE);
				}
				else
				{
					
				}
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
			//manjemos el error
				Log.e(TAG, "error volley: "+error.getMessage());
				
			}
		});
		request.setRetryPolicy(Util.retryPolicy);
		queue.add(request);
	}
	
	
	
	public static  class Content extends android.support.v4.app.Fragment
	{
		String season,imageEpisode,title,episode,airdate,summary;
		 NetworkImageView ivEpisode;
		
		static Content newInstance(String season,String imageEpisode,String title,String airdate,String summary,String episode)
		{
			Content content = new Content();
			Bundle args = new Bundle();
			args.putString("season", season);
			args.putString("imageEpisode", imageEpisode);
			args.putString("title", title);
			args.putString("airdate", airdate);
			args.putString("summary", summary);
			args.putString("episode", episode);
			content.setArguments(args);
			
			return content;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if(getArguments()!=null)
			{
				season = getArguments().getString("season");
				imageEpisode = getArguments().getString("imageEpisode");
				title = getArguments().getString("title");
				airdate = getArguments().getString("airdate");
				summary = getArguments().getString("summary");
				episode = getArguments().getString("episode");
			}
		}
		
		 @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
	            View v = inflater.inflate(R.layout.ep_list, container, false);
	            TextView tvTitle = (TextView)v.findViewById(R.id.tvEpisodeListTitle);
	            TextView tvSeason = (TextView)v.findViewById(R.id.tvEpisodeListSeason);
	            TextView tvAirdate = (TextView)v.findViewById(R.id.tvEpisodeListAirdate);
	            TextView tvSummary = (TextView)v.findViewById(R.id.tvEpisodeListSummary);
	            TextView tvEpisode = (TextView)v.findViewById(R.id.tvEpisodeListNumEpisode);
	            ivEpisode = (NetworkImageView)v.findViewById(R.id.ivEpisodeDescriptionImage); 
	            tvTitle.setText(title);
	            tvSeason.setText(season);
	            tvAirdate.setText(airdate);
	            tvSummary.setText(summary);
	            tvEpisode.setText(episode);
	          
	            return v;
	        }
		 
		 @Override
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			 ImageLoader imageLoader = VolleySingleton.getInstance(getActivity()).getImageLoader();
				Log.d(TAG, "image: "+imageEpisode);
				if(imageEpisode!=null)
				{
					ivEpisode.setImageUrl(imageEpisode, imageLoader);
				}
				else
				{
					ivEpisode.setImageUrl(imageShow, imageLoader);
				}
				
	            
		}
	}
	
	public static class MyAdapter extends FragmentStatePagerAdapter
	{

		private ListEp lista;
		public MyAdapter(FragmentManager fm,ListEp lista) {
			super(fm);
			this.lista = lista;
			
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			//Log.d(TAG,"position: "+lista.getListaEpisodios().get(position).getTitle());
			return Content.newInstance(lista.getListaEpisodios().get(position).getSeason(),
					lista.getListaEpisodios().get(position).getScreencap(),
					lista.getListaEpisodios().get(position).getTitle(),
					lista.getListaEpisodios().get(position).getAirdate(),
					lista.getListaEpisodios().get(position).getSummary(),
					lista.getListaEpisodios().get(position).getSeasonnum());
		}

		@Override
		public int getCount() {
			
			return lista.getListaEpisodios().size();
		}
		
	}
	
	private class GetReminder extends AsyncTask<String, Void, String>
	{

		String airtime,airtimeInDb,eventId;
		public GetReminder(String airtime)
		{
			this.airtime=airtime;
		}
		@Override
		protected String doInBackground(String... params) {
			NewEpisodeDbHelper neDbHelper = new NewEpisodeDbHelper(getBaseContext());
			SQLiteDatabase db = neDbHelper.getWritableDatabase();

			String[] projection={
					ReminderEntry.COLUMN_NAME_STATUS,
					ReminderEntry.COLUMN_NAME_AIRTIME,
					ReminderEntry.COLUMN_NAME_EVENT_ID
				};
			String selection=ReminderEntry.COLUMN_NAME_SHOW_ID+"='"+params[0]+"'";
			Cursor c = db.query(ReminderEntry.TABLE_NAME, projection, selection, null, null, null, null);
			if(c.moveToFirst())
			{
				eventId=c.getString(2);
				airtimeInDb=c.getString(1);
				return c.getString(0);
			}
			return "0";
		
		}
		
		@Override
		protected void onPostExecute(String result) {
		boolean[] checked = {false};
		Log.d(TAG, "result: "+result);
		if(result.equals("1"))
			{
				checked[0] = true;
			}
		Bundle args = new Bundle();
		args.putBooleanArray("checked", checked);
		args.putString("airtime", airtime);
		args.putString("showId", id);
		args.putString("showName", showName);
		args.putString("title", title);
		args.putString("eventId", eventId);
		args.putString("status", result);
		boolean sameAirtime=false;
		if(airtime!=null&&airtime.equals(airtimeInDb))
		{
			
			//si es el mismo airtime no hay que crear un nuevo evento
			sameAirtime=true;
			
		}
		args.putBoolean("sameAirtime", sameAirtime);
		ReminderAlertDialog dialog = new ReminderAlertDialog();
		dialog.setArguments(args);
		dialog.show(getSupportFragmentManager(), null);
		
			
		}
	
	
		
	
	}
}
