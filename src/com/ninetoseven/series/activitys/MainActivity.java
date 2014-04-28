package com.ninetoseven.series.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ninetoseven.series.R;
import com.ninetoseven.series.adapter.NewEpisodeAdapter;
import com.ninetoseven.series.model.Episode;
import com.ninetoseven.series.util.FillNewEpisodeListService;
import com.ninetoseven.series.util.SaveShowService;

public class MainActivity extends Activity {

	private static final String TAG = "NE2";
	private static final String FRAGMENT_TAG = "ne_list";
	private PlaceholderFragment placeholderFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		IntentFilter mSaveIntentFilter = new IntentFilter(SaveShowService.Constants.BROADCAST_ACTION);
		IntentFilter mErrorIntentFilter = new IntentFilter(SaveShowService.Constants.BROADCAST_ERROR);
		IntentFilter mFillListIntentFilter = new IntentFilter(FillNewEpisodeListService.Constants.BROADCAST_FILL_LIST);
		if (savedInstanceState == null) {
			placeholderFragment = new PlaceholderFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.container, placeholderFragment,FRAGMENT_TAG).commit();
		}
		SaveReceiver mSaveReceiver = new SaveReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mSaveReceiver, mSaveIntentFilter);
		ErrorReceiver mErrorReceiver = new ErrorReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mErrorReceiver, mErrorIntentFilter);
		
		FillListReceiver mFillListReceiver = new FillListReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mFillListReceiver, mFillListIntentFilter);
		Intent fillListService = new Intent(this,FillNewEpisodeListService.class);
		startService(fillListService);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		 // Associate searchable configuration with the SearchView
	    SearchManager searchManager =
	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView =
	            (SearchView) menu.findItem(R.id.search).getActionView();
	    //searchView.setSubmitButtonEnabled(true );
	    searchView.setSearchableInfo(
	            searchManager.getSearchableInfo( new ComponentName(this, SearchActivity.class)));
		return true;
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
		if (id == R.id.search) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		
		private NewEpisodeAdapter adapter;
		private GridView gvNuevosEpisodios;
		private List<Episode> eList;
		public PlaceholderFragment() {
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			eList = new ArrayList<Episode>();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			gvNuevosEpisodios = (GridView)rootView.findViewById(R.id.gvNuevosEpisodios);
			//fillList(20);
			gvNuevosEpisodios.setEmptyView(rootView.findViewById(R.id.emptyNew));
			adapter = new NewEpisodeAdapter(getActivity(), eList);
			gvNuevosEpisodios.setAdapter(adapter);
			
			return rootView;
		}
		
		
		
		private void fillList(int num)
		{
			Episode episode= null;
			for (int i = 0; i < num; i++) {
				episode = new Episode();
				episode.setShowName("Show Name");
				episode.setNumber("01x05");
				episode.setTitle("Episode Name");
				episode.setAirdate("2014-04-24");
				episode.setAirtime("Thursday at 10:00 pm");
				eList.add(episode);
			}
		}
		
		public void updateList(ArrayList<Episode> list)
		{
			Log.d(TAG, "updateList");
			if(eList.isEmpty())
			{
				eList.addAll(list);
			}
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
			Log.d(TAG, "actualiza grid");
			Toast.makeText(getApplicationContext(),
					intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_STATUS),
					Toast.LENGTH_SHORT).show();
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
			Toast.makeText(getApplicationContext(),
					intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_ERROR),
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private class FillListReceiver extends BroadcastReceiver
	{
		public FillListReceiver() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, "fill_list");
			if(placeholderFragment.isVisible())
			{
				Log.d(TAG,"fragment visible");
				placeholderFragment.updateList(intent.getParcelableArrayListExtra(FillNewEpisodeListService.Constants.EXTENDED_DATA_FILL_LIST));
			}
				
			//eList = intent.getParcelableArrayListExtra(FillNewEpisodeListService.Constants.EXTENDED_DATA_FILL_LIST);
			/*Toast.makeText(getApplicationContext(),
					intent.getStringExtra(FillNewEpisodeListService.Constants.EXTENDED_DATA_FILL_LIST),
					Toast.LENGTH_SHORT).show();*/
		}
		
	}
}
