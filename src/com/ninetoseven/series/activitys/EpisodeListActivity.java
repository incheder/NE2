package com.ninetoseven.series.activitys;

import java.text.ParseException;
import java.util.Date;

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
import com.ninetoseven.series.R;
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
	private String airtime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queue = VolleySingleton.getInstance(this).getRequestQueue();
		setContentView(R.layout.episode_list_activity);
		pbLoading = (ProgressBar)findViewById(R.id.pbLoadingList);
		viewPager = (ViewPager)findViewById(R.id.pager);
		String id =getIntent().getExtras().getString("showid");
		imageShow =getIntent().getExtras().getString("image");
		String showName = getIntent().getExtras().getString("showName");
		airtime = getIntent().getExtras().getString("airtime");
		getSupportActionBar().setTitle(showName);
		readShow(RUTA+id);
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.calendar, menu);	
		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.calendar) {
			try {
				if(airtime!=null && !airtime.equals(""))
				{
					new ReminderAlertDialog().show(getSupportFragmentManager(),null);
					Date date = Util.parseDate(airtime);
					long eventId = Util.createCalendarEvent(this, date);
					Util.addReminder(this, eventId);
				}
				
				
			} catch (ParseException e) {
				Log.e(TAG, e.getMessage());
			}
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
	
	
		
	
}
