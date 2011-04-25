package com.beenthere.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SafeImage extends ImageView {

	public SafeImage(Context context) {
		super(context);
	}
  	
	public SafeImage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
 	
	public SafeImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		try {
			super.onDraw(canvas);
		} catch (Exception e) {
			// Nothing to do
		}
	}

}
