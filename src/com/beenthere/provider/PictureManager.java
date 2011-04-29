package com.beenthere.provider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.beenthere.util.Constants;
import com.beenthere.util.ExifInterface;

public class PictureManager {
	
	private static final Uri	  MEDIA_URI		 = Media.EXTERNAL_CONTENT_URI;
	private static final String[] IMG_PROJ 		 = {Media._ID,Media.DATA,Media.LATITUDE,Media.LONGITUDE,Media.DATE_TAKEN };
	private static final String   IMG_SORT_ORDER = Media.DATE_TAKEN + " ASC";
	
	private static final Uri	  THUMB_URI      = Thumbnails.EXTERNAL_CONTENT_URI;
	private static final String[] THUMB_PROJ     = {Thumbnails.IMAGE_ID,Thumbnails.DATA,Thumbnails.KIND};
	private static final String	  THUMB_WHERE    = Thumbnails.WIDTH  + "=" + Constants.MICRO_DIM      + " AND " + // Micro
	                     						   Thumbnails.HEIGHT + "=" + Constants.MICRO_DIM      + " OR "  + 
	                     						   Thumbnails.WIDTH  + "=" + Constants.MINI_SHORT_DIM + " AND " + // Mini first case
	                     						   Thumbnails.HEIGHT + "=" + Constants.MINI_LONG_DIM  + " OR "  +
	                     						   Thumbnails.WIDTH  + "=" + Constants.MINI_LONG_DIM  + " AND " + // Mini second case
	                     						   Thumbnails.HEIGHT + "=" + Constants.MINI_SHORT_DIM;
	
