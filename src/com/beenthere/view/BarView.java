package com.beenthere.view;

import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beenthere.util.UIUtils;
import com.picmap.ui.R;

public class BarView extends LinearLayout {
	
	private TextView mTitle;
	private TextView mDate;
	private Locale mLocale;

	public BarView(Context context) {
		super(context);
		init(context);
	}
  	
	public BarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bar_view, this);
		init(context);
	}
	
	private void init(Context context) {
		mTitle = (TextView) findViewById(R.id.title);
		mDate = (TextView) findViewById(R.id.date);
		mLocale = getResources().getConfiguration().locale;
	}
	
	public void setTitle(String title) {
		if (mTitle != null) {
			mTitle.setText(title);
		}
	}
	
	public void setInfo(CharSequence info) {
		if (mDate != null) {
			mDate.setText(info);
		}
	}
	
	public void setDate(long dateMillis) {
		if (mDate != null) {
			mDate.setText(UIUtils.getLocaleDate(mLocale, dateMillis));
		}
	}
}
