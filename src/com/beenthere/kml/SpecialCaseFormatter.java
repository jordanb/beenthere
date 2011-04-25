package com.beenthere.kml;

public class SpecialCaseFormatter {
	
	// public static String toKMLString(java.awt.Color color)
	// {
	// return Integer.toHexString(color.getRGB());
	// }

	public static String toKMLString(java.util.Date date) {
		return date.toString();
	}

	// Treating String as a special case allows
	// me to reduce error checking code
	// It is a special case in that it's a built in
	// but non-primitive
	public static String toKMLString(String str) {
		return str;
	}

	public static String toKMLString(LongDate aDate) {
		return aDate.toString();
	}
}
