package com.ninetoseven.series.db;

import android.provider.BaseColumns;

public final class ReminderContract {
	
	public ReminderContract() {
	
	}
	
	public static abstract class ReminderEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "reminder";
		public static final String COLUMN_NAME_SHOW_ID = "showid";
		public static final String COLUMN_NAME_AIRTIME = "airtime";
		public static final String COLUMN_NAME_EVENT_ID = "eventid";
		public static final String COLUMN_NAME_STATUS = "status";
		
	}

}
