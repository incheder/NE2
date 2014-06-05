package com.ninetoseven.series.activitys;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.ninetoseven.series.R;
import com.ninetoseven.series.db.NewEpisodeDbHelper;
import com.ninetoseven.series.model.Episode;
import com.ninetoseven.series.model.Show;
import com.ninetoseven.series.parser.EpisodeInfoParser;
import com.ninetoseven.series.parser.ShowInfoParser;
import com.ninetoseven.series.util.SaveShowService;
import com.ninetoseven.series.util.Util;
import com.ninetoseven.series.util.VolleySingleton;

public class ShowDescriptionActivity extends Activity {
	private static final String TAG = "NE2";
	public static final int TIMEOUT = 60000;
	Bundle args = new Bundle();
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		
		super.onNewIntent(intent);
		handleIntent(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handleIntent(getIntent());
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
	
	 private void handleIntent(Intent intent) {
		 
	      args.putString("id",intent.getStringExtra("id"));
	    }

	
	
	public static class PlaceholderFragment extends Fragment implements OnClickListener{

		
		private NetworkImageView ivShow;
		private TextView tvSeason,tvStart,tvStatus,tvDuration,tvNetwork,tvTime,tvSummary;
		private ProgressBar pbLoading;
		private LinearLayout llShowInfo;
		private Button btnSaveShow;
		private String id;
		String url="";
		RequestQueue queue;
		private Show show;
		public PlaceholderFragment() {
			
		}
		
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			if(getArguments()!=null)
			{
				id=getArguments().getString("id");
				url = "http://services.tvrage.com/myfeeds/showinfo.php?key=zCBJPmzNSndLMGeApDyD&sid="+id;
			}
			
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.show, container,
					false);
			llShowInfo = (LinearLayout)rootView.findViewById(R.id.llEpisodeInfo);
			llShowInfo.setVisibility(View.GONE);
			ivShow = (NetworkImageView)rootView.findViewById(R.id.ivShowDescriptionImage);
			tvDuration = (TextView)rootView.findViewById(R.id.tvDuration);
			tvNetwork = (TextView)rootView.findViewById(R.id.tvNetwork);
			tvSeason = (TextView)rootView.findViewById(R.id.tvSeason);
			tvStart = (TextView)rootView.findViewById(R.id.tvStartDate);
			tvStatus = (TextView)rootView.findViewById(R.id.tvStatus);
			tvSummary = (TextView)rootView.findViewById(R.id.tvSummary);
			tvTime = (TextView)rootView.findViewById(R.id.tvTime);
			pbLoading = (ProgressBar)rootView.findViewById(R.id.pbLoadingEpisode);
			btnSaveShow = (Button)rootView.findViewById(R.id.btnSaveShow);
			btnSaveShow.setOnClickListener(this);
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
			if(savedInstanceState==null)
			{
				readShow(url);
			}
			else
			{
				show=savedInstanceState.getParcelable("show");
				if(show!=null)
				{
					fillShowViews(show);
				}
				else
				{
					readShow(url);
				}
				
			}
			
			
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);
			if(show!=null)
			{
				outState.putParcelable("show", show);
			}
			
		}
		
		@Override
		public void onClick(View v) {
			if(v.getId()==btnSaveShow.getId())
			{
				saveShow(id);
				NewEpisodeDbHelper neDbHelper = new NewEpisodeDbHelper(getActivity());
				SQLiteDatabase db = neDbHelper.getWritableDatabase();
			}
			
		}
		
		private void fillShowViews(Show show)
		{
			pbLoading.setVisibility(View.GONE);
			llShowInfo.setVisibility(View.VISIBLE);
			tvDuration.setText(show.getRuntime()+" min.");
			tvNetwork.setText(show.getNetwork());
			tvSeason.setText(show.getSeasons());
			tvStart.setText(show.getStarted());
			tvStatus.setText(show.getStatus());
			tvSummary.setText(show.getSummary());
			tvTime.setText(show.getAirday()+" "+show.getAirtime());
			ImageLoader imageLoader = VolleySingleton.getInstance(getActivity()).getImageLoader();
			ivShow.setImageUrl(show.getImage(), imageLoader);
		}
		
		private void readShow(String url)
		{
			
			
			StringRequest request = new StringRequest(url, new Listener<String>() {
				
				@Override
				public void onResponse(String response) {
					//manejamos la respuesta, parseandola
					
					if(response!=null)
					{
						show= new ShowInfoParser(response).parse();
						if(show!=null)
						{
							Log.d(TAG, "showId: "+show.getId());
							fillShowViews(show);
							
						}
						else
						{
							//hubo un probblema al leer el show
						}
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
		
		private void saveShow(String id)
		{
			StringRequest request = new StringRequest("http://services.tvrage.com/myfeeds/episodeinfo.php?key=zCBJPmzNSndLMGeApDyD&sid="+id
					, new Listener<String>() {
				
				@Override
				public void onResponse(String response) {
					//manejamos la respuesta, parseandola
					
					if(response!=null)
					{
						 Episode[] arrayE= new EpisodeInfoParser(response).parse();
						show.setLatestepisode(arrayE[0]);//la posicion cero tiene el ultimo episodio
						show.setNextepisode(arrayE[1]);//la posicion 1 tiene el siguiente episodio
						
								//Log.d(TAG, "next episode: "+show.getNextepisode().getTitle());
								Intent saveShowService = new Intent(getActivity(),SaveShowService.class);
								//saveShowService.setData(Uri.parse("data"));
								saveShowService.putExtra("show", show);
								getActivity().startService(saveShowService);
						
					
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
	
	private class SaveReceiver extends BroadcastReceiver
	{
		

		public SaveReceiver() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(),
					intent.getStringExtra(SaveShowService.Constants.EXTENDED_DATA_STATUS),
					Toast.LENGTH_SHORT).show();
		}
		
	}
}
