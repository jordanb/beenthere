package com.beenthere.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

public class ExifInterface {
	
	private final static String DATE_FORMAT = "yyyy:MM:dd HH:mm:ss";
	
	private JpegImageMetadata mJpegMetadata = null;
	private boolean mHasMetadata;
	
	public ExifInterface(File file) throws IOException {
		mHasMetadata = false;
		if (file != null && file.canRead()) {
			try {
				IImageMetadata metadata = Sanselan.getMetadata(file);
	            if (metadata instanceof JpegImageMetadata) {
		        	mJpegMetadata = (JpegImageMetadata) metadata;
		        	mHasMetadata = true;
		        }
			}
			catch (ImageReadException ire) {
			}
			catch (Exception e) {
			}
		}
		else {
			throw new IOException("File is null");
		}
	}
	
	public boolean hasMetadata() {
		return mHasMetadata;
	}
	
	public long getDate() {
		long dateInMs = getDate(TiffConstants.TIFF_TAG_DATE_TIME);
		if (dateInMs == Constants.NO_DATE) {
			dateInMs = getDate(TiffConstants.EXIF_TAG_CREATE_DATE);
		}
		if (dateInMs == Constants.NO_DATE) {
			dateInMs = getDate(TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
		}
		return dateInMs;
	}
	
	public boolean getLatLong(float[] output) {
		boolean found = false;
		if (mJpegMetadata != null) {
			TiffImageMetadata exifMetadata = mJpegMetadata.getExif();
	        if (exifMetadata != null) {
	        	try {
	        		TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
	                if (gpsInfo != null) {
	                	output[0] = (float) gpsInfo.getLatitudeAsDegreesNorth();
	                	output[1] = (float) gpsInfo.getLongitudeAsDegreesEast();
	                	found = true;
	                }
	        	}
	        	catch (ImageReadException ire) {
	        		found = false;
	        	}
	        }
		}
		return found;
	}
	
	private long getDate(TagInfo tag) {
		long dateInMs = Constants.NO_DATE;
		final JpegImageMetadata metadata = mJpegMetadata;
		if (metadata != null) {
			TiffField dateField = metadata.findEXIFValue(tag);
			if (dateField != null) {
				try {
					String dateStr = dateField.getValueDescription();
					dateStr = dateStr.replaceAll("'", "");
					final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
					final Date date = dateFormat.parse(dateStr);
					dateInMs = date.getTime();
				} catch (ParseException e) {
					dateInMs = Constants.NO_DATE;
				}
			} else {
				dateInMs = Constants.NO_DATE;
			}
		}
		return dateInMs;
	}
}
