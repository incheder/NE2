package com.ninetoseven.series.db;

import android.provider.BaseColumns;

public final class LastEpisodeContract {
	
	public LastEpisodeContract() {
	
	}
	
	public static abstract class LastEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "last_episode";
		public static final String COLUMN_NAME_SHOWNAME = "showname";
		public static final String COLUMN_NAME_SHOW_ID = "showid";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_AIRDATE = "airdate";
		public static final String COLUMN_NAME_AIRTIME = "airtime";
		public static final String COLUMN_NAME_IMAGE = "image";
		
	}

}
