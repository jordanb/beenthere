package com.beenthere.kml;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * java.util.Date doesn't support the range of dates required, eg for
 * paleontological ranges, 4 billion BC, so I'm adding this class. It will be
 * very bare to start, and I'll add functionality as required v0.01 Just used a
 * String and no parsing v0.02 Supports integers too, but can now be compared to
 * each other
 * 
 * @author power
 * @version 0.02
 */
public class LongDate {
	/**
	 * A special value for LongDate which corresponds to all time.
	 */
	public static final LongDate FOR_ALL_TIME = new LongDate();
	
	private final static String DATE_TIME 	= "yyyy-MM-dd'T'HH:mm:ss'Z'";

	private BigInteger year;
	private String dateStr;
	private Date mDate;

	// //////////////////// Constructors ///////////////////////////////

	/**
	 * Special constructor used to create LongDate denoting For All Time Cannot
	 * be called outside class
	 */
	private LongDate() {
		this.year = null;
	}

	/**
	 * Ordinary constructor
	 * @param dateStr
	 */
	public LongDate(String dateStr) {
		// TODO parse properly
		try {
			this.year = new BigInteger(dateStr);
		} catch (RuntimeException e) {
			this.dateStr = dateStr; // just store string
		}
		try {
			final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME);
			mDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			mDate = null;
		}
	}
	
	public LongDate(Date date) {
		this.year = null;
		setDate(date);
	}

	// //////////////////// Public methods ///////////////////////////////
	
	public Date getDate() {
		return mDate;
	}
	
	public void setDate(Date date) {
		mDate = date;
		dateStr = new SimpleDateFormat(DATE_TIME).format(date);
	}
	
	public boolean equals(LongDate other) {
		if (this.year == null && other.year == null) {
			return true;
		} else if (this.year == null) {
			return false;
		} else if (this.year.compareTo(other.year) == 0) {
			return true;
		}
		return false;
	}

	public boolean before(LongDate other) {
		if (this.year != null && this.year.compareTo(other.year) == -1) {
			return true;
		}
		return false;
	}

	public boolean after(LongDate other) {
		if (this.year != null && this.year.compareTo(other.year) == 1) {
			return true;
		}
		return false;
	}

	public BigInteger subtract(LongDate other) {
		if (this.year != null && other.year != null) {
			return this.year.subtract(other.year);
		}
		return null;
	}

	public String toString() {
		if (this.dateStr != null) {
			return dateStr;
		}

		if (this.year == null) {
			return "FOR_ALL_TIME";
		}

		return this.year.toString();
	}

	// //////////////////// Getters and setters ///////////////////////////////
	// //////////////////// Private methods ///////////////////////////////
}
