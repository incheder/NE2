package com.ninetoseven.series.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ninetoseven.series.db.LastEpisodeContract.LastEntry;
import com.ninetoseven.series.db.NextEpisodeContract.NextEntry;
import com.ninetoseven.series.db.ReminderContract.ReminderEntry;
import com.ninetoseven.series.db.ShowContract.ShowEntry;

public class NewEpisodeDbHelper extends SQLiteOpenHelper{
	static NewEpisodeDbHelper instance = null;
	
	private static final String DATABASE_NAME = "NewEpisode.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMA_SEP = ",";
	private static final String SQL_CREATE_SHOW = "CREATE TABLE "+ShowEntry.TABLE_NAME+" ("+ShowEntry._ID+" INTEGER PRIMARY KEY,"+
	ShowEntry.COLUMN_NAME_SHOW_ID + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_SHOWNAME + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_LINK + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_SEASONS + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_IMAGE + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_STARTED + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_ENDED + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_STATUS + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_SUMARY + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_RUNTIME + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_NETWORK + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_AIRTIME + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_AIRDAY + TEXT_TYPE + COMA_SEP+
	ShowEntry.COLUMN_NAME_TIMEZONE + TEXT_TYPE +" );";
	
	private static final String SQL_CREATE_NEXT_EPISODE = "CREATE TABLE "+NextEntry.TABLE_NAME+" ("+NextEntry._ID+" INTEGER PRIMARY KEY,"+
			NextEntry.COLUMN_NAME_TEXT_AIRTIME + TEXT_TYPE + COMA_SEP+
			NextEntry.COLUMN_NAME_SHOWNAME + TEXT_TYPE + COMA_SEP+
			NextEntry.COLUMN_NAME_SHOW_ID + TEXT_TYPE + COMA_SEP+
			NextEntry.COLUMN_NAME_NUMBER + TEXT_TYPE + COMA_SEP+
			NextEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMA_SEP+
			NextEntry.COLUMN_NAME_AIRDATE + TEXT_TYPE + COMA_SEP+
			NextEntry.COLUMN_NAME_IMAGE + TEXT_TYPE + COMA_SEP+
			NextEntry.COLUMN_NAME_AIRTIME + TEXT_TYPE +" );";
	
	private static final String SQL_CREATE_LAST_EPISODE = "CREATE TABLE "+LastEntry.TABLE_NAME+" ("+LastEntry._ID+" INTEGER PRIMARY KEY,"+
			LastEntry.COLUMN_NAME_TEXT_AIRTIME + TEXT_TYPE + COMA_SEP+
			LastEntry.COLUMN_NAME_SHOWNAME + TEXT_TYPE + COMA_SEP+
			LastEntry.COLUMN_NAME_SHOW_ID + TEXT_TYPE + COMA_SEP+
			LastEntry.COLUMN_NAME_NUMBER + TEXT_TYPE + COMA_SEP+
			LastEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMA_SEP+
			LastEntry.COLUMN_NAME_AIRDATE + TEXT_TYPE + COMA_SEP+
			LastEntry.COLUMN_NAME_IMAGE + TEXT_TYPE + COMA_SEP+
			LastEntry.COLUMN_NAME_AIRTIME + TEXT_TYPE +" );";
	
	private static final String SQL_CREATE_REMINDER = "CREATE TABLE "+ReminderEntry.TABLE_NAME+" ("+ReminderEntry._ID+" INTEGER PRIMARY KEY,"+
			ReminderEntry.COLUMN_NAME_SHOW_ID + TEXT_TYPE + COMA_SEP+
			ReminderEntry.COLUMN_NAME_STATUS + TEXT_TYPE + COMA_SEP+
			ReminderEntry.COLUMN_NAME_EVENT_ID + TEXT_TYPE + COMA_SEP+
			ReminderEntry.COLUMN_NAME_AIRTIME + TEXT_TYPE +" );";
	
	private static final String SQL_DELETE_SHOW =
		    "DROP TABLE IF EXISTS " + ShowEntry.TABLE_NAME;
	private static final String SQL_DELETE_NEXT_EPISODE =
		    "DROP TABLE IF EXISTS " + NextEntry.TABLE_NAME;
	private static final String SQL_DELETE_LAST_EPISODE =
		    "DROP TABLE IF EXISTS " + LastEntry.TABLE_NAME;
	private static final String SQL_DELETE_REMINDER =
		    "DROP TABLE IF EXISTS " + ReminderEntry.TABLE_NAME;
	

	public NewEpisodeDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(SQL_CREATE_SHOW);
		db.execSQL(SQL_CREATE_NEXT_EPISODE);
		db.execSQL(SQL_CREATE_LAST_EPISODE);
		db.execSQL(SQL_CREATE_REMINDER);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		  db.execSQL(SQL_DELETE_SHOW);
		  db.execSQL(SQL_DELETE_NEXT_EPISODE);
		  db.execSQL(SQL_DELETE_LAST_EPISODE);
		  db.execSQL(SQL_DELETE_REMINDER);
	      onCreate(db);
	}
	
	
	 public static NewEpisodeDbHelper getInstance(Context context) {
	        if(instance == null) {
	            instance = new NewEpisodeDbHelper(context);
	        }
	        return instance;
	    }
	

}
