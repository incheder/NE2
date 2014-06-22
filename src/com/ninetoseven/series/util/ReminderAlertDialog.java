package com.ninetoseven.series.util;

import java.text.ParseException;
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
	
	private final static String TAG = "NE2";
	
	boolean[] checked;
	boolean addReminder,sameAirtime;
	String airtime,showId,showName,title,eventId,status;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checked = getArguments().getBooleanArray("checked");
		airtime = getArguments().getString("airtime");
		showId = getArguments().getString("showId");
		showName = getArguments().getString("showName");
		title = getArguments().getString("title");
		eventId = getArguments().getString("eventId");
		status = getArguments().getString("status");
		sameAirtime=getArguments().getBoolean("sameAirtime");
		addReminder=checked[0];
		
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    //ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
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
	                   else
	                   //else if (mSelectedItems.contains(which)) 
	                   {
	                	   addReminder = false;
	                       // Else, if the item is already in the array, remove it 
	                     //  mSelectedItems.remove(Integer.valueOf(which));
	                   }
	               }
	           })
	    // Set the action buttons
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
//	                 
//	            	   Log.d(TAG, "addReminder: "+addReminder);
//	            	   Log.d(TAG, "checked: "+checked[0]);
//	            	   if(addReminder != checked[0])//si el estatus seleccionado es diferente del que estaba guardado
	                 {
	                	 new AddReminder(getActivity()).execute(airtime);
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
			Log.d(TAG, "params0: "+params[0]);
			Log.d(TAG, "addReminder: "+addReminder);
			Log.d(TAG, "showId: "+ showId);
			try {
				if(params[0]!=null && !params[0].equals(""))//si no esta vacio el airtime
				{
					
					
					
					if(addReminder)
					{
						reminder.put(ReminderEntry.COLUMN_NAME_STATUS,"1");
						if(!sameAirtime)//si el airtime actual es diferente al airtime de la bd
						{
							Date date = Util.parseDate(params[0]);
							long eventId = Util.createCalendarEvent(context, date,showName,title);
							Util.addReminder(context, eventId);
							reminder.put(ReminderEntry.COLUMN_NAME_EVENT_ID, eventId);
						}
						else if(status.equals("0"))//si el airtime es igual pero no habia eventos
						{
							Date date = Util.parseDate(params[0]);
							long eventId = Util.createCalendarEvent(context, date,showName,title);
							Util.addReminder(context, eventId);
							reminder.put(ReminderEntry.COLUMN_NAME_EVENT_ID, eventId);
						}
					}
					else
					{
						reminder.put(ReminderEntry.COLUMN_NAME_STATUS,"0");
						if(eventId!=null)
						{
							Util.deleteEvent(context, Long.parseLong(eventId));
						}
						
					}
					
					reminder.put(ReminderEntry.COLUMN_NAME_AIRTIME, params[0]);
					
					reminder.put(ReminderEntry.COLUMN_NAME_SHOW_ID, showId);
					
					if(db.update(ReminderEntry.TABLE_NAME, reminder, "showid="+showId, null)!=1)
					{
						db.insert(ReminderEntry.TABLE_NAME, null, reminder);
					}
				}
				else
				{
					if(addReminder)
					{
						reminder.put(ReminderEntry.COLUMN_NAME_STATUS,"1");
					}
					else
					{
						reminder.put(ReminderEntry.COLUMN_NAME_STATUS,"0");
						if(eventId!=null)
						{
							Util.deleteEvent(context, Long.parseLong(eventId));
						}
					}
					reminder.put(ReminderEntry.COLUMN_NAME_SHOW_ID, showId);
					if(db.update(ReminderEntry.TABLE_NAME, reminder, "showid="+showId, null)!=1)
					{
						
						db.insert(ReminderEntry.TABLE_NAME, null, reminder);
					}
				}
				
				if(db.isOpen())
				{
					db.close();
				}
			} catch (ParseException e) {
				//Log.e(TAG, e.getMessage());
			}
			return null;
		}
		
		
	}

}
