package com.ninetoseven.series.activitys;

import java.util.Random;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninetoseven.series.R;
import com.ninetoseven.series.util.Util;

public class ShowDescriptionActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Util.screenSize(this);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
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
	public static class PlaceholderFragment extends Fragment {

		private ImageView ivShow;
		private TextView tvSeason,tvStart,tvStatus,tvDuration,tvNetwork,tvTime,tvSummary;
		Random r = new Random();
		public PlaceholderFragment() {
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
			ivShow = (ImageView)rootView.findViewById(R.id.ivShowDescriptionImage);
			tvDuration = (TextView)rootView.findViewById(R.id.tvDuration);
			tvNetwork = (TextView)rootView.findViewById(R.id.tvNetwork);
			tvSeason = (TextView)rootView.findViewById(R.id.tvSeason);
			tvStart = (TextView)rootView.findViewById(R.id.tvStartDate);
			tvStatus = (TextView)rootView.findViewById(R.id.tvStatus);
			tvSummary = (TextView)rootView.findViewById(R.id.tvSummary);
			tvTime = (TextView)rootView.findViewById(R.id.tvTime);
			
			tvDuration.setText("60 min");
			tvNetwork.setText("ABC");
			tvSeason.setText("5");
			tvStart.setText("22-05-2013");
			tvStatus.setText("live");
			tvSummary.setText("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,");
			tvTime.setText("Thursday at 10:00 pm");
			
			
			if(r.nextInt(2)==1)
			{
				ivShow.setImageResource(R.drawable.show);
			}
			else
			{
				ivShow.setImageResource(R.drawable.show2);
			}
			
			
			return rootView;
		}
		
		
	}
}
