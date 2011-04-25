package com.beenthere.util;

import com.google.android.maps.GeoPoint;

public class GPSUtils {

	static public Area toArea(GeoPoint center, int latitudeSpan, int longitudeSpan) {
		Area area = new Area();
		area.latitudeMax  = center.getLatitudeE6()  + (latitudeSpan  / 2);
		area.latitudeMin  = center.getLatitudeE6()  - (latitudeSpan  / 2);
		area.longitudeMax = center.getLongitudeE6() + (longitudeSpan / 2);
		area.longitudeMin = center.getLongitudeE6() - (longitudeSpan / 2);
		return area;
	}
	
	static public int toMicroDegree(double degree) {
		return (int) (degree * 1E6);
	}
	
	static public double toDegree(int microDegree) {
		return ((double)microDegree) / 1E6;
	}
	
	static public class Area {
		public int latitudeMax; 
		public int latitudeMin;
		public int longitudeMax;
		public int longitudeMin;		
	}
}
