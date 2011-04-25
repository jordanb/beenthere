package com.beenthere.provider;

public abstract class Viewable {
	
	private long mId;
	
	public Viewable(long id) {
		mId = id;
	}
	
	public void setId(long id) {
		mId = id;
	}
	
	public long getId() {
		return mId;
	}
	
	public abstract int getLayout();
	
	public abstract Picture getImage();
	
	public abstract String getText();

}
