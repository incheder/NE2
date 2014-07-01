package com.ninetoseven.series.activitys;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ninetoseven.series.R;
import com.ninetoseven.series.adapter.SearchResultAdapter;
import com.ninetoseven.series.model.Show;
import com.ninetoseven.series.parser.ShowSearchParser;
import com.ninetoseven.series.util.MySuggestionProvider;
import com.ninetoseven.series.util.SaveShowService;
import com.ninetoseven.series.util.Util;
import com.ninetoseven.series.util.VolleySingleton;

public class SearchActivity extends Activity{

	private static final String TAG = "NE2";
	private Bundle args = new Bundle();
	private Toast toast;
	private AdView adView;
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		handleIntent(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		handleIntent(getIntent());
		adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().
	    		addTestDevice("E0041374D0D56B134E69FEED0194E481").
	    		build();
	    adView.loadAd(adRequest);
		IntentFilter mSaveIntentFilter = new IntentFilter(SaveShowService.Constants.BROADCAST_ACTION);
		IntentFilter mErrorIntentFilter = new IntentFilter(SaveShowService.Constants.BROADCAST_ERROR);
		
		if (savedInstanceState == null) {
			
			PlaceholderFragment placeHolderFragment = new PlaceholderFragment();
			placeHolderFragment.setArguments(args);
			getFragmentManager().beginTransaction()
					.add(R.id.container, placeHolderFragment).commit();
		}
		
		ErrorReceiver mErrorReceiver = new ErrorReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mErrorReceiver, mErrorIntentFilter);
		
		SaveReceiver mSaveReceiver = new SaveReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mSaveReceiver, mSaveIntentFilter);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adView.resume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		adView.pause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adView.destroy();
	}
	
	/**
	 * Manejamos el intent con el query de la busqueda
	 * @param intent que tiene el string para hacer el query
	 */
	 private void handleIntent(Intent intent) {
		 toast = new Toast(getApplicationContext());
		 String query="";
	        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	              
	            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
	                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
	            suggestions.saveRecentQuery(intent.getStringExtra(SearchManager.QUERY), null);
	           
	            try {
	            	
					query = "http://services.tvrage.com/feeds/search.php?show="+URLEncoder.encode(intent.getStringExtra(SearchManager.QUERY),"utf-8");
					 args.putString("query", query);
				   
				} catch (UnsupportedEncodingException e) {
					
					Log.e(TAG, "error:"+e.getMessage());
				}
	        }
	    }

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		 SearchManager searchManager =
		           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		    SearchView searchView =
		            (SearchView) menu.findItem(R.id.search).getActionView();
		    searchView.setSearchableInfo(
		            searchManager.getSearchableInfo(getComponentName()));
		return true;
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnItemClickListener {

		
		private SearchResultAdapter adapter;
		private GridView gvSearch;
		private ProgressBar pbLoading;
		private ArrayList<Show> sList;
		private TextView empty;
		private String query;
		public PlaceholderFragment() {
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			if(getArguments()!=null)
			{
				query = getArguments().getString("query");
			}
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			//sList = new ArrayList<Show>();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_search, container,
					false);
			gvSearch = (GridView)rootView.findViewById(R.id.gvBusqueda);
			gvSearch.setOnItemClickListener(this);
			empty = (TextView)rootView.findViewById(R.id.emptySearch);
			//gvSearch.setEmptyView(empty);
			pbLoading = (ProgressBar)rootView.findViewById(R.id.pbLoadingSearch);
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			if(savedInstanceState==null)
			{
				fillList();
			}
			else
			{
				sList=savedInstanceState.getParcelableArrayList("sList");
				if(sList!=null)
				{
					
					adapter = new SearchResultAdapter(getActivity(), sList);
					gvSearch.setAdapter(adapter);
					
					pbLoading.setVisibility(View.GONE);
				}
				else
				{
					fillList();
				}
			}
			
			
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);
		
			if(sList!=null)
			{
				outState.putParcelableArrayList("sList", sList);
			}
			
		}
		
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long id) {
			Intent showInfo = new Intent(getActivity(),ShowDescriptionActivity.class);
			showInfo.putExtra("id", String.valueOf(adapter.getItemIdAtPosition(position)));
			startActivity(showInfo);
			
		}
		
		
		
		private void fillList()
		{
			
			RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
			StringRequest request = new StringRequest(query, new Listener<String>() {
				
				@Override
				public void onResponse(String response) {
					//manejamos la respuesta, parseandola
					
					if(response!=null)
					{
						sList= (ArrayList<Show>) new ShowSearchParser(response).parse();
						if(sList!=null && sList.size()!=0)
						{
							empty.setVisibility(View.GONE);
							pbLoading.setVisibility(View.GONE);
							adapter = new SearchResultAdapter(getActivity(), sList);
							gvSearch.setAdapter(adapter);
							
						}
						else
						{
							//hubo un probblema al leer el show
							pbLoading.setVisibility(View.GONE);
							empty.setVisibility(View.VISIBLE);
							Log.e(TAG, "search list null");
						}
					}
					else
					{
						pbLoading.setVisibility(View.GONE);
						empty.setVisibility(View.VISIBLE);
						Log.e(TAG, "response null");
					}
					
				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
				//manjemos el error poniendo una imagen de un gato
					pbLoading.setVisibility(View.GONE);
					empty.setVisibility(View.VISIBLE);
					Log.e(TAG, "volley error: "+error.getMessage());
					
				}
			});
			request.setRetryPolicy(Util.retryPolicy);
			queue.add(request);
			
		}
		
		
	}
	
	private class ErrorReceiver extends BroadcastReceiver
	{
		

		public ErrorReceiver() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, "error al guardar");
//			Toast.makeText(getApplicationContext(),
//					intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_ERROR),
//					Toast.LENGTH_SHORT).show();
			Util.showAToast(toast,intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_ERROR), context);
		}
		
	}
	
	private class SaveReceiver extends BroadcastReceiver
	{
		

		public SaveReceiver() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(),
//					intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_STATUS),
//					Toast.LENGTH_SHORT).show();
			Util.showAToast(toast, intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_STATUS), context);
		}
		
	}
}
