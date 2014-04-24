package com.ninetoseven.series.activitys;

import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ninetoseven.series.model.Show;
import com.ninetoseven.series.parser.ShowInfoParser;
import com.ninetoseven.series.util.Util;
import com.ninetoseven.series.util.VolleySingleton;

public class ShowDescriptionActivity extends Activity {
	private static final String TAG = "NE2";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Util.screenSize(this);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
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
	public static class PlaceholderFragment extends Fragment{

		
		private NetworkImageView ivShow;
		private TextView tvSeason,tvStart,tvStatus,tvDuration,tvNetwork,tvTime,tvSummary;
		private ProgressBar pbLoading;
		private LinearLayout llShowInfo;
		Random r = new Random();
		
		String url;
		
		public PlaceholderFragment() {
			
		}
		
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			url = "http://services.tvrage.com/myfeeds/showinfo.php?key=zCBJPmzNSndLMGeApDyD&sid=3741";
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
			llShowInfo = (LinearLayout)rootView.findViewById(R.id.llShowInfo);
			llShowInfo.setVisibility(View.GONE);
			ivShow = (NetworkImageView)rootView.findViewById(R.id.ivShowDescriptionImage);
			tvDuration = (TextView)rootView.findViewById(R.id.tvDuration);
			tvNetwork = (TextView)rootView.findViewById(R.id.tvNetwork);
			tvSeason = (TextView)rootView.findViewById(R.id.tvSeason);
			tvStart = (TextView)rootView.findViewById(R.id.tvStartDate);
			tvStatus = (TextView)rootView.findViewById(R.id.tvStatus);
			tvSummary = (TextView)rootView.findViewById(R.id.tvSummary);
			tvTime = (TextView)rootView.findViewById(R.id.tvTime);
			pbLoading = (ProgressBar)rootView.findViewById(R.id.pbLoadingShow);
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			readShow(url);
			
		}
		
		private void readShow(String url)
		{
			
			RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
			StringRequest request = new StringRequest(url, new Listener<String>() {
				
				@Override
				public void onResponse(String response) {
					//manejamos la respuesta, parseandola
					
					if(response!=null)
					{
						Show show= new ShowInfoParser(response).parse();
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
					Log.e(TAG, "error: "+error.getMessage());
					
				}
			});
			queue.add(request);
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
		
		
	}
}
