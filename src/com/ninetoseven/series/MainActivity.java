package com.ninetoseven.series;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ninetoseven.series.adapter.NewEpisodeAdapter;
import com.ninetoseven.series.model.Episode;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
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
	}

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
			fillList(20);
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
				episode.setEpisode("01x05");
				episode.setEpisodeName("Episode Name");
				episode.setDate("2014-04-24");
				episode.setTime("Thursday at 10:00 pm");
				eList.add(episode);
			}
		}
	}

}
