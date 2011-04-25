package com.beenthere.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.beenthere.R;
import com.beenthere.provider.PictureBox;

public class UIUtils {
	
	static public int toPixels(float scale, int dip) {
		return (int) ((float) dip * scale + 0.5f);
	}
	
	static public int toDip(float scale, float pixels) {
		return (int) (pixels / scale + 0.5f);
	}
	
	static public int toPixels(Context context, int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return toPixels(scale, dip);
	}
	
	static public int toDip(Context context, float pixels) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return toDip(scale, pixels);
	}
	
	static public String getLocaleDate(Locale locale, long dateMillis) {
		if (dateMillis > 0 && dateMillis != Constants.NO_DATE) {
			Locale lLocale = null;
			if (locale.getLanguage().equals(Locale.FRENCH.getLanguage())) {
				lLocale = Locale.FRENCH;
			} else {
				lLocale = Locale.ENGLISH;
			}
			final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, lLocale);
			return dateFormat.format(new Date(dateMillis));
		}
		return "";
	}
	
	static public void showNoPicturesVisibleWarning(Context context, PictureBox pictureBox) {
		CharSequence warningText = "";
		if (pictureBox.getGroups().isEmpty()) {
			warningText = context.getResources().getText(R.string.no_pictures_alert);
		} else {
			warningText = context.getResources().getText(R.string.no_visible_albums);
		}
		Toast.makeText(context, warningText, Toast.LENGTH_LONG).show();
	}
	
	static public boolean showAccessWarning(Context context) {
		return showAccessWarning(context, null);
	}

	static public boolean showAccessWarning(Context context, Toast toast) {
		String extStorageState = Environment.getExternalStorageState();
		if (extStorageState.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		} 
		CharSequence warningText = "";
		if (extStorageState.equals(Environment.MEDIA_BAD_REMOVAL)) {
			warningText = context.getResources().getText(R.string.media_bad_removal);
		} else if (extStorageState.equals(Environment.MEDIA_REMOVED)) {
			warningText = context.getResources().getText(R.string.media_removed);	
		} else if (extStorageState .equals(Environment.MEDIA_CHECKING)) {
			warningText = context.getResources().getText(R.string.media_checking);
		} else if (extStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			warningText = context.getResources().getText(R.string.media_mounted_read_only);
		} else if (extStorageState.equals(Environment.MEDIA_NOFS)) {
			warningText = context.getResources().getText(R.string.media_nofs);
		} else if (extStorageState.equals(Environment.MEDIA_SHARED)) {
			warningText = context.getResources().getText(R.string.media_shared);
		} else if (extStorageState.equals(Environment.MEDIA_UNMOUNTED)) {
			warningText = context.getResources().getText(R.string.media_unmounted);
		} else if (extStorageState.equals(Environment.MEDIA_UNMOUNTABLE)) {
        	warningText = context.getResources().getText(R.string.media_unmountable);
		} else {
        	warningText = context.getResources().getText(R.string.media_other);
        }
		if (toast == null) {
			Toast.makeText(context, warningText, Toast.LENGTH_LONG).show();
		} else {
			toast.setText(warningText);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.show();
		}
		return true;
	}
}
