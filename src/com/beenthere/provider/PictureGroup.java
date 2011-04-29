package com.beenthere.provider;

import java.util.ArrayList;

import com.picmap.ui.R;

public class PictureGroup extends Viewable {

	private ArrayList 	mPictures;
	private String		mName = "";
	
	public PictureGroup(long id, String name, ArrayList pictures) {
		super(id);
		mName = name;
		mPictures = pictures;
	}
	
	public ArrayList getPictures() {
		return mPictures;
	}
	
	public int size() {
		final ArrayList pictures = mPictures;
		if (pictures != null) {
			return mPictures.size();
		}
		return 0;
	}
	
	public void removePicture(Picture picture) {
		mPictures.remove(picture);
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getName() {
		return mName;
	}
	
	@Override
	public Picture getImage() {
		final ArrayList pictures = mPictures;
		if (pictures.size() > 0) {
			return (Picture) pictures.get(0);
		}
		return null;
	}
	
	@Override
	public String getText() {
		return mName;
	}

	@Override
	public int getLayout() {
		return R.layout.gallery_picture_thumb;
	}
}
