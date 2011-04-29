package com.beenthere.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.picmap.ui.R;

public class NoteView extends LinearLayout {
	
	private BarView mBar;
	private TextView mNote;

	public NoteView(Context context) {
		super(context);
		init(context);
	}
  	
	public NoteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.note_view, this);
		init(context);
	}
	
	private void init(Context context) {
		mBar = (BarView) findViewById(R.id.bar);
		mNote = (TextView) findViewById(R.id.note);
		mNote.setVisibility(View.GONE);
	}
	
	public BarView getBar() {
		return mBar;
	}
	
	public void setNote(String note) {
		if (mNote != null) {
			mNote.setText(note);
		}
	}
	
	public boolean isOpened() {
		return mNote.getVisibility() == View.VISIBLE;
	}
	
	public void open() {
		mNote.setVisibility(View.VISIBLE);
		startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.note_slide_in));
	}
	
	public void close() {
		mNote.setVisibility(View.GONE);
		//startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.note_slide_out));
	}
}
