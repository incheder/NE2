package com.ninetoseven.series.activitys;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ninetoseven.series.R;
import com.ninetoseven.series.adapter.SearchResultAdapter;
import com.ninetoseven.series.model.Show;
import com.ninetoseven.series.parser.ShowSearchParser;
import com.ninetoseven.series.util.MySuggestionProvider;
import com.ninetoseven.series.util.VolleySingleton;

public class SearchActivity extends Activity{

	private static final String TAG = "NE2";
	Bundle args = new Bundle();
	
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
		if (savedInstanceState == null) {
			
			PlaceholderFragment placeHolderFragment = new PlaceholderFragment();
			placeHolderFragment.setArguments(args);
			getFragmentManager().beginTransaction()
					.add(R.id.container, placeHolderFragment).commit();
		}
	}
	
	/**
	 * Manejamos el intent con el query de la busqueda
	 * @param intent que tiene el string para hacer el query
	 */
	 private void handleIntent(Intent intent) {
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
		private List<Show> sList;
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
			sList = new ArrayList<Show>();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_search, container,
					false);
			gvSearch = (GridView)rootView.findViewById(R.id.gvBusqueda);
			gvSearch.setOnItemClickListener(this);
			pbLoading = (ProgressBar)rootView.findViewById(R.id.pbLoadingSearch);
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			fillList();
			
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
						sList= new ShowSearchParser(response).parse();
						if(sList!=null)
						{
							pbLoading.setVisibility(View.GONE);
							adapter = new SearchResultAdapter(getActivity(), sList);
							gvSearch.setAdapter(adapter);
							
						}
						else
						{
							//hubo un probblema al leer el show
							Log.e(TAG, "search list null");
						}
					}
					else
					{
						Log.e(TAG, "response null");
					}
					
				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
				//manjemos el error poniendo una imagen de un gato
					Log.e(TAG, "volley error: "+error.getMessage());
					
				}
			});
			queue.add(request);
			
		}
		
		
	}
}
