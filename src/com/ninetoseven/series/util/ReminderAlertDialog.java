package com.ninetoseven.series.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.ninetoseven.series.R;
import com.ninetoseven.series.db.NewEpisodeDbHelper;
import com.ninetoseven.series.db.ReminderContract.ReminderEntry;

public class ReminderAlertDialog extends DialogFragment{
	
	boolean[] checked;
	boolean addReminder;
	String airtime,showId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checked = getArguments().getBooleanArray("checked");
		airtime = getArguments().getString("airtime");
		showId = getArguments().getString("showId");
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	    builder.setTitle(R.string.reminder)
	    // Specify the list array, the items to be selected by default (null for none),
	    // and the listener through which to receive callbacks when items are selected
	           .setMultiChoiceItems(R.array.reminder_array, checked,
	                      new DialogInterface.OnMultiChoiceClickListener() {
	        	   
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {
	                   if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                     //  mSelectedItems.add(which);
	                	   addReminder = true;
	                   } 
	                   //else if (mSelectedItems.contains(which)) 
	                   {
	                       // Else, if the item is already in the array, remove it 
	                     //  mSelectedItems.remove(Integer.valueOf(which));
	                   }
	               }
	           })
	    // Set the action buttons
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                 if(addReminder != checked[0])//si el estatus seleccionado es diferente del que estaba guardado
	                 {
	                	 
	                 }
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   
	               }
	           });

	    return builder.create();
	}
	
	private class AddReminder extends AsyncTask<String, Void, Integer>
	{
		Context context;
		public AddReminder(Context context) {
		this.context=context;
		}

		@Override
		protected Integer doInBackground(String... params) {//params 0 el airtime
			NewEpisodeDbHelper neDbHelper = new NewEpisodeDbHelper(context);
				SQLiteDatabase db = neDbHelper.getWritableDatabase();
			ContentValues reminder = new ContentValues();
			try {
				if(params[0]!=null && !params[0].equals(""))
				{
					Date date = Util.parseDate(params[0]);
					long eventId = Util.createCalendarEvent(context, date);
					Util.addReminder(context, eventId);
					if(addReminder)
					{
						reminder.put(ReminderEntry.COLUMN_NAME_STATUS,"1");
					}
					else
					{
						reminder.put(ReminderEntry.COLUMN_NAME_STATUS,"0");
					}
					
					reminder.put(ReminderEntry.COLUMN_NAME_AIRTIME, params[0]);
					reminder.put(ReminderEntry.COLUMN_NAME_EVENT_ID, eventId);
					reminder.put(ReminderEntry.COLUMN_NAME_SHOW_ID, params[0]);
					
					db.update(ReminderEntry.TABLE_NAME, reminder, "showid="+showId, null);
				}
				
				
			} catch (ParseException e) {
				//Log.e(TAG, e.getMessage());
			}
			return null;
		}
		
		
	}

}
