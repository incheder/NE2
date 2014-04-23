package com.ninetoseven.series.activitys;

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

import com.ninetoseven.series.R;
import com.ninetoseven.series.adapter.NewEpisodeAdapter;
import com.ninetoseven.series.adapter.SearchResultAdapter;
import com.ninetoseven.series.model.Episode;
import com.ninetoseven.series.model.Show;

public class SearchActivity extends ActionBarActivity{

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

		
		private SearchResultAdapter adapter;
		private GridView gvSearch;
		private List<Show> sList;
		public PlaceholderFragment() {
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
			fillList(20);
			adapter = new SearchResultAdapter(getActivity(), sList);
			gvSearch.setAdapter(adapter);
			
			
			
			
			
			return rootView;
		}
		
		private void fillList(int num)
		{
			Show show= null;
			for (int i = 0; i < num; i++) {
				 show = new Show();
				show.setShowName("Show Name fdfd dfdfd fdfdf dfdfdf dfdfdf dfdfdf dfdf d dfdfdf");
				sList.add(show);
			}
		}
		
		
	}
}
