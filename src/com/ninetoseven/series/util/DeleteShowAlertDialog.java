package com.ninetoseven.series.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ninetoseven.series.R;
import com.ninetoseven.series.db.LastEpisodeContract.LastEntry;
import com.ninetoseven.series.db.NewEpisodeDbHelper;
import com.ninetoseven.series.db.NextEpisodeContract.NextEntry;
import com.ninetoseven.series.db.ReminderContract.ReminderEntry;
import com.ninetoseven.series.db.ShowContract.ShowEntry;

public class DeleteShowAlertDialog extends DialogFragment{
	
	//private final static String TAG = "NE2";
	public static final String BROADCAST_ACTION_DELETE ="com.ninetoseven.series.util.SaveShowService.DELETE";
	
	String showId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		showId = getArguments().getString("showId");
		
		
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    //ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	   // builder.setTitle(R.string.reminder)
	    // Specify the list array, the items to be selected by default (null for none),
	    // and the listener through which to receive callbacks when items are selected
	           builder.setMessage(R.string.delete)
	    // Set the action buttons
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   new DeleteShow(getActivity()).execute(showId);
//	            	   
	               }
	           })
	           .setNegativeButton(R.string.cancel, null);

	    return builder.create();
	}
	
	private class DeleteShow extends AsyncTask<String, Void, Integer>
	{
		Context context;
		public DeleteShow(Context context) {
		this.context=context;
		}

		@Override
		protected Integer doInBackground(String... params) {//params 0 el id
			NewEpisodeDbHelper neDbHelper = NewEpisodeDbHelper.getInstance(context);
				SQLiteDatabase db = neDbHelper.getWritableDatabase();
			String clause = "showid='"+params[0]+"'";
			try {
				
				db.delete(ShowEntry.TABLE_NAME, clause, null);
				db.delete(NextEntry.TABLE_NAME, clause, null);
				db.delete(LastEntry.TABLE_NAME, clause, null);
				String[] projection={
						ReminderEntry.COLUMN_NAME_EVENT_ID,
						
					};
				
				Cursor c = db.query(ReminderEntry.TABLE_NAME, projection, clause, null, null, null, null);
				if(c.moveToFirst())
				{
					Util.deleteEvent(context, Long.parseLong(c.getString(0)));
				}
				db.delete(ReminderEntry.TABLE_NAME, clause, null);
				
				if(db.isOpen())
				{
					db.close();
				}
			} catch (Exception e) {
				//Log.e(TAG, e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
			Intent localIntent = new Intent(BROADCAST_ACTION_DELETE);
			 LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(localIntent);
		}
		
		
	}

}
