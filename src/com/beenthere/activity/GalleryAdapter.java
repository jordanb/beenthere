package com.beenthere.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.beenthere.provider.Picture;
import com.beenthere.provider.Viewable;
import com.beenthere.util.Constants;
import com.beenthere.util.ThumbManager;
import com.beenthere.util.UIUtils;

public class GalleryAdapter extends ArrayAdapter<Viewable> {
	
    private final LayoutInflater mLayoutInflater;
    private final ThumbManager   mThumbManager;
    private int mImageDipSize;
    private float mScale;
   	   
    public GalleryAdapter(Activity activity, ThumbManager thumbManager) {
    	super(activity, 0);
    	mLayoutInflater = LayoutInflater.from(activity);
    	mThumbManager = thumbManager;
    	mScale = activity.getResources().getDisplayMetrics().density;
    	mImageDipSize = Constants.GALLERY_IMG_SIZE_DEFAULT;
    }
    
    public void setImageDipSize(int dip) {
    	mImageDipSize = dip;
    }
    
    public int getImageDipSize() {
    	return mImageDipSize;
    }
    
    public int getPosition(long itemId) {
    	final int size = getCount();
    	for (int i = 0; i < size; ++i) {
    		final Viewable viewable = getItem(i);
    		if (viewable.getId() == itemId) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	final Viewable item = getItem(position);
    	
    	final int imagePixelSize = UIUtils.toPixels(mScale, mImageDipSize);
    	convertView = (ImageView) mLayoutInflater.inflate(item.getLayout(), parent, false);
    	convertView.setLayoutParams(new Gallery.LayoutParams(imagePixelSize, imagePixelSize));
    	convertView.setTag(item);
    	
    	final Picture picture = item.getImage();
    	if (picture != null) {
    		final Bitmap bitmap = mThumbManager.getMicroBitmap(picture, false);
    		if (ThumbManager.bitmapExists(bitmap)) {
    			((ImageView) convertView).setImageBitmap(bitmap);
    		}
    	}
    	
		return convertView;
    }
}


