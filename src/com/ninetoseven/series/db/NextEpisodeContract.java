package com.ninetoseven.series.db;

import android.provider.BaseColumns;

public final class NextEpisodeContract {
	
	public NextEpisodeContract() {
	
	}
	
	public static abstract class NextEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "next_episode";
		public static final String COLUMN_NAME_SHOW_ID = "showid";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_AIRDATE = "airdate";
		public static final String COLUMN_NAME_AIRTIME = "airtime";
		
	}

}
