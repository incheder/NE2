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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.ninetoseven.series.R;
import com.ninetoseven.series.adapter.NewEpisodeAdapter;
import com.ninetoseven.series.db.LastEpisodeContract.LastEntry;
import com.ninetoseven.series.db.NewEpisodeDbHelper;
import com.ninetoseven.series.db.NextEpisodeContract.NextEntry;
import com.ninetoseven.series.db.ShowContract.ShowEntry;
import com.ninetoseven.series.model.Episode;
import com.ninetoseven.series.util.DeleteShowAlertDialog;
import com.ninetoseven.series.util.SaveShowService;
import com.ninetoseven.series.util.Util;

public class MainActivity extends Activity {

	private static final String TAG = "NE2";
	private static final String FRAGMENT_TAG = "ne_list";
	private PlaceholderFragment placeholderFragment;
	private Toast toast;
	private AdView adView;
	private GoogleAnalytics ga;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "activity onCreate");
		setContentView(R.layout.activity_main);
		toast = new Toast(this);
		adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().
	    		addTestDevice("E0041374D0D56B134E69FEED0194E481").
	    		build();
	    adView.loadAd(adRequest);
	    ga =GoogleAnalytics.getInstance(getApplicationContext());//.newTracker("UA-52427110-1").enableAutoActivityTracking(true);
	    ga.enableAutoActivityReports(getApplication());
	    ga.newTracker("UA-52427110-1").enableAutoActivityTracking(true);
	    
		IntentFilter mDeleteIntentFilter = new IntentFilter(DeleteShowAlertDialog.BROADCAST_ACTION_DELETE);
		IntentFilter mSaveIntentFilter = new IntentFilter(SaveShowService.Constants.BROADCAST_ACTION);
		IntentFilter mErrorIntentFilter = new IntentFilter(SaveShowService.Constants.BROADCAST_ERROR);
		//IntentFilter mFillListIntentFilter = new IntentFilter(FillNewEpisodeListService.Constants.BROADCAST_FILL_LIST);
		placeholderFragment = new PlaceholderFragment();
		if (savedInstanceState == null) {
			
			getFragmentManager().beginTransaction()
					.add(R.id.container, placeholderFragment,FRAGMENT_TAG).commit();
			
		}
		DeleteReceiver mDeleteReceiver = new DeleteReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mDeleteReceiver, mDeleteIntentFilter);
		SaveReceiver mSaveReceiver = new SaveReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mSaveReceiver, mSaveIntentFilter);
		ErrorReceiver mErrorReceiver = new ErrorReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mErrorReceiver, mErrorIntentFilter);
		
		//FillListReceiver mFillListReceiver = new FillListReceiver();
		//LocalBroadcastManager.getInstance(this).registerReceiver(mFillListReceiver, mFillListIntentFilter);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adView.resume();
		Log.i(TAG, "activity onResume");
	//	Intent fillListService = new Intent(this,FillNewEpisodeListService.class);
		//startService(fillListService);
		
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
	public static class PlaceholderFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {

		
		private NewEpisodeAdapter adapter;
		private GridView gvNuevosEpisodios;
		private static ArrayList<Episode> eList;
		private static boolean actualiza=false;
		FillListTask fillListTask=null;
		
		public PlaceholderFragment() {
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			Log.i(TAG, "fragment onCreate");
			super.onCreate(savedInstanceState);
			
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			gvNuevosEpisodios = (GridView)rootView.findViewById(R.id.gvNuevosEpisodios);
			//fillList(20);
			gvNuevosEpisodios.setEmptyView(rootView.findViewById(R.id.emptyNew));
			gvNuevosEpisodios.setOnItemClickListener(this);
			gvNuevosEpisodios.setOnItemLongClickListener(this);
			if(savedInstanceState==null)
			{
				Log.d(TAG, "savedInstanceState null");
				eList = new ArrayList<Episode>();
				//setActualiza();
				if(fillListTask==null)
				{
					fillListTask = new FillListTask(getActivity());
					fillListTask.execute();
				}
				//new FillListTask(getActivity()).execute();
			}
			else
			{
				Log.d(TAG, "savedInstanceState NOT null");
				eList = savedInstanceState.getParcelableArrayList("eList");
				adapter = new NewEpisodeAdapter(getActivity(), eList);
				gvNuevosEpisodios.setAdapter(adapter);
			}
			return rootView;
		}
		
		@Override
		public void onResume() {
			Log.i(TAG, "fragment onResume");
			super.onResume();
			
			
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);
			outState.putParcelableArrayList("eList", eList);
			Log.d(TAG, "lista size: "+eList.size());
		}
		
		
		
		/*private void fillList(int num)
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
		}*/
		
		/*public void updateList(ArrayList<Episode> list)
		{
			Log.d(TAG, "updateList");
			
			//if(eList.isEmpty())
			{
				eList.clear();
				eList.addAll(list);
				adapter.notifyDataSetChanged();
			}
		}*/
		
		//public boolean isActualiza() {
			//return actualiza;
		//}

		public  void setActualiza() {
			actualiza = true;
			//new FillListTask(getActivity()).execute();
			if(fillListTask==null)
			{
				fillListTask = new FillListTask(getActivity());
				fillListTask.execute();
			}
		}



		public class FillListTask extends AsyncTask<Void, Void, List<Episode>>{
			
			Context context;
			
			private static final String TAG = "NE2";
			
			public FillListTask(Context context) {
			this.context=context;
			}
			
			@Override
			protected List<Episode> doInBackground(Void... params) {
				NewEpisodeDbHelper neDbHelper = new NewEpisodeDbHelper(context);
				try
				{
					List<Episode>episodeList = new ArrayList<Episode>();
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
							"image",
							"text_airtime"
							
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
								episode.setShowId(n.getString(0));
								episode.setShowName(n.getString(1));
								episode.setNumber(n.getString(2));
								episode.setTitle(n.getString(3));
								episode.setAirdate(n.getString(4));
								episode.setAirtime(n.getString(5));
								episode.setImage(n.getString(6));
								episode.setTextAirtime(n.getString(7));
								episodeList.add(episode);
							}
							else//si no encontro un nuevo episodio de este id
							{
								//intentamos buscar su ultimo episodio
								Cursor u = db.query(LastEntry.TABLE_NAME, projectionEpisode, selection, null, null, null, null);
								if(u.moveToFirst())
								{
									Episode episode = new Episode();
									episode.setShowId(u.getString(0));
									episode.setShowName(u.getString(1));
									episode.setNumber(u.getString(2));
									episode.setTitle(u.getString(3));
									episode.setAirdate(u.getString(4));
									episode.setAirtime(u.getString(5));
									episode.setImage(u.getString(6));
									episode.setTextAirtime(u.getString(7));
									episodeList.add(episode);
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
				
					return episodeList;
					
					 
					 
				}catch(SQLiteException e)
				{
					Log.e(TAG, "error: "+e.getMessage());
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<Episode> result) {
				super.onPostExecute(result);
				fillListTask=null;
				if(result!=null)
				{
					//eList=(ArrayList<Episode>) result;
					eList.clear();
					eList.addAll((ArrayList<Episode>) result);
					if(actualiza)
					{
						Log.d(TAG, "actualiza");
						adapter.notifyDataSetChanged();
						actualiza = false;
					}
					else
					{
						//adapter.notifyDataSetChanged();
						Log.d(TAG, "new episode adapter");
						adapter = new NewEpisodeAdapter(getActivity(), eList);
						gvNuevosEpisodios.setAdapter(adapter);
					}
					
				}
				
				
			}
		}



		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String showId = ((Episode)parent.getItemAtPosition(position)).getShowId();
			String image = ((Episode)parent.getItemAtPosition(position)).getImage();
			String showName = ((Episode)parent.getItemAtPosition(position)).getShowName();
			String airtime = ((Episode)parent.getItemAtPosition(position)).getAirtime();
			String title = ((Episode)parent.getItemAtPosition(position)).getTitle();
			Intent intent = new Intent(getActivity(),EpisodeListActivity.class);
			intent.putExtra("showid", showId);
			intent.putExtra("image", image);
			intent.putExtra("showName", showName);
			intent.putExtra("airtime", airtime);
			intent.putExtra("title", title);
			
			
			startActivity(intent);
		}
		
		@Override
		public boolean onItemLongClick(AdapterView<?> adapter, View view,
				int position, long id) {
			String showId = ((Episode)adapter.getItemAtPosition(position)).getShowId();
			Bundle args = new Bundle();
			
			args.putString("showId", showId);
			
			DeleteShowAlertDialog dialog = new DeleteShowAlertDialog();
			dialog.setArguments(args);
			
			dialog.show(getFragmentManager(), null);
			return true;
		}
		
		
		
		
	}

	private class DeleteReceiver extends BroadcastReceiver
	{
		

		public DeleteReceiver() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, "delete grid");
			placeholderFragment.setActualiza();
			
			
			//aqui hay que asignar un bandera ara indidicar si en onresume se debe actualizar
			//Intent fillListService = new Intent(getApplicationContext(),FillNewEpisodeListService.class);//esto deberia ir en onresume
			//startService(fillListService);
			
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
//			Toast.makeText(getApplicationContext(),
//					intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_STATUS),
//					Toast.LENGTH_SHORT).show();
			Util.showAToast(toast,intent.getStringExtra(intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_STATUS)),context);
			placeholderFragment.setActualiza();
			
			
			//aqui hay que asignar un bandera ara indidicar si en onresume se debe actualizar
			//Intent fillListService = new Intent(getApplicationContext(),FillNewEpisodeListService.class);//esto deberia ir en onresume
			//startService(fillListService);
			
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
			Util.showAToast(toast,intent.getStringExtra(intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_ERROR)),context);
		}
		
	}
	
	/*private class FillListReceiver extends BroadcastReceiver
	{
		

		public FillListReceiver() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, "fill_list");
			//if(placeholderFragment.isVisible())
			{
				Log.d(TAG,"fragment visible");
				ArrayList<Episode> eList = intent.getParcelableArrayListExtra(FillNewEpisodeListService.Constants.EXTENDED_DATA_FILL_LIST);
				//placeholderFragment.updateList(eList);
			}
			
			
		}
		
	}*/
}
