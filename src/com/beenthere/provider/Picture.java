package com.beenthere.provider;

import android.os.Parcel;
import android.os.Parcelable;

import com.beenthere.util.Constants;
import com.beenthere.util.FileUtils;
import com.beenthere.util.GPSUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.picmap.ui.R;

public class Picture extends Viewable implements Parcelable {
	
	private long			mGroupId = Constants.PHONE_GROUP_ID;
	private OverlayItem 	mOverlay;
	private String 			mFullPath;
	private String 			mMiniPath;
	private String			mMicroPath;
	private boolean			mIsLocal;
	private long			mDateInMs = Constants.NO_DATE;
	private boolean			mToDelete = false;
	private String			mDescription = "";
	
	public Picture(long id, boolean isLocal, double latitude, double longitude, long dateInMs) {
		super(id);
		mIsLocal = isLocal;
		mDateInMs = dateInMs;
		int latitudeMicro  = GPSUtils.toMicroDegree(latitude);
		int longitudeMicro = GPSUtils.toMicroDegree(longitude);
		mOverlay = new OverlayItem(new GeoPoint(latitudeMicro, longitudeMicro), "", "");
	}
		
	public Picture newInstance() {
		Picture newItem = new Picture( getId(),
									   false,
									   getLatitude(),
									   getLongitude(),
									   getDate() );
		newItem.setDescription(mDescription);
		newItem.setFullPath(getFullPath());
		newItem.setMiniPath(getMiniPath());
		newItem.setMicroPath(getMicroPath());
		return newItem;
	}
	
	public void delete() {
		if (mIsLocal) {
			FileUtils.delete(mFullPath);
			FileUtils.delete(mMiniPath);
			FileUtils.delete(mMicroPath);
		}
	}
	
	public void setGroupId(long groupId) {
		mGroupId = groupId;
	}
	
	public long getGroupId() {
		return mGroupId;
	}
	
	public void setLocal(boolean local) {
		mIsLocal = local;
	}
	
	public boolean isLocal() {
		return mIsLocal;
	}
		
	public void setFullPath(String full) {
		mFullPath = full;
	}
	
	public void setMiniPath(String mini) {
		mMiniPath = mini;
	}
	
	public void setMicroPath(String micro) {
		mMicroPath = micro;
	}
	
	public String getFullPath() {
		return mFullPath;
	}
	
	public String getMiniPath() {
		return mMiniPath;
	}
	
	public String getMicroPath() {
		return mMicroPath;
	}
	
	public void switchDelete() {
		mToDelete = !mToDelete;
	}
	
	public void setDelete(boolean delete) {
		mToDelete = delete;
	}
	
	public boolean getDelete() {
		return mToDelete;
	}
	
	public long getDate() {
		return mDateInMs;
	}
	
	public void setDescription(String description) {
		mDescription = description;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public void setOverlay(OverlayItem overlay) {
		mOverlay = overlay;
	}
	
	public OverlayItem getOverlay() {
		return mOverlay;
	}
	
	public double getLatitude() {
		return GPSUtils.toDegree(mOverlay.getPoint().getLatitudeE6());
	}
	
	public double getLongitude() {
		return GPSUtils.toDegree(mOverlay.getPoint().getLongitudeE6());
	}
	
	@Override
	public Picture getImage() {
		return this;
	}

	@Override
	public String getText() {
		return mDescription;
	}
	
	@Override
	public int getLayout() {
		return R.layout.gallery_picture_thumb;
	}
	
	/**
	 * Picture is parcelable in order to bind data to the
	 * different intents.
	 */
	public Picture(Parcel in) {
		super(in.readLong());
		mGroupId 		= in.readLong();
		mFullPath 		= in.readString();
		mMiniPath 		= in.readString();
		mMicroPath 		= in.readString();
		mIsLocal		= in.readInt() == 1;
		mDateInMs		= in.readLong();
		mToDelete 		= in.readInt() == 1;
		mDescription 	= in.readString();
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeLong  (getId()); 
		out.writeLong  (mGroupId);
		out.writeString(mFullPath);
		out.writeString(mMiniPath);
		out.writeString(mMicroPath);
		out.writeInt   (mIsLocal ? 1 : 0);
		out.writeLong  (mDateInMs);
		out.writeInt   (mToDelete ? 1 : 0);
		out.writeString(mDescription);
	}
	
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Picture createFromParcel(Parcel in) {
			return new Picture(in);
		}

		public Picture[] newArray(int size) {
			return new Picture[size];
		}
	};
}
