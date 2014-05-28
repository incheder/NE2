package com.ninetoseven.series.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ninetoseven.series.R;
import com.ninetoseven.series.model.ListEp;
import com.ninetoseven.series.parser.EpisodeListParser;
import com.ninetoseven.series.util.Util;
import com.ninetoseven.series.util.VolleySingleton;

public class EpisodeListActivity extends ActionBarActivity{
	
	private static final String TAG = "NE2";
	private static final String RUTA = "http://services.tvrage.com/myfeeds/episode_list.php?key=zCBJPmzNSndLMGeApDyD&sid=";
	private MyAdapter adapter;
	private ViewPager viewPager;
	private RequestQueue queue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queue = VolleySingleton.getInstance(this).getRequestQueue();
		setContentView(R.layout.episode_list_activity);
		viewPager = (ViewPager)findViewById(R.id.pager);
		String id =getIntent().getExtras().getString("showid");
		readShow(RUTA+id);
		
		
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
					ListEp lista = new EpisodeListParser(response).parse();
					Log.d(TAG, "lista size :"+lista.getListaEpisodios().size());
					adapter = new MyAdapter(getSupportFragmentManager(),lista);
					viewPager.setAdapter(adapter);
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
		String name;
		
		static Content newInstance(String name)
		{
			Content content = new Content();
			Bundle args = new Bundle();
			args.putString("name", name);
			content.setArguments(args);
			
			return content;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if(getArguments()!=null)
			{
				name = getArguments().getString("name");
			}
		}
		
		 @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
	            View v = inflater.inflate(R.layout.episode_list_item, container, false);
	            TextView tvcontenido = (TextView)v.findViewById(R.id.tvContent);
	            tvcontenido.setText(name);
	            
	          
	            return v;
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
			Log.d(TAG,"position: "+lista.getListaEpisodios().get(position).getTitle());
			return Content.newInstance(lista.getListaEpisodios().get(position).getTitle());
		}

		@Override
		public int getCount() {
			
			return lista.getListaEpisodios().size();
		}
		
	}
	
		
	
}
