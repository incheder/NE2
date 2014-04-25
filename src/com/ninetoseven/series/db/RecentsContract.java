package com.ninetoseven.series.db;

import android.provider.BaseColumns;

public final class RecentsContract {
	
	public RecentsContract() {
	
	}
	
	public static abstract class ShowEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "shows";
		public static final String COLUMN_NAME_SHOW_ID = "showid";
		public static final String COLUMN_NAME_SHOWNAME = "name";
		public static final String COLUMN_NAME_LINK = "link";
		public static final String COLUMN_NAME_SEASONS = "seasons";
		public static final String COLUMN_NAME_IMAGE = "image";
		public static final String COLUMN_NAME_STARTED = "started";
		public static final String COLUMN_NAME_ENDED = "ended";
		public static final String COLUMN_NAME_STATUS = "status";
		public static final String COLUMN_NAME_SUMARY = "sumary";
		public static final String COLUMN_NAME_RUNTIME = "runtime";
		public static final String COLUMN_NAME_NETWORK = "network";
		public static final String COLUMN_NAME_AIRTIME = "airtime";
		public static final String COLUMN_NAME_AIRDAY = "airday";
		public static final String COLUMN_NAME_TIMEZONE = "timezone";
		
	}

}