	public static ArrayList loadPictures(ContentResolver cr) throws IOException {
		ArrayList pictures = new ArrayList();
		if (cr != null) {				
			final Cursor pictureCursor = cr.query(MEDIA_URI, IMG_PROJ, null, null, IMG_SORT_ORDER);
			if (pictureCursor == null) {
				throw new IOException();
			}
			else {
				if (pictureCursor.moveToFirst()) {
			    	final int idColumn   = pictureCursor.getColumnIndex(Media._ID);
			    	final int dataColumn = pictureCursor.getColumnIndex(Media.DATA);
			    	final int latColumn  = pictureCursor.getColumnIndex(Media.LATITUDE);
			    	final int longColumn = pictureCursor.getColumnIndex(Media.LONGITUDE);
			    	final int dateColumn = pictureCursor.getColumnIndex(Media.DATE_TAKEN);
			    	
			        do {
			        	final long imageId        = pictureCursor.getLong(idColumn);
			        	final String imagePath    = pictureCursor.getString(dataColumn);
			        	double latitude     	  = pictureCursor.getDouble(latColumn);
			        	double longitude          = pictureCursor.getDouble(longColumn);
			        	final String latitudeStr  = pictureCursor.getString(latColumn);
			        	final String longitudeStr = pictureCursor.getString(longColumn);
			        	long date		 		  = pictureCursor.getLong(dateColumn);
			        	
			        	ExifInterface exif = null;
			        	
			        	// 1. Retrieve GPS data
			        	boolean hasLatLong = true;
			        	if (latitude == Constants.NO_GEO_TAG && longitude == Constants.NO_GEO_TAG) {
			        		// Initialized but no GPS data
			        		hasLatLong = false;
			        	} else if (latitude == 1000.0 && longitude == 1000.0) {
			        		hasLatLong = false;
			        		Uri imageUri = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, Long.toString(imageId));
			        		ContentValues values = new ContentValues(2);
			        		values.put(Media.LATITUDE, Constants.NO_GEO_TAG);
			        		values.put(Media.LONGITUDE, Constants.NO_GEO_TAG);
			        		cr.update(imageUri, values, null, null);
			        	} else if (latitudeStr == null || longitudeStr == null) { 
			        		// Not initialized
			        		hasLatLong = false;
			        		try {
			        			File imageFile = new File(imagePath);
			        			if (imageFile != null) {
					        		exif = new ExifInterface(imageFile);
					        		if (exif.hasMetadata()) {
						        		float[] gpsLatLong = new float[2];
						        		hasLatLong = exif.getLatLong(gpsLatLong);
						        		latitude  = hasLatLong ? gpsLatLong[0] : Constants.NO_GEO_TAG;
						        		longitude = hasLatLong ? gpsLatLong[1] : Constants.NO_GEO_TAG;
					        		} else {
					        			latitude  = Constants.NO_GEO_TAG;
					        			longitude = Constants.NO_GEO_TAG;
					        		}
				        			if (cr != null) {
				        				Uri imageUri = Uri.withAppendedPath( Media.EXTERNAL_CONTENT_URI, 
				        						                             Long.toString(imageId) );
				        				ContentValues values = new ContentValues(2);
				        				values.put(Media.LATITUDE, latitude);
				        				values.put(Media.LONGITUDE, longitude);
				        				cr.update(imageUri, values, null, null);
					        		}
			        			}
			        		} catch (IOException ioe) {
			        			hasLatLong = false;
			        		}
			        	}
			        	
			        	// 2. Retrieve date
			        	if (hasLatLong && date <= 0 && date != Constants.NO_DATE) {
			        		// Not initialized
			        		try {
				        		if (exif == null) {
				        			final File imageFile = new File(imagePath);
				        			if (imageFile != null) {
						        		exif = new ExifInterface(imageFile);
				        			}
				        		}
				        		if (exif.hasMetadata()) {
				        			date = exif.getDate();
				        		} else {
				        			date = Constants.NO_DATE;
				        		}
				        		if (cr != null) {
			        				Uri imageUri = Uri.withAppendedPath( Media.EXTERNAL_CONTENT_URI, 
			        						                             Long.toString(imageId) );
			        				ContentValues values = new ContentValues(1);
			        				values.put(Media.DATE_TAKEN, date);
			        				cr.update(imageUri, values, null, null);
				        		}
			        		} catch (IOException ioe) {
			        			date = Constants.NO_DATE;
			        		}
			        	}
			        	
			        	// 3. Create picture object
			        	if (hasLatLong) {
			        		final Picture item = new Picture(imageId, false, latitude, longitude, date);
			        		if (new File(imagePath).exists()) {
			        			item.setFullPath(imagePath);
			        		}
			        		item.setGroupId(Constants.PHONE_GROUP_ID);
			        		pictures.add(item);
			        	}
			        } while (pictureCursor != null && pictureCursor.moveToNext());
				}
				if (pictureCursor != null) {
					pictureCursor.close();
				}
				
				initThumbnails(cr, pictures);
			}
		}
		return pictures;
	}
	
	private static void initThumbnails(ContentResolver cr, ArrayList pictures) throws IOException {		
    	final Hashtable<Long, String> miniThumbPathTable  = new Hashtable<Long, String>();
    	final Hashtable<Long, String> microThumbPathTable = new Hashtable<Long, String>();
    	
    	final Cursor thumbCursor = cr.query(THUMB_URI, THUMB_PROJ, THUMB_WHERE, null, null);
    	if (thumbCursor == null) {
    		throw new IOException();
    	} else {
    		if (thumbCursor.moveToFirst()) {
	    		int imageIdColumn = thumbCursor.getColumnIndex(Thumbnails.IMAGE_ID);
	    		int dataColumn    = thumbCursor.getColumnIndex(Thumbnails.DATA);
	    		int kindColumn    = thumbCursor.getColumnIndex(Thumbnails.KIND);
	    		do {
					if (thumbCursor.getInt(kindColumn) == Thumbnails.MICRO_KIND) {
						microThumbPathTable.put( thumbCursor.getLong(imageIdColumn), 
								                 thumbCursor.getString(dataColumn) );
					} else {
						miniThumbPathTable.put( thumbCursor.getLong(imageIdColumn), 
				                                thumbCursor.getString(dataColumn) );
					}
	    		} while (thumbCursor.moveToNext());
    		}
    		thumbCursor.close();
    		
    		// Iterate pictures and set thumbnail paths
    		for (int i = 0; i < pictures.size(); ++i) {
    			final Picture item = (Picture) pictures.get(i);
    			final long imageId = item.getId();
    			if (imageId != -1) {
    				final String microPath = microThumbPathTable.get(imageId);
    				final String miniPath = miniThumbPathTable.get(imageId);
    				if (microPath != null && new File(microPath).exists()) {
    					item.setMicroPath(microPath);
    				}
    				if (miniPath != null && new File(miniPath).exists()) {
    					item.setMiniPath(miniPath);
    				}
    			}
    		}
    	}
    }
}
