package com.beenthere.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.beenthere.util.GPSUtils;
import com.beenthere.util.GPSUtils.Area;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class PictureBox {
	
	final private static String SELECTED_GROUP_ID		= "selected_group_id";
	// TODO final private static String GROUP_MODE				= "group_mode";
	final private static String HIDDEN_GROUP_IDS		= "hidden_group_ids";
	final private static String ID_SEPARATOR			= ",";
	
	final private static long   ALL_GROUPS				= -7;
	
	final private ArrayList 	mGroups					= new ArrayList();
	final private ArrayList 	mVisiblePictures    	= new ArrayList();
	final private ArrayList 	mScreenPictures			= new ArrayList();
	final private ArrayList 	mViewables				= new ArrayList();
	final private ArrayList 	mHiddenGroupIds			= new ArrayList();
	final private Comparator 	mPictureComparator 		= new PictureComparator();
	final private Comparator 	mGroupComparator 		= new GroupComparator();
	
	private boolean				mGroupsSortChanged		= false;
	private boolean 			mVisibleChanged 		= false;
	private boolean 			mScreenChanged			= false;
	private boolean 			mViewableChanged		= false;
	private long   				mSelectedGroupId		= ALL_GROUPS;
	private boolean				mGroupMode				= false;
	private Area				mMapArea;	
	
	public PictureBox(SharedPreferences settings) {
		if (settings != null) {
			mSelectedGroupId = settings.getLong(SELECTED_GROUP_ID, ALL_GROUPS);
			// TODO mGroupMode = settings.getBoolean(GROUP_MODE, false);
			final ArrayList<Long> hiddenGroupIds = mHiddenGroupIds;
			final String idsStr = settings.getString(HIDDEN_GROUP_IDS, "");
			final String[] idsArray = idsStr.split(ID_SEPARATOR);
			try {
				for (int i = 0; i < idsArray.length; ++i) {
					long id = Long.valueOf(idsArray[i]);
					hiddenGroupIds.add(id);
				}
			} catch (NumberFormatException nfe) {
			}
		}
	}
	
	public void savePreferences(Editor editor) {
		editor.putLong(SELECTED_GROUP_ID, mSelectedGroupId);
		// TODO editor.putBoolean(GROUP_MODE, mGroupMode);
		String idsStr = "";
		final ArrayList<Long> hiddenGroupIds = mHiddenGroupIds;
		final int size = hiddenGroupIds.size();
		for (int i = 0; i < size; ++i) {
			if (i != 0) {
				idsStr += ID_SEPARATOR;
			}
			idsStr += hiddenGroupIds.get(i);
		}
		editor.putString(HIDDEN_GROUP_IDS, idsStr);
	}
	
	public boolean isLoaded() {
		return mGroups.size() > 0;
	}
	
	public void setGroupMode(boolean groupMode) {
		if (groupMode != mGroupMode) {
			mGroupMode = groupMode;
			mViewableChanged = true;
			selectAllGroups();
		}
	}
	
	public boolean isGroupMode() {
		return mGroupMode;
	}
	
	public void setSelectedGroupId(long groupId) {
		if (mGroupMode && groupId != mSelectedGroupId) {
			mSelectedGroupId = groupId;
			mVisibleChanged = true;
		}
	}
	
	public void selectAllGroups() {
		setSelectedGroupId(ALL_GROUPS);
	}
	
	public long getSelectedGroupId() {
		return mSelectedGroupId;
	}
	
	public boolean isAllGroups() {
		return mSelectedGroupId == ALL_GROUPS;
	}
	
	public void addGroup(PictureGroup group) {
		final PictureGroup groupToRemove = getGroup(group.getId());
		if (groupToRemove != null) {
			mGroups.remove(groupToRemove);
		}
		mGroups.add(group);
		mGroupsSortChanged = true;
		if (!mHiddenGroupIds.contains(group.getId())) {	
			mVisibleChanged = true;
		}
	}
	
	public void addGroups(ArrayList groups) {
		final int size = groups.size();
		for (int i = 0; i < size; ++i) {
			addGroup((PictureGroup) groups.get(i));
		}
	}
	
	public void removeGroup(PictureGroup group) {
		mGroups.remove(group);
		if (!mHiddenGroupIds.contains(group.getId())) {
			mVisibleChanged = true;
		}
	}
	
	public void showGroup(PictureGroup group) {
		final ArrayList hiddenGroups = mHiddenGroupIds;
		final long groupId = group.getId();
		if (hiddenGroups.contains(groupId)) {
			hiddenGroups.remove(groupId);
			mVisibleChanged = true;
		}
	}
	
	public void hideGroup(PictureGroup group) {
		final ArrayList hiddenGroups = mHiddenGroupIds;
		final long groupId = group.getId();
		if (!hiddenGroups.contains(groupId)) {
			hiddenGroups.add(groupId);
			mVisibleChanged = true;
		}
	}
	
	public boolean isGroupEnabled(PictureGroup group) {
		return !mHiddenGroupIds.contains(group.getId());
	}
	
	public PictureGroup getGroup(long id) {
		final ArrayList groups = mGroups;
		final int size = groups.size();
		for (int i = 0; i < size; ++i) {
			final PictureGroup group = (PictureGroup) groups.get(i);
			if (group.getId() == id) {
				return group;
			}
		}
		return null;
	}
	
	public ArrayList getGroups() {
		if (mGroupsSortChanged) {
			refreshGroupsSort();
		}
		return mGroups;
	}
	
	public boolean hasVisibleChanged() {
		return mVisibleChanged;
	}
	
	public void setVisibleChanged() {
		mVisibleChanged = true;
	}

	public ArrayList getVisiblePictures() {
		if (mVisibleChanged) {
			refreshVisisblePictures();
		}
		return mVisiblePictures;
	}
	
	public ArrayList getScreenPictures() {
		if (mVisibleChanged) {
			refreshVisisblePictures();
		}
		if (mScreenChanged) {
			refreshScreenPictures();
		}
		return mScreenPictures;
	}
	
	public ArrayList getScreenViewables() {
		if (mVisibleChanged) {
			refreshVisisblePictures();
		}
		if (mScreenChanged) {
			refreshScreenPictures();
		}
		if (mViewableChanged) {
			refreshViewables();
		}
		return mViewables;
	}
	
	public void setMapInfo(GeoPoint center, int latitudeSpan, int longitudeSpan) {
		mMapArea = GPSUtils.toArea(center, latitudeSpan, longitudeSpan);
		mScreenChanged = true;
	}
	
	private void refreshGroupsSort() {
		Collections.sort(mGroups, mGroupComparator);
		mGroupsSortChanged = false;
	}
	
	private void refreshVisisblePictures() {
		final ArrayList visiblePictures = mVisiblePictures;
		visiblePictures.clear();
		boolean shouldSort = false;
		final long selectedGroupId = mSelectedGroupId;
		if (selectedGroupId != ALL_GROUPS) {
			final PictureGroup selectedGroup = getGroup(selectedGroupId);
			if (selectedGroup != null) {
				visiblePictures.addAll(selectedGroup.getPictures());
				shouldSort = true;
			}
		} else {
			final ArrayList groups = mGroups;
			final int size = groups.size();
			for (int i = 0; i < size; ++i) {
				final PictureGroup group = (PictureGroup) groups.get(i);
				if (!mHiddenGroupIds.contains(group.getId())) {
					shouldSort = true;
					visiblePictures.addAll(group.getPictures());
				}
			}
		}
		if (shouldSort) {
			Collections.sort(visiblePictures, mPictureComparator);
		}
		mVisibleChanged = false;
		mScreenChanged = true;
	}
	
	private void refreshScreenPictures() {
		final ArrayList screenPictures = mScreenPictures;
		screenPictures.clear();
		final ArrayList visiblePictures = mVisiblePictures;
		final Area mapArea = mMapArea;
		if (mapArea == null) {
			screenPictures.addAll(mVisiblePictures);
		} else {
			final int size = visiblePictures.size();
			for (int i = 0; i < size; ++i) {
				final Picture picture = (Picture) visiblePictures.get(i);
				// Is picture on current map?
				final GeoPoint geoPoint = picture.getOverlay().getPoint();
				final int latitude  = geoPoint.getLatitudeE6();
				final int longitude = geoPoint.getLongitudeE6();
				if ( latitude  <= mapArea.latitudeMax  && 
					 latitude  >= mapArea.latitudeMin  && 
					 longitude <= mapArea.longitudeMax && 
					 longitude >= mapArea.longitudeMin ) {
					screenPictures.add(picture);
				}
			}
		}
		mScreenChanged = false;
		mViewableChanged = true;
	}
	
	private void refreshViewables() {
		final ArrayList viewables = mViewables;
		viewables.clear();
		final ArrayList<Picture> screenPictures = mScreenPictures;
		if (mGroupMode && mSelectedGroupId == ALL_GROUPS) {
			final int size = screenPictures.size();
			for (int i = 0; i < size; ++i) {
				final PictureGroup group = getGroup(screenPictures.get(i).getGroupId());
				if (group != null && !viewables.contains(group)) {
					viewables.add(group);
				}
			}
		} else {
			viewables.addAll(screenPictures);
		}
		mViewableChanged = false;
	}

	public Picture getPicture(OverlayItem overlay) {
		if (overlay != null) {
			final ArrayList filteredViewables = mScreenPictures;
			final int size = filteredViewables.size();
			for (int i = 0; i < size; ++i) {
				final Picture picture = (Picture) filteredViewables.get(i);
				if (overlay == picture.getOverlay()) {
					return picture;
				}
			}
		}
		return null;
	}
	
	/**
	 * Group comparator used by the sort
	 * @author Jordan
	 *
	 */
	private static class GroupComparator implements Comparator {

		public int compare(Object group1, Object group2) {
			int compare = 0;
			if (group1 instanceof PictureGroup && group2 instanceof PictureGroup) {
				final long groupId1 = ((PictureGroup) group1).getId();
				final long groupId2 = ((PictureGroup) group2).getId();
				if (groupId1 < groupId2) {
					compare = -1;
				} else {
					compare = 1;
				}
			}
			return compare;
		}
	}
	
	/**
	 * Picture comparator used by the sort
	 * @author Jordan
	 *
	 */
	private static class PictureComparator implements Comparator {

		public int compare(Object pic1, Object pic2) {
			int compare = 0;
			if (pic1 instanceof Picture && pic2 instanceof Picture) {
				final Picture picture1 = (Picture) pic1;
				final Picture picture2 = (Picture) pic2;
				final long date1 = picture1.getDate();
				final long date2 = picture2.getDate();
				if (date1 < date2) {
					compare = -1;
				} else if (date1 > date2) {
					compare = 1;
				} else {
					// Dates are the same
					final long album1 = picture1.getGroupId();
					final long album2 = picture1.getGroupId();
					if (album1 < album2) {
						compare = -1;
					} else if (album1 > album2) {
						compare = 1;
					}
				}
			}
			return compare;
		}
	}
}
