package com.beenthere.util;

public class Constants {

	// Preferences
	public static final String 		PREFS_NAME 			= "been_there_prefs_file";
	
	public static final double 		NO_GEO_TAG 			= 1000.0;
	public static final int 		NO_DATE				= -7;
	public static final int    		NO_SD_ACCESS 		= 1;
	
	// Albums
	public static final int			IMG_DIM 			= 60;

	// Images
	public static final int			MICRO_DIM       	= 96;
	public static final	int			MINI_LONG_DIM   	= 512;
	public static final int			MINI_SHORT_DIM  	= 384;
	public static final String		JPG 				= ".jpg";
	public static final String		KMZ 				= ".kmz";
	
	public static final long		PHONE_GROUP_ID  	= 0;
	
	public static final int 		PREMIUM		 		= 1 << 0;
	public static final int 		NO_VIEW_ALBUMS 		= 1 << 1;
	public static final int 		NO_CREATE_ALBUM		= 1 << 2;
	public static final int 		NO_STATUS_BAR		= 1 << 3;
	

	public static final int			GALLERY_IMG_SIZE_DEFAULT	= 90;
	public static final int			GALLERY_IMG_SIZE_MIN 		= 60;
	public static final int			GALLERY_IMG_SIZE_MAX 		= 200;
	public static final int			HIDE_TIME_THRESHOLD 		= 800;
	public static final int			RESIZE_DELTA_THRESHOLD 		= 20;
	public static final int			RESIZE_TIME_THRESHOLD 		= 300;
	public static final int			SLIDE_SPEED_THRESHOLD 		= 15;
	public static final int			SLIDE_DELTA_THRESHOLD 		= 50;

}
